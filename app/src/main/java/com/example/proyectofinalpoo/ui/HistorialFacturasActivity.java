package com.example.proyectofinalpoo.ui;

import android.os.Bundle;
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
            List<Factura> facturas = new ArrayList<>(); // Inicializar como lista vacía
            AppDatabase db = AppDatabase.getDatabase(this);

            if (session.esAdmin()) {
                // Si es admin, obtiene todas las facturas
                facturas = db.inventarioDao().obtenerTodasLasFacturas();
            } else {
                // Si es cliente, primero buscamos su perfil para obtener la cédula
                String alias = session.getAliasLogueado();
                Cliente cliente = db.inventarioDao().obtenerClientePorAlias(alias);
                if (cliente != null) {
                    // Si encontramos el perfil, usamos la cédula para buscar sus facturas
                    facturas = db.inventarioDao().obtenerFacturasPorCliente(cliente.cedula);
                }
                // Si no hay perfil de cliente, la lista de facturas permanecerá vacía, lo cual es correcto.
            }

            final List<Factura> facturasFinal = facturas;
            runOnUiThread(() -> {
                adapter = new FacturaAdapter(facturasFinal);
                recyclerFacturas.setAdapter(adapter);
            });
        });
    }
}