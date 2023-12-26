package com.vkohler.wealthtracker.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.FragmentLogonBinding;
import com.vkohler.wealthtracker.interfaces.LogCallback;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;


public class LogonFragment extends Fragment {

    FragmentLogonBinding binding;
    PreferenceManager preferenceManager;
    LogManager logManager;
    Context context;

    String name, username, password, confirmPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLogonBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();
        preferenceManager = new PreferenceManager(context);
        logManager = new LogManager(context);

        setListeners();
        return view;
    }

    private void setListeners() {
        binding.logOn.setOnClickListener(v -> {
            username = binding.username.getText().toString();
            name = binding.name.getText().toString();
            password = binding.password.getText().toString();
            confirmPassword = binding.confirmPassword.getText().toString();

            binding.logOn.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);

            logManager.logOn(username, name, password, confirmPassword, new LogCallback() {
                @Override
                public void actionDone() {
                    binding.logOn.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void setMessage(String message) {
                    binding.loadingStatus.setText(message);
                }
            });
        });
    }
}