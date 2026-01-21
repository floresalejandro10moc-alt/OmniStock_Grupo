package com.example.proyectofinalpoo.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "FACTURAS",
        foreignKeys = @ForeignKey(
                entity = Cliente.class,
                parentColumns = "id_Cliente",
                childColumns = "id_Cliente",
                onDelete = ForeignKey.SET_NULL, // Si se borra el cliente, la factura no se borra
                onUpdate = ForeignKey.CASCADE
        ))
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

    // Constructor vacío para Room
    public Factura() {}

    // Constructor actualizado con el ID del cliente
    public Factura(String fecha, Integer id_cliente, double total, String nombreCliente, String cedula, String direccion, String celular) {
        this.fecha = fecha;
        this.id_cliente = id_cliente;
        this.total = total;
        this.nombreCliente = nombreCliente;
        this.cedula = cedula;
        this.direccion = direccion;
        this.celular = celular;
    }
}