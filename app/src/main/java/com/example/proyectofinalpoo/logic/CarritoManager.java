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
        // Verificar si el producto ya existe para aumentar cantidad
        for (ProductoBase p : carrito) {
            if (p.getId() == producto.getId()) {
                p.setCantidadCarrito(p.getCantidadCarrito() + 1);
                return;
            }
        }
        // Si no existe, se agrega (cantidad inicia en 1 por defecto en ProductoBase)
        carrito.add(producto);
    }

    public void aumentarCantidad(int index) {
        if (index >= 0 && index < carrito.size()) {
            ProductoBase p = carrito.get(index);
            p.setCantidadCarrito(p.getCantidadCarrito() + 1);
        }
    }

    public void disminuirCantidad(int index) {
        if (index >= 0 && index < carrito.size()) {
            ProductoBase p = carrito.get(index);
            int nuevaCantidad = p.getCantidadCarrito() - 1;
            if (nuevaCantidad > 0) {
                p.setCantidadCarrito(nuevaCantidad);
            }
        }
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
            // Precio Unitario * Cantidad
            total += p.calcularPrecioFinal() * p.getCantidadCarrito();
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
            subtotal += p.getPrecioBase() * p.getCantidadCarrito();
        }
        return subtotal;
    }

    public java.util.Map<String, Double> calcularDesgloseImpuestos() {
        java.util.Map<String, Double> totalImpuestos = new java.util.HashMap<>();
        for (ProductoBase p : carrito) {
            java.util.Map<String, Double> imp = p.calcularImpuestos();
            int cantidad = p.getCantidadCarrito();

            for (java.util.Map.Entry<String, Double> entry : imp.entrySet()) {
                String k = entry.getKey();
                Double v = entry.getValue() * cantidad; // Impuesto unitario * Cantidad

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