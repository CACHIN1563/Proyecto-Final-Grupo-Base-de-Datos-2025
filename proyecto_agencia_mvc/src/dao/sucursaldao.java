package dao;

import java.sql.*;
import java.util.ArrayList;
import model.sucursal;
import util.DB;
import java.util.*;

public class sucursaldao {

    public void insert(sucursal s) throws Exception {
        String sql = "INSERT INTO sucursal (direccion, telefono, id_pais) VALUES (?, ?, ?)";
        try (Connection c = DB.get();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getdireccion());
            ps.setString(2, s.gettelefono());
            ps.setLong(3, s.getid_pais());
            ps.executeUpdate();
        }
    }

    public void update(sucursal s) throws Exception {
        String sql = "UPDATE sucursal SET direccion = ?, telefono = ?, id_pais = ? WHERE id_sucursal = ?";
        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getdireccion());
            ps.setString(2, s.gettelefono());
            ps.setLong(3, s.getid_pais());
            ps.setLong(4, s.getid_sucursal()); 

            ps.executeUpdate();
        }
    }


    public void delete(Long id) throws Exception {
        String sql = "DELETE FROM sucursal WHERE id_sucursal = ?";
        try (Connection c = DB.get();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public List<sucursal> listar() throws Exception {
        List<sucursal> lista = new ArrayList<>();
        String sql = """
            SELECT s.id_sucursal,
                   s.direccion,
                   s.telefono,
                   s.id_pais,
                   p.nombre AS pais
            FROM sucursal s
            JOIN pais p ON s.id_pais = p.id_pais
            ORDER BY s.id_sucursal
            """;

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                sucursal s = new sucursal();
                s.setid_sucursal(rs.getLong("id_sucursal"));
                s.setdireccion(rs.getString("direccion"));
                s.settelefono(rs.getString("telefono"));
                s.setid_pais(rs.getLong("id_pais"));   
                s.setNombrePais(rs.getString("pais")); 
                lista.add(s);
            }
        }
        return lista;
    }


}
