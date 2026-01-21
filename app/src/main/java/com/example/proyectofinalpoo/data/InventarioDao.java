package com.example.proyectofinalpoo.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.proyectofinalpoo.model.Cliente;
import com.example.proyectofinalpoo.model.DetalleFactura;
import com.example.proyectofinalpoo.model.Factura;
import com.example.proyectofinalpoo.model.Usuario;

@Dao
public interface InventarioDao {
    // --- USUARIOS (Esto soluciona el error .login en LoginActivity) ---
    @Query("SELECT * FROM USUARIOS WHERE usu_Alias = :user AND usu_Clave = :pass AND usu_Estado = 'ACT' LIMIT 1")
    Usuario login(String user, String pass);

    @Insert
    void insertarUsuario(Usuario usuario);

    // --- CLIENTES ---
    @Insert
    void insertarCliente(Cliente cliente);

    @Insert
    long insertarFactura(Factura factura); // Devuelve el ID de la factura creada

    @Insert
    void insertarDetalle(DetalleFactura detalle);
}