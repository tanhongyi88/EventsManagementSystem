package com.fit2081.assignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.assignment1.provider.Category;

import java.util.ArrayList;

public class RecyclerAdapterCategory extends RecyclerView.Adapter<RecyclerAdapterCategory.CategoryItemViewHolder> {

    ArrayList<Category> categoryList = new ArrayList<>();

    public void setCategoryList(ArrayList<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category_item, parent, false); //CardView inflated as RecyclerView list item
        CategoryItemViewHolder viewHolder = new CategoryItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {
        holder.idView.setText(categoryList.get(position).getId());
        holder.nameView.setText(categoryList.get(position).getName());
        holder.eventCountView.setText(String.valueOf(categoryList.get(position).getEventCount()));
        holder.isActiveView.setText(categoryList.get(position).isActive() ? "Active" : "Inactive");
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryItemViewHolder extends RecyclerView.ViewHolder {
        public TextView idView;
        public TextView nameView;
        public TextView eventCountView;
        public TextView isActiveView;

        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            idView = itemView.findViewById(R.id.category_item_id_view);
            nameView = itemView.findViewById(R.id.category_item_name_view);
            eventCountView = itemView.findViewById(R.id.category_item_event_count_view);
            isActiveView = itemView.findViewById(R.id.category_item_isactive_view);
        }
    }
}
