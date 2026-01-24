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

    @ColumnInfo(name = "pro_FechaCaducidad")
    public String fechaCaducidad;

    // CAMPOS NECESARIOS PARA LOS HIJOS (Ropa y Alimento)
    @ColumnInfo(name = "pro_EsTemporadaAnterior")
    public int esTemporadaAnterior;

    public ProductoBase(int id, String nombre, double precioBase, int stock, double ivaActual) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.ivaActual = ivaActual;
    }

    public abstract double calcularPrecioFinal();

    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public int getId() {
        return id;
    }

    // Nuevo método abstracto para desglose de impuestos
    public abstract java.util.Map<String, Double> calcularImpuestos();

    // --- MANEJO DE CANTIDAD ---
    protected int cantidadCarrito = 1;

    public void setCantidadCarrito(int cantidadCarrito) {
        this.cantidadCarrito = cantidadCarrito;
    }

    public int getCantidadCarrito() {
        return cantidadCarrito;
    }
}