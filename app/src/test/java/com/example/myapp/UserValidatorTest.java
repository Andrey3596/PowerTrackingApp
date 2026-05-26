package com.example.myapp;

import com.example.myapp.service.UserValidator;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserValidatorTest {

    @Test
    public void isNameValid_returnsTrue_forNonEmptyName() {
        assertTrue(UserValidator.isNameValid("Иван"));
    }

    @Test
    public void isNameValid_returnsFalse_forEmptyName() {
        assertFalse(UserValidator.isNameValid(""));
        assertFalse(UserValidator.isNameValid("   "));
        assertFalse(UserValidator.isNameValid(null));
    }

    @Test
    public void isGenderValid_returnsTrue_forMaleOrFemale() {
        assertTrue(UserValidator.isGenderValid("м"));
        assertTrue(UserValidator.isGenderValid("ж"));
    }

    @Test
    public void isGenderValid_returnsFalse_forInvalidGender() {
        assertFalse(UserValidator.isGenderValid("male"));
        assertFalse(UserValidator.isGenderValid("female"));
        assertFalse(UserValidator.isGenderValid(""));
        assertFalse(UserValidator.isGenderValid(null));
    }

    @Test
    public void isAgeValid_returnsTrue_forPositiveNumber() {
        assertTrue(UserValidator.isAgeValid("25"));
        assertTrue(UserValidator.isAgeValid("1"));
    }

    @Test
    public void isAgeValid_returnsFalse_forZeroOrNegative() {
        assertFalse(UserValidator.isAgeValid("0"));
        assertFalse(UserValidator.isAgeValid("-5"));
    }

    @Test
    public void isAgeValid_returnsFalse_forInvalidFormat() {
        assertFalse(UserValidator.isAgeValid("abc"));
        assertFalse(UserValidator.isAgeValid(""));
        assertFalse(UserValidator.isAgeValid(null));
    }

    @Test
    public void isWeightValid_returnsTrue_forPositiveNumber() {
        assertTrue(UserValidator.isWeightValid("70.5"));
        assertTrue(UserValidator.isWeightValid("70"));
    }

    @Test
    public void isWeightValid_returnsFalse_forZeroOrNegative() {
        assertFalse(UserValidator.isWeightValid("0"));
        assertFalse(UserValidator.isWeightValid("-10.5"));
    }

    @Test
    public void isHeightValid_returnsTrue_forPositiveNumber() {
        assertTrue(UserValidator.isHeightValid("175.5"));
        assertTrue(UserValidator.isHeightValid("180"));
    }

    @Test
    public void isHeightValid_returnsFalse_forZeroOrNegative() {
        assertFalse(UserValidator.isHeightValid("0"));
        assertFalse(UserValidator.isHeightValid("-20"));
    }

    @Test
    public void isActiveValid_returnsTrue_forPositiveNumber() {
        assertTrue(UserValidator.isActiveValid("1.55"));
        assertTrue(UserValidator.isActiveValid("1.2"));
    }

    @Test
    public void isActiveValid_returnsFalse_forZeroOrNegative() {
        assertFalse(UserValidator.isActiveValid("0"));
        assertFalse(UserValidator.isActiveValid("-0.5"));
    }

    @Test
    public void isAllFieldsValid_returnsTrue_whenAllValid() {
        assertTrue(UserValidator.isAllFieldsValid("Иван", "м", "25", "70", "175", "1.55"));
    }

    @Test
    public void isAllFieldsValid_returnsFalse_whenAnyInvalid() {
        assertFalse(UserValidator.isAllFieldsValid("", "м", "25", "70", "175", "1.55"));
        assertFalse(UserValidator.isAllFieldsValid("Иван", "x", "25", "70", "175", "1.55"));
        assertFalse(UserValidator.isAllFieldsValid("Иван", "м", "-5", "70", "175", "1.55"));
        assertFalse(UserValidator.isAllFieldsValid("Иван", "м", "25", "0", "175", "1.55"));
    }
}