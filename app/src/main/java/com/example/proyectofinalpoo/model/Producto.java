package com.example.proyectofinalpoo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;

@Entity(tableName = "PRODUCTOS",
        foreignKeys = @ForeignKey(entity = Categoria.class,
                parentColumns = "id_Categoria",
                childColumns = "id_Categoria",
                onDelete = ForeignKey.RESTRICT,
                onUpdate = ForeignKey.CASCADE),
        indices = {@Index(value = "pro_Nombre"), @Index(value = "id_Categoria")})
public class Producto {
    @PrimaryKey(autoGenerate = true)
    public int id_Producto;

    public int id_Categoria;

    @ColumnInfo(name = "pro_Nombre")
    public String nombre;

    @ColumnInfo(name = "pro_Descripcion")
    public String descripcion;

    @ColumnInfo(name = "pro_PrecioBase")
    public double precioBase;

    @ColumnInfo(name = "pro_Stock", defaultValue = "0")
    public int stock;

    @ColumnInfo(name = "pro_EsTemporadaAnterior", defaultValue = "0")
    public int esTemporadaAnterior;

    @ColumnInfo(name = "pro_FechaCaducidad")
    public String fechaCaducidad;

    @ColumnInfo(name = "pro_Estado", defaultValue = "ACT")
    public String estado;

    public Producto() {}

    // Constructor completo
    public Producto(int id_Categoria, String nombre, String descripcion, double precio, int stock, int tempAnterior, String caducidad) {
        this.id_Categoria = id_Categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precio;
        this.stock = stock;
        this.esTemporadaAnterior = tempAnterior;
        this.fechaCaducidad = caducidad;
        this.estado = "ACT";
    }
}
