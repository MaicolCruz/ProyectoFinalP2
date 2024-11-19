package co.edu.uniquindio.proyectofinal.proyecto_finalp2.model;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;

public class VendedorDialog extends Dialog<Vendedor> {
    private TextField txtNombre;
    private TextField txtApellidos;
    private TextField txtCedula;
    private TextField txtDireccion;
    private TextField txtUsuario;
    private PasswordField txtContrasena;

    public VendedorDialog(Vendedor vendedor) {
        setTitle(vendedor == null ? "Nuevo Vendedor" : "Editar Vendedor");

        // Personalizar el header
        Text headerText = new Text(vendedor == null ? "Ingrese los datos del nuevo vendedor" : "Modifique los datos del vendedor");
        headerText.setFill(Color.web("#9582ff"));
        headerText.setFont(Font.font("System", 16));
        VBox header = new VBox(headerText);
        header.setPadding(new Insets(20, 20, 10, 20));
        getDialogPane().setHeader(header);

        // Configurar botones personalizados
        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        // Crear grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #f7f1f7;");

        // Crear y estilizar campos
        txtNombre = crearCampoEstilizado("Nombre");
        txtApellidos = crearCampoEstilizado("Apellidos");
        txtCedula = crearCampoEstilizado("Cédula");
        txtDireccion = crearCampoEstilizado("Dirección");
        txtUsuario = crearCampoEstilizado("Usuario");
        txtContrasena = crearPasswordFieldEstilizado("Contraseña");

        // Estilizar labels
        String labelStyle = "-fx-text-fill: #9582ff; -fx-font-size: 14px;";

        // Agregar campos al grid con labels estilizados
        Label lblNombre = new Label("Nombre:");
        lblNombre.setStyle(labelStyle);
        grid.add(lblNombre, 0, 0);
        grid.add(txtNombre, 1, 0);

        Label lblApellidos = new Label("Apellidos:");
        lblApellidos.setStyle(labelStyle);
        grid.add(lblApellidos, 0, 1);
        grid.add(txtApellidos, 1, 1);

        Label lblCedula = new Label("Cédula:");
        lblCedula.setStyle(labelStyle);
        grid.add(lblCedula, 0, 2);
        grid.add(txtCedula, 1, 2);

        Label lblDireccion = new Label("Dirección:");
        lblDireccion.setStyle(labelStyle);
        grid.add(lblDireccion, 0, 3);
        grid.add(txtDireccion, 1, 3);

        Label lblUsuario = new Label("Usuario:");
        lblUsuario.setStyle(labelStyle);
        grid.add(lblUsuario, 0, 4);
        grid.add(txtUsuario, 1, 4);

        Label lblContrasena = new Label("Contraseña:");
        lblContrasena.setStyle(labelStyle);
        grid.add(lblContrasena, 0, 5);
        grid.add(txtContrasena, 1, 5);

        // Si es edición, cargar datos
        if (vendedor != null) {
            txtNombre.setText(vendedor.getNombre());
            txtApellidos.setText(vendedor.getApellidos());
            txtCedula.setText(vendedor.getCedula());
            txtDireccion.setText(vendedor.getDireccion());
            txtUsuario.setText(vendedor.getUsuario());
            txtContrasena.setText(vendedor.getContrasena());
            txtCedula.setDisable(true);
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
                    !txtApellidos.getText().trim().isEmpty() &&
                    !txtCedula.getText().trim().isEmpty() &&
                    !txtDireccion.getText().trim().isEmpty() &&
                    !txtUsuario.getText().trim().isEmpty() &&
                    !txtContrasena.getText().isEmpty();
            guardarButton.setDisable(!datosValidos);
        };

        // Agregar listeners
        txtNombre.textProperty().addListener((obs, old, newValue) -> validador.run());
        txtApellidos.textProperty().addListener((obs, old, newValue) -> validador.run());
        txtCedula.textProperty().addListener((obs, old, newValue) -> validador.run());
        txtDireccion.textProperty().addListener((obs, old, newValue) -> validador.run());
        txtUsuario.textProperty().addListener((obs, old, newValue) -> validador.run());
        txtContrasena.textProperty().addListener((obs, old, newValue) -> validador.run());

        // Estilizar el dialog pane
        DialogPane dialogPane = getDialogPane();
        dialogPane.setStyle("-fx-background-color: #f7f1f7;");
        dialogPane.setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == guardarButtonType) {
                try {
                    Vendedor nuevoVendedor = vendedor == null ? new Vendedor() : vendedor;
                    nuevoVendedor.setNombre(txtNombre.getText().trim());
                    nuevoVendedor.setApellidos(txtApellidos.getText().trim());
                    nuevoVendedor.setCedula(txtCedula.getText().trim());
                    nuevoVendedor.setDireccion(txtDireccion.getText().trim());
                    nuevoVendedor.setUsuario(txtUsuario.getText().trim());
                    nuevoVendedor.setContrasena(txtContrasena.getText());
                    return nuevoVendedor;
                } catch (Exception e) {
                    mostrarError("Error al crear vendedor", e.getMessage());
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

    private PasswordField crearPasswordFieldEstilizado(String promptText) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        passwordField.setStyle("-fx-background-color: rgba(255,255,255,0.0); " +
                "-fx-border-color: #a898ff; " +
                "-fx-border-width: 0 0 2 0; " +
                "-fx-text-fill: #6228f5;");
        return passwordField;
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