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

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // 2. EL CALLBACK MÁGICO
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // --- USUARIOS (ESTO ESTABA BIEN) ---
                // Usaste usu_Alias, usu_Clave, etc. Esto coincide con tu modelo Usuario. ¡Bien!
                db.execSQL("INSERT INTO USUARIOS (usu_Alias, usu_Clave, usu_Correo, usu_Administrador, usu_Estado) VALUES ('AdminAlejo', 'admin123', 'admin@omnistock.com', 1, 'ACT')");
                db.execSQL("INSERT INTO USUARIOS (usu_Alias, usu_Clave, usu_Correo, usu_Administrador, usu_Estado) VALUES ('VendedorJuan', 'vend123', 'juan@omnistock.com', 0, 'ACT')");

                // --- CATEGORIAS (AQUÍ ESTÁ LA CORRECCIÓN) ---
                // Antes decia: (nombre, iva, impuesto_adicional) -> ERROR
                // Ahora dice:  (cat_Nombre, cat_IVA, cat_Impuesto) -> CORRECTO
                db.execSQL("INSERT INTO CATEGORIA (cat_Nombre, cat_IVA, cat_Impuesto) VALUES ('Electronica', 15.0, 5.0)"); // 5% suntuario
                db.execSQL("INSERT INTO CATEGORIA (cat_Nombre, cat_IVA, cat_Impuesto) VALUES ('Ropa', 12.0, 0.0)");
                db.execSQL("INSERT INTO CATEGORIA (cat_Nombre, cat_IVA, cat_Impuesto) VALUES ('Alimentos', 0.0, 0.0)");
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
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}