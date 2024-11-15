package co.edu.uniquindio.proyectofinal.proyecto_finalp2.model;

public abstract class Persona {
    protected String nombre;
    protected String apellidos;
    protected String cedula;
    protected String direccion;
    protected String usuario;
    protected String contrasena;

    public Persona() {
    }

    public Persona(String nombre, String apellidos, String cedula, String direccion, String usuario, String contrasena) {
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

    @Override
    public String toString() {
        return nombre + " " + apellidos + " (CC: " + cedula + ")";
    }
}