package qantica.com.controles;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import qantica.com.fedemarket.R;
import qantica.com.mundo.Comentario;
import qantica.com.mundo.Contenido;

public class ComentarioAdapter extends ArrayAdapter {

	Activity context;
	ArrayList<Comentario> datos;
	
	public ComentarioAdapter(Activity context,ArrayList<Comentario> _datos) {
		super(context, R.layout.item_comentario, _datos);
		datos=_datos;
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
				
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.item_comentario, null);

		TextView lblTitulo = (TextView) item.findViewById(R.id.comentario_username);
		lblTitulo.setText(datos.get(position).getUsuario());

		TextView lblSubtitulo = (TextView) item.findViewById(R.id.comentario_descripcion);
		lblSubtitulo.setText(datos.get(position).getDescripcion());
		
		RatingBar rating=(RatingBar) item.findViewById(R.id.comentario_rating);
		rating.setRating( (int) datos.get(position).getRating());
		rating.setEnabled(false);

		return (item);
	}
	
}