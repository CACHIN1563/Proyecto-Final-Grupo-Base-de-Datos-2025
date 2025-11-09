package model;

public class usuario {
    private Long id_usuario;
    private String username;
    private String email;
    private String password;
    private Long id_role;
    private String activo;
    private Long id_turista;

    public usuario() {}

    public Long getIdUsuario() { return id_usuario; }
    public void setIdUsuario (Long id_usuario) { this.id_usuario = id_usuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getActivo() { return activo; }
    public void setActivo(String activo) { this.activo = activo; }
    
    public Long getRole() { return id_role; }
    public void setRole(Long id_role) { this.id_role = id_role; }

    public Long getIdTurista() { return id_turista; }
    public void setIdTurista(Long id_turista) { this.id_turista = id_turista; }
    
    @Override
    public String toString() {
        return username;
    }
}
