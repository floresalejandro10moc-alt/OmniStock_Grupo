package com.example.proyectofinalpoo.logic;

import java.util.ArrayList;
import java.util.List;

public class PruebaLogica {

    public static List<ProductoBase> getProductosSimulados() {
        List<ProductoBase> lista = new ArrayList<>();

        // CORRECCIÓN: Agregamos un ID inventado al principio (1, 2, 3...)
        // Esto satisface al nuevo constructor que pide (int id, String nombre...)

        // ID: 1
        lista.add(new Electronico(1, "PlayStation 5 (Test)", 100.00, 10, 0.15, 0.05));

        // ID: 2
        lista.add(new Ropa(2, "Camisa Vieja (Test)", 100.00, 5, 0.12, true));

        // ID: 3
        lista.add(new Alimento(3, "Manzanas (Test)", 50.00, 100));

        return lista;
    }
}