package controles;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import qantica.com.fedemarket.R;

public class DialogoPopUp extends Dialog {

    Context context;
    final ImageButton botAceptar;
    final TextView enunciado;

    public DialogoPopUp(Context context,String mensaje) {

        super(context, android.R.style.Theme_Translucent);
        this.context=context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialogo_informes);

        botAceptar = (ImageButton)this.findViewById(R.id.btn_close);
        enunciado = (TextView)this.findViewById(R.id.lbl_mensaje);

        enunciado.setText(mensaje);

        botAceptar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                finish();
            }
        });

    }

    protected void finish() {

        this.cancel();
    }
}
