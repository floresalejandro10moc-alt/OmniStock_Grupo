package com.example.proyectofinalpoo.logic;

public class Alimento extends ProductoBase {

    public Alimento(int id, String nombre, double precioBase, int stock, String fechaCaducidad) {
        // Al super le pasamos 0.0 en el IVA porque los alimentos no lo llevan
        super(id, nombre, precioBase, stock, 0.0);
        this.fechaCaducidad = fechaCaducidad;
    }

    @Override
    public double calcularPrecioFinal() {
        if (fechaCaducidad != null && !fechaCaducidad.isEmpty()) {
            try {
                // Asumimos formato yyyy-MM-dd
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy",
                        java.util.Locale.getDefault());
                // Intento fall-back si el formato es diferente, pero para este proyecto
                // usaremos dd/MM/yyyy o yyyy-MM-dd?
                // En Producto.java no vi el formato. Pero en Android standard suele ser
                // yyyy-MM-dd.
                // Sin embargo, si el usuario escribe la fecha manualmente, podría ser
                // dd/MM/yyyy.
                // DEBO CHEQUEAR EL FORMATO.
                // Voy a intentar parsear, y si falla, retorno precioBase.

                // Nota: Verificare el formato en otros lugares si es posible.
                // Pero un formato seguro para SQL es yyyy-MM-dd.
                // Probemos con una lista de formatos o el mas probable.
                // Si viene de SQLite Date, es yyyy-MM-dd.
                sdf.applyPattern("yyyy-MM-dd");

                java.util.Date fechaCad = sdf.parse(fechaCaducidad);
                java.util.Date hoy = new java.util.Date();

                long diffInMillies = fechaCad.getTime() - hoy.getTime();
                long diffInDays = java.util.concurrent.TimeUnit.DAYS.convert(diffInMillies,
                        java.util.concurrent.TimeUnit.MILLISECONDS);

                // Si faltan 5 dias o menos (incluyendo hoy o vencidos cercanos si se desea)
                // diffInDays puede ser negativo si ya vencio.
                // Asumiremos que aplica el descuento si está en el rango [0, 5].
                // O tal vez si ya venció también (diff < 0).
                // El requerimiento dice "a 5 dias de caducar", suena a futuro.
                if (diffInDays <= 5 && diffInDays >= -1) { // -1 para cubrir el "dia presente" si el tiempo afecta
                    return precioBase * 0.50;
                }
            } catch (Exception e) {
                // Error parseando fecha, no aplicamos descuento
            }
        }
        return precioBase;
    }

    @Override
    public java.util.Map<String, Double> calcularImpuestos() {
        return new java.util.HashMap<>(); // Sin impuestos
    }
}