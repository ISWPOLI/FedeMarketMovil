package com.qantica.mundo;


public class DescargaItem {

    int id;
    String nombre, version, ico;
    boolean update;


    public DescargaItem(int id, String nombre, String version, boolean update, String ico) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.version = version;
        this.update = update;
        this.ico=ico;

    }



    public String getIco() {
        return ico;
    }
    public void setIco(String ico) {
        this.ico = ico;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public boolean isUpdate() {
        return update;
    }
    public void setUpdate(boolean update) {
        this.update = update;
    }





}

