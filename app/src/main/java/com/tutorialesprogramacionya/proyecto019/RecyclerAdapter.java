package com.tutorialesprogramacionya.proyecto019;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.viewholder> {


    private ArrayList<Datos> arrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<Datos> arrayList)
    {
        this.arrayList = arrayList;


    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
     // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.);
        return null;
    }

    @Override
    public void onBindViewHolder(viewholder holder, int position) {
     arrayList.get(position).getDescripcion();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class viewholder extends RecyclerView.ViewHolder{

        ImageView sync_status;


        public viewholder(View itemView) {
            super(itemView);
           // sync_status = (ImageView)itemView.findViewById(R.id.
        }
    }
}
