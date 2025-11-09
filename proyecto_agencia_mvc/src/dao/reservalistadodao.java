package dao;

import java.sql.*;
import java.util.*;
import util.DB;

public class reservalistadodao {
    public List<Long> listarIdsPorTurista(Long idTurista) throws Exception {
        List<Long> out = new ArrayList<>();
        String sql = "SELECT id_reserva FROM reserva WHERE id_turista=? ORDER BY fecha_reserva DESC";
        try (Connection c = DB.get();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idTurista);
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()) out.add(rs.getLong(1));
            }
        }
        return out;
    }
    
    public List<String> listarRegimenPorTurista(Long idTurista, Long idReserva) throws Exception {
    	List<String> lista = new ArrayList<>();
        String sql = "SELECT regimen_hospedaje FROM reserva WHERE id_turista=? and id_reserva =? ORDER BY fecha_reserva DESC";
        try (Connection c = DB.get();
                PreparedStatement ps = c.prepareStatement(sql)) {
               ps.setLong(1, idTurista);
               ps.setLong(2, idReserva);

               try (ResultSet rs = ps.executeQuery()) {
                   while (rs.next()) {
                       lista.add(rs.getString("regimen_hospedaje"));
                   }
               }
           }
           return lista;
    }
}
