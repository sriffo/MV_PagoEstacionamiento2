package com.tutorialesprogramacionya.proyecto019;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RecyclerMovimiento extends AppCompatActivity {
    ArrayList<Datos> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ListadoAdapter adapter;
    private AsyncHttpClient cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_movimiento);
        recyclerView= (RecyclerView)findViewById(R.id.recyclerDatos);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ListadoAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        cliente = new AsyncHttpClient();
        //consultaroffline();
        consultaroffline();

    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    private void consultaroffline() {
        Log.d("cosulta","esta en cosnulta offline");
        arrayList.clear();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();
        Cursor fila = bd.rawQuery(
                "select * from articulos", null);
        while(fila.moveToNext()){
            //  String nserie= fila.getString(fila.getColumnIndex("nserie"));
            int disp= fila.getInt(fila.getColumnIndex("dispositivo"));
            int cod=fila.getInt(fila.getColumnIndex("cod"));
            int estacion=fila.getInt(fila.getColumnIndex("estacion"));
            String nserie= fila.getString(fila.getColumnIndex("nserie"));
            String operacion= fila.getString(fila.getColumnIndex("descripcion"));
            String tarifa= fila.getString(fila.getColumnIndex("precio"));
            String fecha= fila.getString(fila.getColumnIndex("fecha_registro"));
            String estado= fila.getString(fila.getColumnIndex("status"));
            Log.d("recycler","datos"+disp);
            arrayList.add(new Datos(cod,disp,estacion,estado,nserie,operacion,tarifa,fecha));

        }
        adapter.notifyDataSetChanged();
        fila.close();
        bd.close();



    }
    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) { //modulo que revisa si hay red, este gatilla el sync
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            onNetworkChange(ni);
        }
    };
    private void onNetworkChange(NetworkInfo networkInfo) {
        if (networkInfo != null) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                sync();
                adapter.notifyDataSetChanged();
            }
        }
    }//muestra el listado de datos de todala base interna

    public void sync() {
        //arrayList.clear();

        Log.d("check", "sync" );
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("select * from articulos where status = 'Pendiente'", null);
        while (fila.moveToNext()) {
            String url = "https://appnfc.000webhostapp.com/agregar.php?";
            if(fila.getCount()>0){
                // Log.d("SYNC","");
                String parametros = "cod=" +fila.getInt(fila.getColumnIndex("cod")) + "&nserie=" + fila.getString(fila.getColumnIndex("nserie")) + "&descripcion=" + fila.getString(fila.getColumnIndex("descripcion")) + "&precio=" + fila.getString(fila.getColumnIndex("precio")) + "&fecha_registro=" + fila.getString(fila.getColumnIndex("fecha_registro"))
                        + "&dispositivo=" + fila.getString(fila.getColumnIndex("dispositivo"));
                final int numero= fila.getInt(fila.getColumnIndex("cod"));

                cliente.post(url + parametros, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200) {
                            Toast.makeText(RecyclerMovimiento.this, "Datos sincronizados", Toast.LENGTH_SHORT).show();
                            modificacion(numero);


                        }
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //   Toast.makeText(MainActivity.this, "No hay conexion con servidor externo", Toast.LENGTH_SHORT).show();
                    }
                });

            }else{

                // Toast.makeText(MainActivity.this, "Datos ya sincronizados", Toast.LENGTH_SHORT).show();
            }



        }
        bd.close();

    }
    public void modificacion(int cod) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        int status=0;
        registro.put("status","Enviado");
        //registro.put("codigo", cod);

        int cant = bd.update("articulos", registro, "cod=" + cod, null);
        bd.close();
    }
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }
    protected void onPause() {
        super.onPause();

        // disableForegroundDispatchSystem();
        unregisterReceiver(networkStateReceiver);
    }


    public String formateofechaLista(String fecha){

        String dia,mes,año,hor,min,seg;

        dia=fecha.substring(0,2);
        mes=fecha.substring(2,4);
        año=fecha.substring(4,8);
        hor=fecha.substring(8,10);
        min=fecha.substring(10,12);
        seg=fecha.substring(12,14);

        String fechaf=dia+"/"+mes+"/"+año+" "+hor+":"+min+":"+seg;




        return fechaf;
    }




}
