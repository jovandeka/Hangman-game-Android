package com.example.vesanje;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryItem> categories;
    private Context context;
    private OnItemClickListener listener;

    public CategoryAdapter(List<CategoryItem> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryView = inflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(categoryView);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryItem category = categories.get(position);

        holder.categoryNameTextView.setText("GUESS THE "+category.getCategoryName().toUpperCase());
        holder.itemCountTextView.setText(String.valueOf(category.getItemCount()));
        holder.categoryImageView.setImageResource(category.getImageResourceId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            final CategoryItem currentItem = categories.get(position);
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(currentItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryNameTextView;
        TextView itemCountTextView;
        ImageView categoryImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            itemCountTextView = itemView.findViewById(R.id.countWordsTV);
            categoryImageView = itemView.findViewById(R.id.categoryImageView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(CategoryItem item);
    }
}