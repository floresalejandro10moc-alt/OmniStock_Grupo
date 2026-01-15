package com.example.proyectofinalpoo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.*;
import com.example.proyectofinalpoo.model.Usuario;
import com.example.proyectofinalpoo.util.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etClave;
    private Button btnIngresar;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView tvRegistro = findViewById(R.id.tvIrARegistro);

        tvRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // 1. Verificar si ya hay sesión iniciada (Auto-login)
        session = new SessionManager(this);
        if (session.estaLogueado()) {
            irAlMenuPrincipal();
        }

        etUsuario = findViewById(R.id.etUsuario);
        etClave = findViewById(R.id.etClave);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(v -> validarIngreso());
    }

    private void validarIngreso() {
        String u = etUsuario.getText().toString().trim();
        String p = etClave.getText().toString().trim();

        if (u.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consultar BD
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        // NOTA: Para producción, usar hilos (Executor). Para el proyecto escolar, allowMainThreadQueries está bien.
        Usuario usuarioEncontrado = db.inventarioDao().login(u, p);

        if (usuarioEncontrado != null) {
            // LOGIN EXITOSO

            // Guardar sesión usando nuestra clase helper
            session.crearSesionLogin(
                    usuarioEncontrado.id_Usuario,
                    usuarioEncontrado.esAdministrador,
                    usuarioEncontrado.alias
            );

            // Validar Rol (Ejemplo de uso de Constantes)
            if (usuarioEncontrado.esAdministrador()) {
                Toast.makeText(this, "Bienvenido Admin " + usuarioEncontrado.alias, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bienvenido Vendedor " + usuarioEncontrado.alias, Toast.LENGTH_SHORT).show();
            }

            irAlMenuPrincipal();

        } else {
            Toast.makeText(this, "Credenciales incorrectas o usuario inactivo", Toast.LENGTH_SHORT).show();
        }
    }

    private void irAlMenuPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        // Borrar historial para que al dar "Atrás" no vuelva al login
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
