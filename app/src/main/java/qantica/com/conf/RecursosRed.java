package qantica.com.conf;

/**
 * @author Juan Rubiano
 * 15/09/16
 */
public class RecursosRed {

    //public static String DOMINIO = "http://190.85.219.101:8080/FedeMarket/";
    public static String DOMINIO = "http://190.85.219.101:8080/fedecafe/";

    //Servlet que contiene la funcionalidad del Login
    public static String URL_LOGIN = DOMINIO + "ServletLogin";

    //Servlet que iserta el ingreso del usuario
    public static String URL_INGRESO = DOMINIO + "ServletAcceso";

    //Servlet que contiene la funcionalidad para listar Noticias
    public static String URL_NOTICIA = DOMINIO + "ServletNoticias";

    //Servlet que contiene la funcionalidad para listar los contenidos disponibles
    public static String URL_CONTENIDO = DOMINIO + "ServletContenido";

    //Servlet que contiene la funcionalidad para actualizar Contenido
    public static String URL_ACTUALIZAR_CONTENIDO = DOMINIO	+ "ServletActualizar";

    //Servlet que contiene la funcionalidad para actualizar Categorias
    public static String URL_ACTUALIZAR_CATEGORIA = DOMINIO	+ "ServletActualizarCategoria";

    //Servlet que contiene la funcionalidad para listar el contenido destacado
    public static String URL_DESTACADO = DOMINIO + "ServletDestacados";

    //Servlet que contiene la funcionalidad para listar las categorias disponibles
    public static String URL_CATEGORIA = DOMINIO + "ServletCategoria";

    //Servlet que contiene la funcionalidad para listar subcategorias
    public static String URL_SUBCATEGORIA = DOMINIO + "ServletSubCategoria";

    //Servlet que permite la descarga
    public static String URL_DESCARGA = DOMINIO + "ServletDescarga";

    //Servlet que contiene los comentarios
    public static String URL_COMENTARIOS = DOMINIO + "ServletComentario";

    //Servlet que lista los comentarios
    public static String URL_LIST_COMENTARIOS = DOMINIO	+ "ServletListaComentario";

    //Servlet que tiene la funcionalidad para las capturas de pantalla
    public static String URL_CAPTURAS = DOMINIO + "ServletScreenCapture";

    //Servlet que contiene la funcionalidad para los íconos
    public static String URL_ICON_CATEGORIA = DOMINIO + "ServletIconCategoria";
}
