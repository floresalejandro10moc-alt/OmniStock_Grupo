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
import com.example.proyectofinalpoo.model.DetalleFactura;
import com.example.proyectofinalpoo.model.Factura;

import java.util.List;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CarritoAdapter adapter;
    private TextView txtTotal, txtSubtotal, txtImpuestos, txtCantidad;
    private Button btnPagar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        // 1. Conectar variables con el XML
        recyclerView = findViewById(R.id.recyclerCarrito);
        txtTotal = findViewById(R.id.txtTotalPagar);
        txtSubtotal = findViewById(R.id.txtSubtotal); // Nuevo
        txtImpuestos = findViewById(R.id.txtImpuestos); // Nuevo
        txtCantidad = findViewById(R.id.txtCantidadItems); // Nuevo
        btnPagar = findViewById(R.id.btnFinalizarCompra);
        btnCancelar = findViewById(R.id.btnCancelar);
        // 2. CARGAR DATOS DE PRUEBA (MOCKING)
        // Aquí simulamos que el usuario ya agregó cosas.
        // Usamos tu clase 'PruebaLogica' para obtener la lista falsa.
        List<ProductoBase> misProductosFalsos = PruebaLogica.getProductosSimulados();

        // Los metemos al Manager para que calcule el total
        CarritoManager manager = CarritoManager.getInstance();
        manager.vaciarCarrito(); // Limpiar por si acaso
        for (ProductoBase p : misProductosFalsos) {
            manager.agregarProducto(p);
        }

        // 3. CONFIGURAR EL RECYCLERVIEW
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarritoAdapter(manager.getCarrito());
        recyclerView.setAdapter(adapter);

        // 4. MOSTRAR EL TOTAL
        actualizarTotal();

        // Lógica del botón Cancelar
        btnCancelar.setOnClickListener(v -> {
            // Simplemente cerramos esta ventana y Android vuelve a la anterior
            finish();
        });

        btnPagar.setOnClickListener(v -> {
            // 1. Validar que hay cosas en el carrito
            if (manager.getCarrito().isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Preparar datos para guardar
            double totalVenta = manager.calcularTotalPagar();
            String fechaHoy = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

            // 3. Ejecutar Transacción en Segundo Plano
            AppDatabase.databaseWriteExecutor.execute(() -> {
                AppDatabase db = AppDatabase.getDatabase(CarritoActivity.this);

                try {
                    // A) Crear Cabecera de Factura
                    Factura nuevaFactura = new Factura(fechaHoy, totalVenta, "Consumidor Final");

                    // B) Insertar y OBTENER EL ID generado (Vital para los detalles)
                    long idFacturaGenerado = db.inventarioDao().insertarFactura(nuevaFactura);

                    // C) Recorrer el carrito y guardar cada producto
                    for (ProductoBase prod : manager.getCarrito()) {

                        // C.1 Guardar Detalle
                        DetalleFactura detalle = new DetalleFactura(
                                (int) idFacturaGenerado,
                                prod.getId(), // Asegúrate de haber hecho el PASO 0
                                1, // Por ahora asumimos cantidad 1 por item en la lista
                                prod.calcularPrecioFinal(),
                                prod.calcularPrecioFinal()
                        );
                        db.inventarioDao().insertarDetalle(detalle);

                        // C.2 DESCONTAR STOCK (¡Requisito de Rúbrica!)
                        db.productoDao().actualizarStock(prod.getId(), 1);
                    }

                    // D) Limpiar carrito y avisar
                    manager.vaciarCarrito();

                    runOnUiThread(() -> {
                        Toast.makeText(this, "¡Venta Exitosa! Factura #" + idFacturaGenerado, Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged(); // Refrescar lista vacía
                        finish(); // Volver al menú
                    });

                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error en transacción: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            });
        });
    }

    private void actualizarTotal() {
        CarritoManager manager = CarritoManager.getInstance();

        // 1. Cálculos
        double totalPagar = manager.calcularTotalPagar(); // Precio Final con todo
        double subtotalBase = manager.calcularSubtotalBase(); // Precio Base sin nada
        double totalImpuestos = totalPagar - subtotalBase; // La diferencia son los impuestos
        int cantidadItems = manager.getCarrito().size();

        // 2. Mostrar en pantalla
        txtSubtotal.setText(String.format("$%.2f", subtotalBase));
        txtImpuestos.setText(String.format("$%.2f", totalImpuestos));
        txtTotal.setText(String.format("$%.2f", totalPagar));

        // Pluralización simple (1 producto vs 5 productos)
        String textoCantidad = (cantidadItems == 1) ? "1 producto" : cantidadItems + " productos";
        txtCantidad.setText(textoCantidad);
    }
}
