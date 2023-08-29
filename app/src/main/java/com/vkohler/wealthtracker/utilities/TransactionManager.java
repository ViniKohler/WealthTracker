package com.vkohler.wealthtracker.utilities;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransactionManager {

    PreferenceManager preferenceManager;
    private final Context context;

    public TransactionManager(Context context) {
        this.context = context;
        preferenceManager = new PreferenceManager(context);
    }

    public void addTransaction (BigDecimal bigValue, String category, String dateTime) {
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
}
