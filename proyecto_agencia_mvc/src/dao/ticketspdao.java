package dao;

import java.sql.*;
import util.DB;

public class ticketspdao {
    public void agregarTicket(Long idReserva, Long idVuelo, String clase, String asiento, double precio) throws Exception {
        String call = "{ call sp_agregar_ticket(?, ?, ?, ?, ?) }";
        try (Connection c = DB.get();
            CallableStatement cs = c.prepareCall(call)) {

            cs.setLong(1, idReserva);
            cs.setLong(2, idVuelo);
            cs.setString(3, clase);
            if (asiento == null || asiento.trim().isEmpty()) {
                cs.setNull(4, Types.VARCHAR);
            } else cs.setString(4, asiento.trim());
            cs.setBigDecimal(5, java.math.BigDecimal.valueOf(precio));

            cs.execute();
        }
    }
}
