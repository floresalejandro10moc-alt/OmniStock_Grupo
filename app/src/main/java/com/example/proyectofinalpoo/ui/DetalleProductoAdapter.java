package com.example.proyectofinalpoo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.model.DetalleVisual;
import java.util.List;

public class DetalleProductoAdapter extends RecyclerView.Adapter<DetalleProductoAdapter.DetalleProductoViewHolder> {

    private List<DetalleVisual> detalles;

    public DetalleProductoAdapter(List<DetalleVisual> detalles) {
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
        DetalleVisual detalle = detalles.get(position);
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

        void bind(DetalleVisual detalle) {
            txtNombre.setText(detalle.nombreProducto);
            txtCantidad.setText("x" + detalle.cantidad);
            txtPrecio.setText(String.format("$%.2f", detalle.precioUnitario));
        }
    }
}
