package com.tutorialesprogramacionya.proyecto019;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class ListadoAdapter extends RecyclerView.Adapter<ListadoAdapter.DatosViewHolder> {
    private ArrayList<Datos> arrayList = new ArrayList<>();

    public ListadoAdapter(ArrayList<Datos> arrayList){
        this.arrayList= arrayList;
    }
    @Override
    public DatosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new DatosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DatosViewHolder holder, int position) {
        holder.nserie.setText(arrayList.get(position).getNserie());
        holder.operacion.setText(arrayList.get(position).getDescripcion().toString());
        holder.tarifa.setText(arrayList.get(position).getPrecio().toString());
        holder.fecha.setText(formateofechaLista(arrayList.get(position).getFecha_registro()));
        holder.estado.setText(arrayList.get(position).getStatus());
        String sync_status = arrayList.get(position).getStatus();
        if(sync_status.equals("Enviado")){
           holder.status.setImageResource(R.drawable.si);

        }else{
            holder.status.setImageResource(R.drawable.no2);
        }
        String disp=String.valueOf(arrayList.get(position).getDispositivo());
        holder.dispositivo.setText(disp);



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class DatosViewHolder extends RecyclerView.ViewHolder{
        TextView operacion,tarifa,fecha,estado,dispositivo,nserie;
        ImageView status;
        public DatosViewHolder(View itemView){
            super(itemView);
            nserie = (TextView)itemView.findViewById(R.id.textnum);
            operacion = (TextView) itemView.findViewById(R.id.textOperacion);
            tarifa= (TextView) itemView.findViewById(R.id.textTarifa);
            fecha=(TextView) itemView.findViewById(R.id.textFecha);
            estado= (TextView)itemView.findViewById(R.id.textStatus);
            dispositivo = (TextView)itemView.findViewById(R.id.textdisp);
             status=(ImageView)itemView.findViewById(R.id.imagen);
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
