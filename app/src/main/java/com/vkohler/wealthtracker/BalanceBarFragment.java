package com.vkohler.wealthtracker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

                    for (int i = 0; i <= list.size() - 1; i++) {

                        BigDecimal value = new BigDecimal(list.get(i).getValue());

                        if (value.compareTo(BigDecimal.ZERO) > 0) {
                            positive = positive.add(value);

                            total = total.add(value);

                        } else {
                            negative = negative.add(value);

                            total = total.add(value.negate());
                        }
                    }

                    BigDecimal percentPositive = positive.multiply(BigDecimal.valueOf(100)).divide(total, RoundingMode.UP);
                    BigDecimal percentNegative = negative.multiply(BigDecimal.valueOf(100)).divide(total, RoundingMode.UP);

                    if (binding != null) {
                        binding.positive.post(new Runnable() {
                            @Override
                            public void run() {
                                int width = binding.positive.getWidth();
                                int finalPositiveWidth = percentPositive.intValue() * width / 100;
                                int finalNegativeWidth = percentNegative.intValue() * width / 100;

                                NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                                format.setGroupingUsed(true);
                                format.setMinimumFractionDigits(2);

                                String positiveValue = "$ " + format.format(positive);
                                String negativeValue = "-$ " + format.format(negative.abs());

                                int green = ContextCompat.getColor(context, R.color.green);
                                int red = ContextCompat.getColor(context, R.color.red);

                                binding.positive.getLayoutParams().width = finalPositiveWidth;

                                binding.positiveValue.setTextColor(green);
                                binding.positive.setBackgroundTintList(ColorStateList.valueOf(green));
                                binding.negativeValue.setTextColor(red);

                                binding.positiveValue.setText(positiveValue);
                                binding.negativeValue.setText(negativeValue);

                                binding.positive.requestLayout();
                                binding.negative.requestLayout();
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
            }
        });
    }
}