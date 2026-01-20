package com.example.proyectofinalpoo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "FACTURAS")
public class Factura {
    @PrimaryKey(autoGenerate = true)
    public int id_Factura;

    @ColumnInfo(name = "fac_Fecha")
    public String fecha;

    @ColumnInfo(name = "fac_Total")
    public double total;

    // --- NUEVOS CAMPOS COMPLETOS ---
    @ColumnInfo(name = "fac_NombreCompleto")
    public String nombreCliente;

    @ColumnInfo(name = "fac_Cedula")
    public String cedula;

    @ColumnInfo(name = "fac_Direccion")
    public String direccion;

    @ColumnInfo(name = "fac_Celular")
    public String celular;

    // Constructor VACÍO. Room lo necesita para construir los objetos desde la base de datos.
    public Factura() {}

    // Constructor para nosotros (para crear facturas nuevas al vender).
    public Factura(String fecha, double total, String nombreCliente, String cedula, String direccion, String celular) {
        this.fecha = fecha;
        this.total = total;
        this.nombreCliente = nombreCliente;
        this.cedula = cedula;
        this.direccion = direccion;
        this.celular = celular;
    }
}