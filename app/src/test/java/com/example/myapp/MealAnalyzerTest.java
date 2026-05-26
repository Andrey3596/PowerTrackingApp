package com.example.myapp;

import com.example.myapp.model.ConsumedProduct;
import com.example.myapp.model.Meal;
import com.example.myapp.model.Product;
import com.example.myapp.service.MealAnalyzer;
import org.junit.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MealAnalyzerTest {

    private List<Meal> createTestMeals() {
        List<Meal> meals = new ArrayList<>();
        Product product1 = new Product("Продукт 1", 200.0, 15.0, 10.0, 25.0);
        Product product2 = new Product("Продукт 2", 100.0, 5.0, 2.0, 15.0);
        ConsumedProduct cp1 = new ConsumedProduct(product1, 100.0);
        ConsumedProduct cp2 = new ConsumedProduct(product2, 100.0);
        List<ConsumedProduct> products1 = new ArrayList<>();
        products1.add(cp1);
        List<ConsumedProduct> products2 = new ArrayList<>();
        products2.add(cp2);
        meals.add(new Meal(products1, "Завтрак", LocalDate.of(2025, 5, 15)));
        meals.add(new Meal(products2, "Обед", LocalDate.of(2025, 5, 16)));
        return meals;
    }

    @Test
    public void calculateSummary_returnsCorrectTotalCalories() {
        List<Meal> meals = createTestMeals();
        MealAnalyzer.AnalysisResult result = MealAnalyzer.calculateSummary(meals);
        assertEquals(300.0, result.getTotalCalories(), 0.01);
    }

    @Test
    public void calculateSummary_returnsCorrectTotalProtein() {
        List<Meal> meals = createTestMeals();
        MealAnalyzer.AnalysisResult result = MealAnalyzer.calculateSummary(meals);
        assertEquals(20.0, result.getTotalProtein(), 0.01);
    }

    @Test
    public void calculateSummary_returnsCorrectDaysCount() {
        List<Meal> meals = createTestMeals();
        MealAnalyzer.AnalysisResult result = MealAnalyzer.calculateSummary(meals);
        assertEquals(2, result.getDaysCount());
    }

    @Test
    public void calculateSummary_returnsEmptyResult_forEmptyList() {
        List<Meal> meals = new ArrayList<>();
        MealAnalyzer.AnalysisResult result = MealAnalyzer.calculateSummary(meals);
        assertEquals(0, result.getTotalCalories(), 0.01);
        assertEquals(0, result.getDaysCount());
    }

    @Test
    public void calculateSummary_handlesSameDayMeals() {
        List<Meal> meals = new ArrayList<>();
        Product product = new Product("Продукт", 100.0, 10.0, 5.0, 20.0);
        ConsumedProduct cp = new ConsumedProduct(product, 100.0);
        List<ConsumedProduct> products = new ArrayList<>();
        products.add(cp);
        meals.add(new Meal(products, "Завтрак", LocalDate.of(2025, 5, 15)));
        meals.add(new Meal(products, "Обед", LocalDate.of(2025, 5, 15)));
        MealAnalyzer.AnalysisResult result = MealAnalyzer.calculateSummary(meals);
        assertEquals(200.0, result.getTotalCalories(), 0.01);
        assertEquals(1, result.getDaysCount());
    }
}