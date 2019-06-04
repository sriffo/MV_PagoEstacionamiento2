package com.tutorialesprogramacionya.proyecto019;

public class Estaciones {

    private int codigo;
    private String estacion;

    public Estaciones(){}

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public Estaciones(int codigo, String estacion) {
        this.codigo = codigo;
        this.estacion = estacion;
    }
}
