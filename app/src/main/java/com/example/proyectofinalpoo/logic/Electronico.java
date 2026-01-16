package com.example.proyectofinalpoo.logic;

public class Electronico extends ProductoBase {
    private double impuestoSuntuario;

    public Electronico(int id,String nombre, double precioBase, int stock, double ivaBD, double suntuarioBD) {
        super(id,nombre, precioBase, stock, ivaBD);
        this.impuestoSuntuario = suntuarioBD;
    }

    @Override
    public double calcularPrecioFinal() {
        // Sumamos porcentajes: Ej. 0.15 + 0.05 = 0.20
        double totalImpuestos = ivaActual + impuestoSuntuario;
        return precioBase * (1 + totalImpuestos);
    }
}