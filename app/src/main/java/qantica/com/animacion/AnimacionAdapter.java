package qantica.com.animacion;


import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import qantica.com.fedemarket.R;
import qantica.com.fedemarket.Singleton;

public class AnimacionAdapter extends PagerAdapter {


    public boolean animacion=true;
    public Context context;

    public AnimacionAdapter(Context context) {
        this.context=context;
    }

    public int getCount() {
        return Singleton.getNoticias().size();
    }

    public Object instantiateItem(View collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.noticia, null);
        TextView textView = (TextView) view.findViewById(R.id.label_noticia);


        textView.setText(Html.fromHtml("<p><b>"+Singleton.getNoticias().get(position).getTitulo()+"</b></p></br><p>"+Singleton.getNoticias().get(position).getContenido()+"</p>"
        ), TextView.BufferType.SPANNABLE);



        ((ViewPager) collection).addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);

    }

    @Override
    public void finishUpdate(View arg0) {
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);

    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    /**
     * guarda el estado de la animacion cuando entra en un estado de espera, pausa u otras
     */
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }

    public boolean isAnimacion() {
        return animacion;
    }

    public void setAnimacion(boolean animacion) {
        this.animacion = animacion;
    }





}
