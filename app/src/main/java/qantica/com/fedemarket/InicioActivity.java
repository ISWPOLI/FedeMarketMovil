package qantica.com.fedemarket;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import qantica.com.animacion.AnimacionAdapter;
import qantica.com.conexion.Conexion;
import qantica.com.conf.IdActividades;
import qantica.com.conf.RecursosRed;
import qantica.com.mundo.Categoria;
import qantica.com.mundo.Contenido;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import qantica.com.controles.CategoriaAdapterIndex;
import qantica.com.controles.LoaderIconCategoria;

/**
 * @author Juan Rubiano
 * 16/11/2016
 */
public class InicioActivity extends Activity implements	ViewPager.OnPageChangeListener, View.OnClickListener {

    public static ArrayList<Contenido> destacados = Singleton.getInstancia().getRecomendados();
    public static ArrayList<Categoria> categorias = Singleton.getInstancia().getCategorias();

    private TextView lbl_nombre;
    private EditText txtBusqueda;
    private Button btnCerrarSesion;
    private Button btnVerTodos;
    private ImageButton btnBuscar;
    private LinearLayout lista;

    private ViewPager myPager;

    private GridView gvc;

    private HorizontalScrollView hsv;

    private AnimacionAdapter adapter;
    private final Handler handler = new Handler();
    private Thread t;
    private boolean animacion = true;
    private int contador = 0;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pantalla_inicial);

        setControles();
        setListener();

        adapter = new AnimacionAdapter(InicioActivity.this);

        myPager = (ViewPager) findViewById(R.id.myfivepanelpager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);

        //Llena el GridView con las categorias
        try {
            gvc.setAdapter(new CategoriaAdapterIndex(this, categorias));
        } catch (Exception e) {
            Log.e("InicioActivity", "Categorias vacias");
        }

        //Listener para las categorias
        gvc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                Categoria categoria = Singleton.getInstancia().getCategorias().get(position);

                String idItem = categoria.getId();

                //Lista las subcategorias asociadas a la categoria que se le dio click
                ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("categoria",idItem));

                Conexion.listarSubCategorias(InicioActivity.this, param);

                animacion = false;

                //Si la respuesta del Servlet fue 404
                try{
                    if(Singleton.getInstancia().getSubcategorias() == null){
                        ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
                        param1.add(new BasicNameValuePair("categoria", idItem));
                        param1.add(new BasicNameValuePair("subcategoria","0"));


                        Conexion.listarContenido(InicioActivity.this,param1);

                        Intent intent = new Intent(InicioActivity.this, ContenidoActivity.class);
                        intent.putExtra("categoria","No hay subcategorias");
                        intent.putExtra("categoria2",categoria.getNombre());
                        intent.putExtra("idCategoria",categoria.getId());
                        startActivity(intent);
                        //Toast toast = Toast.makeText(InicioActivity.this,"No hay subcategorias... vacio!",Toast.LENGTH_SHORT);
                        //toast.show();
                    }else{
                        //Toast toast = Toast.makeText(InicioActivity.this,"Si hay subcategorias!", Toast.LENGTH_SHORT);
                        //toast.show();
                        Intent intent = new Intent(InicioActivity.this,ContenidoActivity.class);
                        Singleton.getInstancia().setCid(Integer.parseInt(idItem));
                        intent.putExtra("categoria", categoria.getNombre());
                        intent.putExtra("idCategoria",categoria.getId());
                        startActivityForResult(intent, IdActividades.INICIO);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    //Toast toast = Toast.makeText(InicioActivity.this,"EXCEPCIÓN!",Toast.LENGTH_SHORT);
                    //toast.show();
                }

            }
        });

        setDestacados();
        start();

        SingletonActividad.getInstancia().getActividades().add(this);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtBusqueda.getWindowToken(),0);
    }

    private void setControles() {

        txtBusqueda = (EditText) findViewById(R.id.texto_busqueda);
        btnCerrarSesion = (Button) findViewById(R.id.btn_cerrar_sesion);
        btnVerTodos = (Button) findViewById(R.id.btn_ver_todos);
        btnBuscar = (ImageButton) findViewById(R.id.btn_search);
        lbl_nombre = (TextView) findViewById(R.id.label_nombre);

        lbl_nombre.setText("Bienvenido, " + Singleton.getInstancia().getUname());
        gvc = (GridView) findViewById(R.id.list_categoria);
        //hsv = (HorizontalScrollView)findViewById(R.id.horizontal_cat);
    }

    private void setListener() {
        btnBuscar.setOnClickListener(this);
        btnVerTodos.setOnClickListener(this);
        btnCerrarSesion.setOnClickListener(this);
    }

    private void start() {
        t = new Thread() {
            public void run() {
                try {
                    myPager.addOnPageChangeListener(InicioActivity.this);
                    while (Singleton.getInstancia().isAnimacion()) {
                        Thread.sleep(7000);
                        contador++;
                        if (contador == 5) {
                            contador = 0;
                        }
                        handler.post(cambiarAnimacion);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("QanticaMarket", "Error animación noticias");
                }
            }
        };
        t.start();
    }

    final Runnable cambiarAnimacion = new Runnable() {
        public void run() {
            if (animacion) {
                myPager.setCurrentItem(contador);
            } else {
                animacion = true;
            }
        }
    };


    private void setDestacados() {
        lista = (LinearLayout) findViewById(R.id.layout_destacados);
        Context mContext = InicioActivity.this;
        for (int i = 0; i < destacados.size(); i++) {
            if (destacados.get(i).getEstado().equals("true")) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final int aux = i;
                View MyView = new View(InicioActivity.this);
                MyView = inflater.inflate(R.layout.item_destacado, null);
                MyView.setLayoutParams(new GridView.LayoutParams(300, 200));
                LinearLayout icon = (LinearLayout) MyView.findViewById(R.id.img_destacado);
                icon.setSoundEffectsEnabled(true);
                LoaderIconCategoria image = new LoaderIconCategoria(mContext, destacados.get(i).getIcono(),RecursosRed.URL_ICON_CATEGORIA);
      //          image.setLayoutParams(new ViewPager.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);
                icon.addView(image);
                TextView tv = (TextView) MyView.findViewById(R.id.lbl_destacado_titulo);
                tv.setText(destacados.get(i).getNombre());
                TextView tvd = (TextView) MyView.findViewById(R.id.lbl_destacado_descripcion);
                tvd.setText("");
                TextView tvx = (TextView) MyView.findViewById(R.id.lbl_destacado_descarga);
                tvx.setText(destacados.get(i).getDescargas() + " Descargas");
                RatingBar mr = (RatingBar) MyView.findViewById(R.id.rating_destacado);
                mr.setRating((float) destacados.get(i).getRating());
                mr.setEnabled(false);

                MyView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(InicioActivity.this, DescargaView.class);
                        intent.putExtra("nombre", destacados.get(aux).getNombre());
                        intent.putExtra("descargas", destacados.get(aux).getDescargas());
                        intent.putExtra("descripcion", destacados.get(aux).getDescripcion());
                        intent.putExtra("aid", destacados.get(aux).getId());
                        intent.putExtra("version", destacados.get(aux).getVersion());
                        intent.putExtra("cap_1", destacados.get(aux).getCap_1());
                        intent.putExtra("version", destacados.get(aux).getVersion());
                        intent.putExtra("cap_2", destacados.get(aux).getCap_2());
                        intent.putExtra("icono", destacados.get(aux).getIcono());
                        intent.putExtra("rtn", destacados.get(aux).getRating());
                        startActivityForResult(intent, IdActividades.INICIO);
                    }
                });
                lista.addView(MyView);
            }
        }
    }

    public void onPageScrollStateChanged(int arg0) {
        if (arg0 == 1) {
            animacion = false;
        }
    }

    public void onPageSelected(int arg0) {

    }

    /**
     * Método encargado de gestionar la acción del botón "Back"
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Singleton.getInstancia().limpiarNoticias();
            Singleton.getInstancia().setAnimacion(false);
            Intent intent = new Intent(InicioActivity.this, Login.class);
            startActivity(intent);

            /*ArrayList<Activity> actividades = SingletonActividad.getInstancia().getActividades();
            SingletonActividad.getInstancia().setActividades(new ArrayList<Activity>());
            for (int i = 0; i < actividades.size(); i++) {
                actividades.get(i).finish();
            }*/
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Método que gestiona las acciones de los botones en pantalla
     */
    public void onClick(View v) {
        if (v == btnBuscar) {
           /* Intent intent = new Intent(InicioActivity.this,ContenidoActivity.class);
            Singleton.setCid(-1);
            Singleton.setBusqueda(txtBusqueda.getText().toString());
            startActivityForResult(intent, IdActividades.INICIO);*/

        }else if (v == btnVerTodos) {

            /*Intent intent = new Intent(InicioActivity.this,ContenidoActivity.class);
            Singleton.setCid(0);
            intent.putExtra("categoria", "Todos Los Contenidos");
            startActivityForResult(intent, IdActividades.INICIO);*/

        } else if (v == btnCerrarSesion) {
            Intent intent = new Intent(InicioActivity.this, Login.class);
            startActivityForResult(intent, IdActividades.LOGIN);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}
