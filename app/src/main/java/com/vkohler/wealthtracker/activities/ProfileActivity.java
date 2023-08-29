package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.vkohler.wealthtracker.databinding.ActivityProfileBinding;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class ProfileActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    ActivityProfileBinding binding;
    private boolean isPasswordHidden = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        activityManager = new ActivityManager(getApplicationContext());
        logManager = new LogManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());

        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        setListeners();
        updateUI();
    }

    private void setListeners() {
        binding.password.setOnClickListener(v -> {
            if (isPasswordHidden) {
                binding.password.setInputType(InputType.TYPE_CLASS_TEXT);
                isPasswordHidden = false;
                preferenceManager.putString(Constants.KEY_PASSWORD_VISIBILITY_TUTORIAL, "done");
            } else {
                binding.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isPasswordHidden = true;
            }
            binding.passwordHint.setVisibility(View.GONE);
            binding.passwordText.setText("password");
        });
        binding.edit.setOnClickListener(v -> activityManager.startActivity("updateProfile"));
        binding.delete.setOnClickListener(v -> {
            binding.deletePanel.setVisibility(View.VISIBLE);
            binding.edit.setVisibility(View.GONE);
            binding.delete.setVisibility(View.GONE);
        });
        binding.deleteUser.setOnClickListener(v -> logManager.deleteLog());
        binding.cancelDelete.setOnClickListener(v -> {
            binding.deletePanel.setVisibility(View.GONE);
            binding.edit.setVisibility(View.VISIBLE);
            binding.delete.setVisibility(View.VISIBLE);
        });
        binding.back.setOnClickListener(v -> {
            String lastActivity = preferenceManager.getString(Constants.KEY_LAST_ACTIVITY);
            activityManager.startActivity(lastActivity);
        });
    }

    private void updateUI() {
        String currentName = preferenceManager.getString(Constants.KEY_NAME);
        String currentUsername = preferenceManager.getString(Constants.KEY_USERNAME);
        String currentPassword = preferenceManager.getString(Constants.KEY_PASSWORD);

        binding.name.setText(currentName);
        binding.username.setText(currentUsername);
        binding.password.setText(currentPassword);

        if (preferenceManager.getString(Constants.KEY_PASSWORD_VISIBILITY_TUTORIAL).equals("done")) {
            binding.passwordHint.setVisibility(View.GONE);
            binding.passwordText.setText("password");
        }
    }
}