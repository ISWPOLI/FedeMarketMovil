package qantica.com.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DriverSQL extends SQLiteOpenHelper {

    String sqlCreate_contenido = "CREATE TABLE Contenido (codigo INTEGER, nombre TEXT, version TEXT, descarga INTEGER, rating REAL, descripcion TEXT, cap1 TEXT, cap2 TEXT, icono TEXT, categoria INTEGER, subcategoria INTEGER, estado TEXT)";

    String sqlCreate_categoria = "CREATE TABLE Categoria (codigo INTEGER, nombre TEXT, img TEXT)";

    String sqlCreate_descargas = "CREATE TABLE Descarga (codigo INTEGER, nombre TEXT, version TEXT, ico TEXT)";

    String sqlCreate_subcategoria = "CREATE TABLE Subcategoria (codigo INTEGER, nombre TEXT, categoria_id INTEGER, subcategoria_id)";



    public DriverSQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlCreate_contenido);
        db.execSQL(sqlCreate_categoria);
        db.execSQL(sqlCreate_subcategoria);
        db.execSQL(sqlCreate_descargas);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
