package qantica.com.mundo;

public class SubCategoria {

    private String id;
    private String nombre;
    private int id_categoria;
    private int id_subcategoria;
    private String estado;
    String icono;

    public SubCategoria(String id, String nombre, int id_categoria, int id_subcategoria, String estado, String icono) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.id_categoria = id_categoria;
        this.id_subcategoria = id_subcategoria;
        this.estado = estado;
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

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public int getId_subcategoria() {
        return id_subcategoria;
    }

    public void setId_subcategoria(int id_subcategoria) {
        this.id_subcategoria = id_subcategoria;
    }

    public String getEstado() {return estado;}

    public void setEstado(String estado) { this.estado = estado; }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}
