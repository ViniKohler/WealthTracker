package com.vkohler.wealthtracker.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.vkohler.wealthtracker.activities.DataActivity;
import com.vkohler.wealthtracker.activities.HomeActivity;
import com.vkohler.wealthtracker.activities.LogInActivity;
import com.vkohler.wealthtracker.activities.LogOnActivity;
import com.vkohler.wealthtracker.activities.ProfileActivity;
import com.vkohler.wealthtracker.activities.TransactionActivity;

public class ActivityManager {

    private final Context context;
    PreferenceManager preferenceManager;

    public ActivityManager(Context context) {
        preferenceManager = new PreferenceManager(context);
        this.context = context;
    }

    public void startActivity(String activityName) {
        Class newActivity = null;
        switch (activityName) {
            case "login":
                newActivity = LogInActivity.class;
                break;
            case "logon":
                newActivity = LogOnActivity.class;
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

    public void startURLActivity(String URL) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void startLastActivity() {
        String lastActivity = preferenceManager.getString(Constants.KEY_LAST_ACTIVITY);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(lastActivity));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void setLastActivity(String lastActivity) {
        preferenceManager.putString(Constants.KEY_LAST_ACTIVITY, lastActivity);
    }

    public String getLastActivity() {
        return preferenceManager.getString(Constants.KEY_LAST_ACTIVITY);
    }

}
