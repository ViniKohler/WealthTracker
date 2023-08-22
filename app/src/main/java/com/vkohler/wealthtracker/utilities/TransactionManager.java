package com.vkohler.wealthtracker.utilities;

import android.content.Context;
import android.widget.Toast;

import java.math.BigDecimal;

public class TransactionManager {

    PreferenceManager preferenceManager;
    private final Context context;

    public TransactionManager(Context context) {
        this.context = context;
        preferenceManager = new PreferenceManager(context);
    }

    public void addTransaction (BigDecimal bigValue, String category) {
            Toast.makeText(context, String.valueOf(bigValue) + "\n" + category, Toast.LENGTH_SHORT).show();
    }
}
