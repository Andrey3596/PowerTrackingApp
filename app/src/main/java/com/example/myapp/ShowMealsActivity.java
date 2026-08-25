package com.example.myapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.example.myapp.model.Meal;
import com.example.myapp.service.MealFilter;
import com.example.myapp.service.StringFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ShowMealsActivity extends BaseActivity {
    private int dayOffset = 0;
    private DatabaseHelper dbHelper;
    private List<Meal> allMeals;
    private List<Meal> filteredMeals;
    private ArrayAdapter<Meal> adapter;
    private ListView listView;

    private RadioGroup radioGroupFilter;
    private LinearLayout layoutFilterParams,layoutBackForward;
    private LinearLayout layoutDay, layoutMonth, layoutYear, layoutInterval;
    private EditText editDay, editMonth, editYear;
    private EditText editMonthOnly, editYearForMonth;
    private EditText editYearOnly;
    private EditText editStartDay, editStartMonth, editStartYear;
    private EditText editEndDay, editEndMonth, editEndYear;
    private Button buttonApplyFilter, buttonBack, buttonForward;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_meals);
        applyBottomPaddingToNavPanel();

        initData();
        initViews();

        setupListView();

        setupRadioGroup();

        buttonApplyFilter.setOnClickListener(v -> applyFilter());
        buttonBack.setOnClickListener(v -> back());
        buttonForward.setOnClickListener(v -> forward());
        loadData();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initData() {
        dbHelper = new DatabaseHelper(this);
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
    private void loadData() { // тут нужно сделать чтоб был сегоднишний день
        allMeals = dbHelper.loadAllMeals();
        filteredMeals.clear();
        filteredMeals.addAll(MealFilter.filterByDay(allMeals, LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue(), LocalDate.now().getYear()));
        // filteredMeals.addAll(allMeals);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (filteredMeals.isEmpty()) {
            Toast.makeText(this, "Нет приёмов пищи", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        listView = findViewById(R.id.listViewMeals);
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
        buttonBack  = findViewById(R.id.buttonBack);
        buttonForward = findViewById(R.id.buttonForward);
        buttonForward.setVisibility(View.GONE);
        layoutBackForward  = findViewById(R.id.layoutBackForward);
    }

    private void setupListView() {
        adapter = new ArrayAdapter<Meal>(this, R.layout.meal_item, filteredMeals) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    view = inflater.inflate(R.layout.meal_item, parent, false);
                }

                Meal meal = getItem(position);
                TextView tvInfo = view.findViewById(R.id.mealInfo);
                Button btnEdit = view.findViewById(R.id.buttonEditMeal);
                Button btnDelete = view.findViewById(R.id.buttonDeleteMeal);

                tvInfo.setText(StringFormatter.formatMealDetails(meal));

                btnDelete.setOnClickListener(v -> {
                    dbHelper.deleteMeal(meal.getId());
                    allMeals.remove(meal);
                    filteredMeals.remove(meal);
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "Приём удалён", Toast.LENGTH_SHORT).show();
                });

                btnEdit.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), EditMealActivity.class);
                    intent.putExtra("meal_id", meal.getId());
                    startActivity(intent);
                });

                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    private void setupRadioGroup() {
        radioGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            layoutBackForward.setVisibility(View.GONE);
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
        adapter.notifyDataSetChanged();
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
        adapter.notifyDataSetChanged();

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void back(){
        dayOffset--;

        updateMealsForCurrentOffset();
        buttonForward.setVisibility(View.VISIBLE);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void forward(){
        if (dayOffset < 0) {
            dayOffset++;
            updateMealsForCurrentOffset();
            if (dayOffset == 0) {
                buttonForward.setVisibility(View.GONE);
                Toast.makeText(this, "Вы на сегодняшнем дне", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateMealsForCurrentOffset() {
        LocalDate targetDate = LocalDate.now().plusDays(dayOffset);
        List<Meal> filtered = MealFilter.filterByDay(allMeals, targetDate.getDayOfMonth(), targetDate.getMonthValue(), targetDate.getYear() );

        filteredMeals.clear();
        filteredMeals.addAll(filtered);
        adapter.notifyDataSetChanged();

        // Показываем сообщение, если данных нет
        if (filteredMeals.isEmpty()) {
            Toast.makeText(this, "Нет приёмов пищи на " + targetDate.getDayOfMonth()+"."+ targetDate.getMonthValue()+"."+ targetDate.getYear(), Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Приёмов пищи на " + targetDate.getDayOfMonth()+"."+ targetDate.getMonthValue()+"."+ targetDate.getYear(), Toast.LENGTH_SHORT).show();
    }
}