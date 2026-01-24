package data;

import data.interfaces.IDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDB implements IDB {
    private String host;
    private String user;
    private String password;
    private String dbName;

    public PostgresDB(String host, String user, String password, String dbName) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.dbName = dbName;
    }

    @Override
    public Connection getConnection() throws SQLException {
        String connectionUrl = "jdbc:postgresql://" + host + "/" + dbName;
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(connectionUrl, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found", e);
        }
    }

    @Override
    public void close() {
    }
}