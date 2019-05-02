package com.tutorialesprogramacionya.proyecto019;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singleton {

    private static Singleton mInstance;
    private RequestQueue requestQueue;
    private static Context mcontext;


    private Singleton(Context context) {

        mcontext = context;
        requestQueue = getRequestQueue();


    }

    private RequestQueue getRequestQueue()

    {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(mcontext.getApplicationContext());
        return requestQueue;

    }

    public static synchronized Singleton getInstance(Context context){

        if(mcontext==null){

            mInstance = new Singleton(context);
        }
        return mInstance;

    }

    public <T> void addToRequestQue(Request request)
    {
        getRequestQueue().add(request);


    }
}