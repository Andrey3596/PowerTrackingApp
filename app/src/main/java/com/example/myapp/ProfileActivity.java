package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ProfileActivity extends BaseActivity {
    private ReadProfileFragment frameLayoutReadProfile;
    private UpdateProfileFragment frameLayoutUpdateProfile;
    private boolean isRead = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        applyBottomPaddingToNavPanel();

        initFragments();
        setupUpdateButton();
        setupGoalsButton();
    }

    private void initFragments() {
        frameLayoutReadProfile = new ReadProfileFragment();
        frameLayoutUpdateProfile = new UpdateProfileFragment();
        setNewFragment(frameLayoutReadProfile);
    }

    private void setupUpdateButton() {
        Button buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(v -> {
            if (isRead) {
                buttonUpdate.setText("Сохранить");
                setNewFragment(frameLayoutUpdateProfile);
                isRead = false;
            } else {
                if (frameLayoutUpdateProfile.saveUserData()) {
                    frameLayoutReadProfile = new ReadProfileFragment();
                    buttonUpdate.setText("Изменить");
                    setNewFragment(frameLayoutReadProfile);
                    isRead = true;
                    showToast("Данные сохранены");
                }
            }
        });
    }

    private void setupGoalsButton() {
        Button buttonGoals = findViewById(R.id.buttonGoals);
        buttonGoals.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, GoalsActivity.class);
            startActivity(intent);
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void setNewFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameProfile, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}