package com.tutorialesprogramacionya.proyecto019;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.PendingIntent;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> str_NfcTagId;
    private EditText et1,et2,et3,et4;
    NfcAdapter nfcAdapter;
    ToggleButton tglReadWrite;
    EditText txtTagContent;
    TextView txtuid;
    Button button6;
    Button button9;
    private String time;
    private int  numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tglReadWrite = (ToggleButton)findViewById(R.id.tglReadWrite);
        txtTagContent = (EditText)findViewById(R.id.txtTagContent);
        txtuid= (TextView)findViewById(R.id.txtuid);
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        button6= (Button)findViewById(R.id.button6);
        button9= (Button)findViewById(R.id.button9);
        et4=(EditText)findViewById(R.id.et4);

    }

    @Override
    protected void onResume() {
        super.onResume();

        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }

    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));

    }
    protected void onNewIntent(Intent intent){
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
                    /*String numero= txtuid.getText().toString();
                   int decimal = Integer.parseInt(numero, 16);
                    String num= Integer.toString(decimal);*/
                   // et1.setText(bin2hex(tag.getId()));

                    readTextFromMessage((NdefMessage) parcelables[0]);
                }else{

                   // txtuid.setText(bin2hex(tag.getId()));
                    Toast.makeText(this, "Lectura vacía", Toast.LENGTH_SHORT).show();
                }

            }else {


                //String currentTime = Calendar.getInstance().getTime().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                time=sdf.format(new Date()).trim();
              // Toast.makeText(this, time, Toast.LENGTH_SHORT).show();
                  int numero= getultimo();
                 String numerod= Integer.toString(numero);
                //Toast.makeText(this, numerod, Toast.LENGTH_SHORT).show();
                     /*aca tiene que estar el return */
                NdefMessage ndefMessage = createNdefMessage( numerod+"|"+txtTagContent.getText()+"|"+time );
                writeNdefMessage(tag, ndefMessage);
                //Toast.makeText(this,bin2hex(tag.getId()),Toast.LENGTH_LONG).show();
                txtuid.setText(bin2hex(tag.getId()));

            }


        }
        }

    public int[] bytearray2intarray(byte[] barray)
    {
        int[] iarray = new int[barray.length];
        int i = 0;
        for (byte b : barray)
            iarray[i++] = b & 0xff;
        return iarray;
    }
    public static int[] convertToIntArray(byte[] input)
    {
        int[] ret = new int[input.length];
        for (int i = 0; i < input.length; i++)
        {
            ret[i] = input[i] & 0xff; // Range 0 to 255, not -128 to 127
        }
        return ret;
    }

    private final String CHARS = "0123456789ABCDEF";

    private String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(
                    CHARS.charAt(data[i] & 0x0F));
         int  x= Integer.parseInt(sb.toString(),16);
        }
        return sb.toString();
    }


    public void limpiar(View v){
          //  tglReadWrite.setText("");
            txtTagContent.setText("");
            txtuid.setText("");
            et1.setText("");
            et2.setText("");
            et3.setText("");
           et4.setText("");

}
    private void readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if(ndefRecords != null && ndefRecords.length>0){

            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextFromNdefRecord(ndefRecord);

            String substr=tagContent.substring(0,tagContent.indexOf("|"));
            et1.setText(substr);
           // Toast.makeText(this, substr, Toast.LENGTH_SHORT).show();
            txtTagContent.setText(tagContent);

           // et2.setText(tagContent);

        }else
        {
            Toast.makeText(this, "Lectura vacía", Toast.LENGTH_SHORT).show();
        }
        }

    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }

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

    }


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

                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                        "administracion", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();

                // String cod=String.valueOf(i1+1);

                String numeserie = txtuid.getText().toString();

                /*castear descripcion pare que gua*/
                String descri =txtTagContent.getText().toString();
                String pre = String.valueOf(i1);
                ContentValues registro = new ContentValues();
                //registro.put("cod", cod);
                registro.put("nserie",numeserie);
                registro.put("descripcion", descri);
                registro.put("precio", pre);
                registro.put("fecha_registro", time);
                bd.insert("articulos", null, registro);
                bd.close();


                Toast.makeText(this, "Escritura exitosa", Toast.LENGTH_SHORT).show();


            }

        } catch (Exception e) {
            Log.e("writeNdefMessage", e.getMessage());
        }

    }


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
    }


    private NdefMessage createNdefMessage(String content) {

        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

        return ndefMessage;
    }


    public void tglReadWriteOnClick(View view){
        txtTagContent.setText("");
    }


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
    }
    /***************************************************/
    /**************BASE DE DATOS************************/
    /***************************************************/
    public void altas(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod = et1.getText().toString();
        String descri = et2.getText().toString();
        String pre = et3.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("codigo", cod);
        registro.put("descripcion", descri);
        registro.put("precio", pre);
        bd.insert("articulos", null, registro);
        bd.close();
        et1.setText("");
        et2.setText("");
        et3.setText("");
        Toast.makeText(this, "Se cargaron los datos del artículo",
                Toast.LENGTH_SHORT).show();
    }
   public void alta(View v) {

       Random r = new Random();
       int i1 = (r.nextInt(80) + 65);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod = txtuid.getText().toString();
        String descri = txtTagContent.getText().toString();
        String pre = et3.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("codigo", cod);
        registro.put("descripcion", descri);
        registro.put("precio", pre);
        bd.insert("articulos", null, registro);
        bd.close();
        et1.setText("");
        et2.setText("");
        et3.setText("");
        Toast.makeText(this, "Se cargaron los datos del artículo",
                Toast.LENGTH_SHORT).show();
    }


    public int getultimo() {
        int numeroultimo=0;
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "select cod from articulos", null);
        if (fila.moveToLast()) {

            numeroultimo=fila.getInt(0);

        } else
         //   Toast.makeText(this, "Erorr",
           //         Toast.LENGTH_SHORT).show();
        bd.close();
        if (numeroultimo==0){

            return numeroultimo;
        }else{

            return numeroultimo +1;


        }



    }

    public void consultaporcodigo(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod = et1.getText().toString();
        Cursor fila = bd.rawQuery(
                "select descripcion, precio, fecha_registro from articulos where TRIM(cod)= '"+cod.trim()+"'", null);
        if (fila.moveToFirst()) {
           /* Toast.makeText(this, fila.getString(0),
                    Toast.LENGTH_SHORT).show();*/
            et2.setText(fila.getString(0));
            et3.setText(fila.getString(1));
            et4.setText(fila.getString(2));
        } else
            Toast.makeText(this, "No existe registro con dicho código",
                    Toast.LENGTH_SHORT).show();
        bd.close();
    }

    public void consultapordescripcion(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String descri = et2.getText().toString();
        Cursor fila = bd.rawQuery(
                "select codigo,precio,fecha_registro from articulos where descripcion='" + descri +"'", null);
        if (fila.moveToFirst()) {
            et1.setText(fila.getString(0));
            et3.setText(fila.getString(1));
        } else
            Toast.makeText(this, "No existe un artículo con dicha descripción",
                    Toast.LENGTH_SHORT).show();
        bd.close();
    }

    public void bajaporcodigo(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod= et1.getText().toString();
        int cant = bd.delete("articulos", "TRIM(cod)='" +cod.trim()+"'", null);
        bd.close();
        et1.setText("");
        et2.setText("");
        et3.setText("");
        if (cant == 1)
            Toast.makeText(this, "Se borró el registro con dicho código",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No existe un registro con dicho código",
                    Toast.LENGTH_SHORT).show();
    }

    public void borrar(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        bd.delete("articulos", null, null);
        numero=0;


    }

    public void modificacion(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod = et1.getText().toString();
        String descri = et2.getText().toString();
        String pre = et3.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("codigo", cod);
        registro.put("descripcion", descri);
        registro.put("precio", pre);
        int cant = bd.update("articulos", registro, "codigo=" + cod, null);
        bd.close();
        if (cant == 1)
            Toast.makeText(this, "se modificaron los datos", Toast.LENGTH_SHORT)
                    .show();
        else
            Toast.makeText(this, "no existe un artículo con el código ingresado",
                    Toast.LENGTH_SHORT).show();
    }


    /**********************NFC**********************/

















}





