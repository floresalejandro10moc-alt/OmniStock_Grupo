package com.example.proyectofinalpoo.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.model.Factura;
import java.util.List;

public class FacturaAdapter extends RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder> {

    private List<Factura> facturas;

    public FacturaAdapter(List<Factura> facturas) {
        this.facturas = facturas;
    }

    @NonNull
    @Override
    public FacturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_factura, parent, false);
        return new FacturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacturaViewHolder holder, int position) {
        Factura factura = facturas.get(position);
        holder.bind(factura);
    }

    @Override
    public int getItemCount() {
        return facturas.size();
    }

    class FacturaViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNumeroFactura, txtFechaFactura, txtTotalFactura;

        public FacturaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumeroFactura = itemView.findViewById(R.id.txtNumeroFactura);
            txtFechaFactura = itemView.findViewById(R.id.txtFechaFactura);
            txtTotalFactura = itemView.findViewById(R.id.txtTotalFactura);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Factura facturaSeleccionada = facturas.get(position);
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, DetalleFacturaActivity.class);
                    intent.putExtra("FACTURA_ID", (long) facturaSeleccionada.id_Factura);
                    context.startActivity(intent);
                }
            });
        }

        void bind(Factura factura) {
            txtNumeroFactura.setText("Factura #" + factura.id_Factura);
            txtFechaFactura.setText("Fecha: " + factura.fecha);
            txtTotalFactura.setText(String.format("Total: $%.2f", factura.total));
        }
    }
}
