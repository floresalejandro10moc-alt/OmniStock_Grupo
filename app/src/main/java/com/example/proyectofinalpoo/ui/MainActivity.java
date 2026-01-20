package com.example.proyectofinalpoo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.util.SessionManager;

public class MainActivity extends AppCompatActivity {

    // Declaramos todos los botones de la interfaz
    Button btnCerrar, btnCarrito, btnHistorial, btnCatalogo, btnNuevoProd;
    TextView tvNombreUser;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // Estos IDs deben coincidir con tu activity_main.xml
        btnCatalogo = findViewById(R.id.btnIrAlInventario);
        btnNuevoProd = findViewById(R.id.btnAgregarProducto);

        // 4. Mostrar nombre del usuario en la cabecera
        String alias = session.getAliasLogueado();
        tvNombreUser.setText("Bienvenido, " + alias);

        // 5. LÓGICA DE ROLES: Ocultar botón de "Nuevo Producto" si no es Admin
        if (!session.esAdmin()) {
            btnNuevoProd.setVisibility(View.GONE);
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

        // D) Botón Catálogo (Inventario)
        btnCatalogo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CatalogoActivity.class);
            startActivity(intent);
        });

        // E) Botón Nuevo Producto (Solo Admin)
        btnNuevoProd.setOnClickListener(v -> {
            // Cuando crees la pantalla de AltaProductoActivity, descomenta abajo:
            // Intent intent = new Intent(MainActivity.this, AltaProductoActivity.class);
            // startActivity(intent);

            Toast.makeText(this, "Próximamente: Crear Producto", Toast.LENGTH_SHORT).show();
        });
    }

    private void irAlLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        // Flags para limpiar el historial y que no puedan volver atrás
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}