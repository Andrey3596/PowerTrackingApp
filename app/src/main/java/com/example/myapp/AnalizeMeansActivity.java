package com.example.myapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.myapp.database.DatabaseHelper;
import com.example.myapp.model.ConsumedProduct;
import com.example.myapp.model.Goal;
import com.example.myapp.model.Meal;
import com.example.myapp.model.User;
import com.example.myapp.service.CalorieCalculator;
import com.example.myapp.service.MealAnalyzer;
import com.example.myapp.service.MealFilter;
import com.example.myapp.service.StringFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AnalizeMeansActivity extends BaseActivity {

    private DatabaseHelper dbHelper;
    private User user;
    private CalorieCalculator calculator;
    private List<Goal> allGoals;
    private List<Meal> allMeals;
    private List<Meal> filteredMeals;
    private ArrayAdapter<Meal> adapter;
    private ListView listView;
    private TextView textViewSummary;

    private RadioGroup radioGroupFilter;
    private LinearLayout layoutFilterParams;
    private LinearLayout layoutDay, layoutMonth, layoutYear, layoutInterval;
    private EditText editDay, editMonth, editYear;
    private EditText editMonthOnly, editYearForMonth;
    private EditText editYearOnly;
    private EditText editStartDay, editStartMonth, editStartYear;
    private EditText editEndDay, editEndMonth, editEndYear;
    private Button buttonApplyFilter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analize_means);
        applyBottomPaddingToNavPanel();

        initData();
        initViews();

        setupListView();

        setupRadioGroup();

        buttonApplyFilter.setOnClickListener(v -> applyFilter());
        loadData();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initData() {
        dbHelper = new DatabaseHelper(this);
        user = dbHelper.loadUser();
        if (user == null) {
            Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        calculator = new CalorieCalculator(user);
        allGoals = dbHelper.loadAllGoals();
        allMeals = dbHelper.loadAllMeals();
        // тут уже все выводятся вне зависимости от параметров
        filteredMeals = new ArrayList<>();
        //filteredMeals = new ArrayList<>(allMeals);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData() {
        allMeals = dbHelper.loadAllMeals();
        allGoals = dbHelper.loadAllGoals();
        filteredMeals.clear();
        filteredMeals.addAll(MealFilter.filterByDay(allMeals, LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue(), LocalDate.now().getYear()));
        //filteredMeals.addAll(allMeals);
        updateDisplay();
    }

    private void initViews() {
        listView = findViewById(R.id.listViewAnalysis);
        textViewSummary = findViewById(R.id.textViewSummary);
        radioGroupFilter = findViewById(R.id.radioGroupFilter);
        layoutFilterParams = findViewById(R.id.layoutFilterParams);
        layoutDay = findViewById(R.id.layoutDay);
        layoutMonth = findViewById(R.id.layoutMonth);
        layoutYear = findViewById(R.id.layoutYear);
        layoutInterval = findViewById(R.id.layoutInterval);
        editDay = findViewById(R.id.editDay);
        editMonth = findViewById(R.id.editMonth);
        editYear = findViewById(R.id.editYear);
        editMonthOnly = findViewById(R.id.editMonthOnly);
        editYearForMonth = findViewById(R.id.editYearForMonth);
        editYearOnly = findViewById(R.id.editYearOnly);
        editStartDay = findViewById(R.id.editStartDay);
        editStartMonth = findViewById(R.id.editStartMonth);
        editStartYear = findViewById(R.id.editStartYear);
        editEndDay = findViewById(R.id.editEndDay);
        editEndMonth = findViewById(R.id.editEndMonth);
        editEndYear = findViewById(R.id.editEndYear);
        buttonApplyFilter = findViewById(R.id.buttonApplyFilter);
    }

    private void setupListView() {
        adapter = new ArrayAdapter<Meal>(this, android.R.layout.simple_list_item_1, filteredMeals) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Meal meal = getItem(position);
                if (meal != null && view instanceof TextView) {
                    TextView tv = (TextView) view;
                    double sumCal = 0, sumProt = 0, sumFat = 0, sumCarb = 0;
                    for (ConsumedProduct cp : meal.getConsumedProducts()) {
                        sumCal += cp.getCalories();
                        sumProt += cp.getProtein();
                        sumFat += cp.getFat();
                        sumCarb += cp.getCarb();
                    }
                    String text = StringFormatter.formatMealForAnalysis(
                            meal.getTypeMeal(), meal.getDate(), sumCal, sumProt, sumFat, sumCarb
                    );
                    tv.setText(text);
                }
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    private void setupRadioGroup() {
        radioGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioAll) {
                layoutFilterParams.setVisibility(View.GONE);
                showAllMeals();
            } else {
                layoutFilterParams.setVisibility(View.VISIBLE);
                layoutDay.setVisibility(View.GONE);
                layoutMonth.setVisibility(View.GONE);
                layoutYear.setVisibility(View.GONE);
                layoutInterval.setVisibility(View.GONE);
                if (checkedId == R.id.radioDay) layoutDay.setVisibility(View.VISIBLE);
                else if (checkedId == R.id.radioMonth) layoutMonth.setVisibility(View.VISIBLE);
                else if (checkedId == R.id.radioYear) layoutYear.setVisibility(View.VISIBLE);
                else if (checkedId == R.id.radioInterval) layoutInterval.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showAllMeals() {
        filteredMeals.clear();
        filteredMeals.addAll(allMeals);
        updateDisplay();
        if (filteredMeals.isEmpty()) {
            Toast.makeText(this, "Нет приёмов пищи", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void applyFilter() {
        int checkedId = radioGroupFilter.getCheckedRadioButtonId();
        List<Meal> filtered = new ArrayList<>();

        if (checkedId == R.id.radioAll) {
            filtered.addAll(allMeals);
        } else if (checkedId == R.id.radioDay) {
            Integer[] date = getDateFromInputs(editDay, editMonth, editYear);
            if (date == null) return;
            filtered = MealFilter.filterByDay(allMeals, date[0], date[1], date[2]);
        } else if (checkedId == R.id.radioMonth) {
            Integer[] monthYear = getMonthYearFromInputs(editMonthOnly, editYearForMonth);
            if (monthYear == null) return;
            filtered = MealFilter.filterByMonth(allMeals, monthYear[0], monthYear[1]);
        } else if (checkedId == R.id.radioYear) {
            Integer year = getYearFromInput(editYearOnly);
            if (year == null) return;
            filtered = MealFilter.filterByYear(allMeals, year);
        } else if (checkedId == R.id.radioInterval) {
            LocalDate[] interval = getIntervalFromInputs(
                    editStartDay, editStartMonth, editStartYear,
                    editEndDay, editEndMonth, editEndYear
            );
            if (interval == null) return;
            filtered = MealFilter.filterByInterval(allMeals, interval[0], interval[1]);
        }

        filteredMeals.clear();
        filteredMeals.addAll(filtered);
        updateDisplay();

        if (filteredMeals.isEmpty()) {
            Toast.makeText(this, "Нет приёмов пищи за указанный период", Toast.LENGTH_SHORT).show();
        }
    }

    private Integer[] getDateFromInputs(EditText dayInput, EditText monthInput, EditText yearInput) {
        String dayStr = dayInput.getText().toString().trim();
        String monthStr = monthInput.getText().toString().trim();
        String yearStr = yearInput.getText().toString().trim();
        if (dayStr.isEmpty() || monthStr.isEmpty() || yearStr.isEmpty()) {
            Toast.makeText(this, "Заполните день, месяц и год", Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            return new Integer[]{
                    Integer.parseInt(dayStr),
                    Integer.parseInt(monthStr),
                    Integer.parseInt(yearStr)
            };
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Неверный формат даты", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private Integer[] getMonthYearFromInputs(EditText monthInput, EditText yearInput) {
        String monthStr = monthInput.getText().toString().trim();
        String yearStr = yearInput.getText().toString().trim();
        if (monthStr.isEmpty() || yearStr.isEmpty()) {
            Toast.makeText(this, "Заполните месяц и год", Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            return new Integer[]{
                    Integer.parseInt(monthStr),
                    Integer.parseInt(yearStr)
            };
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Некорректные числа", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private Integer getYearFromInput(EditText yearInput) {
        String yearStr = yearInput.getText().toString().trim();
        if (yearStr.isEmpty()) {
            Toast.makeText(this, "Введите год", Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            return Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Некорректный год", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDate[] getIntervalFromInputs(EditText startDay, EditText startMonth, EditText startYear,
                                              EditText endDay, EditText endMonth, EditText endYear) {
        String startDayStr = startDay.getText().toString().trim();
        String startMonthStr = startMonth.getText().toString().trim();
        String startYearStr = startYear.getText().toString().trim();
        String endDayStr = endDay.getText().toString().trim();
        String endMonthStr = endMonth.getText().toString().trim();
        String endYearStr = endYear.getText().toString().trim();

        if (startDayStr.isEmpty() || startMonthStr.isEmpty() || startYearStr.isEmpty() ||
                endDayStr.isEmpty() || endMonthStr.isEmpty() || endYearStr.isEmpty()) {
            Toast.makeText(this, "Заполните все поля интервала", Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            LocalDate start = LocalDate.of(
                    Integer.parseInt(startYearStr),
                    Integer.parseInt(startMonthStr),
                    Integer.parseInt(startDayStr)
            );
            LocalDate end = LocalDate.of(
                    Integer.parseInt(endYearStr),
                    Integer.parseInt(endMonthStr),
                    Integer.parseInt(endDayStr)
            );
            return new LocalDate[]{start, end};
        } catch (NumberFormatException | DateTimeParseException e) {
            Toast.makeText(this, "Неверный формат даты", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void updateDisplay() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        updateSummary();
        if (filteredMeals.isEmpty()) {
            Toast.makeText(this, "Нет приёмов пищи", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSummary() {
        if (filteredMeals.isEmpty()) {
            textViewSummary.setVisibility(View.GONE);
            return;
        }

        MealAnalyzer.AnalysisResult result = MealAnalyzer.calculateSummary(filteredMeals);
        int days = result.getDaysCount();
        double totalCal = result.getTotalCalories();
        double totalProt = result.getTotalProtein();
        double totalFat = result.getTotalFat();
        double totalCarb = result.getTotalCarb();

        StringBuilder summary = new StringBuilder();
        summary.append(StringFormatter.formatSummary(days, totalCal, totalProt, totalFat, totalCarb));

        // Рекомендуемая цель
        double targetCal = calculator.getBMR() * days;
        double targetProt = calculator.getAimProtein() * days;
        double targetFat = calculator.getAimFat() * days;
        double targetCarb = calculator.getAimCarb() * days;
        summary.append(StringFormatter.formatUserGoalComparison(
                "Рекомендуемая цель",
                totalCal, targetCal,
                totalProt, targetProt,
                totalFat, targetFat,
                totalCarb, targetCarb
        ));

        // Пользовательские цели
        for (Goal g : allGoals) {
            if (g.getName().equals("Рекомендуемая")) continue;
            Double tCal = g.hasCalories() ? g.getCalories() * days : null;
            Double tProt = g.hasProtein() ? g.getProtein() * days : null;
            Double tFat = g.hasFat() ? g.getFat() * days : null;
            Double tCarb = g.hasCarb() ? g.getCarb() * days : null;

            summary.append(StringFormatter.formatUserGoalComparison(
                    g.getName(), totalCal, tCal, totalProt, tProt, totalFat, tFat, totalCarb, tCarb
            ));
        }

        textViewSummary.setText(summary.toString());
        textViewSummary.setVisibility(View.VISIBLE);
    }
}