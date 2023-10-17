package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.vkohler.wealthtracker.databinding.ActivityLogInBinding;
import com.vkohler.wealthtracker.interfaces.LogCallback;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class LogInActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    ActivityLogInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityManager = new ActivityManager(getApplicationContext());
        logManager = new LogManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());

        if (preferenceManager.getBoolean(Constants.KEY_IS_LOGGED_IN)) {
            activityManager.startActivity("home");
        }

        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        setListeners();
    }

    private void setListeners() {
        binding.wealthTracker.setOnClickListener(v -> {
            activityManager.startURLActivity("http://github.com/ViniKohler");
        });

        binding.createAccount.setOnClickListener(v -> {
            activityManager.startActivity("logon");
        });

        binding.logIn.setOnClickListener(v -> {
            binding.logIn.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);

            logManager.logIn(binding.username.getText().toString(), binding.password.getText().toString(), new LogCallback() {
                @Override
                public void actionDone() {
                    binding.logIn.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }
            });

        });
    }
}