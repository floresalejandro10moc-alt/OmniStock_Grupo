package com.example.proyectofinalpoo.logic;

public class Ropa extends ProductoBase {
    private boolean esTemporadaAnterior;

    public Ropa(int id,String nombre, double precioBase, int stock, double ivaBD, boolean esTemporadaAnterior) {
        super(id,nombre, precioBase, stock, ivaBD);
        this.esTemporadaAnterior = esTemporadaAnterior;
    }

    @Override
    public double calcularPrecioFinal() {
        // 1. Aplicamos IVA
        double precioConIva = precioBase * (1 + ivaActual);

        // 2. Aplicamos Descuento si corresponde
        if (esTemporadaAnterior) {
            return precioConIva * 0.80; // 20% de descuento
        } else {
            return precioConIva;
        }
    }
}