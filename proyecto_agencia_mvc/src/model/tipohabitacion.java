package model;

public class tipohabitacion {

    private Long id_tipo_hab;
    private String descripcion;

    public tipohabitacion() {}

    public Long getid_tipo_hab() {
        return id_tipo_hab;
    }

    public void setid_tipo_hab(Long id_tipo_hab) {
        this.id_tipo_hab = id_tipo_hab;
    }

    public String getdescripcion() {
        return descripcion;
    }

    public void setdescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion; // para combos o listas
    }
}
