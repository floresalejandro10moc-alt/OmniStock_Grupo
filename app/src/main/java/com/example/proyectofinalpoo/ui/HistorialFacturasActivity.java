package com.example.proyectofinalpoo.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.model.Cliente;
import com.example.proyectofinalpoo.model.Factura;
import com.example.proyectofinalpoo.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class HistorialFacturasActivity extends AppCompatActivity {

    private RecyclerView recyclerFacturas;
    private FacturaAdapter adapter;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_facturas);

        recyclerFacturas = findViewById(R.id.recyclerFacturas);
        recyclerFacturas.setLayoutManager(new LinearLayoutManager(this));

        session = new SessionManager(this);

        cargarFacturas();
    }

    private void cargarFacturas() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            List<Factura> facturasAMostrar = new ArrayList<>();

            if (session.esAdmin()) {
                // REGLA 1: El Admin ve absolutamente todo
                facturasAMostrar = db.inventarioDao().obtenerTodasLasFacturas();
            } else {
                // REGLA 2: El Vendedor/Cliente solo ve lo suyo
                // Intentamos obtener el perfil por ID (más seguro) o Alias según prefieras
                int idUsuarioActual = session.getUserId();
                Cliente perfil = db.inventarioDao().obtenerClientePorIdUsuario(idUsuarioActual);

                // Validación robusta: que exista el perfil y tenga una cédula asociada
                if (perfil != null && perfil.cedula != null) {
                    // Usamos el método de búsqueda por cédula
                    facturasAMostrar = db.inventarioDao().obtenerFacturasPorCedula(perfil.cedula);
                }
            }

            // Variable final para el hilo de UI
            final List<Factura> resultadoFinal = facturasAMostrar;

            // Actualizar la interfaz en el hilo principal
            runOnUiThread(() -> {
                // Seteamos el adaptador con la lista resultante (vacía o con datos)
                adapter = new FacturaAdapter(resultadoFinal);
                recyclerFacturas.setAdapter(adapter);

                // Feedback al usuario si no hay datos
                if (resultadoFinal.isEmpty()) {
                    Toast.makeText(this, "No se encontraron facturas para mostrar", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}