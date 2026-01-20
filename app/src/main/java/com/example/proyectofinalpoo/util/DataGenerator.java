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

            // 1. CARGAR USUARIOS Y CLIENTES (200 registros)
            for (int i = 1; i <= 200; i++) {
                // Si sale error aquí, asegúrate de tener el constructor vacío en Usuario.java
                Usuario u = new Usuario();
                u.alias = "User_" + i;
                u.clave = "pass" + i;
                u.correo = "user" + i + "@omnistock.ec";
                u.esAdministrador = (i == 1) ? 1 : 0;
                u.estado = "ACT";

                Cliente c = new Cliente();
                c.nombre = "Nombre_" + i;
                c.apellido = "Apellido_" + i;
                c.cedula = "17" + String.format("%08d", i);
                c.celular = "099" + String.format("%07d", i);

                // Tu transacción para vincularlos automáticamente
                db.inventarioDao().registrarUsuarioYCliente(u, c);
            }

            // 2. CARGAR PRODUCTOS (200 registros)
            for (int i = 1; i <= 200; i++) {
                Producto p = new Producto();
                p.id_Categoria = rand.nextInt(3) + 1; // 1: Electro, 2: Ropa, 3: Alimentos
                p.nombre = "Producto_Test_" + i;
                p.precioBase = 10 + (500 * rand.nextDouble());
                p.stock = rand.nextInt(100) + 1;
                p.esTemporadaAnterior = rand.nextInt(2);
                p.estado = "ACT";

                db.inventarioDao().insertarProducto(p);
            }

            // 3. CARGAR FACTURAS Y DETALLES (200 registros)
            for (int i = 1; i <= 200; i++) {
                Factura f = new Factura();
                // Eliminamos f.estado porque lo quitaste del modelo
                f.cedula = "17" + String.format("%08d", rand.nextInt(200) + 1);
                f.fecha = "2026-01-20";

                long idFac = db.inventarioDao().insertarFactura(f); //

                for (int j = 0; j < 2; j++) {
                    DetalleFactura d = new DetalleFactura();
                    d.id_Factura = (int) idFac;
                    d.id_Producto = rand.nextInt(200) + 1;
                    d.cantidad = rand.nextInt(5) + 1;
                    d.precioUnitario = 25.0;

                    db.inventarioDao().insertarDetalleFactura(d); //
                }
            }
        });
    }
}