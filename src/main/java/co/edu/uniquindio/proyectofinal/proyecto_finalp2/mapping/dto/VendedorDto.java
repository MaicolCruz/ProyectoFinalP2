package co.edu.uniquindio.proyectofinal.proyecto_finalp2.mapping.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VendedorDto {
    private String nombre;
    private String apellidos;
    private String cedula;
    private String direccion;
    private String usuario;
    private String contrasena;
    private List<ProductoDto> productos;
    private List<VendedorDto> contactos;
    private List<String> muroMensajes;
    private List<ComentarioDto> comentarios;

    public VendedorDto() {
        this.productos = new ArrayList<>();
        this.contactos = new ArrayList<>();
        this.muroMensajes = new ArrayList<>();
        this.comentarios = new ArrayList<>();
    }

    public VendedorDto(String nombre, String apellidos, String cedula,
                       String direccion, String usuario, String contrasena) {
        this();
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.direccion = direccion;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public List<ProductoDto> getProductos() { return new ArrayList<>(productos); }
    public void setProductos(List<ProductoDto> productos) {
        this.productos = new ArrayList<>(productos);
    }

    public List<VendedorDto> getContactos() { return new ArrayList<>(contactos); }
    public void setContactos(List<VendedorDto> contactos) {
        this.contactos = new ArrayList<>(contactos);
    }

    public List<String> getMuroMensajes() { return new ArrayList<>(muroMensajes); }
    public void setMuroMensajes(List<String> muroMensajes) {
        this.muroMensajes = new ArrayList<>(muroMensajes);
    }

    public List<ComentarioDto> getComentarios() { return new ArrayList<>(comentarios); }
    public void setComentarios(List<ComentarioDto> comentarios) {
        this.comentarios = new ArrayList<>(comentarios);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendedorDto that = (VendedorDto) o;
        return Objects.equals(cedula, that.cedula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cedula);
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " (CC: " + cedula + ")";
    }
}