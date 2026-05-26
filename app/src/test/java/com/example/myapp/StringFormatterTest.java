package com.example.myapp;

import com.example.myapp.model.Product;
import com.example.myapp.service.StringFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringFormatterTest {

    @Test
    public void formatConsumedProduct_returnsCorrectFormat() {
        String result = StringFormatter.formatConsumedProduct("Яблоко", 150.0, 78.0);
        // Используем точку, так как в StringFormatter теперь Locale.US
        assertEquals("Яблоко – 150.0 г: 78.0 ккал", result);
    }

    @Test
    public void formatProductKbju_returnsCorrectFormat() {
        Product product = new Product("Овсянка", 350.0, 12.0, 6.0, 60.0);
        String result = StringFormatter.formatProductKbju(product);
        assertTrue(result.contains("350.0 ккал"));
        assertTrue(result.contains("12.0 б"));
        assertTrue(result.contains("6.0 ж"));
        assertTrue(result.contains("60.0 у"));
    }

    @Test
    public void formatWeightValue_returnsOneDecimal() {
        assertEquals("70.5", StringFormatter.formatWeightValue(70.5));
        assertEquals("70.0", StringFormatter.formatWeightValue(70.0));
    }

    @Test
    public void formatHeightValue_returnsOneDecimal() {
        assertEquals("175.5", StringFormatter.formatHeightValue(175.5));
        assertEquals("180.0", StringFormatter.formatHeightValue(180.0));
    }

    @Test
    public void formatActiveValue_returnsOneDecimal() {
        assertEquals("1.6", StringFormatter.formatActiveValue(1.55));
    }

    @Test
    public void formatSummary_returnsCorrectFormat() {
        String result = StringFormatter.formatSummary(5, 2000.0, 150.0, 66.0, 200.0);
        assertTrue(result.contains("Сводка за 5 дн."));
        assertTrue(result.contains("2000 ккал"));
        assertTrue(result.contains("150/66/200 г"));
    }

    @Test
    public void formatRecommendations_returnsCorrectFormat() {
        String result = StringFormatter.formatRecommendations(2000.0, 150.0, 66.0, 200.0);
        assertTrue(result.contains("Калории: 2000 ккал"));
        assertTrue(result.contains("Белки: 150.0 г"));
        assertTrue(result.contains("Жиры: 66.0 г"));
        assertTrue(result.contains("Углеводы: 200.0 г"));
    }
}