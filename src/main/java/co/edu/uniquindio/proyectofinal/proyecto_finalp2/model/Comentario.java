package co.edu.uniquindio.proyectofinal.proyecto_finalp2.model;

import java.time.LocalDateTime;

public class Comentario {
    private String contenido;
    private LocalDateTime fecha;
    private Vendedor autor;
    private Producto producto;

    public Comentario() {
        this.fecha = LocalDateTime.now();
    }

    public Comentario(String contenido, Vendedor autor, Producto producto) {
        this();
        this.contenido = contenido;
        this.autor = autor;
        this.producto = producto;
    }

    // Getters y Setters
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Vendedor getAutor() { return autor; }
    public void setAutor(Vendedor autor) { this.autor = autor; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    @Override
    public String toString() {
        return String.format("%s: %s (%s)",
                autor.getNombre(),
                contenido,
                fecha.toString()
        );
    }
}