package qantica.com.controles;

import java.util.ArrayList;

import qantica.com.fedemarket.R;
import qantica.com.fedemarket.Singleton;
import qantica.com.conf.RecursosRed;
import qantica.com.mundo.Categoria;
import qantica.com.mundo.Contenido;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
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

public class CategoriaAdapter extends BaseAdapter {

	private Context mContext;
	private static ArrayList<Categoria> iconos;


	public CategoriaAdapter(Context c, ArrayList<Categoria> _icono) {
		mContext = c;
		iconos = _icono;
	
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
			MyView = inflater.inflate(R.layout.item_categoria, null);
			MyView.setLayoutParams(new GridView.LayoutParams(LayoutParams.FILL_PARENT, 200));
			
			TextView tv = (TextView) MyView.findViewById(R.id.categoria_titulo);
			tv.setText(iconos.get(position).getNombre());

			
			LinearLayout icon = (LinearLayout) MyView.findViewById(R.id.categoria_image);
			
			LoaderIconCategoria image = new LoaderIconCategoria(mContext, iconos.get(position).getIcono(), RecursosRed.URL_ICON_CATEGORIA);
	        image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			icon.addView(image);
		}

		return MyView;
	}

}
