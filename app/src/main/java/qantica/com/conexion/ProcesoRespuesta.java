package qantica.com.conexion;

import android.util.Log;

import qantica.com.mundo.Actualizacion;
import qantica.com.mundo.Categoria;
import qantica.com.mundo.Comentario;
import qantica.com.mundo.Contenido;
import qantica.com.mundo.Noticia;
import qantica.com.mundo.SubCategoria;

import java.util.ArrayList;

import qantica.com.fedemarket.Singleton;


/**
 * Este archivo procesa todas las respuestas de los Servlet
 * @author Juan Rubiano
 * Q-antica Ltda.
 * Colombia
 * 18/08/2016
 */
public class ProcesoRespuesta {

    /**
     * Procesa la respuesta de ServletLogin
     * @param cadena respuesta del servlet
     * @return true si el login es válido, false si es inválido
     */
    public static boolean login(String cadena) {
        String login = "";
        login.concat(String.valueOf(cadena.charAt(0))).concat(String.valueOf(cadena.charAt(1))).
                concat(String.valueOf(cadena.charAt(2))).concat(String.valueOf(cadena.charAt(3)));
        Log.e("LOGIN -> login ", login);
        if(cadena.contains("false") || cadena.contains("<400>")){
            Singleton.setUname("NoServer");
            return false;
        }else if (cadena.contains("true")){// == "true"){
            getDatos(cadena);
        }
        return true;
    }

    /**
     * Método que procesa el resultado del Servlet y asigna al Singleton
     * los atributos
     * @param cadena que viene del Servlet
     */
    private static void getDatos(String cadena) {
        String resultadoServlet = cadena;
        String extraer = "";
        char[] resultadoServlet1 = resultadoServlet.toCharArray();
        int contSepa = 0;
        for (int i = 0; i < resultadoServlet1.length; i++) {
            if(resultadoServlet.charAt(i) == '|'){
                contSepa++;
                switch (contSepa) {
                    case 0:
                        break;
                    case 1:
                        //¿Se logeo?
                        break;
                    case 2:
                        Singleton.setUid(extraer);
                        break;
                    case 3:
                        Singleton.setUname(extraer);
                        break;
                    case 4:
                        Singleton.setCedula(extraer);
                        break;
                    case 5:
                        Singleton.setRol(extraer);
                        break;
                    case 6:
                        Singleton.setUlname(extraer);
                        break;
                }
                extraer = "";
            }else{
                extraer += resultadoServlet.charAt(i);

            }
        }
    }



    /**
     * Procesa la respuesta que viene del Servlet
     * @param cadena resultado del Servlet
     * @return arreglo con el resultado del procesamiento.
     */
    public static ArrayList<Categoria> listarCategoria(String cadena) {
        ArrayList<Categoria> resultado = null;
        if (!(cadena.contains("<404>"))){

            ArrayList<Categoria> misCategorias = new ArrayList<Categoria>();

            int contador = 0;
            String aux = "";
            String id = "";
            String nombre = "";

            for (int i = 0; i < cadena.length(); i++) {
                if (cadena.charAt(i) == '|') {
                    switch (contador) {
                        case 0:
                            id = aux;
                            aux = "";
                            break;
                        case 1:
                            nombre = aux;
                            aux = "";
                            break;
                        default:
                            break;
                    }
                    contador++;
                } else if (cadena.charAt(i) == '>') {
                    Categoria miCategoria = new Categoria(id, nombre, aux);
                    misCategorias.add(miCategoria);
                    aux = "";
                    contador = 0;
                } else {
                    if (cadena.charAt(i) != '\n') {
                        aux += cadena.charAt(i);
                    }
                }
            }
            resultado = misCategorias;
        }
        return resultado;
    }

    /**
     * Procesa la respuesta enviada desde el request
     * @param cadena
     * @return ArrayList con las noticias
     */
    public static ArrayList<Noticia> listarNoticias(String cadena) {

        ArrayList<Noticia> misNoticias = new ArrayList<Noticia>();

        int contador = 0;
        String aux = "";

        String titulo = "";
        String contenido = "";

        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) == '|') {
                switch (contador) {
                    case 0:
                        titulo = aux;
                        aux = "";
                        break;
                    case 1:
                        contenido = aux;
                        aux = "";
                        break;
                    default:
                        break;
                }
                contador++;
            } else if (cadena.charAt(i) == '>') {
                Noticia miItem = new Noticia();
                miItem.setTitulo(titulo);
                miItem.setContenido(contenido);
                miItem.setFecha(aux);
                misNoticias.add(miItem);
                aux = "";
                contador = 0;
            } else {
                if (cadena.charAt(i) != '\n') {
                    aux += cadena.charAt(i);
                }
            }

        }
        return misNoticias;
    }

    public static ArrayList<SubCategoria> listarSubCategoria(String cadena) {
        Log.e("ProcesoRespuesta" ,cadena);
        if (cadena.equals("<404>\n")) {
            return null;
        }else {
            ArrayList<SubCategoria> misCategorias = new ArrayList<SubCategoria>();

            int contador = 0;
            String aux = "";

            String id = "";
            String nombre = "";
            String categoria = "";
            String estado = "";
            String icono = "";

            for (int i = 0; i < cadena.length(); i++) {
                if (cadena.charAt(i) == '|') {
                    switch (contador) {
                        case 0:
                            id = aux;
                            aux = "";
                            break;
                        case 1:
                            nombre = aux;
                            aux = "";
                            break;
                        case 2:
                            aux = "";
                            break;
                        case 3:
                            categoria = aux;
                            aux = "";
                            break;
                        case 4:
                            icono = aux;
                            aux = "";
                            break;
                        default:
                            break;
                    }
                    contador++;
                } else if (cadena.charAt(i) == '>') {
                    if(categoria == "") categoria = "0";

                    SubCategoria miItem = new SubCategoria(id, nombre,Integer.valueOf(categoria),0,estado,icono);
                    misCategorias.add(miItem);

                    aux = "";
                    contador = 0;
                } else {
                    if (cadena.charAt(i) != '\n') {
                        aux = aux + cadena.charAt(i);
                    }
                }

            }
            return misCategorias;
        }
    }

    /**
     * procesa la respuesta y llena un array con los items para actualizar
     */
    public static ArrayList<Actualizacion> actualizarContenido(String cadena) {

        ArrayList<Actualizacion> misActualizaciones = new ArrayList<Actualizacion>();

        int contador = 0;
        String aux = "";
        String id = "", rating = "", descarga = "", version="", nombre="", estado="",descripcion="",cap_1="",cap_2="";

        for (int i = 0; i < cadena.length(); i++) {

            if (cadena.charAt(i) == '|') {

                switch (contador) {
                    case 0:
                        id = aux;
                        aux = "";
                        break;
                    case 1:
                        descarga = aux;
                        aux = "";
                        break;
                    case 2:
                        rating = aux;
                        aux = "";
                        break;
                    case 3:
                        version= aux;
                        aux="";
                        break;
                    case 4:
                        nombre=aux;
                        aux="";
                        break;
                    case 5:
                        estado=aux;
                        aux="";
                        break;
                    case 6:
                        descripcion=aux;
                        aux="";
                        break;
                    case 7:
                        cap_1=aux;
                        aux="";
                        break;
                    case 8:
                        cap_2=aux;
                        aux="";
                        break;
                    default:
                        break;
                }
                contador++;
            } else {

                if (cadena.charAt(i) == '>') {

                    Actualizacion miActualizacion = new Actualizacion();
                    miActualizacion.setId(Integer.parseInt(id));
                    miActualizacion.setDescarga(Integer.parseInt(descarga));
                    miActualizacion.setRating(Double.parseDouble(rating));
                    miActualizacion.setVersion(version);
                    miActualizacion.setNombre(nombre);
                    miActualizacion.setEstado(estado);
                    miActualizacion.setDescripcion(descripcion);
                    miActualizacion.setCap_1(cap_1);
                    miActualizacion.setCap_2(cap_2);
                    miActualizacion.setIco(aux);

                    misActualizaciones.add(miActualizacion);
                    aux = "";
                    contador = 0;

                } else {
                    if (cadena.charAt(i) != '\n') {
                        aux = aux + cadena.charAt(i);
                    }
                }

            }
        }

        return misActualizaciones;
    }

    /**
     * procesa la respuesta y lista los contenidos disponibles
     *
     * @param cadena
     * @return
     */
    public static ArrayList<Contenido> listarContenido(String cadena) {

        ArrayList<Contenido> misContenidos = new ArrayList<Contenido>();

        int contador = 0;
        String aux = "";

        String id = "", nombre = "", descargas = "", descripcion = "", categoria = "", version = "", subcategoria = "", cap_1 = "", cap_2 = "", icono = "", rating="";

        for (int i = 0; i < cadena.length(); i++) {

            if (cadena.charAt(i) == '|') {

                switch (contador) {
                    case 0:
                        id = aux;
                        aux = "";
                        break;
                    case 1:
                        nombre = aux;
                        aux = "";
                        break;
                    case 2:

                        descripcion = aux;
                        aux = "";
                        break;
                    case 3:
                        descargas = aux;
                        aux = "";
                        break;
                    case 4:
                        version = aux;
                        aux = "";
                        break;
                    case 5:
                        categoria = aux;
                        aux = "";
                        break;
                    case 6:
                        subcategoria = aux;
                        aux = "";
                        break;
                    case 7:
                        cap_1 = aux;
                        aux = "";
                        break;
                    case 8:
                        cap_2 = aux;
                        aux = "";
                        break;
                    case 9:
                        icono = aux;
                        aux = "";
                        break;
                    case 10:
                        rating=aux;
                        aux="";
                        break;
                    default:
                        break;
                }

                contador++;
            } else if (cadena.charAt(i) == '>') {

                Contenido miItem = new Contenido();
                miItem.setId(id);
                miItem.setNombre(nombre);
                miItem.setDescargas(descargas);
                miItem.setDescripcion(descripcion);
                miItem.setCategoria(Integer.parseInt(categoria));
                miItem.setSubcategoria(Integer.parseInt(subcategoria));
                miItem.setCap_1(cap_1);
                miItem.setCap_2(cap_2);
                miItem.setIcono(icono);
                miItem.setVersion(version);
                miItem.setRating(Double.parseDouble(rating));
                miItem.setEstado(aux);

                misContenidos.add(miItem);
                aux = "";
                contador = 0;
            } else {
                if (cadena.charAt(i) != '\n') {
                    aux = aux + cadena.charAt(i);
                }
            }

        }

        return misContenidos;
    }



    public static ArrayList<Comentario> listarComentarios(String cadena) {

        ArrayList<Comentario> misComentario = new ArrayList<Comentario>();

        int contador = 0;
        String aux = "";

        String usuario = "", descripcion = "";

        for (int i = 0; i < cadena.length(); i++) {

            if (cadena.charAt(i) == '|') {

                switch (contador) {
                    case 0:
                        usuario = aux;
                        aux = "";
                        break;
                    case 1:
                        descripcion = aux;
                        aux = "";
                        break;

                    default:
                        break;
                }

                contador++;
            } else if (cadena.charAt(i) == '>') {

                Comentario miItem = new Comentario(usuario, descripcion,
                        Integer.parseInt(aux));
                misComentario.add(miItem);

                aux = "";
                contador = 0;
            } else {
                if (cadena.charAt(i) != '\n') {
                    aux = aux + cadena.charAt(i);
                }
            }

        }

        return misComentario;
    }

    public static boolean comentarAplicacion(String result) {

        String auxiliar = "";

        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) != '\n') {
                auxiliar = auxiliar + result.charAt(i);
            }
        }

        if (auxiliar.equals("<010>")) {
            return true;
        }

        return false;
    }

}
