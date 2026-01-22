package com.example.proyectofinalpoo.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.model.Categoria;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(Categoria categoria);
    }

    private List<Categoria> categorias;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0; // Default: 0 (Usually "All" or first)

    public CategoryAdapter(List<Categoria> categorias, OnCategoryClickListener listener) {
        this.categorias = categorias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_chip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.txtCategoryName.setText(categoria.nombre);

        if (selectedPosition == position) {
            // Selected Style (Accent Stroke, maybe slight tint)
            holder.cardView.setStrokeColor(Color.parseColor("#FFFFFF")); // High Emphasis stroke
            holder.cardView.setCardBackgroundColor(Color.parseColor("#33FFFFFF")); // More visible glass
        } else {
            // Unselected Style (Glass Stroke, Transparent-ish)
            holder.cardView.setStrokeColor(Color.parseColor("#4DFFFFFF")); // Glass stroke
            holder.cardView.setCardBackgroundColor(Color.parseColor("#1AFFFFFF")); // Glass white 10
        }

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            listener.onCategoryClick(categoria);
        });
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView txtCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardCategoryChip);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
        }
    }
}
