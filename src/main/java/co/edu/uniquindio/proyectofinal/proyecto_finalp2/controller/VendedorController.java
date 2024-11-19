package co.edu.uniquindio.proyectofinal.proyecto_finalp2.controller;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.EstadoProducto;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces.IMarketPlaceMapping;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces.IVendedorCrud;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.factory.ServiceFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VendedorController {
    @FXML private ListView<String> listViewMuro;
    @FXML private TextField txtMensaje;
    @FXML private TableView<Producto> tableProductos;
    @FXML private TableColumn<Producto, String> colNombreProducto;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, EstadoProducto> colEstado;
    @FXML private TableColumn<Producto, EstadoProducto> colFecha;
    @FXML private ListView<Vendedor> listViewContactos;
    @FXML private Button btnPublicar = new Button();
    @FXML private Button btnEditarProducto = new Button();
    @FXML private Button btnEliminarProducto= new Button();
    @FXML private Button btnAgregarContacto= new Button();
    @FXML private TableView<Producto> tableProductosRed;
    @FXML private TableColumn<Producto, String> colRedVendedor;
    @FXML private TableColumn<Producto, String> colRedNombre = new TableColumn<>();
    @FXML private TableColumn<Producto, Double> colRedPrecio;
    @FXML private TableColumn<Producto, EstadoProducto> colRedEstado;
    @FXML private TableColumn<Producto, EstadoProducto> colRedFecha;


    private IVendedorCrud vendedorService;
    private boolean modoVisualizacion = false;
    private IMarketPlaceMapping marketPlaceMapping;

    public void inicializar(IVendedorCrud vendedorService) {
        this.vendedorService = vendedorService;
        this.marketPlaceMapping = ServiceFactory.getInstance().createMarketPlaceMapping();
        configurarTablas();
        configurarTablaProductos();
        configurarListViews();
        configurarBotones();
        configurarListViews();
        cargarDatos();
        configurarTablas();
    }

    public void setModoVisualizacion(boolean modoVisualizacion) {
        this.modoVisualizacion = modoVisualizacion;
        actualizarEstadoControles();
    }

    private void configurarTablas() {
        // Configuración de columnas para tableProductos
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPublicacion"));

        // Configuración de columnas para tableProductosRed
        colRedVendedor.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getVendedor().getNombre()));
        colRedNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colRedPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colRedEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colRedFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPublicacion"));
    }


    private void configurarListViews() {
        listViewMuro.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setWrapText(true);
                }
            }
        });

        listViewContactos.setCellFactory(param -> new ListCell<Vendedor>() {
            @Override
            protected void updateItem(Vendedor vendedor, boolean empty) {
                super.updateItem(vendedor, empty);
                if (empty || vendedor == null) {
                    setText(null);
                } else {
                    setText(vendedor.getNombre() + " " + vendedor.getApellidos());
                }
            }
        });
    }

    private void configurarBotones() {
        btnPublicar.setDisable(txtMensaje.getText().trim().isEmpty());
        txtMensaje.textProperty().addListener((obs, oldText, newText) ->
                btnPublicar.setDisable(newText.trim().isEmpty())
        );
    }

    private void actualizarEstadoControles() {
        boolean hayProductoSeleccionado = tableProductos.getSelectionModel().getSelectedItem() != null;
        btnEditarProducto.setDisable(modoVisualizacion || !hayProductoSeleccionado);
        btnEliminarProducto.setDisable(modoVisualizacion || !hayProductoSeleccionado);
        btnAgregarContacto.setDisable(modoVisualizacion);
        txtMensaje.setDisable(modoVisualizacion);
        btnPublicar.setDisable(modoVisualizacion);
    }

    private void cargarDatos() {
        if (vendedorService != null) {
            // Cargar productos del vendedor
            tableProductos.getItems().clear();
            tableProductos.setItems(FXCollections.observableArrayList(
                    vendedorService.listarMisProductos()));
            tableProductos.refresh();

            // Cargar productos de la red
            tableProductosRed.getItems().clear();
            tableProductosRed.setItems(FXCollections.observableArrayList(
                    vendedorService.obtenerProductosRed()));
            tableProductosRed.refresh();

            // Cargar contactos
            listViewContactos.getItems().clear();
            listViewContactos.setItems(FXCollections.observableArrayList(
                    vendedorService.listarMisContactos()));
            listViewContactos.refresh();

             //Cargar mensajes del muro
            listViewMuro.getItems().clear();
            listViewMuro.setItems(FXCollections.observableArrayList(
                    vendedorService.obtenerMuroMensajes()));
            listViewMuro.refresh();

//            //Cargar Comentarios del muro
//            listViewMuro.getItems().clear();
//            listViewMuro.setItems(FXCollections.observableArrayList(
//                    vendedorService.obtener()));
//            listViewMuro.refresh();

        }
    }

    @FXML
    private void handlePublicarMensaje() {
        String mensaje = txtMensaje.getText().trim();
        if (!mensaje.isEmpty()  ) {
            try {
                vendedorService.agregarMensaje(mensaje);
                txtMensaje.clear();
                cargarDatos();
            } catch (Exception e) {
                mostrarError("Error al publicar mensaje", e.getMessage());
            }
        }
    }

    @FXML
    private void handleNuevoProducto() {
        try {
            Dialog<Producto> dialog = new ProductoDialog(null, vendedorService.getVendedorActual());
            dialog.showAndWait().ifPresent(producto -> {
                try {

                    vendedorService.publicarProducto(producto);
                    mostrarInformacion("Producto publicado exitosamente");
                    cargarDatos();
                } catch (Exception e) {
                    mostrarError("Error", e.getMessage());
                }
            });
        } catch (Exception e) {
            mostrarError("Error", "No se pudo crear el diálogo");
        }
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
    @FXML
    private void handleEditarProducto() {
        Producto productoSeleccionado = tableProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            try {
                Dialog<Producto> dialog = new ProductoDialog(productoSeleccionado, vendedorService.getVendedorActual());
                Optional<Producto> resultado = dialog.showAndWait();

                resultado.ifPresent(productoActualizado -> {
                    try {
                        vendedorService.actualizarEstadoProducto(productoActualizado, productoActualizado.getEstado());
                        mostrarInformacion("Producto actualizado exitosamente");
                        cargarDatos();
                    } catch (Exception e) {
                        mostrarError("Error al actualizar producto", e.getMessage());
                    }
                });
            } catch (Exception e) {
                mostrarError("Error", "No se pudo abrir el diálogo de edición");
            }
        } else {
            mostrarAdvertencia("Selección necesaria", "Por favor, seleccione un producto para editar");
        }
    }

    @FXML
    private void handleEliminarProducto() {
        Producto productoSeleccionado = tableProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            Optional<ButtonType> confirmacion = mostrarConfirmacion(
                    "¿Está seguro que desea eliminar el producto " +
                            productoSeleccionado.getNombre() + "?"
            );

            if (confirmacion.isPresent() && confirmacion.get() == ButtonType.OK) {
                try {
                    vendedorService.eliminarProducto(productoSeleccionado.getNombre());
                    mostrarInformacion("Producto eliminado exitosamente");
                    cargarDatos();
                } catch (Exception e) {
                    mostrarError("Error al eliminar producto", e.getMessage());
                }
            }
        } else {
            mostrarAdvertencia("Selección necesaria", "Por favor, seleccione un producto para eliminar");
        }
    }

    @FXML
    private void handleCambiarEstado() {
        Producto productoSeleccionado = tableProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            Dialog<EstadoProducto> dialog = crearDialogoCambioEstado(productoSeleccionado.getEstado());
            Optional<EstadoProducto> resultado = dialog.showAndWait();

            resultado.ifPresent(nuevoEstado -> {
                try {
                    vendedorService.actualizarEstadoProducto(productoSeleccionado, nuevoEstado);
                    mostrarInformacion("Estado del producto actualizado exitosamente");
                    cargarDatos();
                } catch (Exception e) {
                    mostrarError("Error al actualizar estado", e.getMessage());
                }
            });
        } else {
            mostrarAdvertencia("Selección necesaria", "Por favor, seleccione un producto para cambiar su estado");
        }
    }

    @FXML
    private void handleAgregarContacto() {
        try {
            Dialog<Vendedor> dialog = crearDialogoSeleccionContacto();
            Optional<Vendedor> resultado = dialog.showAndWait();

            resultado.ifPresent(nuevoContacto -> {
                try {
                    vendedorService.agregarContacto(nuevoContacto);
                    mostrarInformacion("Contacto agregado exitosamente");
                    cargarDatos();
                } catch (Exception e) {
                    mostrarError("Error al agregar contacto", e.getMessage());
                }
            });
        } catch (Exception e) {
            mostrarError("Error", "No se pudo abrir el diálogo de selección de contactos");
        }
    }

    private void configurarTablaProductosRed() {
        colRedVendedor.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getVendedor().getNombre()));
        colRedNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colRedPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colRedEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void configurarTablaProductos() {
        // Configurar las columnas con los atributos de Producto
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        // Formatear la columna de precio
        colPrecio.setCellFactory(tc -> new TableCell<Producto, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });

        // Configurar selección
        tableProductos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }


        @FXML
    private void handleMeGusta() {
        Producto producto = tableProductosRed.getSelectionModel().getSelectedItem();
        if (producto != null) {
            try {
                vendedorService.agregarMeGusta(producto);
                cargarDatos();
                mostrarInformacion("Me gusta agregado exitosamente");
            } catch (Exception e) {
                mostrarError("Error", e.getMessage());
            }
        }
    }

    @FXML
    private void handleComentar() {
        Producto producto = tableProductosRed.getSelectionModel().getSelectedItem();
        if (producto != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nuevo Comentario");
            dialog.setHeaderText("Escriba su comentario para: " + producto.getNombre());

            dialog.showAndWait().ifPresent(comentario -> {
                try {
                    vendedorService.agregarComentario(producto, comentario);
                    mostrarInformacion("Comentario agregado exitosamente");
                    cargarDatos();
                } catch (Exception e) {
                    mostrarError("Error", e.getMessage());
                }
            });
        }
    }
    private Dialog<EstadoProducto> crearDialogoCambioEstado(EstadoProducto estadoActual) {
        Dialog<EstadoProducto> dialog = new Dialog<>();
        dialog.setTitle("Cambiar Estado del Producto");

        Text headerText = new Text("Seleccione el nuevo estado");
        headerText.setFill(Color.web("#9582ff"));
        headerText.setFont(Font.font("System", 16));

        VBox header = new VBox(headerText); // Crear el VBox con el texto
        header.setPadding(new Insets(20, 20, 10, 20));
        dialog.getDialogPane().setHeader(header);

        // Configurar botones
        ButtonType confirmarButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarButtonType, ButtonType.CANCEL);

        // Crear y estilizar ComboBox
        ComboBox<EstadoProducto> comboEstados = new ComboBox<>();
        comboEstados.getItems().addAll(EstadoProducto.values());
        comboEstados.setValue(estadoActual);
        comboEstados.setStyle("-fx-background-color: #e1dcfc; " +
                "-fx-text-fill: #6228f5; " +
                "-fx-border-color: #a898ff; " +
                "-fx-border-width: 0 0 2 0;");

        // Contenedor principal
        VBox contenedor = new VBox(10);
        contenedor.setPadding(new Insets(20));
        contenedor.setStyle("-fx-background-color: #f7f1f7;");
        contenedor.getChildren().add(comboEstados);

        // Estilizar botones
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(contenedor);
        dialogPane.setStyle("-fx-background-color: #f7f1f7;");

        Node confirmarButton = dialogPane.lookupButton(confirmarButtonType);
        confirmarButton.setStyle("-fx-background-color: #9582ff; -fx-text-fill: white;");

        Node cancelarButton = dialogPane.lookupButton(ButtonType.CANCEL);
        cancelarButton.setStyle("-fx-background-color: #e1dcfc; -fx-text-fill: #9582ff;");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmarButtonType) {
                return comboEstados.getValue();
            }
            return null;
        });

        return dialog;
    }

    private Dialog<Vendedor> crearDialogoSeleccionContacto() {
        Dialog<Vendedor> dialog = new Dialog<>();
        dialog.setTitle("Agregar Contacto");

        // Header personalizado
        Text headerText = new Text("Seleccione un vendedor para agregar a sus contactos");
        headerText.setFill(Color.web("#9582ff"));
        headerText.setFont(Font.font("System", 16));
        VBox header = new VBox(headerText);
        header.setPadding(new Insets(20, 20, 10, 20));
        dialog.getDialogPane().setHeader(header);

        // Configurar botones
        ButtonType agregarButtonType = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(agregarButtonType, ButtonType.CANCEL);

        // Crear y estilizar ListView
        ListView<Vendedor> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(obtenerVendedoresSugeridos()));
        listView.setStyle("-fx-background-color: #e1dcfc; " +
                "-fx-control-inner-background: #f7f1f7; " +
                "-fx-border-color: #a898ff; " +
                "-fx-border-width: 1px;");

        // Contenedor principal
        VBox contenedor = new VBox(10);
        contenedor.setPadding(new Insets(20));
        contenedor.setStyle("-fx-background-color: #f7f1f7;");
        contenedor.getChildren().add(listView);

        // Estilizar botones y diálogo
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(contenedor);
        dialogPane.setStyle("-fx-background-color: #f7f1f7;");

        Node agregarButton = dialogPane.lookupButton(agregarButtonType);
        agregarButton.setStyle("-fx-background-color: #9582ff; -fx-text-fill: white;");

        Node cancelarButton = dialogPane.lookupButton(ButtonType.CANCEL);
        cancelarButton.setStyle("-fx-background-color: #e1dcfc; -fx-text-fill: #9582ff;");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == agregarButtonType) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        return dialog;
    }

    private Dialog<String> crearDialogoComentario() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Agregar Comentario");

        // Header personalizado
        Text headerText = new Text("Escriba su comentario");
        headerText.setFill(Color.web("#9582ff"));
        headerText.setFont(Font.font("System", 16));
        VBox header = new VBox(headerText);
        header.setPadding(new Insets(20, 20, 10, 20));
        dialog.getDialogPane().setHeader(header);

        // Configurar botones
        ButtonType comentarButtonType = new ButtonType("Comentar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(comentarButtonType, ButtonType.CANCEL);

        // Crear y estilizar TextArea
        TextArea textArea = new TextArea();
        textArea.setPromptText("Escriba su comentario aquí...");
        textArea.setPrefRowCount(3);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-background-color: #e1dcfc; " +
                "-fx-control-inner-background: #f7f1f7; " +
                "-fx-border-color: #a898ff; " +
                "-fx-border-width: 2px; " +
                "-fx-text-fill: #6228f5;");

        // Contenedor principal
        VBox contenedor = new VBox(10);
        contenedor.setPadding(new Insets(20));
        contenedor.setStyle("-fx-background-color: #f7f1f7;");
        contenedor.getChildren().add(textArea);

        // Estilizar botones y diálogo
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(contenedor);
        dialogPane.setStyle("-fx-background-color: #f7f1f7;");

        Node comentarButton = dialogPane.lookupButton(comentarButtonType);
        comentarButton.setStyle("-fx-background-color: #9582ff; -fx-text-fill: white;");
        comentarButton.setDisable(true);

        Node cancelarButton = dialogPane.lookupButton(ButtonType.CANCEL);
        cancelarButton.setStyle("-fx-background-color: #e1dcfc; -fx-text-fill: #9582ff;");

        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            comentarButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == comentarButtonType) {
                String comentario = textArea.getText().trim();
                return comentario.isEmpty() ? null : comentario;
            }
            return null;
        });

        return dialog;
    }

    private List<Vendedor> obtenerVendedoresSugeridos() {
        Vendedor vendedorActual = vendedorService.getVendedorActual();
        return MarketPlace.getInstance().getVendedores().stream()
                .filter(vendedor -> !vendedor.getCedula().equals(vendedorActual.getCedula()) &&
                        !vendedorService.listarMisContactos().contains(vendedor))
                .collect(Collectors.toList());
    }


    private Optional<ButtonType> mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Acción");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait();
    }

    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    @FXML
    private void handleVerDetallesProducto() {
        Producto productoSeleccionado = tableProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            mostrarDetallesProducto(productoSeleccionado);
        }
    }

    private void mostrarDetallesProducto(Producto producto) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detalles del Producto");
        dialog.setHeaderText(producto.getNombre());

        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(20));


        contenido.getChildren().addAll(
                new Label("Categoría: " + producto.getCategoria()),
                new Label("Precio: $" + String.format("%.2f", producto.getPrecio())),
                new Label("Estado: " + producto.getEstado()),
                new Label("Fecha de publicación: " + producto.getFechaPublicacion().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                ))
        );

        // Me gusta
        Label lblMeGusta = new Label("Me gusta: " + producto.getMeGusta().size());
        ListView<String> listaMeGusta = new ListView<>();
        listaMeGusta.setItems(FXCollections.observableArrayList(
                producto.getMeGusta().stream()
                        .map(mg -> mg.getVendedor().getNombre() + " - " +
                                mg.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                        .collect(Collectors.toList())
        ));
        listaMeGusta.setPrefHeight(100);

        // Comentarios
        Label lblComentarios = new Label("Comentarios: " + producto.getComentarios().size());
        ListView<String> listaComentarios = new ListView<>();
        listaComentarios.setItems(FXCollections.observableArrayList(
                producto.getComentarios().stream()
                        .map(c -> c.getAutor().getNombre() + ": " + c.getContenido() + " - " +
                                c.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                        .collect(Collectors.toList())
        ));
        listaComentarios.setPrefHeight(150);

        contenido.getChildren().addAll(
                new Separator(),
                lblMeGusta,
                listaMeGusta,
                new Separator(),
                lblComentarios,
                listaComentarios
        );

        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

}