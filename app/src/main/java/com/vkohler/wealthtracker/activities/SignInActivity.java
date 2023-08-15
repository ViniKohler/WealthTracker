package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vkohler.wealthtracker.databinding.ActivitySignInBinding;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class SignInActivity extends AppCompatActivity {

    ActivityManager activityManager;
    PreferenceManager preferenceManager;
    ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManager = new ActivityManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            activityManager.startActivity("home");
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.wealthTracker.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/ViniKohler"));
            startActivity(browserIntent);
        });
        binding.createAccount.setOnClickListener(v -> {
            activityManager.startActivity("signup");
        });
        binding.signIn.setOnClickListener(v -> {
            if (isValidSignInData()) {
                signIn();
            }
        });
    }

    private void signIn() {
        loading(true);
        binding.status.setText("");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_USERNAME, binding.username.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.password.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_USERNAME, documentSnapshot.getString(Constants.KEY_USERNAME));
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_PASSWORD, documentSnapshot.getString(Constants.KEY_PASSWORD));
                        preferenceManager.putString(Constants.KEY_PASSWORD_VISIBILITY_TUTORIAL, "0");

                        database.collection(Constants.KEY_COLLECTION_WALLETS)
                                .whereEqualTo(Constants.KEY_USER_ID, documentSnapshot.getId())
                                .get()
                                .addOnSuccessListener(walletTask -> {
                                    DocumentSnapshot walletSnapshot = walletTask.getDocuments().get(0);
                                    preferenceManager.putString(Constants.KEY_WALLET_ID, walletSnapshot.getId());
                                    preferenceManager.putString(Constants.KEY_BALANCE, walletSnapshot.getString(Constants.KEY_BALANCE));
                                })
                                .addOnCompleteListener(completeTask -> {
                                    activityManager.startActivity("home");
                                });
                    } else {
                        loading(false);
                        showToast("Unable to sign in");
                    }
                });
    }

    private boolean isValidSignInData() {
        if (binding.username.getText().toString().trim().isEmpty()) {
            showToast("Enter username");
            return false;
        } else if (binding.password.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else {
            return true;
        }
    }

    private void loading(boolean bool) {
        if (bool) {
            binding.signIn.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.signIn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    private void showToast(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
    }
}