package com.tutorialesprogramacionya.proyecto019;

import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by diego on 12/10/2016.
 */

public class AdminSQLiteOpenHelper  extends SQLiteOpenHelper {

    public static final String nom_tabla      = "articulos";
    public static final String dispositivo           = "dispositivo";
    public static final String codigo           = "cod";
    public static final String nserie           = "nserie";
    public static final String descripcion          = "descripcion";
    public static final String precio         = "precio";
    public static final String fecha_registro        = "fecha_registro";
    public static final String status       = "status";

    private static final int DB_VERSION = 1;
    public AdminSQLiteOpenHelper(Context context) {
        super(context, nom_tabla, null, DB_VERSION);
       // Datos d = new Datos();
    }
    private static final String DATABASE_CREATE_TEAM = "create table "
            + nom_tabla + "(" + codigo + " integer primary key autoincrement, "
            + nserie + " text, "
            + dispositivo + " integer, "
            + descripcion + " text, "
            + precio + " string,"
            + fecha_registro + " text,"
            + status + " text" + ");";

    public static final String tabla = "articulos";



    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

      //  d = new Datos();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("create table articulos(codigo  int primary key, nserie text ,descripcion text ,precio real)");
        db.execSQL(DATABASE_CREATE_TEAM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     /*  String sql= "drop table if exists articulos";
       db.execSQL(sql);
       onCreate(db);*/
    }

  public void guardainterna(String nseries, String descripcions, String precios, String Fecha, String statuss, int disp, SQLiteDatabase database  ){
      Log.d("check","esta guardainterna");
     ContentValues contentValues = new ContentValues();
      //  contentValues.put(codigo,cod);
      Log.d("check","datos: "+nseries+descripcions+precios+Fecha+statuss+disp);
        contentValues.put(nserie,nseries);
        contentValues.put(descripcion,descripcions);
        contentValues.put(precio,precios);
        contentValues.put(fecha_registro,Fecha);
        contentValues.put(status,statuss);
        contentValues.put(dispositivo,disp);
      Log.d("check","datos: "+contentValues.get(nserie));
      Log.d("check","datos: "+contentValues.get(status));
        database.insert(nom_tabla,null,contentValues);
    /*  long rowInserted = database.insert(nom_tabla, null, contentValues);
      if(rowInserted != -1)
          Log.d("check","si");
      else
          Log.d("check","no");
*/



  }
  public Cursor leerinterna(SQLiteDatabase database){

        String[] projection = {codigo,nserie,descripcion,precio,fecha_registro,status};

        return (database.query(nom_tabla,projection,null,null,null,null,null));

  }

  public void actualizarinterna(int cod, String nseries, String descripcions, String precios, String Fecha, int statuss, SQLiteDatabase database  )
  {
      ContentValues contentValues = new ContentValues();



  }



}
