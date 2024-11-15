package co.edu.uniquindio.proyectofinal.proyecto_finalp2.model;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MarketPlace {
    private static MarketPlace instance;
    private List<Vendedor> vendedores;
    private List<Producto> productos;
    private Administrador administrador;

    private MarketPlace() {
        DataUtil dataUtil = DataUtil.getInstance();
        this.vendedores = new ArrayList<>(dataUtil.getVendedores());
        this.productos = new ArrayList<>(dataUtil.getProductos());
        this.administrador = dataUtil.getAdministrador();
    }

    public static synchronized MarketPlace getInstance() {
        if (instance == null) {
            instance = new MarketPlace();
        }
        return instance;
    }

    // Métodos de gestión de vendedores
    public boolean agregarVendedor(Vendedor vendedor) {
        if (vendedor != null && !existeVendedor(vendedor.getCedula())) {
            return vendedores.add(vendedor);
        }
        return false;
    }

    public boolean eliminarVendedor(String cedula) {
        return vendedores.removeIf(v -> v.getCedula().equals(cedula));
    }

    public Optional<Vendedor> buscarVendedor(String cedula) {
        return vendedores.stream()
                .filter(v -> v.getCedula().equals(cedula))
                .findFirst();
    }

    private boolean existeVendedor(String cedula) {
        return vendedores.stream()
                .anyMatch(v -> v.getCedula().equals(cedula));
    }

    // Métodos de gestión deagregarProducto productos

    public boolean agregarProducto(Producto producto) {
        if (producto == null) {
            return false;
        }
        // Validar que el producto tenga todos los datos necesarios
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty() ||
                producto.getVendedor() == null) {
            return false;
        }
        // Verificar que no exista un producto con el mismo nombre
        if (existeProducto(producto.getNombre())) {
            return false;
        }
        // Agregar el producto a la lista general
        boolean agregado = productos.add(producto);

        return agregado;
    }

    public boolean eliminarProducto(String nombre) {
        Producto producto = buscarProducto(nombre);
        if (producto != null) {
            // Eliminar el producto de la lista del vendedor
            producto.getVendedor().getProductos().remove(producto);
            // Eliminar el producto de la lista general
            boolean eliminado = productos.remove(producto);
            if (eliminado) {
                System.out.println("Producto eliminado exitosamente: " + nombre); // Debug
            }
            return eliminado;
        }
        return false;
    }

    public Producto buscarProducto(String nombre) {
        return productos.stream()
                .filter(p -> p.getNombre().equals(nombre))
                .findFirst()
                .orElse(null);
    }

    private boolean existeProducto(String nombre) {
        boolean existe = productos.stream()
                .anyMatch(p -> p.getNombre().equals(nombre));
        System.out.println("Verificando existencia de producto " + nombre + ": " + existe); // Debug
        return existe;
    }


    // Getters con copias defensivas
    public List<Vendedor> getVendedores() {
        return new ArrayList<>(vendedores);
    }

    public List<Producto> getProductos() {
        if (productos == null) {
            productos = new ArrayList<>();
        }
        return productos;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    // Método de autenticación
    public boolean autenticarUsuario(String usuario, String contrasena) {
        if (administrador.verificarCredenciales(usuario, contrasena)) {
            return true;
        }
        return vendedores.stream()
                .anyMatch(v -> v.getUsuario().equals(usuario) &&
                        v.getContrasena().equals(contrasena));
    }

    public Object obtenerUsuario(String usuario, String contrasena) {
        if (administrador.verificarCredenciales(usuario, contrasena)) {
            return administrador;
        }

        return vendedores.stream()
                .filter(v -> v.getUsuario().equals(usuario) &&
                        v.getContrasena().equals(contrasena))
                .findFirst()
                .orElse(null);
    }
}