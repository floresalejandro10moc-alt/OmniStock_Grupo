package com.example.proyectofinalpoo.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.logic.Alimento;
import com.example.proyectofinalpoo.logic.Electronico;
import com.example.proyectofinalpoo.logic.ProductoBase;
import com.example.proyectofinalpoo.logic.Ropa;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ProductoViewHolder> {

    private List<ProductoBase> listaProductos;

    public CarritoAdapter(List<ProductoBase> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        ProductoBase producto = listaProductos.get(position);

        // 1. Llenamos los datos básicos
        holder.txtNombre.setText(producto.getNombre());

        // 2. Usamos el POLIMORFISMO para obtener el precio final calculado
        double precioFinal = producto.calcularPrecioFinal();
        holder.txtPrecio.setText(String.format("$%.2f", precioFinal));

        // 3. CAMBIO VISUAL DINÁMICO (Requisito Rúbrica)
        // Cambiamos el color de fondo de la tarjeta según el tipo de clase
        if (producto instanceof Electronico) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E3F2FD")); // Azulito
            holder.txtDetalle.setText("Incluye IVA + Suntuario");
        } else if (producto instanceof Ropa) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FCE4EC")); // Rosadito
            holder.txtDetalle.setText("Incluye IVA (Posible Descuento)");
        } else if (producto instanceof Alimento) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E8F5E9")); // Verdesito
            holder.txtDetalle.setText("0% IVA");
        }
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    // Clase interna que guarda las referencias a los controles del XML
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtPrecio, txtDetalle;
        CardView cardView;
        ImageView icono;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreProducto);
            txtPrecio = itemView.findViewById(R.id.txtPrecioProducto);
            txtDetalle = itemView.findViewById(R.id.txtDetalleImpuesto);
            cardView = itemView.findViewById(R.id.cardViewProducto);
            icono = itemView.findViewById(R.id.imgIcono);
        }
    }
}