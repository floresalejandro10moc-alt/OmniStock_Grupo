package com.example.proyectofinalpoo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.model.DetalleParaLogica;
import com.example.proyectofinalpoo.model.Factura;

import java.util.List;
import java.util.Locale;

public class DetalleFacturaActivity extends AppCompatActivity {

    private TextView txtTitulo, txtCliente, txtCedula, txtDireccion, txtTelefono, txtFecha;
    private TextView txtSubtotal, txtIva15, txtIva12, txtImpSantuario, txtTotal;
    private RecyclerView recyclerProductos;
    private DetalleProductoAdapter adapter;
    private android.widget.ImageButton btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_factura);
        vincularVistas();

        btnVolver.setOnClickListener(v -> finish()); // Regresar

        long facturaId = getIntent().getLongExtra("FACTURA_ID", -1);

        if (facturaId == -1) {
            Toast.makeText(this, "Error: No se encontró el ID de la factura.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        cargarDetalles(facturaId);
    }

    private void vincularVistas() {
        txtTitulo = findViewById(R.id.txtDetalleFacturaTitulo);
        txtCliente = findViewById(R.id.txtDetalleFacturaCliente);
        txtCedula = findViewById(R.id.txtDetalleFacturaCedula);
        txtDireccion = findViewById(R.id.txtDetalleFacturaDireccion);
        txtTelefono = findViewById(R.id.txtDetalleFacturaTelefono);
        txtFecha = findViewById(R.id.txtDetalleFacturaFecha);
        txtSubtotal = findViewById(R.id.txtDetalleSubtotal);
        txtIva15 = findViewById(R.id.txtDetalleIva15);
        txtIva12 = findViewById(R.id.txtDetalleIva12);
        txtImpSantuario = findViewById(R.id.txtDetalleImpSantuario);
        txtTotal = findViewById(R.id.txtDetalleFacturaTotal);
        recyclerProductos = findViewById(R.id.recyclerDetalleProductos);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));
        btnVolver = findViewById(R.id.btnVolver);
    }

    private void cargarDetalles(long facturaId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            Factura factura = db.inventarioDao().obtenerFacturaPorId(facturaId);
            List<DetalleParaLogica> detallesCompletos = db.inventarioDao()
                    .obtenerDetallesCompletosParaLogica(facturaId);

            runOnUiThread(() -> {
                if (factura == null || detallesCompletos == null) {
                    Toast.makeText(this, "Error al cargar los datos de la factura.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 1. Llenar encabezado
                txtTitulo.setText(String.format(Locale.US, "Detalle de Factura #%d", factura.id_Factura));
                txtCliente.setText(String.format("Cliente: %s", factura.nombreCliente));
                txtCedula.setText(String.format("C.I.: %s", factura.cedula));
                txtDireccion.setText(String.format("Dir: %s", factura.direccion));
                txtTelefono.setText(String.format("Telf: %s", factura.celular));
                txtFecha.setText(String.format("Fecha: %s", factura.fecha));

                // 2. Llenar lista de productos
                adapter = new DetalleProductoAdapter(detallesCompletos);
                recyclerProductos.setAdapter(adapter);

                // 3. Calcular y mostrar desglose de pie de página
                calcularYMostrarTotales(detallesCompletos);
            });
        });
    }

    private void calcularYMostrarTotales(List<DetalleParaLogica> detalles) {
        double subtotal = 0;
        double totalIva15 = 0;
        double totalIva12 = 0;
        double totalImpSantuario = 0;

        for (DetalleParaLogica item : detalles) {
            double precioBase = item.producto.precioBase;
            subtotal += precioBase;

            // Cálculo de impuestos por producto
            if (item.categoria.iva == 15.0) {
                totalIva15 += precioBase * 0.15;
            }
            if (item.categoria.iva == 12.0) {
                totalIva12 += precioBase * 0.12;
            }
            if (item.categoria.impuesto == 5.0) {
                totalImpSantuario += precioBase * 0.05;
            }
        }

        double totalFinal = subtotal + totalIva15 + totalIva12 + totalImpSantuario;

        // Mostrar valores
        txtSubtotal.setText(String.format(Locale.US, "Subtotal: $%.2f", subtotal));
        txtTotal.setText(String.format(Locale.US, "Total Pagado: $%.2f", totalFinal));

        // Mostrar y llenar solo los impuestos que aplican
        if (totalIva15 > 0) {
            txtIva15.setText(String.format(Locale.US, "IVA (15%%): $%.2f", totalIva15));
            txtIva15.setVisibility(View.VISIBLE);
        }
        if (totalIva12 > 0) {
            txtIva12.setText(String.format(Locale.US, "IVA (12%%): $%.2f", totalIva12));
            txtIva12.setVisibility(View.VISIBLE);
        }
        if (totalImpSantuario > 0) {
            txtImpSantuario.setText(String.format(Locale.US, "Imp. Santuario (5%%): $%.2f", totalImpSantuario));
            txtImpSantuario.setVisibility(View.VISIBLE);
        }
    }
}