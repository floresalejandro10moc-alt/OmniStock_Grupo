package com.example.proyectofinalpoo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;

@Entity(tableName = "CATEGORIA",
        indices = {@Index(value = "cat_Nombre", unique = true)})
public class Categoria {
    @PrimaryKey(autoGenerate = true)
    public int id_Categoria;

    @ColumnInfo(name = "cat_Nombre")
    public String nombre;

    @ColumnInfo(name = "cat_Descripcion")
    public String descripcion;

    @ColumnInfo(name = "cat_IVA", defaultValue = "0.00")
    public double iva;

    @ColumnInfo(name = "cat_Impuesto", defaultValue = "0.00")
    public double impuesto;

    @ColumnInfo(name = "cat_Estado", defaultValue = "ACT")
    public String estado;

    // Constructor vacío requerido por Room
    public Categoria() {}

    // Constructor para insertar fácil
    public Categoria(String nombre, String descripcion, double iva, double impuesto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.iva = iva;
        this.impuesto = impuesto;
        this.estado = "ACT";
    }
}
