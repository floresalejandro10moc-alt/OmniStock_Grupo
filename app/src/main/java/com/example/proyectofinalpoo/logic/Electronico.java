package com.example.proyectofinalpoo.logic;

public class Electronico extends ProductoBase {
    private double impuestoSuntuario;

    public Electronico(int id, String nombre, double precioBase, int stock, double ivaBD, double suntuarioBD, int imagenResId) {
        super(id, nombre, precioBase, stock, ivaBD, imagenResId);
        this.impuestoSuntuario = suntuarioBD;
    }

    @Override
    public double calcularPrecioFinal() {
        double totalImpuestos = ivaActual + impuestoSuntuario;
        return precioBase * (1 + totalImpuestos);
    }
}