package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import com.vkohler.wealthtracker.databinding.ActivityMainBinding;
import com.vkohler.wealthtracker.fragments.BalanceBarFragment;
import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.fragments.BalanceFragment;
import com.vkohler.wealthtracker.fragments.TransactionsListFragment;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;
import com.vkohler.wealthtracker.utilities.TransactionManager;


public class MainActivity extends AppCompatActivity {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    TransactionManager transactionManager;
    ActivityMainBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        activityManager = new ActivityManager(getApplicationContext());
        logManager = new LogManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        transactionManager = new TransactionManager(getApplicationContext());

        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        setListeners();
        updateUI();
    }

    private void setListeners() {
        binding.name.setOnClickListener(v -> activityManager.startActivity("profile"));
        binding.logOut.setOnClickListener(v -> logManager.logOut());
        binding.home.setOnClickListener(v -> {
            preferenceManager.putString(Constants.KEY_LAST_ACTIVITY, "home");
            binding.transactionsListFragment.setVisibility(View.GONE);
            binding.balanceFragment.setVisibility(View.VISIBLE);
            binding.balanceBarFragment.setVisibility(View.VISIBLE);
            binding.home.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
            binding.data.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_secondary)));

        });
        binding.addButton.setOnClickListener(v -> activityManager.startActivity("transaction"));
        binding.data.setOnClickListener(v -> {
            preferenceManager.putString(Constants.KEY_LAST_ACTIVITY, "data");
            binding.balanceFragment.setVisibility(View.GONE);
            binding.balanceBarFragment.setVisibility(View.GONE);
            binding.transactionsListFragment.setVisibility(View.VISIBLE);
            binding.home.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_secondary)));
            binding.data.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
        });
        binding.balanceBarFragment.setOnClickListener(v -> {
            binding.balanceFragment.setVisibility(View.GONE);
            binding.balanceBarFragment.setVisibility(View.GONE);
            binding.transactionsListFragment.setVisibility(View.VISIBLE);
            binding.home.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_secondary)));
            binding.data.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
        });
    }

    private void updateUI() {
        try {
            binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Fragment balanceFragment = new BalanceFragment();
        Fragment balanceBarFragment = new BalanceBarFragment();
        Fragment transactionsListFragment = new TransactionsListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(binding.balanceFragment.getId(), balanceFragment);
        fragmentTransaction.replace(binding.balanceBarFragment.getId(), balanceBarFragment);
        fragmentTransaction.replace(binding.transactionsListFragment.getId(), transactionsListFragment);

        fragmentTransaction.commit();

        switch (activityManager.getLastActivity()) {
            case "home":
                binding.home.performClick();
                break;
            case "data":
                binding.data.performClick();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}