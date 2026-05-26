package com.example.myapp;

import com.example.myapp.model.User;
import com.example.myapp.service.CalorieCalculator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CalorieCalculatorTest {

    @Test
    public void bmrForMale_isCalculatedCorrectly() {
        User user = new User(30, 80, 180, "м", "Тест", 1.55);
        CalorieCalculator calculator = new CalorieCalculator(user);
        double expected = ((10 * 80) + (6.25 * 180) - (5 * 30) + 5) * 1.55;
        assertEquals(expected, calculator.getBMR(), 0.01);
    }

    @Test
    public void bmrForFemale_isCalculatedCorrectly() {
        User user = new User(30, 65, 170, "ж", "Тест", 1.55);
        CalorieCalculator calculator = new CalorieCalculator(user);
        double expected = ((10 * 65) + (6.25 * 170) - (5 * 30) - 161) * 1.55;
        assertEquals(expected, calculator.getBMR(), 0.01);
    }

    @Test
    public void aimProtein_is30PercentOfBMRdividedBy4() {
        User user = new User(25, 70, 175, "м", "Тест", 1.55);
        CalorieCalculator calculator = new CalorieCalculator(user);
        double expectedProtein = (calculator.getBMR() * 0.3) / 4;
        assertEquals(expectedProtein, calculator.getAimProtein(), 0.01);
    }

    @Test
    public void aimFat_is30PercentOfBMRdividedBy9() {
        User user = new User(25, 70, 175, "м", "Тест", 1.55);
        CalorieCalculator calculator = new CalorieCalculator(user);
        double expectedFat = (calculator.getBMR() * 0.3) / 9;
        assertEquals(expectedFat, calculator.getAimFat(), 0.01);
    }

    @Test
    public void aimCarb_is40PercentOfBMRdividedBy4() {
        User user = new User(25, 70, 175, "м", "Тест", 1.55);
        CalorieCalculator calculator = new CalorieCalculator(user);
        double expectedCarb = (calculator.getBMR() * 0.4) / 4;
        assertEquals(expectedCarb, calculator.getAimCarb(), 0.01);
    }

    @Test
    public void setArg_recalculatesValues_whenUserDataChanges() {
        User user = new User(25, 70, 175, "м", "Тест", 1.55);
        CalorieCalculator calculator = new CalorieCalculator(user);
        double oldBMR = calculator.getBMR();
        user.setWeight(75);
        user.setActive(1.375);
        calculator.setArg(user);
        double newBMR = calculator.getBMR();
        assertNotEquals(oldBMR, newBMR, 0.01);
    }

    @Test
    public void genderChange_switchesFormula() {
        User user = new User(30, 80, 180, "м", "Тест", 1.55);
        CalorieCalculator calculator = new CalorieCalculator(user);
        double maleBMR = calculator.getBMR();
        user.setGender("ж");
        calculator.setArg(user);
        double femaleBMR = calculator.getBMR();
        assertTrue(femaleBMR < maleBMR);
    }
}