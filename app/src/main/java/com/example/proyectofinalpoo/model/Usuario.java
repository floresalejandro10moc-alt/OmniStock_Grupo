package com.example.proyectofinalpoo.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;

@Entity(tableName = "USUARIOS",
        indices = {
                @Index(value = "usu_Alias", unique = true),
                @Index(value = "usu_Correo", unique = true)
        })

public class Usuario {
    // CONSTANTES DE ROLES (Para fácil modificación futura)
    public static final int ROL_VENDEDOR = 0;
    public static final int ROL_ADMIN = 1;
    // Si mañana quieres otro rol, solo agregas: public static final int ROL_SUPERVISOR = 2;
    @PrimaryKey(autoGenerate = true)
    public int id_Usuario;

    @ColumnInfo(name = "usu_Alias")
    public String alias;

    @ColumnInfo(name = "usu_Administrador", defaultValue = "0")
    public int esAdministrador; // 1 o 0

    @ColumnInfo(name = "usu_Clave")
    public String clave;

    @ColumnInfo(name = "usu_Correo")
    public String correo;

    @ColumnInfo(name = "usu_Estado", defaultValue = "ACT")
    public String estado;

    public Usuario() {}

    public Usuario(String alias, int esAdministrador, String clave, String correo) {
        this.alias = alias;
        this.esAdministrador = esAdministrador;
        this.clave = clave;
        this.correo = correo;
        this.estado = "ACT";
    }

    public boolean esAdministrador() {
        return this.esAdministrador == ROL_ADMIN;
    }
}