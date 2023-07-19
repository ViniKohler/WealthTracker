package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputType;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.ActivityMainBinding;
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
            preferenceManager.clear();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        });
        binding.name.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
        });
        binding.eye.setOnClickListener(v -> {
            if (isBalanceVisible) {
                binding.visibility.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                binding.eye.setColorFilter(ContextCompat.getColor(this, R.color.red));
                binding.balance.setTextColor(getResources().getColor(R.color.text_secondary));
                binding.currency.setTextColor(getResources().getColor(R.color.text_secondary));
                binding.balance.setText("0000");
                binding.balance.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isBalanceVisible = false;
            } else {
                binding.visibility.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                binding.eye.setColorFilter(ContextCompat.getColor(this, R.color.green));
                binding.balance.setTextColor(getResources().getColor(R.color.text_primary));
                binding.currency.setTextColor(getResources().getColor(R.color.text_primary));
                binding.balance.setInputType(InputType.TYPE_CLASS_TEXT);
                binding.balance.setText("0.00");
                isBalanceVisible = true;
            }
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