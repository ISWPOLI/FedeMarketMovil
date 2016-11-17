package qantica.com.controles;

import java.util.ArrayList;

import qantica.com.fedemarket.R;
import qantica.com.fedemarket.Singleton;
import qantica.com.conf.RecursosRed;
import qantica.com.mundo.Contenido;
import qantica.com.mundo.DescargaItem;

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

public class AplicacionAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<DescargaItem> iconos;

	public AplicacionAdapter(Context c, ArrayList<DescargaItem> _icono) {
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
			MyView = inflater.inflate(R.layout.item_descarga, null);
			MyView.setLayoutParams(new GridView.LayoutParams(250, 120));

			TextView tv = (TextView) MyView.findViewById(R.id.descarga_item_text);
			tv.setText(iconos.get(position).getNombre());

			if(!iconos.get(position).isUpdate()){
			
			ImageView update=(ImageView) MyView.findViewById(R.id.descarga_update);
			update.setVisibility(ImageView.INVISIBLE);
			}
			
			LinearLayout icon = (LinearLayout) MyView.findViewById(R.id.ico_contenido_descarga);
			LoaderIconCategoria image = new LoaderIconCategoria(mContext, iconos.get(position).getIco(), RecursosRed.URL_ICON_CATEGORIA);
	        image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	
	        icon.addView(image);
		
		}

		return MyView;
	}
}
