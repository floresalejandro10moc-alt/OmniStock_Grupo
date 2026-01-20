package com.example.proyectofinalpoo.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.model.DetalleVisual;
import com.example.proyectofinalpoo.model.Factura;

import java.util.List;

public class DetalleFacturaActivity extends AppCompatActivity {

    private TextView txtCedula, txtDireccion, txtTitulo, txtCliente, txtFecha, txtTotal, txtCantidad;
    private RecyclerView recyclerProductos;
    private DetalleProductoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_factura);

        txtTitulo = findViewById(R.id.txtDetalleFacturaTitulo);
        txtCliente = findViewById(R.id.txtDetalleFacturaCliente);
        txtFecha = findViewById(R.id.txtDetalleFacturaFecha);
        txtTotal = findViewById(R.id.txtDetalleFacturaTotal);
        txtCantidad = findViewById(R.id.txtDetalleFacturaCantidad);
        txtCedula = findViewById(R.id.txtDetalleFacturaCedula);
        txtDireccion = findViewById(R.id.txtDetalleFacturaDireccion);
        recyclerProductos = findViewById(R.id.recyclerDetalleProductos);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));

        long facturaId = getIntent().getLongExtra("FACTURA_ID", -1);

        if (facturaId != -1) {
            cargarDetalles(facturaId);
        }
    }

    private void cargarDetalles(long facturaId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            Factura factura = db.inventarioDao().obtenerFacturaPorId(facturaId);

            // 1. RECUPERACION MANUAL DEL CLIENTE (Si existe ID)
            if (factura != null && factura.id_cliente != null) {
                com.example.proyectofinalpoo.model.Cliente clienteReal = db.inventarioDao()
                        .obtenerCliente(factura.id_cliente);
                if (clienteReal != null) {
                    factura.nombreCliente = clienteReal.nombre + " " + clienteReal.apellido;
                    factura.cedula = clienteReal.cedula;
                    factura.direccion = clienteReal.direccion;
                    // factura.celular = clienteReal.celular; // Si hiciera falta
                }
            }

            // 2. RECUPERACION MANUAL DE PRODUCTOS (Evitando JOIN fallido)
            java.util.List<com.example.proyectofinalpoo.model.DetalleFactura> rawDetalles = db.inventarioDao()
                    .obtenerDetallesPorFactura(facturaId);
            java.util.List<DetalleVisual> detallesVisuales = new java.util.ArrayList<>();

            for (com.example.proyectofinalpoo.model.DetalleFactura raw : rawDetalles) {
                DetalleVisual dv = new DetalleVisual();
                dv.cantidad = raw.cantidad;
                dv.precioUnitario = raw.precioUnitario;
                dv.subtotal = raw.subtotal;

                // Buscar nombre del producto
                com.example.proyectofinalpoo.model.Producto p = db.inventarioDao().obtenerProducto(raw.id_Producto);
                if (p != null) {
                    dv.nombreProducto = p.nombre;
                } else {
                    dv.nombreProducto = "Producto ID: " + raw.id_Producto + " (No encontrado)";
                }
                detallesVisuales.add(dv);
            }

            // 3. ACTUALIZAR UI
            runOnUiThread(() -> {
                if (factura != null) {
                    txtTitulo.setText("Factura #" + factura.id_Factura);
                    txtCliente.setText("Cliente: " + factura.nombreCliente);
                    txtCedula.setText("C.I.: " + factura.cedula);
                    txtDireccion.setText("Dir: " + factura.direccion);
                    txtFecha.setText("Fecha: " + factura.fecha);
                    txtTotal.setText(String.format("Total: $%.2f", factura.total));

                    int totalItems = 0;
                    for (DetalleVisual dv : detallesVisuales) {
                        totalItems += dv.cantidad;
                    }
                    txtCantidad.setText("Cantidad Total: " + totalItems);

                    if (detallesVisuales.isEmpty()) {
                        android.widget.Toast.makeText(DetalleFacturaActivity.this,
                                "Advertencia: No se encontraron productos para la Factura ID " + factura.id_Factura,
                                android.widget.Toast.LENGTH_LONG).show();
                    }

                    adapter = new DetalleProductoAdapter(detallesVisuales);
                    recyclerProductos.setAdapter(adapter);
                }
            });
        });
    }
}