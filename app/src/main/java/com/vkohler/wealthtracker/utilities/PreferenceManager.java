package com.vkohler.wealthtracker.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

//    public void updateBalance(Context context, String transactionString) {
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        PreferenceManager preferenceManager = new PreferenceManager(context);
//        DocumentReference walletReference = database.collection(Constants.KEY_COLLECTION_WALLETS)
//                .document(preferenceManager.getString(Constants.KEY_WALLET_ID));
//        HashMap<String, Object> updateWallet = new HashMap<>();
//
//        String currentBalanceString = preferenceManager.getString(Constants.KEY_BALANCE);
//
//        BigDecimal currentBalance = new BigDecimal(currentBalanceString);
//        BigDecimal transaction = new BigDecimal(transactionString);
//
//        BigDecimal newBalance = currentBalance.add(transaction);
//        String newBalanceString = newBalance.toString();
//
//        Toast.makeText(context, newBalanceString, Toast.LENGTH_SHORT).show();
//        preferenceManager.putString(Constants.KEY_BALANCE, newBalanceString);
//    }
}
