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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CatalogoAdapter adapter;
    private Button btnIrAlCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        recyclerView = findViewById(R.id.recyclerCatalogo);
        btnIrAlCarrito = findViewById(R.id.btnIrAlCarritoDesdeCatalogo);

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
}
