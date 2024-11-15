package co.edu.uniquindio.proyectofinal.proyecto_finalp2.controller;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.utils.MarketPlaceConstantes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Label lblError;
    @FXML private Button btnLogin;

    private MarketPlace marketPlace = MarketPlace.getInstance();

    @FXML
    public void initialize() {
        // Configurar validaciones en tiempo real
        txtUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
            validarCampos();
        });

        txtContrasena.textProperty().addListener((observable, oldValue, newValue) -> {
            validarCampos();
        });

        btnLogin.setDisable(true);
    }

    private void validarCampos() {
        boolean camposValidos = !txtUsuario.getText().trim().isEmpty() &&
                !txtContrasena.getText().trim().isEmpty();
        btnLogin.setDisable(!camposValidos);
        lblError.setVisible(false);
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            String usuario = txtUsuario.getText().trim();
            String contrasena = txtContrasena.getText();

            if (marketPlace.autenticarUsuario(usuario, contrasena)) {
                Object usuarioObj = marketPlace.obtenerUsuario(usuario, contrasena);
                abrirVentanaPrincipal(usuarioObj);
            } else {
                mostrarError(MarketPlaceConstantes.ERROR_LOGIN);
                txtContrasena.clear();
                txtContrasena.requestFocus();
                mostrarError("Error al iniciar sesión: " );
            }
        } catch (Exception e) {
            mostrarError("Error al iniciar sesión: " + e.getMessage());
        }
    }

    private void abrirVentanaPrincipal(Object usuario) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(MarketPlaceConstantes.VISTA_PRINCIPAL));

            Scene scene = new Scene(loader.load());

            MarketPlaceAppController controller = loader.getController();
            if (controller == null) {
                throw new IllegalStateException("No se pudo cargar el controlador principal");
            }

            controller.inicializarUsuario(usuario);

            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setTitle(MarketPlaceConstantes.TITULO_APP + " - " +
                    (usuario instanceof Administrador ? "Administrador" : "Vendedor"));
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al cargar la aplicación: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }
}
