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
import com.vkohler.wealthtracker.databinding.FragmentBalanceBarBinding;
import com.vkohler.wealthtracker.interfaces.TransactionManagerCallback;
import com.vkohler.wealthtracker.models.Transaction;
import com.vkohler.wealthtracker.utilities.TransactionManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BalanceBarFragment extends Fragment {
    private FragmentBalanceBarBinding binding;
    TransactionManager transactionManager;
    Context context;

    BigDecimal positive = BigDecimal.ZERO;
    BigDecimal negative = BigDecimal.ZERO;
    BigDecimal total = BigDecimal.ZERO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBalanceBarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();
        transactionManager = new TransactionManager(context);

        init();
        return view;
    }

    private void init() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month = new SimpleDateFormat("MMMM");
        String monthString = month.format(cal.getTime());
        binding.month.setText(monthString);

        updateProgressBar();
    }

    private void updateProgressBar() {
        List<Transaction> list = new ArrayList<>();
        transactionManager.getTransactions(new TransactionManagerCallback() {
            @Override
            public void onTransactionsLoaded(List<Transaction> transactions) {
                list.clear();
                list.addAll(transactions);

                if (list.size() != 0) {

                    for (Transaction transaction : list) {

                        BigDecimal value = new BigDecimal(transaction.getValue());

                        if (value.compareTo(BigDecimal.ZERO) > 0) {
                            positive = positive.add(value);

                            total = total.add(value);
                        } else {
                            negative = negative.add(value);

                            total = total.add(value.negate());
                        }
                    }

                    BigDecimal percent = BigDecimal.ZERO;

                    if (positive.intValue() != 0) {
                        percent = positive.multiply(BigDecimal.valueOf(100)).divide(total, RoundingMode.UP);
                    }

                    if (binding != null) {
                        BigDecimal finalPercent = percent;
                        binding.month.post(() -> {
                            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                            format.setGroupingUsed(true);
                            format.setMinimumFractionDigits(2);

                            String positiveValue = "$ " + format.format(positive);
                            String negativeValue = "$ " + format.format(negative.abs());

                            if (finalPercent.intValue() != 0) {
                                binding.progressBar.setProgress(finalPercent.intValue());
                            } else {
                                binding.progressBar.setVisibility(View.GONE);
                            }

                            binding.positiveValue.setText(positiveValue);
                            binding.negativeValue.setText(negativeValue);
                        });
                    }
                } else {
                    if (binding != null) {
                        binding.month.post(() -> {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.positiveValue.setVisibility(View.GONE);
                            binding.greenBall.setVisibility(View.GONE);
                            binding.negativeValue.setVisibility(View.GONE);
                            binding.redBall.setVisibility(View.GONE);

                            binding.noTransactionsFound.setVisibility(View.VISIBLE);
                        });
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}