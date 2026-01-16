package com.example.proyectofinalpoo.logic;

// Clase Abstracta: Define el comportamiento, no guarda datos en BD
public abstract class ProductoBase {
    protected int id; // <--- AGREGA ESTO
    protected String nombre;
    protected double precioBase;
    protected int stock;
    protected double ivaActual; // El valor del IVA se lo pasaremos desde fuera

    public ProductoBase(int id,String nombre, double precioBase, int stock, double ivaActual) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.ivaActual = ivaActual;
    }

    // MÉTODO POLIMÓRFICO: Cada hijo definirá su fórmula
    public abstract double calcularPrecioFinal();

    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }

    public int getId() { return id; }
}