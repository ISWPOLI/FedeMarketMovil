package com.qantica.mundo;

public class Categoria {

    public String id;
    public String nombre;
    public String icono;

    public Categoria(String id, String nombre, String icono) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
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

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }



}
