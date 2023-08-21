package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.ActivityTransactionBinding;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class TransactionActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    ActivityTransactionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManager = new ActivityManager(getApplicationContext());
        logManager = new LogManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        setListeners();
        init();
    }

    private void setListeners() {
        binding.back.setOnClickListener(v -> {
            activityManager.startLastActivity();
        });
        binding.buttonPlus.setOnClickListener(v -> {
            changeStrokeColor("green");
            binding.buttonPlus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            binding.buttonMinus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.background_dark)));
            binding.addTransaction.setBackgroundColor(getResources().getColor(R.color.green));
            binding.addTransaction.setVisibility(View.VISIBLE);
        });
        binding.buttonMinus.setOnClickListener(v -> {
            changeStrokeColor("red");
            binding.buttonMinus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            binding.buttonPlus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.background_dark)));
            binding.addTransaction.setBackgroundColor(getResources().getColor(R.color.red));
            binding.addTransaction.setVisibility(View.VISIBLE);
        });

        binding.swipeRefresh.setColorSchemeResources(R.color.no_color);
        binding.swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.no_color);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                activityManager.startLastActivity();
            }
        });
    }

    private void changeStrokeColor(String color) {
        int colorResource = 0;
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.stroked_corner);
        
        switch (color) {
            case "white":
                colorResource = getResources().getColor(R.color.text_primary);
                break;
            case "red":
                colorResource = getResources().getColor(R.color.red);
                break;
            case "green":
                colorResource = getResources().getColor(R.color.green);
                break;
        }
        
        if (backgroundDrawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) backgroundDrawable;
            gradientDrawable.setStroke(5, colorResource);
            binding.button0.setBackground(gradientDrawable);
            binding.button1.setBackground(gradientDrawable);
            binding.button2.setBackground(gradientDrawable);
            binding.button3.setBackground(gradientDrawable);
            binding.button4.setBackground(gradientDrawable);
            binding.button5.setBackground(gradientDrawable);
            binding.button6.setBackground(gradientDrawable);
            binding.button7.setBackground(gradientDrawable);
            binding.button8.setBackground(gradientDrawable);
            binding.button9.setBackground(gradientDrawable);
        }
    }

    private void init() {
        changeStrokeColor("white");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activityManager.startLastActivity();
    }
}