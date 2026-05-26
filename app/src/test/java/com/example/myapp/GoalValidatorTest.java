package com.example.myapp;

import com.example.myapp.service.GoalValidator;
import org.junit.Test;
import static org.junit.Assert.*;

public class GoalValidatorTest {

    @Test
    public void isNameValid_returnsTrue_forNonEmptyName() {
        assertTrue(GoalValidator.isNameValid("Похудение"));
    }

    @Test
    public void isNameValid_returnsFalse_forEmptyName() {
        assertFalse(GoalValidator.isNameValid(""));
        assertFalse(GoalValidator.isNameValid("   "));
        assertFalse(GoalValidator.isNameValid(null));
    }

    @Test
    public void hasAtLeastOneValue_returnsTrue_whenCaloriesProvided() {
        assertTrue(GoalValidator.hasAtLeastOneValue(2000.0, null, null, null));
    }

    @Test
    public void hasAtLeastOneValue_returnsTrue_whenProteinProvided() {
        assertTrue(GoalValidator.hasAtLeastOneValue(null, 150.0, null, null));
    }

    @Test
    public void hasAtLeastOneValue_returnsTrue_whenFatProvided() {
        assertTrue(GoalValidator.hasAtLeastOneValue(null, null, 50.0, null));
    }

    @Test
    public void hasAtLeastOneValue_returnsTrue_whenCarbProvided() {
        assertTrue(GoalValidator.hasAtLeastOneValue(null, null, null, 250.0));
    }

    @Test
    public void hasAtLeastOneValue_returnsFalse_whenAllNull() {
        assertFalse(GoalValidator.hasAtLeastOneValue(null, null, null, null));
    }

    @Test
    public void hasAtLeastOneValue_returnsFalse_whenAllZero() {
        assertFalse(GoalValidator.hasAtLeastOneValue(0.0, 0.0, 0.0, 0.0));
    }
}