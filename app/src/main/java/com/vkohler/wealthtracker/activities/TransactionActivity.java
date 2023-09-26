package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.ActivityTransactionBinding;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;
import com.vkohler.wealthtracker.utilities.TransactionManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class TransactionActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    TransactionManager transactionManager;
    ActivityTransactionBinding binding;
    String strValue;
    BigDecimal bigValue;
    String signal;
    String category = "default";
    String dateTime = "default";
    final static int LIMIT_VALUE = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManager = new ActivityManager(getApplicationContext());
        logManager = new LogManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        transactionManager = new TransactionManager(getApplicationContext());

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
        binding.x.setOnClickListener(v -> {
            strValue = "";
            signal = null;
            category = null;
            updateUI();
            binding.x.setVisibility(View.GONE);
            changeStrokeColor("white");
            binding.buttonMinus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.background_dark)));
            binding.buttonPlus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.background_dark)));
            binding.addTransaction.setVisibility(View.GONE);
        });
        binding.buttonPlus.setOnClickListener(v -> {
            changeStrokeColor("green");
            binding.buttonPlus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            binding.buttonMinus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.background_dark)));
            binding.addTransaction.setBackgroundColor(getResources().getColor(R.color.green));
            updateUI();
        });
        binding.buttonMinus.setOnClickListener(v -> {
            changeStrokeColor("red");
            binding.buttonMinus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            binding.buttonPlus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.background_dark)));
            binding.addTransaction.setBackgroundColor(getResources().getColor(R.color.red));
            updateUI();
        });
        binding.addTransaction.setOnClickListener(v -> {
            if (category != null && !strValue.isEmpty()) {
                switch (signal) {
                    case "+":
                        transactionManager.addTransaction(bigValue, category, dateTime);
                        break;
                    case "-":
                        transactionManager.addTransaction(bigValue.negate(), category, dateTime);
                }
                binding.x.performClick();
            }
        });

        binding.button0.setOnClickListener(v -> {
            if (strValue.length() < LIMIT_VALUE && strValue.length() > 0) {
                strValue += "0";
            }
            updateUI();
        });
        binding.button1.setOnClickListener(v -> {
            if (strValue.length() < LIMIT_VALUE) {
                strValue += "1";
            }
            updateUI();
        });
        binding.button2.setOnClickListener(v -> {
            if (strValue.length() < LIMIT_VALUE) {
                strValue += "2";
            }
            updateUI();
        });
        binding.button3.setOnClickListener(v -> {
            if (strValue.length() < LIMIT_VALUE) {
                strValue += "3";
            }
            updateUI();
        });
        binding.button4.setOnClickListener(v -> {
            if (strValue.length() < LIMIT_VALUE) {
                strValue += "4";
            }
            updateUI();
        });
        binding.button5.setOnClickListener(v -> {
            if (strValue.length() < LIMIT_VALUE) {
                strValue += "5";
            }
            updateUI();
        });
        binding.button6.setOnClickListener(v -> {
            if (strValue.length() < LIMIT_VALUE) {
                strValue += "6";
            }
            updateUI();
        });
        binding.button7.setOnClickListener(v -> {
            if (strValue.length() < LIMIT_VALUE) {
                strValue += "7";
            }
            updateUI();
        });
        binding.button8.setOnClickListener(v -> {
            if (strValue.length() < LIMIT_VALUE) {
                strValue += "8";
            }
            updateUI();
        });
        binding.button9.setOnClickListener(v -> {
            if (strValue.length() < LIMIT_VALUE) {
                strValue += "9";
            }
            updateUI();
        });
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                activityManager.startLastActivity();
            }
        });
    }

    private void updateUI() {
        //TODO:
        // verificar a condição if abaixo.
        // Se não funcionar, trocar por strValue.equals("")
        if (!strValue.isEmpty()) {
            bigValue = new BigDecimal(strValue).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            binding.value.setText(String.valueOf(bigValue));
            binding.x.setVisibility(View.VISIBLE);
        } else {
            binding.value.setText("0.00");
        }
        if (!strValue.isEmpty() && signal != null) {
            binding.addTransaction.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        binding.swipeRefresh.setColorSchemeResources(R.color.no_color);
        binding.swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.no_color);
        changeStrokeColor("white");
        strValue = "";
    }

    private void changeStrokeColor(String color) {
        int colorResource = 0;
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.stroked_corner);

        switch (color) {
            case "white":
                colorResource = getResources().getColor(R.color.text_primary);
                break;
            case "red":
                signal = "-";
                colorResource = getResources().getColor(R.color.red);
                break;
            case "green":
                signal = "+";
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activityManager.startLastActivity();
    }
}