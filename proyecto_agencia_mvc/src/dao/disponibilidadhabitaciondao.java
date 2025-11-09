package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.disponibilidadhabitacion;
import util.DB;

public class disponibilidadhabitaciondao {

    public List<disponibilidadhabitacion> listar() throws Exception {
        List<disponibilidadhabitacion> lista = new ArrayList<>();

        String sql = """
            SELECT d.id_disponibilidad,
                   d.id_hotel,
                   h.nombre AS hotel,
                   d.id_tipo_hab,
                   t.descripcion AS tipo_hab,
                   d.plazas_disponibles
            FROM disponibilidad_habitacion d
            JOIN hotel h ON d.id_hotel = h.id_hotel
            JOIN tipo_habitacion t ON d.id_tipo_hab = t.id_tipo_hab
            ORDER BY h.nombre, t.descripcion
            """;

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                disponibilidadhabitacion d = new disponibilidadhabitacion();
                d.setid_disponibilidad(rs.getLong("id_disponibilidad"));
                d.setid_hotel(rs.getLong("id_hotel"));
                d.setNombreHotel(rs.getString("hotel"));
                d.setid_tipo_hab(rs.getLong("id_tipo_hab"));
                d.setTipoHabitacion(rs.getString("tipo_hab"));
                d.setplazas_disponibles(rs.getInt("plazas_disponibles"));
                lista.add(d);
            }
        }

        return lista;
    }

    public void insert(disponibilidadhabitacion d) throws Exception {
        String sql = """
            INSERT INTO disponibilidad_habitacion
                (id_hotel, id_tipo_hab, plazas_disponibles)
            VALUES (?, ?, ?)
            """;

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, d.getid_hotel());
            ps.setLong(2, d.getid_tipo_hab());
            ps.setInt(3, d.getplazas_disponibles());

            ps.executeUpdate();
        }
    }

    public void update(disponibilidadhabitacion d) throws Exception {
        String sql = """
            UPDATE disponibilidad_habitacion
            SET id_hotel = ?, id_tipo_hab = ?, plazas_disponibles = ?
            WHERE id_disponibilidad = ?
            """;

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, d.getid_hotel());
            ps.setLong(2, d.getid_tipo_hab());
            ps.setInt(3, d.getplazas_disponibles());
            ps.setLong(4, d.getid_disponibilidad());

            ps.executeUpdate();
        }
    }

    public void delete(Long id) throws Exception {
        String sql = "DELETE FROM disponibilidad_habitacion WHERE id_disponibilidad = ?";

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
