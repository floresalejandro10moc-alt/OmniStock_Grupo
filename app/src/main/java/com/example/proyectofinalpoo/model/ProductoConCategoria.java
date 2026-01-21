package com.example.proyectofinalpoo.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ProductoConCategoria {
    @Embedded
    public Producto producto;

    @Relation(
         parentColumn = "id_Categoria",
         entityColumn = "id_Categoria"
    )
    public Categoria categoria;
}