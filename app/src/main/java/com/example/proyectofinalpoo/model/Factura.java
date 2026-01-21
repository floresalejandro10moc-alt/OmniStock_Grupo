package com.example.proyectofinalpoo.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "FACTURAS", indices = {@Index(value = "id_Cliente")}, foreignKeys = @ForeignKey(entity = Cliente.class, parentColumns = "id_Cliente", childColumns = "id_Cliente", onDelete = ForeignKey.SET_NULL, // Si
                                                                                                                                                                             // se
                                                                                                                                                                             // borra
                                                                                                                                                                             // el
                                                                                                                                                                             // cliente,
                                                                                                                                                                             // la
                                                                                                                                                                             // factura
                                                                                                                                                                             // no
                                                                                                                                                                             // se
                                                                                                                                                                             // borra
        onUpdate = ForeignKey.CASCADE))
public class Factura {
    @PrimaryKey(autoGenerate = true)
    public int id_Factura;

    @ColumnInfo(name = "id_Cliente")
    public Integer id_cliente; // Usamos Integer para permitir null (Consumidor Final)

    @ColumnInfo(name = "fac_Fecha")
    public String fecha;

    @ColumnInfo(name = "fac_Total")
    public double total;

    @ColumnInfo(name = "fac_NombreCompleto")
    public String nombreCliente;

    @ColumnInfo(name = "fac_Cedula")
    public String cedula;

    @ColumnInfo(name = "fac_Direccion")
    public String direccion;

    @ColumnInfo(name = "fac_Celular")
    public String celular;

    // NUEVOS CAMPOS PARA DESGLOSE
    @ColumnInfo(name = "fac_SubtotalBase")
    public double subtotalBase;

    @ColumnInfo(name = "fac_DesgloseImpuestos")
    public String desgloseImpuestos; // Formato: "Nombre:Monto|Nombre:Monto"

    // Constructor vacío para Room
    public Factura() {
    }

    // Constructor actualizado
    @Ignore
    public Factura(String fecha, Integer id_cliente, double total, String nombreCliente, String cedula,
            String direccion, String celular, double subtotalBase, String desgloseImpuestos) {
        this.fecha = fecha;
        this.id_cliente = id_cliente;
        this.total = total;
        this.nombreCliente = nombreCliente;
        this.cedula = cedula;
        this.direccion = direccion;
        this.celular = celular;
        this.subtotalBase = subtotalBase;
        this.desgloseImpuestos = desgloseImpuestos;
    }
}