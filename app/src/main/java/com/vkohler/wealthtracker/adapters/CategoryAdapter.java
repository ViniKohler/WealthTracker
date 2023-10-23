package com.vkohler.wealthtracker.adapters;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vkohler.wealthtracker.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categoryList;
    private OnItemClickListener listener;

    public CategoryAdapter(List<String> categoryList, OnItemClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        String category = categoryList.get(position);
        Icon icon = null;
        String categoryName = "";

        switch (category) {
            case "Bill":
                icon = Icon.createWithResource(context, R.drawable.ic_bill);
                categoryName = "Bill";
                break;
            case "Clothing":
                icon = Icon.createWithResource(context, R.drawable.ic_clothing);
                categoryName = "Clothing";
                break;
            case "Debts":
                icon = Icon.createWithResource(context, R.drawable.ic_debt);
                categoryName = "Debts";
                break;
            case "Education":
                icon = Icon.createWithResource(context, R.drawable.ic_education);
                categoryName = "Education";
                break;
            case "Entertainment":
                icon = Icon.createWithResource(context, R.drawable.ic_entertainment);
                categoryName = "Entertainment";
                break;
            case "Food":
                icon = Icon.createWithResource(context, R.drawable.ic_food);
                categoryName = "Food";
                break;
            case "Health":
                icon = Icon.createWithResource(context, R.drawable.ic_health);
                categoryName = "Health";
                break;
            case "Insurance":
                icon = Icon.createWithResource(context, R.drawable.ic_insurance);
                categoryName = "Insurance";
                break;
            case "Investment":
                icon = Icon.createWithResource(context, R.drawable.ic_investment);
                categoryName = "Investment";
                break;
            case "Maintenance":
                icon = Icon.createWithResource(context, R.drawable.ic_maintenance);
                categoryName = "Maintenance";
                break;
            case "Salary":
                icon = Icon.createWithResource(context, R.drawable.ic_dollar);
                categoryName = "Salary";
                break;
            case "Savings":
                icon = Icon.createWithResource(context, R.drawable.ic_savings);
                categoryName = "Savings";
                break;
            case "Taxes":
                icon = Icon.createWithResource(context, R.drawable.ic_tax);
                categoryName = "Taxes";
                break;
            case "Transport":
                icon = Icon.createWithResource(context, R.drawable.ic_transport);
                categoryName = "Transport";
                break;
            case "Traveling":
                icon = Icon.createWithResource(context, R.drawable.ic_traveling);
                categoryName = "Traveling";
                break;
        }
        holder.categoryIcon.setImageIcon(icon);
        holder.categoryName.setText(categoryName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(category);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String category);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public ImageView categoryIcon;
        public TextView categoryName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }

}
