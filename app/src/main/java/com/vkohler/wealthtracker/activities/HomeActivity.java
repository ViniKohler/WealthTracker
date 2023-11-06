package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.adapters.TransactionAdapter;
import com.vkohler.wealthtracker.databinding.ActivityHomeBinding;
import com.vkohler.wealthtracker.interfaces.TransactionCallback;
import com.vkohler.wealthtracker.models.Transaction;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;
import com.vkohler.wealthtracker.utilities.TransactionManager;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    TransactionManager transactionManager;
    ActivityHomeBinding binding;
    private TransactionAdapter transactionAdapter;
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
        updateRecyclerView();
    }

    private void setListeners() {
        binding.name.setOnClickListener(v -> activityManager.startActivity("profile"));
        binding.logOut.setOnClickListener(v -> logManager.logOut());
        binding.eye.setOnClickListener(v -> changeVisibility());
        binding.home.setOnClickListener(v -> activityManager.startActivity("home"));
        binding.addButton.setOnClickListener(v -> activityManager.startActivity("transaction"));
        binding.data.setOnClickListener(v -> activityManager.startActivity("data"));

    }

    private void updateUI() {
        try {
            binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
            binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE));
            changeVisibility();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void updateRecyclerView() {
        binding.transactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        transactionManager.getTransactions(new TransactionCallback() {
            @Override
            public void onTransactionsLoaded(List<Transaction> transactions) {
                transactionAdapter = new TransactionAdapter(transactions);
                binding.transactionRecyclerView.setAdapter(transactionAdapter);

                if (transactions.isEmpty()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.notFound.setVisibility(View.VISIBLE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.notFound.setVisibility(View.GONE);
                }

                updateProgressBar(transactions);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProgressBar(List<Transaction> list) {
        if (list.size() != 0) {

            BigDecimal positive = BigDecimal.ZERO;
            BigDecimal negative = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;

            for (int i = 0; i <= list.size() - 1; i++) {

                BigDecimal value = new BigDecimal(list.get(i).getValue());

                if (value.compareTo(BigDecimal.ZERO) > 0) {
                    positive = positive.add(value);

                    total = total.add(value);

                } else {
                    negative = negative.add(value);

                    total = total.add(value.negate());
                }
            }

            BigDecimal percentPositive = positive.multiply(BigDecimal.valueOf(100)).divide(total, RoundingMode.UP);
            BigDecimal percentNegative = negative.multiply(BigDecimal.valueOf(100)).divide(total, RoundingMode.UP);

            int width = binding.positive.getWidth();

            int finalPositiveWidth = percentPositive.intValue() * width / 100;
            int finalNegativeWidth = percentNegative.intValue() * width / 100;

            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            format.setGroupingUsed(true);
            format.setMinimumFractionDigits(2);

            String positiveValue = "$ " + format.format(positive);
            String negativeValue = "-$ " + format.format(negative.abs());

            int green = ContextCompat.getColor(context, R.color.green);
            int red = ContextCompat.getColor(context, R.color.red);

            binding.positive.getLayoutParams().width = finalPositiveWidth;

            binding.positiveValue.setTextColor(green);
            binding.positive.setBackgroundTintList(ColorStateList.valueOf(green));
            binding.negativeValue.setTextColor(red);

            binding.positiveValue.setText(positiveValue);
            binding.negativeValue.setText(negativeValue);

            binding.positive.requestLayout();
            binding.negative.requestLayout();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}