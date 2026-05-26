package com.example.myapp;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public abstract class BaseActivity extends AppCompatActivity {

    private static final long DOUBLE_CLICK_DELAY_MS = 500;
    private long lastClickTime = 0;


    protected boolean isDoubleClick() {
        long now = System.currentTimeMillis();
        if (now - lastClickTime < DOUBLE_CLICK_DELAY_MS) {
            return true;
        }
        lastClickTime = now;
        return false;
    }


    protected void applyBottomPaddingToNavPanel() {
        View navPanel = findViewById(R.id.bottomNavLayout);
        if (navPanel != null) {
            ViewCompat.setOnApplyWindowInsetsListener(navPanel, (v, insets) -> {
                int bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
                v.setPadding(0, 0, 0, bottom);
                return insets;
            });
        }
    }



    public void profileActivity(View view) {
        if (isDoubleClick()) return;
        if (!(this instanceof ProfileActivity)) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else {
            Toast.makeText(this, "Вы уже в профиле", Toast.LENGTH_SHORT).show();
        }
    }

    public void addMealActivity(View view) {
        if (isDoubleClick()) return;
        if (!(this instanceof AddMealActivity)) {
            startActivity(new Intent(this, AddMealActivity.class));
        } else {
            Toast.makeText(this, "Вы уже на экране добавления", Toast.LENGTH_SHORT).show();
        }
    }

    public void showMealsActivity(View view) {
        if (isDoubleClick()) return;
        if (!(this instanceof ShowMealsActivity)) {
            startActivity(new Intent(this, ShowMealsActivity.class));
        } else {
            Toast.makeText(this, "Вы уже на экране просмотра", Toast.LENGTH_SHORT).show();
        }
    }

    public void analizeMeansActivity(View view) {
        if (isDoubleClick()) return;
        if (!(this instanceof AnalizeMeansActivity)) {
            startActivity(new Intent(this, AnalizeMeansActivity.class));
        } else {
            Toast.makeText(this, "Вы уже на экране анализа", Toast.LENGTH_SHORT).show();
        }
    }

    public void editProductsActivity(View view) {
        if (isDoubleClick()) return;
        if (!(this instanceof editProductsActivity)) {
            startActivity(new Intent(this, editProductsActivity.class));
        } else {
            Toast.makeText(this, "Вы уже в редактировании продуктов", Toast.LENGTH_SHORT).show();
        }
    }
}