package com.qantica.conexion;


import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.qantica.bd.ManagerBD;
import com.qantica.mundo.Actualizacion;
import com.qantica.mundo.Categoria;
import com.qantica.mundo.Comentario;
import com.qantica.mundo.Contenido;
import com.qantica.mundo.SubCategoria;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

import qantica.com.fedemarket.Singleton;

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
            Log.e("QanticaMarket", "Error en Conexion");
            return false;
        }
    }

    /**
     * metodo encargado de listar las noticias
     */
    public static void listarNoticias(ArrayList<NameValuePair> postParameters) {
        try {
            Singleton.setNoticias(Ejecucion.executeHttpPostNoticias(postParameters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo encargado de actualizar la base de datos y adicionar nuevos
     * registros de contenidos para posteriormente utilizarlos dentro de la
     * aplicacion
     *
     * @param context
     */
    public static void buscarGaleria(Context context) {
        try {
            // lista los contenidos existentes en la base de datos
            ArrayList<Contenido> result = ManagerBD.listarContenido(context);
            // si no existen contenidos realiza la peticion al servidor para
            // obtenerlos y posteriormente guardarlos
            if (result.size() > 0) {

                // realiza la peticion al servidor para actualizar los datos de
                // los contenidos
                ArrayList<Actualizacion> misActualizacions = Ejecucion.executeActualizarContenido();

                // realiza la actualizacion de los registros existentes
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

                // verifica si el numero de actualizaciones es mayor al numero
                // de contenidos.
                // si es asi entonces lista los contenidos y adiciona los
                // faltantes
                if (result.size() < misActualizacions.size()) {

                    ArrayList<Contenido> resultados = Ejecucion.executeHttpPostContenido();

                    // inserta los contenidos recibidos del servidor a la bd
                    // local
                    for (int i = result.size(); i < resultados.size(); i++) {

                        ManagerBD
                                .adicionarContenido(resultados.get(i), context);
                    }
                }

                // asigna el resultado de los contenidos actualizados en la bd
                // local al arreglo que contiene los contenidos globale en el
                // singleton

                result = ManagerBD.listarContenido(context);
                Singleton.setContenidos(result);

            } else {

                // realiza la peticion al servidor
                result = Ejecucion.executeHttpPostContenido();

                // inserta los contenidos recibidos del servidor a la bd local
                for (int i = 0; i < result.size(); i++) {

                    ManagerBD.adicionarContenido(result.get(i), context);
                }

                // asigna los contenidos al arreglo que contendra los valores
                // globales
                Singleton.setContenidos(result);

            }

            Singleton.setEstado(true);
        } catch (Exception e) {
            e.printStackTrace();

            Singleton.setEstado(false);
            Log.w("fed", "Error en el listado de contenidos");

        }
    }

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
            Singleton.setCategorias(result);
        } catch (Exception e) {
            e.printStackTrace();
            Singleton.setEstado(false);
            Log.w("QANTICAMARKET", "Error en el listado de categorias");
        }
    }

    /**
     * Lista las subcategorias de acuerdo al rol
     * @param context
     * @param postParameters parámetros de la url
     */
    public static void listarSubCategorias(Context context, ArrayList<NameValuePair> postParameters) {
        try {
			/*ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("size", "0"));*/

            ArrayList<SubCategoria> actualizar = Ejecucion.executeListSubCategoria(postParameters);

            Singleton.setSubcategorias(actualizar);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.w("QANTICAMARKET", "Error subcategorias");
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

