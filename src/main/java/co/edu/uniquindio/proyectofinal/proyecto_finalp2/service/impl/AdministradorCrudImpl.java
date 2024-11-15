package co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.impl;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces.IAdministradorCrud;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.EstadoProducto;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AdministradorCrudImpl implements IAdministradorCrud {
    private final MarketPlace marketPlace = MarketPlace.getInstance();

    @Override
    public void agregarVendedor(Vendedor vendedor) {
        if (vendedor != null && validarVendedor(vendedor)) {
            marketPlace.agregarVendedor(vendedor);
        }
    }

    @Override
    public void eliminarVendedor(String cedula) {
        marketPlace.eliminarVendedor(cedula);
    }

    @Override
    public void actualizarVendedor(Vendedor vendedor) {
        if (vendedor != null && validarVendedor(vendedor)) {
            Vendedor existente = buscarVendedor(vendedor.getCedula());
            if (existente != null) {
                marketPlace.eliminarVendedor(existente.getCedula());
                marketPlace.agregarVendedor(vendedor);
            }
        }
    }

    @Override
    public Vendedor buscarVendedor(String cedula) {
        return marketPlace.getVendedores().stream()
                .filter(v -> v.getCedula().equals(cedula))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Vendedor> listarVendedores() {
        return marketPlace.getVendedores();
    }

    @Override
    public void exportarEstadisticas(String ruta, String tipoReporte) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
            writer.println("Reporte e informe generado " + tipoReporte);
            writer.println("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            writer.println("Usuario: Administrador\n");

            switch (tipoReporte.toUpperCase()) {
                case "VENDEDORES":
                    writer.println(generarReporteVendedores());
                    break;
                case "PRODUCTOS":
                    writer.println(generarReporteProductos());
                    break;
                case "INTERACCIONES":
                    writer.println(generarReporteInteracciones());
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al exportar estadísticas: " + e.getMessage());
        }
    }

    @Override
    public String generarReporteVendedores() {
        StringBuilder report = new StringBuilder("REPORTE DE VENDEDORES\n\n");

        List<Vendedor> vendedores = marketPlace.getVendedores();
        report.append("Total de vendedores: ").append(vendedores.size()).append("\n\n");

        vendedores.forEach(v -> {
            report.append("Vendedor: ").append(v.getNombre()).append(" ").append(v.getApellidos()).append("\n");
            report.append("Cédula: ").append(v.getCedula()).append("\n");
            report.append("Productos publicados: ").append(v.getProductos().size()).append("\n");
            report.append("Contactos: ").append(v.getContactos().size()).append("\n");
            report.append("----------------------------------------\n");
        });

        return report.toString();
    }

    @Override
    public String generarReporteProductos() {
        StringBuilder report = new StringBuilder("REPORTE DE PRODUCTOS\n\n");

        List<Producto> productos = marketPlace.getProductos();
        long productosPublicados = productos.stream()
                .filter(p -> p.getEstado() == EstadoProducto.PUBLICADO)
                .count();
        long productosVendidos = productos.stream()
                .filter(p -> p.getEstado() == EstadoProducto.VENDIDO)
                .count();

        report.append("Total de productos: ").append(productos.size()).append("\n");
        report.append("Productos publicados: ").append(productosPublicados).append("\n");
        report.append("Productos vendidos: ").append(productosVendidos).append("\n\n");

        report.append("Top productos por me gusta:\n");
        productos.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getMeGusta().size(), p1.getMeGusta().size()))
                .limit(10)
                .forEach(p -> {
                    report.append("- ").append(p.getNombre())
                            .append(" (").append(p.getMeGusta().size()).append(" me gusta)\n");
                });

        return report.toString();
    }

    @Override
    public String generarReporteInteracciones() {
        StringBuilder report = new StringBuilder("REPORTE DE INTERACCIONES\n\n");

        long totalComentarios = marketPlace.getProductos().stream()
                .mapToLong(p -> p.getComentarios().size())
                .sum();
        long totalMeGusta = marketPlace.getProductos().stream()
                .mapToLong(p -> p.getMeGusta().size())
                .sum();

        report.append("Total de interacciones: ").append(totalComentarios + totalMeGusta).append("\n");
        report.append("Total comentarios: ").append(totalComentarios).append("\n");
        report.append("Total me gusta: ").append(totalMeGusta).append("\n\n");

        report.append("Vendedores más activos:\n");
        marketPlace.getVendedores().stream()
                .sorted((v1, v2) -> Integer.compare(
                        v2.getComentarios().size() + v2.getMuroMensajes().size(),
                        v1.getComentarios().size() + v1.getMuroMensajes().size()))
                .limit(5)
                .forEach(v -> {
                    report.append("- ").append(v.getNombre())
                            .append(" (").append(v.getComentarios().size() + v.getMuroMensajes().size())
                            .append(" interacciones)\n");
                });

        return report.toString();
    }

    private boolean validarVendedor(Vendedor vendedor) {
        return vendedor.getNombre() != null && !vendedor.getNombre().trim().isEmpty() &&
                vendedor.getApellidos() != null && !vendedor.getApellidos().trim().isEmpty() &&
                vendedor.getCedula() != null && !vendedor.getCedula().trim().isEmpty() &&
                vendedor.getUsuario() != null && !vendedor.getUsuario().trim().isEmpty() &&
                vendedor.getContrasena() != null && !vendedor.getContrasena().trim().isEmpty();
    }
}
