package com.example.proyectofinalpoo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.util.SessionManager; // Importante importar esto

public class MainActivity extends AppCompatActivity {

    Button btnCerrar;
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

        btnCerrar.setOnClickListener(v -> {
            // 1. Borrar datos de sesión
            session.cerrarSesion();

            // 2. Volver al Login
            irAlLogin();
        });
    }

    private void irAlLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        // Esto evita que puedan volver atrás con el botón del celular
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}