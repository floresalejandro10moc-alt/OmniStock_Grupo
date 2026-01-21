package com.example.proyectofinalpoo.ui;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.model.Producto;

public class AltaProductoActivity extends AppCompatActivity {

    EditText etNombre, etPrecio, etStock;
    Spinner spnCat;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_producto);

        etNombre = findViewById(R.id.etNombreProd);
        etPrecio = findViewById(R.id.etPrecioProd);
        etStock = findViewById(R.id.etStockProd);
        spnCat = findViewById(R.id.spnCategoria);
        btnGuardar = findViewById(R.id.btnGuardarProducto);

        // Configurar Spinner con tus categorías (1: Electro, 2: Ropa, 3: Alimentos)
        String[] opciones = {"Electrónica", "Ropa", "Alimentos"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        spnCat.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> guardarProducto());
    }

    private void guardarProducto() {
        String nombre = etNombre.getText().toString();
        String precioStr = etPrecio.getText().toString();
        String stockStr = etStock.getText().toString();

        if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio = Double.parseDouble(precioStr);
        int stock = Integer.parseInt(stockStr);
        int idCat = spnCat.getSelectedItemPosition() + 1; // Ajustamos al ID de tu base de datos

        AppDatabase.databaseWriteExecutor.execute(() -> {
            Producto p = new Producto();
            p.nombre = nombre;
            p.precioBase = precio;
            p.stock = stock;
            p.id_Categoria = idCat;
            p.estado = "ACT";
            p.esTemporadaAnterior = 0; // Valor por defecto

            AppDatabase.getDatabase(this).inventarioDao().insertarProducto(p);

            runOnUiThread(() -> {
                Toast.makeText(this, "Producto guardado exitosamente", Toast.LENGTH_SHORT).show();
                finish(); // Regresa al MainActivity
            });
        });
    }
}