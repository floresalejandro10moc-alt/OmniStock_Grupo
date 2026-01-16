package com.example.proyectofinalpoo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.util.SessionManager; // Importante importar esto

public class    MainActivity extends AppCompatActivity {

    Button btnCerrar,btnCarrito;;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar SessionManager
        session = new SessionManager(this);

        // Verificar si NO está logueado (Seguridad extra por si alguien entra directo)
        if (!session.estaLogueado()) {
            irAlLogin();
        }

        btnCerrar = findViewById(R.id.btnCerrarSesion);
        btnCarrito = findViewById(R.id.btnIrAlCarrito);
        btnCerrar.setOnClickListener(v -> {
            // 1. Borrar datos de sesión
            session.cerrarSesion();

            // 2. Volver al Login
            irAlLogin();
        });

        // 3. Acción para IR AL CARRITO (Aquí está la conexión que buscabas)
        btnCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CarritoActivity.class);
            startActivity(intent);
        });

        /*
        // "Script para actualizar el IVA"
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase.getDatabase(context).inventarioDao().actualizarIVACategoria("Electronica", 8.0);
        });
        */


        /*// "Script para actualizar el impuesto"
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase.getDatabase(this).inventarioDao()
                .actualizarImpuestoCategoria("Electronica", 0.10); // Ojo: 0.10 si usas decimales, o 10.0 si usas porcentajes
        });
        * */
    }

    private void irAlLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        // Esto evita que puedan volver atrás con el botón del celular
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}