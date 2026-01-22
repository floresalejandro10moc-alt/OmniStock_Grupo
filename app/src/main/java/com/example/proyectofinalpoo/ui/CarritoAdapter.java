package com.example.proyectofinalpoo.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.logic.Alimento;
import com.example.proyectofinalpoo.logic.Electronico;
import com.example.proyectofinalpoo.logic.ProductoBase;
import com.example.proyectofinalpoo.logic.Ropa;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ProductoViewHolder> {

    private List<ProductoBase> listaProductos;
    private Runnable onCartUpdate;

    public CarritoAdapter(List<ProductoBase> listaProductos, Runnable onCartUpdate) {
        this.listaProductos = listaProductos;
        this.onCartUpdate = onCartUpdate;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        ProductoBase producto = listaProductos.get(position);

        // 1. Llenamos los datos básicos
        holder.txtNombre.setText(producto.getNombre());

        // 2. Usamos el POLIMORFISMO para obtener el precio final calculado
        double precioFinal = producto.calcularPrecioFinal();
        holder.txtPrecio.setText(String.format("$%.2f", precioFinal));

        // 3. CAMBIO VISUAL DINÁMICO (Requisito Rúbrica)
        // Cambiamos el color de fondo de la tarjeta según el tipo de clase
        // USAREMOS COLORES CON ALPHA (VIDRIO TINTADO) PARA MANTENER LA ESTÉTICA DARK
        if (producto instanceof Electronico) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#262196F3")); // Azul Vidrio
            holder.txtDetalle.setText("Incluye IVA + Suntuario");
        } else if (producto instanceof Ropa) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#26E91E63")); // Rosa Vidrio
            holder.txtDetalle.setText("Incluye IVA (Posible Descuento)");
        } else if (producto instanceof Alimento) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#264CAF50")); // Verde Vidrio
            holder.txtDetalle.setText("0% IVA");
        } else {
            // Default Glass
            holder.cardView.setCardBackgroundColor(Color.parseColor("#1AFFFFFF"));
        }

        // 4. Configurar cantidad y botones
        holder.txtCantidad.setText(String.valueOf(producto.getCantidadCarrito()));

        // Botón MÁS
        // Botón MÁS
        holder.btnMas.setOnClickListener(v -> {
            // CAMBIO: Se permite agregar hasta completar el stock (<= stock)
            // Si tiene 19 y stock es 20, entra (19 < 20) -> sube a 20.
            // Si tiene 20 y stock es 20, no entra (20 < 20 es false).
            if (producto.getCantidadCarrito() < producto.stock) {
                com.example.proyectofinalpoo.logic.CarritoManager.getInstance().aumentarCantidad(position);
                notifyItemChanged(position); // Solo actualizamos este item
                if (onCartUpdate != null) {
                    onCartUpdate.run();
                }
            } else {
                android.widget.Toast.makeText(holder.itemView.getContext(),
                        "No puedes agregar más de lo disponible en stock (" + producto.stock + ")",
                        android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        // Botón MENOS (Con lógica de eliminación)
        holder.btnMenos.setOnClickListener(v -> {
            if (producto.getCantidadCarrito() > 1) {
                // Solo disminuimos
                com.example.proyectofinalpoo.logic.CarritoManager.getInstance().disminuirCantidad(position);
                notifyItemChanged(position);
                if (onCartUpdate != null)
                    onCartUpdate.run();
            } else {
                // Pedir confirmación para eliminar
                new android.app.AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Eliminar Producto")
                        .setMessage("¿Estás seguro de que deseas eliminar este producto del carrito?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            com.example.proyectofinalpoo.logic.CarritoManager.getInstance().eliminarProducto(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, listaProductos.size());
                            if (onCartUpdate != null)
                                onCartUpdate.run();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

        // Icono visual del botón menos (Cambiar a "Basura" si qty es 1, opcional pero
        // buena UX)
        if (producto.getCantidadCarrito() == 1) {
            holder.btnMenos.setImageResource(android.R.drawable.ic_menu_delete);
            holder.btnMenos.setColorFilter(Color.parseColor("#D32F2F")); // Rojo
        } else {
            holder.btnMenos.setImageResource(android.R.drawable.ic_menu_revert); // O un signo menos
            // Como no tengo un ic_remove a mano facil, usare uno generico o el mismo delete
            // pero con logica
            // Para simplificar segun pedido: "boton de menos".
            // Usaremos un recurso del sistema si existe, sino dejamos el delete visualmente
            // o buscamos uno mejor.
            // Android standard: android.R.drawable.btn_minus (a veces no disponible).
            // Usaremos ic_input_delete para todo por ahora, o ic_menu_revert.
            // MEJOR: Dejemos el icono que pusimos en XML (ic_delete), pero si hay más de 1
            // podría ser confuso.
            // Cambiemos a un icono de "Minus" si es posible.
            // Al no tener garantía de drawables, mantendré el XML base pero bueno saberlo.
            // SI QUIERES: holder.btnMenos.setImageResource(android.R.drawable.t);
            // Dejaremos el icono del XML base por defecto.

            // TRUCO: Usar el caracter "-" en un TextButton seria mejor, pero ya puse
            // ImageButton.
            // Vamos a dejarlo así, la funcionalidad es lo que importa.
        }
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    // Clase interna que guarda las referencias a los controles del XML
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtPrecio, txtDetalle, txtCantidad;
        CardView cardView;
        android.widget.ImageButton btnMas, btnMenos; // Cambiado a ImageButton

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreProducto);
            txtPrecio = itemView.findViewById(R.id.txtPrecioProducto);
            txtDetalle = itemView.findViewById(R.id.txtDetalleImpuesto);
            cardView = itemView.findViewById(R.id.cardViewProducto);
            // Referencias nuevas
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            btnMas = itemView.findViewById(R.id.btnMas);
            btnMenos = itemView.findViewById(R.id.btnMenos);
        }
    }
}