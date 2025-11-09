package dao;

import java.sql.*;
import model.usuario;
import util.DB;

public class usuariodao {

    public void insertCliente(String username, String plainPassword, Long idTurista) throws Exception {
        String sql = "INSERT INTO usuario(username, password, id_role, id_turista) " +
                     "VALUES(?, ?, (SELECT id_role FROM rol WHERE descripcion='CLIENTE'), ?)";
        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, plainPassword);
            ps.setLong(3, idTurista);
            ps.executeUpdate();
        }
    }

    public usuario login(String username, String plainPassword) throws Exception {
        String sql = """
            SELECT id_usuario, username, email, password, id_role, id_turista, activo
            FROM usuario
            WHERE username = ? AND activo = 'SI'
            """;

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");

                    if (!storedPassword.equals(plainPassword)) {
                        return null;
                    }

                    usuario u = new usuario();
                    u.setIdUsuario(rs.getLong("id_usuario"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(storedPassword);
                    u.setEmail(rs.getString("email"));
                    u.setRole(rs.getLong("id_role"));
                    u.setIdTurista(rs.getLong("id_turista"));
                    u.setActivo(rs.getString("activo"));
                    return u;
                }
            }
        }
        return null; 
    }
}
