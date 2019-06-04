package com.tutorialesprogramacionya.proyecto019;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RecyclerMovimientoOnline extends AppCompatActivity {
    ArrayList<Datos> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MovimientoAdapterOnline adapter;
    private AsyncHttpClient cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_movimiento_online);
        recyclerView= (RecyclerView)findViewById(R.id.recyclerDatos);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MovimientoAdapterOnline(arrayList);
        recyclerView.setAdapter(adapter);
        cliente = new AsyncHttpClient();
        obtenerdatos();
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
     //   ArrayList <Datos> lista = new ArrayList <Datos>();

        try{
            JSONArray jsonarreglo = new JSONArray(respuesta);

            for (int i=0; i<jsonarreglo.length(); i++){
                Datos d = new Datos();

                d.setCod(jsonarreglo.getJSONObject(i).getInt("cod"));
                d.setNserie(jsonarreglo.getJSONObject(i).getString("nserie"));
                d.setDescripcion(jsonarreglo.getJSONObject(i).getString("descripcion"));
                d.setPrecio(jsonarreglo.getJSONObject(i).getString("precio"));
                d.setFecha_registro(formateofechaLista(jsonarreglo.getJSONObject(i).getString("fecha_registro")));
                d.setDispositivo(jsonarreglo.getJSONObject(i).getInt("dispositivo"));
                d.setEstacion(jsonarreglo.getJSONObject(i).getInt("estacion"));



                //Toast.makeText(this, formateofechaLista(d.getFecha_registro()), Toast.LENGTH_SHORT).show();


                arrayList.add(d);

            }
            //recyclerView = new recycler(new Datos(disp,cod,nserie,operacion,tarifa,fecha,estado));
            adapter.notifyDataSetChanged();


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



}
