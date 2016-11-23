package qantica.com.conexion;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import qantica.com.bd.ManagerBD;
import qantica.com.mundo.Actualizacion;
import qantica.com.mundo.Categoria;
import qantica.com.mundo.Comentario;
import qantica.com.mundo.Contenido;
import qantica.com.mundo.SubCategoria;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

import qantica.com.fedemarket.Singleton;

/**
 * Realiza la petición al Servlet
 * @author Juan Rubiano
 * 15/11/2016
 */
public class Conexion {

    Resources res;

    public Conexion(Resources res) {
        super();
        this.res = res;
    }

    public static boolean verificarLogin(ArrayList<NameValuePair> postParameters) {
        try {
            return ProcesoRespuesta.login(Ejecucion.executeHttpPostLogin(postParameters));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Conexion.java", "Error en Conexion");
            return false;
        }
    }

    /**
     * Lista las noticias
     * @param postParameters parámetros que se pasaran por url
     */
    public static void listarNoticias(ArrayList<NameValuePair> postParameters) {
        try {
            Singleton.setNoticias(Ejecucion.executeHttpPostNoticias(postParameters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualización de los datos
     * @param context

    public static void buscarGaleria(Context context) {
        try {
            ArrayList<Contenido> result = ManagerBD.listarContenido(context);
            if (result.size() > 0) {
                ArrayList<Actualizacion> misActualizacions = Ejecucion.executeActualizarContenido();
                for (int i = 0; i < misActualizacions.size(); i++) {

                    ManagerBD.updateContenido(misActualizacions.get(i)
                                    .getRating(), misActualizacions.get(i)
                                    .getDescarga(), misActualizacions.get(i).getId(),
                            misActualizacions.get(i).getVersion(),
                            misActualizacions.get(i).getNombre(),
                            misActualizacions.get(i).getEstado(),
                            misActualizacions.get(i).getDescripcion(),
                            misActualizacions.get(i).getCap_1(),
                            misActualizacions.get(i).getCap_2(),
                            misActualizacions.get(i).getIco(), context);
                }

                if (result.size() < misActualizacions.size()) {
                    ArrayList<Contenido> resultados = Ejecucion.executeHttpPostContenido();
                    for (int i = result.size(); i < resultados.size(); i++) {
                        ManagerBD.adicionarContenido(resultados.get(i), context);
                    }
                }
                result = ManagerBD.listarContenido(context);
                Singleton.setContenidos(result);
            } else {
                result = Ejecucion.executeHttpPostContenido();
                for (int i = 0; i < result.size(); i++) {
                    ManagerBD.adicionarContenido(result.get(i), context);
                }
                Singleton.setContenidos(result);

            }

            Singleton.setEstado(true);
        } catch (Exception e) {
            e.printStackTrace();

            Singleton.setEstado(false);
            Log.w("fed", "Error en el listado de contenidos");

        }
    }*/

    /**
     * realiza la busqueda en el servidor de los contenidos mas descargados
     *
     * @return
     */
    public static ArrayList<Contenido> buscarDestacados() {
        try {
            Singleton.setRecomendados(new ArrayList<Contenido>());
            return Ejecucion.executeHttpPostDestacado();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista las categorias existentes de acuerdo al rol
     * @param context
     * @param postParameters parametros de la url
     */
    public static void listarCategorias(Context context, ArrayList<NameValuePair> postParameters) {
        try {
            ArrayList<Categoria> result = Ejecucion.executeHttpPostCategoria(postParameters);
            Singleton.getInstancia().setCategorias(result);
        } catch (Exception e) {
            e.printStackTrace();
            Singleton.getInstancia().setEstado(false);
            Log.w("Conexion.java", "Error en el listado de categorias");
        }
    }

    /**
     * Lista los contenidos de acuerdo a la subcategoria y categoria
     * @param context Contexto en el que se encuentra la aplicación
     * @param postParameters Parámetros que se envian al server
     */
    public static void listarContenido(Context context, ArrayList<NameValuePair> postParameters) {
        try {
            for (int i = 0; i < postParameters.size(); i++) {
                Log.e("Conexion","Parametro Contenido: "+String.valueOf(postParameters.get(i)));
            }
            ArrayList<Contenido> result = Ejecucion.executeHttpPostContenido(postParameters);
            Singleton.getInstancia().setContenidos(result);
        } catch (Exception e) {
            e.printStackTrace();
            Singleton.getInstancia().setEstado(false);
            Log.w("Conexion.java", "Error en el listado de contenido");
        }
    }

    /**
     * Lista las subcategorias de acuerdo a la categoria
     * @param context
     * @param postParameters parámetros de la url
     */
    public static void listarSubCategorias(Context context, ArrayList<NameValuePair> postParameters) {
        try {
            ArrayList<SubCategoria> actualizar = Ejecucion.executeListSubCategoria(postParameters);

            Singleton.getInstancia().setSubcategorias(actualizar);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.w("Conexión", "Error subcategorias");
            Singleton.setEstado(false);
        }

    }

    public static ArrayList<Comentario> listarComentarios(
            ArrayList<NameValuePair> postParameters) {

        try {

            return Ejecucion.listarComentarios(postParameters);

        } catch (Exception e) {
            Singleton.setEstado(false);
            // TODO: handle exception
        }

        return null;
    }

    public static boolean comentarAplicacion(
            ArrayList<NameValuePair> postParameters) {

        try {

            return Ejecucion.executeComentarAplicacion(postParameters);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

}

