package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vkohler.wealthtracker.databinding.ActivityDataBinding;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class DataActivity extends AppCompatActivity {

    ActivityManager activityManager;
    PreferenceManager preferenceManager;
    ActivityDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataBinding.inflate(getLayoutInflater());
        activityManager = new ActivityManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        preferenceManager.putString(Constants.KEY_LAST_SCREEN, "data");
        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        setListeners();
        updateHUD();
    }

    private void setListeners() {
        binding.name.setOnClickListener(v -> {
            activityManager.startActivity("profile");
        });
        binding.signOut.setOnClickListener(v -> {
            signOut();
        });
        binding.home.setOnClickListener(v -> {
            activityManager.startActivity("home");
        });
        binding.addButton.setOnClickListener(v -> {
            activityManager.startActivity("transaction");
        });
        binding.data.setOnClickListener(v -> {
            activityManager.startActivity("data");
        });
    }

    private void signOut() {
        preferenceManager.clear();
        activityManager.startActivity("signin");
    }

    private void updateHUD() {
        try {
            binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}