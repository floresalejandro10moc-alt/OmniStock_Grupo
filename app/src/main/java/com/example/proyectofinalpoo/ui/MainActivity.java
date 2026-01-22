package com.example.proyectofinalpoo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager; // Import GridLayoutManager
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.model.Categoria;
import com.example.proyectofinalpoo.model.Producto;
import com.example.proyectofinalpoo.util.DataGenerator;
import com.example.proyectofinalpoo.util.DataGenerator;
import com.example.proyectofinalpoo.util.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Declaramos todos los botones de la interfaz
    // Declaramos componentes UI
    ImageButton btnCerrar, btnCarrito, btnHistorial, btnNuevoProd, btnCategorias;
    TextView tvNombreUser;
    RecyclerView recyclerView;
    CatalogoAdapter adapter;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // --- LÓGICA DE CARGA ÚNICA ---
        android.content.SharedPreferences prefs = getSharedPreferences("OmniStockPrefs", MODE_PRIVATE);
        boolean datosCargados = prefs.getBoolean("datos_iniciales_cargados", false);

        if (!datosCargados) {
            // Solo se ejecuta si es la primera vez
            DataGenerator.cargar200Datos(this);

            // Guardamos que ya se hizo la carga
            prefs.edit().putBoolean("datos_iniciales_cargados", true).apply();
            Toast.makeText(this, "Base de datos inicializada", Toast.LENGTH_SHORT).show();
        }
        // ----------------------------
        // Dentro del onCreate de MainActivity.java

        // 1. Inicializar SessionManager
        session = new SessionManager(this);

        // 2. Verificar si NO está logueado (Seguridad)
        if (!session.estaLogueado()) {
            irAlLogin();
            return; // Detenemos la ejecución si no hay sesión
        }

        // 3. Vincular Vistas con el XML
        tvNombreUser = findViewById(R.id.tvNombreUsuario);
        btnCerrar = findViewById(R.id.btnCerrarSesion);
        btnCarrito = findViewById(R.id.btnIrAlCarrito);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnCategorias = findViewById(R.id.btnGestionCategorias);

        // Estos IDs deben coincidir con tu activity_main.xml
        // btnCatalogo ya no existe, ahora es el recycler en la misma pantalla
        btnNuevoProd = findViewById(R.id.btnAgregarProducto);
        recyclerView = findViewById(R.id.recyclerMain);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Use Grid with 2 columns

        cargarProductos();

        // 4. Mostrar nombre del usuario en la cabecera
        String alias = session.getAliasLogueado();
        tvNombreUser.setText("¡Bienvenido, " + alias + "!");

        // 5. LÓGICA DE ROLES: Ocultar botón de "Nuevo Producto" si no es Admin
        if (!session.esAdmin()) {
            btnNuevoProd.setVisibility(View.GONE);
            btnCategorias.setVisibility(View.GONE); // Solo el admin podrá verlo
        }

        // --- LISTENERS (ACCIONES DE LOS BOTONES) ---

        // A) Botón Cerrar Sesión
        btnCerrar.setOnClickListener(v -> {
            session.cerrarSesion();
            irAlLogin();
        });

        // B) Botón Ir al Carrito
        btnCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CarritoActivity.class);
            startActivity(intent);
        });

        // C) Botón Ir al Historial
        btnHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistorialFacturasActivity.class);
            startActivity(intent);
        });

        // D) Cargar Productos al regresar (para actualizar la lista si algo cambia)
        // Se maneja automático al inicio, pero si agregan prod y vuelven:
        // Lo ideal sería onResume(), pero por simplicidad lo dejamos en onCreate
        // inicial.

        // E) Botón Nuevo Producto (Solo Admin)
        btnNuevoProd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroProductoActivity.class);
            startActivity(intent);
        });
        // F) Botón Gestión de Categorías (Solo Admin)
        btnCategorias.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
            startActivity(intent);
        });
    }

    private void cargarProductos() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            List<Producto> productos = db.inventarioDao().obtenerTodosProductos();
            List<Categoria> categoriasList = db.inventarioDao().obtenerCategorias();

            Map<Integer, Categoria> categoriasMap = new HashMap<>();
            for (Categoria c : categoriasList) {
                categoriasMap.put(c.id_Categoria, c);
            }

            runOnUiThread(() -> {
                adapter = new CatalogoAdapter(productos, categoriasMap);
                recyclerView.setAdapter(adapter);
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos(); // Recargar lista al volver
    }

    private void irAlLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        // Flags para limpiar el historial y que no puedan volver atrás
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}