package com.vkohler.wealthtracker.utilities;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vkohler.wealthtracker.interfaces.LogCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogManager {
    ActivityManager activityManager;
    PreferenceManager preferenceManager;
    private final Context context;

    public LogManager(Context context) {
        this.context = context;
        activityManager = new ActivityManager(context);
        preferenceManager = new PreferenceManager(context);
    }

    public void logIn(String username, String password, LogCallback callback) {
        if (authLogIn(username, password)) {
            callback.setMessage("Loading user");
            FirebaseFirestore database = FirebaseFirestore.getInstance();

            database.collection(Constants.KEY_COLLECTION_USERS)
                    .whereEqualTo(Constants.KEY_USERNAME, username)
                    .whereEqualTo(Constants.KEY_PASSWORD, password)
                    .get()
                    .addOnCompleteListener(userTask -> {
                        if (userTask.isSuccessful() && userTask.getResult() != null
                                && userTask.getResult().getDocuments().size() > 0) {
                            callback.setMessage("Loading wallet");
                            DocumentSnapshot documentSnapshot = userTask.getResult().getDocuments().get(0);

                            preferenceManager.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
                            preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                            preferenceManager.putString(Constants.KEY_USERNAME, documentSnapshot.getString(Constants.KEY_USERNAME));
                            preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                            preferenceManager.putString(Constants.KEY_PASSWORD, documentSnapshot.getString(Constants.KEY_PASSWORD));
                            preferenceManager.putBoolean(Constants.KEY_IS_BALANCE_VISIBLE, true);

                            database.collection(Constants.KEY_COLLECTION_WALLETS)
                                    .whereEqualTo(Constants.KEY_USER_ID, documentSnapshot.getId())
                                    .get()
                                    .addOnSuccessListener(walletTask -> {
                                        DocumentSnapshot walletSnapshot = walletTask.getDocuments().get(0);

                                        preferenceManager.putString(Constants.KEY_WALLET_ID, walletSnapshot.getId());
                                        preferenceManager.putString(Constants.KEY_BALANCE, walletSnapshot.getString(Constants.KEY_BALANCE));
                                        callback.actionDone();
                                    })
                                    .addOnCompleteListener(completeTask -> activityManager.startActivity("main"));
                        }
                    }).addOnFailureListener(e -> {
                        callback.setMessage("Unable to log in");
                        showToast(e.getMessage());
                    });
        }
    }

    private boolean authLogIn(String username, String password) {
        if (username.trim().isEmpty()) {
            showToast("Enter username");
            return false;
        } else if (password.trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else {
            return true;
        }
    }

    public void logOn(String username, String name, String password, String confirmPassword, LogCallback callback) {
        if (authLogOn(username, name, password, confirmPassword)) {
            callback.setMessage("Logging in");
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            HashMap<String, Object> user = new HashMap<>();
            user.put(Constants.KEY_USERNAME, username);
            user.put(Constants.KEY_NAME, name);
            user.put(Constants.KEY_PASSWORD, password);
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .add(user)
                    .addOnSuccessListener(userReference -> {

                        preferenceManager.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, userReference.getId());
                        preferenceManager.putString(Constants.KEY_USERNAME, username);
                        preferenceManager.putString(Constants.KEY_NAME, name);
                        preferenceManager.putString(Constants.KEY_PASSWORD, password);
                        preferenceManager.putBoolean(Constants.KEY_IS_BALANCE_VISIBLE, true);

                        HashMap<String, Object> wallet = new HashMap<>();
                        wallet.put(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                        wallet.put(Constants.KEY_BALANCE, "0.00");
                        database.collection(Constants.KEY_COLLECTION_WALLETS)
                                .add(wallet)
                                .addOnSuccessListener(walletReference -> {

                                    preferenceManager.putString(Constants.KEY_WALLET_ID, walletReference.getId());
                                    preferenceManager.putString(Constants.KEY_BALANCE, "0.00");

                                    callback.actionDone();

                                    activityManager.startActivity("main");
                                }).addOnFailureListener(e -> {
                                    callback.actionDone();
                                    callback.setMessage("Unable to log on");
                                    showToast(e.getMessage());
                                    logOut();
                                });
                    })
                    .addOnFailureListener(e -> {
                        callback.setMessage("Unable to log on");
                        showToast(e.getMessage());
                    });
        }
    }

    private boolean authLogOn(String username, String name, String password, String confirmPassword) {
        if (username.isEmpty()) {
            showToast("Enter a username");
            return false;
        } else if (name.isEmpty()) {
            showToast("Enter a name");
            return false;
        } else if (password.isEmpty()) {
            showToast("Enter a password");
            return false;
        } else if (confirmPassword.isEmpty()) {
            showToast("Confirm your password");
            return false;
        } else if (!password.equals(confirmPassword)) {
            showToast("Passwords are not the same");
            return false;
        } else {
            return true;
        }
    }

    public void logOut() {
        preferenceManager.clear();
        activityManager.startActivity("login");
    }

    public void updateLog(String newUsername, String newName, String newPassword, String confirmNewPassword, LogCallback callback) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference userReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));

        String currentUsername = preferenceManager.getString(Constants.KEY_USERNAME);
        String currentName = preferenceManager.getString(Constants.KEY_NAME);
        String currentPassword = preferenceManager.getString(Constants.KEY_PASSWORD);

        if (currentUsername.equals(newUsername)
                && currentName.equals(newName)
                && currentPassword.equals(newPassword)) {
            showToast("Nothing to update");
        } else {
            if (!currentUsername.equals(newUsername)) {
                updateFieldAndShowToast(userReference, Constants.KEY_USERNAME, newUsername, "Username", callback);
            }

            if (!currentName.equals(newName)) {
                updateFieldAndShowToast(userReference, Constants.KEY_NAME, newName, "Name", callback);
            }

            if (!currentPassword.equals(newPassword) && newPassword.equals(confirmNewPassword)) {
                updateFieldAndShowToast(userReference, Constants.KEY_PASSWORD, newPassword, "Password", callback);
            } else if (!currentPassword.equals(newPassword)) {
                showToast("Passwords are not the same");
            }
        }
    }

    private void updateFieldAndShowToast(DocumentReference userReference, String fieldKey, String newValue, String successMessage, LogCallback callback) {
        userReference.update(fieldKey, newValue)
                .addOnSuccessListener(v -> {
                    preferenceManager.putString(fieldKey, newValue);
                    showToast(successMessage + " updated successfully");
                    callback.actionDone();
                })
                .addOnCompleteListener(v -> {
                    activityManager.startActivity("profile");
                    callback.actionDone();
                })
                .addOnFailureListener(e -> {
                    showToast(e.getMessage());
                    callback.actionDone();
                });
    }

    public void deleteLog() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        CollectionReference transactionsRef = database.collection(Constants.KEY_COLLECTION_TRANSACTIONS);
        Query transactionsQuery = transactionsRef.whereEqualTo(Constants.KEY_WALLET_ID, preferenceManager.getString(Constants.KEY_WALLET_ID));

        transactionsQuery.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> transactions = queryDocumentSnapshots.getDocuments();
                    List<Task<Void>> deleteTasks = new ArrayList<>();

                    for (DocumentSnapshot transactionDoc : transactions) {
                        deleteTasks.add(transactionDoc.getReference().delete());
                    }

                    Tasks.whenAllComplete(deleteTasks)
                            .addOnSuccessListener(results -> {
                                DocumentReference walletReference = database.collection(Constants.KEY_COLLECTION_WALLETS)
                                        .document(preferenceManager.getString(Constants.KEY_WALLET_ID));
                                walletReference.delete()
                                        .addOnSuccessListener(v -> {
                                            DocumentReference userReference = database.collection(Constants.KEY_COLLECTION_USERS)
                                                    .document(preferenceManager.getString(Constants.KEY_USER_ID));
                                            userReference.delete()
                                                    .addOnSuccessListener(task -> {
                                                        logOut();
                                                        showToast("User deleted successfully");
                                                    })
                                                    .addOnFailureListener(e -> showToast(e.getMessage()));
                                        })
                                        .addOnFailureListener(e -> showToast(e.getMessage()));
                            })
                            .addOnFailureListener(e -> showToast(e.getMessage()));
                })
                .addOnFailureListener(e -> showToast(e.getMessage()));
    }

    private void showToast(String m) {
        Toast.makeText(context, m, Toast.LENGTH_SHORT).show();
    }
}
