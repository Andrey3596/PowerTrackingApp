package com.example.myapp.model;

public class Goal {
    private int id;
    private String name;
    private Double calories;   // null = не задано
    private Double protein;
    private Double fat;
    private Double carb;

    public Goal(String name, Double calories, Double protein, Double fat, Double carb) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carb = carb;
    }
    public Goal() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getCalories() { return calories; }
    public void setCalories(Double calories) { this.calories = calories; }

    public Double getProtein() { return protein; }
    public void setProtein(Double protein) { this.protein = protein; }

    public Double getFat() { return fat; }
    public void setFat(Double fat) { this.fat = fat; }

    public Double getCarb() { return carb; }
    public void setCarb(Double carb) { this.carb = carb; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        if (hasCalories()) sb.append(": ").append(calories).append(" ккал");
        if (hasProtein()) sb.append(", ").append(protein).append(" г белков");
        if (hasFat()) sb.append(", ").append(fat).append(" г жиров");
        if (hasCarb()) sb.append(", ").append(carb).append(" г углеводов");
        return sb.toString();
    }

    public boolean hasCalories() { return calories != null && calories > 0; }
    public boolean hasProtein()  { return protein != null && protein > 0; }
    public boolean hasFat()      { return fat != null && fat > 0; }
    public boolean hasCarb()     { return carb != null && carb > 0; }
}
