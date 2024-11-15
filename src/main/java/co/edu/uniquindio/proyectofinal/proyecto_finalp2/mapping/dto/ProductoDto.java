package co.edu.uniquindio.proyectofinal.proyecto_finalp2.mapping.dto;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.EstadoProducto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductoDto {
    private String nombre;
    private String imagen;
    private String categoria;
    private double precio;
    private EstadoProducto estado;
    private LocalDateTime fechaPublicacion;
    private String vendedorCedula;
    private List<ComentarioDto> comentarios;
    private List<MeGustaDto> meGusta;

    public ProductoDto() {
        this.comentarios = new ArrayList<>();
        this.meGusta = new ArrayList<>();
        this.fechaPublicacion = LocalDateTime.now();
        this.estado = EstadoProducto.PUBLICADO;
    }

    public ProductoDto(String nombre, String imagen, String categoria, double precio,
                       String vendedorCedula) {
        this();
        this.nombre = nombre;
        this.imagen = imagen;
        this.categoria = categoria;
        this.precio = precio;
        this.vendedorCedula = vendedorCedula;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public EstadoProducto getEstado() { return estado; }
    public void setEstado(EstadoProducto estado) { this.estado = estado; }

    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public String getVendedorCedula() { return vendedorCedula; }
    public void setVendedorCedula(String vendedorCedula) { this.vendedorCedula = vendedorCedula; }

    public List<ComentarioDto> getComentarios() { return new ArrayList<>(comentarios); }
    public void setComentarios(List<ComentarioDto> comentarios) { this.comentarios = new ArrayList<>(comentarios); }

    public List<MeGustaDto> getMeGusta() { return new ArrayList<>(meGusta); }
    public void setMeGusta(List<MeGustaDto> meGusta) { this.meGusta = new ArrayList<>(meGusta); }

    public void addComentario(ComentarioDto comentario) {
        if (comentario != null) {
            this.comentarios.add(comentario);
        }
    }

    public void addMeGusta(MeGustaDto meGusta) {
        if (meGusta != null) {
            this.meGusta.add(meGusta);
        }
    }

    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() &&
                categoria != null && !categoria.trim().isEmpty() &&
                precio > 0 &&
                vendedorCedula != null && !vendedorCedula.trim().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoDto that = (ProductoDto) o;
        return Objects.equals(nombre, that.nombre) &&
                Objects.equals(vendedorCedula, that.vendedorCedula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, vendedorCedula);
    }

    @Override
    public String toString() {
        return "ProductoDto{" +
                "nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", precio=" + precio +
                ", estado=" + estado +
                ", fechaPublicacion=" + fechaPublicacion +
                ", vendedorCedula='" + vendedorCedula + '\'' +
                ", comentarios=" + comentarios.size() +
                ", meGusta=" + meGusta.size() +
                '}';
    }
}