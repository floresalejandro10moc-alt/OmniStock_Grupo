package com.example.proyectofinalpoo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.logic.ProductoBase;
import com.example.proyectofinalpoo.logic.ProductoMapper;
import com.example.proyectofinalpoo.model.ProductoConCategoria;
import com.example.proyectofinalpoo.util.DataGenerator;
import com.example.proyectofinalpoo.util.ProductoAdapter;
import com.example.proyectofinalpoo.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SessionManager session;
    private RecyclerView rvProductos;
    private ProductoAdapter adapter;
    private ProductoViewModel productoViewModel;
    private Button btnAgregarProducto, btnHistorial, btnCarrito, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- LÓGICA DE CARGA ÚNICA (De Incoming) ---
        android.content.SharedPreferences prefs = getSharedPreferences("OmniStockPrefs", MODE_PRIVATE);
        boolean datosCargados = prefs.getBoolean("datos_iniciales_cargados", false);
        if (!datosCargados) {
            DataGenerator.cargar200Datos(this);
            prefs.edit().putBoolean("datos_iniciales_cargados", true).apply();
            Toast.makeText(this, "Base de datos inicializada", Toast.LENGTH_SHORT).show();
        }

        // Session
        session = new SessionManager(this);
        if (!session.estaLogueado()) {
            irAlLogin();
            return;
        }

        // UI Binding
        TextView tvBienvenida = findViewById(R.id.tvBienvenidaUsuario);
        tvBienvenida.setText("Bienvenido, " + session.getAlias());

        rvProductos = findViewById(R.id.rvProductos);
        // Buttons (added in XML)
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnCarrito = findViewById(R.id.btnIrAlCarrito);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Recycler Logic (HEAD)
        rvProductos.setLayoutManager(new GridLayoutManager(this, 2));
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        productoViewModel.getAllProductos().observe(this, productosConCategoria -> {
            List<ProductoBase> productsLogic = new ArrayList<>();
            for (ProductoConCategoria pcc : productosConCategoria) {
                 productsLogic.add(ProductoMapper.convertirEntidadALogica(pcc.producto, pcc.categoria));
            }
            adapter = new ProductoAdapter(productsLogic, this);
            rvProductos.setAdapter(adapter);
        });

        // Permissions (Incoming)
        if (!session.esAdmin()) {
            btnAgregarProducto.setVisibility(View.GONE);
        } else {
             btnAgregarProducto.setVisibility(View.VISIBLE);
        }

        // Listeners
        btnCerrarSesion.setOnClickListener(v -> {
            session.cerrarSesion();
            irAlLogin();
        });

        btnCarrito.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CarritoActivity.class));
        });

        btnHistorial.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, HistorialFacturasActivity.class));
        });

        btnAgregarProducto.setOnClickListener(v -> {
             Toast.makeText(this, "Próximamente: Crear Producto", Toast.LENGTH_SHORT).show();
             // startActivity(new Intent(MainActivity.this, AltaProductoActivity.class));
        });
    }

    private void irAlLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}