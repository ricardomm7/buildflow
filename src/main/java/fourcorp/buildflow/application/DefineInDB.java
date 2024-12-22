package fourcorp.buildflow.application;

import fourcorp.buildflow.repository.Repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DefineInDB {
    private Connection conn;

    public DefineInDB() {
        connect();
    }

    private void connect() {
        conn = Repositories.getInstance().getDatabase().getConnection();
    }

    /**
     * Defines average operation time.
     *
     * @param time          the time
     * @param operationName the operation name
     */
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
