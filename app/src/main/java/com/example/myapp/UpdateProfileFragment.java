package com.example.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapp.database.DatabaseHelper;
import com.example.myapp.model.Goal;
import com.example.myapp.model.User;
import com.example.myapp.service.CalorieCalculator;
import com.example.myapp.service.GoalService;
import com.example.myapp.service.StringFormatter;
import com.example.myapp.service.UserValidator;

import java.util.List;

public class UpdateProfileFragment extends Fragment {

    private EditText editTextName, editTextGender, editTextAge, editTextWeight, editTextHeight, editTextActive;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        User user = dbHelper.loadUser();

        if (user == null) {
            Toast.makeText(getContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        initViews(view);
        populateFields(user);
    }

    private void initViews(@NonNull View view) {
        editTextName = view.findViewById(R.id.textViewNameUpdate);
        editTextGender = view.findViewById(R.id.textViewGenderUpdate);
        editTextAge = view.findViewById(R.id.textViewAgeUpdate);
        editTextWeight = view.findViewById(R.id.textViewWeightUpdate);
        editTextHeight = view.findViewById(R.id.textViewHeightUpdate);
        editTextActive = view.findViewById(R.id.textViewActiveUpdate);
    }

    private void populateFields(User user) {
        editTextName.setText(user.getUsername());
        editTextGender.setText(user.getGender());
        editTextAge.setText(String.valueOf(user.getAge()));
        editTextWeight.setText(StringFormatter.formatWeightValue(user.getWeight()));
        editTextHeight.setText(StringFormatter.formatHeightValue(user.getHeight()));
        editTextActive.setText(StringFormatter.formatActiveValue(user.getActive()));
    }

    public boolean saveUserData() {

        String name = editTextName.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();
        String ageStr = editTextAge.getText().toString().trim();
        String weightStr = editTextWeight.getText().toString().trim();
        String heightStr = editTextHeight.getText().toString().trim();
        String activeStr = editTextActive.getText().toString().trim();


        if (!UserValidator.isAllFieldsValid(name, gender, ageStr, weightStr, heightStr, activeStr)) {
            Toast.makeText(getContext(), "Заполните все поля корректно (положительные числа)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!UserValidator.isGenderValid(gender)) {
            Toast.makeText(getContext(), "Пол должен быть 'м' или 'ж'", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Парсим числовые значения
        int age;
        double weight, height, active;
        try {
            age = Integer.parseInt(ageStr);
            weight = Double.parseDouble(weightStr);
            height = Double.parseDouble(heightStr);
            active = Double.parseDouble(activeStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Введите корректные числа", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Сохраняем обновлённого пользователя
        User updatedUser = new User(age, weight, height, gender, name, active);
        dbHelper.saveUser(updatedUser);

        // Обновляем эталонную цель
        updateDefaultGoal(updatedUser);

        Toast.makeText(getContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void updateDefaultGoal(User user) {
        CalorieCalculator calculator = new CalorieCalculator(user);
        Goal defaultGoal = GoalService.createDefaultGoal(
                calculator.getBMR(),
                calculator.getAimProtein(),
                calculator.getAimFat(),
                calculator.getAimCarb()
        );

        List<Goal> existingGoals = dbHelper.loadAllGoals();
        boolean exists = GoalService.isGoalExists(existingGoals, "Рекомендуемая");

        if (exists) {
            dbHelper.updateGoal(defaultGoal);
        } else {
            dbHelper.saveGoal(defaultGoal);
        }
    }
}