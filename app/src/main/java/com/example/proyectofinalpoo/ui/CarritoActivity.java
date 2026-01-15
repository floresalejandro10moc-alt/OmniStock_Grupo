package com.example.proyectofinalpoo.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.logic.CarritoManager;
import com.example.proyectofinalpoo.logic.ProductoBase;
import com.example.proyectofinalpoo.logic.PruebaLogica;

import java.util.List;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CarritoAdapter adapter;
    private TextView txtTotal;
    private Button btnPagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        // 1. Conectar variables con el XML
        recyclerView = findViewById(R.id.recyclerCarrito);
        txtTotal = findViewById(R.id.txtTotalPagar);
        btnPagar = findViewById(R.id.btnFinalizarCompra);

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

        // 5. ACCIÓN DEL BOTÓN PAGAR
        btnPagar.setOnClickListener(v -> {
            Toast.makeText(this, "Simulando transacción...", Toast.LENGTH_SHORT).show();
            // Aquí iría la lógica de guardar en BD (que haremos en Semana 3)
        });
    }

    private void actualizarTotal() {
        double total = CarritoManager.getInstance().calcularTotalPagar();
        txtTotal.setText(String.format("$%.2f", total));
    }
}