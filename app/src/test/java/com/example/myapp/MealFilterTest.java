package com.example.myapp;

import com.example.myapp.model.ConsumedProduct;
import com.example.myapp.model.Meal;
import com.example.myapp.model.Product;
import com.example.myapp.service.MealFilter;
import org.junit.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MealFilterTest {

    private List<Meal> createTestMeals() {
        List<Meal> meals = new ArrayList<>();
        Product product = new Product("Тестовый продукт", 100.0, 10.0, 5.0, 20.0);
        ConsumedProduct cp = new ConsumedProduct(product, 100.0);
        List<ConsumedProduct> products = new ArrayList<>();
        products.add(cp);
        meals.add(new Meal(products, "Завтрак", LocalDate.of(2025, 5, 15)));
        meals.add(new Meal(products, "Обед", LocalDate.of(2025, 5, 16)));
        meals.add(new Meal(products, "Ужин", LocalDate.of(2025, 6, 10)));
        meals.add(new Meal(products, "Завтрак", LocalDate.of(2024, 5, 15)));
        return meals;
    }

    @Test
    public void filterByDay_returnsOnlyMealsOnThatDay() {
        List<Meal> meals = createTestMeals();
        List<Meal> filtered = MealFilter.filterByDay(meals, 15, 5, 2025);
        assertEquals(1, filtered.size());
        assertEquals(LocalDate.of(2025, 5, 15), filtered.get(0).getDate());
    }

    @Test
    public void filterByMonth_returnsOnlyMealsInThatMonthAndYear() {
        List<Meal> meals = createTestMeals();
        List<Meal> filtered = MealFilter.filterByMonth(meals, 5, 2025);
        assertEquals(2, filtered.size());
    }

    @Test
    public void filterByYear_returnsOnlyMealsInThatYear() {
        List<Meal> meals = createTestMeals();
        List<Meal> filtered = MealFilter.filterByYear(meals, 2025);
        assertEquals(3, filtered.size());
    }

    @Test
    public void filterByInterval_returnsOnlyMealsWithinInterval() {
        List<Meal> meals = createTestMeals();
        LocalDate start = LocalDate.of(2025, 5, 10);
        LocalDate end = LocalDate.of(2025, 6, 15);
        List<Meal> filtered = MealFilter.filterByInterval(meals, start, end);
        assertEquals(3, filtered.size());
    }

    @Test
    public void filterByDay_returnsEmptyList_whenNoMatch() {
        List<Meal> meals = createTestMeals();
        List<Meal> filtered = MealFilter.filterByDay(meals, 1, 1, 2020);
        assertTrue(filtered.isEmpty());
    }
}