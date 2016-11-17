package qantica.com.controles;

import java.util.ArrayList;

import qantica.com.fedemarket.R;
import qantica.com.fedemarket.ContenidoActivity;
import qantica.com.fedemarket.Singleton;
import qantica.com.mundo.Categoria;
import qantica.com.mundo.SubCategoria;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class CategoriaPrimerNivelAdapter extends BaseExpandableListAdapter {

	Context c;
	/**
	 * arreglo de las categorias las cuales seran lo item de primer nivel
	 */
	private ArrayList<Categoria> misCategorias = Singleton.getCategorias();
	private ArrayList<SubCategoria> misSubCategorias = Singleton
			.getSubcategorias();

	private ArrayList<SubCategoria> result;// = new ArrayList<SubCategoria>();

	ContenidoActivity actividad;

	public CategoriaPrimerNivelAdapter(Context c, ContenidoActivity act) {
		this.c = c;
		actividad = act;
	}

	public Object getChild(int arg0, int arg1) {
		return arg1;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		CategoriaExpListView SecondLevelexplv = new CategoriaExpListView(c);
		SecondLevelexplv.setGroupIndicator(getIcon());
		SecondLevelexplv
				.setAdapter(new CategoriaSegundoNivelAdapter(c, result, actividad));
		SecondLevelexplv.setPadding(30, 8, 8, 8);
		
		/**
		 * imprime el item de tercer nivel
		 */
		SecondLevelexplv
				.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition_, int childPosition,
							long id) {

						
						actividad.listar(groupPosition, groupPosition_,
								childPosition);
						return false;
					}
				});

		return SecondLevelexplv;

	}

	/**
	 * numero de hijos que va tener
	 * 
	 * 
	 * se calcula el numero de subcategorias asociadas y si es mayor a 0 se
	 * retorna 1
	 * 
	 * de lo contrario se retorna 0 para evitar el espacio adicional que se
	 * presenta
	 */
	public int getChildrenCount(int groupPosition) {
		result = new ArrayList<SubCategoria>();

		for (int i = 0; i < misSubCategorias.size(); i++) {

			if (misCategorias.get(groupPosition).getId()
					.equals(misSubCategorias.get(i).getId_subcategoria() + "")
					&& 0 == misSubCategorias.get(i).getId_categoria()) {
				result.add(misSubCategorias.get(i));
			}
		}

		if (result.size() != 0) {
			return 1;
		} else {
			return 0;
		}
	}

	public Object getGroup(int groupPosition) {
		return groupPosition;
	}

	/**
	 * numero de items que va tener
	 */
	public int getGroupCount() {
		return misCategorias.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView tv = new TextView(c);
		tv.setText(misCategorias.get(groupPosition).getNombre());
		tv.setPadding(40, 15, 15, 15);

		return tv;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public Drawable getIcon() {

		return c.getResources().getDrawable(R.drawable.fedecafe_style_navigation);

	}
}