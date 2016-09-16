package com.qantica.bd;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qantica.mundo.DescargaItem;

import java.util.ArrayList;

public class ManagerDescarga {

    public static int VERSION = 7;

    /**
     * Método para adicionar un nuevo elemento a la base de datos de descargas realizadas localmente
     * @param descarga
     * @param context
     */
    public static void adicionarDescarga(DescargaItem descarga, Context context) {

        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();

        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("codigo", descarga.getId());
        nuevoRegistro.put("nombre", descarga.getNombre());
        nuevoRegistro.put("version", descarga.getVersion());
        nuevoRegistro.put("ico", descarga.getIco());

        // Insertamos el registro en la base de datos
        db.insert("Descarga", null, nuevoRegistro);

        db.close();

    }

    /**
     * Método encargado de actualizar la version del item descargado
     * @param id de la descarga
     * @param version
     * @param context
     */
    public static void updateDescarga(int id, String version, Context context) {

        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();

        String sql = "UPDATE Descarga SET version=" + version +" WHERE codigo=" + id;
        db.execSQL(sql);
        db.close();
    }


    public static DescargaItem buscarContenido(String aid, Context context)
    {


        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                        "SELECT codigo, nombre, version, ico FROM Descarga WHERE codigo="+aid,
                        null);

        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya m�s registros
            do {
                String codigo = cursor.getString(0);
                String nombre = cursor.getString(1);
                String version = cursor.getString(2);
                String ico = cursor.getString(3);

                DescargaItem miItem = new DescargaItem(Integer.parseInt(codigo), nombre, version,false,ico);
                return miItem;

            } while (cursor.moveToNext());
        }
        db.close();
        return null;
    }

    /**
     * metodo encargado de listar los contenidos disponibles en la base de datos
     *
     * @param context
     * @return
     */
    public static ArrayList<DescargaItem> listarDescargas(Context context) {

        ArrayList<DescargaItem> listaContenidos = new ArrayList<DescargaItem>();

        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                        "SELECT codigo, nombre, version, ico FROM Descarga",
                        null);

        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya m�s registros
            do {
                String codigo = cursor.getString(0);
                String nombre = cursor.getString(1);
                String version = cursor.getString(2);
                String ico = cursor.getString(3);

                DescargaItem miItem = new DescargaItem(Integer.parseInt(codigo), nombre, version,false,ico);

                listaContenidos.add(miItem);

            } while (cursor.moveToNext());
        }
        db.close();
        return listaContenidos;
    }

}
