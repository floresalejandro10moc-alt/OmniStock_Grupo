package com.example.proyectofinalpoo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.model.Producto;

import java.util.List;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.ViewHolder> {

    private List<Producto> productos;

    public FeaturedAdapter(List<Producto> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_featured, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.txtFeaturedName.setText(producto.nombre);
        holder.txtFeaturedPrice.setText(String.format("$%.2f", producto.precioBase));
        // Note: Icon is static for now, or could vary based on category if we had logic
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFeaturedName, txtFeaturedPrice;
        ImageView imgFeatured;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFeaturedName = itemView.findViewById(R.id.txtFeaturedName);
            txtFeaturedPrice = itemView.findViewById(R.id.txtFeaturedPrice);
            imgFeatured = itemView.findViewById(R.id.imgFeatured);
        }
    }
}
