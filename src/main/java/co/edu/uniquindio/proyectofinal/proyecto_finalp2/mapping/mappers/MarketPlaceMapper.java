package co.edu.uniquindio.proyectofinal.proyecto_finalp2.mapping.mappers;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.mapping.dto.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import java.util.List;
import java.util.stream.Collectors;

public class MarketPlaceMapper {
    public static VendedorDto toVendedorDto(Vendedor vendedor) {
        VendedorDto dto = new VendedorDto();
        dto.setNombre(vendedor.getNombre());
        dto.setApellidos(vendedor.getApellidos());
        dto.setCedula(vendedor.getCedula());
        dto.setDireccion(vendedor.getDireccion());
        dto.setUsuario(vendedor.getUsuario());
        dto.setContrasena(vendedor.getContrasena());

        dto.setProductos(vendedor.getProductos().stream()
                .map(MarketPlaceMapper::toProductoDto)
                .collect(Collectors.toList()));

        dto.setContactos(vendedor.getContactos().stream()
                .map(MarketPlaceMapper::toVendedorDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public static ProductoDto toProductoDto(Producto producto) {
        ProductoDto dto = new ProductoDto();
        dto.setNombre(producto.getNombre());
        dto.setImagen(producto.getImagen());
        dto.setCategoria(producto.getCategoria());
        dto.setPrecio(producto.getPrecio());
        dto.setEstado(producto.getEstado());
        dto.setFechaPublicacion(producto.getFechaPublicacion());
        dto.setVendedorCedula(producto.getVendedor().getCedula());
        dto.setComentarios(producto.getComentarios().stream()
                .map(MarketPlaceMapper::toComentarioDto)
                .collect(Collectors.toList()));
        dto.setMeGusta(producto.getMeGusta().stream()
                .map(MarketPlaceMapper::toMeGustaDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public static ComentarioDto toComentarioDto(Comentario comentario) {
        ComentarioDto dto = new ComentarioDto();
        dto.setContenido(comentario.getContenido());
        dto.setFecha(comentario.getFecha());
        dto.setAutorCedula(comentario.getAutor().getCedula());
        dto.setProductoNombre(comentario.getProducto().getNombre());
        return dto;
    }

    public static MeGustaDto toMeGustaDto(MeGusta meGusta) {
        MeGustaDto dto = new MeGustaDto();
        dto.setVendedorCedula(meGusta.getVendedor().getCedula());
        dto.setProductoNombre(meGusta.getProducto().getNombre());
        dto.setFecha(meGusta.getFecha());
        return dto;
    }
}