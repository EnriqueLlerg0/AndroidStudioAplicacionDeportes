package com.example.registromisdeportes;

public class clasemostrardatosactividades
{
    String nombre_deporte;
    String fecha;
    String duracion;

    public clasemostrardatosactividades(String nombre_deporte, String fecha, String duracion) {
        this.nombre_deporte = nombre_deporte;
        this.fecha = fecha;
        this.duracion = duracion;
    }

    public String getNombre_deporte() {
        return nombre_deporte;
    }

    public void setNombre_deporte(String nombre_deporte) {
        this.nombre_deporte = nombre_deporte;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}
