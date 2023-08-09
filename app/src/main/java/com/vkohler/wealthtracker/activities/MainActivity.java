package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.ActivityMainBinding;
import com.vkohler.wealthtracker.fragments.DataFragment;
import com.vkohler.wealthtracker.fragments.HomeFragment;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;
    ActivityMainBinding binding;
    boolean isBalanceVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        setListeners();
        init();
    }

    private void setListeners() {
        binding.signOut.setOnClickListener(v -> {
            signOut();
        });
        binding.name.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
        });
        binding.eye.setOnClickListener(v -> {
            changeVisibility();
        });
        binding.home.setOnClickListener(v -> {
            homeFragment();
            binding.home.setColorFilter(ContextCompat.getColor(this, R.color.green));
            binding.data.setColorFilter(ContextCompat.getColor(this, R.color.text_secondary));

        });
        binding.data.setOnClickListener(v -> {
            dataFragment();
            binding.data.setColorFilter(ContextCompat.getColor(this, R.color.green));
            binding.home.setColorFilter(ContextCompat.getColor(this, R.color.text_secondary));
        });
    }

    private void init() {
        binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
        homeFragment();
    }

    private void signOut() {
        preferenceManager.clear();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
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
            binding.balance.setText("0.00");
            isBalanceVisible = true;
        }
    }

    private void homeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, homeFragment);
        fragmentTransaction.commit();
    }

    private void dataFragment() {
        DataFragment dataFragment = new DataFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, dataFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}