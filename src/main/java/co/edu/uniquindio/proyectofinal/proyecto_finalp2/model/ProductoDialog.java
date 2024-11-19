package co.edu.uniquindio.proyectofinal.proyecto_finalp2.model;

import javafx.scene.Node;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.EstadoProducto;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import java.io.File;

public class ProductoDialog extends Dialog<Producto> {
    private TextField txtNombre;
    private TextField txtCategoria;
    private TextField txtPrecio;
    private ComboBox<EstadoProducto> comboEstado;
    private TextField txtImagen;
    private final Vendedor vendedor;

    public ProductoDialog(Producto producto, Vendedor vendedor) {
        this.vendedor = vendedor;
        setTitle(producto == null ? "Nuevo Producto" : "Editar Producto");

        // Personalizar el header
        Text headerText = new Text(producto == null ? "Ingrese los datos del nuevo producto" : "Modifique los datos del producto");
        headerText.setFill(Color.web("#9582ff"));
        headerText.setFont(Font.font("System", 16));
        VBox header = new VBox(headerText);
        header.setPadding(new Insets(20, 20, 10, 20));
        getDialogPane().setHeader(header);

        // Configurar botones
        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        // Crear grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #f7f1f7;");

        // Crear campos estilizados
        txtNombre = crearCampoEstilizado("Nombre del producto");
        txtCategoria = crearCampoEstilizado("Categoría");
        txtPrecio = crearCampoEstilizado("Precio");
        txtImagen = crearCampoEstilizado("Ruta de la imagen");

        // Estilizar ComboBox
        comboEstado = new ComboBox<>();
        comboEstado.getItems().addAll(EstadoProducto.values());
        comboEstado.setValue(EstadoProducto.PUBLICADO);
        comboEstado.setStyle("-fx-background-color: #e1dcfc; " +
                "-fx-text-fill: #6228f5; " +
                "-fx-border-color: #a898ff; " +
                "-fx-border-width: 0 0 2 0;");

        // Botón seleccionar imagen estilizado
        Button btnSeleccionarImagen = new Button("Seleccionar");
        btnSeleccionarImagen.setStyle("-fx-background-color: #9582ff; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 12px;");

        btnSeleccionarImagen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar Imagen");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
            );
            File file = fileChooser.showOpenDialog(getDialogPane().getScene().getWindow());
            if (file != null) {
                txtImagen.setText(file.getAbsolutePath());
            }
        });

        // Estilizar labels
        String labelStyle = "-fx-text-fill: #9582ff; -fx-font-size: 14px;";

        // Agregar campos al grid con labels estilizados
        Label lblNombre = new Label("Nombre:");
        lblNombre.setStyle(labelStyle);
        grid.add(lblNombre, 0, 0);
        grid.add(txtNombre, 1, 0, 2, 1);

        Label lblCategoria = new Label("Categoría:");
        lblCategoria.setStyle(labelStyle);
        grid.add(lblCategoria, 0, 1);
        grid.add(txtCategoria, 1, 1, 2, 1);

        Label lblPrecio = new Label("Precio:");
        lblPrecio.setStyle(labelStyle);
        grid.add(lblPrecio, 0, 2);
        grid.add(txtPrecio, 1, 2, 2, 1);

        Label lblEstado = new Label("Estado:");
        lblEstado.setStyle(labelStyle);
        grid.add(lblEstado, 0, 3);
        grid.add(comboEstado, 1, 3, 2, 1);

        Label lblImagen = new Label("Imagen:");
        lblImagen.setStyle(labelStyle);
        grid.add(lblImagen, 0, 4);

        // Crear HBox para imagen y botón
        HBox imagenContainer = new HBox(10);
        imagenContainer.getChildren().addAll(txtImagen, btnSeleccionarImagen);
        grid.add(imagenContainer, 1, 4, 2, 1);

        // Si es edición, cargar datos
        if (producto != null) {
            txtNombre.setText(producto.getNombre());
            txtCategoria.setText(producto.getCategoria());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
            comboEstado.setValue(producto.getEstado());
            txtImagen.setText(producto.getImagen());
            txtNombre.setDisable(true);
        }

        // Estilizar el botón de guardar
        Node guardarButton = getDialogPane().lookupButton(guardarButtonType);
        guardarButton.setStyle("-fx-background-color: #9582ff; -fx-text-fill: white;");
        guardarButton.setDisable(true);

        // Estilizar el botón de cancelar
        Node cancelarButton = getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelarButton.setStyle("-fx-background-color: #e1dcfc; -fx-text-fill: #9582ff;");

        // Listener para validación
        Runnable validador = () -> {
            boolean datosValidos = !txtNombre.getText().trim().isEmpty() &&
                    !txtCategoria.getText().trim().isEmpty() &&
                    !txtPrecio.getText().trim().isEmpty() &&
                    validarPrecio(txtPrecio.getText().trim());
            guardarButton.setDisable(!datosValidos);
        };

        // Agregar listeners
        txtNombre.textProperty().addListener((obs, old, newValue) -> validador.run());
        txtCategoria.textProperty().addListener((obs, old, newValue) -> validador.run());
        txtPrecio.textProperty().addListener((obs, old, newValue) -> validador.run());

        // Estilizar el dialog pane
        DialogPane dialogPane = getDialogPane();
        dialogPane.setStyle("-fx-background-color: #f7f1f7;");
        dialogPane.setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == guardarButtonType) {
                try {
                    Producto nuevoProducto = producto == null ? new Producto() : producto;
                    nuevoProducto.setNombre(txtNombre.getText().trim());
                    nuevoProducto.setCategoria(txtCategoria.getText().trim());
                    nuevoProducto.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
                    nuevoProducto.setEstado(comboEstado.getValue());
                    nuevoProducto.setImagen(txtImagen.getText().trim());
                    nuevoProducto.setVendedor(vendedor);
                    return nuevoProducto;
                } catch (Exception e) {
                    mostrarError("Error al crear producto", e.getMessage());
                    return null;
                }
            }
            return null;
        });
    }

    private TextField crearCampoEstilizado(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle("-fx-background-color: rgba(255,255,255,0.0); " +
                "-fx-border-color: #a898ff; " +
                "-fx-border-width: 0 0 2 0; " +
                "-fx-text-fill: #6228f5;");
        return textField;
    }

    private boolean validarPrecio(String precio) {
        try {
            double value = Double.parseDouble(precio);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Estilizar el diálogo de error
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #f7f1f7;");

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle("-fx-background-color: #9582ff; -fx-text-fill: white;");

        alert.showAndWait();
    }
}