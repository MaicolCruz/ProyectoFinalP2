package co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.impl;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces.IProductoCrud;
import java.util.List;
import java.util.stream.Collectors;

public class ProductoCrudImpl implements IProductoCrud {
    private final MarketPlace marketPlace;

    public ProductoCrudImpl() {
        this.marketPlace = MarketPlace.getInstance();
    }
    @Override
    public void agregarProducto(Producto producto) {
        if (!validarProducto(producto)) {
            throw new IllegalArgumentException("Datos del producto inválidos");
        }

        if (existeProductoConNombre(producto.getNombre())) {
            throw new IllegalStateException("Ya existe un producto con ese nombre");
        }

        // Agregar al marketplace y al vendedor
        marketPlace.agregarProducto(producto);
        producto.getVendedor().getProductos().add(producto);

        System.out.println("Producto agregado: " + producto.getNombre()); // Debug
    }

    @Override
    public void eliminarProducto(String nombre) {
        Producto producto = buscarProducto(nombre);
        if (producto != null) {
            marketPlace.eliminarProducto(String.valueOf(producto));
            producto.getVendedor().getProductos().remove(producto);
        }
    }

    @Override
    public void actualizarProducto(Producto productoActualizado) {
        if (!validarProducto(productoActualizado)) {
            throw new IllegalArgumentException("Datos del producto inválidos");
        }

        Producto productoExistente = buscarProducto(productoActualizado.getNombre());
        if (productoExistente == null) {
            throw new IllegalStateException("El producto no existe");
        }

        if (!productoExistente.getVendedor().equals(productoActualizado.getVendedor())) {
            throw new IllegalStateException("No tienes permiso para actualizar este producto");
        }

        // Actualizar los datos del producto existente
        productoExistente.setCategoria(productoActualizado.getCategoria());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setImagen(productoActualizado.getImagen());
        productoExistente.setEstado(productoActualizado.getEstado());

        System.out.println("Producto actualizado: " + productoExistente.getNombre()); // Debug
    }

    @Override
    public Producto buscarProducto(String nombre) {
        return marketPlace.getProductos().stream()
                .filter(p -> p.getNombre().equals(nombre))
                .findFirst()
                .orElse(null);
    }


    @Override
    public List<Producto> listarProductos() {
        return marketPlace.getProductos().stream()
                .sorted((p1, p2) -> p2.getFechaPublicacion().compareTo(p1.getFechaPublicacion()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía");
        }

        return marketPlace.getProductos().stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria.trim()))
                .sorted((p1, p2) -> p2.getFechaPublicacion().compareTo(p1.getFechaPublicacion()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> buscarPorVendedor(String cedulaVendedor) {
        if (cedulaVendedor == null || cedulaVendedor.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del vendedor no puede estar vacía");
        }

        return marketPlace.getProductos().stream()
                .filter(p -> p.getVendedor().getCedula().equals(cedulaVendedor))
                .sorted((p1, p2) -> p2.getFechaPublicacion().compareTo(p1.getFechaPublicacion()))
                .collect(Collectors.toList());
    }

    private boolean validarProducto(Producto producto) {
        return producto != null &&
                producto.getNombre() != null && !producto.getNombre().trim().isEmpty() &&
                producto.getCategoria() != null && !producto.getCategoria().trim().isEmpty() &&
                producto.getPrecio() > 0 &&
                producto.getVendedor() != null;
    }

    private boolean existeProductoConNombre(String nombre) {
        return marketPlace.getProductos().stream()
                .anyMatch(p -> p.getNombre().equals(nombre));
    }
}