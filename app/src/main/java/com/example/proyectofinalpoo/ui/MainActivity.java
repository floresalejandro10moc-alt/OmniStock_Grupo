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

    // Declaramos componentes UI
    ImageButton btnCerrar, btnCarrito, btnHistorial;
    com.google.android.material.floatingactionbutton.FloatingActionButton fabMain, fabAddProduct, fabAddCategory;
    TextView tvNombreUser, lblAddProduct, lblAddCategory;
    RecyclerView recyclerView;
    CatalogoAdapter adapter;
    SessionManager session;
    boolean isFabOpen = false;

    // Search
    android.widget.EditText etBusqueda;
    ImageButton btnClearSearch;

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

        // Binding Search
        etBusqueda = findViewById(R.id.etBusqueda);
        btnClearSearch = findViewById(R.id.btnClearSearch);

        // FABs
        fabMain = findViewById(R.id.fabMain);
        fabAddProduct = findViewById(R.id.fabAddProduct);
        fabAddCategory = findViewById(R.id.fabAddCategory);
        lblAddProduct = findViewById(R.id.lblAddProduct);
        lblAddCategory = findViewById(R.id.lblAddCategory);

        recyclerView = findViewById(R.id.recyclerMain);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Use Grid with 2 columns

        // 4. Mostrar nombre del usuario en la cabecera (Dos líneas)
        String alias = session.getAliasLogueado();
        tvNombreUser.setText("¡Bienvenido,\n" + alias + "!");

        // Carga inicial (Query vacío)
        cargarProductos("");

        // LOGIC SEARCH
        etBusqueda.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnClearSearch.setVisibility(View.VISIBLE);
                } else {
                    btnClearSearch.setVisibility(View.GONE);
                }
                cargarProductos(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });

        btnClearSearch.setOnClickListener(v -> {
            etBusqueda.setText("");
            cargarProductos("");
        });

        // 5. LÓGICA DE ROLES: Ocultar botón de "Nuevo Producto" si no es Admin
        if (!session.esAdmin()) {
            fabMain.setVisibility(View.GONE);
            // Los otros ya estan GONE por layout
        } else {
            fabMain.setVisibility(View.VISIBLE);
            fabMain.setOnClickListener(v -> toggleFabMenu());
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

        // E) Botón Nuevo Producto (Solo Admin)
        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroProductoActivity.class);
            startActivity(intent);
            toggleFabMenu(); // Close menu
        });
        // F) Botón Gestión de Categorías (Solo Admin)
        fabAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
            startActivity(intent);
            toggleFabMenu(); // Close menu
        });
    }

    private void toggleFabMenu() {
        if (isFabOpen) {
            fabAddProduct.setVisibility(View.GONE);
            lblAddProduct.setVisibility(View.GONE);
            fabAddCategory.setVisibility(View.GONE);
            lblAddCategory.setVisibility(View.GONE);
            fabMain.setImageResource(R.drawable.ic_add); // Change icon back to +
            isFabOpen = false;
        } else {
            fabAddProduct.setVisibility(View.VISIBLE);
            lblAddProduct.setVisibility(View.VISIBLE);
            fabAddCategory.setVisibility(View.VISIBLE);
            lblAddCategory.setVisibility(View.VISIBLE);
            // Optionally change icon to close
            // fabMain.setImageResource(R.drawable.ic_close);
            isFabOpen = true;
        }
    }

    private void cargarProductos(String query) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            List<Producto> productos;
            if (query == null || query.trim().isEmpty()) {
                productos = db.inventarioDao().obtenerTodosProductos();
            } else {
                productos = db.inventarioDao().buscarProductosPorNombre(query);
            }

            List<Categoria> categoriasList = db.inventarioDao().obtenerCategorias();

            Map<Integer, Categoria> categoriasMap = new HashMap<>();
            for (Categoria c : categoriasList) {
                categoriasMap.put(c.id_Categoria, c);
            }

            runOnUiThread(() -> {
                // Pasamos si es ADMIN para mostrar/ocultar stock
                boolean esAdmin = session.esAdmin();
                adapter = new CatalogoAdapter(productos, categoriasMap, esAdmin);
                recyclerView.setAdapter(adapter);
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos(""); // Recargar lista al volver
    }

    private void irAlLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        // Flags para limpiar el historial y que no puedan volver atrás
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}