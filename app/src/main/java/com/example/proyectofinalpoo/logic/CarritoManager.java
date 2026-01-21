package com.example.proyectofinalpoo.logic;

import java.util.ArrayList;
import java.util.List;

public class CarritoManager {
    private static CarritoManager instance;
    private List<ProductoBase> carrito;

    private CarritoManager() {
        carrito = new ArrayList<>();
    }

    public static synchronized CarritoManager getInstance() {
        if (instance == null) {
            instance = new CarritoManager();
        }
        return instance;
    }

    // Metodo para agregar producto al carrito
    public void agregarProducto(ProductoBase producto) {
        carrito.add(producto);
    }

    // Metodo para eliminar producto del carrito (NUEVO)
    public void eliminarProducto(int index) {
        if (index >= 0 && index < carrito.size()) {
            carrito.remove(index);
        }
    }

    // Metodo VITAL: Usa el polimorfismo para sumar totales
    public double calcularTotalPagar() {
        double total = 0;
        for (ProductoBase p : carrito) {
            // AQUÍ OCURRE LA MAGIA DEL POLIMORFISMO
            total += p.calcularPrecioFinal();
        }
        return total;
    }

    public List<ProductoBase> getCarrito() {
        return carrito;
    }

    public void vaciarCarrito() {
        carrito.clear();
    }

    // Nuevo metodo: Suma solo los precios BASE (antes de impuestos)
    public double calcularSubtotalBase() {
        double subtotal = 0;
        for (ProductoBase p : carrito) {
            subtotal += p.getPrecioBase();
        }
        return subtotal;
    }

    public java.util.Map<String, Double> calcularDesgloseImpuestos() {
        java.util.Map<String, Double> totalImpuestos = new java.util.HashMap<>();
        for (ProductoBase p : carrito) {
            java.util.Map<String, Double> imp = p.calcularImpuestos();
            for (java.util.Map.Entry<String, Double> entry : imp.entrySet()) {
                String k = entry.getKey();
                Double v = entry.getValue();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    totalImpuestos.put(k, totalImpuestos.getOrDefault(k, 0.0) + v);
                } else {
                    // Compatibilidad simple
                    Double actual = totalImpuestos.get(k);
                    if (actual == null)
                        actual = 0.0;
                    totalImpuestos.put(k, actual + v);
                }
            }
        }
        return totalImpuestos;
    }
}