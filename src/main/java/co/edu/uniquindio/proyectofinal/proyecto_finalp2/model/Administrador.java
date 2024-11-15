package co.edu.uniquindio.proyectofinal.proyecto_finalp2.model;

public class Administrador extends Persona {

    public Administrador() {
        super();
    }

    public Administrador(String nombre, String apellidos, String cedula,
                         String direccion, String usuario, String contrasena) {
        super(nombre, apellidos, cedula, direccion, usuario, contrasena);
    }

    public boolean verificarCredenciales(String usuario, String contrasena) {
        return this.usuario.equals(usuario) && this.contrasena.equals(contrasena);
    }

    @Override
    public String toString() {
        return "Administrador: " + super.toString();
    }
}
