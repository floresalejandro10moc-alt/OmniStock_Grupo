package com.example.proyectofinalpoo.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.model.Categoria;
import com.example.proyectofinalpoo.model.Producto;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class RegistroProductoActivity extends AppCompatActivity {

    private TextInputEditText etNombre, etDescripcion, etPrecio, etStock, etFechaCaducidad;
    private TextInputLayout layoutFechaCaducidad;
    private Spinner spinnerCategoria;
    private CheckBox cbTemporadaAnterior;
    private Button btnGuardar, btnCancelar;
    private android.widget.ImageButton btnVolver;

    // Mapa para saber el ID de categoría según la posición del Spinner
    // O simplemente una lista paralela
    private List<Categoria> listaCategorias;
    private int idProductoEditar = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_producto);

        // 1. Vincular Vistas
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        etStock = findViewById(R.id.etStock);
        etFechaCaducidad = findViewById(R.id.etFechaCaducidad);
        layoutFechaCaducidad = findViewById(R.id.layoutFechaCaducidad);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        cbTemporadaAnterior = findViewById(R.id.cbTemporadaAnterior);
        btnGuardar = findViewById(R.id.btnGuardarProducto);
        btnCancelar = findViewById(R.id.btnCancelarRegistro);
        btnVolver = findViewById(R.id.btnVolver);

        // Check for Edit Mode
        if (getIntent().hasExtra("extra_id_producto")) {
            idProductoEditar = getIntent().getIntExtra("extra_id_producto", -1);
            if (idProductoEditar != -1) {
                // Change UI for Edit
                btnGuardar.setText("Actualizar Producto");
                // Load Data
                cargarDatosProducto(idProductoEditar);
            }
        }

        // 2. Cargar Categorías
        cargarCategorias();

        // 3. Listener del Spinner para mostrar campos dinámicos
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listaCategorias == null || listaCategorias.isEmpty())
                    return;

                Categoria catSeleccionada = listaCategorias.get(position);
                actualizarCamposDinamicos(catSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        // 4. Botón Guardar
        btnGuardar.setOnClickListener(v -> guardarProducto());

        // 5. Botón Cancelar
        btnCancelar.setOnClickListener(v -> finish());
        btnVolver.setOnClickListener(v -> finish()); // Regresar a Main
        btnVolver.setOnClickListener(v -> finish()); // Regresar a Main

        // 6. Listener para Fecha
        etFechaCaducidad.setOnClickListener(v -> mostrarDatePicker());
    }

    private void mostrarDatePicker() {
        final java.util.Calendar c = java.util.Calendar.getInstance();
        int year = c.get(java.util.Calendar.YEAR);
        int month = c.get(java.util.Calendar.MONTH);
        int day = c.get(java.util.Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Formato dd/MM/yyyy
                    // Nota: monthOfYear es 0-indexed
                    String fechaSeleccionada = String.format(java.util.Locale.getDefault(), "%02d/%02d/%04d",
                            dayOfMonth, monthOfYear + 1, year1);
                    etFechaCaducidad.setText(fechaSeleccionada);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void cargarDatosProducto(int id) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            Producto p = db.inventarioDao().obtenerProductoPorId(id);
            if (p != null) {
                runOnUiThread(() -> {
                    etNombre.setText(p.nombre);
                    etDescripcion.setText(p.descripcion);
                    etPrecio.setText(String.valueOf(p.precioBase));
                    etStock.setText(String.valueOf(p.stock));

                    // Handle Category Spinner selection
                    if (listaCategorias != null) {
                        for (int i = 0; i < listaCategorias.size(); i++) {
                            if (listaCategorias.get(i).id_Categoria == p.id_Categoria) {
                                spinnerCategoria.setSelection(i);
                                actualizarCamposDinamicos(listaCategorias.get(i));
                                break;
                            }
                        }
                    }

                    // Handle specifics
                    cbTemporadaAnterior.setChecked(p.esTemporadaAnterior == 1);
                    if (p.fechaCaducidad != null) {
                        etFechaCaducidad.setText(p.fechaCaducidad);
                    }
                });
            }
        });
    }

    private void cargarCategorias() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            listaCategorias = db.inventarioDao().obtenerCategorias();

            runOnUiThread(() -> {
                if (listaCategorias != null) {
                    List<String> nombresCategorias = new ArrayList<>();
                    for (Categoria c : listaCategorias) {
                        nombresCategorias.add(c.nombre);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, nombresCategorias);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategoria.setAdapter(adapter);

                    // Re-select category if loading product data happened before categories loaded
                    if (idProductoEditar != -1) {
                        // This is a simple retry, or we could chain the calls.
                        // For simplicity, let's just trigger load again if title is set or rely on user
                        // wait.
                        // Better: call cargarDatosProducto AFTER categories if in edit mode.
                        // But for now, we leave the re-selection logic inside cargarDatosProducto
                        // primarily.
                        // We can just call it again here if needed, but concurrency is tricky.
                        // Let's rely on the fact that we call cargarDatosProducto in onCreate,
                        // but effectively we might miss the spinner update if categories aren't ready.
                        // FIX: Call cargarDatosProducto inside here after setting adapter.
                        cargarDatosProducto(idProductoEditar);
                    }
                }
            });
        });
    }

    private void actualizarCamposDinamicos(Categoria categoria) {
        String nombreCat = categoria.nombre.toLowerCase();

        // Resetear visibilidad
        cbTemporadaAnterior.setVisibility(View.GONE);
        layoutFechaCaducidad.setVisibility(View.GONE);

        if (nombreCat.contains("ropa")) {
            cbTemporadaAnterior.setVisibility(View.VISIBLE);
        } else if (nombreCat.contains("alimento")) {
            layoutFechaCaducidad.setVisibility(View.VISIBLE);
        }
        // Electrónico no tiene campos extra
    }

    private void guardarProducto() {
        // VALIDACIONES
        String nombre = etNombre.getText().toString().trim();
        String desc = etDescripcion.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            etNombre.setError("Requerido");
            return;
        }
        if (TextUtils.isEmpty(desc)) {
            etDescripcion.setError("Requerido");
            return;
        }
        if (desc.length() > 50) {
            etDescripcion.setError("Máximo 50 caracteres");
            return;
        }
        if (TextUtils.isEmpty(precioStr)) {
            etPrecio.setError("Requerido");
            return;
        }
        if (TextUtils.isEmpty(stockStr)) {
            etStock.setError("Requerido");
            return;
        }

        double precio = 0;
        int stock = 0;
        try {
            precio = Double.parseDouble(precioStr);
            if (precio < 0) {
                etPrecio.setError("No puede ser negativo");
                return;
            }
        } catch (NumberFormatException e) {
            etPrecio.setError("Inválido");
            return;
        }

        try {
            stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                etStock.setError("No puede ser negativo");
                return;
            }
        } catch (NumberFormatException e) {
            etStock.setError("Inválido");
            return;
        }

        // Obtener Categoría
        int pos = spinnerCategoria.getSelectedItemPosition();
        if (pos < 0 || listaCategorias == null) {
            Toast.makeText(this, "Seleccione categoría", Toast.LENGTH_SHORT).show();
            return;
        }
        Categoria cat = listaCategorias.get(pos);

        // Campos Dinámicos
        int esTemporada = 0;
        String fechaCad = null;

        if (cat.nombre.toLowerCase().contains("ropa")) {
            esTemporada = cbTemporadaAnterior.isChecked() ? 1 : 0;
        } else if (cat.nombre.toLowerCase().contains("alimento")) {
            fechaCad = etFechaCaducidad.getText().toString().trim();
            if (TextUtils.isEmpty(fechaCad)) {
                etFechaCaducidad.setError("Requerido");
                return;
            }
            // Validación simple de formato (se podría mejorar con Regex)
            // Asumimos que el usuario lo pone bien o usamos DatePicker (me pidieron campo
            // normal)
        }

        // CREAR OBJETO
        Producto nuevoProd = new Producto(
                cat.id_Categoria,
                nombre,
                desc,
                precio,
                stock,
                esTemporada,
                fechaCad);
        nuevoProd.estado = "ACT"; // Asegurar estado

        // SAVE or UPDATE
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            if (idProductoEditar != -1) {
                nuevoProd.id_Producto = idProductoEditar;
                db.inventarioDao().actualizarProducto(nuevoProd);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Producto Actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                db.inventarioDao().insertarProducto(nuevoProd);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Producto Registrado con Éxito", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }
}
