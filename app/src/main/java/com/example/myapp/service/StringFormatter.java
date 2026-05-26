package com.example.myapp.service;

import com.example.myapp.model.ConsumedProduct;
import com.example.myapp.model.Meal;
import com.example.myapp.model.Product;

import java.time.LocalDate;
import java.util.Locale;

public class StringFormatter {

    // Форматирует потреблённый продукт для списка
    public static String formatConsumedProduct(String name, double weight, double calories) {
        return String.format(Locale.US, "%s – %.1f г: %.1f ккал", name, weight, calories);
    }

    // Форматирует КБЖУ продукта для спиннера
    public static String formatProductForSpinner(String name, double calories, double protein, double fat, double carb) {
        return String.format(Locale.US, "%s (%.1f ккал, %.1f б, %.1f ж, %.1f у)", name, calories, protein, fat, carb);
    }

    // Форматирует приём пищи для анализа
    public static String formatMealForAnalysis(String type, LocalDate date,
                                               double calories, double protein, double fat, double carb) {
        return String.format(Locale.US, "%s – %s\nКБЖУ: %.0f ккал, %.0f/%.0f/%.0f г",
                type, date, calories, protein, fat, carb);
    }

    // Форматирует сводку за период
    public static String formatSummary(int days, double totalCal, double totalProt,
                                       double totalFat, double totalCarb) {
        StringBuilder sb = new StringBuilder();
        sb.append("Сводка за ").append(days).append(" дн.\n");
        sb.append(String.format(Locale.US, "Всего КБЖУ: %.0f ккал, %.0f/%.0f/%.0f г\n",
                totalCal, totalProt, totalFat, totalCarb));
        return sb.toString();
    }

    // Форматирует сравнение с рекомендуемой целью (только калории)
    public static String formatRecommendedGoalComparison(double actual, double target, int days) {
        double targetTotal = target * days;
        return String.format(Locale.US, "Рекомендуемая цель:\nкалории %.0f/%.0f (%.0f%%)",
                actual, targetTotal, actual / targetTotal * 100);
    }

    // Форматирует сравнение с пользовательской целью (все нутриенты)
    public static String formatUserGoalComparison(String name,
                                                  double actualCal, Double targetCal,
                                                  double actualProt, Double targetProt,
                                                  double actualFat, Double targetFat,
                                                  double actualCarb, Double targetCarb) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":\n");
        if (targetCal != null) {
            sb.append(String.format(Locale.US, "калории %.0f/%.0f (%.0f%%)\n", actualCal, targetCal, actualCal / targetCal * 100));
        }
        if (targetProt != null) {
            sb.append(String.format(Locale.US, "белки %.0f/%.0f (%.0f%%)\n", actualProt, targetProt, actualProt / targetProt * 100));
        }
        if (targetFat != null) {
            sb.append(String.format(Locale.US, "жиры %.0f/%.0f (%.0f%%)\n", actualFat, targetFat, actualFat / targetFat * 100));
        }
        if (targetCarb != null) {
            sb.append(String.format(Locale.US, "углеводы %.0f/%.0f (%.0f%%)\n", actualCarb, targetCarb, actualCarb / targetCarb * 100));
        }
        return sb.toString();
    }



    public static String formatProductKbju(Product product) {
        return String.format(Locale.US, "%.1f ккал, %.1f б, %.1f ж, %.1f у на 100г",
                product.getCalory(), product.getProtein(), product.getFat(), product.getCarb());
    }

    // Форматирование веса (с 1 знаком после запятой)
    public static String formatWeight(double weight) {
        return String.format(Locale.US, "%.1f кг", weight);
    }

    // Форматирование роста (с 1 знаком после запятой)
    public static String formatHeight(double height) {
        return String.format(Locale.US, "%.1f см", height);
    }

    // Форматирование уровня активности (с 1 знаком после запятой)
    public static String formatActivity(double active) {
        return String.format(Locale.US, "%.1f", active);
    }

    // Форматирование рекомендаций КБЖУ
    public static String formatRecommendations(double bmr, double protein, double fat, double carb) {
        return String.format(Locale.US, "Рекомендации:\n" +
                        "Калории: %.0f ккал\n" +
                        "Белки: %.1f г\n" +
                        "Жиры: %.1f г\n" +
                        "Углеводы: %.1f г",
                bmr, protein, fat, carb);
    }

    // Форматирует детали приёма пищи для отображения в списке
    public static String formatMealDetails(Meal meal) {
        StringBuilder sb = new StringBuilder();
        sb.append("Тип: ").append(meal.getTypeMeal()).append("\n");
        sb.append("Дата: ").append(meal.getDate()).append("\n");
        sb.append("Продукты:\n");
        for (ConsumedProduct cp : meal.getConsumedProducts()) {
            sb.append("  • ").append(cp.getProduct().getNameProduct())
                    .append(" (").append(cp.getWeightGrams()).append(" г) – ")
                    .append(String.format(Locale.US, "%.1f", cp.getCalories())).append(" ккал\n");
        }
        return sb.toString();
    }

    // Форматирование значения веса для поля ввода (с 1 знаком после запятой)
    public static String formatWeightValue(double weight) {
        return String.format(Locale.US, "%.1f", weight);
    }

    // Форматирование значения роста для поля ввода (с 1 знаком после запятой)
    public static String formatHeightValue(double height) {
        return String.format(Locale.US, "%.1f", height);
    }

    // Форматирование значения активности для поля ввода (с 1 знаком после запятой)
    public static String formatActiveValue(double active) {
        return String.format(Locale.US, "%.1f", active);
    }
}