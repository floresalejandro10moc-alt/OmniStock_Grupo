package com.example.proyectofinalpoo.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.proyectofinalpoo.model.Producto;
import com.example.proyectofinalpoo.model.ProductoConCategoria;
import java.util.List;

@Dao
public interface ProductoDao {
    @Insert
    void insertarProducto(Producto producto);

    @Query("SELECT * FROM PRODUCTOS")
    LiveData<List<Producto>> obtenerTodosLosProductos();

    @Transaction
    @Query("SELECT * FROM PRODUCTOS")
    LiveData<List<ProductoConCategoria>> obtenerTodosLosProductosConCategoria();

    @Query("UPDATE PRODUCTOS SET pro_Stock = pro_Stock - :cantidad WHERE id_Producto = :id")
    void actualizarStock(int id, int cantidad);
}
