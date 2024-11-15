package co.edu.uniquindio.proyectofinal.proyecto_finalp2.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class MeGusta {
    private Vendedor vendedor;
    private LocalDateTime fecha;
    private Producto producto;

    public MeGusta() {
        this.fecha = LocalDateTime.now();
    }

    public MeGusta(Vendedor vendedor, Producto producto) {
        this();
        this.vendedor = vendedor;
        this.producto = producto;
    }

    // Getters y Setters
    public Vendedor getVendedor() { return vendedor; }
    public void setVendedor(Vendedor vendedor) { this.vendedor = vendedor; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeGusta meGusta = (MeGusta) o;
        return Objects.equals(vendedor, meGusta.vendedor) &&
                Objects.equals(producto, meGusta.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendedor, producto);
    }

    @Override
    public String toString() {
        return String.format("Me gusta de %s en %s",
                vendedor.getNombre(),
                producto.getNombre()
        );
    }
}