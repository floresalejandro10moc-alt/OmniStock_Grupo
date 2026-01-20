package com.example.proyectofinalpoo.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.logic.CarritoManager;
import com.example.proyectofinalpoo.logic.ProductoBase;
import com.example.proyectofinalpoo.logic.PruebaLogica;
import com.example.proyectofinalpoo.model.Cliente;
import com.example.proyectofinalpoo.model.DetalleFactura;
import com.example.proyectofinalpoo.model.Factura;
import com.example.proyectofinalpoo.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CarritoAdapter adapter;
    private TextView txtTotal, txtSubtotal, txtImpuestos, txtCantidad;
    private Button btnPagar, btnCancelar;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        // 1. Conectar variables con el XML
        session = new SessionManager(this);
        recyclerView = findViewById(R.id.recyclerCarrito);
        txtTotal = findViewById(R.id.txtTotalPagar);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtImpuestos = findViewById(R.id.txtImpuestos);
        txtCantidad = findViewById(R.id.txtCantidadItems);
        btnPagar = findViewById(R.id.btnFinalizarCompra);
        btnCancelar = findViewById(R.id.btnCancelar);

        // 2. Cargar datos del carrito
        CarritoManager manager = CarritoManager.getInstance();

        // 3. Configurar el RecyclerView

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarritoAdapter(manager.getCarrito());
        recyclerView.setAdapter(adapter);

        // 4. Mostrar el total
        actualizarTotal();

        // Lógica del botón Cancelar
        btnCancelar.setOnClickListener(v -> finish());

        // Lógica del botón Pagar
        btnPagar.setOnClickListener(v -> {
            if (manager.getCarrito().isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            finalizarCompra(manager);
        });
    }

    private void finalizarCompra(CarritoManager manager) {
        double totalVenta = manager.calcularTotalPagar();
        String fechaHoy = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(CarritoActivity.this);

            try {
                String aliasLogueado = session.getAliasLogueado();
                Cliente clienteData = db.inventarioDao().obtenerClientePorAlias(aliasLogueado);

                String nombreFinal, cedulaFinal, dirFinal, telfFinal;

                if (clienteData != null) {
                    nombreFinal = clienteData.nombre + " " + clienteData.apellido;
                    cedulaFinal = clienteData.cedula;
                    dirFinal = clienteData.direccion;
                    telfFinal = clienteData.celular;
                } else {
                    nombreFinal = "Consumidor Final";
                    cedulaFinal = "9999999999";
                    dirFinal = "N/A";
                    telfFinal = "N/A";
                }

                Factura nuevaFactura = new Factura(fechaHoy, totalVenta, nombreFinal, cedulaFinal, dirFinal, telfFinal);
                long idFacturaGenerado = db.inventarioDao().insertarFactura(nuevaFactura);

                for (ProductoBase prod : manager.getCarrito()) {
                    DetalleFactura detalle = new DetalleFactura(
                            (int) idFacturaGenerado,
                            prod.getId(),
                            1, // Asumiendo cantidad 1
                            prod.calcularPrecioFinal(),
                            prod.calcularPrecioFinal());
                    db.inventarioDao().insertarDetalle(detalle);
                    db.inventarioDao().actualizarStock(prod.getId(), 1);
                }

                manager.vaciarCarrito();
                runOnUiThread(() -> {
                    Toast.makeText(this, "¡Venta Exitosa! Factura #" + idFacturaGenerado, Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                    actualizarTotal();
                    finish();
                });

            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error en transacción: " + e.getMessage(), Toast.LENGTH_LONG)
                        .show());
            }
        });
    }

    private void actualizarTotal() {
        CarritoManager manager = CarritoManager.getInstance();
        double totalPagar = manager.calcularTotalPagar();
        double subtotalBase = manager.calcularSubtotalBase();
        double totalImpuestos = totalPagar - subtotalBase;
        int cantidadItems = manager.getCarrito().size();

        txtSubtotal.setText(String.format(Locale.US, "$%.2f", subtotalBase));
        txtImpuestos.setText(String.format(Locale.US, "$%.2f", totalImpuestos));
        txtTotal.setText(String.format(Locale.US, "$%.2f", totalPagar));
        txtCantidad.setText(
                String.format(Locale.US, "%d %s", cantidadItems, (cantidadItems == 1) ? "producto" : "productos"));
    }
}