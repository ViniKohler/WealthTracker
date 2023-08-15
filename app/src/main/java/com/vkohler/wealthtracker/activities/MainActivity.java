package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Toast;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.ActivityMainBinding;
import com.vkohler.wealthtracker.fragments.AddTransactionFragment;
import com.vkohler.wealthtracker.fragments.DataFragment;
import com.vkohler.wealthtracker.fragments.HomeFragment;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;


public class MainActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;
    ActivityMainBinding binding;
    boolean isBalanceVisible = true;
    boolean isAddTransactionVisible = false;
    int lastFragment = 1; // 1-Home 2-Data 3-Transaction
    Fragment activeFragment;

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
            signOut();
        });
        binding.name.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
        });
        binding.eye.setOnClickListener(v -> {
            changeVisibility();
        });
        binding.addButton.setOnClickListener(v -> {
//            if (!isAddTransactionVisible) {
//                isAddTransactionVisible = true;
//                switchFragments(3);
//            } else {
//                lastFragment();
//            }
            switchFragments(0);
        });
        binding.home.setOnClickListener(v -> {
            switchFragments(1);
            binding.home.setColorFilter(ContextCompat.getColor(this, R.color.green));
            binding.data.setColorFilter(ContextCompat.getColor(this, R.color.text_secondary));
            lastFragment = 0;
            isAddTransactionVisible = false;
        });
        binding.data.setOnClickListener(v -> {
            switchFragments(2);
            binding.data.setColorFilter(ContextCompat.getColor(this, R.color.green));
            binding.home.setColorFilter(ContextCompat.getColor(this, R.color.text_secondary));
            lastFragment = 1;
            isAddTransactionVisible = false;
        });

    }

    private void init() {
        binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE));
        switchFragments(1);
    }

    private void signOut() {
        preferenceManager.clear();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
    }

    private void changeVisibility() {
        if (isBalanceVisible) {
            binding.visibility.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            binding.eye.setColorFilter(ContextCompat.getColor(this, R.color.red));
            binding.balance.setTextColor(getResources().getColor(R.color.text_secondary));
            binding.currency.setTextColor(getResources().getColor(R.color.text_secondary));
            binding.balance.setText("••••");
            isBalanceVisible = false;
        } else {
            binding.visibility.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            binding.eye.setColorFilter(ContextCompat.getColor(this, R.color.green));
            binding.balance.setTextColor(getResources().getColor(R.color.text_primary));
            binding.currency.setTextColor(getResources().getColor(R.color.text_primary));
            binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE));
            isBalanceVisible = true;
        }
    }


    private void switchFragments(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (index) {
            case 0:
                fragmentManager.beginTransaction().remove(activeFragment).commit();
                break;
            case 1:
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.fragmentContainer, homeFragment);
                activeFragment = homeFragment;
                break;
            case 2:
                DataFragment dataFragment = new DataFragment();
                fragmentTransaction.replace(R.id.fragmentContainer, dataFragment);
                activeFragment = dataFragment;
                break;
            case 3:
                AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
                fragmentTransaction.replace(R.id.fragmentContainer, addTransactionFragment);
                activeFragment = addTransactionFragment;
                break;
        }
        fragmentTransaction.commit();
    }

    private void lastFragment() {
        switch (lastFragment) {
            case 1:
                isAddTransactionVisible = false;
                switchFragments(1);
                break;
            case 2:
                isAddTransactionVisible = false;
                switchFragments(2);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void showToast(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
    }
}