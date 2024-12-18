package fourcorp.buildflow.application;

import java.sql.*;

public class DefineInDB {
    private final String URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private final String USERNAME = "fourcorp";
    private final String PASSWORD = "1234";
    private Connection conn;

    public DefineInDB() {
        connect();
    }

    private void connect() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to " + URL + " as " + USERNAME + ".");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void defineAverageOperationTime(double time, String operationName) {
        try {
            String findOperationTypeIdQuery = "SELECT ID FROM Operation_Type WHERE Description = ?";
            PreparedStatement stmt = conn.prepareStatement(findOperationTypeIdQuery);
            stmt.setString(1, operationName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int operationTypeId = rs.getInt("ID");

                String checkExistQuery = "SELECT COUNT(*) FROM Average_Production_Operation WHERE Operation_TypeID = ?";
                stmt = conn.prepareStatement(checkExistQuery);
                stmt.setInt(1, operationTypeId);
                rs = stmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    String updateQuery = "UPDATE Average_Production_Operation SET time = ? WHERE Operation_TypeID = ?";
                    stmt = conn.prepareStatement(updateQuery);
                    stmt.setDouble(1, time);
                    stmt.setInt(2, operationTypeId);
                    stmt.executeUpdate();
                } else {
                    String insertQuery = "INSERT INTO Average_Production_Operation (Operation_TypeID, time) VALUES (?, ?)";
                    stmt = conn.prepareStatement(insertQuery);
                    stmt.setInt(1, operationTypeId);
                    stmt.setDouble(2, time);
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
