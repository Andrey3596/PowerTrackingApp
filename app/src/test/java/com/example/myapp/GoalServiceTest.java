package com.example.myapp;

import com.example.myapp.model.Goal;
import com.example.myapp.service.GoalService;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class GoalServiceTest {

    @Test
    public void createDefaultGoal_setsCorrectValues() {
        Goal goal = GoalService.createDefaultGoal(2000, 150, 66, 200);
        assertEquals("Рекомендуемая", goal.getName());
        assertEquals(2000, goal.getCalories(), 0.01);
        assertEquals(150, goal.getProtein(), 0.01);
        assertEquals(66, goal.getFat(), 0.01);
        assertEquals(200, goal.getCarb(), 0.01);
    }

    @Test
    public void isGoalExists_returnsTrue_whenGoalExists() {
        List<Goal> goals = new ArrayList<>();
        Goal goal1 = new Goal("Цель 1", 2000.0, null, null, null);
        Goal goal2 = new Goal("Рекомендуемая", 1800.0, null, null, null);
        goals.add(goal1);
        goals.add(goal2);
        assertTrue(GoalService.isGoalExists(goals, "Рекомендуемая"));
    }

    @Test
    public void isGoalExists_returnsFalse_whenGoalDoesNotExist() {
        List<Goal> goals = new ArrayList<>();
        Goal goal = new Goal("Цель 1", 2000.0, null, null, null);
        goals.add(goal);
        assertFalse(GoalService.isGoalExists(goals, "Рекомендуемая"));
    }

    @Test
    public void isGoalExists_returnsFalse_forEmptyList() {
        List<Goal> goals = new ArrayList<>();
        assertFalse(GoalService.isGoalExists(goals, "Рекомендуемая"));
    }
}