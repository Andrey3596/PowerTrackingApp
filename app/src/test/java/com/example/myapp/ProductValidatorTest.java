package com.example.myapp;

import com.example.myapp.service.ProductValidator;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProductValidatorTest {

    @Test
    public void isWeightValid_returnsTrue_forPositiveNumber() {
        assertTrue(ProductValidator.isWeightValid("100"));
        assertTrue(ProductValidator.isWeightValid("50.5"));
    }

    @Test
    public void isWeightValid_returnsFalse_forZeroOrNegative() {
        assertFalse(ProductValidator.isWeightValid("0"));
        assertFalse(ProductValidator.isWeightValid("-10"));
    }

    @Test
    public void isWeightValid_returnsFalse_forInvalidFormat() {
        assertFalse(ProductValidator.isWeightValid(""));
        assertFalse(ProductValidator.isWeightValid("abc"));
        assertFalse(ProductValidator.isWeightValid(null));
    }

    @Test
    public void isKbjuValid_returnsTrue_forPositiveNumber() {
        assertTrue(ProductValidator.isKbjuValid("350"));
        assertTrue(ProductValidator.isKbjuValid("12.5"));
    }

    @Test
    public void isKbjuValid_returnsFalse_forZeroOrNegative() {
        assertFalse(ProductValidator.isKbjuValid("0"));
        assertFalse(ProductValidator.isKbjuValid("-5"));
    }

    @Test
    public void isNewProductValid_returnsTrue_whenAllValid() {
        assertTrue(ProductValidator.isNewProductValid("Овсянка", "100", "350", "12", "6", "60"));
    }

    @Test
    public void isNewProductValid_returnsFalse_whenNameEmpty() {
        assertFalse(ProductValidator.isNewProductValid("", "100", "350", "12", "6", "60"));
        assertFalse(ProductValidator.isNewProductValid("   ", "100", "350", "12", "6", "60"));
    }

    @Test
    public void isNewProductValid_returnsFalse_whenAnyValueInvalid() {
        assertFalse(ProductValidator.isNewProductValid("Овсянка", "0", "350", "12", "6", "60"));
        assertFalse(ProductValidator.isNewProductValid("Овсянка", "100", "-350", "12", "6", "60"));
        assertFalse(ProductValidator.isNewProductValid("Овсянка", "100", "350", "", "6", "60"));
    }
}