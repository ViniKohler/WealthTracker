package com.vkohler.wealthtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vkohler.wealthtracker.adapters.TransactionAdapter;
import com.vkohler.wealthtracker.databinding.FragmentTransactionsListBinding;
import com.vkohler.wealthtracker.interfaces.TransactionManagerCallback;
import com.vkohler.wealthtracker.models.Transaction;
import com.vkohler.wealthtracker.utilities.PreferenceManager;
import com.vkohler.wealthtracker.utilities.TransactionManager;

import java.util.List;

public class TransactionsListFragment extends Fragment {

    private FragmentTransactionsListBinding binding;
    PreferenceManager preferenceManager;
    TransactionManager transactionManager;
    private TransactionAdapter transactionAdapter;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransactionsListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();
        preferenceManager = new PreferenceManager(context);
        transactionManager = new TransactionManager(context);

        init();
        return view;
    }

    private void init() {
        getTransactions();
    }

    private void getTransactions() {
        binding.transactionRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        transactionManager.getTransactions(new TransactionManagerCallback() {
            @Override
            public void onTransactionsLoaded(List<Transaction> transactions) {
                if (binding != null) {
                    transactionAdapter = new TransactionAdapter(transactions);
                    binding.transactionRecyclerView.setAdapter(transactionAdapter);

                    if (transactions.isEmpty()) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.notFound.setVisibility(View.VISIBLE);
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.notFound.setVisibility(View.GONE);
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