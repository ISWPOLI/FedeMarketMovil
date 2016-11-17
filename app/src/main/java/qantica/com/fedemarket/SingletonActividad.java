package qantica.com.fedemarket;

import java.util.ArrayList;
import android.app.Activity;

/**
 * @author Juan Rubiano
 * 15/09/2016
 * Q-antica Ltda.
 * Colombia
 */
public class SingletonActividad {

    private static final SingletonActividad INSTANCIA = new SingletonActividad();

    private static ArrayList<Activity> actividades = new ArrayList<Activity>();

    private SingletonActividad(){}

    public static SingletonActividad getInstancia(){
        return INSTANCIA;
    }

    public static ArrayList<Activity> getActividades() {
        return actividades;
    }

    public static void setActividades(ArrayList<Activity> actividades) {
        SingletonActividad.actividades = actividades;
    }
}