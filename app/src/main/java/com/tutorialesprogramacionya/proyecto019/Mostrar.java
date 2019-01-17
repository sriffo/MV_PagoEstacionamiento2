package com.tutorialesprogramacionya.proyecto019;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.simple.parser.JSONParser;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class Mostrar extends Activity {

    private ListView lvlList;
    private AsyncHttpClient cliente;
    Button button11;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTitle("Movimientos");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar);
        ListView lvlList = (ListView)findViewById(R.id.lvlList);

        button11= (Button)findViewById(R.id.button11);

        cliente = new AsyncHttpClient();



        if(isOnline()==true){
            obtenerdatos();

        }else {
            new AlertDialog.Builder(this)
                    .setTitle("No hay red")
                    .setMessage("Datos pueden estar incompletos")
                    .setPositiveButton("Mostrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                            listaroffline();
                        }
                    })
                    .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  Log.d("MainActivity", "Aborting mission...");
                            Intent i = new Intent(Mostrar.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .show();
        }

        
button11.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        /*Intent i= new Intent(Mostrar.this,MainActivity.class);
        startActivity(i);*/
        moveTaskToBack(true);

    }
});


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

    private void obtenerdatos(){
        String numero= getIntent().getStringExtra("nserie");
        String url="https://appnfc.000webhostapp.com/obtener.php?";
        final String parametros = "nserie="+numero;
        cliente.post(url+parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
               // Toast.makeText(Mostrar.this, "entro al if", Toast.LENGTH_SHORT).show();
                if (statusCode == 200 ){
                   // Toast.makeText(Mostrar.this, "entro al if 2", Toast.LENGTH_SHORT).show();
                    listardatos(new String(responseBody));
              //    Toast.makeText(Mostrar.this, parametros, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }
    public void listardatos(String respuesta){
        //Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();
        ArrayList <Datos> lista = new ArrayList <Datos>();

        try{
            JSONArray jsonarreglo = new JSONArray(respuesta);

            for (int i=0; i<jsonarreglo.length(); i++){
                    Datos d = new Datos();

               d.setCod(jsonarreglo.getJSONObject(i).getInt("cod"));
                d.setNserie(jsonarreglo.getJSONObject(i).getString("nserie"));
               d.setDescripcion(jsonarreglo.getJSONObject(i).getString("descripcion"));
                d.setPrecio(jsonarreglo.getJSONObject(i).getString("precio"));
               d.setFecha_registro(formateofechaLista(jsonarreglo.getJSONObject(i).getString("fecha_registro")));
              //Toast.makeText(this, formateofechaLista(d.getFecha_registro()), Toast.LENGTH_SHORT).show();

               lista.add(d);

            }
          // lvlList.setAdapter(new ArrayAdapter<Datos>(this,android.R.layout.simple_expandable_list_item_1,lista));
            ListView lvlList = (ListView)findViewById(R.id.lvlList);
          ArrayAdapter <Datos> a =new ArrayAdapter(this,android.R.layout.simple_list_item_1,lista);
           // ArrayAdapter <Datos> b =new ArrayAdapter(this,android.R.layout.simple_list_item_1,as);
           // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            lvlList.setAdapter(a);

        }catch(Exception e){


        }


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


    public void listaroffline(){

        ListView lvlList = (ListView)findViewById(R.id.lvlList);
        ArrayList <String> lista = new ArrayList<>();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
       // AdminSQLiteOpenHelper juego = new AdminSQLiteOpenHelper(this, "juego", null, 5);
        SQLiteDatabase bd = admin.getWritableDatabase();
       // SQLiteDatabase bd = juego.getWritableDatabase();
        String descri =  getIntent().getStringExtra("nserie");
        Cursor fila = bd.rawQuery(
                "select descripcion,precio,fecha_registro from articulos where nserie='" + descri +"'", null);
        if(fila.moveToFirst()){
            do{
                lista.add(fila.getString(0) + " | " + fila.getString(1) + " | " + formateofechaLista(fila.getString(2)));
            }while(fila.moveToNext());
        }
        bd.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        lvlList.setAdapter(adapter);
//descripcion+" | "+precio+" | "+fecha_registro;

    }




}
