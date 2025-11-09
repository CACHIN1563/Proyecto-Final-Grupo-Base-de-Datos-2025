package util;
import java.sql.*;

public class DB {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:umg";
    private static final String USER = "system";
    private static final String PASS = "Umg$2025";
    static {
        try { Class.forName("oracle.jdbc.driver.OracleDriver"); } catch(Exception ignored){}
    }
    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
