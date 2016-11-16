package qantica.com.controles;

import java.util.ArrayList;

import qantica.com.fedemarket.R;
import qantica.com.fedemarket.Singleton;
import qantica.com.conf.RecursosRed;
import qantica.com.mundo.Categoria;
import qantica.com.mundo.Contenido;
import qantica.com.mundo.SubCategoria;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SubCategoriaAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Contenido> iconos = new ArrayList<Contenido>();

	public SubCategoriaAdapter(Context c, int categoria, int subcategoria,
			int seleccion) {

		mContext = c;
		ArrayList<Contenido> misContenidos = Singleton.getContenidos();

		// se busca y se almacena el id de la categoria de primer nivel basado
		// en la posicion
		int id_categoria = Integer.parseInt(Singleton.getCategorias()
				.get(categoria).getId());
		// se busca y almacena el id de la categoria de segundo nivel basado en
		// la posicion y el id de la categoria de primer nivel
		int id_subcategoria = searchSubcategoria(id_categoria, subcategoria);
		// se busca y almacena el id de la categoria de tercer nivel basado en
		// la posicion y el id de la categoria de tercer nivel
		int id_seleccion = searchSeleccion(id_subcategoria, seleccion);

		// se recorre el array de contenidos en busca de contenidos que como
		// categoria final tengan el id de la categoria de tercer nivel y se
		// adiciona al array de resultados
		for (int i = 0; i < misContenidos.size(); i++) {

			if (misContenidos.get(i).getSubcategoria() == id_seleccion && misContenidos.get(i).getEstado().equals("true")) {
				iconos.add(misContenidos.get(i));
			}

		}

//		Log.i("---->", id_categoria+"     "+id_subcategoria+"   "+id_seleccion+"   "+iconos.size() );
		// se asiga el arreglo que se esta mostrando al singleton 
		Singleton.setPresentacion(iconos);
	}

	public int getCount() {
		return iconos.size();
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

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			MyView = inflater.inflate(R.layout.item_galeria, null);
			MyView.setLayoutParams(new GridView.LayoutParams(250, 120));

			TextView tv = (TextView) MyView.findViewById(R.id.grid_item_text);
			tv.setText(iconos.get(position).getNombre());

			TextView tvx = (TextView) MyView
					.findViewById(R.id.grid_item_descarga);
			tvx.setText(iconos.get(position).getDescargas()+" Descargas");

			
			 RatingBar mr=(RatingBar) MyView.findViewById(R.id.rating_galeria);
			 mr.setRating((float) iconos.get(position).getRating());
			 mr.setEnabled(false);
			 

			LinearLayout icon = (LinearLayout) MyView.findViewById(R.id.grid_item_image);
			LoaderIconCategoria image = new LoaderIconCategoria(mContext, iconos.get(position).getIcono(), RecursosRed.URL_ICON_CATEGORIA);
	        image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			icon.addView(image);
		}

		return MyView;
	}

	private int searchSubcategoria(int categoria, int posicion) {

		/**
		 * arreglo que contiene las categorias las cuales son los items de
		 * segundo nivel
		 */
		ArrayList<SubCategoria> misSubCategorias = Singleton.getSubcategorias();
		ArrayList<SubCategoria> result = new ArrayList<SubCategoria>();

		for (int j = 0; j < misSubCategorias.size(); j++) {

			// verificacion que la categoria de segundo nivel pertenesca
			// a la categoria de primer nivel
			if (categoria == misSubCategorias.get(j).getId_subcategoria()
					&& misSubCategorias.get(j).getId_categoria() == 0) {

				result.add(misSubCategorias.get(j));
			}
		}

		return Integer.parseInt(result.get(posicion).getId());
	}

	private int searchSeleccion(int subcategoria, int posicion) {

		/**
		 * arreglo que contiene las categorias las cuales son los items de
		 * segundo nivel
		 */
		ArrayList<SubCategoria> miSeleccion = Singleton.getSubcategorias();
		ArrayList<SubCategoria> result = new ArrayList<SubCategoria>();

		for (int j = 0; j < miSeleccion.size(); j++) {

			// verificacion que la categoria de segundo nivel pertenesca
			// a la categoria de primer nivel
			if (subcategoria == miSeleccion.get(j).getId_categoria()) {

				result.add(miSeleccion.get(j));
			}
		}

		return Integer.parseInt(result.get(posicion).getId());
	}
}
