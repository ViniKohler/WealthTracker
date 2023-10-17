package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.vkohler.wealthtracker.databinding.ActivityLogOnBinding;
import com.vkohler.wealthtracker.interfaces.LogCallback;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class LogOnActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    ActivityLogOnBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityManager = new ActivityManager(getApplicationContext());
        logManager = new LogManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding = ActivityLogOnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
    }

    private void setListeners() {
        binding.login.setOnClickListener(v -> {
            activityManager.startActivity("login");
        });

        binding.logOn.setOnClickListener(v -> {
            String username = binding.username.getText().toString();
            String name = binding.name.getText().toString();
            String password = binding.password.getText().toString();
            String confirmPassword = binding.confirmPassword.getText().toString();

            binding.logOn.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);

            logManager.logOn(username, name, password, confirmPassword, new LogCallback() {
                @Override
                public void actionDone() {
                    binding.logOn.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }
            });

        });
    }
}