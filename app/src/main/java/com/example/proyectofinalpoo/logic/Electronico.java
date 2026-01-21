package com.example.proyectofinalpoo.logic;

public class Electronico extends ProductoBase {
    private double impuestoSuntuario;

    public Electronico(int id, String nombre, double precioBase, int stock, double ivaBD, double suntuarioBD) {
        super(id, nombre, precioBase, stock, ivaBD);
        this.impuestoSuntuario = suntuarioBD;
    }

    @Override
    public double calcularPrecioFinal() {
        // Sumamos porcentajes: Ej. 0.15 + 0.05 = 0.20
        double totalImpuestos = ivaActual + impuestoSuntuario;
        return precioBase * (1 + totalImpuestos);
    }

    @Override
    public java.util.Map<String, Double> calcularImpuestos() {
        java.util.Map<String, Double> impuestos = new java.util.HashMap<>();
        // 1. IVA
        if (ivaActual > 0) {
            impuestos.put("IVA " + (int) (ivaActual * 100) + "%", precioBase * ivaActual);
        }
        // 2. Impuesto Suntuario
        if (impuestoSuntuario > 0) {
            impuestos.put("Impuesto Suntuario", precioBase * impuestoSuntuario);
        }
        return impuestos;
    }
}