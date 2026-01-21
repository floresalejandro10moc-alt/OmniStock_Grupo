package com.example.proyectofinalpoo.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.proyectofinalpoo.model.Cliente;
import com.example.proyectofinalpoo.model.DetalleFactura;
import com.example.proyectofinalpoo.model.Factura;
import com.example.proyectofinalpoo.model.Usuario;
import com.example.proyectofinalpoo.model.Producto;
import com.example.proyectofinalpoo.model.Categoria;
import com.example.proyectofinalpoo.model.DetalleVisual;
import androidx.room.Transaction;
import java.util.List;

@Dao
public interface InventarioDao {
    // --- PRODUCTOS ---
    @Insert
    void insertarProducto(Producto producto);

    @Insert
    long insertarFactura(Factura factura);

    @Insert
    void insertarDetalle(DetalleFactura detalle);

    @Query("SELECT * FROM PRODUCTOS")
    List<Producto> obtenerTodosProductos();

    @Query("UPDATE PRODUCTOS SET pro_Stock = pro_Stock - :cantidad WHERE id_Producto = :id")
    void actualizarStock(int id, int cantidad);

    @Query("UPDATE PRODUCTOS SET pro_Estado = 'INA' WHERE id_Producto = :id")
    void eliminarProductoLogico(int id);

    // --- CATEGORIAS ---
    @Insert
    void insertarCategoria(Categoria categoria);

    @Query("SELECT * FROM CATEGORIA")
    List<Categoria> obtenerCategorias();

    // MeTODO PARA CAMBIAR IVA ESPECÍFICO
    @Query("UPDATE CATEGORIA SET cat_IVA = :nuevoIVA WHERE cat_Nombre = :nombreCategoria")
    void actualizarIVACategoria(String nombreCategoria, double nuevoIVA);

    // MeTODO PARA ACTUALIZAR IMPUESTO SUNTUARIO (cat_Impuesto)
    @Query("UPDATE CATEGORIA SET cat_Impuesto = :nuevoImpuesto WHERE cat_Nombre = :nombreCategoria")
    void actualizarImpuestoCategoria(String nombreCategoria, double nuevoImpuesto);

    // --- HISTORIAL DE FACTURAS ---

    // 1. Para el ADMIN: Ver todas las facturas
    @Query("SELECT * FROM FACTURAS ORDER BY id_Factura DESC")
    List<Factura> obtenerTodasLasFacturas();

    // 2. Para el CLIENTE: Ver solo SUS facturas (CORREGIDO)
    @Query("SELECT * FROM FACTURAS WHERE fac_Cedula = :cedulaCliente ORDER BY id_Factura DESC")
    List<Factura> obtenerFacturasPorCliente(String cedulaCliente);

    // 3. Para el DETALLE: Obtener una factura específica
    @Query("SELECT * FROM FACTURAS WHERE id_Factura = :id LIMIT 1")
    Factura obtenerFacturaPorId(long id);

    // 4. Para el DETALLE: Obtener los productos de esa factura
    @Query("SELECT * FROM DETALLE_FACTURA WHERE id_Factura = :idFactura")
    List<DetalleFactura> obtenerDetallesPorFactura(long idFactura);

    @Query("SELECT * FROM USUARIOS WHERE usu_Alias = :user AND usu_Clave = :pass AND usu_Estado = 'ACT' LIMIT 1")
    Usuario login(String user, String pass);

    // DEBE SER LONG para que la transacción funcione
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertarUsuario(Usuario usuario);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertarCliente(Cliente cliente);

    @Transaction
    default void registrarUsuarioYCliente(Usuario usuario, Cliente cliente) {
        long nuevoId = insertarUsuario(usuario); // Aquí se usa el long
        cliente.id_Usuario = (int) nuevoId;
        insertarCliente(cliente);
    }

    // 1. BUSCAR CLIENTE COMPLETO POR ALIAS DE USUARIO
    // Unimos Cliente con Usuario para encontrar al dueño de la sesión actual
    @Query("SELECT c.* FROM CLIENTE c " +
            "INNER JOIN USUARIOS u ON c.id_Usuario = u.id_Usuario " +
            "WHERE u.usu_Alias = :aliasUsuario LIMIT 1")
    Cliente obtenerClientePorAlias(String aliasUsuario);

    // 2. OBTENER DETALLES + NOMBRE DEL PRODUCTO (JOIN)
    // Esto soluciona que la lista salga vacía o sin nombres
    @Query("SELECT p.pro_Nombre as nombreProducto, d.det_Cantidad as cantidad, d.det_PrecioUnitario as precioUnitario, d.det_Subtotal as subtotal "
            +
            "FROM DETALLE_FACTURA d " +
            "INNER JOIN PRODUCTOS p ON d.id_Producto = p.id_Producto " +
            "WHERE d.id_Factura = :idFactura")
    List<DetalleVisual> obtenerDetallesVisuales(long idFactura);
}