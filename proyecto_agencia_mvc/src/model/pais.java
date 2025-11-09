package model;

public class pais {
    private Long id_pais;
    private String nombre;

    public pais(){}

    public Long getid_pais() { return id_pais; }
    public void setid_pais(Long id_pais) { this.id_pais = id_pais; }

    public String getnombre() { return nombre; }
    public void setnombre(String nombre) { this.nombre = nombre; }
    
    @Override
    public String toString() {
        return nombre;
    }
}
