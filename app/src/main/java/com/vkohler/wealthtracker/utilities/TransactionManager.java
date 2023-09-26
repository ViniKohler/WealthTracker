package com.vkohler.wealthtracker.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vkohler.wealthtracker.models.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionManager {

    PreferenceManager preferenceManager;
    private final Context context;
    private List<Transaction> transactions = new ArrayList<>();

    public TransactionManager(Context context) {
        this.context = context;
        preferenceManager = new PreferenceManager(context);
    }

    public void addTransaction(BigDecimal bigValue, String category, String dateTime) {
        String strBalance = preferenceManager.getString(Constants.KEY_BALANCE);
        BigDecimal bigBalance = new BigDecimal(strBalance);
        bigBalance = bigBalance.add(bigValue);
        String strFinalBalance = String.valueOf(bigBalance);

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        HashMap<String, Object> transaction = new HashMap<>();
        transaction.put(Constants.KEY_WALLET_ID, preferenceManager.getString(Constants.KEY_WALLET_ID));
        transaction.put(Constants.KEY_TRANSACTION_VALUE, String.valueOf(bigValue));
        transaction.put(Constants.KEY_TRANSACTION_CATEGORY, category);
        transaction.put(Constants.KEY_TRANSACTION_DATETIME, dateTime);
        BigDecimal finalBigBalance = bigBalance;

        database.collection(Constants.KEY_COLLECTION_TRANSACTIONS)
                .add(transaction)
                .addOnSuccessListener(transactionReference -> {
                    DocumentReference walletReference = database.collection(Constants.KEY_COLLECTION_WALLETS)
                            .document(preferenceManager.getString(Constants.KEY_WALLET_ID));

                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put(Constants.KEY_BALANCE, strFinalBalance.toString());
                    walletReference.update(updateMap)
                            .addOnSuccessListener(aVoid -> {
                                preferenceManager.putString(Constants.KEY_BALANCE, strFinalBalance);
                                Toast.makeText(context, "Balance updated successfully!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public List<Transaction> getTransactions() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String walletId = preferenceManager.getString(Constants.KEY_WALLET_ID);

        transactions.clear();

        database.collection(Constants.KEY_COLLECTION_TRANSACTIONS).whereEqualTo("walletId", walletId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Transaction transaction = documentSnapshot.toObject(Transaction.class);
                            transactions.add(transaction);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Transactions Error. " + e, Toast.LENGTH_SHORT).show();
                    }
                });
        return transactions;
    }
}
