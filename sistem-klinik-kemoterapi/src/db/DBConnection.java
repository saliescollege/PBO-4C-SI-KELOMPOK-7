package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/db_chemoclinic";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver tidak ditemukan.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("Gagal konek ke database.");
            e.printStackTrace();
            return null;
        }
    }
}
