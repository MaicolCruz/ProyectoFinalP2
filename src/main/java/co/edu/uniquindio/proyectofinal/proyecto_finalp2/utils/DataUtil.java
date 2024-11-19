package co.edu.uniquindio.proyectofinal.proyecto_finalp2.utils;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.builder.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.EstadoProducto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataUtil {
    private static DataUtil instance;
    private List<Vendedor> vendedores;
    private List<Producto> productos;
    private Administrador administrador;

    private DataUtil() {
        this.vendedores = new ArrayList<>();
        this.productos = new ArrayList<>();
        inicializarDatos();
    }

    public static synchronized DataUtil getInstance() {
        if (instance == null) {
            instance = new DataUtil();
        }
        return instance;
    }

    private void inicializarDatos() {
        // Crear administrador por defecto
        administrador = new Administrador("Admin", "Sistema", "1234567890",
                "Dirección Admin", "admin", "admin123");

// Crear vendedores con Builder
        Vendedor vendedor1 = new VendedorBuilder()
                .conNombre("Juan")
                .conApellidos("Pérez")
                .conCedula("123456789")
                .conDireccion("Calle 1 #1-1")
                .conUsuario("juan")
                .conContrasena("123")
                .build();

        Vendedor vendedor2 = new VendedorBuilder()
                .conNombre("María")
                .conApellidos("González")
                .conCedula("987654321")
                .conDireccion("Calle 2 #2-2")
                .conUsuario("maria")
                .conContrasena("123")
                .build();

        Vendedor vendedor3 = new VendedorBuilder()
                .conNombre("Esteban")
                .conApellidos("Carmona")
                .conCedula("1094878402")
                .conDireccion("Laureles MzB Cs2")
                .conUsuario("Esteban")
                .conContrasena("123")
                .build();

        Vendedor vendedor4 = new VendedorBuilder()
                .conNombre("Julian")
                .conApellidos("Mendez")
                .conCedula("14878402")
                .conDireccion("caciques")
                .conUsuario("julian")
                .conContrasena("123")
                .build();

        Vendedor vendedor5 = new VendedorBuilder()
                .conNombre("Martina")
                .conApellidos("Salazar")
                .conCedula("108402")
                .conDireccion("La montañera")
                .conUsuario("martina")
                .conContrasena("123")
                .build();

        Vendedor vendedor6 = new VendedorBuilder()
                .conNombre("Josepo")
                .conApellidos("Gonzalez")
                .conCedula("109485232")
                .conDireccion("Pereira nueva")
                .conUsuario("josepo")
                .conContrasena("123")
                .build();

// Agregar vendedores a la lista
        vendedores.addAll(Arrays.asList(vendedor1, vendedor2, vendedor3, vendedor4, vendedor5, vendedor6));

// Crear productos
        Producto laptop = new ProductoBuilder()
                .conNombre("Laptop HP")
                .conCategoria("Electrónicos")
                .conPrecio(2500000)
                .conVendedor(vendedor1)
                .conImagen("/images/laptop.png")
                .build();

        Producto celular = new ProductoBuilder()
                .conNombre("iPhone 13")
                .conCategoria("Electrónicos")
                .conPrecio(3800000)
                .conVendedor(vendedor2)
                .conImagen("/images/iphone.png")
                .build();

        Producto tablet = new ProductoBuilder()
                .conNombre("iPad Pro")
                .conCategoria("Electrónicos")
                .conPrecio(4200000)
                .conVendedor(vendedor3)
                .conImagen("/images/ipad.png")
                .build();

        Producto smartwatch = new ProductoBuilder()
                .conNombre("Apple Watch")
                .conCategoria("Electrónicos")
                .conPrecio(1800000)
                .conVendedor(vendedor4)
                .conImagen("/images/watch.png")
                .build();

// Agregar productos a vendedores
        vendedor1.publicarProducto(laptop);
        vendedor2.publicarProducto(celular);
        vendedor3.publicarProducto(tablet);
        vendedor4.publicarProducto(smartwatch);
        productos.addAll(Arrays.asList(laptop, celular, tablet, smartwatch));

// Establecer contactos (no todos tienen contactos)
        vendedor1.agregarContacto(vendedor2);
        vendedor2.agregarContacto(vendedor1);
        vendedor1.agregarContacto(vendedor3);
        vendedor3.agregarContacto(vendedor1);
        vendedor2.agregarContacto(vendedor4);
        vendedor4.agregarContacto(vendedor2);

// Crear interacciones (algunos tienen más interacciones que otros)
        vendedor2.agregarMeGusta(laptop);
        vendedor1.agregarMeGusta(celular);
        vendedor3.agregarMeGusta(laptop);
        vendedor4.agregarMeGusta(celular);

// Agregar comentarios (solo algunos productos tienen comentarios)
        vendedor2.agregarComentario(laptop, "¡Excelente producto! ¿Tiene descuento?");
        vendedor1.agregarComentario(celular, "¿Aún está disponible?");
        vendedor3.agregarComentario(laptop, "¿Tiene garantía extendida?");
        vendedor4.agregarComentario(celular, "¿Aceptas cambios?");

// Agregar mensajes al muro (no todos publican mensajes)
        vendedor1.agregarMensaje("¡Nuevos productos disponibles!");
        vendedor2.agregarMensaje("Ofertas especiales en smartphones");
        vendedor3.agregarMensaje("¡Visita mi tienda de tecnología!");


    }

    // Getters con copias defensivas
    public List<Vendedor> getVendedores() {
        return new ArrayList<>(vendedores);
    }

    public List<Producto> getProductos() {
        return new ArrayList<>(productos);
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    // Métodos de búsqueda
    public Vendedor buscarVendedor(String usuario, String contrasena) {
        return vendedores.stream()
                .filter(v -> v.getUsuario().equals(usuario) &&
                        v.getContrasena().equals(contrasena))
                .findFirst()
                .orElse(null);
    }

    public boolean esAdministradorValido(String usuario, String contrasena) {
        return administrador.getUsuario().equals(usuario) &&
                administrador.getContrasena().equals(contrasena);
    }

    // Métodos de validación
    public boolean existeUsuario(String usuario) {
        return vendedores.stream()
                .anyMatch(v -> v.getUsuario().equals(usuario)) ||
                administrador.getUsuario().equals(usuario);
    }

    public boolean existeProducto(String nombre) {
        return productos.stream()
                .anyMatch(p -> p.getNombre().equals(nombre));
    }

    // Métodos de utilidad para estadísticas
    public int getTotalProductosVendidos() {
        return (int) productos.stream()
                .filter(p -> p.getEstado() == EstadoProducto.VENDIDO)
                .count();
    }

    public int getTotalInteracciones() {
        return productos.stream()
                .mapToInt(p -> p.getComentarios().size() + p.getMeGusta().size())
                .sum();
    }

    public List<Producto> getProductosMasPopulares() {
        List<Producto> productosOrdenados = new ArrayList<>(productos);
        productosOrdenados.sort((p1, p2) ->
                Integer.compare(
                        p2.getMeGusta().size() + p2.getComentarios().size(),
                        p1.getMeGusta().size() + p1.getComentarios().size()
                )
        );
        return productosOrdenados;
    }

    public List<Vendedor> getVendedoresMasActivos() {
        List<Vendedor> vendedoresOrdenados = new ArrayList<>(vendedores);
        vendedoresOrdenados.sort((v1, v2) ->
                Integer.compare(
                        getInteraccionesVendedor(v2),
                        getInteraccionesVendedor(v1)
                )
        );
        return vendedoresOrdenados;
    }

    private int getInteraccionesVendedor(Vendedor vendedor) {
        return vendedor.getProductos().size() +
                vendedor.getComentarios().size() +
                vendedor.getMuroMensajes().size();
    }
}
