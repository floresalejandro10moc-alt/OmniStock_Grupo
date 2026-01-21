package com.example.proyectofinalpoo.logic;

public class Ropa extends ProductoBase {
    private boolean esTemporadaAnterior;

    public Ropa(int id, String nombre, double precioBase, int stock, double ivaBD, boolean esTemporadaAnterior) {
        super(id, nombre, precioBase, stock, ivaBD);
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

    @Override
    public java.util.Map<String, Double> calcularImpuestos() {
        java.util.Map<String, Double> impuestos = new java.util.HashMap<>();

        // El descuento aplica sobre el precio total, así que afecta proporcionalmente
        // al IVA.
        // Formula: BaseDescontada = Base * Factor
        // IVA = BaseDescontada * TasaIVA
        double factor = esTemporadaAnterior ? 0.80 : 1.0;
        double baseCalculo = precioBase * factor;

        if (ivaActual > 0) {
            impuestos.put("IVA " + (int) (ivaActual * 100) + "%", baseCalculo * ivaActual);
        }
        return impuestos;
    }
}