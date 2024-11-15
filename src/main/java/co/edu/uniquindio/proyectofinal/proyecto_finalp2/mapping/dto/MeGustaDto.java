package co.edu.uniquindio.proyectofinal.proyecto_finalp2.mapping.dto;


import java.time.LocalDateTime;

public class MeGustaDto {
    private String vendedorCedula;
    private LocalDateTime fecha;
    private String productoNombre;

    public MeGustaDto() {
        this.fecha = LocalDateTime.now();
    }

    public MeGustaDto(String vendedorCedula, String productoNombre) {
        this();
        this.vendedorCedula = vendedorCedula;
        this.productoNombre = productoNombre;
    }

    // Getters y Setters
    public String getVendedorCedula() { return vendedorCedula; }
    public void setVendedorCedula(String vendedorCedula) { this.vendedorCedula = vendedorCedula; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public boolean esValido() {
        return vendedorCedula != null && !vendedorCedula.trim().isEmpty() &&
                productoNombre != null && !productoNombre.trim().isEmpty();
    }
}