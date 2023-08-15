package com.vkohler.wealthtracker.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.vkohler.wealthtracker.databinding.FragmentAddTransactionBinding;
import com.vkohler.wealthtracker.utilities.Constants;
import com.vkohler.wealthtracker.utilities.PreferenceManager;

import java.math.BigDecimal;
import java.util.HashMap;

public class AddTransactionFragment extends Fragment {

    private FragmentAddTransactionBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);
        preferenceManager = new PreferenceManager(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners() {
        binding.addTransaction.setOnClickListener(v -> {
            String stringValue = binding.value.getText().toString();
            if (isValueValid(stringValue)) {
                BigDecimal bigDecimal = new BigDecimal(stringValue);
                String value = bigDecimal.toString();
                addTransaction(value);
            }
        });
    }

    private void addTransaction(String value) {
//        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> transaction = new HashMap();
        transaction.put(Constants.KEY_TRANSACTION_VALUE, value);
        transaction.put(Constants.KEY_WALLET_ID, preferenceManager.getString(Constants.KEY_WALLET_ID));
        database.collection(Constants.KEY_COLLECTION_TRANSACTIONS)
                .add(transaction)
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        preferenceManager.updateBalance(getActivity(), value);
    }

    public static boolean isValueValid(String value) {
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}