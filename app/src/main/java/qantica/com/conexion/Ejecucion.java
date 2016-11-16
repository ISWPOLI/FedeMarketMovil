package qantica.com.conexion;


import android.util.Log;

import qantica.com.conf.RecursosRed;
import qantica.com.mundo.Actualizacion;
import qantica.com.mundo.Categoria;
import qantica.com.mundo.Comentario;
import qantica.com.mundo.Contenido;
import qantica.com.mundo.Noticia;
import qantica.com.mundo.SubCategoria;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import qantica.com.fedemarket.Singleton;

/**
 * En esta clase están todas las peticiones GET y POST hacia los Servlet
 * @author Juan Rubiano
 * Q-antica Ltda.
 * Colombia.
 * 18/08/2016
 */

public class Ejecucion {

    public static final int HTTP_TIMEOUT = 30 * 1000;
    private static HttpClient mHttpClient;

    static String aux = " ";

    /**
     * Obtiene una instancia del HttpClient
     * @return mHttpClient objeto HttpClient con los parámetros de la conexión
     */
    private static HttpClient getHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = new DefaultHttpClient();
            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);

        }
        return mHttpClient;
    }

    /**
     * HTTP Request Post para el login
     * @param postParameters parámetros que se envían en el request
     * @return El resultado del request
     * @throws Exception
     */
    public static String executeHttpPostLogin(ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader in = null;
        String url = RecursosRed.URL_LOGIN;
        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String result = sb.toString();
            return result;

        } catch (Exception e) {
            return "<400>" + e.getStackTrace();
        }
    }

    /**
     * Método encargado de listar las noticias de acuerdo al rol
     * @return ArrayList con las noticias
     * @throws Exception
     */
    public static ArrayList<Noticia> executeHttpPostNoticias(ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader in = null;
        String url = RecursosRed.URL_NOTICIA;
        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");

            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }

            in.close();

            String result = sb.toString();
            return ProcesoRespuesta.listarNoticias(result);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Método encargado de realizar la peticion para listar las categorias
     * @return arreglo con las categorias enviadas desde el Servlet
     * @throws Exception
     */
    public static ArrayList<Categoria> executeHttpPostCategoria(ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader in = null;
        String url = RecursosRed.URL_CATEGORIA;
        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();

            return ProcesoRespuesta.listarCategoria(result);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Método encargado actualizar las nuevas categorias
     * @param postParameters
     * @return
     * @throws Exception
     */
    public static ArrayList<Categoria> executeActualizarCategoria(
            ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader in = null;

        String url = RecursosRed.URL_ACTUALIZAR_CATEGORIA;

        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();

            return ProcesoRespuesta.listarCategoria(result);

        } catch (Exception e) {
            e.getStackTrace();
            Log.w("fed", "Error en el listado de categorias");
            return null;
        }
    }

    /**
     * Método encargado de listar y actualizar las subcategorias
     * @return ArrayList con las subcategorias
     * @throws Exception
     */
    public static ArrayList<SubCategoria> executeListSubCategoria(
            ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader in = null;

        String url = RecursosRed.URL_SUBCATEGORIA;

        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters);
            request.setEntity(formEntity);

            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();

            Singleton.setEstado(true);
            return ProcesoRespuesta.listarSubCategoria(result);

        } catch (Exception e) {

            Log.w("fed", "Error en el listado de subcategorias");
            Singleton.setEstado(false);
            return null;
        }
    }



    /**
     * metodo encargado de consumir el servicio para listar los contenidos
     *
     * @return
     * @throws Exception
     */
    public static ArrayList<Contenido> executeHttpPostContenido()
            throws Exception {
        BufferedReader in = null;

        String url = RecursosRed.URL_CONTENIDO;

        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();

            return ProcesoRespuesta.listarContenido(result);

        } catch (Exception e) {

            return null;
        }
    }

    /**
     * metodo encargado de consumir el servicio para listar los contenidos
     *
     * @return
     * @throws Exception
     */
    public static ArrayList<Actualizacion> executeActualizarContenido()
            throws Exception {

        BufferedReader in = null;

        String url = RecursosRed.URL_ACTUALIZAR_CONTENIDO;

        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);

            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");

            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }

            in.close();

            String result = sb.toString();

            return ProcesoRespuesta.actualizarContenido(result);

        } catch (Exception e) {

            Log.i("Error De Peticion", "" + e.getStackTrace());
            return null;
        }
    }

    /**
     * metodo encargado de listar los elementos destacados
     *
     * @return
     * @throws Exception
     */
    public static ArrayList<Contenido> executeHttpPostDestacado()
            throws Exception {
        BufferedReader in = null;

        String url = RecursosRed.URL_DESTACADO;

        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();

            return ProcesoRespuesta.listarContenido(result);

        } catch (Exception e) {

            Log.w("fed", "Error en el listado de destacados");
            return null;
        }
    }

    public static ArrayList<Comentario> listarComentarios(
            ArrayList<NameValuePair> postParameters) {

        BufferedReader in = null;

        String url = RecursosRed.URL_LIST_COMENTARIOS;

        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters);
            request.setEntity(formEntity);

            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();


            return ProcesoRespuesta.listarComentarios(result);

        } catch (Exception e) {

            return null;
        }
    }

    public static boolean executeComentarAplicacion(
            ArrayList<NameValuePair> postParameters) {

        BufferedReader in = null;

        String url = RecursosRed.URL_COMENTARIOS;

        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters);
            request.setEntity(formEntity);

            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();

            return ProcesoRespuesta.comentarAplicacion(result);

        } catch (Exception e) {

            e.getStackTrace();
            return false;
        }

    }

}
