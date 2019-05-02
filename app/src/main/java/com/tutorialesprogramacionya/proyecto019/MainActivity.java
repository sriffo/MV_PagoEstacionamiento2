package com.tutorialesprogramacionya.proyecto019;

import android.app.DownloadManager;
import android.content.AsyncTaskLoader;
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
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.PendingIntent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity  {
    /*Creacion de varibles*/
    private ArrayList<String> str_NfcTagId;
    private EditText et1,et2,et3,et4;
    NfcAdapter nfcAdapter;
    ToggleButton tglReadWrite,toggleButton3,toggleButton4;
    EditText txtTagContent;
    TextView txtuid;
    Button button6;
    Button button9;
    Button button7;
    Button button8;
    private String time;
    private int  numero;
    private ListView lvlList;
    private AsyncHttpClient cliente;
    private String pre;
    private String opcion="";
    ArrayList<Datos> arrayList = new ArrayList<>();
    BroadcastReceiver broadcastReceiver;
//    private ConnectivityManager red =



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Inicializacion de variables*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tglReadWrite = (ToggleButton) findViewById(R.id.tglReadWrite);
        toggleButton3 = (ToggleButton) findViewById(R.id.toggleButton3);
        toggleButton4 = (ToggleButton) findViewById(R.id.toggleButton4);
        txtTagContent = (EditText) findViewById(R.id.txtTagContent);
        txtuid = (TextView) findViewById(R.id.txtuid);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        button6 = (Button) findViewById(R.id.button6);
        button9 = (Button) findViewById(R.id.button9);
        button8 = (Button) findViewById(R.id.button8);
        et4 = (EditText) findViewById(R.id.et4);
        button7 = (Button) findViewById(R.id.button7);
        cliente = new AsyncHttpClient();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };


      //   new Sincronizacion().execute();
        /*precio definido hasta implementar metodo de cobro*/
        pre = "300";
        lvlList = (ListView) findViewById(R.id.lvlList);
        /*Se desactivan campos de textos*/
        txtTagContent.setEnabled(false);
        et1.setEnabled(false);
        et2.setEnabled(false);
        et3.setEnabled(false);
        et4.setEnabled(false);
        button7.setEnabled(false);
        /*Este if verifica si esta habilitado el NFC*/
        if (!nfcAdapter.isEnabled()) {
            /*Si esta deshabilitado se envia una alerta*/
            new AlertDialog.Builder(this)
                    .setTitle("Activa NFC")
                    .setMessage("El lector no esta encendido")
                    .setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*Se verifica version de android para desplegar menu de ajustes*/
                            if (android.os.Build.VERSION.SDK_INT >= 16) {
                                /*Menu actual*/
                                startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                            } else {
                                /*Menu antiguo*/
                                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            }

                        }
                    })   /*Se da la opcion de salir  no querer activar nfc */
                    .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                            System.exit(0);

                        }
                    })
                    .show();

        }

        /*Este listener es para limpiar, activar y descativar botones de entrada y salida*/
        tglReadWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*si esta en leer se desactivan los botones*/
                if (tglReadWrite.isChecked()) {
                    button7.setEnabled(true);
                    txtTagContent.setText("");
                    txtuid.setText("");
                    et1.setText("");
                    et2.setText("");
                    et3.setText("");
                    et4.setText("");
                    toggleButton3.setEnabled(false);
                    toggleButton4.setEnabled(false);
                } else {
                    /*si esta enescribir se activan los botones*/
                    button7.setEnabled(false);
                    txtuid.setText("");
                    et1.setText("");
                    et2.setText("");
                    et3.setText("");
                    et4.setText("");
                    toggleButton3.setEnabled(true);
                    toggleButton4.setEnabled(true);
                }
            }
        });

        /*Listeners para bloquear botones si otro se aprieta (entrada y salida) */
        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleButton3.isChecked()) {
                    toggleButton4.setEnabled(false);
                } else {
                    toggleButton4.setEnabled(true);

                }
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RecyclerListado.class);
                i.putExtra("dispositivo", getIntent().getIntExtra("dispinicio",0));

                startActivity(i);



                //sync();
            }
        });
        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleButton4.isChecked()) {
                    toggleButton3.setEnabled(false);
                } else {
                    toggleButton3.setEnabled(true);

                }
            }
        });



        /*Validacion de que no hay leido una tarjeta*/
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtuid.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "No ha leido ninguna Tarjeta!!!!!", Toast.LENGTH_SHORT).show();
                } else {
                    if(isOnline()==true){
                        Intent i = new Intent(MainActivity.this, RecyclerMovimientoOnline.class);
                        i.putExtra("nserie", txtuid.getText().toString());
                        startActivity(i);

                    }else {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("No hay red")
                                .setMessage("Datos pueden estar incompletos")
                                .setPositiveButton("Mostrar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                                        /*Activa activity mostrar datos*/
                                        Intent i = new Intent(MainActivity.this, RecyclerMovimiento.class);
                                        i.putExtra("nserie", txtuid.getText().toString());
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
                    }


                }
            }
        });


    } //inicializacion de modulos y variables

    public boolean checkred(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!= null && networkInfo.isConnected());

    }//modulo para ver el estado de red
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
/*Modulos para obtener datos e insertar en BD externa*/
    private void Almacenar(){
          int num =getultimo();
          Datos d = new Datos();
          //int num;
        int numero= getIntent().getIntExtra("dispinicio",0);
          d.setDispositivo(numero);
          d.setCod(num);
          d.setNserie(txtuid.getText().toString());
          d.setDescripcion(opcion.trim());
          d.setFecha_registro(time);
          d.setPrecio(pre);
          insertar(d);


          }
          private void insertar(Datos d){
              Log.d("check","esta en insertar");
             String url = "https://appnfc.000webhostapp.com/agregar.php?";
              Log.d("check","Datos:" +d.getDispositivo() +d.getCod()+d.getNserie()+d.getDescripcion()+d.getPrecio()+d.getFecha_registro());
             final String parametros = "cod="+d.getCod()+"&nserie="+d.getNserie()+"&descripcion="+d.getDescripcion()+"&precio="+d.getPrecio()+"&fecha_registro="+d.getFecha_registro()+"&dispositivo="+d.getDispositivo();
             // Toast.makeText(this, parametros, Toast.LENGTH_SHORT).show();
              Log.d("parametros",parametros.toString());
              final String nseries = d.getNserie();
              final String descripcions = d.getDescripcion();
              final String precios = d.getPrecio();
              final String Fecha = d.getFecha_registro();
              final int disp = d.getDispositivo();
             cliente.post(url+parametros, new AsyncHttpResponseHandler() {
                 @Override
                 public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                     if(statusCode == 200) {
                         Toast.makeText(MainActivity.this, "Se guardo en servidor externo", Toast.LENGTH_SHORT).show();
                         guardaralocal(nseries,descripcions,precios,Fecha,"Enviado",disp);

                     }}


                 @Override
                 public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                     Toast.makeText(MainActivity.this, "No hay conexion con servidor externo", Toast.LENGTH_SHORT).show();
                     guardaralocal(nseries,descripcions,precios,Fecha,"Pendiente",disp);
                 }
             });
          }
/*modulos obligatorios (constructores)*/
    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatchSystem();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
        unregisterReceiver(networkStateReceiver);
    }

    /*Metodo para transformar uid de tag nfc*/
    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));

    }

    /*Modulo principal nfc (lectura y escritura)*/
    protected void onNewIntent(Intent intent){
/*Verficicacion de nfc*/
        if (!nfcAdapter.isEnabled()){
            // Toast.makeText(this, "esta habilitado", Toast.LENGTH_SHORT).show();
            // Toast.makeText(this, "no esta habilitado", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(this)
                    .setTitle("Activa NFC")
                    .setMessage("El lector no esta encendido")
                    .setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                            if (android.os.Build.VERSION.SDK_INT >= 16) {
                                startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                            } else {
                                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            }

                        }
                    })
                    .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  Log.d("MainActivity", "Aborting mission...");
                            finish();
                            System.exit(0);

                        }
                    })
                    .show();

        }else{
            /*Se asigna a variable opcion la seleccion de entrada o salida*/
        if (toggleButton3.isChecked()){
            opcion="Entrada";
        }else if(toggleButton4.isChecked()){
            opcion="Salida";

        }
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)){
            Toast.makeText(this,"Leyendo", Toast.LENGTH_SHORT).show();

            /***ACA GATILLA LECTURA Y ESCRITURA***////
            if (tglReadWrite.isChecked()){

                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if(parcelables != null && parcelables.length > 0)
                {

                    txtTagContent.setText("");
                    txtuid.setText(bin2hex(tag.getId()));
                    readTextFromMessage((NdefMessage) parcelables[0]);
                }else{

                   // txtuid.setText(bin2hex(tag.getId()));
                    Toast.makeText(this, "Lectura vacía", Toast.LENGTH_SHORT).show();
                }

            }else if(opcion==""){
/*Validacion de ninuga seleccion (entrada o salida)*/
                Toast.makeText(this, "No ha seleccionado ninguna opción!!!!", Toast.LENGTH_SHORT).show();
            }else {

                button7.setEnabled(false);
            /*se setea el mensaje que se escribira en el tag*/
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
                time = sdf.format(new Date()).trim();

                int numero = getultimo();
                String opcion = "";
                String numerod = Integer.toString(numero);
                if (toggleButton3.isChecked()) {
                    opcion = "Entrada";
                } else if (toggleButton4.isChecked()) {
                    opcion = "Salida";

                }
                Log.d("MainActivity", "numero:"+numerod);

                NdefMessage ndefMessage = createNdefMessage(numerod + "|" + opcion + "|" + time);
                /*metodo que escribi en el tag*/
                writeNdefMessage(tag, ndefMessage);

                txtuid.setText(bin2hex(tag.getId()));
                toggleButton3.setEnabled(true);
                toggleButton3.setChecked(false);
                toggleButton4.setEnabled(true);
                toggleButton4.setChecked(false);



            }

        }
        }}

        private final String CHARS = "0123456789ABCDEF"; //utilitario para formatear tag
    public void limpiar(View v){
          //  tglReadWrite.setText("");
            txtTagContent.setText("");
            txtuid.setText("");
            et1.setText("");
            et2.setText("");
            et3.setText("");
            et4.setText("");

}//limpia pantalla
    private void readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if(ndefRecords != null && ndefRecords.length>0){

            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextFromNdefRecord(ndefRecord);
            String[] cadena= tagContent.split("\\|");
            et1.setText(cadena[0]);
            et2.setText(cadena[1]);
            et3.setText("300");
            et4.setText(formateofechaLista(cadena[2]));

           // String substr=tagContent.substring(0,tagContent.indexOf("|"));
            //List<String> parts = Arrays.asList(tagContent.split("|"));


           //Toast.makeText(this,cadena[0]+cadena[1]+cadena[2], Toast.LENGTH_SHORT).show();
            txtTagContent.setText(tagContent);

           // et2.setText(tagContent);

        }else
        {
            Toast.makeText(this, "Lectura vacía", Toast.LENGTH_SHORT).show();
        }
        }//metodo de lectura nfc
        private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }//metodo intent nfc
    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    } //utilitario nfc
    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {

            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag is not ndef formatable!", Toast.LENGTH_SHORT).show();
                return;
            }


            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Escritura exitosa", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }

    }//metodo para formatear mensaje nfc
    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage){

        try {

            if (tag == null) {
                Toast.makeText(this, "No se puede escribir null", Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) {
                // format tag with the ndef format and writes the message.
                formatTag(tag, ndefMessage);
            } else {
                ndef.connect();

                if (!ndef.isWritable()) {
                    Toast.makeText(this, "No se puede escribir en la tarjeta", Toast.LENGTH_SHORT).show();

                    ndef.close();
                    return;
                }

                et1.setText("");
                et2.setText("");
                et3.setText("");
                txtuid.setText("");

                Toast.makeText(this, "Se cargaron los datos de la tarjeta",
                        Toast.LENGTH_SHORT).show();


                  /*ESCRITURA*/
                txtuid.setText(bin2hex(tag.getId()));
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();



                Random r = new Random();
                int i1 = (r.nextInt(80) + 65);

              String numeserie = txtuid.getText().toString();

                Almacenar();
               // guardararemoto(numeserie,opcion.trim(),"300",time,1);



            }

        } catch (Exception e) {
            Log.e("writeNdefMessage", e.getMessage());
        }

    }//metodo de escritura nfc
    private NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

        } catch (UnsupportedEncodingException e) {
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }//metodo para crear mensaje (escritura)
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
    }//ya no se ocupa
    private NdefMessage createNdefMessage(String content) {

        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

        return ndefMessage;
    }//metodo para crear mensaje(lectura)
    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }//obtener mensaje de tag
    public int getultimo() {
        int numeroultimo=0;
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "select cod from articulos", null);
        if (fila.moveToLast()) {

            numeroultimo=fila.getInt(0);
            Log.d("MainActivity", "numero:"+numeroultimo);
        } else
         //   Toast.makeText(this, "Erorr",
           //         Toast.LENGTH_SHORT).show();
        bd.close();
        if (numeroultimo==0){

            return numeroultimo;
        }else{

            return numeroultimo +1;


        }



    }//obetenr ultimo registro sqlite
    public void borrar(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        bd.delete("articulos", null, null);
        numero=0;


    }//no se ocupa
    public void modificacion(int cod,int disp) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();

        registro.put("status","Enviado");
        //registro.put("codigo", cod);

        int cant = bd.update("articulos", registro, "cod=" + cod , null);
        bd.close();
    }//actualizar status al sync
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
                final int disp= fila.getInt(fila.getColumnIndex("dispositivo"));

     cliente.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    Toast.makeText(MainActivity.this, "Datos sincronizados", Toast.LENGTH_SHORT).show();
                modificacion(numero,disp);


                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "No hay conexion con servidor externo", Toast.LENGTH_SHORT).show();
            }
        });

    }else{

             Toast.makeText(MainActivity.this, "Datos ya sincronizados", Toast.LENGTH_SHORT).show();
         }



    }


}//sincronizacion
    private void guardararemoto(final String nseries, final String descripcions, final String precios, final String Fecha, final int disp){
    String url = "https://appnfc.000webhostapp.com/agregar2.php";
    Log.d("check","esta guardaremoto");
   // guardaralocal(nseries,descripcions,precios,Fecha,DbContract.SYNC_STATUS_FAILED);
if(checkred()){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    if (response.equals("OK")){
                        guardaralocal(nseries,descripcions,precios,Fecha,"Enviado",disp);

                    }else{
                       Log.d("check","esta en el else");
                        guardaralocal(nseries,descripcions,precios,Fecha,"Pendiente",disp);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                guardaralocal(nseries,descripcions,precios,Fecha,"Pendiente",disp);

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("nserie",nseries);
                params.put("descripcion",descripcions);
                params.put("precio",precios);
                params.put("fecha_registro",Fecha);

                return super.getParams();
            }
        }
        ;
       Singleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);

    }else{

       guardaralocal(nseries,descripcions,precios,Fecha,"Pendiente",disp);
    }


}//innsert remoto
    private void guardaralocal(String nseries, String descripcions, String precios, String Fecha,String sync, int disp){
    Log.d("check","esta guardalocal");
  //  AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
            "administracion", null, 1);
    SQLiteDatabase bd = admin.getWritableDatabase();
    SQLiteDatabase database = admin.getWritableDatabase();
    admin.guardainterna(nseries,descripcions,precios,Fecha,sync,disp,database);
  //  leerdesdelocal();
    admin.close();





}//insert local
    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  /*gatilla sync si esta */
            NetworkInfo ni = manager.getActiveNetworkInfo();
            onNetworkChange(ni);
        }
    };
    private void onNetworkChange(NetworkInfo networkInfo) {
        if (networkInfo != null) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                sync();
                Log.d("MenuActivity", "CONNECTED");
            } else {
                Log.d("MenuActivity", "DISCONNECTED");
            }
        }        }
        @Override
        protected void onStart() {
        super.onStart();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }
}





