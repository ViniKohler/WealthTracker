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

    private int selectedItemPosition = -1;
    private final List<String> categoryList;
    private final OnItemClickListener listener;

    public CategoryAdapter(List<String> categoryList, OnItemClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
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

        switch (category) {
            case "Bill":
                icon = Icon.createWithResource(context, R.drawable.ic_bill);
                break;
            case "Clothing":
                icon = Icon.createWithResource(context, R.drawable.ic_clothing);
                break;
            case "Debts":
                icon = Icon.createWithResource(context, R.drawable.ic_debt);
                break;
            case "Education":
                icon = Icon.createWithResource(context, R.drawable.ic_education);
                break;
            case "Entertainment":
                icon = Icon.createWithResource(context, R.drawable.ic_entertainment);
                break;
            case "Food":
                icon = Icon.createWithResource(context, R.drawable.ic_food);
                break;
            case "Health":
                icon = Icon.createWithResource(context, R.drawable.ic_health);
                break;
            case "Insurance":
                icon = Icon.createWithResource(context, R.drawable.ic_insurance);
                break;
            case "Investment":
                icon = Icon.createWithResource(context, R.drawable.ic_investment);
                break;
            case "Maintenance":
                icon = Icon.createWithResource(context, R.drawable.ic_maintenance);
                break;
            case "Salary":
                icon = Icon.createWithResource(context, R.drawable.ic_dollar);
                break;
            case "Savings":
                icon = Icon.createWithResource(context, R.drawable.ic_savings);
                break;
            case "Taxes":
                icon = Icon.createWithResource(context, R.drawable.ic_tax);
                break;
            case "Transport":
                icon = Icon.createWithResource(context, R.drawable.ic_transport);
                break;
            case "Traveling":
                icon = Icon.createWithResource(context, R.drawable.ic_traveling);
                break;
        }

        holder.categoryIcon.setImageIcon(icon);
        holder.category.setText(category);

        //if (position == selectedItemPosition) {
        //    holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.white_corner));
        ///    holder.categoryIcon.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.background_dark)));
        //} else {
        //    holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.stroke_white_corner));
        //    holder.categoryIcon.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.text_primary)));
        //}

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(category);

                selectedItemPosition = position;
                notifyDataSetChanged();
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

    public void clearSelection() {
        selectedItemPosition = -1;
        notifyDataSetChanged();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public ImageView categoryIcon;
        public TextView category;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            category = itemView.findViewById(R.id.category);
        }
    }

}
