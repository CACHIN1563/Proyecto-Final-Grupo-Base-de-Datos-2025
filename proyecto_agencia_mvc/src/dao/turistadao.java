package dao;

import model.turista;
import util.DB;
import java.sql.*;

public class turistadao {

    public void insert(turista t) throws Exception {
        String sql = "INSERT INTO turista(" +
                    "nombre1,nombre2,nombre3,apellido1,apellido2," +
                    "direccion,telefono1,telefono2,email,id_pais) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DB.get();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1,  t.getNombre1());
            ps.setString(2,  t.getNombre2());
            ps.setString(3,  t.getNombre3());
            ps.setString(4,  t.getApellido1());
            ps.setString(5,  t.getApellido2());
            ps.setString(6,  t.getDireccion());
            ps.setString(7,  t.getTelefono1());
            ps.setString(8,  t.getTelefono2());   // <- antes repetías el 7
            ps.setString(9,  t.getEmail());
            ps.setLong(10,   t.getid_pais());     // <- debe ser el parámetro 10
            ps.executeUpdate();
        }
    }

    public Long insertReturningId(turista t) throws Exception {
        String sql = "INSERT INTO turista(" +
                    "nombre1,nombre2,nombre3,apellido1,apellido2," +
                    "direccion,telefono1,telefono2,email,id_pais) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DB.get();
            PreparedStatement ps = c.prepareStatement(sql, new String[]{"ID_TURISTA"})) {

            ps.setString(1,  t.getNombre1());
            ps.setString(2,  t.getNombre2());
            ps.setString(3,  t.getNombre3());
            ps.setString(4,  t.getApellido1());
            ps.setString(5,  t.getApellido2());
            ps.setString(6,  t.getDireccion());
            ps.setString(7,  t.getTelefono1());
            ps.setString(8,  t.getTelefono2());
            ps.setString(9,  t.getEmail());
            ps.setLong(10,   t.getid_pais());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            throw new SQLException("No se obtuvo el ID_TURISTA generado.");
        }
    }
}
