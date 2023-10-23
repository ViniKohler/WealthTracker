package com.vkohler.wealthtracker.models;

import java.util.Date;

public class Transaction {
    private String walletId, category, value;
    private Date dateTime;

    public Transaction() {
    }

    public Transaction(String title, String category, String value) {
        this.category = category;
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public String getValue() {
        return value;
    }

    public Date getDateTime() {
        return dateTime;
    }
}
