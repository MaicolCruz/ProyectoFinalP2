package co.edu.uniquindio.proyectofinal.proyecto_finalp2.controller;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.EstadoProducto;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces.IAdministradorCrud;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

public class AdministradorController {
    @FXML private TableView<Vendedor> tableVendedores;
    @FXML private TableColumn<Vendedor, String> colCedula;
    @FXML private TableColumn<Vendedor, String> colNombre;
    @FXML private TableColumn<Vendedor, String> colApellidos;
    @FXML private TableColumn<Vendedor, String> colUsuario;

    @FXML private Label lblTotalVendedores;
    @FXML private Label lblTotalProductos;
    @FXML private Label lblProductosVendidos;

    @FXML private ComboBox<String> comboTipoEstadistica;
    private IAdministradorCrud adminService;

    public void inicializar(IAdministradorCrud adminService) {
        this.adminService = adminService;
        configurarTabla();
        configurarComboBox();
        cargarDatos();
        actualizarEstadisticas();
        configurarSeleccionTabla();
    }

    private void configurarTabla() {
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));

        // Agregar tooltip a las celdas
        tableVendedores.setRowFactory(tv -> {
            TableRow<Vendedor> row = new TableRow<>();
            row.setTooltip(new Tooltip("Doble clic para ver detalles"));
            return row;
        });

        // Configurar doble clic en las filas
        tableVendedores.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tableVendedores.getSelectionModel().getSelectedItem() != null) {
                mostrarDetallesVendedor(tableVendedores.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void configurarSeleccionTabla() {
        tableVendedores.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean haySeleccion = newSelection != null;
                    // Aquí puedes habilitar/deshabilitar botones según si hay selección
                }
        );
    }

    private void configurarComboBox() {
        comboTipoEstadistica.setItems(FXCollections.observableArrayList(
                "Vendedores Activos",
                "Productos",
                "Interacciones"
        ));
        comboTipoEstadistica.getSelectionModel().selectFirst();
    }

    private void cargarDatos() {
        tableVendedores.getItems().clear();
        tableVendedores.setItems(FXCollections.observableArrayList(adminService.listarVendedores()));
        tableVendedores.refresh();
    }

    private void actualizarEstadisticas() {
        int totalVendedores = adminService.listarVendedores().size();
        lblTotalVendedores.setText(String.valueOf(totalVendedores));

        int totalProductos = adminService.listarVendedores().stream()
                .mapToInt(v -> v.getProductos().size())
                .sum();
        lblTotalProductos.setText(String.valueOf(totalProductos));

        long productosVendidos = adminService.listarVendedores().stream()
                .flatMap(v -> v.getProductos().stream())
                .filter(p -> p.getEstado() == EstadoProducto.VENDIDO)
                .count();
        lblProductosVendidos.setText(String.valueOf(productosVendidos));
    }

    @FXML
    private void handleNuevoVendedor() {
        try {
            Dialog<Vendedor> dialog = new VendedorDialog(null);
            Optional<Vendedor> resultado = dialog.showAndWait();

            resultado.ifPresent(vendedor -> {
                try {
                    adminService.agregarVendedor(vendedor);
                    actualizarEstadisticas();
                    mostrarInformacion("Vendedor agregado exitosamente");
                    cargarDatos();
                } catch (Exception e) {
                    mostrarError("Error al agregar vendedor", e.getMessage());
                }
            });
        } catch (Exception e) {
            mostrarError("Error", "No se pudo crear el diálogo de nuevo vendedor");
        }
    }

    @FXML
    private void handleEditarVendedor() {
        Vendedor vendedor = tableVendedores.getSelectionModel().getSelectedItem();
        if (vendedor != null) {
            try {
                Dialog<Vendedor> dialog = new VendedorDialog(vendedor);
                Optional<Vendedor> resultado = dialog.showAndWait();

                resultado.ifPresent(vendedorActualizado -> {
                    try {
                        adminService.actualizarVendedor(vendedorActualizado);

                        actualizarEstadisticas();
                        mostrarInformacion("Vendedor actualizado exitosamente");
                        cargarDatos();
                    } catch (Exception e) {
                        mostrarError("Error al actualizar vendedor", e.getMessage());
                    }
                });
            } catch (Exception e) {
                mostrarError("Error", "No se pudo crear el diálogo de edición");
            }
        } else {
            mostrarAdvertencia("Selección requerida", "Por favor, seleccione un vendedor para editar");
        }
    }

    @FXML
    private void handleEliminarVendedor() {
        Vendedor vendedor = tableVendedores.getSelectionModel().getSelectedItem();
        if (vendedor != null) {
            Optional<ButtonType> resultado = mostrarConfirmacion(
                    "Confirmar eliminación",
                    "¿Está seguro de eliminar al vendedor " + vendedor.getNombre() + "?"
            );

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try {
                    adminService.eliminarVendedor(vendedor.getCedula());
                    actualizarEstadisticas();
                    mostrarInformacion("Vendedor eliminado exitosamente");
                    cargarDatos();
                } catch (Exception e) {
                    mostrarError("Error al eliminar vendedor", e.getMessage());
                }
            }
        } else {
            mostrarAdvertencia("Selección requerida", "Por favor, seleccione un vendedor para eliminar");
        }
    }

    @FXML
    private void handleGenerarReporte() {
        String tipoReporte = comboTipoEstadistica.getValue();
        if (tipoReporte == null) {
            mostrarAdvertencia("Selección requerida", "Por favor, seleccione un tipo de reporte");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos de texto", "*.txt")
        );

        File file = fileChooser.showSaveDialog(tableVendedores.getScene().getWindow());
        if (file != null) {
            try {
                adminService.exportarEstadisticas(file.getAbsolutePath(),
                        tipoReporte.replace(" ", "_").toUpperCase());
                mostrarInformacion("Reporte generado exitosamente");
            } catch (Exception e) {
                mostrarError("Error al generar reporte", e.getMessage());
            }
        }
    }

    private void mostrarDetallesVendedor(Vendedor vendedor) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detalles del Vendedor");
        dialog.setHeaderText("Información detallada de " + vendedor.getNombre());

        ButtonType cerrarButton = new ButtonType("Cerrar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(cerrarButton);

        VBox contenido = new VBox(10);
        contenido.getChildren().addAll(
                new Label("Cédula: " + vendedor.getCedula()),
                new Label("Nombre completo: " + vendedor.getNombre() + " " + vendedor.getApellidos()),
                new Label("Usuario: " + vendedor.getUsuario()),
                new Label("Dirección: " + vendedor.getDireccion()),
                new Label("Total productos: " + vendedor.getProductos().size()),
                new Label("Total contactos: " + vendedor.getContactos().size())
        );

        dialog.getDialogPane().setContent(contenido);
        dialog.showAndWait();
    }

    private Optional<ButtonType> mostrarConfirmacion(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        return alert.showAndWait();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
