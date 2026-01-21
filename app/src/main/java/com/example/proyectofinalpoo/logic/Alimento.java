package com.example.proyectofinalpoo.logic;

public class Alimento extends ProductoBase {

    public Alimento(int id, String nombre, double precioBase, int stock) {
        // Al super le pasamos 0.0 en el IVA porque los alimentos no lo llevan
        super(id, nombre, precioBase, stock, 0.0);
    }

    @Override
    public double calcularPrecioFinal() {
        return precioBase;
    }

    @Override
    public java.util.Map<String, Double> calcularImpuestos() {
        return new java.util.HashMap<>(); // Sin impuestos
    }
}