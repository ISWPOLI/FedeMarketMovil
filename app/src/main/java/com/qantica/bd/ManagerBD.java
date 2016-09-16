package com.qantica.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qantica.mundo.Categoria;
import com.qantica.mundo.Contenido;
import com.qantica.mundo.SubCategoria;

import java.util.ArrayList;

public class ManagerBD {

    public static int VERSION = 7;

    /**
     * metodo para adicionar un nuevo elemento contenido a la base de datos
     *
     * @param contenido
     * @param context
     */
    public static void adicionarContenido(Contenido contenido, Context context) {

        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();

        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("codigo", contenido.getId());
        nuevoRegistro.put("nombre", contenido.getNombre());
        nuevoRegistro.put("version", contenido.getVersion());
        nuevoRegistro.put("descarga", contenido.getDescargas());
        nuevoRegistro.put("descripcion", contenido.getDescripcion());
        nuevoRegistro.put("rating", contenido.getRating());
        nuevoRegistro.put("categoria", contenido.getCategoria());
        nuevoRegistro.put("subcategoria", contenido.getSubcategoria());
        nuevoRegistro.put("cap1", contenido.getCap_1());
        nuevoRegistro.put("cap2", contenido.getCap_2());
        nuevoRegistro.put("icono", contenido.getIcono());
        nuevoRegistro.put("estado", contenido.getEstado());
        // Insertamos el registro en la base de datos
        db.insert("Contenido", null, nuevoRegistro);

        db.close();

    }

    /**
     * Método encargado de actualizar el rating y el numero de descargas de un
     * contenido
     * @param rating del contenido
     * @param descarga
     * @param id
     * @param version
     * @param nombre
     * @param estado
     * @param descripcion
     * @param cap_1
     * @param cap_2
     * @param ico
     * @param context
     */
    public static void updateContenido(double rating, int descarga, int id,
                                       String version, String nombre, String estado, String descripcion,
                                       String cap_1, String cap_2, String ico, Context context) {

        try {

            version = "" + Double.parseDouble(version);

        } catch (Exception e) {
            version = "1";
        }

        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();

        String sql = "UPDATE Contenido SET rating=" + rating + ", descarga="
                + descarga + ", version=" + version + ", nombre='" + nombre
                + "', descripcion='" + descripcion + "', estado='" + estado
                + "', cap1 ='" + cap_1 + "', cap2='" + cap_2 + "', icono='"
                +ico+ "' WHERE codigo=" + id;

        db.execSQL(sql);
        db.close();
    }

    /**
     * metodo encargado de listar los contenidos disponibles en la base de datos
     *
     * @param context
     * @return
     */
    public static ArrayList<Contenido> listarContenido(Context context) {

        ArrayList<Contenido> listaContenidos = new ArrayList<Contenido>();

        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                        "SELECT codigo, nombre, version, descarga, descripcion, rating, categoria, subcategoria, cap1, cap2, icono, estado FROM Contenido",
                        null);

        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya m�s registros
            do {
                String codigo = cursor.getString(0);
                String nombre = cursor.getString(1);
                String version = cursor.getString(2);
                String descarga = cursor.getString(3);
                String descripcion = cursor.getString(4);
                String rating = cursor.getString(5);
                String categoria = cursor.getString(6);
                String subcategoria = cursor.getString(7);
                String cap_1 = cursor.getString(8);
                String cap_2 = cursor.getString(9);
                String icono = cursor.getString(10);
                String estado = cursor.getString(11);

                Contenido miContenido = new Contenido(codigo, nombre, descarga,
                        descripcion, version, Integer.parseInt(categoria),
                        Integer.parseInt(subcategoria),
                        Double.parseDouble(rating), cap_1, cap_2, icono, estado);
                listaContenidos.add(miContenido);

            } while (cursor.moveToNext());
        }
        db.close();
        return listaContenidos;
    }

    /**
     * metodo para adicionar un nuevo elemento categoria a la base de datos
     *
     * @param categoria
     * @param context
     */
    public static void adicionarCategoria(Categoria categoria, Context context) {

        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();

        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("codigo", categoria.getId());
        nuevoRegistro.put("nombre", categoria.getNombre());
        nuevoRegistro.put("img", categoria.getIcono());

        // Insertamos el registro en la base de datos
        db.insert("Categoria", null, nuevoRegistro);

        db.close();
    }

    /**
     * metodo encargado de listar las categorias que existen en la base de datos
     *
     * @param context
     * @return
     */
    public static ArrayList<Categoria> listarCategoria(Context context) {

        ArrayList<Categoria> listaCategoria = new ArrayList<Categoria>();

        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT codigo, nombre, img FROM Categoria ORDER BY nombre",
                null);

        if (cursor.moveToFirst()) {
            do {
                String codigo = cursor.getString(0);
                String nombre = cursor.getString(1);
                String icono = cursor.getString(2);

                Categoria categoria = new Categoria(codigo, nombre, icono);
                listaCategoria.add(categoria);

            } while (cursor.moveToNext());
        }
        db.close();

        return listaCategoria;
    }

    /**
     * Método encargado de listar las subcategorias que existen en la base de
     * datos
     * @param context
     * @return ArrayList con las Subcategorias
     */
    public static ArrayList<SubCategoria> listarSubCategoria(Context context) {

        ArrayList<SubCategoria> listaCategoria = new ArrayList<SubCategoria>();

        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                        "SELECT codigo, nombre, categoria_id, subcategoria_id FROM Subcategoria ORDER BY nombre",
                        null);

        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya m�s registros
            do {
                String id = cursor.getString(0);
                String nombre = cursor.getString(1);
                String categoria = cursor.getString(2);
                String subcategoria = cursor.getString(3);

                SubCategoria miItem = new SubCategoria(id, nombre,
                        Integer.parseInt(categoria),
                        Integer.parseInt(subcategoria));
                listaCategoria.add(miItem);

            } while (cursor.moveToNext());
        }
        db.close();

        return listaCategoria;
    }

    /**
     * metodo para adicionar un nuevo elemento subcategoria a la base de datos
     *
     * @param subcategoria susbcategoria
     * @param context
     */
    public static void adicionarSubCategoria(SubCategoria subcategoria,
                                             Context context) {

        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();

        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("codigo", subcategoria.getId());
        nuevoRegistro.put("nombre", subcategoria.getNombre());
        nuevoRegistro.put("categoria_id", subcategoria.getId_categoria());
        nuevoRegistro.put("subcategoria_id", subcategoria.getId_subcategoria());

        // Insertamos el registro en la base de datos
        db.insert("Subcategoria", null, nuevoRegistro);

        db.close();
    }

    public static ArrayList<Integer> encontrarMaximo(Context context) {

        ArrayList<Integer> valores = new ArrayList<Integer>();

        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                        "SELECT COUNT(e.nombre) FROM subcategoria e where e.categoria_id=0 GROUP BY (e.subcategoria_id)",
                        null);

        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya m�s registros
            do {
                valores.add(cursor.getInt(0));

            } while (cursor.moveToNext());
        }
        db.close();

        return valores;

    }

    public static void actualizarCategoria(Categoria categoria, Context context) {

        DriverSQL connect = new DriverSQL(context, "contenido", null, VERSION);
        SQLiteDatabase db = connect.getWritableDatabase();
        String sql = "UPDATE Categoria SET nombre=" + categoria.getNombre()
                + " WHERE codigo=" + categoria.getId();

        db.execSQL(sql);
        db.close();
    }

}

