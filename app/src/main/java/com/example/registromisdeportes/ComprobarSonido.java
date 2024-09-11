package com.example.registromisdeportes;

import android.app.Application;

public class ComprobarSonido extends Application
{
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
