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
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MostrarTodo extends AppCompatActivity {    //boton mostrar datos offline

    private ListView lsltodo;
    private AsyncHttpClient cliente;
    ArrayAdapter<String> adapter;
    ArrayList<String> lista;
    //Button button10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_todo);
        ListView lvlList = (ListView)findViewById(R.id.list_todo);
        cliente = new AsyncHttpClient();
        lista = new ArrayList<>();
        //button10 = (Button) findViewById(R.id.button10);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "select descripcion,precio,fecha_registro,status from articulos", null);

        if(fila.moveToFirst()){
            do{
                if(fila.getInt(3)==0){
                    lista.add(fila.getString(0) + " | " + fila.getString(1) + " | " + formateofechaLista(fila.getString(2)) + " | " + "Enviado");}
                else{
                    lista.add(fila.getString(0) + " | " + fila.getString(1) + " | " + formateofechaLista(fila.getString(2)) + " | " + "Pendiente");

                }

            }while(fila.moveToNext());
        }
     bd.close();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);;
        lvlList.setAdapter(adapter);




    }  //modulo principal,se inicializa todo al partir
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
    } // se formatea fecha para mostrar
    public void sync() {
        //arrayList.clear();

        Log.d("check", "sync" );
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("select * from articulos where status = 1", null);
        while (fila.moveToNext()) {
            String url = "https://appnfc.000webhostapp.com/agregar.php?";
            if(fila.getCount()>0){
                // Log.d("SYNC","");
                String parametros = "cod=" +fila.getInt(fila.getColumnIndex("cod")) + "&nserie=" + fila.getString(fila.getColumnIndex("nserie")) + "&descripcion=" + fila.getString(fila.getColumnIndex("descripcion")) + "&precio=" + fila.getString(fila.getColumnIndex("precio")) + "&fecha_registro=" + fila.getString(fila.getColumnIndex("fecha_registro"));
                final int numero= fila.getInt(fila.getColumnIndex("cod"));

                cliente.post(url + parametros, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200) {
                            Toast.makeText(MostrarTodo.this, "Datos sincronizados", Toast.LENGTH_SHORT).show();
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

    } // modulo se envio de transacciones una vez online
    public void modificacion(int cod) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        int status=0;
        registro.put("status",status);
        //registro.put("codigo", cod);

        int cant = bd.update("articulos", registro, "cod=" + cod, null);
        bd.close();
    } //modulo para modificar transaccion intern ana vez enviada
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

                lista.clear();
                ListView lvlList = (ListView)findViewById(R.id.list_todo);
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                        "administracion", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                Cursor fila = bd.rawQuery(
                        "select descripcion,precio,fecha_registro,status from articulos", null);

                if(fila.moveToNext()){
                    do{
                        if(fila.getInt(3)==0){
                            lista.add(fila.getString(0) + " | " + fila.getString(1) + " | " + formateofechaLista(fila.getString(2)) + " | " + "Enviado");}
                        else{
                            lista.add(fila.getString(0) + " | " + fila.getString(1) + " | " + formateofechaLista(fila.getString(2)) + " | " + "Pendiente");

                        }

                    }while(fila.moveToNext());
                }

                adapter.notifyDataSetChanged();
                fila.close();
                bd.close();

            }
        }
    } //muestra el listado de datos de todala base interna
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }
    protected void onPause() {
        super.onPause();

        // disableForegroundDispatchSystem();
        unregisterReceiver(networkStateReceiver);
    }

}
