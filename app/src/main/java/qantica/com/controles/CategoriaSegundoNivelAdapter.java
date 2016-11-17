package qantica.com.controles;

import java.util.ArrayList;

import qantica.com.fedemarket.ContenidoActivity;
import qantica.com.fedemarket.Singleton;
import qantica.com.conf.Mensaje;
import qantica.com.mundo.SubCategoria;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class CategoriaSegundoNivelAdapter extends BaseExpandableListAdapter {

	Context c;
	ArrayList<SubCategoria> categoriaSegundoNivel = new ArrayList<SubCategoria>();
	ArrayList<SubCategoria> categoriaTercerNivel = new ArrayList<SubCategoria>();
	ArrayList<SubCategoria> misCategorias = Singleton.getSubcategorias();
	boolean centinela=true;
	ContenidoActivity actividad;

	public CategoriaSegundoNivelAdapter(Context c, ArrayList<SubCategoria> result, ContenidoActivity act){

		this.c = c;
		actividad=act;
		categoriaSegundoNivel=result;
		
	}
	

	public Object getChild(int groupPosition, int childPosition) {
		return childPosition;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
//		Log.i("fed", "fed  "+categoriaTercerNivel.get(childPosition).getNombre());
		TextView tv = new TextView(c);
		tv.setText(categoriaTercerNivel.get(childPosition).getNombre());
		tv.setPadding(40, 8, 8, 8);
//		tv.setLayoutParams(new ListView.LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.FILL_PARENT));

		return tv;
	}

	public int getChildrenCount(int groupPosition) {
		int i = 0;
		try {

			// recorrido de la fila de la categoria
			for (int j = 0; j <misCategorias.size() ; j++) {

				if(misCategorias.get(j).getId_categoria()!=0 && categoriaSegundoNivel.get(groupPosition).getId().equals(""+misCategorias.get(j).getId_categoria() ))
				{
				i++;
				categoriaTercerNivel.add(misCategorias.get(j));
				}
			}

		} catch (Exception e) {
			Log.w("ERROR ", "error");
		}

		if (i==0 && centinela) {
			centinela=false;
			actividad.limpiar();
		} else {

			centinela=true;
		}
		
		return i;
	}

	public Object getGroup(int groupPosition) {
		return 1;
	}

	public int getGroupCount() {
		return categoriaSegundoNivel.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		LayoutParams lp = new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, 40);
		
		TextView tv = new TextView(c);
		tv.setLayoutParams(lp);
		tv.setText(categoriaSegundoNivel.get(groupPosition).getNombre());
		tv.setPadding(40, 8, 8, 8);
		
		return tv;

	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
