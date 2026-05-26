package com.example.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapp.database.DatabaseHelper;
import com.example.myapp.model.User;
import com.example.myapp.service.CalorieCalculator;
import com.example.myapp.service.StringFormatter;

public class ReadProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_read_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        User user = dbHelper.loadUser();

        if (user == null) {
            return; // если пользователь не найден, ничего не отображаем
        }

        CalorieCalculator calculator = new CalorieCalculator(user);

        // Находим TextView в разметке
        TextView tvName = view.findViewById(R.id.textViewName);
        TextView tvGender = view.findViewById(R.id.textViewGender);
        TextView tvAge = view.findViewById(R.id.textViewBAge);
        TextView tvWeight = view.findViewById(R.id.textViewBWeight);
        TextView tvHeight = view.findViewById(R.id.textViewBHeight);
        TextView tvActive = view.findViewById(R.id.textViewBActive);
        TextView tvProportions = view.findViewById(R.id.textViewBProportion);

        // Заполняем основные данные пользователя
        tvName.setText(getString(R.string.name_fragment) + " " + user.getUsername());
        tvGender.setText(getString(R.string.gender_fragment) + " " + user.getGender());
        tvAge.setText(getString(R.string.age_fragment) + " " + user.getAge());

        tvWeight.setText(getString(R.string.weight_fragment) + " " +
                StringFormatter.formatWeight(user.getWeight()));
        tvHeight.setText(getString(R.string.height_fragment) + " " +
                StringFormatter.formatHeight(user.getHeight()));
        tvActive.setText(getString(R.string.active_fragment) + " " +
                StringFormatter.formatActivity(user.getActive()));

        // Формируем и отображаем рекомендации КБЖУ
        String proportions = StringFormatter.formatRecommendations(
                calculator.getBMR(),
                calculator.getAimProtein(),
                calculator.getAimFat(),
                calculator.getAimCarb()
        );
        tvProportions.setText(proportions);
    }
}