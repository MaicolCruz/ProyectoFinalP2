package co.edu.uniquindio.proyectofinal.proyecto_finalp2.controller;

import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.utils.MarketPlaceConstantes;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Duration;

public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Label lblError;
    @FXML private Button btnLogin;
    @FXML private Hyperlink forgotPasswordLink;

    private MarketPlace marketPlace = MarketPlace.getInstance();

    @FXML
    public void initialize() {

        txtUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
            validarCampos();
            lblError.setVisible((false));
        });

        txtContrasena.textProperty().addListener((observable, oldValue, newValue) -> {
            validarCampos();
            lblError.setVisible((false));
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
                mostrarError("Error al iniciar sesión: "+MarketPlaceConstantes.ERROR_LOGIN );
            }
        } catch (Exception e) {
            mostrarError("Error al iniciar sesión " + e.getMessage());
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
        lblError.setManaged(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(lblError.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(100), new KeyValue(lblError.translateXProperty(), -10)),
                new KeyFrame(Duration.millis(200), new KeyValue(lblError.translateXProperty(), 10)),
                new KeyFrame(Duration.millis(300), new KeyValue(lblError.translateXProperty(), -10)),
                new KeyFrame(Duration.millis(400), new KeyValue(lblError.translateXProperty(), 0))
        );
        timeline.play();
    }

}
