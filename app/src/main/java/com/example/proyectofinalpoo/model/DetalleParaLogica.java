package com.example.proyectofinalpoo.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class DetalleParaLogica {

    // Añadimos un prefijo para desempatar las columnas del producto.
    @Embedded(prefix = "prod_")
    public Producto producto;

    // Añadimos otro prefijo para las columnas de la categoría.
    @Embedded(prefix = "cat_")
    public Categoria categoria;

    // Esta columna viene directamente del join y no necesita prefijo si su alias es único.
    public int cantidadComprada;
}
