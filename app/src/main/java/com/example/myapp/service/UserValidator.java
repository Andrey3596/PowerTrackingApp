package com.example.myapp.service;

public class UserValidator {

    public static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isGenderValid(String gender) {
        return "м".equals(gender) || "ж".equals(gender);
    }

    public static boolean isAgeValid(String ageStr) {
        try {
            int age = Integer.parseInt(ageStr);
            return age > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isWeightValid(String weightStr) {
        try {
            double weight = Double.parseDouble(weightStr);
            return weight > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isHeightValid(String heightStr) {
        try {
            double height = Double.parseDouble(heightStr);
            return height > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isActiveValid(String activeStr) {
        try {
            double active = Double.parseDouble(activeStr);
            return active > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isAllFieldsValid(String name, String gender, String ageStr,
                                           String weightStr, String heightStr, String activeStr) {
        return isNameValid(name) && isGenderValid(gender) && isAgeValid(ageStr) &&
                isWeightValid(weightStr) && isHeightValid(heightStr) && isActiveValid(activeStr);
    }


}