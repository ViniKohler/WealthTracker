package com.vkohler.wealthtracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vkohler.wealthtracker.databinding.ActivityMyProfileBinding;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        if (preferenceManager.getString(Constants.KEY_PASSWORD_VISIBILITY_TUTORIAL).equals("done")) {
            binding.passwordHint.setVisibility(View.GONE);
            binding.passwordText.setText("password");
        }
    }

    private void setListeners() {
        binding.password.setOnClickListener(v -> {
            if (isPasswordHidden) {
                binding.password.setInputType(InputType.TYPE_CLASS_TEXT);
                isPasswordHidden = false;
                preferenceManager.putString(Constants.KEY_PASSWORD_VISIBILITY_TUTORIAL, "done");
            } else {
                binding.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isPasswordHidden = true;
            }
            binding.passwordHint.setVisibility(View.GONE);
            binding.passwordText.setText("password");
        });
        binding.edit.setOnClickListener(v -> {
            binding.editPanel.setVisibility(View.VISIBLE);
            binding.edit.setVisibility(View.GONE);
            binding.delete.setVisibility(View.GONE);
        });
        binding.update.setOnClickListener(v -> {
            updateUser();
        });
        binding.cancelUpdate.setOnClickListener(v -> {
            binding.editPanel.setVisibility(View.GONE);
            binding.edit.setVisibility(View.VISIBLE);
            binding.delete.setVisibility(View.VISIBLE);
        });
        binding.delete.setOnClickListener(v -> {
            binding.deletePanel.setVisibility(View.VISIBLE);
            binding.edit.setVisibility(View.GONE);
            binding.delete.setVisibility(View.GONE);
        });
        binding.deleteUser.setOnClickListener(v -> {
            deleteUser();
        });
        binding.cancelDelete.setOnClickListener(v -> {
            binding.deletePanel.setVisibility(View.GONE);
            binding.edit.setVisibility(View.VISIBLE);
            binding.delete.setVisibility(View.VISIBLE);
        });
        binding.back.setOnClickListener(v -> onBackPressed());
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

        if (currentUsername.equals(newUsername) &&
                currentName.equals(newName) && currentPassword.equals(newPassword)) {
            showToast("Nothing to update");
        }

        if (!currentUsername.equals(newUsername)) {
            userReference.update(Constants.KEY_USERNAME, newUsername)
                    .addOnSuccessListener(v -> {
                        preferenceManager.putString(Constants.KEY_USERNAME, newUsername);
                        showToast("Username updated successfully");
                        binding.editPanel.setVisibility(View.GONE);
                        binding.edit.setVisibility(View.VISIBLE);
                        binding.delete.setVisibility(View.VISIBLE);
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
                        binding.editPanel.setVisibility(View.GONE);
                        binding.edit.setVisibility(View.VISIBLE);
                        binding.delete.setVisibility(View.VISIBLE);
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
                        binding.editPanel.setVisibility(View.GONE);
                        binding.edit.setVisibility(View.VISIBLE);
                        binding.delete.setVisibility(View.VISIBLE);
                        init();

                    })
                    .addOnFailureListener(e -> {
                        showToast(e.getMessage());
                    });
        }
    }

    private void deleteUser() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Primeiro, exclua as transações associadas à wallet do usuário
        CollectionReference transactionsRef = database.collection(Constants.KEY_COLLECTION_TRANSACTIONS);
        Query transactionsQuery = transactionsRef.whereEqualTo(Constants.KEY_WALLET_ID, preferenceManager.getString(Constants.KEY_WALLET_ID));

        transactionsQuery.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> transactions = queryDocumentSnapshots.getDocuments();
                    List<Task<Void>> deleteTasks = new ArrayList<>();

                    for (DocumentSnapshot transactionDoc : transactions) {
                        deleteTasks.add(transactionDoc.getReference().delete());
                    }

                    // Aguarde a conclusão de todas as tarefas de exclusão
                    Tasks.whenAllComplete(deleteTasks)
                            .addOnSuccessListener(results -> {
                                // Todas as transações foram excluídas, agora você pode prosseguir para excluir a wallet e o usuário
                                DocumentReference walletReference = database.collection(Constants.KEY_COLLECTION_WALLETS)
                                        .document(preferenceManager.getString(Constants.KEY_WALLET_ID));
                                walletReference.delete()
                                        .addOnSuccessListener(v -> {
                                            DocumentReference userReference = database.collection(Constants.KEY_COLLECTION_USERS)
                                                    .document(preferenceManager.getString(Constants.KEY_USER_ID));
                                            userReference.delete()
                                                    .addOnSuccessListener(task -> {
                                                        preferenceManager.clear();
                                                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                                        finish();
                                                        showToast("User deleted successfully");
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        showToast(e.getMessage());
                                                    });
                                        })
                                        .addOnFailureListener(e -> {
                                            showToast(e.getMessage());
                                        });
                            })
                            .addOnFailureListener(e -> {
                                showToast(e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    showToast(e.getMessage());
                });
    }

    private void showToast(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
    }
}