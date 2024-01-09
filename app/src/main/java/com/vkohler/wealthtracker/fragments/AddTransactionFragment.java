package com.vkohler.wealthtracker.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.adapters.CategoryAdapter;
import com.vkohler.wealthtracker.databinding.FragmentAddTransactionBinding;
import com.vkohler.wealthtracker.interfaces.TransactionCallback;
import com.vkohler.wealthtracker.utilities.TransactionManager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

public class AddTransactionFragment extends Fragment {

    FragmentAddTransactionBinding binding;
    TransactionManager transactionManager;
    Context context;

    ColorStateList red, green, white;

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
            setListeners();
            setTextChangedListeners();
        }
        return view;
    }

    private void init() {
        updateRecyclerView();
        red = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red));
        green = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green));
        white = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_primary));
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
        binding.inputContainer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                updateUI();
            }
        });

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
            binding.inputContainer.clearFocus();
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

            binding.addTransaction.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);

            transactionManager.addTransaction(title, bigValue, category, dateTime, new TransactionCallback() {
                @Override
                public void transactionSuccess() {
                    binding.addTransaction.setVisibility(View.VISIBLE);
                    clearInput();
                }

                @Override
                public void transactionFailed() {
                    binding.addTransaction.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "Unable to add transaction", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setTextChangedListeners() {
        binding.title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateUI();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateUI();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateUI();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i2 < i1) {
                    binding.date.setBackgroundTintList(white);
                } else {
                    if (isPositive) {
                        binding.date.setBackgroundTintList(green);
                    } else {
                        binding.date.setBackgroundTintList(red);
                    }
                }

                if ((charSequence.length() == 2 || charSequence.length() == 5) && i2 > 0) {
                    binding.date.setText(
                            new StringBuilder(charSequence)
                                    .insert(charSequence.length(), "/")
                                    .toString()
                    );
                    binding.date.setSelection(binding.date.getText().length());
                } else if (charSequence.length() == 10) {
                    int day = Integer.parseInt(binding.date.getText().toString().substring(0, 2));
                    int month = Integer.parseInt(binding.date.getText().toString().substring(3, 5)) - 1;
                    int year = Integer.parseInt(binding.date.getText().toString().substring(6, 10)) - 1900;

                    if (day > 31 || month > 12 - 1) {
                        binding.info.setVisibility(View.VISIBLE);
                    } else {
                        binding.info.setVisibility(View.GONE);
                        dateTime = new Date(year, month, day);
                        updateUI();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void updateUI() {
        String title = binding.title.getText().toString();
        String category = binding.category.getText().toString();
        String bigValue = binding.value.getText().toString();
        String dateTime = binding.date.getText().toString();

        if (!title.isEmpty() && !category.isEmpty() && !bigValue.toString().isEmpty() && !dateTime.toString().isEmpty()) {
            binding.addTransaction.setVisibility(View.VISIBLE);
        } else {
            binding.addTransaction.setVisibility(View.GONE);
        }
    }

    private void clearInput() {
        binding.title.setText("");
        binding.category.setText("");
        binding.value.setText("");
        binding.date.setText("");
        binding.addTransaction.setVisibility(View.GONE);
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