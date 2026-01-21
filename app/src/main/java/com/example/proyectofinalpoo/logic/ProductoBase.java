package com.example.proyectofinalpoo.logic;

public abstract class ProductoBase {
    protected int id, stock, imagenResId;
    protected String nombre;
    protected double precioBase, ivaActual;

    public ProductoBase(int id, String nombre, double precioBase, int stock, double ivaActual, int imagenResId) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.ivaActual = ivaActual;
        this.imagenResId = imagenResId; //
    }

    public abstract double calcularPrecioFinal();
    
    public String getNombre() { return nombre; }
    public int getStock() { return stock; }
    public int getImagenResId() { return imagenResId; }

    public double getPrecioBase() {
        return precioBase;
    }

    public double getIvaActual() {
        return ivaActual;

    }

    public int getId() {
        return id;
    }
}