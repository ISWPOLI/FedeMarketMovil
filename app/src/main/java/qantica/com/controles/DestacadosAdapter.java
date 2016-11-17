package qantica.com.controles;

import java.util.ArrayList;

import qantica.com.fedemarket.R;
import qantica.com.mundo.Contenido;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DestacadosAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Contenido> iconos;


	public DestacadosAdapter(Context c, ArrayList<Contenido> _icono) {
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
			MyView = inflater.inflate(R.layout.item_destacado, null);
			MyView.setLayoutParams(new GridView.LayoutParams(300, 180));

			TextView tv = (TextView) MyView.findViewById(R.id.lbl_destacado_titulo);
			tv.setText(iconos.get(position).getNombre());

			TextView tvx = (TextView) MyView.findViewById(R.id.lbl_destacado_descarga);
			tvx.setText(iconos.get(position).getDescargas());
			
			RatingBar mr=(RatingBar) MyView.findViewById(R.id.rating_destacado);
			mr.setRating((float) iconos.get(position).getRating());
			mr.setEnabled(false);
			
			/*ImageView iv = (ImageView) MyView
					.findViewById(R.id.img_destacado);
			iv.setImageResource(iconos.get(position).getImg());*/
		}

		return MyView;
	}

}
