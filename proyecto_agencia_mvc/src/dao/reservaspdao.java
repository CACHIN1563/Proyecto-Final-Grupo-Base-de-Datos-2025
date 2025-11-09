package dao;

import java.sql.*;
import util.DB;

public class reservaspdao {

    public Long crearReserva(Long idSucursal, Long idTurista, String regimen, String obs) throws Exception {
        String call = "{ call sp_crear_reserva(?, ?, ?, ?, ?) }";
        try (Connection c = DB.get();
             CallableStatement cs = c.prepareCall(call)) {

            cs.setLong(1, idSucursal);
            cs.setLong(2, idTurista);
            cs.setString(3, regimen);
            cs.setString(4, obs);
            cs.registerOutParameter(5, Types.NUMERIC);

            cs.execute();
            return cs.getLong(5); // id_reserva OUT
        }
    }
}
