package com.vkohler.wealthtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.vkohler.wealthtracker.databinding.FragmentUpdateUserBinding;
import com.vkohler.wealthtracker.interfaces.LogCallback;
import com.vkohler.wealthtracker.interfaces.UserFragmentListener;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

import io.grpc.internal.ConscryptLoader;

public class UpdateUserFragment extends Fragment {

    FragmentUpdateUserBinding binding;
    PreferenceManager preferenceManager;
    LogManager logManager;
    ActivityManager activityManager;
    Context context;

    String username, name, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUpdateUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();
        preferenceManager = new PreferenceManager(context);
        activityManager = new ActivityManager(context);
        logManager = new LogManager(context);

        init();
        return view;
    }

    private void init() {
        username = preferenceManager.getString(Constants.KEY_USERNAME);
        name = preferenceManager.getString(Constants.KEY_NAME);
        password = preferenceManager.getString(Constants.KEY_PASSWORD);

        binding.name.setText(name);
        binding.username.setText(username);
        binding.password.setText(password);
        binding.confirmPassword.setText(password);

        setListeners();
    }

    private void setListeners() {
        binding.cancel.setOnClickListener(v -> {
            if (getActivity() instanceof UserFragmentListener) {
                ((UserFragmentListener) getActivity()).changeFragment("User");
            }
        });
        binding.update.setOnClickListener(v -> {
            String newName, newUsername, newPassword, confirmNewPassword;

            newName = binding.name.getText().toString();
            newUsername = binding.username.getText().toString();
            newPassword = binding.password.getText().toString();
            confirmNewPassword = binding.confirmPassword.getText().toString();

            binding.update.setVisibility(View.GONE);
            binding.cancel.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);

            logManager.updateLog(newName, newUsername, newPassword, confirmNewPassword, new LogCallback() {
                @Override
                public void actionDone() {
                    binding.update.setVisibility(View.VISIBLE);
                    binding.cancel.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void setMessage(String message) {}
            });
        });
    }
}