package fourcorp.buildflow.application;

import fourcorp.buildflow.repository.Repositories;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code WorkstationCompleter} class ensures that every operation in the system
 * is associated with at least one workstation in the workstations file.
 * It reads existing workstations from a file, retrieves operations from the database,
 * and updates the file as necessary.
 */
public class WorkstationCompleter {
    private Connection conn;
    private final String WORKSTATIONS_FILE = "textFiles/workstations.csv";

    /**
     * Constructs a new {@code WorkstationCompleter} and initializes the database connection.
     */
    public WorkstationCompleter() {
        connect();
    }

    /**
     * Establishes a connection to the database using the {@link Repositories} class.
     */
    private void connect() {
        conn = Repositories.getInstance().getDatabase().getConnection();
    }

    /**
     * Ensures the workstations file contains entries for all operations.
     * If an operation is not associated with a workstation, a new workstation entry is added to the file.
     *
     * @throws SQLException if a database access error occurs
     */
    public void ensureCompleteWorkstationsFile() throws SQLException {
        Map<String, String> fileWorkstations = readWorkstationsFromFile();
        Map<Integer, String> operations = fetchOperationsFromDB();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WORKSTATIONS_FILE, true))) {
            for (Map.Entry<Integer, String> operation : operations.entrySet()) {
                String workstationId = "ws" + operation.getKey(); // Workstation ID
                String entry = workstationId + ";" + operation.getValue();

                // Add only if the workstation does not already exist in the file
                if (!fileWorkstations.containsKey(workstationId)) {
                    writer.write(entry);
                    writer.newLine();
                    System.out.println("Added workstation: " + entry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating workstations file: " + e.getMessage());
        }
    }

    /**
     * Fetches all operations from the database table `Operation_Type`.
     *
     * @return A map where the key is the operation ID, and the value is the formatted operation details for the CSV file
     * (operation name and time).
     * @throws SQLException if a database access error occurs
     */
    private Map<Integer, String> fetchOperationsFromDB() throws SQLException {
        Map<Integer, String> operations = new HashMap<>();
        String query = "SELECT ID, Description, Expec_Time FROM Operation_Type";

        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String operationName = formatOperationName(rs.getString("Description"));
                int time = rs.getInt("Expec_Time");
                String formattedEntry = operationName + ";" + time;
                operations.put(id, formattedEntry);
            }
        }

        return operations;
    }

    /**
     * Reads the existing workstations from the CSV file.
     *
     * @return A map where the key is the workstation ID, and the value is the full entry from the file.
     */
    private Map<String, String> readWorkstationsFromFile() {
        Map<String, String> workstations = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(WORKSTATIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String workstationId = parts[0].trim();
                    workstations.put(workstationId, line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Workstations file not found. A new file will be created.");
        } catch (IOException e) {
            System.err.println("Error reading workstations file: " + e.getMessage());
        }

        return workstations;
    }

    /**
     * Formats the operation name by capitalizing the first letter and making the rest lowercase.
     *
     * @param name the original operation name
     * @return the formatted operation name
     */
    private String formatOperationName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        name = name.trim().toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}