package model;

public class sucursal {
    private Long id_sucursal;
    private String direccion;
    private String telefono;
    private Long id_pais;
    private String nombrePais;

    public Long getid_sucursal() { return id_sucursal; }
    public void setid_sucursal(Long id_sucursal) { this.id_sucursal = id_sucursal; }

    public String getdireccion() { return direccion; }
    public void setdireccion(String direccion) { this.direccion = direccion; }

    public String gettelefono() { return telefono; }
    public void settelefono(String telefono) { this.telefono = telefono; }

    public Long getid_pais() { return id_pais; }
    public void setid_pais(Long id_pais) { this.id_pais = id_pais; }

    public String getNombrePais() { return nombrePais; }
    public void setNombrePais(String nombrePais) { this.nombrePais = nombrePais; }
    
    @Override
    public String toString() {
        return direccion + " - " + nombrePais; // 👈 así se mostrará en el combo
    }
}

