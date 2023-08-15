package com.vkohler.wealthtracker.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.vkohler.wealthtracker.activities.DataActivity;
import com.vkohler.wealthtracker.activities.HomeActivity;
import com.vkohler.wealthtracker.activities.ProfileActivity;
import com.vkohler.wealthtracker.activities.SignInActivity;
import com.vkohler.wealthtracker.activities.SignUpActivity;
import com.vkohler.wealthtracker.activities.TransactionActivity;

public class ActivityManager {

    private final Context context;

    public ActivityManager(Context context) {
        this.context = context;
    }

    public void startActivity(String activityName) {
        Class newActivity;
        switch (activityName) {
            case "signin":
                newActivity = SignInActivity.class;
                break;
            case "signup":
                newActivity = SignUpActivity.class;
                break;
            case "home":
                newActivity = HomeActivity.class;
                break;
            case "transaction":
                newActivity = TransactionActivity.class;
                break;
            case "data":
                newActivity = DataActivity.class;
                break;
            case "profile":
                newActivity = ProfileActivity.class;
                break;
        }
        Intent intent = new Intent(context, newActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

}