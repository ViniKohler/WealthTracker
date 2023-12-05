package com.vkohler.wealthtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.vkohler.wealthtracker.databinding.FragmentDeleteUserBinding;
import com.vkohler.wealthtracker.databinding.FragmentUserBinding;
import com.vkohler.wealthtracker.interfaces.UserFragmentListener;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class UserFragment extends Fragment {

    FragmentUserBinding binding;
    Context context;
    PreferenceManager preferenceManager;
    ActivityManager activityManager;
    LogManager logManager;

    String name, username, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();
        preferenceManager = new PreferenceManager(context);
        activityManager = new ActivityManager(context);
        logManager = new LogManager(context);

        init();
        return view;
    }

    private void init() {
        setListeners();
        name = preferenceManager.getString(Constants.KEY_NAME);
        username = preferenceManager.getString(Constants.KEY_USERNAME);
        password = preferenceManager.getString(Constants.KEY_PASSWORD);

        binding.name.setText(name);
        binding.username.setText(username);
        binding.password.setText(password);
    }

    private void setListeners() {
        binding.edit.setOnClickListener(v -> {
            if (getActivity() instanceof UserFragmentListener) {
                ((UserFragmentListener) getActivity()).changeFragment("UpdateUser");
            }
        });
        binding.delete.setOnClickListener(v -> {
            if (getActivity() instanceof UserFragmentListener) {
                ((UserFragmentListener) getActivity()).changeFragment("DeleteUser");
            }
        });
    }
}