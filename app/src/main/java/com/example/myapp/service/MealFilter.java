package com.example.myapp.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.myapp.model.Meal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MealFilter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Meal> filterByDay(List<Meal> meals, int day, int month, int year) {
        List<Meal> filtered = new ArrayList<>();
        LocalDate target = LocalDate.of(year, month, day);
        for (Meal meal : meals) {
            if (meal.getDate().equals(target)) {
                filtered.add(meal);
            }
        }
        return filtered;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Meal> filterByMonth(List<Meal> meals, int month, int year) {
        List<Meal> filtered = new ArrayList<>();
        for (Meal meal : meals) {
            LocalDate date = meal.getDate();
            if (date.getMonthValue() == month && date.getYear() == year) {
                filtered.add(meal);
            }
        }
        return filtered;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Meal> filterByYear(List<Meal> meals, int year) {
        List<Meal> filtered = new ArrayList<>();
        for (Meal meal : meals) {
            if (meal.getDate().getYear() == year) {
                filtered.add(meal);
            }
        }
        return filtered;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Meal> filterByInterval(List<Meal> meals, LocalDate startDate, LocalDate endDate) {
        List<Meal> filtered = new ArrayList<>();
        for (Meal meal : meals) {
            LocalDate date = meal.getDate();
            if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                filtered.add(meal);
            }
        }
        return filtered;
    }


}