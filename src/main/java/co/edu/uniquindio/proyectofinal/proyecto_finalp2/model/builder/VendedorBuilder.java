package co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.builder;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.Vendedor;

public class VendedorBuilder {
    private String nombre;
    private String apellidos;
    private String cedula;
    private String direccion;
    private String usuario;
    private String contrasena;

    public VendedorBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public VendedorBuilder conApellidos(String apellidos) {
        this.apellidos = apellidos;
        return this;
    }

    public VendedorBuilder conCedula(String cedula) {
        this.cedula = cedula;
        return this;
    }

    public VendedorBuilder conDireccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public VendedorBuilder conUsuario(String usuario) {
        this.usuario = usuario;
        return this;
    }

    public VendedorBuilder conContrasena(String contrasena) {
        this.contrasena = contrasena;
        return this;
    }

    public Vendedor build() {
        return new Vendedor(nombre, apellidos, cedula, direccion, usuario, contrasena);
    }
}