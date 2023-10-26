package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.ActivityProfileUpdateBinding;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class ProfileUpdateActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    ActivityProfileUpdateBinding binding;
    Context context;

    String username, name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        binding = com.vkohler.wealthtracker.databinding.ActivityProfileUpdateBinding.inflate(getLayoutInflater());
        activityManager = new ActivityManager(context);
        logManager = new LogManager(context);
        preferenceManager = new PreferenceManager(context);

        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        updateUI();
        setListeners();
    }

    private void setListeners() {
        binding.update.setOnClickListener(v -> updateUser());
        binding.cancel.setOnClickListener(v -> activityManager.startActivity("profile"));
        binding.username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateInputUI();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateInputUI();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateInputUI();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateInputUI();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void updateUser() {
        String newUsername = binding.username.getText().toString();
        String newName = binding.name.getText().toString();
        String newPassword = binding.password.getText().toString();
        String confirmNewPassword = binding.confirmPassword.getText().toString();

        logManager.updateLog(newUsername, newName, newPassword, confirmNewPassword);
    }

    private void updateInputUI() {
        String newUsername = binding.username.getText().toString();
        String newName = binding.name.getText().toString();
        String newPassword = binding.password.getText().toString();
        String confirmNewPassword = binding.confirmPassword.getText().toString();

        if (!username.equals(newUsername)) {
            binding.username.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
        } else {
            binding.username.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_secondary)));
        }
        if (!name.equals(newName)) {
            binding.name.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
        } else {
            binding.name.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_secondary)));
        }

        if (!password.equals(newPassword) || !password.equals(confirmNewPassword)) {
            binding.password.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
            binding.confirmPassword.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
        } else {
            binding.password.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_secondary)));
            binding.confirmPassword.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_secondary)));
        }

        if (!newPassword.equals(confirmNewPassword) || newPassword.isEmpty()) {
            binding.password.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red)));
            binding.confirmPassword.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red)));
        }
    }

    private void updateUI() {
        username = preferenceManager.getString(Constants.KEY_USERNAME);
        name = preferenceManager.getString(Constants.KEY_NAME);
        password = preferenceManager.getString(Constants.KEY_PASSWORD);

        binding.username.setText(username);
        binding.name.setText(name);
        binding.password.setText(password);
        binding.confirmPassword.setText(password);
    }
}