package co.edu.uniquindio.proyectofinal.proyecto_finalp2.utils;

public class MarketPlaceConstantes {
    // Configuración general
    public static final String TITULO_APP = "MarketPlace Social";
    public static final String VERSION = "1.0.0";
    public static final int MAX_CONTACTOS = 10;

    // Rutas de vistas FXML
    public static final String VISTA_LOGIN = "/co/edu/uniquindio/proyectofinal/proyecto_finalp2/fxml/Login.fxml";
    public static final String VISTA_PRINCIPAL = "/co/edu/uniquindio/proyectofinal/proyecto_finalp2/fxml/MarketPlaceApp.fxml";
    public static final String VISTA_ADMINISTRADOR = "/co/edu/uniquindio/proyectofinal/proyecto_finalp2/fxml/Administrador.fxml";
    public static final String VISTA_VENDEDOR = "/co/edu/uniquindio/proyectofinal/proyecto_finalp2/fxml/Vendedor.fxml";

    // Mensajes de error
    public static final String ERROR_LOGIN = "Usuario o contraseña incorrectos";
    public static final String ERROR_CAMPOS_VACIOS = "Todos los campos son obligatorios";
    public static final String ERROR_MAX_CONTACTOS = "Has alcanzado el límite de contactos permitidos";
    public static final String ERROR_PRODUCTO_INVALIDO = "Los datos del producto son inválidos";
    public static final String ERROR_PERMISO_DENEGADO = "No tienes permiso para realizar esta acción";
    public static final String ERROR_CARGAR_VISTA = "Error al cargar la vista";
    public static final String ERROR_GUARDAR_DATOS = "Error al guardar los datos";

    // Mensajes de éxito
    public static final String EXITO_GUARDAR = "Datos guardados exitosamente";
    public static final String EXITO_ELIMINAR = "Elemento eliminado exitosamente";
    public static final String EXITO_ACTUALIZAR = "Datos actualizados exitosamente";
    public static final String EXITO_PUBLICAR = "Producto publicado exitosamente";
    public static final String EXITO_CONTACTO = "Contacto agregado exitosamente";

    // Tipos de reportes
    public static final String REPORTE_VENDEDORES = "VENDEDORES";
    public static final String REPORTE_PRODUCTOS = "PRODUCTOS";
    public static final String REPORTE_INTERACCIONES = "INTERACCIONES";

    // Formato de fechas
    public static final String FORMATO_FECHA = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMATO_FECHA_CORTO = "dd/MM/yyyy";

    // Estilos CSS
    public static final String ESTILO_PRINCIPAL = "/co/edu/uniquindio/proyectofinal/proyecto_finalp2/css/styles.css";
    public static final String ESTILO_LOGIN = "/co/edu/uniquindio/proyectofinal/proyecto_finalp2/css/login.css";
}