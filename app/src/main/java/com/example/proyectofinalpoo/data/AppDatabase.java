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

@Database(entities = {Usuario.class, Categoria.class, Producto.class, Cliente.class, Factura.class, DetalleFactura.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract InventarioDao inventarioDao();
    public abstract ProductoDao productoDao();
    public abstract CategoriaDao categoriaDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // --- INSERCIÓN DE DATOS INICIALES ---
                db.execSQL("INSERT INTO USUARIOS (usu_Alias, usu_Clave, usu_Correo, usu_Administrador, usu_Estado) VALUES ('AdminAlejo', 'admin123', 'admin@omnistock.com', 1, 'ACT')");
                db.execSQL("INSERT INTO USUARIOS (usu_Alias, usu_Clave, usu_Correo, usu_Administrador, usu_Estado) VALUES ('VendedorJuan', 'vend123', 'juan@omnistock.com', 0, 'ACT')");

                db.execSQL("INSERT INTO CATEGORIA (cat_Nombre, cat_IVA, cat_Impuesto) VALUES ('Electronica', 15.0, 5.0)");
                db.execSQL("INSERT INTO CATEGORIA (cat_Nombre, cat_IVA, cat_Impuesto) VALUES ('Ropa', 12.0, 0.0)");
                db.execSQL("INSERT INTO CATEGORIA (cat_Nombre, cat_IVA, cat_Impuesto) VALUES ('Alimentos', 0.0, 0.0)");

                // ¡CORRECCIÓN FINAL! Usamos 0 para todas las imágenes.
                // El ProductoMapper se encargará de asignar una imagen por defecto.
                db.execSQL("INSERT INTO PRODUCTOS (id_Producto, id_Categoria, pro_Nombre, pro_PrecioBase, pro_Stock, pro_ImagenResId, pro_EsTemporadaAnterior) VALUES (1, 1, 'PlayStation 5', 500.0, 15, 0, 0)");
                db.execSQL("INSERT INTO PRODUCTOS (id_Producto, id_Categoria, pro_Nombre, pro_PrecioBase, pro_Stock, pro_ImagenResId, pro_EsTemporadaAnterior) VALUES (2, 2, 'Camisa de Temporada', 25.0, 50, 0, 0)");
                db.execSQL("INSERT INTO PRODUCTOS (id_Producto, id_Categoria, pro_Nombre, pro_PrecioBase, pro_Stock, pro_ImagenResId, pro_EsTemporadaAnterior) VALUES (3, 3, 'Manzanas Frescas', 2.5, 200, 0, 0)");
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
                            .fallbackToDestructiveMigration() // La base de datos se recreará por el cambio de versión.
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}