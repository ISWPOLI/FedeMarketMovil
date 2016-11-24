package qantica.com.fedemarket;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import qantica.com.bd.ManagerDescarga;
import qantica.com.conexion.Conexion;
import qantica.com.conexion.Descarga;
import qantica.com.conf.Mensaje;
import qantica.com.conf.RecursosRed;
import qantica.com.controles.ComentarioAdapter;
import qantica.com.controles.DialogoPopUp;
import qantica.com.controles.LoaderImageView;
import qantica.com.mundo.Comentario;
import qantica.com.mundo.DescargaItem;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class DescargaView extends Activity implements OnClickListener {

    // DESCARGAR LA IMAGEN DE LA CAPTURA SevletScreenCapture

    final DescargaView miActividad = this;
    private Bundle datos;
    private ImageButton botDescargar, botAtras, botHome, botApp, botComentar;
    private ImageView icoUpdate;
    private TextView lblTitulo, lblDescarga, lblDescripcion;
    private RatingBar rtnValoracion, rtnCalificacion;
    private EditText txtComentar;
    private ListView listComentario;
    private LinearLayout descarga_image, img_descarga;
    //private TextView lblNavegador;
    ArrayList<Comentario> misComentarios;

    private final Handler handler = new Handler();
    private Thread t;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.descarga_item);
        Resources res = getResources();
        datos = this.getIntent().getExtras();

        setControles();

        setListeners();

        listarCapturas();

        verificarActualizacion();

        listarComentario();

        SingletonActividad.getActividades().add(this);
    }

    private void verificarActualizacion() {

        DescargaItem contenido = ManagerDescarga.buscarContenido(
                datos.getString("aid"), DescargaView.this);

        if (contenido != null) {
            if (contenido.getVersion().equals(datos.getString("version"))) {
                botDescargar.setBackgroundResource(R.drawable.bot_instalada);
                botDescargar.setEnabled(false);
            } else {
                botDescargar.setBackgroundResource(R.drawable.bot_actualizar);
                icoUpdate.setVisibility(ImageView.VISIBLE);
            }
        }

    }

    private void listarCapturas() {
        LoaderImageView image;
        if (datos.getString("cap_1") != null&& !datos.getString("cap_1").equals("")) {
            image = new LoaderImageView(this, datos.getString("cap_1"),RecursosRed.URL_CAPTURAS);
            image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
            image.setBottom(10);
            descarga_image.addView(image);
        }
        if (datos.getString("cap_2") != null&& !datos.getString("cap_2").equals("")) {
            image = new LoaderImageView(this, datos.getString("cap_2"),RecursosRed.URL_CAPTURAS);
            image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
            image.setLeft(10);
            // image.setRight(10);
            descarga_image.addView(image);
        }

        if (datos.getString("icono") != null&& !datos.getString("icono").equals("")) {
            image = new LoaderImageView(this, datos.getString("icono"),RecursosRed.URL_CAPTURAS);
            image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
            image.setLeft(10);
            // image.setRight(10);
            img_descarga.addView(image);
        }
    }

    private void setControles() {
        botDescargar = (ImageButton) findViewById(R.id.bot_descargar);
        botAtras = (ImageButton) findViewById(R.id.btn_atras);
        botHome = (ImageButton) findViewById(R.id.btn_home);
        botApp = (ImageButton) findViewById(R.id.btn_apps);
        botComentar = (ImageButton) findViewById(R.id.btn_comentar);
        icoUpdate = (ImageView) findViewById(R.id.ico_update);
        icoUpdate.setVisibility(ImageView.INVISIBLE);

        listComentario = (ListView) findViewById(R.id.descarga_comentario_list);
        descarga_image = (LinearLayout) findViewById(R.id.descarga_image);
        img_descarga = (LinearLayout) findViewById(R.id.img_descarga);

        lblTitulo = (TextView) findViewById(R.id.label_titulo);
        lblDescarga = (TextView) findViewById(R.id.label_descargas);
        lblDescripcion = (TextView) findViewById(R.id.label_descripcion);
        //lblNavegador = (TextView) findViewById(R.id.lbl_navegador_descarga);

        rtnValoracion = (RatingBar) findViewById(R.id.rating_valoracion);
        rtnCalificacion = (RatingBar) findViewById(R.id.comentario_calificacion);

        txtComentar = (EditText) findViewById(R.id.comentario_descripcion);

        lblTitulo.setText(datos.getString("nombre"));
        //lblNavegador.setText("Descargas > " + datos.getString("nombre"));
        lblDescarga.setText(datos.getString("descargas") + " Descargas");
        lblDescripcion.setText(datos.getString("descripcion"));
        rtnValoracion.setRating((float) datos.getDouble("rtn"));
        rtnValoracion.setEnabled(false);
    }

    private void listarComentario() {
        final ProgressDialog dialogo = ProgressDialog.show(DescargaView.this,"", Mensaje.MSJ_PROCESANDO);
        t = new Thread(new Runnable() {
            public void run() {
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("aid", datos.getString("aid")));
                misComentarios = Conexion.listarComentarios(postParameters);
                handler.post(listar);
                dialogo.dismiss();
            }
        });
        t.start();
    }

    private void setListeners() {

        botDescargar.setOnClickListener(this);
        botHome.setOnClickListener(this);
        botAtras.setOnClickListener(this);
        botApp.setOnClickListener(this);
        botComentar.setOnClickListener(this);

    }

    private void enviarAtras() {
        finish();
    }

    private void enviarHome() {
        Intent intent = new Intent(DescargaView.this, InicioActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * handler para listar los comentarios
     */
    final Runnable listar = new Runnable() {
        public void run() {
            ComentarioAdapter adapter = new ComentarioAdapter(DescargaView.this, misComentarios);
            listComentario.setAdapter(adapter);
        }
    };

    /**
     * handler para listar los comentarios despues de comentar
     */

    final Runnable listarComentario = new Runnable() {
        public void run() {
            listarComentario();
        }
    };

    public void onClick(View v) {
        if (v == botDescargar) {
            DialogoPopUp dialogoPopUp = new DialogoPopUp(DescargaView.this,Mensaje.MJS_DESCARGANDO);
            dialogoPopUp.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Descarga.descargar(miActividad, DescargaView.this,datos.getString("aid"));
                        ManagerDescarga.adicionarDescarga(
                                new DescargaItem(Integer.parseInt(datos
                                        .getString("aid")), datos
                                        .getString("nombre"), datos
                                        .getString("version"), false, datos
                                        .getString("icono")), DescargaView.this);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        Log.w("Error en la descarga", "Error descargando");
                    }

                }
            }).start();

        } else if (v == botHome) {

            enviarHome();
        } else if (v == botAtras) {

            enviarAtras();
        } else if (v == botApp) {
            Intent intent = new Intent(DescargaView.this,
                    AplicacionActivity.class);
            startActivity(intent);
        } else if (v == botComentar) {

            if (txtComentar.getText().toString().length() > 4) {
                /**
                 * mandar los parametros que recibe el servlet
                 */
                final ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("id_app", datos.getString("aid")));
                postParameters.add(new BasicNameValuePair("valoracion", ""+ (int) rtnCalificacion.getRating()));
                postParameters.add(new BasicNameValuePair("uid", Singleton.getUid()));
                postParameters.add(new BasicNameValuePair("uname", Singleton.getUname()));
                postParameters.add(new BasicNameValuePair("comentario",txtComentar.getText().toString()));
                txtComentar.setText("");

                final ProgressDialog dialogo = ProgressDialog.show(DescargaView.this, "", "Cargando...");
                t = new Thread(new Runnable() {
                    public void run() {
                        try {
                            Conexion.comentarAplicacion(postParameters);
                            handler.post(listarComentario);
                            dialogo.dismiss();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.w("Error el comentario", "Error Comentario");
                        }
                    }
                });

                t.start();
            } else {
                DialogoPopUp dialogoPopUp = new DialogoPopUp(DescargaView.this,Mensaje.MSJ_COMENTARIO);
                dialogoPopUp.show();
            }
        }
    }
}
