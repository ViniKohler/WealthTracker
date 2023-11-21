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
import com.vkohler.wealthtracker.fragments.BalanceFragment;
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
    }

    private void setListeners() {
        binding.name.setOnClickListener(v -> activityManager.startActivity("profile"));
        binding.logOut.setOnClickListener(v -> logManager.logOut());
        binding.home.setOnClickListener(v -> activityManager.startActivity("home"));
        binding.addButton.setOnClickListener(v -> activityManager.startActivity("transaction"));
        binding.data.setOnClickListener(v -> activityManager.startActivity("data"));
        binding.balanceBarFragment.setOnClickListener(v -> activityManager.startActivity("data"));
    }

    private void updateUI() {
        try {
            binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Fragment balanceFragment = new BalanceFragment();
        Fragment balanceBarFragment = new BalanceBarFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentBalance = fragmentManager.beginTransaction();
        FragmentTransaction fragmentBalanceBar = fragmentManager.beginTransaction();

        fragmentBalance.replace(binding.balanceFragment.getId(), balanceFragment);
        fragmentBalanceBar.replace(binding.balanceBarFragment.getId(), balanceBarFragment);

        fragmentBalance.commit();
        fragmentBalanceBar.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}