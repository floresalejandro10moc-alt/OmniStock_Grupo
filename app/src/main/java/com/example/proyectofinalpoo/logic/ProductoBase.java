package com.example.proyectofinalpoo.logic;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "PRODUCTOS")
public abstract class ProductoBase {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_Producto")
    public int id; // ID para la base de datos

    @ColumnInfo(name = "id_Categoria")
    public int idCategoria;

    @ColumnInfo(name = "pro_Nombre")
    public String nombre;

    @ColumnInfo(name = "pro_PrecioBase")
    public double precioBase;

    @ColumnInfo(name = "pro_Stock")
    public int stock;

    @ColumnInfo(name = "ivaActual")
    public double ivaActual;

    // CAMPOS NECESARIOS PARA LOS HIJOS (Ropa y Alimento)
    @ColumnInfo(name = "pro_EsTemporadaAnterior")
    public int esTemporadaAnterior;

    @ColumnInfo(name = "pro_FechaCaducidad")
    public String fechaCaducidad;

    @ColumnInfo(name = "pro_Estado")
    public String estado = "ACT";

    // Constructor vacío para Room
    public ProductoBase() {}

    // Constructor para la lógica
    public ProductoBase(int id, String nombre, double precioBase, int stock, double ivaActual) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.ivaActual = ivaActual;
    }

    public abstract double calcularPrecioFinal();

    // Getters para el Mapper
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }
    public int getStock() { return stock; }
}