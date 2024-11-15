package co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.impl;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces.IMarketPlaceMapping;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.factory.ModelFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MarketPlaceMappingImpl implements IMarketPlaceMapping {
    private final ModelFactory modelFactory;
    private final MarketPlace marketPlace;

    public MarketPlaceMappingImpl() {
        this.modelFactory = ModelFactory.getInstance();
        this.marketPlace = MarketPlace.getInstance();
    }

    @Override
    public void vincularVendedores(Vendedor vendedor1, Vendedor vendedor2) throws Exception {
        if (vendedor1 == null || vendedor2 == null) {
            throw new Exception("Los vendedores no pueden ser nulos");
        }
        if (vendedor1.equals(vendedor2)) {
            throw new Exception("Un vendedor no puede vincularse consigo mismo");
        }
        if (vendedor1.getContactos().size() >= 10) {
            throw new Exception("El vendedor 1 ha alcanzado el límite de contactos");
        }
        if (vendedor2.getContactos().size() >= 10) {
            throw new Exception("El vendedor 2 ha alcanzado el límite de contactos");
        }

        vendedor1.agregarContacto(vendedor2);
    }

    @Override
    public List<Vendedor> obtenerContactosSugeridos(Vendedor vendedor) {
        if (vendedor == null) return new ArrayList<>();

        return vendedor.getContactos().stream()
                .flatMap(contacto -> contacto.getContactos().stream())
                .distinct()
                .filter(sugerido -> !vendedor.getContactos().contains(sugerido) &&
                        !sugerido.equals(vendedor))
                .collect(Collectors.toList());
    }

    @Override
    public void agregarMeGusta(Producto producto, Vendedor vendedor) throws Exception {
        if (producto == null || vendedor == null) {
            throw new Exception("El producto y el vendedor no pueden ser nulos");
        }

        if (!esProductoAccesible(producto, vendedor)) {
            throw new Exception("No tienes permiso para dar me gusta a este producto");
        }

        boolean yaExiste = producto.getMeGusta().stream()
                .anyMatch(mg -> mg.getVendedor().equals(vendedor));

        if (yaExiste) {
            throw new Exception("Ya has dado me gusta a este producto");
        }

        MeGusta meGusta = modelFactory.crearMeGusta(vendedor, producto);
        producto.getMeGusta().add(meGusta);
    }

    @Override
    public void agregarComentario(Producto producto, String comentario, Vendedor autor) throws Exception {
        if (producto == null || comentario == null || autor == null) {
            throw new Exception("El producto, comentario y autor no pueden ser nulos");
        }

        if (comentario.trim().isEmpty()) {
            throw new Exception("El comentario no puede estar vacío");
        }

        if (!esProductoAccesible(producto, autor)) {
            throw new Exception("No tienes permiso para comentar este producto");
        }

        Comentario nuevoComentario = modelFactory.crearComentario(comentario, autor, producto);
        producto.getComentarios().add(nuevoComentario);
        autor.getComentarios().add(nuevoComentario);
    }

    @Override
    public List<Producto> obtenerProductosRed(Vendedor vendedor) {
        if (vendedor == null) return new ArrayList<>();

        List<Producto> productosRed = new ArrayList<>(vendedor.getProductos());
        vendedor.getContactos().forEach(contacto -> productosRed.addAll(contacto.getProductos()));

        return productosRed.stream()
                .distinct()
                .sorted((p1, p2) -> p2.getFechaPublicacion().compareTo(p1.getFechaPublicacion()))
                .collect(Collectors.toList());
    }

    private boolean esProductoAccesible(Producto producto, Vendedor vendedor) {
        return producto.getVendedor().equals(vendedor) ||
                vendedor.getContactos().contains(producto.getVendedor());
    }
}