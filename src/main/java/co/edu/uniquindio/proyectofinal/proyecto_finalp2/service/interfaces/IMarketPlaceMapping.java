package co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import java.util.List;

public interface IMarketPlaceMapping {
    void vincularVendedores(Vendedor vendedor1, Vendedor vendedor2) throws Exception;
    List<Vendedor> obtenerContactosSugeridos(Vendedor vendedor);
    void agregarMeGusta(Producto producto, Vendedor vendedor) throws Exception;
    void agregarComentario(Producto producto, String comentario, Vendedor autor) throws Exception;
    List<Producto> obtenerProductosRed(Vendedor vendedor);
}