package com.vkohler.wealthtracker.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.databinding.FragmentBalanceBinding;
import com.vkohler.wealthtracker.interfaces.TransactionManagerCallback;
import com.vkohler.wealthtracker.models.Transaction;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;
import com.vkohler.wealthtracker.utilities.TransactionManager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BalanceFragment extends Fragment {

    private FragmentBalanceBinding binding;
    PreferenceManager preferenceManager;
    TransactionManager transactionManager;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBalanceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();
        preferenceManager = new PreferenceManager(context);
        transactionManager = new TransactionManager(context);

        init();
        return view;
    }

    private void init() {
        updateValues();
        setListeners();
    }

    private void updateValues() {
        List<Transaction> list = new ArrayList<>();
        transactionManager.getTransactions(new TransactionManagerCallback() {

            @Override
            public void onTransactionsLoaded(List<Transaction> transactions) {
                list.clear();
                list.addAll(transactions);

                BigDecimal total = new BigDecimal(BigInteger.ZERO);

                if (list.size() != 0) {
                    for (Transaction transaction : list) {

                        BigDecimal value = new BigDecimal(transaction.getValue());

                        total = total.add(value);
                    }
                }

                if (binding != null) {
                    binding.balance.setText(total.toString());
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListeners() {
        binding.eye.setOnClickListener(v -> changeVisibility());
    }

    private void changeVisibility() {
        Boolean visibility = preferenceManager.getBoolean(Constants.KEY_IS_BALANCE_VISIBLE);
        if (visibility) {
            binding.visibility.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_secondary)));
            binding.eye.setColorFilter(ContextCompat.getColor(context, R.color.text_secondary));
            binding.balance.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
            binding.currency.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
            binding.balance.setText("••••");
            binding.currency.setText("$ ");
            preferenceManager.putBoolean(Constants.KEY_IS_BALANCE_VISIBLE, false);
        } else {
            String balance = preferenceManager.getString(Constants.KEY_BALANCE);
            if (!balance.contains("-")) {
                binding.visibility.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
                binding.eye.setColorFilter(ContextCompat.getColor(context, R.color.green));
                binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE));
                binding.currency.setText("$ ");
            } else {
                binding.visibility.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red)));
                binding.eye.setColorFilter(ContextCompat.getColor(context, R.color.red));
                binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE).replace("-", ""));
                binding.currency.setText("-$ ");
            }
            binding.balance.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
            binding.currency.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
            preferenceManager.putBoolean(Constants.KEY_IS_BALANCE_VISIBLE, true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}