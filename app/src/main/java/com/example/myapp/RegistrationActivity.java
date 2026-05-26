package com.example.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.model.*;
import com.example.myapp.service.*;
import com.example.myapp.database.*;

import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextGender;
    private EditText editTextAge;
    private EditText editTextWeight;
    private EditText editTextHeight;
    private EditText editTextActive;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrati);
        dbHelper = new DatabaseHelper(this);

        if (dbHelper.hasUser()) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setupHintButtons();
        initEditTexts();
    }

    private void setupHintButtons() {
        Button btnHintName = findViewById(R.id.buttonHintName);
        btnHintName.setOnClickListener(v -> showHintDialog("Введите имя чтобы мы знали как к вам обращаться)"));

        Button btnHintGender = findViewById(R.id.buttonHintGender);
        btnHintGender.setOnClickListener(v -> showHintDialog("Введите ваш пол(м/ж). Нужен для анализа ваших данных"));

        Button btnHintAge = findViewById(R.id.buttonHintAge);
        btnHintAge.setOnClickListener(v -> showHintDialog("Введите ваш возраст(целые числа от 0). Нужен для анализа ваших данных"));

        Button btnHintWeight = findViewById(R.id.buttonHintWeight);
        btnHintWeight.setOnClickListener(v -> showHintDialog("Введите ваш вес (от 0). Нужен для анализа ваших данных"));

        Button btnHintHeight = findViewById(R.id.buttonHintHeight);
        btnHintHeight.setOnClickListener(v -> showHintDialog("Введите ваш рост(от 0). Нужен для анализа ваших данных"));

        Button btnHintActive = findViewById(R.id.buttonHintActive);
        btnHintActive.setOnClickListener(v -> {
            String message = "Введите ваш уровень активности:\n\n" +
                    "1.2 - Сидячий образ жизни (офис, минимум движений)\n" +
                    "1.375 - Лёгкая активность (лёгкие прогулки, 1–3 тренировки в неделю)\n" +
                    "1.55 - Умеренная (подвижная работа, 3–5 тренировок)\n" +
                    "1.725 - Высокая (ежедневные интенсивные тренировки или физическая работа)\n" +
                    "1.9 - Экстремальная (спортсмены на пике, строители, 2 тренировки в день)\n\n" +
                    "Нужен для анализа ваших данных";
            showHintDialog(message);
        });
    }

    private void showHintDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    private void initEditTexts() {
        editTextName = findViewById(R.id.editTextName);
        editTextGender = findViewById(R.id.editTextGender);
        editTextAge = findViewById(R.id.editTextAge);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextActive = findViewById(R.id.editTextActive);
    }

    public void registration(View view) {
        String name = editTextName.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();
        String ageStr = editTextAge.getText().toString().trim();
        String weightStr = editTextWeight.getText().toString().trim();
        String heightStr = editTextHeight.getText().toString().trim();
        String activeStr = editTextActive.getText().toString().trim();

        boolean isValid = UserValidator.isAllFieldsValid(name, gender, ageStr, weightStr, heightStr, activeStr);

        if (isValid) {
            int age = Integer.parseInt(ageStr);
            double weight = Double.parseDouble(weightStr);
            double height = Double.parseDouble(heightStr);
            double active = Double.parseDouble(activeStr);

            User user = new User(age, weight, height, gender, name, active);
            dbHelper.saveUser(user);
            saveDefaultGoal(user);

            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        } else {
            showErrorDialog();
        }
    }

    private void saveDefaultGoal(User user) {
        CalorieCalculator calculator = new CalorieCalculator(user);
        Goal defaultGoal = new Goal();
        defaultGoal.setName("Рекомендуемая");
        defaultGoal.setCalories(calculator.getBMR());
        defaultGoal.setProtein(calculator.getAimProtein());
        defaultGoal.setFat(calculator.getAimFat());
        defaultGoal.setCarb(calculator.getAimCarb());

        List<Goal> existingGoals = dbHelper.loadAllGoals();
        boolean exists = false;
        for (Goal g : existingGoals) {
            if ("Рекомендуемая".equals(g.getName())) {
                exists = true;
                break;
            }
        }

        if (exists) {
            dbHelper.updateGoal(defaultGoal);
        } else {
            dbHelper.saveGoal(defaultGoal);
        }
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы ввели что-то не правильно. Посмотрите Подсказки (Знак вопроса).")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }
}