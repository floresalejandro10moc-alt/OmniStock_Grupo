package com.example.proyectofinalpoo.logic;

public class Alimento extends ProductoBase {

    public Alimento(int id, String nombre, double precioBase, int stock, double ivaBD, int imagenResId) {
        super(id, nombre, precioBase, stock, ivaBD, imagenResId);
    }

    @Override
    public double calcularPrecioFinal() {
        return precioBase * (1 + ivaActual);
    }
}