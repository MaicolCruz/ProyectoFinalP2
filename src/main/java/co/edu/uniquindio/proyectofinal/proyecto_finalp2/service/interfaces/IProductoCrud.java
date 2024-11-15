package co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.Producto;
import java.util.List;

public interface IProductoCrud {
    void agregarProducto(Producto producto);
    void eliminarProducto(String nombre);
    void actualizarProducto(Producto producto);
    Producto buscarProducto(String nombre);
    List<Producto> listarProductos();
    List<Producto> buscarPorCategoria(String categoria);
    List<Producto> buscarPorVendedor(String cedulaVendedor);
}