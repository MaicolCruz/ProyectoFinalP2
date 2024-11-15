package co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.factory;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.builder.ProductoBuilder;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.builder.VendedorBuilder;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.EstadoProducto;
import java.time.LocalDateTime;

public class ModelFactory {
    private static ModelFactory instance;

    private ModelFactory() {}

    public static ModelFactory getInstance() {
        if (instance == null) {
            instance = new ModelFactory();
        }
        return instance;
    }

    public Administrador crearAdministrador(String nombre, String apellidos, String cedula,
                                            String direccion, String usuario, String contrasena) {
        return new Administrador(nombre, apellidos, cedula, direccion, usuario, contrasena);
    }

    public Vendedor crearVendedor(String nombre, String apellidos, String cedula,
                                  String direccion, String usuario, String contrasena) {
        return new VendedorBuilder()
                .conNombre(nombre)
                .conApellidos(apellidos)
                .conCedula(cedula)
                .conDireccion(direccion)
                .conUsuario(usuario)
                .conContrasena(contrasena)
                .build();
    }

    public Producto crearProducto(String nombre, String imagen, String categoria,
                                  double precio, Vendedor vendedor) {
        return new ProductoBuilder()
                .conNombre(nombre)
                .conImagen(imagen)
                .conCategoria(categoria)
                .conPrecio(precio)
                .conVendedor(vendedor)
                .conEstado(EstadoProducto.PUBLICADO)
                .build();
    }

    public Comentario crearComentario(String contenido, Vendedor autor, Producto producto) {
        Comentario comentario = new Comentario();
        comentario.setContenido(contenido);
        comentario.setAutor(autor);
        comentario.setProducto(producto);
        comentario.setFecha(LocalDateTime.now());
        return comentario;
    }

    public MeGusta crearMeGusta(Vendedor vendedor, Producto producto) {
        MeGusta meGusta = new MeGusta();
        meGusta.setVendedor(vendedor);
        meGusta.setProducto(producto);
        meGusta.setFecha(LocalDateTime.now());
        return meGusta;
    }
}
