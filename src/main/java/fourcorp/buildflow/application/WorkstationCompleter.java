package fourcorp.buildflow.application;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class WorkstationCompleter {

    private final String JDBC_URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private final String USERNAME = "fourcorp";
    private final String PASSWORD = "1234";

    private final String WORKSTATIONS_FILE = "textFiles/workstations.csv";

    /**
     * Atualiza o arquivo de workstations garantindo que cada operação tenha pelo menos uma workstation.
     */
    public void ensureCompleteWorkstationsFile() {
        Map<String, String> fileWorkstations = readWorkstationsFromFile();
        Map<Integer, String> operations = fetchOperationsFromDB();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WORKSTATIONS_FILE, true))) {
            for (Map.Entry<Integer, String> operation : operations.entrySet()) {
                String workstationId = "ws" + operation.getKey(); // ID da Workstation
                String entry = workstationId + ";" + operation.getValue();

                // Adicionar apenas se a Workstation ainda não existe no arquivo
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
     * Busca todas as operações diretamente da tabela Operation_Type.
     *
     * @return Um mapa onde a chave é o ID da operação e o valor é o nome e tempo da operação (formatados para CSV).
     */
    private Map<Integer, String> fetchOperationsFromDB() {
        Map<Integer, String> operations = new HashMap<>();
        String query = "SELECT ID, Description, Expec_Time FROM Operation_Type";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String operationName = formatOperationName(rs.getString("Description"));
                int time = rs.getInt("Expec_Time");
                String formattedEntry = operationName + ";" + time;
                operations.put(id, formattedEntry);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching operations from database: " + e.getMessage());
        }

        return operations;
    }

    /**
     * Lê as workstations existentes no arquivo CSV.
     *
     * @return Um mapa com o ID da workstation como chave e a entrada completa como valor.
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
     * Formata o nome da operação: Primeira letra maiúscula, restante minúscula.
     *
     * @param name O nome original da operação.
     * @return O nome formatado.
     */
    private String formatOperationName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        name = name.trim().toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
