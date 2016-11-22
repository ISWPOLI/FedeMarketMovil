package qantica.com.fedemarket;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import qantica.com.animacion.AnimacionAdapter;
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
	private Bundle datos;
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

		if(datos.getString("categoria").equals("No hay subcategorias")){
			contenido = getContentAvalaible();
			SingletonActividad.getInstancia().getActividades().add(this);
		}else{
			subcategorias = getSubcategoriasAvalaible();
			//Llena el GridView con las subcategorias
			try {
				gv.setAdapter(new SubCategoriaAdapterIndex(this, subcategorias));
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("ContenidoActivityIni", "Subcategorias vacias");
			}

		}

		AnimacionAdapter adapter = new AnimacionAdapter(ContenidoActivity.this);
		myPager = (ViewPager) findViewById(R.id.banner_noticia_categoria);
		myPager.setAdapter(adapter);
		myPager.setCurrentItem(1);

		start();

	}

	
	/**
	 * Metodo encargado de gestionar el contenido disponible
	 * @return ArrayLisy con el contenido
	 */
	private ArrayList<Contenido> getContentAvalaible() {
		ArrayList<Contenido> result=new ArrayList<Contenido>();		
		for (int i = 0; i < Singleton.getInstancia().getContenidos().size(); i++) {
			if(Singleton.getInstancia().getContenidos().get(i).getEstado().equals("true")){
				Log.v("ContenidoActivity", "nombre: "+Singleton.getInstancia().
						getContenidos().get(i).getNombre()+"  estado: "
						+Singleton.getInstancia().getContenidos().get(i).getEstado());
				result.add(Singleton.getInstancia().getContenidos().get(i));
			}
		}
		//Organizar alfabéticamente
		Collections.sort(result);

		return result;
	}

	/**
	 * Metodo encargado de gestionar las subcategorias disponibles
	 * @return ArrayList con el contenido
	 */
	private ArrayList<SubCategoria> getSubcategoriasAvalaible() {
		ArrayList<SubCategoria> result=new ArrayList<SubCategoria>();
		for (int i = 0; i < Singleton.getInstancia().getSubcategorias().size(); i++) {
			//if(Singleton.getInstancia().getSubcategorias().get(i).getEstado().equals("true")){
				Log.v("ContenidoActivity2", "nombre: "+Singleton.getInstancia().getSubcategorias().get(i).getNombre()+
						"  estado: "+Singleton.getInstancia().getSubcategorias().get(i).getEstado()+" icono: "+
				Singleton.getInstancia().getSubcategorias().get(i).getIcono());
				result.add(Singleton.getInstancia().getSubcategorias().get(i));
			//}
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
			public void onItemClick(AdapterView parent, View v, int position,long id) {
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
		gv.setAdapter(new ImageAdapter(ContenidoActivity.this,new ArrayList<Contenido>()));
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

			Intent intent = new Intent(ContenidoActivity.this,DescargaView.class);
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
			Intent intent = new Intent(ContenidoActivity.this,AplicacionActivity.class);
			startActivity(intent);
		}
	}
}