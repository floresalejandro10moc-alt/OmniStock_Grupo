package com.example.proyectofinalpoo.util;

import android.content.Context;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.model.*;
import java.util.Random;

public class DataGenerator {

    public static void cargar200Datos(Context context) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            Random rand = new Random();

            // Datos reales de Ecuador para que no haya nulos
            String[] nombres = {"Juan", "María", "Luis", "Ana", "Carlos", "Carmen", "José", "Patricia", "Jorge", "Martha"};
            String[] apellidos = {"Flores", "García", "Zambrano", "López", "Castillo", "Moreno", "Suárez", "Torres", "Vaca", "Sánchez"};
            String[] electro = {"Laptop ASUS", "Samsung A54", "Sony WH", "Tablet Lenovo", "Monitor Dell"};
            String[] ropa = {"Camiseta Marathon", "Jean Levi's", "Chaqueta Cuero", "Zapatos Venus", "Gorra New Era"};
            String[] alimentos = {"Arroz Extra", "Aceite Favorita", "Atún Real", "Leche Vita", "Café Buendía"};

            // 1. CARGAR USUARIOS Y CLIENTES (200 registros)
            for (int i = 1; i <= 200; i++) {
                Usuario u = new Usuario();
                String n = nombres[rand.nextInt(10)];
                String a = apellidos[rand.nextInt(10)];

                u.alias = n.toLowerCase() + i;
                u.clave = "pass" + i;
                u.correo = u.alias + "@omnistock.ec";
                u.esAdministrador = (i == 1) ? 1 : 0; // El primero es el AdminAlejo
                u.estado = "ACT";

                Cliente c = new Cliente();
                c.nombre = n;
                c.apellido = a;
                c.cedula = (rand.nextBoolean() ? "17" : "09") + String.format("%08d", i); // Pichincha o Guayas
                c.celular = "09" + (80000000 + rand.nextInt(10000000));
                c.estado = "ACT"; // 2. CORRECCIÓN: Todos los clientes activos

                // Vinculación automática mediante tu DAO
                db.inventarioDao().registrarUsuarioYCliente(u, c);
            }

            // 2. CARGAR PRODUCTOS (200 registros)
            for (int i = 1; i <= 200; i++) {
                Producto p = new Producto();
                int cat = rand.nextInt(3) + 1; // 1:Electro, 2:Ropa, 3:Alimentos
                p.id_Categoria = cat;

                if (cat == 1) p.nombre = electro[rand.nextInt(5)] + " " + i;
                else if (cat == 2) p.nombre = ropa[rand.nextInt(5)] + " " + i;
                else p.nombre = alimentos[rand.nextInt(5)] + " " + i;

                p.precioBase = 5.50 + (rand.nextDouble() * 450);
                p.stock = rand.nextInt(50) + 10;
                p.esTemporadaAnterior = rand.nextInt(2); // 0 o 1
                p.estado = "ACT";

                db.inventarioDao().insertarProducto(p);
            }

            // 3. CARGAR FACTURAS Y DETALLES (200 registros)
            for (int i = 1; i <= 200; i++) {
                Factura f = new Factura();
                // 1. CORRECCIÓN: Incluimos el id_Cliente vinculando con los clientes creados
                // Como creamos 200 clientes, les asignamos un ID aleatorio entre 1 y 200
                f.id_cliente = rand.nextInt(200) + 1;

                f.cedula = "17" + String.format("%08d", f.id_cliente);
                f.fecha = "2026-01-21"; // Fecha actual de tu sistema

                // Insertamos la factura y recuperamos el ID para el detalle
                long idFac = db.inventarioDao().insertarFactura(f);

                for (int j = 0; j < 2; j++) {
                    DetalleFactura d = new DetalleFactura();
                    d.id_Factura = (int) idFac;
                    d.id_Producto = rand.nextInt(200) + 1;
                    d.cantidad = rand.nextInt(5) + 1;
                    d.precioUnitario = 15.0 + (rand.nextDouble() * 100);

                    // Aseguramos que el detalle tampoco tenga nulos en subtotal si lo usas
                    // d.det_Subtotal = d.cantidad * d.precioUnitario;

                    db.inventarioDao().insertarDetalleFactura(d);
                }
            }
        });
    }
}