package qantica.com.controles;

import java.util.ArrayList;

import qantica.com.fedemarket.R;
import qantica.com.fedemarket.Singleton;
import qantica.com.conf.Mensaje;
import qantica.com.conf.RecursosRed;
import qantica.com.mundo.Categoria;
import qantica.com.mundo.Contenido;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ContenidoAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Contenido> iconos = new ArrayList<Contenido>();

	public ContenidoAdapter(Context c, ArrayList<Contenido> _icono) {
		mContext = c;
		int aux = Singleton.getInstancia().getCid();
		_icono = Singleton.getInstancia().getContenidos();

		// se verifica si es una categoria en especial a listar
		if (aux != 0) {
			for (int i = 0; i < _icono.size(); i++) {
				if (aux == _icono.get(i).getCategoria() && _icono.get(i).getEstado().equals("true")) {
					iconos.add(_icono.get(i));
				}
			}
		}
		// de lo contrario se sabe que va se va listar todo
		else {
			for (int i = 0; i < _icono.size(); i++) {
				if (_icono.get(i).getEstado().equals("true")) {
					iconos.add(_icono.get(i));
				}
			}
		}

		// se verifica si no existen contenidos y se muestra un mensaje indicandolo
		if (iconos.size() == 0) {
			//DialogoPopUp dialogoPopUp = new DialogoPopUp(c,Mensaje.MSJ_NO_CONTENIDOS);
			//dialogoPopUp.show();
		}

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
			//MyView.setLayoutParams(new GridView.LayoutParams(250, 120));

			TextView tv = (TextView) MyView.findViewById(R.id.grid_item_text);
			tv.setText(iconos.get(position).getNombre());

			TextView tvx = (TextView) MyView.findViewById(R.id.grid_item_descarga);
			tvx.setText(iconos.get(position).getDescargas() + " Descargas");

			RatingBar mr = (RatingBar) MyView.findViewById(R.id.rating_galeria);
			mr.setRating((float) iconos.get(position).getRating());
			mr.setEnabled(false);

			LinearLayout icon = (LinearLayout) MyView.findViewById(R.id.grid_item_image);
			LoaderIconCategoria image = new LoaderIconCategoria(mContext,iconos.get(position).getIcono(),
					RecursosRed.URL_ICON_CATEGORIA);
			//image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				//	LayoutParams.WRAP_CONTENT));
			icon.addView(image);

		}

		return MyView;
	}
	
}
