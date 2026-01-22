package com.example.proyectofinalpoo.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.proyectofinalpoo.model.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// ¡VERSIÓN 3! Esto fuerza la recreación de la base de datos.
@Database(entities = { Usuario.class, Categoria.class, Producto.class, Cliente.class, Factura.class,
        DetalleFactura.class }, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract InventarioDao inventarioDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // --- Inserción de datos iniciales ---

                // 1. Usuarios
                db.execSQL(
                        "INSERT INTO USUARIOS (id_Usuario, usu_Alias, usu_Clave, usu_Correo, usu_Administrador, usu_Estado) VALUES (1, 'AdminAlejo', 'admin123', 'admin@omnistock.com', 1, 'ACT')");
                db.execSQL(
                        "INSERT INTO USUARIOS (id_Usuario, usu_Alias, usu_Clave, usu_Correo, usu_Administrador, usu_Estado) VALUES (2, 'VendedorJuan', 'vend123', 'juan@omnistock.com', 0, 'ACT')");

                // 2. Cliente (Asociado a VendedorJuan, ID 2)
                db.execSQL(
                        "INSERT INTO CLIENTE (id_Usuario, cli_Nombre, cli_Apellido, cli_Cedula, cli_Direccion, cli_Celular, cli_Estado) VALUES (2, 'Juan', 'Perez', '0987654321', 'Av. Siempre Viva 123', '0991234567', 'ACT')");

                // 3. Categorías
                db.execSQL(
                        "INSERT INTO CATEGORIA (id_Categoria, cat_Nombre, cat_IVA, cat_Impuesto) VALUES (1, 'Electronica', 15.0, 5.0)");
                db.execSQL(
                        "INSERT INTO CATEGORIA (id_Categoria, cat_Nombre, cat_IVA, cat_Impuesto) VALUES (2, 'Ropa', 12.0, 0.0)");
                db.execSQL(
                        "INSERT INTO CATEGORIA (id_Categoria, cat_Nombre, cat_IVA, cat_Impuesto) VALUES (3, 'Alimentos', 0.0, 0.0)");

                // 4. Productos de Prueba (¡LA RAÍZ DEL PROBLEMA!)
                db.execSQL(
                        "INSERT INTO PRODUCTOS (id_Producto, id_Categoria, pro_Nombre, pro_PrecioBase, pro_Stock, pro_EsTemporadaAnterior) VALUES (1, 1, 'PlayStation 5', 100.0, 10, 0)");
                db.execSQL(
                        "INSERT INTO PRODUCTOS (id_Producto, id_Categoria, pro_Nombre, pro_PrecioBase, pro_Stock, pro_EsTemporadaAnterior) VALUES (2, 2, 'Camisa Vieja', 100.0, 5, 1)");
                db.execSQL(
                        "INSERT INTO PRODUCTOS (id_Producto, id_Categoria, pro_Nombre, pro_PrecioBase, pro_Stock, pro_EsTemporadaAnterior) VALUES (3, 3, 'Manzanas', 50.0, 100, 0)");
            });
        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "omnistock_db")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration() // Permite destruir la BD al cambiar de versión
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}