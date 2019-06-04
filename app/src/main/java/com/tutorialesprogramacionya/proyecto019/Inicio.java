package com.tutorialesprogramacionya.proyecto019;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.util.List;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Inicio extends AppCompatActivity  {
    Spinner spinner;
    Spinner spinner2;
    Button button11;
    EditText inicio;
    public ProgressBar progressBar;
    private AsyncHttpClient cliente;
    int dispo,estacion;
    String disp,est;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        progressBar = (ProgressBar) findViewById(R.id.barra);
        button11 = (Button) findViewById(R.id.button11);
        inicio = (EditText) findViewById(R.id.editText);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        cliente = new AsyncHttpClient();
       obtenerdatos();
        obtenerdatosdisp();



        String[] spinnerlists = getdipositivo();

        ArrayAdapter<String> adapters = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerlists);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapters);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                disp= adapterView.getItemAtPosition(i).toString();
            //   dispo=numerodisp(disp);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] spinnerlist = getestaciones();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               est= adapterView.getItemAtPosition(i).toString();
                //obetener numero disp
             //   estacion=numeroest(disp);
               // Log.d("est",disp);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });












        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(Inicio.this)
                        .setTitle("BIENVENIDO")
                        //.setMessage("Datos pueden estar incompletos")
                        .setPositiveButton("Ingresar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent i = new Intent(Inicio.this, MainActivity.class);
                                i.putExtra("dispinicio", numerodisp(disp));
                                i.putExtra("disestacion",numeroest(est));
                                Log.d("putextra", String.valueOf(numerodisp(disp)));
                                Log.d("putextra", String.valueOf(numeroest(est)));



                                startActivity(i);


                            }
                        })
                        .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //  Log.d("MainActivity", "Aborting mission...");
                                     /*   Intent i = new Intent(RecyclerMovimiento.this, MainActivity.class);
                                        startActivity(i);
                                        finish();*/
                            }
                        })
                        .show();

             /*   if (inicio.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(Inicio.this)
                            .setTitle("Error al ingresar")
                            .setMessage("Ingrese un numero de dispositivo")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                                    /*Activa activity mostrar datos*/
                                    // Intent i = new Intent(Inicio.this, MainActivity.class);
                                    //  i.putExtra("nserie", txtuid.getText().toString());
                                    //  startActivity(i);

/*
                                }
                            })
                            .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    finish();
                                }
                            })
                            .show();
                }*/

            }
        });

}



    private void obtenerdatos(){
        Log.d("debug","obtener datos");
        String url="https://appnfc.000webhostapp.com/estaciones.php?";
        final String parametros = "";
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Toast.makeText(Mostrar.this, "entro al if", Toast.LENGTH_SHORT).show();
                if (statusCode == 200 ){
                    Log.d("debug","obtener datos 200");
                    insertar(new String(responseBody));


                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void obtenerdatosdisp(){
        Log.d("debug","obtener datos disp");
        String url="https://appnfc.000webhostapp.com/dispositivos.php?";
        final String parametros = "";
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Toast.makeText(Mostrar.this, "entro al if", Toast.LENGTH_SHORT).show();
                if (statusCode == 200 ){
                    Log.d("debug","obtener datos 200");
                    insertardisp(new String(responseBody));


                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    public void insertar(String respuesta){
        //Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();
        //   ArrayList <Datos> lista = new ArrayList <Datos>();
        Log.d("debug","insertar");
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();
        database.delete("estacion",null,null);

        try{
            JSONArray jsonarreglo = new JSONArray(respuesta);

            for (int i=0; i<jsonarreglo.length(); i++){
                ContentValues contentValues = new ContentValues();
                contentValues.put("cod",jsonarreglo.getJSONObject(i).getInt("codigo"));
                Log.d("debug", String.valueOf(jsonarreglo.getJSONObject(i).getInt("codigo")));
                contentValues.put("estacion",jsonarreglo.getJSONObject(i).getString("estacion"));
                Log.d("debug", jsonarreglo.getJSONObject(i).getString("estacion"));
               // Log.d("debug", String.valueOf(jsonarreglo.getJSONObject(i).getInt("estacion")));


                database.insert("estacion",null,contentValues);

            }
            //recyclerView = new recycler(new Datos(disp,cod,nserie,operacion,tarifa,fecha,estado));
           // adapter.notifyDataSetChanged();
         database.close();

        }catch(Exception e){


        }


    }

    public void insertardisp(String respuesta){
        //Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();
        //   ArrayList <Datos> lista = new ArrayList <Datos>();
        Log.d("debug","insertar");
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();
        database.delete("dispositivos",null,null);

        try{
            JSONArray jsonarreglo = new JSONArray(respuesta);

            for (int i=0; i<jsonarreglo.length(); i++){
                ContentValues contentValues = new ContentValues();
                contentValues.put("cod",jsonarreglo.getJSONObject(i).getInt("codigo"));
                Log.d("debug", String.valueOf(jsonarreglo.getJSONObject(i).getInt("codigo")));
                contentValues.put("descripcion",jsonarreglo.getJSONObject(i).getString("descripcion"));
                Log.d("debug", jsonarreglo.getJSONObject(i).getString("descripcion"));
                // Log.d("debug", String.valueOf(jsonarreglo.getJSONObject(i).getInt("estacion")));


                database.insert("dispositivos",null,contentValues);

            }
            //recyclerView = new recycler(new Datos(disp,cod,nserie,operacion,tarifa,fecha,estado));
            // adapter.notifyDataSetChanged();
            database.close();

        }catch(Exception e){


        }


    }




    public String[] getestaciones(){
        ArrayList<String> spinner = new ArrayList<String>();
        AdminSQLiteOpenHelper  admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "select * from estacion", null);
        if(fila.moveToFirst()){
            Log.d("datoslocal","esta en el if");
            do{
                String estacion = fila.getString(1);
                Log.d("datoslocal",fila.getString(1));
                spinner.add(estacion);

            }while (fila.moveToNext());

        }
        fila.close();
        String[] allspinner = new String[spinner.size()];
        allspinner= spinner.toArray(allspinner);
        return allspinner;



    }

    public String[] getdipositivo(){
        ArrayList<String> spinner = new ArrayList<String>();
        AdminSQLiteOpenHelper  admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "select * from dispositivos", null);
        if(fila.moveToFirst()){
            Log.d("datoslocal","esta en el if disp");
            do{
                String estacion = fila.getString(1);
                Log.d("datoslocal",fila.getString(1));
                spinner.add(estacion);

            }while (fila.moveToNext());

        }
        fila.close();
        String[] allspinner = new String[spinner.size()];
        allspinner= spinner.toArray(allspinner);
        return allspinner;



    }
public int numerodisp(String disp){
    int estacion=0;
    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
            "administracion", null, 1);
    SQLiteDatabase bd = admin.getWritableDatabase();

    Cursor fila = bd.rawQuery(
            "select cod from dispositivos where descripcion = '" + disp +"'", null);
    if(fila.moveToFirst()){
        Log.d("datoslocal","esta en el if disp");
        do{
            estacion = fila.getInt(0);
            Log.d("datoslocal", String.valueOf(fila.getInt(0)));

        }while (fila.moveToNext());

    }

          return  estacion;

}

    public int numeroest(String disp){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery(
                "select cod from estacion where estacion = '" + disp +"'", null);
        if(fila.moveToFirst()){
            Log.d("datoslocal","esta en el if disp");
            do{
                estacion = fila.getInt(0);
                Log.d("datoslocal", String.valueOf(fila.getInt(0)));

            }while (fila.moveToNext());

        }

        return  estacion;

    }



}
