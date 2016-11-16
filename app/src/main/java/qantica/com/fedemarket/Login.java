package qantica.com.fedemarket;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import qantica.com.conexion.Conexion;
import qantica.com.conf.Mensaje;
import qantica.com.conf.RecursosRed;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;

import qantica.com.controles.DialogoPopUp;

public class Login extends Activity implements View.OnClickListener {

    boolean registrado = false;
    String resultado = "a";
    public Context contexto = this;
    private ImageButton login;
    private CheckBox check;
    private EditText nombreUsuario;
    private EditText contrasena;

    private int contador = 0;

    private Handler handler = new Handler();

    public Login() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        if(contador != 0){
            onResume();
            onPause();
            onStop();
            onStart();
            onResume();
        }else{
            //onCreateDialog(contador);
        }

        crearCarpeta();
        Singleton.getInstancia().setAnimacion(true);

        if (!networkAvailable()) {
            mostrarDialogo(Mensaje.MSJ_NO_INTERNET);
        }

        setControles();
        setListeners();
        SingletonActividad.getInstancia().getActividades().add(this);
    }

    /**
     * Método encargado de referenciar los atributos del layout
     */
    private void setControles() {
        login = (ImageButton) findViewById(R.id.botIniciarSesion);
        check = (CheckBox) findViewById(R.id.checkBox1);
        nombreUsuario = (EditText) findViewById(R.id.nombreUsuario);
        contrasena = (EditText) findViewById(R.id.contrasena);

        //Verificación del checkbox de almacenamiento de la contraseña
        SharedPreferences prefs = getSharedPreferences("InformacionPersonal",Context.MODE_PRIVATE);
        if (prefs.getBoolean("check", false)) {
            nombreUsuario.setText(prefs.getString("user", ""));
            contrasena.setText(prefs.getString("pass", ""));
            check.setChecked(true);
        }
    }

    /**
     * Método encargado de agregar los listener a los botones
     */
    private void setListeners() {
        login.setOnClickListener(this);
    }

    /**
     * Verifica si existe las carpetas
     */
    private void crearCarpeta() {
        File arc = new File("sdcard/Qantica Market");
        if (!arc.isDirectory()) {
            arc.mkdirs();
        }
    }


    /**
     * Verifica la conexión a internet
     * @return true si hay conexión, false si no
     */
    public boolean networkAvailable() {
        Context context = getApplicationContext();
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectMgr != null) {
            NetworkInfo[] netInfo = connectMgr.getAllNetworkInfo();
            if (netInfo != null) {
                for (NetworkInfo net : netInfo) {
                    if (net.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } else {
            Log.d("NETWORK", "No hay conexión a internet");
        }
        return false;
    }

    /**
     * Notificación al usuario no hay internet.
     * @param mensaje que se va a mostrar al usuario
     */
    public void mostrarDialogo(String mensaje) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Error de Conexión");
        alertDialogBuilder.setMessage(mensaje).setCancelable(false).setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Si el botón es seleccionado se cierra
                        dialog.cancel();
                        Login.this.finish();
                    }
                });
        // Creación de la ventana.
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Método encargado de gestionar la acción del botón "Back"
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Singleton.getInstancia().limpiar();
            Singleton.getInstancia().setAnimacion(false);
            ArrayList<Activity> actividades = SingletonActividad.getActividades();
            SingletonActividad.getInstancia().setActividades(new ArrayList<Activity>());
            for (int i = 0; i < actividades.size(); i++) {
                actividades.get(i).finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Método encargado de gestionar los eventos de los botones
     */
    public void onClick(View v) {
        if (v == login) {
            if (check.isChecked()
                    && !nombreUsuario.getText().toString().equals("")
                    && !contrasena.getText().toString().equals("")) {
                SharedPreferences prefs = getSharedPreferences("InformacionPersonal", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user", nombreUsuario.getText().toString());
                editor.putString("pass", contrasena.getText().toString());
                editor.putBoolean("check", true);
                editor.commit();
            } else {
                SharedPreferences prefs = getSharedPreferences("InformacionPersonal", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user", "");
                editor.putString("pass", "");
                editor.putBoolean("check", false);
                editor.commit();
            }

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setMessage(Mensaje.MSJ_PROCESANDO);
            dialog.show();

            contador = 1;
            if (Singleton.getInstancia().isEstado()) {
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            Singleton.getInstancia().limpiar();
                            ArrayList<NameValuePair> paramLogin = new ArrayList<NameValuePair>();
                            ArrayList<NameValuePair> paramNoticias = new ArrayList<NameValuePair>();
                            paramLogin.add(new BasicNameValuePair("nombre_usuario",nombreUsuario.getText().toString()));
                            paramLogin.add(new BasicNameValuePair("contrasena",contrasena.getText().toString()));
                            Log.e("QANTICAMARKET", "Se intentará conectar a: "+ RecursosRed.DOMINIO);
                            if (Conexion.verificarLogin(paramLogin)) {
                                paramNoticias.add(new BasicNameValuePair("rol", Singleton.getInstancia().getRol()));
                                Conexion.listarNoticias(paramNoticias);
                                Conexion.listarCategorias(Login.this, paramNoticias);
                                Conexion.listarSubCategorias(Login.this, paramNoticias);
                               //Singleton.getInstancia().setRecomendados(Conexion.buscarDestacados());
                                //Conexion.buscarGaleria(Login.this);
                                Intent intent = new Intent(Login.this,InicioActivity.class);
                                //Toast toast = Toast.makeText(Login.this,"Entro", Toast.LENGTH_LONG);
                                //toast.show();

                                if(!Singleton.getInstancia().isEstado()){
                                    registrado = false;
                                }else{
                                    startActivity(intent);
                                    registrado = true;
                                }
                                onPause();
                                onStop();
                                handler.post(cambiarAnimacion);
                                dialog.cancel();
                            } else {
                                registrado = false;
                                handler.post(cambiarAnimacion);
                                dialog.cancel();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                mostrarDialogo(Mensaje.MSJ_AUTENTICACION);
            }
        }
    }

    final Runnable cambiarAnimacion = new Runnable() {
        public void run() {
            if (!registrado) {
                if(!Singleton.getInstancia().isEstado()){
                    DialogoPopUp dialogoPopUp = new DialogoPopUp(Login.this,"!Singleton.isEstado()");
                    dialogoPopUp.show();
                }else if (Singleton.getInstancia().getUname().equals("NoServer")) {
                    Toast toast = new Toast(getApplicationContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.lytLayout));
                    TextView txtMsg = (TextView) layout.findViewById(R.id.txtMensaje);
                    txtMsg.setText(Mensaje.MSJ_AUTENTICACION);
                    toast.setGravity(4, 200, 120);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }
            }
        }
    };

    /**
     * Crea el diálogo
     */
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setCancelable(false);
                dialog.setMessage(Mensaje.MSJ_PROCESANDO);
                dialog.show();
                return dialog;
            default:
                break;
        }
        return super.onCreateDialog(id);
    };
}
