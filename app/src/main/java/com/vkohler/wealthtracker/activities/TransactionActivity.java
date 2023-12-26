package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;

import com.vkohler.wealthtracker.databinding.ActivityTransactionBinding;
import com.vkohler.wealthtracker.fragments.AddTransactionFragment;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;
import com.vkohler.wealthtracker.utilities.TransactionManager;

import java.math.BigDecimal;

public class TransactionActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    TransactionManager transactionManager;
    ActivityTransactionBinding binding;
    Context context;

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
    }

    private void init() {
        Fragment addTransactionFragment = new AddTransactionFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(binding.addTransactionFragment.getId(), addTransactionFragment);

        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activityManager.startActivity("main");
    }
}