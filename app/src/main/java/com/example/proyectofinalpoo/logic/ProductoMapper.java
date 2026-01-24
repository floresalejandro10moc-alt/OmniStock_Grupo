package com.example.proyectofinalpoo.logic;

import com.example.proyectofinalpoo.model.Categoria;
import com.example.proyectofinalpoo.model.Producto;

public class ProductoMapper {

    public static ProductoBase convertirEntidadALogica(Producto entidadProducto, Categoria entidadCategoria) {

        if (entidadProducto == null || entidadCategoria == null) {
            return null;
        }

        double ivaDeLaBD = entidadCategoria.iva / 100.0;
        double impuestoExtra = entidadCategoria.impuesto / 100.0;
        String tipo = (entidadCategoria.nombre != null) ? entidadCategoria.nombre.toUpperCase() : "";

        // CASO 1: ELECTRONICOS
        if (tipo.contains("ELECTRONI") || tipo.contains("ELECTRO")) {
            return new Electronico(
                    entidadProducto.id_Producto, // <--- IMPORTANTE: EL ID
                    entidadProducto.nombre,
                    entidadProducto.precioBase,
                    entidadProducto.stock,
                    ivaDeLaBD,
                    impuestoExtra);
        }
        // CASO 2: ROPA
        else if (tipo.contains("ROPA") || tipo.contains("VESTIMENTA") || tipo.contains("TEXTIL")) {
            boolean esViejo = (entidadProducto.esTemporadaAnterior == 1);
            return new Ropa(
                    entidadProducto.id_Producto, // <--- IMPORTANTE: EL ID
                    entidadProducto.nombre,
                    entidadProducto.precioBase,
                    entidadProducto.stock,
                    ivaDeLaBD,
                    esViejo);
        }
        // CASO 3: ALIMENTOS
        else if (tipo.contains("ALIMENTO") || tipo.contains("COMIDA") || tipo.contains("FRUTA")) {
            return new Alimento(
                    entidadProducto.id_Producto, // <--- IMPORTANTE: EL ID
                    entidadProducto.nombre,
                    entidadProducto.precioBase,
                    entidadProducto.stock,
                    entidadProducto.fechaCaducidad); // Nuevo: pasamos fecha
        }

        // CASO 4: DEFECTO
        return new Electronico(entidadProducto.id_Producto, entidadProducto.nombre, entidadProducto.precioBase,
                entidadProducto.stock, ivaDeLaBD, 0);
    }
}