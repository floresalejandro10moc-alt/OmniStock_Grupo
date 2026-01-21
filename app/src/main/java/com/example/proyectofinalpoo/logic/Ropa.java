package com.example.proyectofinalpoo.logic;

public class Ropa extends ProductoBase {
    private boolean esTemporadaAnterior;

    public Ropa(int id, String nombre, double precioBase, int stock, double ivaBD, boolean esTemporadaAnterior, int imagenResId) {
        super(id, nombre, precioBase, stock, ivaBD, imagenResId);
        this.esTemporadaAnterior = esTemporadaAnterior;
    }

    @Override
    public double calcularPrecioFinal() {
        double precioConIva = precioBase * (1 + ivaActual);
        if (esTemporadaAnterior) {
            return precioConIva * 0.80; // 20% de descuento
        }
        return precioConIva;
    }
}