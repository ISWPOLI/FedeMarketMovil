package qantica.com.fedemarket;

import java.util.ArrayList;

import qantica.com.bd.ManagerDescarga;
import qantica.com.conf.Mensaje;
import qantica.com.controles.AplicacionAdapter;
import qantica.com.controles.DialogoPopUp;
import qantica.com.mundo.Contenido;
import qantica.com.mundo.DescargaItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Actividad encargada de manipular la ventana en la cual se publicara los
 * contenidos descargados
 */

public class AplicacionActivity extends Activity {

	private ImageButton botAtras, botHome;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.pantalla_aplicacion);

		ArrayList<DescargaItem> misContenidos = listarContenido();

		// invocacion al metodo para asignar los valores de la interface a las
		// variables locales
		setControles();
		// invoca el metodo para colocarle el evento a los botones
		setListeners();

		if (misContenidos.size() > 0) {
			final GridView lista_descargados = (GridView) findViewById(R.id.aplicacion_lista_apps);
			lista_descargados.setAdapter(new AplicacionAdapter(	AplicacionActivity.this, misContenidos));
		} else {
			
			DialogoPopUp _dialogo = new DialogoPopUp(AplicacionActivity.this, Mensaje.MSJ_NO_DESCARGAS);
			_dialogo.show();
		}
		SingletonActividad.getActividades().add(this);
	}

	private void setListeners() {

		botHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(AplicacionActivity.this, InicioActivity.class);
				startActivity(intent);
				finish();
			}
		});

		botAtras.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		
		
	}

	private void setControles() {

		botAtras = (ImageButton) findViewById(R.id.btn_atras);
		botHome = (ImageButton) findViewById(R.id.btn_home);
		
	}

	/**
	 * metodo para definir si existe una version mas actual de los contenidos
	 * descargados
	 * 
	 * @return
	 */
	private ArrayList<DescargaItem> listarContenido() {

		// se consulta la lista de contenidos descargados desde el dispositivo

		ArrayList<DescargaItem> misDescargaItems = ManagerDescarga.listarDescargas(AplicacionActivity.this);

		// se recorre el arreglo de los contenidos descargados
		for (int i = 0; i < misDescargaItems.size(); i++) {

			// se recorre el arreglo de los contenidos disponibles
			for (int j = 0; j < Singleton.getContenidos().size(); j++) {

				// se busca el contenido descargado en la lista de contenidos
				// disponibles
				if (Singleton.getContenidos().get(j).getId()
						.equals("" + misDescargaItems.get(i).getId())) {

					// se compara la version del contenido descargado con la
					// actual
					if (!Singleton.getContenidos().get(j).getVersion()
							.equals(misDescargaItems.get(i).getVersion())) {

						// si existe una version del contenido disponible se
						// cambia el valor a true para mostrar el icono en la
						// pantalla
						misDescargaItems.get(i).setUpdate(true);
					}
					break;

				}
			}
		}

		// se retorna el arreglo con los contenidos descargados y con la
		// aclaracion del update
		return misDescargaItems;
	}

}
