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
}