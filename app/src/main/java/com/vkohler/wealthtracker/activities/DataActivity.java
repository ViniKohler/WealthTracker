package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vkohler.wealthtracker.adapters.TransactionAdapter;
import com.vkohler.wealthtracker.databinding.ActivityDataBinding;
import com.vkohler.wealthtracker.interfaces.TransactionCallback;
import com.vkohler.wealthtracker.models.Transaction;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;
import com.vkohler.wealthtracker.utilities.TransactionManager;

import java.util.List;

public class DataActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    TransactionManager transactionManager;
    private TransactionAdapter transactionAdapter;
    ActivityDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataBinding.inflate(getLayoutInflater());
        activityManager = new ActivityManager(getApplicationContext());
        logManager = new LogManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        transactionManager = new TransactionManager(getApplicationContext());

        activityManager.setLastActivity("data");
        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        setListeners();
        updateUI();
        updateRecyclerView();
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
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListeners() {
        binding.name.setOnClickListener(v -> activityManager.startActivity("profile"));
        binding.logOut.setOnClickListener(v -> logManager.logOut());
        binding.home.setOnClickListener(v -> activityManager.startActivity("home"));
        binding.addButton.setOnClickListener(v -> activityManager.startActivity("transaction"));
        binding.data.setOnClickListener(v -> activityManager.startActivity("data"));
    }

    private void updateUI() {
        try {
            binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}