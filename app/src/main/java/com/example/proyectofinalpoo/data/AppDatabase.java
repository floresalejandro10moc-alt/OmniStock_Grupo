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
@Database(entities = {Usuario.class, Categoria.class, Producto.class, Cliente.class, Factura.class, DetalleFactura.class}, version = 2, exportSchema = false)
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

                // --- 3. PRODUCTOS (¡ESTO FALTABA!) ---
                // OJO: Insertamos manualmente los IDs (1, 2, 3) para que coincidan con PruebaLogica

                // Producto 1: PlayStation (id_Categoria = 1 Electronica)
                db.execSQL("INSERT INTO PRODUCTOS (id_Producto, id_Categoria, pro_Nombre, pro_PrecioBase, pro_Stock, pro_EsTemporadaAnterior, pro_Estado) VALUES (1, 1, 'PlayStation 5 (Test)', 100.0, 10, 0, 'ACT')");

                // Producto 2: Camisa Vieja (id_Categoria = 2 Ropa)
                db.execSQL("INSERT INTO PRODUCTOS (id_Producto, id_Categoria, pro_Nombre, pro_PrecioBase, pro_Stock, pro_EsTemporadaAnterior, pro_Estado) VALUES (2, 2, 'Camisa Vieja (Test)', 100.0, 5, 1, 'ACT')");

                // Producto 3: Manzanas (id_Categoria = 3 Alimentos)
                db.execSQL("INSERT INTO PRODUCTOS (id_Producto, id_Categoria, pro_Nombre, pro_PrecioBase, pro_Stock, pro_EsTemporadaAnterior, pro_Estado) VALUES (3, 3, 'Manzanas (Test)', 50.0, 100, 0, 'ACT')");
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
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}