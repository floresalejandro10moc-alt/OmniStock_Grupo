package com.example.proyectofinalpoo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.model.Usuario;
import com.example.proyectofinalpoo.util.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etClave;
    private Button btnIngresar;
    private ImageView ivBigO;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Inflar vista estática

        session = new SessionManager(this);

        // Verificar si ya está logueado
        if (session.estaLogueado()) {
            irAlMenuPrincipal();
            return;
        }

        // Vincular componentes
        etUsuario = findViewById(R.id.etUsuario);
        etClave = findViewById(R.id.etClave);
        btnIngresar = findViewById(R.id.btnIngresar);
        ivBigO = findViewById(R.id.ivBigO);
        TextView tvRegistro = findViewById(R.id.tvIrARegistro);

        btnIngresar.setOnClickListener(v -> validarIngreso());

        tvRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void validarIngreso() {
        String u = etUsuario.getText().toString().trim();
        String p = etClave.getText().toString().trim();

        if (u.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consulta en hilo secundario para evitar bloqueos
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            Usuario usuarioEncontrado = db.inventarioDao().login(u, p);

            runOnUiThread(() -> {
                if (usuarioEncontrado != null) {
                    session.crearSesionLogin(
                            usuarioEncontrado.id_Usuario,
                            usuarioEncontrado.esAdministrador,
                            usuarioEncontrado.alias);

                    // Animación de "Big O"
                    if (ivBigO != null) {
                        ivBigO.animate()
                                .scaleX(50f)
                                .scaleY(50f)
                                .setDuration(1500)
                                .withEndAction(() -> irAlMenuPrincipal())
                                .start();
                    } else {
                        irAlMenuPrincipal();
                    }
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void irAlMenuPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}