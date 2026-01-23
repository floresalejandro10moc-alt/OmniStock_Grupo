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
    private boolean esAdmin;

    private OnProductoActionListener listener;

    public interface OnProductoActionListener {
        void onEdit(Producto producto);

        void onToggleStatus(Producto producto);

        void onDelete(Producto producto);
    }

    public CatalogoAdapter(List<Producto> productos, Map<Integer, Categoria> categorias, boolean esAdmin) {
        this.productos = productos;
        this.categorias = categorias;
        this.esAdmin = esAdmin;
    }

    public void setOnProductoActionListener(OnProductoActionListener listener) {
        this.listener = listener;
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
        private android.widget.ImageView imgIcon;
        private Button btnAgregar;
        private android.widget.LinearLayout layoutAdminButtons;
        private android.widget.ImageButton btnEditar, btnEliminar;

        public CatalogoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtItemCatalogoNombre);
            txtPrecio = itemView.findViewById(R.id.txtItemCatalogoPrecio);
            txtStock = itemView.findViewById(R.id.txtItemCatalogoStock);
            imgIcon = itemView.findViewById(R.id.imgItemCatalogoIcon);
            btnAgregar = itemView.findViewById(R.id.btnItemCatalogoAgregar);
            layoutAdminButtons = itemView.findViewById(R.id.layoutAdminButtons);
            btnEditar = itemView.findViewById(R.id.btnItemCatalogoEditar);
            btnEliminar = itemView.findViewById(R.id.btnItemCatalogoEliminar);
        }

        void bind(Producto p, Categoria c) {
            txtNombre.setText(p.nombre);
            txtPrecio.setText(String.format("$%.2f", p.precioBase));
            txtStock.setText("Stock: " + p.stock);

            // Visual indicator for Inactive products
            if ("INA".equals(p.estado)) {
                txtNombre.setTextColor(android.graphics.Color.GRAY);
                itemView.setAlpha(0.6f);
            } else {
                txtNombre.setTextColor(android.graphics.Color.BLACK); // or default color
                itemView.setAlpha(1.0f);
            }

            // Dynamic Icon Logic
            if (c != null && c.nombre != null) {
                String catName = c.nombre.toLowerCase();
                if (catName.contains("electr") || catName.contains("tecnolog")) {
                    imgIcon.setImageResource(R.drawable.ic_tech);
                } else if (catName.contains("ropa") || catName.contains("vesti")) {
                    imgIcon.setImageResource(R.drawable.ic_clothing);
                } else if (catName.contains("alim") || catName.contains("comida") || catName.contains("bebida")) {
                    imgIcon.setImageResource(R.drawable.ic_food);
                } else {
                    imgIcon.setImageResource(R.drawable.caja_omni);
                }
            } else {
                imgIcon.setImageResource(R.drawable.caja_omni);
            }

            // Logic for Admin
            if (!esAdmin) {
                txtStock.setVisibility(View.GONE);
                layoutAdminButtons.setVisibility(View.GONE);
            } else {
                txtStock.setVisibility(View.VISIBLE);
                layoutAdminButtons.setVisibility(View.VISIBLE);

                // Configure Toggle Button Icon
                if ("INA".equals(p.estado)) {
                    // Show "Activate" icon (recycle/check)
                    // Assuming we might not have a specific 'activate' icon, we can reuse or use a
                    // standard android one if allowed.
                    // For now let's use android.R.drawable.ic_menu_revert or similar available, or
                    // just keep trash but change color?
                    // Better: Let's assume we want to restore.
                    // Since I can't generate new drawables easily without 'generate_image' and
                    // waiting,
                    // I will use a system drawable or just the same button but rely on the dialog
                    // context.
                    // But user asked for specific icons/behavior.
                    // Let's use `android.R.drawable.ic_menu_rotate` or similar for restore if
                    // possible,
                    // or just use the local resources.
                    // Safest: Use `R.drawable.ic_add` (plus) or similar for "Activate" if available
                    // in standard,
                    // or just change color/tint.
                    // Let's try to set a standard "restore" icon if possible.
                    // `btnEliminar` is an ImageButton.

                    // Note: User said "si esat ACT se veo el boton eliminar si esta INA se vea un
                    // boton activar"
                    // I will attempt to use a standard android system icon for "restore" / "check".
                    btnEliminar.setImageResource(android.R.drawable.ic_input_add); // Temporary placeholder for
                                                                                   // "Activate"
                } else {
                    // Regular delete icon
                    btnEliminar.setImageResource(android.R.drawable.ic_menu_delete); // Or the original one defined in
                                                                                     // XML
                    // Actually, the XML has `srcCompat="@android:drawable/ic_menu_delete"`.
                    // So I'll stick to that.
                }
            }

            btnEditar.setOnClickListener(v -> {
                if (listener != null)
                    listener.onEdit(p);
            });

            btnEliminar.setOnClickListener(v -> {
                if (listener != null)
                    listener.onToggleStatus(p);
            });

            btnAgregar.setOnClickListener(v -> {
                if (p.stock > 0) {
                    ProductoBase logicProduct = ProductoMapper.convertirEntidadALogica(p, c);
                    if (logicProduct != null) {
                        CarritoManager.getInstance().agregarProducto(logicProduct);
                        Toast.makeText(itemView.getContext(), p.nombre + " añadido al carrito", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(itemView.getContext(), "Error: Categoría no válida para " + p.nombre,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(itemView.getContext(), "Sin stock disponible", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
