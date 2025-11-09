package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.tarifahotel;
import util.DB;

public class tarifahoteldao {

    public List<tarifahotel> listar() throws Exception {
        List<tarifahotel> lista = new ArrayList<>();

        String sql = """
            SELECT th.id_tarifa,
                   th.id_hotel,
                   h.nombre AS hotel,
                   th.id_tipo_hab,
                   t.descripcion AS tipo_hab,
                   th.regimen,
                   th.precio_noche
            FROM tarifa_hotel th
            JOIN hotel h ON th.id_hotel = h.id_hotel
            JOIN tipo_habitacion t ON th.id_tipo_hab = t.id_tipo_hab
            ORDER BY h.nombre, t.descripcion, th.regimen
            """;

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tarifahotel th = new tarifahotel();
                th.setid_tarifa(rs.getLong("id_tarifa"));
                th.setid_hotel(rs.getLong("id_hotel"));
                th.setNombreHotel(rs.getString("hotel"));
                th.setid_tipo_hab(rs.getLong("id_tipo_hab"));
                th.setTipoHabitacion(rs.getString("tipo_hab"));
                th.setregimen(rs.getString("regimen"));
                th.setprecio_noche(rs.getDouble("precio_noche"));
                lista.add(th);
            }
        }
        return lista;
    }

    public void insert(tarifahotel th) throws Exception {
        String sql = "INSERT INTO tarifa_hotel (id_hotel, id_tipo_hab, regimen, precio_noche) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, th.getid_hotel());
            ps.setLong(2, th.getid_tipo_hab());
            ps.setString(3, th.getregimen());
            ps.setDouble(4, th.getprecio_noche());

            ps.executeUpdate();
        }
    }

    public void update(tarifahotel th) throws Exception {
        String sql = "UPDATE tarifa_hotel " +
                     "SET id_hotel = ?, id_tipo_hab = ?, regimen = ?, precio_noche = ? " +
                     "WHERE id_tarifa = ?";

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, th.getid_hotel());
            ps.setLong(2, th.getid_tipo_hab());
            ps.setString(3, th.getregimen());
            ps.setDouble(4, th.getprecio_noche());
            ps.setLong(5, th.getid_tarifa());

            ps.executeUpdate();
        }
    }

    public void delete(Long id) throws Exception {
        String sql = "DELETE FROM tarifa_hotel WHERE id_tarifa = ?";

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
    
    public Double buscarPrecio(Long idHotel, Long idTipoHab, String regimen) throws Exception {
        String sql = """
            SELECT precio_noche
            FROM tarifa_hotel
            WHERE id_hotel = ?
              AND id_tipo_hab = ?
              AND regimen = ?
            """;

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, idHotel);
            ps.setLong(2, idTipoHab);
            ps.setString(3, regimen);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("precio_noche");
                }
            }
        }
        return null;
    }

}
