//package com.example.myapp.service;
//
//import java.time.LocalDate;
//import java.util.Set;
//
//public class AnalysisData {
//    private double totalCalories;
//    private double totalProtein;
//    private double totalFat;
//    private double totalCarb;
//    private int daysCount;
//    private Set<LocalDate> uniqueDays;
//
//    public AnalysisData() {
//        this.totalCalories = 0;
//        this.totalProtein = 0;
//        this.totalFat = 0;
//        this.totalCarb = 0;
//        this.daysCount = 0;
//    }
//
//    // Геттеры и сеттеры
//    public double getTotalCalories() { return totalCalories; }
//    public void setTotalCalories(double totalCalories) { this.totalCalories = totalCalories; }
//    public void addCalories(double calories) { this.totalCalories += calories; }
//
//    public double getTotalProtein() { return totalProtein; }
//    public void setTotalProtein(double totalProtein) { this.totalProtein = totalProtein; }
//    public void addProtein(double protein) { this.totalProtein += protein; }
//
//    public double getTotalFat() { return totalFat; }
//    public void setTotalFat(double totalFat) { this.totalFat = totalFat; }
//    public void addFat(double fat) { this.totalFat += fat; }
//
//    public double getTotalCarb() { return totalCarb; }
//    public void setTotalCarb(double totalCarb) { this.totalCarb = totalCarb; }
//    public void addCarb(double carb) { this.totalCarb += carb; }
//
//    public int getDaysCount() { return daysCount; }
//    public void setDaysCount(int daysCount) { this.daysCount = daysCount; }
//
//    public Set<LocalDate> getUniqueDays() { return uniqueDays; }
//    public void setUniqueDays(Set<LocalDate> uniqueDays) { this.uniqueDays = uniqueDays; }
//}