package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vkohler.wealthtracker.databinding.ActivityMyProfileBinding;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

import java.util.HashMap;

public class MyProfileActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;
    ActivityMyProfileBinding binding;
    private boolean isPasswordHidden = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void init() {
        String currentUsername = preferenceManager.getString(Constants.KEY_USERNAME);
        String currentName = preferenceManager.getString(Constants.KEY_NAME);
        String currentPassword = preferenceManager.getString(Constants.KEY_PASSWORD);

        binding.name.setText(currentName);
        binding.username.setText(currentUsername);
        binding.name.setText(currentName);
        binding.password.setText(currentPassword);

        binding.newUsername.setText(currentUsername);
        binding.newPassword.setText(currentPassword);
        binding.newName.setText(currentName);
        binding.newConfirmPassword.setText(currentPassword);

        if (preferenceManager.getString(Constants.KEY_PASSWORD_VISIBILITY_TUTORIAL).equals("1")) {
            binding.passwordHint.setVisibility(View.GONE);
            binding.passwordText.setText("password");
            preferenceManager.putString(Constants.KEY_PASSWORD_VISIBILITY_TUTORIAL, "1");
        }
    }

    private void setListeners() {
        binding.password.setOnClickListener(v -> {
            if (isPasswordHidden) {
                binding.password.setInputType(InputType.TYPE_CLASS_TEXT);
                isPasswordHidden = false;
            } else {
                binding.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isPasswordHidden = true;
            }
            binding.passwordHint.setVisibility(View.GONE);
            preferenceManager.putString(Constants.KEY_PASSWORD_VISIBILITY_TUTORIAL, "1");
            binding.passwordText.setText("password");
        });
        binding.back.setOnClickListener(v -> onBackPressed());
        binding.edit.setOnClickListener(v -> {
            binding.editPanel.setVisibility(View.VISIBLE);
            binding.edit.setVisibility(View.GONE);
        });
        binding.update.setOnClickListener(v -> {
            updateUser();
            binding.editPanel.setVisibility(View.GONE);
            binding.edit.setVisibility(View.VISIBLE);
        });
    }

    private void updateUser() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference userReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updatedUser = new HashMap<>();

        String currentUsername = preferenceManager.getString(Constants.KEY_USERNAME);
        String currentName = preferenceManager.getString(Constants.KEY_NAME);
        String currentPassword = preferenceManager.getString(Constants.KEY_PASSWORD);

        String newUsername = binding.newUsername.getText().toString();
        String newName = binding.newName.getText().toString();
        String newPassword = binding.newPassword.getText().toString();

        if (!currentUsername.equals(newUsername)) {
            userReference.update(Constants.KEY_USERNAME, newUsername)
                    .addOnSuccessListener(v -> {
                        preferenceManager.putString(Constants.KEY_USERNAME, newUsername);
                        showToast("Username updated successfully");
                        init();
                    }).addOnFailureListener(e -> {
                        showToast(e.getMessage());
                    });
        }

        if (!currentName.equals(newName)) {
            userReference.update(Constants.KEY_NAME, newName)
                    .addOnSuccessListener(v -> {
                        preferenceManager.putString(Constants.KEY_NAME, newName);
                        showToast("Name updated successfully");
                        init();
                    })
                    .addOnFailureListener(e -> {
                        showToast(e.getMessage());
                    });
        }

        if (!currentPassword.equals(newPassword)) {
            userReference.update(Constants.KEY_PASSWORD, newPassword)
                    .addOnSuccessListener(v -> {
                        preferenceManager.putString(Constants.KEY_PASSWORD, newPassword);
                        showToast("Password updated successfully");
                        init();
                    })
                    .addOnFailureListener(e -> {
                        showToast(e.getMessage());
                    });
        }
    }

    private void showToast(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
    }
}