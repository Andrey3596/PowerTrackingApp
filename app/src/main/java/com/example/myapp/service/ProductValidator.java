package com.example.myapp.service;

public class ProductValidator {

    public static boolean isWeightValid(String weightStr) {
        if (weightStr == null || weightStr.isEmpty()) return false;
        try {
            double weight = Double.parseDouble(weightStr);
            return weight > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isKbjuValid(String valueStr) {
        if (valueStr == null || valueStr.isEmpty()) return false;
        try {
            double value = Double.parseDouble(valueStr);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNewProductValid(String name, String weightStr, String calStr,
                                            String protStr, String fatStr, String carbStr) {
        if (name == null || name.trim().isEmpty()) return false;
        return isWeightValid(weightStr) && isKbjuValid(calStr) &&
                isKbjuValid(protStr) && isKbjuValid(fatStr) && isKbjuValid(carbStr);
    }

    public static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean areKbjuValid(Double cal, Double prot, Double fat, Double carb) {
        if (cal == null || cal <= 0) return false;
        if (prot == null || prot <= 0) return false;
        if (fat == null || fat <= 0) return false;
        if (carb == null || carb <= 0) return false;
        return true;
    }
}