package co.edu.uniquindio.proyectofinal.proyecto_finalp2.controller;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.EstadoProducto;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces.IAdministradorCrud;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdministradorController {
    @FXML private TableView<Vendedor> tableVendedores;
    @FXML private TableColumn<Vendedor, String> colCedula;
    @FXML private TableColumn<Vendedor, String> colNombre;
    @FXML private TableColumn<Vendedor, String> colApellidos;
    @FXML private TableColumn<Vendedor, String> colUsuario;
    @FXML private TableColumn<Producto, String> colRedVendedor;
    @FXML private TableColumn<Producto, String> colRedNombre = new TableColumn<>();
    @FXML private TableColumn<Producto, Double> colRedPrecio;
    @FXML private TableColumn<Producto, EstadoProducto> colRedEstado;
    @FXML private TableColumn<Producto, EstadoProducto> colRedFecha;
    @FXML private Label lblTotalVendedores;
    @FXML private Label lblTotalProductos;
    @FXML private Label lblProductosVendidos;
    @FXML private TableView<Producto> tableProductosRed;
    @FXML private ComboBox<String> comboTipoEstadistica;
    private IAdministradorCrud adminService;

    public void inicializar(IAdministradorCrud adminService) {
        this.adminService = adminService;
        configurarTabla();
        actualizarTablaProductos();
        configurarTablas();
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


        tableVendedores.setRowFactory(tv -> {
            TableRow<Vendedor> row = new TableRow<>() {
                private Tooltip tooltip = new Tooltip();

                @Override
                protected void updateItem(Vendedor vendedor, boolean empty) {
                    super.updateItem(vendedor, empty);

                    if (empty || vendedor == null) {
                        setStyle("");
                        setTooltip(null);
                    } else {
                        // Estilo al pasar el mouse
                        setOnMouseEntered(event ->
                                setStyle("-fx-background-color: #e1dcfc;"));

                        // Crear contenido del tooltip con los productos
                        StringBuilder tooltipText = new StringBuilder();
                        tooltipText.append("Vendedor: ").append(vendedor.getNombre())
                                .append(" ").append(vendedor.getApellidos())
                                .append("\nCédula: ").append(vendedor.getCedula())
                                .append("\n\nProductos:");

                        if (vendedor.getProductos().isEmpty()) {
                            tooltipText.append("\nNo tiene productos registrados");
                        } else {
                            vendedor.getProductos().forEach(producto ->
                                    tooltipText.append("\n• ").append(producto.getNombre())
                                            .append(" - $").append(String.format("%,.0f", producto.getPrecio()))
                                            .append(" - ").append(producto.getEstado())
                            );
                        }

                        tooltip.setText(tooltipText.toString());
                        // Estilizar el tooltip
                        tooltip.setStyle("-fx-background-color: #f7f1f7; " +
                                "-fx-text-fill: #6228f5; " +
                                "-fx-font-size: 12px; " +
                                "-fx-border-color: #9582ff; " +
                                "-fx-border-width: 1px; " +
                                "-fx-padding: 10px;");

                        setTooltip(tooltip);
                    }
                }
            };

            // Manejar el doble clic en la fila
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    // Mostrar detalles del vendedor
                    mostrarDetallesVendedor(row.getItem());
                }
            });

            return row;
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

    private void configurarTablas() {
        // Configuración de columnas para tableProductosRed
// Configuración de columnas para tableProductosRed
        colRedVendedor.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getVendedor().getNombre()));
        colRedNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colRedPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colRedEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));


        // Agregar estilo a la tabla
        tableProductosRed.setStyle("-fx-background-color: white; -fx-border-color: #9582ff;");


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

        // Cargar productos de todos los vendedores
        List<Producto> todosLosProductos = adminService.listarVendedores().stream()
                .flatMap(vendedor -> vendedor.getProductos().stream())
                .collect(Collectors.toList());

        tableProductosRed.getItems().clear();
        tableProductosRed.setItems(FXCollections.observableArrayList(todosLosProductos));
        tableProductosRed.refresh();
        actualizarTablaProductos();
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

    private void actualizarTablaProductos() {
        List<Producto> todosLosProductos = adminService.listarVendedores().stream()
                .flatMap(vendedor -> vendedor.getProductos().stream())
                .collect(Collectors.toList());

        tableProductosRed.getItems().clear();
        tableProductosRed.setItems(FXCollections.observableArrayList(todosLosProductos));
        tableProductosRed.refresh();
    }

}
