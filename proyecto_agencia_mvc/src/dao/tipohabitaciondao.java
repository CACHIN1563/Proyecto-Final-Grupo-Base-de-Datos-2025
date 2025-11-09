package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.tipohabitacion;
import util.DB;

public class tipohabitaciondao {

    public List<tipohabitacion> listar() throws Exception {
        List<tipohabitacion> lista = new ArrayList<>();
        String sql = "SELECT id_tipo_hab, descripcion FROM tipo_habitacion ORDER BY id_tipo_hab";

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tipohabitacion t = new tipohabitacion();
                t.setid_tipo_hab(rs.getLong("id_tipo_hab"));
                t.setdescripcion(rs.getString("descripcion"));
                lista.add(t);
            }
        }
        return lista;
    }

    public void insert(tipohabitacion t) throws Exception {
        String sql = "INSERT INTO tipo_habitacion (descripcion) VALUES (?)";
        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, t.getdescripcion());
            ps.executeUpdate();
        }
    }

    public void update(tipohabitacion t) throws Exception {
        String sql = "UPDATE tipo_habitacion SET descripcion = ? WHERE id_tipo_hab = ?";
        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, t.getdescripcion());
            ps.setLong(2, t.getid_tipo_hab());
            ps.executeUpdate();
        }
    }

    public void delete(Long id) throws Exception {
        String sql = "DELETE FROM tipo_habitacion WHERE id_tipo_hab = ?";
        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
