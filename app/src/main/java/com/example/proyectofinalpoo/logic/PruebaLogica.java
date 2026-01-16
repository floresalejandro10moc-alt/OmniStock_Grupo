package com.example.proyectofinalpoo.logic;

import java.util.ArrayList;
import java.util.List;

public class PruebaLogica {

    // Este metodo simula que la Base de Datos nos devolvió productos
    public static List<ProductoBase> getProductosSimulados() {
        List<ProductoBase> lista = new ArrayList<>();

        // Simulamos un Electrónico (IVA 15%, Suntuario 5%)
        // Precio base 100 -> Total esperado: 120
        lista.add(new Electronico("PlayStation 5 (Test)", 100.00, 10, 0.15, 0.05));

        // Simulamos Ropa Temporada Anterior (IVA 12%, Descuento 20%)
        // Precio 100 -> +IVA(112) -> -20%(89.6)
        lista.add(new Ropa("Camisa Vieja (Test)", 100.00, 5, 0.12, true));

        // Simulamos Alimento (Sin IVA)
        // Precio 50 -> Total 50
        lista.add(new Alimento("Manzanas (Test)", 50.00, 100));

        return lista;
    }
}