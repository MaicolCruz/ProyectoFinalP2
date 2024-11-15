package co.edu.uniquindio.proyectofinal.proyecto_finalp2.model;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.EstadoProducto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Producto {
    private String nombre;
    private String imagen;
    private String categoria;
    private double precio;
    private EstadoProducto estado;
    private LocalDateTime fechaPublicacion;
    private Vendedor vendedor;
    private List<Comentario> comentarios;
    private List<MeGusta> meGusta;

    public Producto() {
        this.comentarios = new ArrayList<>();
        this.meGusta = new ArrayList<>();
        this.fechaPublicacion = LocalDateTime.now();
        this.estado = EstadoProducto.PUBLICADO;
    }

    public Producto(String nombre, String imagen, String categoria, double precio, Vendedor vendedor) {
        this();
        this.nombre = nombre;
        this.imagen = imagen;
        this.categoria = categoria;
        this.precio = precio;
        this.vendedor = vendedor;
    }

    // Métodos de negocio
    public void agregarComentario(Comentario comentario) {
        if (comentario != null) {
            comentarios.add(comentario);
        }
    }

    public void agregarMeGusta(MeGusta meGusta) {
        if (meGusta != null && !this.meGusta.contains(meGusta)) {
            this.meGusta.add(meGusta);
        }
    }

    public boolean tieneInteraccionDe(Vendedor vendedor) {
        return comentarios.stream().anyMatch(c -> c.getAutor().equals(vendedor)) ||
                meGusta.stream().anyMatch(m -> m.getVendedor().equals(vendedor));
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
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Vendedor getVendedor() { return vendedor; }
    public void setVendedor(Vendedor vendedor) { this.vendedor = vendedor; }

    public List<Comentario> getComentarios() { return new ArrayList<>(comentarios); }
    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = new ArrayList<>(comentarios);
    }

    public List<MeGusta> getMeGusta() { return new ArrayList<>(meGusta); }
    public void setMeGusta(List<MeGusta> meGusta) {
        this.meGusta = new ArrayList<>(meGusta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(nombre, producto.nombre) &&
                Objects.equals(vendedor, producto.vendedor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, vendedor);
    }


    @Override
    public String toString() {
        return nombre + " - " + categoria + " ($" + precio + ")";
    }
}
