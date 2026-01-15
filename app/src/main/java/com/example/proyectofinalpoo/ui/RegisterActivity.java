package com.example.proyectofinalpoo.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.model.Usuario;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsuario, etCorreo, etClave;
    CheckBox cbEsAdmin;
    Button btnGuardar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. Vincular vistas
        etUsuario = findViewById(R.id.etRegUsuario);
        etCorreo = findViewById(R.id.etRegCorreo);
        etClave = findViewById(R.id.etRegClave);
        cbEsAdmin = findViewById(R.id.cbEsAdmin);
        btnGuardar = findViewById(R.id.btnRegistrarUsuario);
        btnCancelar = findViewById(R.id.btnCancelar);

        // 2. Acción del botón Guardar
        btnGuardar.setOnClickListener(v -> registrarUsuario());

        // 3. Acción del botón Cancelar
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void registrarUsuario() {
        String alias = etUsuario.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String clave = etClave.getText().toString().trim();

        // Validaciones básicas
        if (alias.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.alias = alias;
        nuevoUsuario.correo = correo;
        nuevoUsuario.clave = clave;
        nuevoUsuario.estado = "ACT";
        nuevoUsuario.esAdministrador = cbEsAdmin.isChecked() ? 1 : 0;

        AppDatabase db = AppDatabase.getDatabase(this);

        // 🔴 CAMBIO IMPORTANTE: Usamos el Executor para ir al segundo plano
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                // 1. Esto ocurre en "segundo plano" (No congela la pantalla)
                db.inventarioDao().insertarUsuario(nuevoUsuario);

                // 2. Para mostrar mensajes o cambiar de pantalla, debemos VOLVER al hilo principal
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "¡Usuario creado! Ingresa ahora.", Toast.LENGTH_LONG).show();
                    finish(); // Cierra el registro
                });

            } catch (Exception e) {
                // Si falla, también volvemos al hilo principal para mostrar el error
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "Error: El usuario ya existe.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}