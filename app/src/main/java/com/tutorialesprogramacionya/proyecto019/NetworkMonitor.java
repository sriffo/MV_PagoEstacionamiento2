package com.tutorialesprogramacionya.proyecto019;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class NetworkMonitor extends BroadcastReceiver {

    private AsyncHttpClient clientes;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("sync","Esta en el broadcast");
        clientes = new AsyncHttpClient();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context,
                    "administracion", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            Cursor fila = bd.rawQuery("select * from articulos where status = 1", null);
            while (fila.moveToNext()) {
                String url = "https://appnfc.000webhostapp.com/agregar.php?";
                if(fila.getCount()>0){
                    // Log.d("SYNC","");
                    String parametros = "cod=" +fila.getInt(fila.getColumnIndex("cod")) + "&nserie=" + fila.getString(fila.getColumnIndex("nserie")) + "&descripcion=" + fila.getString(fila.getColumnIndex("descripcion")) + "&precio=" + fila.getString(fila.getColumnIndex("precio")) + "&fecha_registro=" + fila.getString(fila.getColumnIndex("fecha_registro"));
                    final int numero= fila.getInt(fila.getColumnIndex("cod"));
                    ContentValues registro = new ContentValues();
                    int status=0;
                    registro.put("status",status);
                    int cant = bd.update("articulos", registro, "cod=" + numero, null);

                    context.sendBroadcast(new Intent());
                    clientes.post(url + parametros, new AsyncHttpResponseHandler()
                    {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode == 200) {

                            }
                        }


                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    //        Toast.makeText(MainActivity.this, "No hay conexion con servidor externo", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{

                  //  Toast.makeText(MainActivity.this, "Datos ya sincronizados", Toast.LENGTH_SHORT).show();
                }



            }
bd.close();







    }



    public boolean checkred(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!= null && networkInfo.isConnected());

    }


}
