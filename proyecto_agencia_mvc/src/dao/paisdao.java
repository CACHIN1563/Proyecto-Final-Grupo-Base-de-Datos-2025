package dao;
import util.DB;
import model.pais;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class paisdao {

    public List<pais> listar() throws Exception {
        List<pais> lista = new ArrayList<>();
        String sql = "SELECT id_pais, nombre FROM pais ORDER BY nombre";
        try (Connection con = DB.get(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pais p = new pais();
                p.setid_pais(rs.getLong("id_pais"));
                p.setnombre(rs.getString("nombre"));
                lista.add(p);
            }
        }
        return lista;
    }

    public void insert(pais t) throws Exception {
        String sql = "INSERT INTO pais(nombre) VALUES(?)";
        try (Connection c = DB.get();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, t.getnombre());
            ps.executeUpdate();
        }
    }

    public void update(pais t) throws Exception {
        String sql = "UPDATE pais SET nombre = ? WHERE id_pais = ?";
        try (Connection c = DB.get();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, t.getnombre());
            ps.setLong(2, t.getid_pais());
            ps.executeUpdate();
        }
    }

    public void delete(Long id_pais) throws Exception {
        String sql = "DELETE FROM pais WHERE id_pais = ?";
        try (Connection c = DB.get();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id_pais);
            ps.executeUpdate();
        }
    }

}
