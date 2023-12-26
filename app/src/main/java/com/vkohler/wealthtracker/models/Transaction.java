package com.vkohler.wealthtracker.models;

import java.util.Date;

public class Transaction {
    private String walletId, title, category, value;
    private Date dateTime;

    public Transaction() {
    }

    public Transaction(String title, String category, String value, Date dateTime) {
        this.title = title;
        this.category = category;
        this.value = value;
        this.dateTime = dateTime;
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

    public Date getDateTime() {
        return dateTime;
    }
}
