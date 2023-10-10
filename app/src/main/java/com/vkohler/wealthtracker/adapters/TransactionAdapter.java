package com.vkohler.wealthtracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vkohler.wealthtracker.R;
import com.vkohler.wealthtracker.models.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_line_view, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.title.setText(transaction.getTitle());
        holder.category.setText(transaction.getCategory());
        holder.value.setText(String.valueOf(transaction.getValue()));
        int background;
        int temp = new BigDecimal(transaction.getValue().toString()).compareTo(BigDecimal.ZERO);
        if (temp == 1) {
            background = R.drawable.category_green_background;
        } else {
            background = R.drawable.category_red_background;
        }
        holder.categoryIcon.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), background));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        public TextView title, category, value;
        public ImageView categoryIcon;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            value = itemView.findViewById(R.id.value);
        }
    }

}
