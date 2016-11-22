package qantica.com.controles;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import qantica.com.fedemarket.R;
import qantica.com.fedemarket.Singleton;
import qantica.com.conf.Mensaje;
import qantica.com.conf.RecursosRed;
import qantica.com.mundo.Categoria;
import qantica.com.mundo.Contenido;

public class BusquedaAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Contenido> contenidos = Singleton.getContenidos();
	private ArrayList<Categoria> categorias = Singleton.getCategorias();
	private ArrayList<Object> result = new ArrayList<Object>();
	private Contenido contenido;
	private Categoria categoria;

	public BusquedaAdapter(Context c) {
		mContext = c;
		String cadena = Singleton.getBusqueda().toUpperCase();
		cadena = cambiar(cadena);
		
		for (int i = 0; i < categorias.size(); i++) {
			// se compara que los nombre de los contenidos contengan palabra
			// ingresada
			if (comparar(categorias.get(i).getNombre().toUpperCase()).contains(cadena)) {
				result.add(categorias.get(i));
			}

		}

		for (int i = 0; i < contenidos.size(); i++) {
			// se compara que los nombre de los contenidos contengan palabra
			// ingresada
			if (comparar(contenidos.get(i).getNombre().toUpperCase()).contains(cadena)) {
				result.add(contenidos.get(i));
			}

		}

		// se verifica si no existen contenidos y se muestra un mensaje
		// indicandolo
		if (result.size() == 0) {
			DialogoPopUp dialogoPopUp = new DialogoPopUp(c,
					Mensaje.MSJ_NO_BUSQUEDA);
			dialogoPopUp.show();
		}

		Singleton.setResultados(result);
	}

	public static String cambiar (String n){
		String resultado = "";
		char arreglo[] = n.toCharArray();
		for (int i = 0; i < arreglo.length; i++) {
			if(arreglo[i] == '�'){
				arreglo[i] = 'A';				
			}else if(arreglo[i] == '�'){
				arreglo[i] = 'E';
			}else if(arreglo[i] == '�'){
				arreglo[i] = 'I';
			}else if(arreglo[i] == '�'){
				arreglo[i] = 'O';
			}else if(arreglo[i] == '�'){
				arreglo[i] = 'U';
			}
		}
		for (int i = 0; i < arreglo.length; i++) {
			resultado += arreglo[i];
		}
		return resultado;
	}
	
	public static String comparar(String n){
		String resultado = "";
		char []arreglo =  n.toCharArray();		
		for (int i = 0; i < arreglo.length; i++) {		
			if(arreglo[i] == '�'){
				arreglo[i] = 'A';				
			}else if(arreglo[i] == '�'){
				arreglo[i] = 'E';
			}else if(arreglo[i] == '�'){
				arreglo[i] = 'I';
			}else if(arreglo[i] == '�'){
				arreglo[i] = 'O';
			}else if(arreglo[i] == '�'){
				arreglo[i] = 'U';
			}
		}
		for (int i = 0; i < arreglo.length; i++) {
			resultado += arreglo[i];
		}
		return resultado;
	}
	
	public int getCount() {
		return result.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {

		View MyView = convertView;

		if (convertView == null) {
			if (result.get(position) instanceof Contenido) {
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				MyView = inflater.inflate(R.layout.item_galeria, null);
				MyView.setLayoutParams(new GridView.LayoutParams(250, 200));

				contenido = (Contenido) result.get(position);

				TextView tv = (TextView) MyView.findViewById(R.id.grid_item_text);
				tv.setText(contenido.getNombre());

				TextView tvx = (TextView) MyView.findViewById(R.id.grid_item_descarga);
				tvx.setText(contenido.getDescargas() + " Descargas");

				RatingBar mr = (RatingBar) MyView.findViewById(R.id.rating_galeria);
				mr.setRating((float) contenido.getRating());
				mr.setEnabled(false);

				LinearLayout icon = (LinearLayout) MyView.findViewById(R.id.grid_item_image);
				LoaderIconCategoria image = new LoaderIconCategoria(mContext,contenido.getIcono(), RecursosRed.URL_ICON_CATEGORIA);

				image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				icon.addView(image);
			} else {

				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				MyView = inflater.inflate(R.layout.item_categoria, null);
				MyView.setLayoutParams(new GridView.LayoutParams(250, 200));

				categoria = (Categoria) result.get(position);

				TextView tv = (TextView) MyView.findViewById(R.id.categoria_titulo);
				tv.setText(categoria.getNombre());

				LinearLayout icon = (LinearLayout) MyView.findViewById(R.id.categoria_image);

				LoaderIconCategoria image = new LoaderIconCategoria(mContext,categoria.getIcono(), RecursosRed.URL_ICON_CATEGORIA);
				image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				icon.addView(image);
			}
		}
		return MyView;
	}

	/**
	 * metodo encargado de listar los codigos de las categorias que cuentes con
	 * la cadena de busqueda
	 * 
	 * @param cadena
	 * @return
	 */
	private ArrayList<Integer> searchCategorias(String cadena) {

		ArrayList<Integer> resultados = new ArrayList<Integer>();

		for (int i = 0; i < Singleton.getCategorias().size(); i++) {

			// se compara que los nombre de los contenidos contengan palabra
			// ingresada
			if (Singleton.getCategorias().get(i).getNombre().toUpperCase()
					.contains(cadena)) {

				resultados.add(Integer.parseInt(Singleton.getCategorias()
						.get(i).getId()));
			}
		}

		return resultados;
	}

	/**
	 * metodo encargado de verificar si un contenido hace parte de una categoria
	 * definida
	 * 
	 * @return
	 */
	private boolean isContenido(ArrayList<Integer> resultados, int id) {

		for (int j = 0; j < resultados.size(); j++) {

			if (resultados.get(j) == id) {

				return true;
			}
		}

		return false;
	}
}
