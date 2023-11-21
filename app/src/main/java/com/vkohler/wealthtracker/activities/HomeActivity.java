package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import com.vkohler.wealthtracker.fragments.BalanceBarFragment;
import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.ActivityHomeBinding;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;
import com.vkohler.wealthtracker.utilities.TransactionManager;


public class HomeActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    TransactionManager transactionManager;
    ActivityHomeBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        activityManager = new ActivityManager(getApplicationContext());
        logManager = new LogManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        transactionManager = new TransactionManager(getApplicationContext());

        activityManager.setLastActivity("home");
        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        setListeners();
        updateUI();
//        updateProgressBar();
    }

    private void setListeners() {
        binding.name.setOnClickListener(v -> activityManager.startActivity("profile"));
        binding.logOut.setOnClickListener(v -> logManager.logOut());
        binding.eye.setOnClickListener(v -> changeVisibility());
        binding.home.setOnClickListener(v -> activityManager.startActivity("home"));
        binding.addButton.setOnClickListener(v -> activityManager.startActivity("transaction"));
        binding.data.setOnClickListener(v -> activityManager.startActivity("data"));
        binding.balanceBarFragment.setOnClickListener(v -> activityManager.startActivity("data"));
    }

    private void updateUI() {
        try {
            binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
            binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE));
            changeVisibility();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Fragment fragment = new BalanceBarFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentBalanceBar = fragmentManager.beginTransaction();
        fragmentBalanceBar.replace(binding.balanceBarFragment.getId(), fragment);
        fragmentBalanceBar.commit();
    }

    private void changeVisibility() {
        Boolean visibility = preferenceManager.getBoolean(Constants.KEY_IS_BALANCE_VISIBLE);
        if (visibility) {
            binding.visibility.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_secondary)));
            binding.eye.setColorFilter(ContextCompat.getColor(context, R.color.text_secondary));
            binding.balance.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
            binding.currency.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
            binding.balance.setText("••••");
            binding.currency.setText("$ ");
            preferenceManager.putBoolean(Constants.KEY_IS_BALANCE_VISIBLE, false);
        } else {
            String balance = preferenceManager.getString(Constants.KEY_BALANCE);
            if (!balance.contains("-")) {
                binding.visibility.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
                binding.eye.setColorFilter(ContextCompat.getColor(this, R.color.green));
                binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE));
                binding.currency.setText("$ ");
            } else {
                binding.visibility.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red)));
                binding.eye.setColorFilter(ContextCompat.getColor(this, R.color.red));
                binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE).replace("-", ""));
                binding.currency.setText("-$ ");
            }
            binding.balance.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
            binding.currency.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
            preferenceManager.putBoolean(Constants.KEY_IS_BALANCE_VISIBLE, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}