package com.example.myapp;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.database.DatabaseHelper;
import com.example.myapp.model.ConsumedProduct;
import com.example.myapp.model.Meal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EditMealActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Meal currentMeal;
    private int mealId;

    private Spinner spinnerMealType;
    private EditText editDateDay, editDateMonth, editDateYear;
    private ListView listViewProducts;
    private Button buttonSave;

    private List<ConsumedProduct> editableProducts;
    private ArrayAdapter<ConsumedProduct> productAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meal);

        dbHelper = new DatabaseHelper(this);
        mealId = getIntent().getIntExtra("meal_id", -1);
        if (mealId == -1) {
            Toast.makeText(this, "Ошибка: ID приёма не передан", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Загружаем приём
        List<Meal> allMeals = dbHelper.loadAllMeals();
        for (Meal m : allMeals) {
            if (m.getId() == mealId) {
                currentMeal = m;
                break;
            }
        }
        if (currentMeal == null) {
            Toast.makeText(this, "Приём не найден", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        populateFields();
        setupProductList();

        buttonSave.setOnClickListener(v -> saveChanges());
    }

    private void initViews() {
        spinnerMealType = findViewById(R.id.spinnerMealType);
        editDateDay = findViewById(R.id.editDateDay);
        editDateMonth = findViewById(R.id.editDateMonth);
        editDateYear = findViewById(R.id.editDateYear);
        listViewProducts = findViewById(R.id.listViewProducts);
        buttonSave = findViewById(R.id.buttonSave);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void populateFields() {
        String[] mealTypes = {"Завтрак", "Обед", "Ужин", "Перекус"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mealTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(adapter);
        for (int i = 0; i < mealTypes.length; i++) {
            if (mealTypes[i].equals(currentMeal.getTypeMeal())) {
                spinnerMealType.setSelection(i);
                break;
            }
        }

        LocalDate date = currentMeal.getDate();
        editDateDay.setText(String.valueOf(date.getDayOfMonth()));
        editDateMonth.setText(String.valueOf(date.getMonthValue()));
        editDateYear.setText(String.valueOf(date.getYear()));

        editableProducts = new ArrayList<>();
        for (ConsumedProduct cp : currentMeal.getConsumedProducts()) {
            editableProducts.add(cp);
        }
    }

    private void setupProductList() {
        productAdapter = new ArrayAdapter<ConsumedProduct>(this, R.layout.edit_product_item, editableProducts) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    view = inflater.inflate(R.layout.edit_product_item, parent, false);
                }
                ConsumedProduct cp = getItem(position);
                TextView tvName = view.findViewById(R.id.productName);
                EditText etWeight = view.findViewById(R.id.editWeight);
                Button btnRemove = view.findViewById(R.id.removeProduct);

                tvName.setText(cp.getProduct().getNameProduct());
                etWeight.setText(String.format("%.1f", cp.getWeightGrams()));

                etWeight.setOnFocusChangeListener((v, hasFocus) -> {
                    if (!hasFocus) {
                        String w = etWeight.getText().toString().trim();
                        if (!w.isEmpty()) {
                            try {
                                double newWeight = Double.parseDouble(w);
                                if (newWeight > 0) cp.setWeightGrams(newWeight);
                                else Toast.makeText(getContext(), "Вес должен быть >0", Toast.LENGTH_SHORT).show();
                            } catch (NumberFormatException e) {
                                Toast.makeText(getContext(), "Неверное число", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                btnRemove.setOnClickListener(v -> {
                    editableProducts.remove(position);
                    notifyDataSetChanged();
                });
                return view;
            }
        };
        listViewProducts.setAdapter(productAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveChanges() {
        String newType = spinnerMealType.getSelectedItem().toString();
        int day = Integer.parseInt(editDateDay.getText().toString());
        int month = Integer.parseInt(editDateMonth.getText().toString());
        int year = Integer.parseInt(editDateYear.getText().toString());
        LocalDate newDate = LocalDate.of(year, month, day);

        currentMeal.setTypeMeal(newType);
        currentMeal.setDate(newDate);
        currentMeal.setConsumedProducts(editableProducts);

        dbHelper.updateMeal(currentMeal);
        Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_SHORT).show();
        finish();
    }
}