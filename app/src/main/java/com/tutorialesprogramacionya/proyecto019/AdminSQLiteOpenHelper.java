package com.tutorialesprogramacionya.proyecto019;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by diego on 12/10/2016.
 */

public class AdminSQLiteOpenHelper  extends SQLiteOpenHelper {
    public static final String nom_tabla      = "articulos";
    public static final String codigo           = "cod";
    public static final String nserie           = "nserie";
    public static final String descripcion          = "descripcion";
    public static final String precio         = "precio";
    public static final String fecha_registro        = "fecha_registro";

  /*  private static final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE "
            + nom_tabla + " ADD COLUMN " + COLUMN_COACH + " text;";
*/
    private static final String DATABASE_CREATE_TEAM = "create table "
            + nom_tabla + "(" + codigo + " integer primary key autoincrement, "
            + nserie + " text, "
            + descripcion + " text, "
            + precio + " string,"
            + fecha_registro + " text" +");";

    public static final String tabla = "articulos";



    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("create table articulos(codigo  int primary key, nserie text ,descripcion text ,precio real)");
        db.execSQL(DATABASE_CREATE_TEAM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       /*if(oldVersion > 0){
           db.execSQL(DATABASE_ALTER_TEAM_1);
           }*/
    }
}
