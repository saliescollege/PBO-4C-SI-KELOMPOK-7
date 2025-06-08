package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_chemoclinic";
    private static final String USER = "root";  // Ganti sesuai konfigurasi
    private static final String PASS = "";      // Ganti sesuai konfigurasi

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
