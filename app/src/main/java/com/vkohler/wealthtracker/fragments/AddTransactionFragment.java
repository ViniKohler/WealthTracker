package com.vkohler.wealthtracker.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.adapters.CategoryAdapter;
import com.vkohler.wealthtracker.databinding.FragmentAddTransactionBinding;
import com.vkohler.wealthtracker.interfaces.TransactionCallback;
import com.vkohler.wealthtracker.utilities.TransactionManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

public class AddTransactionFragment extends Fragment {

    FragmentAddTransactionBinding binding;
    TransactionManager transactionManager;
    Context context;

    ColorStateList red;
    ColorStateList green;

    Boolean isPositive = true;
    String title, category;
    BigDecimal bigValue;
    Date dateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();
        transactionManager = new TransactionManager(context);
        if (binding != null) {
            init();
        }
        setListeners();
        return view;
    }

    private void init() {
        updateRecyclerView();
        red = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red));
        green = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green));
    }

    private void updateRecyclerView() {
        String[] categoryList = {"Bill", "Clothing", "Debts", "Education", "Entertainment", "Food", "Health", "Insurance", "Investment", "Maintenance", "Salary", "Savings", "Taxes", "Transport", "Traveling"};

        CategoryAdapter adapter = new CategoryAdapter(Arrays.asList(categoryList), new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String categoryString) {
                category = categoryString;
                binding.category.setText(categoryString);
                binding.categoryRecyclerView.setVisibility(View.GONE);
                binding.category.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
            }
        });
        binding.categoryRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.categoryRecyclerView.setLayoutManager(layoutManager);
    }

    private void setListeners() {
        binding.category.setOnClickListener(v -> {
            binding.inputContainer.clearFocus();
            if (binding.categoryRecyclerView.getVisibility() != View.VISIBLE) {
                binding.categoryRecyclerView.setVisibility(View.VISIBLE);
            } else {
                binding.categoryRecyclerView.setVisibility(View.GONE);
            }
        });


        binding.categoryArrow.setOnClickListener(v -> {
            binding.inputContainer.clearFocus();
            if (binding.categoryRecyclerView.getVisibility() != View.VISIBLE) {
                binding.categoryRecyclerView.setVisibility(View.VISIBLE);
            } else {
                binding.categoryRecyclerView.setVisibility(View.GONE);
            }
        });

        binding.plusminus.setOnClickListener(v -> {
            if (isPositive) {
                isPositive = false;
                changeColor(red);
            } else {
                isPositive = true;
                changeColor(green);
            }
        });

        binding.date.setOnClickListener(v -> {
            binding.inputContainer.clearFocus();
        });

        binding.addTransaction.setOnClickListener(v -> {
            title = binding.title.getText().toString();
            category = binding.category.getText().toString();
            bigValue = new BigDecimal(binding.value.getText().toString());

            if (!isPositive) {
                bigValue = bigValue.negate();
            }

            int day = Integer.parseInt(binding.date.getText().toString().substring(0, 2));
            int month = Integer.parseInt(binding.date.getText().toString().substring(3, 5)) - 1;
            int year = Integer.parseInt(binding.date.getText().toString().substring(6, 10)) - 1900;

            dateTime = new Date(year, month, day);

            Toast.makeText(context, String.valueOf(dateTime), Toast.LENGTH_SHORT).show();

            binding.addTransaction.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);

            transactionManager.addTransaction(title, bigValue, category, dateTime, new TransactionCallback() {
                @Override
                public void transactionSuccess() {
                    binding.addTransaction.setVisibility(View.VISIBLE);
                }

                @Override
                public void transactionFailed() {
                    binding.addTransaction.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "Unable to add transaction", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void clearInput() {
        binding.title.setText("");
        binding.category.setText("");
        binding.value.setText("");
        binding.date.setText("");
        binding.addTransaction.setVisibility(View.VISIBLE);
        changeColor(green);
    }

    private void changeColor(ColorStateList color) {
        binding.title.setBackgroundTintList(color);
        binding.category.setBackgroundTintList(color);
        binding.value.setBackgroundTintList(color);
        binding.date.setBackgroundTintList(color);
        binding.categoryArrow.setImageTintList(color);
        binding.plusminus.setTextColor(color);
        binding.addTransaction.setBackgroundTintList(color);
    }
}