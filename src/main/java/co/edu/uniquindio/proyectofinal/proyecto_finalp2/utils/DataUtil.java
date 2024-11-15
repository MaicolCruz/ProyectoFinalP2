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
                .conUsuario("juan")  // Simplificado para pruebas
                .conContrasena("123") // Simplificado para pruebas
                .build();

        Vendedor vendedor2 = new VendedorBuilder()
                .conNombre("María")
                .conApellidos("González")
                .conCedula("987654321")
                .conDireccion("Calle 2 #2-2")
                .conUsuario("maria")
                .conContrasena("123")
                .build();

        // Agregar vendedores a la lista
        vendedores.add(vendedor1);
        vendedores.add(vendedor2);

        // Crear y asignar productos
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

        // Agregar productos
        vendedor1.publicarProducto(laptop);
        vendedor2.publicarProducto(celular);
        productos.addAll(Arrays.asList(laptop, celular));

        // Establecer contactos
        vendedor1.agregarContacto(vendedor2);
        vendedor2.agregarContacto(vendedor1);

        // Crear interacciones
        vendedor2.agregarMeGusta(laptop);
        vendedor1.agregarMeGusta(celular);

        // Agregar comentarios
        vendedor2.agregarComentario(laptop, "¡Excelente producto! ¿Tiene descuento?");
        vendedor1.agregarComentario(celular, "¿Aún está disponible?");

        // Agregar mensajes al muro
        vendedor1.agregarMensaje("¡Nuevos productos disponibles!");
        vendedor2.agregarMensaje("Ofertas especiales en smartphones");
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
