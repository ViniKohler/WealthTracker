package com.vkohler.wealthtracker.interfaces;

import com.vkohler.wealthtracker.models.Transaction;

import java.util.List;

public interface TransactionManagerCallback {
    void onTransactionsLoaded(List<Transaction> transactions);
    void onError(String errorMessage);

}
