package co.edu.uniquindio.proyectofinal.proyecto_finalp2.service;

public enum EstadoProducto {
    VENDIDO("Vendido"),
    PUBLICADO("Publicado"),
    CANCELADO("Cancelado");

    private final String descripcion;

    EstadoProducto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
