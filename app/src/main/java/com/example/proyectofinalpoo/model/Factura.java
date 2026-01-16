package com.example.proyectofinalpoo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "FACTURAS")
public class Factura {
    @PrimaryKey(autoGenerate = true)
    public int id_Factura;

    @ColumnInfo(name = "fac_Fecha")
    public String fecha; // Guardaremos como texto "dd/MM/yyyy"

    @ColumnInfo(name = "fac_Total")
    public double total;

    @ColumnInfo(name = "fac_Cliente")
    public String nombreCliente; // Por simplicidad guardamos el nombre o ID

    public Factura(String fecha, double total, String nombreCliente) {
        this.fecha = fecha;
        this.total = total;
        this.nombreCliente = nombreCliente;
    }
}