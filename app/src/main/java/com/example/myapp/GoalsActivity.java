package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapp.database.DatabaseHelper;
import com.example.myapp.model.Goal;
import com.example.myapp.service.GoalValidator;
import com.example.myapp.service.StringFormatter;

import java.util.List;

public class GoalsActivity extends BaseActivity {

    private DatabaseHelper dbHelper;
    private List<Goal> goalsList;
    private ArrayAdapter<Goal> goalAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        applyBottomPaddingToNavPanel();

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewGoals);
        updateGoalsList();

        Button buttonCreateGoal = findViewById(R.id.buttonCreateGoal);
        buttonCreateGoal.setOnClickListener(v -> createGoal());
    }

    private void createGoal() {
        EditText editTextKal = findViewById(R.id.editTextKal);
        EditText editTextProtein = findViewById(R.id.editTextProtein);
        EditText editTextFat = findViewById(R.id.editTextFat);
        EditText editTextCarb = findViewById(R.id.editTextCarb);
        EditText editTextNameGoal = findViewById(R.id.editTextNameGoal);

        String name = editTextNameGoal.getText().toString().trim();
        if (!GoalValidator.isNameValid(name)) {
            Toast.makeText(this, "Введите название цели", Toast.LENGTH_SHORT).show();
            return;
        }

        Double kal = parseDoubleOrNull(editTextKal);
        Double protein = parseDoubleOrNull(editTextProtein);
        Double fat = parseDoubleOrNull(editTextFat);
        Double carb = parseDoubleOrNull(editTextCarb);

        if (!GoalValidator.hasAtLeastOneValue(kal, protein, fat, carb)) {
            Toast.makeText(this, "Укажите хотя бы один целевой параметр (калории, белки, жиры, углеводы)", Toast.LENGTH_SHORT).show();
            return;
        }

        Goal newGoal = new Goal(name, kal, protein, fat, carb);
        dbHelper.saveGoal(newGoal);
        updateGoalsList();

        clearGoalFields(editTextNameGoal, editTextKal, editTextProtein, editTextFat, editTextCarb);
        Toast.makeText(GoalsActivity.this, "Цель \"" + name + "\" добавлена", Toast.LENGTH_SHORT).show();
    }

    private Double parseDoubleOrNull(EditText editText) {
        String valueStr = editText.getText().toString().trim();
        if (valueStr.isEmpty()) return null;
        try {
            return Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void clearGoalFields(EditText name, EditText kal, EditText protein, EditText fat, EditText carb) {
        name.setText("");
        kal.setText("");
        protein.setText("");
        fat.setText("");
        carb.setText("");
    }

    private void updateGoalsList() {
        goalsList = dbHelper.loadAllGoals();
        if (goalAdapter == null) {
            goalAdapter = new GoalsAdapter(this, goalsList);
            listView.setAdapter(goalAdapter);
        } else {
            goalAdapter.clear();
            goalAdapter.addAll(goalsList);
            goalAdapter.notifyDataSetChanged();
        }
    }

    private class GoalsAdapter extends ArrayAdapter<Goal> {
        private Context context;
        private List<Goal> goals;

        public GoalsAdapter(Context context, List<Goal> goals) {
            super(context, R.layout.goal_item, goals);
            this.context = context;
            this.goals = goals;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.goal_item, parent, false);
            }

            Goal goal = goals.get(position);
            TextView textView = view.findViewById(R.id.textViewGoal);
            Button buttonDelete = view.findViewById(R.id.buttonDelete);

            textView.setText(goal.toString());
            buttonDelete.setOnClickListener(v -> {
                dbHelper.deleteGoal(goal.getId());
                goals.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Цель \"" + goal.getName() + "\" удалена", Toast.LENGTH_SHORT).show();
            });
            return view;
        }
    }
}