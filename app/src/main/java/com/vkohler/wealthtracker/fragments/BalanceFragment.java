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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BalanceFragment extends Fragment {

    private FragmentBalanceBinding binding;
    PreferenceManager preferenceManager;
    TransactionManager transactionManager;
    Context context;
    ColorStateList green, red, text_primary, text_secondary;

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
        setColors();
        updateValues();
        setListeners();
    }

    private void setColors() {
        green = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green));
        red = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red));
        text_primary = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_primary));
        text_secondary = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_secondary));
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

                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    String strTotal = decimalFormat.format(total.abs());

                    binding.balance.setText(strTotal);

                    if (total.compareTo(BigDecimal.ZERO) < 0) { // if total < 0
                        binding.visibility.setBackgroundTintList(red);
                        binding.eye.setImageTintList(red);
                        binding.currency.setText("-$ ");
                    }
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
            binding.visibility.setBackgroundTintList(text_secondary);
            binding.eye.setImageTintList(text_secondary);
            binding.balance.setTextColor(text_secondary);
            binding.currency.setTextColor(text_secondary);
            binding.balance.setText("••••");
            binding.currency.setText("$ ");
            preferenceManager.putBoolean(Constants.KEY_IS_BALANCE_VISIBLE, false);
        } else {
            String balance = preferenceManager.getString(Constants.KEY_BALANCE);
            if (!balance.contains("-")) {
                binding.visibility.setBackgroundTintList(green);
                binding.eye.setImageTintList(green);
                binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE));
                binding.currency.setText("$ ");
            } else {
                binding.visibility.setBackgroundTintList(red);
                binding.eye.setImageTintList(red);
                binding.balance.setText(preferenceManager.getString(Constants.KEY_BALANCE).replace("-", ""));
                binding.currency.setText("-$ ");
            }
            binding.balance.setTextColor(text_primary);
            binding.currency.setTextColor(text_primary);
            preferenceManager.putBoolean(Constants.KEY_IS_BALANCE_VISIBLE, true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}