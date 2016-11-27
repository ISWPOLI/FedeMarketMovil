package qantica.com.fedemarket;

import qantica.com.mundo.Categoria;
import qantica.com.mundo.Comentario;
import qantica.com.mundo.Contenido;
import qantica.com.mundo.Noticia;
import qantica.com.mundo.SubCategoria;

import java.util.ArrayList;

public class Singleton {

    private static final Singleton INSTANCIA = new Singleton();

    //Id del usuario
    private static String uid = "101010";

    //Nombre del usuario
    private static String uname = "@Usuario";

    //Apellido del usuario
    private static String ulname = "@LastName";

    //Rol del usuario
    private static String rol = "1";

    //Cedula
    private static String cedula = "";

    //Cadena de la busqueda que se va realizar
    private static String busqueda = "";

    //Id de la categoria que se va listar
    private static int cid = 0;

    //Id de la subcategoria que se va listar
    private static int sid = 0;

    //Atributo para verificar el estado de las conexiones
    private static boolean estado = true;

    //Atributo para verificar el estado de las animaciones
    private static boolean animacion = true;

    //Cadena de texto con el archivo descargado
    private static String urlArchivo = "";

    //Arreglo que contiene todos los contenidos
    private static ArrayList<Contenido> contenidos = new ArrayList<Contenido>();

    //Arreglo de los contenidos recomendados
    private static ArrayList<Contenido> recomendados = new ArrayList<Contenido>();

    //Arreglo que va contener los contenidos que se van a mostrar segun la categoria
    private static ArrayList<Contenido> presentacion = new ArrayList<Contenido>();

    //Arreglo de las categorias disponibles para la aplicacion
    private static ArrayList<Categoria> categorias = new ArrayList<Categoria>();

    //Arreglo de las subcategorias disponibles para la aplicacion
    private static ArrayList<SubCategoria> subcategorias = new ArrayList<SubCategoria>();

    //Arreglo de la lista de comentarios de un contenido
    private static ArrayList<Comentario> comentarios = new ArrayList<Comentario>();

    //Arreglo que va contener las noticias
    private static ArrayList<Noticia> noticias = new ArrayList<Noticia>();

    //Arreglo que va contener los resultados de las busquedas
    private static ArrayList<Object> resultados = new ArrayList<Object>();

    private Singleton() {
    }

    public static Singleton getInstancia() {
        return INSTANCIA;
    }

    public static void limpiar() {
        uid = "";
        uname = "";
        ulname = "";
        rol = "0";
        contenidos = new ArrayList<Contenido>();
        //recomendados = new ArrayList<Contenido>();
        presentacion = new ArrayList<Contenido>();
        categorias = new ArrayList<Categoria>();
        subcategorias = new ArrayList<SubCategoria>();
        comentarios = new ArrayList<Comentario>();
        noticias = new ArrayList<Noticia>();
        resultados = new ArrayList<Object>();
        busqueda = "";
        cid = 0;
        sid = 0;
        estado = true;
        animacion = true;
        urlArchivo = "";

    }

    public static void limpiarSubcategoria(){
        subcategorias = new ArrayList<SubCategoria>();
    }

    public static void limpiarNoticias(){
        noticias = new ArrayList<Noticia>();
    }

    //GETTERS AND SETTERS

    public static ArrayList<Object> getResultados() {
        return resultados;
    }

    public static void setResultados(ArrayList<Object> resultados) {
        Singleton.resultados = resultados;
    }

    public static ArrayList<Contenido> getContenidos() {
        return contenidos;
    }

    public static void setContenidos(ArrayList<Contenido> _contenidos) {
        Singleton.contenidos = _contenidos;
    }

    public static ArrayList<Contenido> getRecomendados() {
        return recomendados;
    }

    public static void setRecomendados(ArrayList<Contenido> _contenidos) {

        Singleton.recomendados = _contenidos;
    }

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        Singleton.uid = uid;
    }

    public static ArrayList<Categoria> getCategorias() {
        return categorias;
    }

    public static void setCategorias(ArrayList<Categoria> categorias) {
        Singleton.categorias = categorias;
    }

    public static ArrayList<SubCategoria> getSubcategorias() {
        return subcategorias;
    }

    public static void setSubcategorias(ArrayList<SubCategoria> subcategorias) {
        Singleton.subcategorias = subcategorias;
    }

    public static int getCid() {
        return cid;
    }

    public static void setCid(int cid) {
        Singleton.cid = cid;
    }

    public static ArrayList<Comentario> getComentarios() {
        return comentarios;
    }

    public static ArrayList<Contenido> getPresentacion() {
        return presentacion;
    }

    public static void setPresentacion(ArrayList<Contenido> presentacion) {
        Singleton.presentacion = presentacion;
    }

    public static void setComentarios(ArrayList<Comentario> comentarios) {
        Singleton.comentarios = comentarios;
    }

    public static String getUname() {
        return uname;
    }

    public static void setUname(String uname) {
        Singleton.uname = uname;
    }

    public static String getUlname() {
        return ulname;
    }

    public static void setUlname(String ulname) {
        Singleton.ulname = ulname;
    }

    public static String getRol() {
        return rol;
    }

    public static void setRol(String rol) {
        Singleton.rol = rol;
    }

    public static int getSid() {
        return sid;
    }

    public static void setSid(int sid) {
        Singleton.sid = sid;
    }

    public static String getBusqueda() {
        return busqueda;
    }

    public static void setBusqueda(String busqueda) {
        Singleton.busqueda = busqueda;
    }

    public static boolean isEstado() {
        return estado;
    }

    public static void setEstado(boolean estado) {
        Singleton.estado = estado;
    }

    public static ArrayList<Noticia> getNoticias() {
        return noticias;
    }

    public static void setNoticias(ArrayList<Noticia> noticias) {
        Singleton.noticias = noticias;
    }

    public static boolean isAnimacion() {
        return animacion;
    }

    public static void setAnimacion(boolean animacion) {
        Singleton.animacion = animacion;
    }

    public static String getUrlArchivo() {
        return urlArchivo;
    }

    public static void setUrlArchivo(String urlArchivo) {
        Singleton.urlArchivo = urlArchivo;
    }

    public static String getCedula() {
        return cedula;
    }

    public static void setCedula(String cedula) {
        Singleton.cedula = cedula;
    }


}
