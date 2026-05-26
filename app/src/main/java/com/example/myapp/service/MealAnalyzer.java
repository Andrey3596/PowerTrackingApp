package com.example.myapp.service;

import com.example.myapp.model.ConsumedProduct;
import com.example.myapp.model.Meal;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MealAnalyzer {

    public static class AnalysisResult {
        private double totalCalories;
        private double totalProtein;
        private double totalFat;
        private double totalCarb;
        private int daysCount;
        private Set<LocalDate> uniqueDays;

        public AnalysisResult() {
            this.totalCalories = 0;
            this.totalProtein = 0;
            this.totalFat = 0;
            this.totalCarb = 0;
            this.daysCount = 0;
            this.uniqueDays = new HashSet<>();
        }

        public double getTotalCalories() { return totalCalories; }
        public void setTotalCalories(double totalCalories) { this.totalCalories = totalCalories; }
        public void addCalories(double calories) { this.totalCalories += calories; }

        public double getTotalProtein() { return totalProtein; }
        public void setTotalProtein(double totalProtein) { this.totalProtein = totalProtein; }
        public void addProtein(double protein) { this.totalProtein += protein; }

        public double getTotalFat() { return totalFat; }
        public void setTotalFat(double totalFat) { this.totalFat = totalFat; }
        public void addFat(double fat) { this.totalFat += fat; }

        public double getTotalCarb() { return totalCarb; }
        public void setTotalCarb(double totalCarb) { this.totalCarb = totalCarb; }
        public void addCarb(double carb) { this.totalCarb += carb; }

        public int getDaysCount() { return daysCount; }
        public void setDaysCount(int daysCount) { this.daysCount = daysCount; }

        public Set<LocalDate> getUniqueDays() { return uniqueDays; }
        public void setUniqueDays(Set<LocalDate> uniqueDays) { this.uniqueDays = uniqueDays; }
    }

    public static AnalysisResult calculateSummary(List<Meal> meals) {
        AnalysisResult result = new AnalysisResult();
        for (Meal meal : meals) {
            result.getUniqueDays().add(meal.getDate());
            for (ConsumedProduct cp : meal.getConsumedProducts()) {
                result.addCalories(cp.getCalories());
                result.addProtein(cp.getProtein());
                result.addFat(cp.getFat());
                result.addCarb(cp.getCarb());
            }
        }
        result.setDaysCount(result.getUniqueDays().size());
        return result;
    }
}