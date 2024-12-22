package fourcorp.buildflow.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final String URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private final String USERNAME = "fourcorp";
    private final String PASSWORD = "1234";
    private Connection connection;

    /**
     * Establishes a connection to the database.
     */
    public void connect() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        connect();
        return connection;
    }
}
