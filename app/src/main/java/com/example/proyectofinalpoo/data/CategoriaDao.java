package com.example.proyectofinalpoo.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.proyectofinalpoo.model.Categoria;
import java.util.List;

@Dao
public interface CategoriaDao {
    @Insert
    void insertarCategoria(Categoria categoria);

    @Query("SELECT * FROM CATEGORIA")
    LiveData<List<Categoria>> obtenerCategorias();
    
    @Query("SELECT * FROM CATEGORIA WHERE id_Categoria = :idCategoria")
    Categoria obtenerCategoriaPorId(int idCategoria);

    @Query("UPDATE CATEGORIA SET cat_IVA = :nuevoIVA WHERE cat_Nombre = :nombreCategoria")
    void actualizarIVACategoria(String nombreCategoria, double nuevoIVA);

    @Query("UPDATE CATEGORIA SET cat_Impuesto = :nuevoImpuesto WHERE cat_Nombre = :nombreCategoria")
    void actualizarImpuestoCategoria(String nombreCategoria, double nuevoImpuesto);
}