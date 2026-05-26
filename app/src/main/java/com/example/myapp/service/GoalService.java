package com.example.myapp.service;

import com.example.myapp.model.Goal;

import java.util.List;

public class GoalService {

    public static Goal createDefaultGoal(double calories, double protein, double fat, double carb) {
        Goal goal = new Goal();
        goal.setName("Рекомендуемая");
        goal.setCalories(calories);
        goal.setProtein(protein);
        goal.setFat(fat);
        goal.setCarb(carb);
        return goal;
    }

    public static boolean isGoalExists(List<Goal> goals, String name) {
        for (Goal g : goals) {
            if (g.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}