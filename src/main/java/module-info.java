module co.edu.uniquindio.proyectofinal.proyecto_finalp2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens co.edu.uniquindio.proyectofinal.proyecto_finalp2 to javafx.fxml;
    exports co.edu.uniquindio.proyectofinal.proyecto_finalp2;

    opens co.edu.uniquindio.proyectofinal.proyecto_finalp2.controller to javafx.fxml;
    exports co.edu.uniquindio.proyectofinal.proyecto_finalp2.controller;

    opens co.edu.uniquindio.proyectofinal.proyecto_finalp2.model to javafx.base;
    exports co.edu.uniquindio.proyectofinal.proyecto_finalp2.model;
}