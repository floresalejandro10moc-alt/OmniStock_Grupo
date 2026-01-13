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

// 1. Aquí definimos las tablas de la base de datos
@Database(entities = {Usuario.class, Categoria.class, Producto.class, Cliente.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract InventarioDao inventarioDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    // Executor para realizar operaciones en segundo plano (Standard en Room)
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // 2. EL CALLBACK MÁGICO (Esto llena los datos)
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Usamos un hilo secundario para no congelar la app al instalar
            databaseWriteExecutor.execute(() -> {
                // Inyectar USUARIOS
                // Admin (Clave: admin123)
                db.execSQL("INSERT INTO USUARIOS (usu_Alias, usu_Clave, usu_Correo, usu_Administrador, usu_Estado) VALUES ('AdminAlejo', 'admin123', 'admin@omnistock.com', 1, 'ACT')");

                // Vendedor (Clave: vend123)
                db.execSQL("INSERT INTO USUARIOS (usu_Alias, usu_Clave, usu_Correo, usu_Administrador, usu_Estado) VALUES ('VendedorJuan', 'vend123', 'juan@omnistock.com', 0, 'ACT')");

                // Inyectar CATEGORIAS (Necesarias para crear productos después)
                db.execSQL("INSERT INTO CATEGORIA (nombre, iva, impuesto_adicional) VALUES ('Electronica', 15.0, 5.0)");
                db.execSQL("INSERT INTO CATEGORIA (nombre, iva, impuesto_adicional) VALUES ('Ropa', 12.0, 0.0)");
                db.execSQL("INSERT INTO CATEGORIA (nombre, iva, impuesto_adicional) VALUES ('Alimentos', 0.0, 0.0)");
            });
        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "omnistock_db")
                            .addCallback(sRoomDatabaseCallback) // <--- Aquí conectamos el Callback
                            .allowMainThreadQueries() // Permite consultas simples en el Login
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}