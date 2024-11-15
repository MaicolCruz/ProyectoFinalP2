package co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.impl;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.EstadoProducto;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces.IVendedorCrud;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VendedorCrudImpl implements IVendedorCrud {
    private final Vendedor vendedorActual;
    private final MarketPlace marketPlace;

    public VendedorCrudImpl(Vendedor vendedor) {
        this.vendedorActual = vendedor;
        this.marketPlace = MarketPlace.getInstance();
    }

    @Override
    public void publicarProducto(Producto producto) {
        if (!validarProducto(producto)) {
            throw new IllegalArgumentException("El producto no cumple con los requisitos mínimos");
        }

        if (!producto.getVendedor().equals(vendedorActual)) {
            throw new IllegalStateException("No tienes permiso para publicar productos de otro vendedor");
        }

        producto.setFechaPublicacion(LocalDateTime.now());
        producto.setEstado(EstadoProducto.PUBLICADO);
        vendedorActual.agregarProducto(producto);
        marketPlace.agregarProducto(producto);
    }

    @Override
    public void actualizarEstadoProducto(Producto producto, EstadoProducto estado) {
        if (!vendedorActual.getProductos().contains(producto)) {
            throw new IllegalStateException("El producto no pertenece a este vendedor");
        }

        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        // Validaciones específicas por estado
        switch (estado) {
            case VENDIDO:
                if (producto.getEstado() != EstadoProducto.PUBLICADO) {
                    throw new IllegalStateException("Solo se pueden vender productos publicados");
                }
                break;
            case CANCELADO:
                if (producto.getEstado() == EstadoProducto.VENDIDO) {
                    throw new IllegalStateException("No se pueden cancelar productos ya vendidos");
                }
                break;
        }

        producto.setEstado(estado);
    }

    @Override
    public List<Producto> listarMisProductos() {
        return new ArrayList<>(vendedorActual.getProductos());
    }

    @Override
    public List<Vendedor> listarMisContactos() {
        return new ArrayList<>(vendedorActual.getContactos());
    }

    @Override
    public void agregarContacto(Vendedor contacto) {
        if (contacto == null) {
            throw new IllegalArgumentException("El contacto no puede ser nulo");
        }

        if (contacto.equals(vendedorActual)) {
            throw new IllegalArgumentException("No puedes agregarte a ti mismo como contacto");
        }

        if (vendedorActual.getContactos().contains(contacto)) {
            throw new IllegalStateException("El contacto ya existe en tu lista");
        }

        if (vendedorActual.getContactos().size() >= 10) {
            throw new IllegalStateException("Has alcanzado el límite máximo de contactos (10)");
        }

        vendedorActual.agregarContacto(contacto);
        contacto.agregarContacto(vendedorActual); // Relación bidireccional
    }

    @Override
    public List<String> obtenerMuroMensajes() {
        return vendedorActual.getMuroMensajes().stream()
                .sorted((m1, m2) -> m2.compareTo(m1)) // Ordenar por más recientes primero
                .collect(Collectors.toList());
    }

    @Override
    public void agregarMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }

        String mensajeFormateado = String.format("[%s] %s: %s",
                LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                vendedorActual.getNombre(),
                mensaje.trim());

        vendedorActual.agregarMensajeMuro(mensajeFormateado);
    }

    @Override
    public void agregarComentario(Producto producto, String contenido) {
        if (!esProductoAccesible(producto)) {
            throw new IllegalStateException("No tienes permiso para comentar este producto");
        }

        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío");
        }

        Comentario comentario = new Comentario();
        comentario.setContenido(contenido.trim());
        comentario.setAutor(vendedorActual);
        comentario.setProducto(producto);
        comentario.setFecha(LocalDateTime.now());

        producto.getComentarios().add(comentario);
        vendedorActual.getComentarios().add(comentario);

        // Notificar al vendedor del producto
        String notificacion = String.format("[%s] Nuevo comentario de %s en tu producto %s",
                LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                vendedorActual.getNombre(),
                producto.getNombre());
        producto.getVendedor().getMuroMensajes().add(notificacion);
    }

    @Override
    public void agregarMeGusta(Producto producto) {
        if (!esProductoAccesible(producto)) {
            throw new IllegalStateException("No tienes permiso para dar me gusta a este producto");
        }

        // Verificar si ya dio me gusta
        boolean yaExiste = producto.getMeGusta().stream()
                .anyMatch(mg -> mg.getVendedor().equals(vendedorActual));

        if (yaExiste) {
            throw new IllegalStateException("Ya has dado me gusta a este producto");
        }

        MeGusta meGusta = new MeGusta();
        meGusta.setVendedor(vendedorActual);
        meGusta.setProducto(producto);
        meGusta.setFecha(LocalDateTime.now());

        producto.getMeGusta().add(meGusta);

        // Notificar al vendedor del producto
        String notificacion = String.format("[%s] A %s le gusta tu producto %s",
                LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                vendedorActual.getNombre(),
                producto.getNombre());
        producto.getVendedor().getMuroMensajes().add(notificacion);
    }

    @Override
    public void eliminarProducto(String nombre) {
        Producto producto = vendedorActual.getProductos().stream()
                .filter(p -> p.getNombre().equals(nombre))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (producto.getEstado() == EstadoProducto.VENDIDO) {
            throw new IllegalStateException("No se puede eliminar un producto vendido");
        }

        vendedorActual.eliminarProducto(producto);
        marketPlace.eliminarProducto(String.valueOf(producto));
    }

    @Override
    public Vendedor getVendedorActual() {
        return vendedorActual;
    }

    @Override
    public List<Producto> obtenerProductosRed() {
        List<Producto> productosRed = new ArrayList<>();

        // Agregar productos propios
        productosRed.addAll(vendedorActual.getProductos());

        // Agregar productos de los contactos
        for (Vendedor contacto : vendedorActual.getContactos()) {
            productosRed.addAll(contacto.getProductos());
        }

        return productosRed;
    }

    private boolean validarProducto(Producto producto) {
        return producto != null &&
                producto.getNombre() != null && !producto.getNombre().trim().isEmpty() &&
                producto.getCategoria() != null && !producto.getCategoria().trim().isEmpty() &&
                producto.getPrecio() > 0;
    }

    private boolean esProductoAccesible(Producto producto) {
        return producto.getVendedor().equals(vendedorActual) ||
                vendedorActual.getContactos().contains(producto.getVendedor());
    }


}