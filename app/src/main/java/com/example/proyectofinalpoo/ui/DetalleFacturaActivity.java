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
            List<DetalleVisual> detalles = db.inventarioDao().obtenerDetallesVisuales(facturaId);

            runOnUiThread(() -> {
                if (factura != null) {
                    // Llenar cabecera completa
                    txtTitulo.setText("Factura #" + factura.id_Factura);
                    txtCliente.setText("Cliente: " + factura.nombreCliente);
                    txtCedula.setText("C.I.: " + factura.cedula); // Nuevo
                    txtDireccion.setText("Dir: " + factura.direccion); // Nuevo
                    txtFecha.setText("Fecha: " + factura.fecha);
                    txtTotal.setText(String.format("Total: $%.2f", factura.total));

                    // Calcular cantidad total
                    int totalItems = 0;
                    for (DetalleVisual dv : detalles) {
                        totalItems += dv.cantidad;
                    }
                    txtCantidad.setText("Cantidad Total: " + totalItems);

                    // Pasar la lista visual al adaptador
                    adapter = new DetalleProductoAdapter(detalles);
                    recyclerProductos.setAdapter(adapter);
                }
            });
        });
    }
}