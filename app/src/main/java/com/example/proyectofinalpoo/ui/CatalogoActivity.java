package com.example.proyectofinalpoo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.model.Categoria;
import com.example.proyectofinalpoo.model.Producto;
import com.example.proyectofinalpoo.util.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CatalogoAdapter adapter;
    private Button btnIrAlCarrito;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        recyclerView = findViewById(R.id.recyclerCatalogo);
        btnIrAlCarrito = findViewById(R.id.btnIrAlCarritoDesdeCatalogo);
        session = new SessionManager(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarDatos();

        btnIrAlCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(this, CarritoActivity.class);
            startActivity(intent);
        });
    }

    private void cargarDatos() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            boolean esAdmin = session.esAdmin();
            List<Producto> productos;

            if (esAdmin) {
                productos = db.inventarioDao().obtenerTodosProductos();
            } else {
                productos = db.inventarioDao().obtenerProductosActivos();
            }

            List<Categoria> categoriasList = db.inventarioDao().obtenerCategorias();

            Map<Integer, Categoria> categoriasMap = new HashMap<>();
            for (Categoria c : categoriasList) {
                categoriasMap.put(c.id_Categoria, c);
            }

            runOnUiThread(() -> {
                adapter = new CatalogoAdapter(productos, categoriasMap, esAdmin);

                adapter.setOnProductoActionListener(new CatalogoAdapter.OnProductoActionListener() {
                    @Override
                    public void onEdit(Producto producto) {
                        Intent intent = new Intent(CatalogoActivity.this, RegistroProductoActivity.class);
                        intent.putExtra("extra_id_producto", producto.id_Producto);
                        startActivity(intent);
                    }

                    @Override
                    public void onToggleStatus(Producto producto) {
                        String nuevoEstado = "INA".equals(producto.estado) ? "ACT" : "INA";
                        String accionInfo = "INA".equals(producto.estado) ? "activar" : "deshabilitar";

                        new androidx.appcompat.app.AlertDialog.Builder(CatalogoActivity.this)
                                .setTitle("Confirmar Acción")
                                .setMessage("¿Estás seguro de " + accionInfo + " el producto " + producto.nombre + "?")
                                .setPositiveButton("Sí", (dialog, which) -> {
                                    AppDatabase.databaseWriteExecutor.execute(() -> {
                                        AppDatabase db = AppDatabase.getDatabase(CatalogoActivity.this);
                                        db.inventarioDao().actualizarEstadoProducto(producto.id_Producto, nuevoEstado);
                                        runOnUiThread(() -> {
                                            android.widget.Toast.makeText(CatalogoActivity.this,
                                                    "Producto actualizado a " + nuevoEstado,
                                                    android.widget.Toast.LENGTH_SHORT).show();
                                            cargarDatos(); // Reload list
                                        });
                                    });
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }

                    @Override
                    public void onDelete(Producto producto) {
                        new androidx.appcompat.app.AlertDialog.Builder(CatalogoActivity.this)
                                .setTitle("Confirmar Eliminación")
                                .setMessage("¿Estás seguro de eliminar " + producto.nombre + "?")
                                .setPositiveButton("Sí", (dialog, which) -> {
                                    AppDatabase.databaseWriteExecutor.execute(() -> {
                                        AppDatabase db = AppDatabase.getDatabase(CatalogoActivity.this);
                                        db.inventarioDao().eliminarProducto(producto);
                                        runOnUiThread(() -> {
                                            android.widget.Toast.makeText(CatalogoActivity.this, "Producto eliminado",
                                                    android.widget.Toast.LENGTH_SHORT).show();
                                            cargarDatos(); // Reload list
                                        });
                                    });
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                });

                recyclerView.setAdapter(adapter);
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }
}
