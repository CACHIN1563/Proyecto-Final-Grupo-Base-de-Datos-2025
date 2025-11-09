package model;

public class turista {
    private Long id_turista;
    private String nombre1;
    private String nombre2;
    private String nombre3;
    private String apellido1;
    private String apellido2;
    private String direccion;
    private String telefono1;
    private String telefono2;
    private String email;
    private Long id_pais;

    public turista(){}

    public Long getId() { return id_turista; }
    public void setId(Long id_turista) { this.id_turista = id_turista; }

    public String getNombre1() { return nombre1; }
    public void setNombre1(String nombre1) { this.nombre1 = nombre1; }

    public String getNombre2() { return nombre2; }
    public void setNombre2(String nombre2) { this.nombre2 = nombre2; }

    public String getNombre3() { return nombre3; }
    public void setNombre3(String nombre3) { this.nombre3 = nombre3; }

    public String getApellido1() { return apellido1; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }

    public String getApellido2() { return apellido2; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono1() { return telefono1; }
    public void setTelefono1(String telefono1) { this.telefono1 = telefono1; }

    public String getTelefono2() { return telefono2; }
    public void setTelefono2(String telefono2) { this.telefono2 = telefono2; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getid_pais() { return id_pais; }
    public void setid_pais(Long id_pais) { this.id_pais = id_pais; }
}
