package co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.Vendedor;
import java.util.List;

public interface IAdministradorCrud {
    void agregarVendedor(Vendedor vendedor);
    void eliminarVendedor(String cedula);
    void actualizarVendedor(Vendedor vendedor);
    Vendedor buscarVendedor(String cedula);
    List<Vendedor> listarVendedores();
    void exportarEstadisticas(String ruta, String tipoReporte);
    String generarReporteVendedores();
    String generarReporteProductos();
    String generarReporteInteracciones();
}