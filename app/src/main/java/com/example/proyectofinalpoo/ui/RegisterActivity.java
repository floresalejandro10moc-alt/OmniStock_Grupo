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
        nuevoUsuario.estado = "ACT"; // Activo por defecto

        // Si el Checkbox está marcado es 1 (Admin), si no es 0 (Cliente)
        nuevoUsuario.esAdministrador = cbEsAdmin.isChecked() ? 1 : 0;

        // Guardar en Base de Datos
        AppDatabase db = AppDatabase.getDatabase(this);

        try {
            db.inventarioDao().insertarUsuario(nuevoUsuario);
            Toast.makeText(this, "¡Usuario creado! Ahora puedes ingresar.", Toast.LENGTH_LONG).show();
            finish(); // Cierra esta pantalla y vuelve al Login
        } catch (Exception e) {
            Toast.makeText(this, "Error: El usuario ya existe o falló la base de datos.", Toast.LENGTH_LONG).show();
        }
    }
}