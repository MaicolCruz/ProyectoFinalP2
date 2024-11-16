package co.edu.uniquindio.proyectofinal.proyecto_finalp2.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vendedor extends Persona {
    private List<Producto> productos;
    private List<Vendedor> contactos;
    private List<String> muroMensajes;
    private List<Comentario> comentarios;
    private int maxContactos = 10;

    public Vendedor() {
        super();
        inicializarListas();
    }

    public Vendedor(String nombre, String apellidos, String cedula,
                    String direccion, String usuario, String contrasena) {
        super(nombre, apellidos, cedula, direccion, usuario, contrasena);
        inicializarListas();
    }

    private void inicializarListas() {
        this.productos = new ArrayList<>();
        this.contactos = new ArrayList<>();
        this.muroMensajes = new ArrayList<>();
        this.comentarios = new ArrayList<>();
    }

    // Métodos de negocio
    public boolean agregarContacto(Vendedor vendedor) {
        if (contactos.size() >= maxContactos) {
            return false;
        }
        if (!contactos.contains(vendedor)) {
            contactos.add(vendedor);
            vendedor.getContactos().add(this);
            return true;
        }
        return false;
    }

    public void publicarProducto(Producto producto) {
        if (producto != null && !productos.contains(producto)) {
            productos.add(producto);
            producto.setVendedor(this);
        }
    }

    public void agregarMensajeMuro(String mensaje) {
        if (mensaje != null && !mensaje.trim().isEmpty()) {
            muroMensajes.add(mensaje);
        }
    }

    // Getters y setters con copias defensivas
    public List<Producto> getProductos() {
        return new ArrayList<>(productos);
    }

    public void setProductos(List<Producto> productos) {
        this.productos = new ArrayList<>(productos);
    }

    public List<Vendedor> getContactos() {
        return new ArrayList<>(contactos);
    }

    public void setContactos(List<Vendedor> contactos) {
        this.contactos = new ArrayList<>(contactos);
    }

    public List<String> getMuroMensajes() {
        return new ArrayList<>(muroMensajes);
    }

    public void setMuroMensajes(List<String> muroMensajes) {
        this.muroMensajes = new ArrayList<>(muroMensajes);
    }

    public List<Comentario> getComentarios() {
        return new ArrayList<>(comentarios);
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = new ArrayList<>(comentarios);
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos;
    }

    public void agregarComentario(Producto producto, String contenido) {
        if (producto == null || contenido == null || contenido.trim().isEmpty()) {
            return;
        }

        if (!this.getContactos().contains(producto.getVendedor()) &&
                !this.equals(producto.getVendedor())) {
            throw new IllegalStateException("No puedes comentar productos de vendedores que no son tus contactos");
        }

        Comentario comentario = new Comentario();
        comentario.setContenido(contenido.trim());
        comentario.setAutor(this);
        comentario.setProducto(producto);
        comentario.setFecha(LocalDateTime.now());

        producto.getComentarios().add(comentario);
        this.comentarios.add(comentario);
    }

    public void agregarMeGusta(Producto producto) {
        if (producto == null) {
            return;
        }
        if (!this.getContactos().contains(producto.getVendedor()) &&
                !this.equals(producto.getVendedor())) {
            throw new IllegalStateException("No puedes dar me gusta a productos de vendedores que no son tus contactos");
        }

        boolean yaExiste = producto.getMeGusta().stream()
                .anyMatch(mg -> mg.getVendedor().equals(this));

        if (!yaExiste) {
            MeGusta meGusta = new MeGusta();
            meGusta.setVendedor(this);
            meGusta.setProducto(producto);
            meGusta.setFecha(LocalDateTime.now());
            producto.agregarMeGusta(meGusta);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vendedor vendedor = (Vendedor) o;
        return Objects.equals(cedula, vendedor.cedula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cedula);
    }

    public void agregarMensaje(String mensaje) {
        if (mensaje != null && !mensaje.trim().isEmpty()) {
            String mensajeFormateado = String.format("[%s] %s: %s",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    this.nombre,
                    mensaje.trim()
            );
            muroMensajes.add(mensajeFormateado);
        }
    }

    public void agregarProducto(Producto producto) {
        if (producto != null ) {
            productos.add(producto);
        }
    }

    public void eliminarProducto(Producto producto) {
        if (producto != null ) {
            productos.remove(producto);
        }
    }
}