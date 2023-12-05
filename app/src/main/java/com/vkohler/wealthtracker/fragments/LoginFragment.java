package com.vkohler.wealthtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.vkohler.wealthtracker.databinding.FragmentLoginBinding;
import com.vkohler.wealthtracker.interfaces.LogCallback;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    PreferenceManager preferenceManager;
    LogManager logManager;
    Context context;

    String username, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();
        preferenceManager = new PreferenceManager(context);
        logManager = new LogManager(context);

        setListeners();
        return view;
    }

    private void setListeners() {
        binding.logIn.setOnClickListener(v -> {
            username = binding.username.getText().toString();
            password = binding.password.getText().toString();

            binding.logIn.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);

            logManager.logIn(binding.username.getText().toString(), binding.password.getText().toString(), new LogCallback() {
                @Override
                public void actionDone() {
                    binding.logIn.setVisibility(View.VISIBLE);
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