package com.vkohler.wealthtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.vkohler.wealthtracker.databinding.FragmentDeleteUserBinding;
import com.vkohler.wealthtracker.interfaces.UserFragmentListener;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class DeleteUserFragment extends Fragment {

    FragmentDeleteUserBinding binding;
    PreferenceManager preferenceManager;
    LogManager logManager;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDeleteUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();
        preferenceManager = new PreferenceManager(context);
        logManager = new LogManager(context);

        setListeners();
        return view;
    }

    private void setListeners() {
        binding.cancel.setOnClickListener(v -> {
            if (getActivity() instanceof UserFragmentListener) {
                ((UserFragmentListener) getActivity()).changeFragment("User");
            }
        });
        binding.delete.setOnClickListener(v -> {
            logManager.deleteLog();
        });
    }
}