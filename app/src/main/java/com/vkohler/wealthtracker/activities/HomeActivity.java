package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Toast;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.adapters.TransactionAdapter;
import com.vkohler.wealthtracker.databinding.ActivityHomeBinding;
import com.vkohler.wealthtracker.models.Transaction;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;
import com.vkohler.wealthtracker.utilities.TransactionManager;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    TransactionManager transactionManager;
    ActivityHomeBinding binding;
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        updateRecycleView();
    }

    private void setListeners() {
        binding.name.setOnClickListener(v -> {
            activityManager.startActivity("profile");
        });
        binding.logOut.setOnClickListener(v -> {
            logManager.logOut();
        });
        binding.eye.setOnClickListener(v -> {
            changeVisibility();
        });
        binding.home.setOnClickListener(v -> {
            activityManager.startActivity("home");
        });
        binding.addButton.setOnClickListener(v -> {
            activityManager.startActivity("transaction");
        });
        binding.data.setOnClickListener(v -> {
            activityManager.startActivity("data");
        });

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
            binding.visibility.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.text_secondary)));
            binding.eye.setColorFilter(ContextCompat.getColor(this, R.color.text_secondary));
            binding.balance.setTextColor(getResources().getColor(R.color.text_secondary));
            binding.currency.setTextColor(getResources().getColor(R.color.text_secondary));
            binding.balance.setText("••••");
            binding.currency.setText("$ ");
            preferenceManager.putBoolean(Constants.KEY_IS_BALANCE_VISIBLE, false);
        } else {
            String balance = preferenceManager.getString(Constants.KEY_BALANCE);
            if (!balance.contains("-")) {
                binding.visibility.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                binding.eye.setColorFilter(ContextCompat.getColor(this, R.color.green));
                binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE));
                binding.currency.setText("$ ");
            } else {
                binding.visibility.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                binding.eye.setColorFilter(ContextCompat.getColor(this, R.color.red));
                binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE).replace("-", ""));
                binding.currency.setText("-$ ");
            }
            binding.balance.setTextColor(getResources().getColor(R.color.text_primary));
            binding.currency.setTextColor(getResources().getColor(R.color.text_primary));
            preferenceManager.putBoolean(Constants.KEY_IS_BALANCE_VISIBLE, true);
        }
    }

    private void updateRecycleView() {
        binding.transactionsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction("Transaction 1", "Category 1", "100"));
        transactions.add(new Transaction("Transaction 2", "Category 2", "200"));

//        transactions = transactionManager.getTransactions();

        transactionAdapter = new TransactionAdapter(transactions);
        binding.transactionsRecycleView.setAdapter(transactionAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}