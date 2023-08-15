package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.ActivityNewTransactionBinding;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class TransactionActivity extends AppCompatActivity {

    ActivityManager activityManager;
    PreferenceManager preferenceManager;
    ActivityNewTransactionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManager = new ActivityManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivityNewTransactionBinding.inflate(getLayoutInflater());
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
            onBackPressed();
        });
        binding.data.setOnClickListener(v -> {
            activityManager.startActivity("data");
        });
        binding.addTransaction.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), preferenceManager.getString(Constants.KEY_LAST_SCREEN), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateHUD() {
        try {
            binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String lastActivity = preferenceManager.getString(Constants.KEY_LAST_SCREEN);
        switch (lastActivity) {
            case "home":
                binding.home.setColorFilter(ContextCompat.getColor(this, R.color.green));
                break;
            case "data":
                binding.data.setColorFilter(ContextCompat.getColor(this, R.color.green));
                break;
        }
    }

    private void signOut() {
        preferenceManager.clear();
        activityManager.startActivity("signin");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activityManager.startActivity(preferenceManager.getString(Constants.KEY_LAST_SCREEN));
    }
}