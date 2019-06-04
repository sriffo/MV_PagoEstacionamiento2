package com.tutorialesprogramacionya.proyecto019;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MovimientoAdapterOnline extends RecyclerView.Adapter<MovimientoAdapterOnline.DatosViewHolder> {
    private ArrayList<Datos> arrayList = new ArrayList<>();

    public MovimientoAdapterOnline(ArrayList<Datos> arrayList){
        this.arrayList= arrayList;
    }
    @Override
    public DatosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_mov_online,parent,false);
        return new DatosViewHolder(view);

    }

    @Override
    public void onBindViewHolder(DatosViewHolder holder, int position) {
        holder.nserie.setText(arrayList.get(position).getNserie());
        holder.operacion.setText(arrayList.get(position).getDescripcion());
        holder.tarifa.setText(arrayList.get(position).getPrecio());
        holder.fecha.setText(arrayList.get(position).getFecha_registro());
    //    holder.estado.setText(arrayList.get(position).getStatus());
        String disp=String.valueOf(arrayList.get(position).getDispositivo());
        holder.dispositivo.setText(disp);
        Log.d("holder onlone","esta adentro");
        holder.estacion.setText(String.valueOf(arrayList.get(position).getEstacion()));





    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class DatosViewHolder extends RecyclerView.ViewHolder{
        TextView operacion,tarifa,fecha,estado,dispositivo,nserie,estacion;
        ImageView status;
        public DatosViewHolder(View itemView){
            super(itemView);
            nserie = (TextView)itemView.findViewById(R.id.textnum);
            operacion = (TextView) itemView.findViewById(R.id.textOperacion);
            tarifa= (TextView) itemView.findViewById(R.id.textTarifa);
            fecha=(TextView) itemView.findViewById(R.id.textFecha);
            dispositivo = (TextView)itemView.findViewById(R.id.textdisp);
            estacion = (TextView)itemView.findViewById(R.id.textest);

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

    public String obtnerestacion(int disp){
        String estacion="";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery(
                "select estacion from estacion where cod =  " + 1, null);
        if(fila.moveToFirst()){
            Log.d("datoslocal","esta en el if disp");
            do{
                estacion = fila.getString(0);
                Log.d("datoslocal", String.valueOf(fila.getInt(0)));

            }while (fila.moveToNext());

        }

        return  estacion;

    }




}
