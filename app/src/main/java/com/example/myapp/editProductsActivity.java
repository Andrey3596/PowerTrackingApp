package com.example.myapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.database.DatabaseHelper;
import com.example.myapp.model.Product;
import com.example.myapp.service.ProductValidator;
import com.example.myapp.service.StringFormatter;

import java.util.List;

public class editProductsActivity extends BaseActivity {

    private DatabaseHelper dbHelper;
    private List<Product> productList;
    private ListView listView;
    private ArrayAdapter<Product> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products);
        applyBottomPaddingToNavPanel();

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewProductsEdit);
        loadProducts();

        findViewById(R.id.buttonAddProduct).setOnClickListener(v -> showProductDialog(null));
    }

    private void loadProducts() {
        productList = dbHelper.loadAllProducts();
        adapter = new ArrayAdapter<Product>(this, R.layout.product_edit_item, productList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    view = LayoutInflater.from(getContext()).inflate(R.layout.product_edit_item, parent, false);
                }

                Product product = getItem(position);
                TextView tvName = view.findViewById(R.id.productNameEdit);
                TextView tvKbju = view.findViewById(R.id.productKbjuEdit);
                Button btnEdit = view.findViewById(R.id.buttonEditProduct);
                Button btnDelete = view.findViewById(R.id.buttonDeleteProduct);

                tvName.setText(product.getNameProduct());
                tvKbju.setText(StringFormatter.formatProductKbju(product));

                btnEdit.setOnClickListener(v -> showProductDialog(product));
                btnDelete.setOnClickListener(v -> {
                    dbHelper.deleteProduct(product.getId());
                    productList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "Продукт удалён", Toast.LENGTH_SHORT).show();
                });
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    private void showProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_product, null);

        EditText etName = dialogView.findViewById(R.id.dialogProductName);
        EditText etCal = dialogView.findViewById(R.id.dialogProductCal);
        EditText etProt = dialogView.findViewById(R.id.dialogProductProt);
        EditText etFat = dialogView.findViewById(R.id.dialogProductFat);
        EditText etCarb = dialogView.findViewById(R.id.dialogProductCarb);

        if (product != null) {
            etName.setText(product.getNameProduct());
            etCal.setText(String.valueOf(product.getCalory()));
            etProt.setText(String.valueOf(product.getProtein()));
            etFat.setText(String.valueOf(product.getFat()));
            etCarb.setText(String.valueOf(product.getCarb()));
        }

        builder.setView(dialogView)
                .setTitle(product == null ? "Добавить продукт" : "Редактировать продукт")
                .setPositiveButton("Сохранить", (dialog, which) -> saveProduct(product, etName, etCal, etProt, etFat, etCarb))
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void saveProduct(Product product, EditText etName, EditText etCal,
                             EditText etProt, EditText etFat, EditText etCarb) {
        String name = etName.getText().toString().trim();
        if (!ProductValidator.isNameValid(name)) {
            Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show();
            return;
        }

        Double cal = parseDouble(etCal);
        Double prot = parseDouble(etProt);
        Double fat = parseDouble(etFat);
        Double carb = parseDouble(etCarb);

        if (!ProductValidator.areKbjuValid(cal, prot, fat, carb)) {
            Toast.makeText(this, "Введите корректные положительные числа", Toast.LENGTH_SHORT).show();
            return;
        }

        if (product == null) {
            createNewProduct(name, cal, prot, fat, carb);
        } else {
            updateExistingProduct(product, name, cal, prot, fat, carb);
        }
    }

    private Double parseDouble(EditText editText) {
        String valueStr = editText.getText().toString().trim();
        if (valueStr.isEmpty()) return null;
        try {
            return Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void createNewProduct(String name, Double cal, Double prot, Double fat, Double carb) {
        // Проверка на дубликат имени
        List<Product> existing = dbHelper.loadAllProducts();
        for (Product p : existing) {
            if (p.getNameProduct().equalsIgnoreCase(name)) {
                Toast.makeText(this, "Продукт с таким названием уже существует", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Product newProduct = new Product(name, cal, prot, fat, carb);
        dbHelper.saveProduct(newProduct);
        loadProducts();
        Toast.makeText(this, "Продукт добавлен", Toast.LENGTH_SHORT).show();
    }

    private void updateExistingProduct(Product product, String name, Double cal,
                                       Double prot, Double fat, Double carb) {
        product.setNameProduct(name);
        product.setCalory(cal);
        product.setProtein(prot);
        product.setFat(fat);
        product.setCarb(carb);
        dbHelper.updateProduct(product);
        loadProducts();
        Toast.makeText(this, "Продукт обновлён", Toast.LENGTH_SHORT).show();
    }
}