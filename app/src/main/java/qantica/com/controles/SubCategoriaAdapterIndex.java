package qantica.com.controles;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import qantica.com.conf.RecursosRed;
import qantica.com.mundo.Categoria;

import java.util.ArrayList;

import qantica.com.fedemarket.R;
import qantica.com.fedemarket.Singleton;
import qantica.com.mundo.SubCategoria;

public class SubCategoriaAdapterIndex extends BaseAdapter {

    private Context mContext;
    private static ArrayList<SubCategoria> iconos;
    int aux=0;

    public SubCategoriaAdapterIndex(Context c, ArrayList<SubCategoria> _icono) {
        mContext = c;
        iconos =  Singleton.getInstancia().getSubcategorias();
    }

    /**
     * Crea un nuevo ImageView para el item referenciado por el adaptador
     * Infla el layout
     * @param position
     * @param convertView
     * @param parent
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        View MyView = convertView;

        if (convertView == null || true) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            MyView = inflater.inflate(R.layout.item_categoria, null);
            // MyView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 200));

            TextView tv = (TextView) MyView.findViewById(R.id.categoria_titulo);
            tv.setText(Singleton.getInstancia().getSubcategorias().get(position).getNombre());

            LinearLayout icon = (LinearLayout) MyView.findViewById(R.id.categoria_image);

            LoaderIconCategoria image = new LoaderIconCategoria(mContext, Singleton.getSubcategorias().get(position)
                    .getIcono(), RecursosRed.URL_ICON_CATEGORIA);
            //image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            icon.addView(image);
        }else{
            MyView = convertView;
            Log.v("adaptador"," NULL: "+Singleton.getInstancia().getSubcategorias().get(position).getNombre()+
                    "  ICONO: "+Singleton.getInstancia().getSubcategorias().get(aux).getNombre());
        }

        return MyView;
    }

    public int getCount() {
        return iconos.size();
    }

    public Object getItem(int position) {
        return iconos.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

}
