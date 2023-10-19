package com.vkohler.wealthtracker.adapters;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.rpc.context.AttributeContext;
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
        int background;
        Context context = holder.itemView.getContext();
        IconCompat icon = IconCompat.createWithResource(context, R.drawable.ic_dollar);
        Transaction transaction = transactionList.get(position);

        String value = transaction.getValue();

        if (new BigDecimal(value).compareTo(BigDecimal.ZERO) < 0) { // if == 0 return 0; if > 0 return 1; if < 0 return 2
            background = R.drawable.category_red_background;
            holder.currency.setText("- $");
        } else {
            background = R.drawable.category_green_background;
        }

        switch (transaction.getCategory()) {
            case "Bill":
                icon = IconCompat.createWithResource(context, R.drawable.ic_bill);
                break;
            case "Clothing":
                icon = IconCompat.createWithResource(context, R.drawable.ic_clothing);
                break;
            case "Debts":
                icon = IconCompat.createWithResource(context, R.drawable.ic_debt);
                break;
            case "Education":
                icon = IconCompat.createWithResource(context, R.drawable.ic_education);
                break;
            case "Entertainment":
                icon = IconCompat.createWithResource(context, R.drawable.ic_entertainment);
                break;
            case "Food":
                icon = IconCompat.createWithResource(context, R.drawable.ic_food);
                break;
            case "Health":
                icon = IconCompat.createWithResource(context, R.drawable.ic_health);
                break;
            case "Insurance":
                icon = IconCompat.createWithResource(context, R.drawable.ic_insurance);
                break;
            case "Investment":
                icon = IconCompat.createWithResource(context, R.drawable.ic_investment);
                break;
            case "Maintenance":
                icon = IconCompat.createWithResource(context, R.drawable.ic_maintenance);
                break;
            case "Salary":
                icon = IconCompat.createWithResource(context, R.drawable.ic_dollar);
                break;
            case "Savings":
                icon = IconCompat.createWithResource(context, R.drawable.ic_savings);
                break;
            case "Taxes":
                icon = IconCompat.createWithResource(context, R.drawable.ic_tax);
                break;
            case "Transport":
                icon = IconCompat.createWithResource(context, R.drawable.ic_transport);
                break;
            case "Traveling":
                icon = IconCompat.createWithResource(context, R.drawable.ic_traveling);
                break;
        }

        holder.categoryIcon.setBackground(ContextCompat.getDrawable(context, background));
        holder.categoryIcon.setImageIcon(icon.toIcon().setTint(ContextCompat.getColor(context, R.color.text_primary)));
        holder.title.setText(transaction.getTitle());
        holder.category.setText(transaction.getCategory());
        holder.value.setText(value.replace("-", ""));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        public TextView title, category, currency, value;
        public ImageView categoryIcon;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            currency = itemView.findViewById(R.id.currency);
            value = itemView.findViewById(R.id.value);
        }
    }

}
