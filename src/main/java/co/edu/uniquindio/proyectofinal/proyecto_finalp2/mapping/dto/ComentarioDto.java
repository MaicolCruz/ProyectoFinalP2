package co.edu.uniquindio.proyectofinal.proyecto_finalp2.mapping.dto;

import java.time.LocalDateTime;

public class ComentarioDto {
    private String contenido;
    private LocalDateTime fecha;
    private String autorCedula;
    private String productoNombre;

    public ComentarioDto() {
        this.fecha = LocalDateTime.now();
    }

    public ComentarioDto(String contenido, String autorCedula, String productoNombre) {
        this();
        this.contenido = contenido;
        this.autorCedula = autorCedula;
        this.productoNombre = productoNombre;
    }

    // Getters y Setters
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getAutorCedula() { return autorCedula; }
    public void setAutorCedula(String autorCedula) { this.autorCedula = autorCedula; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public boolean esValido() {
        return contenido != null && !contenido.trim().isEmpty() &&
                autorCedula != null && !autorCedula.trim().isEmpty() &&
                productoNombre != null && !productoNombre.trim().isEmpty();
    }
}
