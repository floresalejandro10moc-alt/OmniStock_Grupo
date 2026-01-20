package com.example.proyectofinalpoo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.logic.CarritoManager;
import com.example.proyectofinalpoo.logic.ProductoBase;
import com.example.proyectofinalpoo.logic.ProductoMapper;
import com.example.proyectofinalpoo.model.Categoria;
import com.example.proyectofinalpoo.model.Producto;

import java.util.List;
import java.util.Map;

public class CatalogoAdapter extends RecyclerView.Adapter<CatalogoAdapter.CatalogoViewHolder> {

    private List<Producto> productos;
    private Map<Integer, Categoria> categorias;

    public CatalogoAdapter(List<Producto> productos, Map<Integer, Categoria> categorias) {
        this.productos = productos;
        this.categorias = categorias;
    }

    @NonNull
    @Override
    public CatalogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catalogo, parent, false);
        return new CatalogoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        Categoria categoria = categorias.get(producto.id_Categoria);
        holder.bind(producto, categoria);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    class CatalogoViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNombre, txtPrecio, txtStock;
        private Button btnAgregar;

        public CatalogoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtItemCatalogoNombre);
            txtPrecio = itemView.findViewById(R.id.txtItemCatalogoPrecio);
            txtStock = itemView.findViewById(R.id.txtItemCatalogoStock);
            btnAgregar = itemView.findViewById(R.id.btnItemCatalogoAgregar);
        }

        void bind(Producto p, Categoria c) {
            txtNombre.setText(p.nombre);
            txtPrecio.setText(String.format("$%.2f", p.precioBase));
            txtStock.setText("Stock: " + p.stock);

            btnAgregar.setOnClickListener(v -> {
                if (p.stock > 0) {
                    ProductoBase logicProduct = ProductoMapper.convertirEntidadALogica(p, c);
                    if (logicProduct != null) {
                        CarritoManager.getInstance().agregarProducto(logicProduct);
                        Toast.makeText(itemView.getContext(), p.nombre + " añadido al carrito", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(itemView.getContext(), "Sin stock disponible", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
