package com.vkohler.wealthtracker.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vkohler.wealthtracker.databinding.ActivityProfileBinding;
import com.vkohler.wealthtracker.fragments.DeleteUserFragment;
import com.vkohler.wealthtracker.fragments.UpdateUserFragment;
import com.vkohler.wealthtracker.fragments.UserFragment;
import com.vkohler.wealthtracker.interfaces.UserFragmentListener;
import com.vkohler.wealthtracker.utilities.ActivityManager;
import com.vkohler.wealthtracker.utilities.LogManager;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

public class ProfileActivity extends AppCompatActivity implements UserFragmentListener {

    ActivityManager activityManager;
    LogManager logManager;
    PreferenceManager preferenceManager;
    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        activityManager = new ActivityManager(getApplicationContext());
        logManager = new LogManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());

        overridePendingTransition(0, 0);
        setContentView(binding.getRoot());

        setListeners();
        setFragments();
    }

    private void setListeners() {
        binding.back.setOnClickListener(v -> {
            activityManager.startActivity("main");
        });
    }

    private void setFragments() {
        Fragment userFragment = new UserFragment();
        Fragment updateUserFragment = new UpdateUserFragment();
        Fragment deleteUserFragment = new DeleteUserFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(binding.userFragment.getId(), userFragment);
        fragmentTransaction.replace(binding.updateUserFragment.getId(), updateUserFragment);
        fragmentTransaction.replace(binding.deleteUserFragment.getId(), deleteUserFragment);

        fragmentTransaction.commit();
    }

    @Override
    public void changeFragment(String fragment) {
        switch (fragment) {
            case "User":
                binding.profilePicture.setVisibility(View.VISIBLE);
                binding.userFragment.setVisibility(View.VISIBLE);
                binding.updateUserFragment.setVisibility(View.GONE);
                binding.deleteUserFragment.setVisibility(View.GONE);
                break;
            case "DeleteUser":
                binding.profilePicture.setVisibility(View.GONE);
                binding.userFragment.setVisibility(View.GONE);
                binding.updateUserFragment.setVisibility(View.GONE);
                binding.deleteUserFragment.setVisibility(View.VISIBLE);
                break;
            case "UpdateUser":
                binding.profilePicture.setVisibility(View.GONE);
                binding.userFragment.setVisibility(View.GONE);
                binding.updateUserFragment.setVisibility(View.VISIBLE);
                binding.deleteUserFragment.setVisibility(View.GONE);
        }
    }
}