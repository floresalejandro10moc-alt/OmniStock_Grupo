package com.example.proyectofinalpoo.logic;

import com.example.proyectofinalpoo.model.Categoria;
import com.example.proyectofinalpoo.model.Producto;

public class ProductoMapper {

    // Método estático: No necesitas crear una instancia de Mapper para usarlo
    public static ProductoBase convertirEntidadALogica(Producto entidadProducto, Categoria entidadCategoria) {

        // 1. Validaciones de seguridad (por si la BD devuelve nulos)
        if (entidadProducto == null || entidadCategoria == null) {
            return null;
        }

        // 2. Obtenemos los datos clave de la CATEGORÍA
        double ivaDeLaBD = entidadCategoria.iva;
        double impuestoExtra = entidadCategoria.impuesto; // El suntuario viene de aquí

        // 3. Normalizamos el nombre para detectar el tipo (Todo a mayúsculas)
        String tipo = entidadCategoria.nombre.toUpperCase();

        // 4. EL CEREBRO: Decide qué clase crear
        if (tipo.contains("ELECTRONI") || tipo.contains("ELECTRO")) {
            return new Electronico(
                    entidadProducto.nombre,
                    entidadProducto.precioBase,
                    entidadProducto.stock,
                    ivaDeLaBD,
                    impuestoExtra
            );
        }
        else if (tipo.contains("ROPA") || tipo.contains("VESTIMENTA") || tipo.contains("TEXTIL")) {
            // Convertimos el int (0 o 1) de la BD a boolean
            boolean esViejo = (entidadProducto.esTemporadaAnterior == 1);

            return new Ropa(
                    entidadProducto.nombre,
                    entidadProducto.precioBase,
                    entidadProducto.stock,
                    ivaDeLaBD,
                    esViejo
            );
        }
        else if (tipo.contains("ALIMENTO") || tipo.contains("COMIDA") || tipo.contains("FRUTA")) {
            return new Alimento(
                    entidadProducto.nombre,
                    entidadProducto.precioBase,
                    entidadProducto.stock
            );
        }

        // 5. Caso por defecto: Si crearon una categoría rara (ej: "Juguetes")
        // Devolvemos un producto genérico que al menos tenga precio + IVA
        return new Electronico(entidadProducto.nombre, entidadProducto.precioBase, entidadProducto.stock, ivaDeLaBD, 0);
    }
}