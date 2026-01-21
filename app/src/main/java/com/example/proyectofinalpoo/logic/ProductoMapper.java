package com.example.proyectofinalpoo.logic;

import com.example.proyectofinalpoo.R;
import com.example.proyectofinalpoo.model.Categoria;
import com.example.proyectofinalpoo.model.Producto;

public class ProductoMapper {
    public static ProductoBase convertirEntidadALogica(Producto p, Categoria c) {
        if (p == null || c == null) {
            return null;
        }

        String tipo = (c.nombre != null) ? c.nombre.toUpperCase() : "";

        // Si el ID de la imagen no es válido (0 o menor), usamos una imagen por defecto.
        int imagenResId = (p.imagenResId <= 0) ? R.drawable.ic_default_image : p.imagenResId;

        if (tipo.contains("ELECTRONI")) {
            return new Electronico(p.id_Producto, p.nombre, p.precioBase, p.stock, c.iva, c.impuesto, imagenResId);
        } else if (tipo.contains("ROPA")) {
            return new Ropa(p.id_Producto, p.nombre, p.precioBase, p.stock, c.iva, p.esTemporadaAnterior == 1, imagenResId);
        } else {
            return new Alimento(p.id_Producto, p.nombre, p.precioBase, p.stock, c.iva, imagenResId);
        }
    }
}