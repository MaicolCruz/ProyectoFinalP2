package co.edu.uniquindio.proyectofinal.proyecto_finalp2.controller;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.factory.ServiceFactory;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.utils.MarketPlaceConstantes;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class MarketPlaceAppController {
    @FXML private TabPane tabPane;
    @FXML private MenuBar menuBar = new MenuBar();

    private Object usuarioActual;
    private ServiceFactory serviceFactory;

    @FXML
    public void initialize() {
        serviceFactory = ServiceFactory.getInstance();
        configurarMenus();
    }

    public void inicializarUsuario(Object usuario) {
        this.usuarioActual = usuario;
        cargarPestanas();
        actualizarMenus();
    }

    private void configurarMenus() {
        menuBar.setUseSystemMenuBar(true); // Para mejor integración en macOS
    }

    private void cargarPestanas() {
        tabPane.getTabs().clear();

        if (usuarioActual instanceof Administrador) {
            cargarVistaAdministrador();
        } else if (usuarioActual instanceof Vendedor) {
            cargarVistaVendedor((Vendedor) usuarioActual);
        }
    }

    private void cargarVistaAdministrador() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MarketPlaceConstantes.VISTA_ADMINISTRADOR));
            Tab tab = new Tab("Panel de Control");
            tab.setContent(loader.load());
            tab.setClosable(false);

            AdministradorController controller = loader.getController();
            controller.inicializar(serviceFactory.createAdministradorCrud());

            tabPane.getTabs().add(tab);

        } catch (IOException e) {
            mostrarError("Error al cargar vista de administrador", e);
        }
    }

    private void cargarVistaVendedor(Vendedor vendedor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MarketPlaceConstantes.VISTA_VENDEDOR));
            Tab tab = new Tab(vendedor.getNombre());
            tab.setContent(loader.load());
            tab.setClosable(false);

            VendedorController controller = loader.getController();
            controller.inicializar(serviceFactory.createVendedorCrud(vendedor));

            tabPane.getTabs().add(tab);


            cargarPestanasContactos(vendedor);

        } catch (IOException e) {
            mostrarError("Error al cargar vista de vendedor", e);
        }
    }

    private void cargarPestanasContactos(Vendedor vendedor) {

        vendedor.getContactos().forEach(contacto -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(MarketPlaceConstantes.VISTA_VENDEDOR));
                Tab tab = new Tab(contacto.getNombre());
                tab.setContent(loader.load());

                VendedorController controller = loader.getController();
                controller.inicializar(serviceFactory.createVendedorCrud(contacto));
                controller.setModoVisualizacion(true); // Solo lectura para contactos

                tabPane.getTabs().add(tab);
            } catch (IOException e) {
                mostrarError("Error al cargar pestaña de contacto", e);
            }
        });
    }

    private void actualizarMenus() {

        if (usuarioActual instanceof Administrador) {
            habilitarMenusAdministrador();
        } else {
            habilitarMenusVendedor();
        }
    }

    @FXML
    private void handleCerrarSesion(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Cierre de Sesión");
        alert.setContentText("¿Está seguro que desea cerrar sesión?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(MarketPlaceConstantes.VISTA_LOGIN));
                Scene scene = new Scene(loader.load());

                Stage stage = (Stage) tabPane.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle(MarketPlaceConstantes.TITULO_APP + " - Login");
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                mostrarError("Error al volver a la pantalla de login", e);
            }
        }
    }

    @FXML
    private void handleExportarEstadisticas(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Estadísticas");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos de texto", "*.txt")
        );

        File file = fileChooser.showSaveDialog(tabPane.getScene().getWindow());
        if (file != null) {
            try {
                if (usuarioActual instanceof Administrador) {
                    serviceFactory.createAdministradorCrud()
                            .exportarEstadisticas(file.getAbsolutePath(), "COMPLETO");
                    mostrarInformacion("Estadísticas exportadas exitosamente");
                }
            } catch (Exception e) {
                mostrarError("Error al exportar estadísticas", e);
            }
        }
    }

    @FXML
    private void handleSalir(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Salida");
        alert.setContentText("¿Está seguro que desea salir de la aplicación?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    private void mostrarError(String mensaje, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(mensaje);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void habilitarMenusAdministrador() {

    }

    private void habilitarMenusVendedor() {

    }
}