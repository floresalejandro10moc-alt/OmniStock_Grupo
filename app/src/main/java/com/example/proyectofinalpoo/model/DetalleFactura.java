package com.example.proyectofinalpoo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "DETALLE_FACTURA",
        foreignKeys = {
                @ForeignKey(entity = Factura.class, parentColumns = "id_Factura", childColumns = "id_Factura"),
                @ForeignKey(entity = Producto.class, parentColumns = "id_Producto", childColumns = "id_Producto")
        },
        indices = {@Index("id_Factura"), @Index("id_Producto")})
public class DetalleFactura {
    @PrimaryKey(autoGenerate = true)
    public int id_Detalle;
    @ColumnInfo(name = "id_Factura")
    public int id_Factura;
    @ColumnInfo(name = "id_Producto")
    public int id_Producto;

    @ColumnInfo(name = "det_Cantidad")
    public int cantidad;

    @ColumnInfo(name = "det_PrecioUnitario")
    public double precioUnitario; // Precio al momento de la venta

    @ColumnInfo(name = "det_Subtotal")
    public double subtotal;

    public DetalleFactura(int id_Factura, int id_Producto, int cantidad, double precioUnitario, double subtotal) {
        this.id_Factura = id_Factura;
        this.id_Producto = id_Producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }
    public DetalleFactura() {

    }
}