package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.adapters.CategoryAdapter;
import com.vkohler.wealthtracker.databinding.ActivityTransactionBinding;
import com.vkohler.wealthtracker.interfaces.TransactionCallback;
import com.vkohler.wealthtracker.interfaces.TransactionManagerCallback;
import com.vkohler.wealthtracker.models.Transaction;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;
import com.vkohler.wealthtracker.utilities.TransactionManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    TransactionManager transactionManager;
    ActivityTransactionBinding binding;
    Context context;
    String title = "";
    String signal = "";
    String category = "";
    String strValue = "";
    BigDecimal bigValue;
    Date dateTime = new Date();
    final static int LIMIT_VALUE = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        activityManager = new ActivityManager(context);
        logManager = new LogManager(context);
        preferenceManager = new PreferenceManager(context);
        transactionManager = new TransactionManager(context);

        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        setListeners();
        init();
    }


    private void setListeners() {
        binding.back.setOnClickListener(v -> activityManager.startActivity("main"));
        binding.x.setOnClickListener(v -> clearTransactionData());
        binding.backspace.setOnClickListener(v -> {
            strValue = strValue.substring(0, strValue.length() - 1);
            if (strValue.length() == 0) {
                clearTransactionData();
            }
            updateUI();
        });
        binding.buttonPlus.setOnClickListener(v -> {
            changeStrokeColor("green");
            binding.buttonPlus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
            binding.buttonMinus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.background_dark)));
            binding.addTransaction.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            updateUI();
        });
        binding.buttonMinus.setOnClickListener(v -> {
            changeStrokeColor("red");
            binding.buttonMinus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red)));
            binding.buttonPlus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.background_dark)));
            binding.addTransaction.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            updateUI();
        });
        binding.addTransaction.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.addTransaction.setVisibility(View.GONE);

            if (category != "" && strValue != "") {

                BigDecimal value = null;

                switch (signal) {
                    case "+":
                        value = bigValue;
                        binding.progressBar.setIndeterminateTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
                        break;
                    case "-":
                        value = bigValue.negate();
                        binding.progressBar.setIndeterminateTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red)));
                }
                transactionManager.addTransaction(title, value, category, dateTime, new TransactionCallback() {

                    @Override
                    public void transactionSuccess() {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.addTransaction.setVisibility(View.VISIBLE);
                        clearTransactionData();
                    }

                    @Override
                    public void transactionFailed() {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.addTransaction.setVisibility(View.VISIBLE);
                    }
                });
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

        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            private float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float endY = event.getY();
                        if (endY - 300 > startY) {
                            activityManager.startActivity("main");
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void updateUI() {

        if (!strValue.isEmpty()) {
            bigValue = new BigDecimal(strValue).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal value = new BigDecimal(strValue);
            BigDecimal dividedValue = value.divide(BigDecimal.valueOf(100));
            bigValue = dividedValue.setScale(2, RoundingMode.HALF_UP);
            binding.value.setText(String.valueOf(bigValue));
            binding.x.setVisibility(View.VISIBLE);
            binding.backspace.setVisibility(View.VISIBLE);
        } else {
            binding.value.setText(R.string.decimalZero);
            binding.backspace.setVisibility(View.GONE);
            binding.x.setVisibility(View.GONE);
        }

        if (strValue != "" && signal != "" && category != "") {
            binding.addTransaction.setVisibility(View.VISIBLE);
        }

        if (binding.categoryRecyclerView.getAdapter() != null)
            (binding.categoryRecyclerView.getAdapter()).notifyDataSetChanged();
    }

    private void init() {
        changeStrokeColor("white");
        updateRecyclerView();
        updateUI();
    }

    private void changeStrokeColor(String color) {
        int colorResource = 0;
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.stroke_white_corner); //Don't change this

        switch (color) {
            case "white":
                colorResource = ContextCompat.getColor(context, R.color.text_primary);
                break;
            case "red":
                signal = "-";
                colorResource = ContextCompat.getColor(context, R.color.red);
                break;
            case "green":
                signal = "+";
                colorResource = ContextCompat.getColor(context, R.color.green);
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

    private void updateRecyclerView() {
        String[] categoryList = {"Bill", "Clothing", "Debts", "Education", "Entertainment", "Food", "Health", "Insurance", "Investment", "Maintenance", "Salary", "Savings", "Taxes", "Transport", "Traveling"};

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.categoryRecyclerView.setLayoutManager(layoutManager);

        CategoryAdapter adapter = new CategoryAdapter(Arrays.asList(categoryList), new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String categoryString) {
                category = categoryString;
                updateUI();
            }
        });
        binding.categoryRecyclerView.setAdapter(adapter);
    }

    private void clearTransactionData() {
        strValue = "";
        signal = "";
        category = "";
        updateUI();
        changeStrokeColor("white");
        binding.buttonMinus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.background_dark)));
        binding.buttonPlus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.background_dark)));
        binding.addTransaction.setVisibility(View.GONE);
        if (binding.categoryRecyclerView.getAdapter() != null) {
            ((CategoryAdapter) binding.categoryRecyclerView.getAdapter()).clearSelection();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activityManager.startActivity("main");
    }
}