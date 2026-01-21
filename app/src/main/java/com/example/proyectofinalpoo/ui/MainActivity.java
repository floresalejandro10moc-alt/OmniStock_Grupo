package com.example.proyectofinalpoo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.logic.ProductoBase;
import com.example.proyectofinalpoo.logic.ProductoMapper;
import com.example.proyectofinalpoo.model.ProductoConCategoria;
import com.example.proyectofinalpoo.util.ProductoAdapter;
import com.example.proyectofinalpoo.util.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SessionManager session;
    private RecyclerView rvProductos;
    private ProductoAdapter adapter;
    private ProductoViewModel productoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(this);
        if (!session.estaLogueado()) {
            regresarAlLogin();
            return;
        }

        TextView tvBienvenida = findViewById(R.id.tvBienvenidaUsuario);
        tvBienvenida.setText("Bienvenido, " + session.getAlias());

        rvProductos = findViewById(R.id.rvProductos);
        rvProductos.setLayoutManager(new GridLayoutManager(this, 2));

        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);

        productoViewModel.getAllProductos().observe(this, productosConCategoria -> {
            List<ProductoBase> productsLogic = new ArrayList<>();
            for (ProductoConCategoria pcc : productosConCategoria) {
                productsLogic.add(ProductoMapper.convertirEntidadALogica(pcc.producto, pcc.categoria));
            }
            adapter = new ProductoAdapter(productsLogic, this);
            rvProductos.setAdapter(adapter);
        });

        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(v -> {
            session.cerrarSesion();
            regresarAlLogin();
        });
    }

    private void regresarAlLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}