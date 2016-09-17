package com.qantica.mundo;


public class Comentario {

    public String usuario;
    public String descripcion;
    public double rating;

    public Comentario(String usuario, String descripcion, double rating) {
        super();
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.rating = rating;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }



}
