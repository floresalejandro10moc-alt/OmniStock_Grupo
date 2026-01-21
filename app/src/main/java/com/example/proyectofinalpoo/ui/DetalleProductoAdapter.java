package com.example.proyectofinalpoo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.model.DetalleParaLogica;

import java.util.List;
import java.util.Locale;

public class DetalleProductoAdapter extends RecyclerView.Adapter<DetalleProductoAdapter.DetalleProductoViewHolder> {

    private List<DetalleParaLogica> detalles;

    public DetalleProductoAdapter(List<DetalleParaLogica> detalles) {
        this.detalles = detalles;
    }

    @NonNull
    @Override
    public DetalleProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_producto, parent, false);
        return new DetalleProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetalleProductoViewHolder holder, int position) {
        DetalleParaLogica detalle = detalles.get(position);
        holder.bind(detalle);
    }

    @Override
    public int getItemCount() {
        return detalles != null ? detalles.size() : 0;
    }

    class DetalleProductoViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNombre, txtCantidad, txtPrecio;

        public DetalleProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtDetalleNombreProducto);
            txtCantidad = itemView.findViewById(R.id.txtDetalleCantidad);
            txtPrecio = itemView.findViewById(R.id.txtDetallePrecioUnitario);
        }

        void bind(DetalleParaLogica detalle) {
            // Usamos los datos del objeto DetalleParaLogica
            txtNombre.setText(detalle.producto.nombre);
            txtCantidad.setText(String.format(Locale.US, "x%d", detalle.cantidadComprada));
            txtPrecio.setText(String.format(Locale.US, "$%.2f", detalle.producto.precioBase));
        }
    }
}
