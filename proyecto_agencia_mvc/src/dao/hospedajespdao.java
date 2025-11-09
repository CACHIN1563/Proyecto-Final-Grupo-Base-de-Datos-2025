package dao;

import java.sql.*;
import util.DB;

public class hospedajespdao {
    public void agregarHospedaje(Long idReserva, Long idHotel, java.sql.Date fLleg, java.sql.Date fSal, int habitaciones, double precioTotal, Long idTipoHabitacion) throws Exception {
        String call = "{ call sp_agregar_hospedaje(?, ?, ?, ?, ?, ?, ?) }";
        try (Connection c = DB.get();
            CallableStatement cs = c.prepareCall(call)) {

            cs.setLong(1, idReserva);
            cs.setLong(2, idHotel);
            cs.setDate(3, fLleg);
            cs.setDate(4, fSal);
            cs.setInt(5, habitaciones);
            cs.setBigDecimal(6, java.math.BigDecimal.valueOf(precioTotal));
            cs.setLong(7, idTipoHabitacion);

            cs.execute();
        }
    }
}
