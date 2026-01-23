package com.example.proyectofinalpoo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.model.Categoria;
import com.example.proyectofinalpoo.model.Producto;
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

    // Recyclers
    RecyclerView recyclerMain;
    RecyclerView recyclerCategories;
    RecyclerView recyclerFeatured;

    // Adapters
    CatalogoAdapter adapter;
    CategoryAdapter categoryAdapter;
    FeaturedAdapter featuredAdapter;

    SessionManager session;
    boolean isFabOpen = false;

    // Search & Filter
    android.widget.EditText etBusqueda;
    ImageButton btnClearSearch;
    int selectedCategoryId = -1; // -1 = Todos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- LÓGICA DE CARGA ÚNICA ---
        android.content.SharedPreferences prefs = getSharedPreferences("OmniStockPrefs", MODE_PRIVATE);
        boolean datosCargados = prefs.getBoolean("datos_iniciales_cargados", false);

        if (!datosCargados) {
            DataGenerator.cargar200Datos(this);
            prefs.edit().putBoolean("datos_iniciales_cargados", true).apply();
            Toast.makeText(this, "Base de datos inicializada", Toast.LENGTH_SHORT).show();
        }
        // ----------------------------

        // 1. Inicializar SessionManager
        session = new SessionManager(this);

        // 2. Verificar si NO está logueado
        if (!session.estaLogueado()) {
            irAlLogin();
            return;
        }

        // 3. Vincular Vistas
        tvNombreUser = findViewById(R.id.tvNombreUsuario);
        btnCerrar = findViewById(R.id.btnCerrarSesion);
        btnCarrito = findViewById(R.id.btnIrAlCarrito);
        btnHistorial = findViewById(R.id.btnHistorial);

        // Search
        etBusqueda = findViewById(R.id.etBusqueda);
        btnClearSearch = findViewById(R.id.btnClearSearch);

        // Recyclers
        recyclerMain = findViewById(R.id.recyclerMain);
        recyclerCategories = findViewById(R.id.recyclerCategories);
        recyclerFeatured = findViewById(R.id.recyclerFeatured);

        // Layout Managers
        recyclerMain.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerCategories.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this,
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));
        recyclerFeatured.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this,
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));

        // FABs
        fabMain = findViewById(R.id.fabMain);
        fabAddProduct = findViewById(R.id.fabAddProduct);
        fabAddCategory = findViewById(R.id.fabAddCategory);
        lblAddProduct = findViewById(R.id.lblAddProduct);
        lblAddCategory = findViewById(R.id.lblAddCategory);

        // 4. Mostrar nombre
        String alias = session.getAliasLogueado();
        tvNombreUser.setText("¡Bienvenido,\n" + alias + "!");

        // Cargar Datos
        cargarCategorias();
        cargarDestacados();
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

        // 5. LÓGICA DE ROLES
        if (!session.esAdmin()) {
            fabMain.setVisibility(View.GONE);
        } else {
            fabMain.setVisibility(View.VISIBLE);
            fabMain.setOnClickListener(v -> toggleFabMenu());
        }

        // --- LISTENERS ---
        btnCerrar.setOnClickListener(v -> {
            session.cerrarSesion();
            irAlLogin();
        });

        btnCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CarritoActivity.class);
            startActivity(intent);
        });

        btnHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistorialFacturasActivity.class);
            startActivity(intent);
        });

        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroProductoActivity.class);
            startActivity(intent);
            toggleFabMenu();
        });
        fabAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
            startActivity(intent);
            toggleFabMenu();
        });
    }

    private void toggleFabMenu() {
        if (isFabOpen) {
            fabAddProduct.setVisibility(View.GONE);
            lblAddProduct.setVisibility(View.GONE);
            fabAddCategory.setVisibility(View.GONE);
            lblAddCategory.setVisibility(View.GONE);
            fabMain.setImageResource(R.drawable.ic_add);
            isFabOpen = false;
        } else {
            fabAddProduct.setVisibility(View.VISIBLE);
            lblAddProduct.setVisibility(View.VISIBLE);
            fabAddCategory.setVisibility(View.VISIBLE);
            lblAddCategory.setVisibility(View.VISIBLE);
            isFabOpen = true;
        }
    }

    private void cargarCategorias() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            List<Categoria> dbCategorias = db.inventarioDao().obtenerCategorias();

            // Add "Todos" option
            Categoria all = new Categoria();
            all.id_Categoria = -1; // Public field
            all.nombre = "Todos"; // Public field
            dbCategorias.add(0, all);

            runOnUiThread(() -> {
                categoryAdapter = new CategoryAdapter(dbCategorias, categoria -> {
                    selectedCategoryId = categoria.id_Categoria; // Public field
                    cargarProductos(etBusqueda.getText().toString());
                });
                recyclerCategories.setAdapter(categoryAdapter);
            });
        });
    }

    private void cargarDestacados() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            List<Producto> all = db.inventarioDao().obtenerTodosProductos();
            List<Producto> featured = new java.util.ArrayList<>();
            if (all.size() > 5) {
                featured.addAll(all.subList(0, 5));
            } else {
                featured.addAll(all);
            }

            runOnUiThread(() -> {
                featuredAdapter = new FeaturedAdapter(featured);
                recyclerFeatured.setAdapter(featuredAdapter);
            });
        });
    }

    private void cargarProductos(String query) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            boolean esAdmin = session.esAdmin();
            List<Producto> productos;

            // 1. Filter by Search Query & Role
            if (query == null || query.trim().isEmpty()) {
                if (esAdmin) {
                    productos = db.inventarioDao().obtenerTodosProductos();
                } else {
                    productos = db.inventarioDao().obtenerProductosActivos();
                }
            } else {
                if (esAdmin) {
                    productos = db.inventarioDao().buscarProductosPorNombre(query);
                } else {
                    productos = db.inventarioDao().buscarProductosActivosPorNombre(query);
                }
            }

            // 2. Filter by Category (In Memory)
            if (selectedCategoryId != -1) {
                List<Producto> filtered = new java.util.ArrayList<>();
                for (Producto p : productos) {
                    if (p.id_Categoria == selectedCategoryId) { // Public field
                        filtered.add(p);
                    }
                }
                productos = filtered;
            }

            List<Categoria> categoriasList = db.inventarioDao().obtenerCategorias();
            Map<Integer, Categoria> categoriasMap = new HashMap<>();
            for (Categoria c : categoriasList) {
                categoriasMap.put(c.id_Categoria, c); // Public field
            }

            List<Producto> finalProductos = productos;
            runOnUiThread(() -> {
                adapter = new CatalogoAdapter(finalProductos, categoriasMap, esAdmin);

                adapter.setOnProductoActionListener(new CatalogoAdapter.OnProductoActionListener() {
                    @Override
                    public void onEdit(Producto producto) {
                        Intent intent = new Intent(MainActivity.this, RegistroProductoActivity.class);
                        intent.putExtra("extra_id_producto", producto.id_Producto);
                        startActivity(intent);
                    }

                    @Override
                    public void onToggleStatus(Producto producto) {
                        String nuevoEstado = "INA".equals(producto.estado) ? "ACT" : "INA";
                        String accionInfo = "INA".equals(producto.estado) ? "activar" : "deshabilitar";

                        new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                                .setTitle("Confirmar Acción")
                                .setMessage("¿Estás seguro de " + accionInfo + " el producto " + producto.nombre + "?")
                                .setPositiveButton("Sí", (dialog, which) -> {
                                    AppDatabase.databaseWriteExecutor.execute(() -> {
                                        AppDatabase db = AppDatabase.getDatabase(MainActivity.this);
                                        db.inventarioDao().actualizarEstadoProducto(producto.id_Producto, nuevoEstado);
                                        runOnUiThread(() -> {
                                            android.widget.Toast.makeText(MainActivity.this,
                                                    "Producto actualizado a " + nuevoEstado,
                                                    android.widget.Toast.LENGTH_SHORT).show();
                                            cargarProductos(etBusqueda.getText().toString()); // Refresh list
                                        });
                                    });
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }

                    @Override
                    public void onDelete(Producto producto) {

                    }
                });

                recyclerMain.setAdapter(adapter);
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos("");
    }

    private void irAlLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}