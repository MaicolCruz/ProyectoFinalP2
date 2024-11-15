package co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.EstadoProducto;
import java.util.List;

public interface IVendedorCrud {
    void publicarProducto(Producto producto);
    void actualizarEstadoProducto(Producto producto, EstadoProducto estado);
    List<Producto> listarMisProductos();
    List<Vendedor> listarMisContactos();
    void agregarContacto(Vendedor contacto);
    List<String> obtenerMuroMensajes();
    void agregarMensaje(String mensaje);
    void agregarComentario(Producto producto, String comentario);
    void agregarMeGusta(Producto producto);
    void eliminarProducto(String nombre);
    Vendedor getVendedorActual();
    List<Producto> obtenerProductosRed();
}