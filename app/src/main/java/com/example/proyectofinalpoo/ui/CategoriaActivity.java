package com.example.proyectofinalpoo.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase; // Revisa que este sea el nombre de tu BD
import com.example.proyectofinalpoo.model.Categoria;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoriaActivity extends AppCompatActivity {

    private RecyclerView rvCategorias;
    private CategoriaAdapter adapter;
    private List<Categoria> listaCategorias = new ArrayList<>();
    private AppDatabase db;
    private FloatingActionButton fabAdd;
    private android.widget.ImageButton btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        // 1. Inicializar base de datos y vistas
        db = AppDatabase.getDatabase(this);
        rvCategorias = findViewById(R.id.rvCategorias);
        fabAdd = findViewById(R.id.fabAddCategoria);
        btnVolver = findViewById(R.id.btnVolver);

        btnVolver.setOnClickListener(v -> finish()); // Regresar a Main

        rvCategorias.setLayoutManager(new LinearLayoutManager(this));

        // 2. Configurar el botón para agregar nueva categoría
        fabAdd.setOnClickListener(v -> mostrarDialogo(null));

        // 3. Cargar datos iniciales
        cargarDatos();
    }

    private void cargarDatos() {
        new Thread(() -> {
            listaCategorias = db.inventarioDao().obtenerCategorias();
            runOnUiThread(() -> {
                adapter = new CategoriaAdapter(listaCategorias, new CategoriaAdapter.OnCategoriaClickListener() {
                    @Override
                    public void onEdit(Categoria cat) {
                        mostrarDialogo(cat);
                    }

                    @Override
                    public void onDelete(Categoria cat) {
                        confirmarEliminacion(cat);
                    }
                });
                rvCategorias.setAdapter(adapter);
            });
        }).start();
    }

    // Diálogo único para Crear y Editar
    private void mostrarDialogo(Categoria catExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_categoria, null);

        EditText etNombre = view.findViewById(R.id.etNombreCat);
        EditText etDesc = view.findViewById(R.id.etDescCat);
        EditText etIva = view.findViewById(R.id.etIvaCat);
        EditText etImp = view.findViewById(R.id.etImpuestoCat);

        if (catExistente != null) {
            builder.setTitle("Editar Categoría");
            etNombre.setText(catExistente.nombre);
            etDesc.setText(catExistente.descripcion);
            etIva.setText(String.valueOf(catExistente.iva));
            etImp.setText(String.valueOf(catExistente.impuesto));
        } else {
            builder.setTitle("Nueva Categoría");
        }

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nom = etNombre.getText().toString();
            String desc = etDesc.getText().toString();
            double iva = Double.parseDouble(etIva.getText().toString().isEmpty() ? "0" : etIva.getText().toString());
            double imp = Double.parseDouble(etImp.getText().toString().isEmpty() ? "0" : etImp.getText().toString());

            new Thread(() -> {
                if (catExistente == null) {
                    // CREAR
                    Categoria nueva = new Categoria(nom, desc, iva, imp);
                    db.inventarioDao().insertarCategoria(nueva);
                } else {
                    // EDITAR
                    catExistente.nombre = nom;
                    catExistente.descripcion = desc;
                    catExistente.iva = iva;
                    catExistente.impuesto = imp;
                    db.inventarioDao().actualizarCategoria(catExistente);
                }
                runOnUiThread(() -> {
                    Toast.makeText(this, "Operación exitosa", Toast.LENGTH_SHORT).show();
                    cargarDatos(); // Refrescar lista
                });
            }).start();
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void confirmarEliminacion(Categoria cat) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar")
                .setMessage("¿Estás seguro de eliminar " + cat.nombre + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    new Thread(() -> {
                        db.inventarioDao().eliminarCategoria(cat);
                        runOnUiThread(this::cargarDatos);
                    }).start();
                })
                .setNegativeButton("No", null)
                .show();
    }
}