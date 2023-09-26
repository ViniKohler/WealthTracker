package com.vkohler.wealthtracker.models;

public class Transaction {
    private String walletId, title, category, value, dateTime;

    public Transaction() {
    }

    public Transaction(String title, String category, String value) {
        this.title = title;
        this.category = category;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getValue() {
        return value;
    }
}
