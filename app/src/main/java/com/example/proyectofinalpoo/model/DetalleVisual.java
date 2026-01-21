package com.example.proyectofinalpoo.model;

// POJO (Plain Old Java Object) para mostrar los detalles de la factura de forma amigable.
// Room se encarga de llenar esta clase con el resultado del JOIN.
public class DetalleVisual {
    public String nombreProducto;
    public int cantidad;
    public double precioUnitario;
    public double subtotal;
}
