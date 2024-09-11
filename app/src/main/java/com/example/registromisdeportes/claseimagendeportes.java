package com.example.registromisdeportes;

public class claseimagendeportes
{
    String imagen;
    String deportes;

    public claseimagendeportes(String deportes, String imagen) {
        this.deportes = deportes;
        this.imagen = imagen;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDeportes() {
        return deportes;
    }

    public void setDeportes(String deportes) {
        this.deportes = deportes;
    }
}
