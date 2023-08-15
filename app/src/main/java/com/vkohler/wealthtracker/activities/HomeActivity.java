package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Toast;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.ActivityHomeBinding;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;


public class HomeActivity extends AppCompatActivity {

    ActivityManager activityManager;
    PreferenceManager preferenceManager;
    ActivityHomeBinding binding;
    boolean isBalanceVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        activityManager = new ActivityManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        preferenceManager.putString(Constants.KEY_LAST_SCREEN, "home");
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
        binding.eye.setOnClickListener(v -> {
            changeVisibility();
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

    private void updateHUD() {
        try {
            binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
            binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signOut() {
        preferenceManager.clear();
        activityManager.startActivity("signin");
    }

    private void changeVisibility() {
        if (isBalanceVisible) {
            binding.visibility.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            binding.eye.setColorFilter(ContextCompat.getColor(this, R.color.red));
            binding.balance.setTextColor(getResources().getColor(R.color.text_secondary));
            binding.currency.setTextColor(getResources().getColor(R.color.text_secondary));
            binding.balance.setText("••••");
            isBalanceVisible = false;
        } else {
            binding.visibility.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            binding.eye.setColorFilter(ContextCompat.getColor(this, R.color.green));
            binding.balance.setTextColor(getResources().getColor(R.color.text_primary));
            binding.currency.setTextColor(getResources().getColor(R.color.text_primary));
            binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE));
            isBalanceVisible = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHUD();
    }

    private void showToast(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
    }
}