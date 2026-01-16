package com.example.proyectofinalpoo.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.proyectofinalpoo.model.*;
import java.util.List;

@Dao
public interface InventarioDao {
    // --- USUARIOS (Esto soluciona el error .login en LoginActivity) ---
    @Query("SELECT * FROM USUARIOS WHERE usu_Alias = :user AND usu_Clave = :pass AND usu_Estado = 'ACT' LIMIT 1")
    Usuario login(String user, String pass);

    @Insert
    void insertarUsuario(Usuario usuario);

    // --- PRODUCTOS ---
    @Insert
    void insertarProducto(Producto producto);

    @Query("SELECT * FROM PRODUCTOS")
    List<Producto> obtenerTodosProductos();

    @Query("UPDATE PRODUCTOS SET pro_Stock = pro_Stock - :cantidad WHERE id_Producto = :id")
    void actualizarStock(int id, int cantidad);

    // --- CATEGORIAS ---
    @Insert
    void insertarCategoria(Categoria categoria);

    @Query("SELECT * FROM CATEGORIA")
    List<Categoria> obtenerCategorias();

    // --- CLIENTES ---
    @Insert
    void insertarCliente(Cliente cliente);

    // MeTODO PARA CAMBIAR IVA ESPECÍFICO
    @Query("UPDATE CATEGORIA SET cat_IVA = :nuevoIVA WHERE cat_Nombre = :nombreCategoria")
    void actualizarIVACategoria(String nombreCategoria, double nuevoIVA);

    // MeTODO PARA ACTUALIZAR IMPUESTO SUNTUARIO (cat_Impuesto)
    @Query("UPDATE CATEGORIA SET cat_Impuesto = :nuevoImpuesto WHERE cat_Nombre = :nombreCategoria")
    void actualizarImpuestoCategoria(String nombreCategoria, double nuevoImpuesto);

    @Insert
    long insertarFactura(Factura factura); // Devuelve el ID de la factura creada

    @Insert
    void insertarDetalle(DetalleFactura detalle);
}