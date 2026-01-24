package com.example.proyectofinalpoo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.model.Categoria;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    protected List<Categoria> listaCategorias;
    protected OnCategoriaClickListener listener;

    // Interfaz para comunicar los clics al Activity
    public interface OnCategoriaClickListener {
        void onEdit(Categoria cat);
        void onDelete(Categoria cat);
    }

    public CategoriaAdapter(List<Categoria> lista, OnCategoriaClickListener listener) {
        this.listaCategorias = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria cat = listaCategorias.get(position);

        // Asignar los valores a los textos
        holder.txtNombre.setText(cat.nombre);
        holder.txtDetalles.setText(String.format("IVA: %.2f%% | Impuesto: %.2f%%", cat.iva, cat.impuesto));
        holder.txtEstado.setText("Estado: " + cat.estado);

        // Configurar clics en los botones de acción
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(cat);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(cat);
        });
    }

    @Override
    public int getItemCount() {
        return listaCategorias != null ? listaCategorias.size() : 0;
    }

    // Clase interna para referenciar los elementos visuales de cada fila
    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDetalles, txtEstado;
        ImageButton btnEdit, btnDelete;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreCatItem);
            txtDetalles = itemView.findViewById(R.id.txtDetallesCatItem);
            txtEstado = itemView.findViewById(R.id.txtEstadoCatItem);
            btnEdit = itemView.findViewById(R.id.btnEditCat);
            btnDelete = itemView.findViewById(R.id.btnDeleteCat);
        }
    }
}