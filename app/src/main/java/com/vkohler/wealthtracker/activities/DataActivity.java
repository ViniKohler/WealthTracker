package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.vkohler.wealthtracker.databinding.ActivityDataBinding;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class DataActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;
    ActivityDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        preferenceManager.putString(Constants.KEY_LAST_SCREEN, "data");
        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        setListeners();
        updateHUD();
    }

    private void setListeners() {
        binding.name.setOnClickListener(v -> {
            changeActivity("profile");
        });
        binding.signOut.setOnClickListener(v -> {
            signOut();
        });
        binding.home.setOnClickListener(v -> {
            changeActivity("home");
        });
        binding.addButton.setOnClickListener(v -> {
            changeActivity("transaction");
        });
        binding.data.setOnClickListener(v -> {
            changeActivity("data");
        });
    }

    private void changeActivity(String activityName) {
        Class newActivity;
        switch (activityName) {
            case "home":
                newActivity = HomeActivity.class;
                break;
            case "transaction":
                newActivity = TransactionActivity.class;
                break;
            case "data":
                newActivity = DataActivity.class;
                break;
            case "profile":
                newActivity = ProfileActivity.class;
                break;
            default:
                newActivity = SignInActivity.class;
        }
        Intent intent = new Intent(getApplicationContext(), newActivity);
        startActivity(intent);
        finish();
    }

    private void signOut() {
        preferenceManager.clear();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
    }

    private void updateHUD() {
        try {
            binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}