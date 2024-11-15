package co.edu.uniquindio.proyectofinal.proyecto_finalp2.model;
import javafx.scene.Node;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.EstadoProducto;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
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
        setHeaderText(producto == null ? "Ingrese los datos del nuevo producto" : "Modifique los datos del producto");

        // Configurar botones
        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        // Crear grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Crear campos
        txtNombre = new TextField();
        txtCategoria = new TextField();
        txtPrecio = new TextField();
        comboEstado = new ComboBox<>();
        txtImagen = new TextField();
        Button btnSeleccionarImagen = new Button("Seleccionar");

        // Configurar campos
        txtNombre.setPromptText("Nombre del producto");
        txtCategoria.setPromptText("Categoría");
        txtPrecio.setPromptText("Precio");
        txtImagen.setPromptText("Ruta de la imagen");
        comboEstado.getItems().addAll(EstadoProducto.values());
        comboEstado.setValue(EstadoProducto.PUBLICADO);

        // Configurar selector de imagen
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

        // Agregar campos al grid
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Categoría:"), 0, 1);
        grid.add(txtCategoria, 1, 1);
        grid.add(new Label("Precio:"), 0, 2);
        grid.add(txtPrecio, 1, 2);
        grid.add(new Label("Estado:"), 0, 3);
        grid.add(comboEstado, 1, 3);
        grid.add(new Label("Imagen:"), 0, 4);
        grid.add(txtImagen, 1, 4);
        grid.add(btnSeleccionarImagen, 2, 4);

        // Si es edición, cargar datos
        if (producto != null) {
            txtNombre.setText(producto.getNombre());
            txtCategoria.setText(producto.getCategoria());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
            comboEstado.setValue(producto.getEstado());
            txtImagen.setText(producto.getImagen());
            txtNombre.setDisable(true);
        }

        // Validación en tiempo real
        Node guardarButton = getDialogPane().lookupButton(guardarButtonType);
        guardarButton.setDisable(true);

        // Listener para validación
        Runnable validador = () -> {
            boolean datosValidos = !txtNombre.getText().trim().isEmpty() &&
                    !txtCategoria.getText().trim().isEmpty() &&
                    !txtPrecio.getText().trim().isEmpty() &&
                    validarPrecio(txtPrecio.getText().trim());
            guardarButton.setDisable(!datosValidos);
        };

        txtNombre.textProperty().addListener((obs, old, newValue) -> validador.run());
        txtCategoria.textProperty().addListener((obs, old, newValue) -> validador.run());
        txtPrecio.textProperty().addListener((obs, old, newValue) -> validador.run());

        getDialogPane().setContent(grid);

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
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}