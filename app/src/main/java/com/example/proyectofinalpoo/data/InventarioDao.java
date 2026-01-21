package com.example.proyectofinalpoo.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.proyectofinalpoo.model.*;
import java.util.List;

@Dao
public interface InventarioDao {

    @Insert
    long insertarFactura(Factura factura);

    @Insert
    void insertarDetalle(DetalleFactura detalle);

    @Query("UPDATE PRODUCTOS SET pro_Stock = pro_Stock - :cantidad WHERE id_Producto = :id")
    void actualizarStock(int id, int cantidad);

    @Query("SELECT * FROM USUARIOS WHERE usu_Alias = :user AND usu_Clave = :pass AND usu_Estado = 'ACT' LIMIT 1")
    Usuario login(String user, String pass);

    @Transaction
    default void registrarUsuarioYCliente(Usuario usuario, Cliente cliente) {
        long nuevoId = insertarUsuario(usuario);
        cliente.id_Usuario = (int) nuevoId;
        insertarCliente(cliente);
    }

    @Insert
    long insertarUsuario(Usuario usuario);

    @Insert
    void insertarCliente(Cliente cliente);

    @Query("SELECT c.* FROM CLIENTE c INNER JOIN USUARIOS u ON c.id_Usuario = u.id_Usuario WHERE u.usu_Alias = :aliasUsuario LIMIT 1")
    Cliente obtenerClientePorAlias(String aliasUsuario);

    // --- Consultas para Historial y Detalle ---

    @Query("SELECT * FROM FACTURAS ORDER BY id_Factura DESC")
    List<Factura> obtenerTodasLasFacturas();

    @Query("SELECT * FROM FACTURAS WHERE fac_Cedula = :cedulaCliente ORDER BY id_Factura DESC")
    List<Factura> obtenerFacturasPorCliente(String cedulaCliente);

    @Query("SELECT * FROM FACTURAS WHERE id_Factura = :id LIMIT 1")
    Factura obtenerFacturaPorId(long id);

    // ¡CONSULTA CORREGIDA!
    // Se usan alias (AS) para que Room pueda mapear las columnas a los objetos con
    // prefijo.
    @Transaction
    @Query("SELECT " +
            "    p.id_Producto AS prod_id_Producto, " +
            "    p.pro_Nombre AS prod_pro_Nombre, " +
            "    p.pro_Descripcion AS prod_pro_Descripcion, " +
            "    p.pro_PrecioBase AS prod_pro_PrecioBase, " + // CORREGIDO: El alias coincide con el campo
            "    p.pro_Stock AS prod_pro_Stock, " +
            "    p.pro_EsTemporadaAnterior AS prod_pro_EsTemporadaAnterior, " +
            "    p.pro_FechaCaducidad AS prod_pro_FechaCaducidad, " +
            "    p.pro_Estado AS prod_pro_Estado, " +
            "    p.id_Categoria AS prod_id_Categoria, " +
            "    c.id_Categoria AS cat_id_Categoria, " +
            "    c.cat_Nombre AS cat_cat_Nombre, " +
            "    c.cat_Descripcion AS cat_cat_Descripcion, " +
            "    c.cat_IVA AS cat_cat_IVA, " +
            "    c.cat_Impuesto AS cat_cat_Impuesto, " +
            "    c.cat_Estado AS cat_cat_Estado, " +
            "    d.det_Cantidad AS cantidadComprada " +
            "FROM DETALLE_FACTURA d " +
            "INNER JOIN PRODUCTOS p ON d.id_Producto = p.id_Producto " +
            "INNER JOIN CATEGORIA c ON p.id_Categoria = c.id_Categoria " +
            "WHERE d.id_Factura = :idFactura")
    List<DetalleParaLogica> obtenerDetallesCompletosParaLogica(long idFactura);

    @Query("SELECT * FROM PRODUCTOS")
    List<Producto> obtenerTodosProductos();

    @Query("SELECT * FROM Categoria")
    List<Categoria> obtenerCategorias();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarProducto(Producto producto);

    @Insert
    void insertarDetalleFactura(DetalleFactura detalleFactura);

}
