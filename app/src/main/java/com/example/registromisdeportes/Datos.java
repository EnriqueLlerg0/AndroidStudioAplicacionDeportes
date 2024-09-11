package com.example.registromisdeportes;

public class Datos {
    String deporte, descripcion, imagen;
    int id;

    @Override
    public String toString() {
        String cadena=deporte;
        return cadena;
    }

    public Datos(int id,String deporte, String descripcion, String imagen) {
        this.id=id;
        this.deporte = deporte;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public Datos(String deporte) {
        this.deporte = deporte;
    }

    public Datos(int id, String deporte) {
        this.id = id;
        this.deporte = deporte;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
