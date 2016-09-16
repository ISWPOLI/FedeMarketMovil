package com.qantica.mundo;

import qantica.com.fedemarket.R;

public class Contenido implements Comparable{

    public String id,nombre,descargas, descripcion, version, cap_1, cap_2, icono, estado;
    public int categoria,subcategoria;
    public double rating;


    public Contenido(){

    }

    public Contenido(String id, String nombre, String descargas,
                     String descripcion, String version, int categoria, int subcategoria, double rating, String cap_1, String cap_2, String icono, String estado) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.descargas = descargas;
        this.descripcion = descripcion;
        this.version = version;
        this.categoria = categoria;
        this.subcategoria=subcategoria;
        this.rating = rating;
        this.cap_1 =cap_1;
        this.cap_2 =cap_2;
        this.icono=icono;
        this.estado=estado;
    }


    /**
     * 0--> imagenes
     * 1--> videos
     * 2--> aplicaciones
     * 3--> juegos
     * @return
     */
    public Integer getImg(){

        Integer aux=0;

        switch (categoria) {
            case 0:
                aux= R.drawable.ico_cont_08;
                break;
            case 1:
                aux= R.drawable.ico_cont_11;
                break;
            case 2:
                aux= R.drawable.ico_cont_06;
                break;
            case 3:
                aux= R.drawable.ico_cont_01;
                break;

            default:
                aux= R.drawable.ico_cont_05;
                break;
        }

        return aux;
    }



    public int getCategoria() {
        return categoria;
    }
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescargas() {
        return descargas;
    }

    public void setDescargas(String descargas) {
        this.descargas = descargas;
    }



    public String getVersion() {
        return version;
    }



    public void setVersion(String version) {
        this.version = version;
    }



    public double getRating() {
        return rating;
    }



    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(int subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getCap_1() {
        return cap_1;
    }

    public void setCap_1(String cap_1) {
        this.cap_1 = cap_1;
    }

    public String getCap_2() {
        return cap_2;
    }

    public void setCap_2(String cap_2) {
        this.cap_2 = cap_2;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        return 0;
    }



}

