package com.tutorialesprogramacionya.proyecto019;

public class Datos {
    private int  cod,dispositivo;
    private String status;
    private String nserie,descripcion,precio,fecha_registro;


   public Datos() {
        }

    public Datos(int dispositivo,int cod, String nserie, String descripcion, String precio, String fecha_registro, String status) {
       this.dispositivo = dispositivo;
       this.cod = cod;
        this.nserie = nserie;
        this.descripcion = descripcion;
        this.precio = precio;
        this.fecha_registro = fecha_registro;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(int dispositivo) {
        this.dispositivo = dispositivo;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNserie() {
        return nserie;
    }

    public void setNserie(String nserie) {
        this.nserie = nserie;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    @Override
    public String toString() {
        return descripcion+" | "+precio+" | "+fecha_registro;


    }
}
