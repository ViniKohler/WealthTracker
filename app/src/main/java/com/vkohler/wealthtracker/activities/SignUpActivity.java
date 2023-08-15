package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.vkohler.wealthtracker.databinding.ActivitySignUpBinding;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    ActivityManager activityManager;
    PreferenceManager preferenceManager;
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManager = new ActivityManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.login.setOnClickListener(v -> onBackPressed());
        binding.signUp.setOnClickListener(v -> {
            if (isValidSignUpData()) {
                signUp();
            }
        });
    }

    private boolean isValidSignUpData() {
        if (binding.username.getText().toString().trim().isEmpty()) {
            showToast("Enter username");
            return false;
        } else if (binding.password.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else if (binding.confirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter confirm password");
            return false;
        } else if (!binding.password.getText().toString().equals(binding.confirmPassword.getText().toString())) {
            showToast("Password & confirm password must be the same");
            return false;
        } else {
            return true;
        }
    }

    private void signUp() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_USERNAME, binding.username.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.password.getText().toString());
        user.put(Constants.KEY_NAME, binding.name.getText().toString());
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(userReference -> {
                    preferenceManager.putString(Constants.KEY_USER_ID, userReference.getId());
                    preferenceManager.putString(Constants.KEY_USERNAME, binding.username.getText().toString());
                    preferenceManager.putString(Constants.KEY_NAME, binding.name.getText().toString());
                    preferenceManager.putString(Constants.KEY_PASSWORD, binding.password.getText().toString());
                    preferenceManager.putString(Constants.KEY_PASSWORD_VISIBILITY_TUTORIAL, "0");
                    HashMap<String, Object> wallet = new HashMap<>();
                    wallet.put(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                    wallet.put(Constants.KEY_BALANCE, "0.00");
                    database.collection(Constants.KEY_COLLECTION_WALLETS)
                            .add(wallet)
                            .addOnSuccessListener(walletReference -> {
                                preferenceManager.putString(Constants.KEY_WALLET_ID, walletReference.getId());
                                preferenceManager.putString(Constants.KEY_BALANCE, "0.00");
                                activityManager.startActivity("home");
                            }).addOnFailureListener(e -> {
                                loading(false);
                                showToast(e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    loading(false);
                    showToast(e.getMessage());
                });
    }

    private void loading(boolean bool) {
        if (bool) {
            binding.signUp.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.signUp.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    private void showToast(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
    }
}