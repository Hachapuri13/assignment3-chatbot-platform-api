package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/chatbot_platform";
    private static final String USER = "postgres";
    private static final String PASSWORD = "YourPostgresPassword";

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("DEBUG: Connected to database successfully.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("PostgreSQL Driver not found", e);
            }
        }
        return connection;
    }
}