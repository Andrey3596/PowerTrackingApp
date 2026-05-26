package com.example.myapp.service;

public class GoalValidator {

    public static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean hasAtLeastOneValue(Double kal, Double protein, Double fat, Double carb) {
        return (kal != null && kal > 0) ||
                (protein != null && protein > 0) ||
                (fat != null && fat > 0) ||
                (carb != null && carb > 0);
    }


}