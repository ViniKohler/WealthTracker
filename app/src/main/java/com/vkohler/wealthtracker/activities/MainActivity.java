package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.vkohler.wealthtracker.databinding.ActivityMainBinding;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;
    ActivityMainBinding binding;

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
            preferenceManager.clear();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        });
        binding.name.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
        });
    }

    private void init() {
        binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}