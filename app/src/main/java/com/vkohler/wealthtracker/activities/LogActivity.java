package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.ActivityLogBinding;
import com.vkohler.wealthtracker.databinding.ActivityLogInBinding;
import com.vkohler.wealthtracker.fragments.LoginFragment;
import com.vkohler.wealthtracker.fragments.LogonFragment;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class LogActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    ActivityLogBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityManager = new ActivityManager(getApplicationContext());
        logManager = new LogManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());

        if (preferenceManager.getBoolean(Constants.KEY_IS_LOGGED_IN)) {
            activityManager.startActivity("main");
        }

        binding = ActivityLogBinding.inflate(getLayoutInflater());
        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        setListeners();
        setFragments();
    }

    private void setListeners() {
        binding.logSwitch.setOnClickListener(v -> {
            if (binding.loginFragment.getVisibility() == View.VISIBLE) {
                binding.loginFragment.setVisibility(View.GONE);
                binding.logonFragment.setVisibility(View.VISIBLE);
                binding.logSwitch.setText("Login");
            } else {
                binding.loginFragment.setVisibility(View.VISIBLE);
                binding.logonFragment.setVisibility(View.GONE);
                binding.logSwitch.setText("Create account");
            }
        });
    }

    private void setFragments() {
        Fragment loginFragment = new LoginFragment();
        Fragment logonFragment = new LogonFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(binding.loginFragment.getId(), loginFragment);
        fragmentTransaction.replace(binding.logonFragment.getId(), logonFragment);

        fragmentTransaction.commit();
    }
}