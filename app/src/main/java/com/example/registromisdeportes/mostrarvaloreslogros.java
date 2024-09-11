package com.example.registromisdeportes;

public class mostrarvaloreslogros
{
    String deporte;
    String duracion;

    public mostrarvaloreslogros(String deporte, String duracion) {
        this.deporte = deporte;
        this.duracion = duracion;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}
