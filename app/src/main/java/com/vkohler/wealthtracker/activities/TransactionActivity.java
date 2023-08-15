package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.ActivityNewTransactionBinding;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class TransactionActivity extends AppCompatActivity {

    ActivityNewTransactionBinding binding;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewTransactionBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        setListeners();
        updateHUD();
    }

    private void setListeners() {
        binding.name.setOnClickListener(v -> {
            changeActivity("profile");
        });
        binding.signOut.setOnClickListener(v -> {
            signOut();
        });
        binding.home.setOnClickListener(v -> {
            changeActivity("home");
        });
        binding.addButton.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.data.setOnClickListener(v -> {
            changeActivity("data");
        });
        binding.addTransaction.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), preferenceManager.getString(Constants.KEY_LAST_SCREEN), Toast.LENGTH_SHORT).show();
        });
    }

    private void changeActivity(String activityName) {
        Class newActivity;
        switch (activityName) {
            case "home":
                newActivity = HomeActivity.class;
                break;
            case "transaction":
                newActivity = TransactionActivity.class;
                break;
            case "data":
                newActivity = DataActivity.class;
                break;
            case "profile":
                newActivity = ProfileActivity.class;
                break;
            default:
                newActivity = SignInActivity.class;
        }
        Intent intent = new Intent(getApplicationContext(), newActivity);
        startActivity(intent);
        finish();
    }

    private void updateHUD() {
        try {
            binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String lastActivity = preferenceManager.getString(Constants.KEY_LAST_SCREEN);
        switch (lastActivity) {
            case "home":
                binding.home.setColorFilter(ContextCompat.getColor(this, R.color.green));
                break;
            case "data":
                binding.data.setColorFilter(ContextCompat.getColor(this, R.color.green));
                break;
        }
    }

    private void signOut() {
        preferenceManager.clear();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        changeActivity(preferenceManager.getString(Constants.KEY_LAST_SCREEN));
    }
}