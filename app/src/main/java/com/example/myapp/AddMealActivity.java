package com.example.myapp;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.myapp.database.DatabaseHelper;
import com.example.myapp.model.ConsumedProduct;
import com.example.myapp.model.Meal;
import com.example.myapp.model.Product;
import com.example.myapp.service.ProductValidator;
import com.example.myapp.service.StringFormatter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddMealActivity extends BaseActivity {

    private DatabaseHelper dbHelper;

    // UI элементы
    private Spinner spinnerMealType;
    private RadioGroup radioGroupChoice;
    private LinearLayout layoutExistingProduct, layoutNewProduct,layoutDate;
    private Spinner spinnerProduct;
    private EditText editWeightExisting, editWeightNew, editName, editCalories, editProtein, editFat, editCarb,editDateDayMeal,editDateMonthMeal,editDateYearMeal;
    private Button buttonAddProduct, buttonSaveMeal;
    private ListView listViewAddedProducts;

    // Данные
    private List<Product> productList;
    private ArrayAdapter<Product> productSpinnerAdapter;
    private List<ConsumedProduct> consumedProducts = new ArrayList<>();
    private ArrayAdapter<ConsumedProduct> addedProductsAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        applyBottomPaddingToNavPanel();
        dbHelper = new DatabaseHelper(this);

        initViews();
        setupSpinnerMealType();
        setupRadioButtons();

        loadProductSpinner();

        setupAddedProductsList();

        setupDateMeal();

        buttonAddProduct.setOnClickListener(v -> addProductToMeal());
        buttonSaveMeal.setOnClickListener(v -> saveMeal());
    }

    private void initViews() {
        spinnerMealType = findViewById(R.id.spinnerMealType);
        radioGroupChoice = findViewById(R.id.radioGroupChoice);
        layoutExistingProduct = findViewById(R.id.layoutExistingProduct);
        layoutNewProduct = findViewById(R.id.layoutNewProduct);
        spinnerProduct = findViewById(R.id.spinnerProduct);
        editWeightExisting = findViewById(R.id.editWeightExisting);
        editWeightNew = findViewById(R.id.editWeightNew);
        editName = findViewById(R.id.editName);
        editCalories = findViewById(R.id.editCalories);
        editProtein = findViewById(R.id.editProtein);
        editFat = findViewById(R.id.editFat);
        editCarb = findViewById(R.id.editCarb);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        buttonSaveMeal = findViewById(R.id.buttonSaveMeal);
        listViewAddedProducts = findViewById(R.id.listViewAddedProducts);

        layoutDate = findViewById(R.id.layoutDate);
        editDateDayMeal= findViewById(R.id.editDateDayMeal);
        editDateMonthMeal= findViewById(R.id.editDateMonthMeal);
        editDateYearMeal= findViewById(R.id.editDateYearMeal);
    }

    private void setupSpinnerMealType() {
        String[] mealTypes = {"Завтрак", "Обед", "Ужин", "Перекус"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mealTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(adapter);
    }

    private void setupRadioButtons() {
        radioGroupChoice.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioExisting) {
                layoutExistingProduct.setVisibility(View.VISIBLE);
                layoutNewProduct.setVisibility(View.GONE);
            } else {
                layoutExistingProduct.setVisibility(View.GONE);
                layoutNewProduct.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadProductSpinner() {
        productList = dbHelper.loadAllProducts();
        productSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productList);
        productSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduct.setAdapter(productSpinnerAdapter);
    }

    private void setupAddedProductsList() {
        addedProductsAdapter = new ArrayAdapter<ConsumedProduct>(this, android.R.layout.simple_list_item_1, consumedProducts) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ConsumedProduct cp = getItem(position);
                String text = StringFormatter.formatConsumedProduct(
                        cp.getProduct().getNameProduct(),
                        cp.getWeightGrams(),
                        cp.getCalories()
                );
                TextView tv = (TextView) view;
                tv.setText(text);
                return view;
            }
        };
        listViewAddedProducts.setAdapter(addedProductsAdapter);

        listViewAddedProducts.setOnItemLongClickListener((parent, view, position, id) -> {
            consumedProducts.remove(position);
            addedProductsAdapter.notifyDataSetChanged();
            Toast.makeText(AddMealActivity.this, "Продукт удалён", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void addProductToMeal() {
        int checkedId = radioGroupChoice.getCheckedRadioButtonId();
        if (checkedId == R.id.radioExisting) {
            addExistingProduct();
        } else {
            addNewProduct();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupDateMeal() {
        LocalDate today = LocalDate.now();

        editDateDayMeal.setText(String.valueOf(today.getDayOfMonth()));
        editDateMonthMeal.setText(String.valueOf(today.getMonthValue()));
        editDateYearMeal.setText(String.valueOf(today.getYear()));

    }
    private void addExistingProduct() {
        int position = spinnerProduct.getSelectedItemPosition();
        if (position < 0 || position >= productSpinnerAdapter.getCount()) {
            Toast.makeText(this, "Выберите продукт", Toast.LENGTH_SHORT).show();
            return;
        }

        Product selectedProduct = productSpinnerAdapter.getItem(position);
        if (selectedProduct == null) {
            Toast.makeText(this, "Продукт не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        String weightStr = editWeightExisting.getText().toString().trim();
        if (!ProductValidator.isWeightValid(weightStr)) {
            Toast.makeText(this, "Введите корректный вес (>0)", Toast.LENGTH_SHORT).show();
            return;
        }

        double weight = Double.parseDouble(weightStr);
        ConsumedProduct cp = new ConsumedProduct(selectedProduct, weight);
        consumedProducts.add(cp);
        addedProductsAdapter.notifyDataSetChanged();
        editWeightExisting.setText("");
    }

    private void addNewProduct() {
        String name = editName.getText().toString().trim();
        String weightStr = editWeightNew.getText().toString().trim();
        String calStr = editCalories.getText().toString().trim();
        String protStr = editProtein.getText().toString().trim();
        String fatStr = editFat.getText().toString().trim();
        String carbStr = editCarb.getText().toString().trim();

        if (!ProductValidator.isNewProductValid(name, weightStr, calStr, protStr, fatStr, carbStr)) {
            Toast.makeText(this, "Заполните все поля нового продукта корректно (>0)", Toast.LENGTH_SHORT).show();
            return;
        }

        double weight = Double.parseDouble(weightStr);
        double cal = Double.parseDouble(calStr);
        double prot = Double.parseDouble(protStr);
        double fat = Double.parseDouble(fatStr);
        double carb = Double.parseDouble(carbStr);

        Product newProduct = new Product(name, cal, prot, fat, carb);
        dbHelper.saveProduct(newProduct);
        loadProductSpinner();

        ConsumedProduct cp = new ConsumedProduct(newProduct, weight);
        consumedProducts.add(cp);
        addedProductsAdapter.notifyDataSetChanged();

        clearNewProductFields();
    }

    private void clearNewProductFields() {
        editName.setText("");
        editWeightNew.setText("");
        editCalories.setText("");
        editProtein.setText("");
        editFat.setText("");
        editCarb.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveMeal() {
        if (consumedProducts.isEmpty()) {
            Toast.makeText(this, "Добавьте хотя бы один продукт", Toast.LENGTH_SHORT).show();
            return;
        }
        String type = spinnerMealType.getSelectedItem().toString();

        int day,month,year;
        try {
            day = Integer.parseInt(editDateDayMeal.getText().toString());
            month = Integer.parseInt(editDateMonthMeal.getText().toString());
            year = Integer.parseInt(editDateYearMeal.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Введите числа во все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate current;
        try {
            current = LocalDate.of(year, month, day);
        }
        catch (DateTimeException e) {
            Toast.makeText(this, "Введите корректную дату", Toast.LENGTH_SHORT).show();
            return;
        }

        Meal meal = new Meal(consumedProducts, type, current);

        dbHelper.saveMeal(meal);
        Toast.makeText(this, "Приём пищи сохранён", Toast.LENGTH_SHORT).show();
        finish();
    }
}