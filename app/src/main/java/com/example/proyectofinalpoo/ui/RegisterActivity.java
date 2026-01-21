package com.example.proyectofinalpoo.ui;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.model.*;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etAlias, etCorreo, etClave, etNombre, etApellido, etCedula, etCelular, etDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        vincularVistas();

        findViewById(R.id.btnFinalizarRegistro).setOnClickListener(v -> validarYRegistrar());
        findViewById(R.id.btnRegVolver).setOnClickListener(v -> finish());
    }

    private void vincularVistas() {
        etAlias = findViewById(R.id.etRegAlias);
        etCorreo = findViewById(R.id.etRegCorreo);
        etClave = findViewById(R.id.etRegClave);
        etNombre = findViewById(R.id.etRegNombre);
        etApellido = findViewById(R.id.etRegApellido);
        etCedula = findViewById(R.id.etRegCedula);
        etCelular = findViewById(R.id.etRegCelular);
        etDireccion = findViewById(R.id.etRegDireccion);
    }

    private void validarYRegistrar() {
        String correo = etCorreo.getText().toString().trim();
        String cedula = etCedula.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();

        // 1. Validación de Correo
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Correo inválido");
            return;
        }

        // 2. Validación de Cédula Ecuatoriana (Algoritmo Módulo 10)
        if (!validarCedulaEcuatoriana(cedula)) {
            etCedula.setError("Número de cédula real no válido");
            return;
        }

        // 3. Solo letras en Nombres y Apellidos
        if (!Pattern.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", nombre)) {
            etNombre.setError("Solo se permiten letras");
            return;
        }
        if (!Pattern.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", apellido)) {
            etApellido.setError("Solo se permiten letras");
            return;
        }

        procederConGuardado();
    }

    private boolean validarCedulaEcuatoriana(String cedula) {
        if (cedula.length() != 10) return false;
        try {
            int provincia = Integer.parseInt(cedula.substring(0, 2));
            if (provincia < 1 || provincia > 24) return false;

            int d10 = Integer.parseInt(cedula.substring(9, 10));
            int suma = 0;
            for (int i = 0; i < 9; i++) {
                int d = Integer.parseInt(cedula.substring(i, i + 1));
                if (i % 2 == 0) {
                    d *= 2;
                    if (d > 9) d -= 9;
                }
                suma += d;
            }
            int verificador = (suma % 10 == 0) ? 0 : 10 - (suma % 10);
            return verificador == d10;
        } catch (Exception e) { return false; }
    }

    private void procederConGuardado() {
        // Captura de datos
        Usuario u = new Usuario(etAlias.getText().toString(), Usuario.ROL_VENDEDOR,
                etClave.getText().toString(), etCorreo.getText().toString());

        Cliente c = new Cliente(etNombre.getText().toString(), etApellido.getText().toString(),
                etCedula.getText().toString(), etDireccion.getText().toString(),
                etCelular.getText().toString());

        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                // USAMOS LA TRANSACCIÓN QUE CREAMOS
                AppDatabase.getDatabase(this).inventarioDao().registrarUsuarioYCliente(u, c);

                runOnUiThread(() -> {
                    Toast.makeText(this, "¡Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (android.database.sqlite.SQLiteConstraintException e) {
                runOnUiThread(() -> {
                    // ANALIZAMOS QUÉ DATO ES EL DUPLICADO REAL
                    String msg = e.getMessage();
                    if (msg.contains("usu_Alias")) etAlias.setError("Este Alias ya existe");
                    else if (msg.contains("usu_Correo")) etCorreo.setError("Este Correo ya existe");
                    else if (msg.contains("cli_Cedula")) etCedula.setError("Esta Cédula ya está registrada");
                    else Toast.makeText(this, "Error: Datos duplicados", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}