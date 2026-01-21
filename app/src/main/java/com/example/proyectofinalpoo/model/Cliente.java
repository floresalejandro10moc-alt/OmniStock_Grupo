package com.example.proyectofinalpoo.model;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;

@Entity(tableName = "CLIENTE",
        foreignKeys = @ForeignKey(entity = Usuario.class,
                parentColumns = "id_Usuario",
                childColumns = "id_Usuario",
                onDelete = ForeignKey.SET_NULL,
                onUpdate = ForeignKey.CASCADE),
        indices = {@Index(value = "cli_Cedula", unique = true), @Index(value = "id_Usuario")})
public class Cliente {
    @PrimaryKey(autoGenerate = true)
    public int id_Cliente;

    public Integer id_Usuario; // Puede ser Null

    @ColumnInfo(name = "cli_Nombre")
    public String nombre;

    @ColumnInfo(name = "cli_Apellido")
    public String apellido;

    @ColumnInfo(name = "cli_Cedula")
    public String cedula;

    @ColumnInfo(name = "cli_Direccion")
    public String direccion;

    @ColumnInfo(name = "cli_Celular")
    public String celular;

    @ColumnInfo(name = "cli_Estado", defaultValue = "ACT")
    public String estado;

    public Cliente() {}

    @Ignore
    public Cliente(String nombre, String apellido, String cedula, String direccion, String celular) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.direccion = direccion;
        this.celular = celular;
        this.estado = "ACT";
    }
}
