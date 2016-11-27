package qantica.com.fedemarket;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import qantica.com.animacion.AnimacionAdapter;
import qantica.com.conexion.Conexion;
import qantica.com.conf.IdActividades;
import qantica.com.controles.BusquedaAdapter;
import qantica.com.controles.ContenidoAdapter;
import qantica.com.controles.ImageAdapter;
import qantica.com.controles.SubCategoriaAdapter;
import qantica.com.controles.SubCategoriaAdapterIndex;
import qantica.com.mundo.Categoria;
import qantica.com.mundo.Contenido;
import qantica.com.mundo.SubCategoria;

public class ContenidoActivity extends Activity implements OnClickListener {

    private TextView lblCategoria;
    private static Bundle datos;
    private ImageButton botAtras;
    private ImageButton botHome;
    private ImageButton botApp;
    private GridView gv;

    public static ArrayList<Contenido> contenido;
    public static ArrayList<SubCategoria> subcategorias;

    Activity app = this;
    Context context = ContenidoActivity.this;

    private ViewPager myPager;
    private final Handler handler = new Handler();
    private Thread t;
    private boolean animacion = true;
    private int contador = 0;
    public static boolean existSubc = false, isApp = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pantalla_categoria);

        datos = this.getIntent().getExtras();

        setControles();
        setListener();

        //Si no hay subcategorias, lista el contenido asociado a la categoria
        if (datos.getString("categoria").equals("No hay subcategorias") ||
                datos.getString("categoria").equals("content")) {
            isApp = true;
            lblCategoria.setText("Contenido disponible:");
            Log.e("ContenidoActivity",String.valueOf(Singleton.getInstancia().getContenidos().size()));
            try{
                gv.setAdapter(new ContenidoAdapter(this, Singleton.getInstancia().getContenidos()));
            }catch (Exception e){
                e.printStackTrace();
                Log.e("ContenidoActivityIni", "EXCEPCION PONIENDO EL CONTENIDO");
            }
            // Si hay subcategorias, las lista
        } else {
            existSubc = true;
            subcategorias = getSubcategoriasAvalaible();
            try {
                gv.setAdapter(new SubCategoriaAdapterIndex(this, subcategorias));
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ContenidoActivityIni", "EXCEPCION PONIENDO LAS SUBCATEGORIAS");
            }

        }

        //¿Qué hacer cuando se selecciona una subcategoria o un contenido?
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                //Si no hay subcategorias, mando a la pantalla de descripción de Aplicación
                if(!existSubc || isApp){
                    //Busco la aplicación para extraer los datos
                    Toast toast = Toast.makeText(ContenidoActivity.this, "Mandar a la pantalla de aplicación",Toast.LENGTH_SHORT);
                    toast.show();
                    getContenido(position);

                //Refresco esta pantalla con la lista de los contenidos
                }else{
                    SubCategoria subcategoria = Singleton.getInstancia().getSubcategorias().get(position);

                    String idSubcategoria= subcategoria.getId();

                    //Lista el contenido asociadas a la categoria que se le dio click
                    ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("categoria", String.valueOf(Singleton.getInstancia().getCid())));
                    param.add(new BasicNameValuePair("subcategoria",idSubcategoria));

                    Conexion.listarContenido(ContenidoActivity.this, param);

                    animacion = false;

                    //Si la respuesta del Servlet que lista las subcategorias fue 404
                    try{
                        if(Singleton.getInstancia().getContenidos().isEmpty()){
                            Intent intent = new Intent(ContenidoActivity.this, ContenidoActivity.class);
                            intent.putExtra("categoria",datos.getString("categoria"));
                            startActivity(intent);
                           // Toast toast = Toast.makeText(ContenidoActivity.this,"No hay contenido",Toast.LENGTH_SHORT);
                            //toast.show();
                        }else{
                            //Toast toast = Toast.makeText(ContenidoActivity.this,"Si hay contenido", Toast.LENGTH_SHORT);
                            //toast.show();

                            Conexion.listarContenido(ContenidoActivity.this,param);

                            Intent intent = new Intent(ContenidoActivity.this,ContenidoActivity.class);
                            intent.putExtra("categoria","content");
                            startActivityForResult(intent, IdActividades.CONTENIDO);
                        }
                    }catch (Exception e){
                        /*Intent intent = new Intent(InicioActivity.this, ContenidoActivity.class);
                        intent.putExtra("categoria","No hay subcategorias");
                        startActivity(intent);
                        //Toast toast = Toast.makeText(InicioActivity.this,"Vacio",Toast.LENGTH_SHORT);
                        //toast.show();*/
                        e.printStackTrace();
                    }

                }
            }
        });

        AnimacionAdapter adapter = new AnimacionAdapter(ContenidoActivity.this);
        myPager = (ViewPager) findViewById(R.id.banner_noticia_categoria);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(1);

        start();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Método encargado de gestionar la acción del botón "Back"
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if(Singleton.getInstancia().getSubcategorias() != null ) Singleton.getInstancia().getSubcategorias().clear();
            if(Singleton.getInstancia().getContenidos() != null) Singleton.getInstancia().getContenidos().clear();

            Singleton.getInstancia().setAnimacion(false);
            ArrayList<Activity> actividades = SingletonActividad.getInstancia().getActividades();
            Intent intent = new Intent(ContenidoActivity.this,InicioActivity.class);
            startActivity(intent);
            /*SingletonActividad.getInstancia().setActividades(new ArrayList<Activity>());
            for (int i = 0; i < actividades.size(); i++) {
                actividades.get(i).finish();
            }*/
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * Metodo encargado de gestionar el contenido disponible
     *
     * @return ArrayLisy con el contenido
     */
    private ArrayList<Contenido> getContentAvalaible() {
        ArrayList<Contenido> result = new ArrayList<Contenido>();
        for (int i = 0; i < Singleton.getInstancia().getContenidos().size(); i++) {
            if (Singleton.getInstancia().getContenidos().get(i).getEstado().equals("true")) {
                result.add(Singleton.getInstancia().getContenidos().get(i));
            }
        }
        //Organizar alfabéticamente
        Collections.sort(result);

        return result;
    }

    /**
     * Metodo encargado de gestionar las subcategorias disponibles
     *
     * @return ArrayList con el contenido
     */
    private ArrayList<SubCategoria> getSubcategoriasAvalaible() {
        ArrayList<SubCategoria> result = new ArrayList<SubCategoria>();
        for (int i = 0; i < Singleton.getInstancia().getSubcategorias().size(); i++) {
            result.add(Singleton.getInstancia().getSubcategorias().get(i));
        }
        //Organizar alfabéticamente
        //Collections.sort(result);

        return result;
    }

    private void setControles() {

        botAtras = (ImageButton) findViewById(R.id.btn_atras);
        botHome = (ImageButton) findViewById(R.id.btn_home);
        botApp = (ImageButton) findViewById(R.id.btn_apps);
        lblCategoria = (TextView) findViewById(R.id.lbl_busqueda);

        //grid view que contendra los resultados
        gv = (GridView) findViewById(R.id.lista_contenido);

        //verifica si es una busqueda para mostrar informacion
        if (Singleton.getInstancia().getCid() == -1) {
            gv.setAdapter(new BusquedaAdapter(context));
        } else {
            lblCategoria.setText(datos.getString("categoria"));
            gv.setAdapter(new ContenidoAdapter(context, contenido));
        }

    }

    private void setListener() {
        botApp.setOnClickListener(this);
        botAtras.setOnClickListener(this);
        botHome.setOnClickListener(this);

        //eventos de la cuadricula que contiene los contenidos
        gv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if (Singleton.getCid() != -1) {
                    getContenido(position);
                } else {
                    getBusqueda(position);
                }
            }
        });
    }

    private void start() {
        t = new Thread() {
            public void run() {
                try {
                    while (Singleton.isAnimacion()) {
                        contador++;
                        if (contador == 5) {
                            contador = 0;
                        }
                        Thread.sleep(5000);
                        handler.post(cambiarAnimacion);
                    }
                } catch (Exception e) {
                    Log.e("ContenidoActivity", "fed error en el retardo");
                }
            }
        };
        t.start();
    }

    final Runnable cambiarAnimacion = new Runnable() {
        public void run() {
            myPager.setCurrentItem(contador);
        }
    };

    public void listarSubcategoria(int categoria, int subcategoria, int seleccion) {
        gv.setAdapter(new SubCategoriaAdapter(context, categoria, subcategoria, seleccion));
    }

    public void limpiar() {
        gv.setAdapter(new ImageAdapter(ContenidoActivity.this, new ArrayList<Contenido>()));
    }

    private void enviarAtras() {
        finish();
    }

    private void enviarHome() {
        Intent intent = new Intent(ContenidoActivity.this, InicioActivity.class);
        startActivity(intent);
        finish();
    }

    public Drawable getIcon() {
        return getResources().getDrawable(R.drawable.fedecafe_style_navigation);

    }

    /**
     * Obtiene el contenido asociado a una subcategoria
     *
     * @param position
     */
    private void getContenido(int position) {

        Intent intent = new Intent(ContenidoActivity.this, DescargaView.class);

        intent.putExtra("nombre", Singleton.getInstancia().getPresentacion().get(position).getNombre());
        intent.putExtra("descargas", Singleton.getInstancia().getPresentacion().get(position).getDescargas());
        intent.putExtra("descripcion", Singleton.getInstancia().getPresentacion().get(position).getDescripcion());
        intent.putExtra("aid", Singleton.getInstancia().getPresentacion().get(position).getId());
        intent.putExtra("version", Singleton.getInstancia().getPresentacion().get(position).getVersion());
        intent.putExtra("cap_1", Singleton.getInstancia().getPresentacion().get(position).getCap_1());
        intent.putExtra("cap_2", Singleton.getInstancia().getPresentacion().get(position).getCap_2());
        intent.putExtra("version", Singleton.getInstancia().getPresentacion().get(position).getVersion());
        intent.putExtra("icono", Singleton.getInstancia().getPresentacion().get(position).getIcono());
        intent.putExtra("rtn", Singleton.getInstancia().getPresentacion().get(position).getRating());

        startActivity(intent);

    }

    private void getBusqueda(int position) {
        if (Singleton.getResultados().get(position) instanceof Contenido) {

            Contenido contenido = (Contenido) Singleton.getResultados().get(position);

            Intent intent = new Intent(ContenidoActivity.this, DescargaView.class);
            intent.putExtra("nombre", contenido.getNombre());
            intent.putExtra("descargas", contenido.getDescargas());
            intent.putExtra("descripcion", contenido.getDescripcion());
            intent.putExtra("aid", contenido.getId());
            intent.putExtra("version", contenido.getVersion());
            intent.putExtra("cap_1", contenido.getCap_1());
            intent.putExtra("cap_2", contenido.getCap_2());
            intent.putExtra("icono", contenido.getIcono());
            intent.putExtra("rtn", contenido.getRating());
            intent.putExtra("version", contenido.getVersion());
            startActivity(intent);

        } else {
            Categoria categoria = (Categoria) Singleton.getResultados().get(position);
            Singleton.setCid(Integer.parseInt(categoria.getId()));
            gv.setAdapter(new ImageAdapter(context, contenido));
        }
    }

    public void onClick(View v) {
        if (v == botHome) {
            enviarHome();
        } else if (v == botAtras) {
            enviarAtras();
        } else if (v == botApp) {
            Intent intent = new Intent(ContenidoActivity.this, AplicacionActivity.class);
            startActivity(intent);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Contenido Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}