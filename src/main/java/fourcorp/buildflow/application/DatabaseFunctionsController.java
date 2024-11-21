package fourcorp.buildflow.application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFunctionsController {
    private final String URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private final String USERNAME = "fourcorp";
    private final String PASSWORD = "1234";
    private Connection connection;
    private final String DATA_INSERTS_AND_CREATION = "textFiles/databaseSQL/tables-insert-and-drop.sql";
    private final String FUNCTIONS_AND_PROCEDURES = "textFiles/databaseSQL/functions-and-procedures.sql";

    public DatabaseFunctionsController() {
        System.out.println();
        connect();
        executeAllTasks();
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to " + URL + " as " + USERNAME + ".");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void executeAllTasks() {
        int totalSteps = calculateTotalSteps();
        int currentStep = 0;

        System.out.print("Initializing database setup:\n[-----------------------------------] 0%");
        currentStep = executeTask(DATA_INSERTS_AND_CREATION, ";", currentStep, totalSteps);
        currentStep = executeTask(FUNCTIONS_AND_PROCEDURES, "/", currentStep, totalSteps);

        displayProgressBar(totalSteps, totalSteps);
        System.out.println("\nDatabase setup completed successfully.");
    }

    private int calculateTotalSteps() {
        return countSteps(DATA_INSERTS_AND_CREATION, ";") + countSteps(FUNCTIONS_AND_PROCEDURES, "/");
    }

    private int countSteps(String filePath, String delimiter) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            count = content.toString().split(delimiter).length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private int executeTask(String filePath, String delimiter, int currentStep, int totalSteps) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n");
            }
            String[] sqlCommands = sql.toString().split(delimiter);

            for (String command : sqlCommands) {
                command = command.trim();
                if (!command.isEmpty()) {
                    try (Statement statement = connection.createStatement()) {
                        statement.execute(command);
                    } catch (SQLException e) {
                        if (e.getErrorCode() == 942) {
                            continue;
                        } else {
                            e.printStackTrace();
                        }
                    }
                    currentStep++;
                    displayProgressBar(currentStep, totalSteps);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentStep;
    }

    private void displayProgressBar(int current, int total) {
        int width = 35;
        int completed = (current * width) / total;
        int remaining = width - completed;

        String bar = "[" + "#".repeat(completed) + "-".repeat(remaining) + "] " +
                String.format("%d%%", (current * 100) / total);

        System.out.print("\r" + bar);
    }

    public void callSeeProductParts(String productId) {
        String query = "{? = call GetProductOperationParts(?)}"; // Ajuste conforme necessário para seu banco de dados
        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // Oracle usa REF_CURSOR para cursores
            callableStatement.setString(2, productId);

            callableStatement.execute();

            try (ResultSet rs = (ResultSet) callableStatement.getObject(1)) {
                List<String[]> rows = new ArrayList<>();
                rows.add(new String[]{"Part ID", "Quantity"}); // Cabeçalhos da tabela

                while (rs.next()) {
                    String partId = rs.getString(1);
                    int quantity = rs.getInt(2);
                    rows.add(new String[]{partId, String.valueOf(quantity)});
                }

                printTable(rows); // Método auxiliar para formatar a tabela
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void callGetProductOperationsAndWorkstations(String productId) {
        String query = "{? = call GetProductOperationsAndWorkstations(?)}"; // Ajuste conforme necessário para seu banco de dados
        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // Oracle usa REF_CURSOR para cursores
            callableStatement.setString(2, productId);

            callableStatement.execute();

            try (ResultSet rs = (ResultSet) callableStatement.getObject(1)) {
                List<String[]> rows = new ArrayList<>();
                rows.add(new String[]{"Operation ID", "Operation Designation", "Workstation Type ID"}); // Cabeçalhos da tabela

                while (rs.next()) {
                    String operationId = String.valueOf(rs.getInt(1)); // Operation ID
                    String operationDesignation = rs.getString(2);     // Operation Designation
                    String workstationTypeId = rs.getString(3);        // Workstation Type ID
                    rows.add(new String[]{operationId, operationDesignation, workstationTypeId});
                }

                printTable(rows); // Método para exibir os dados formatados em uma tabela
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void callPrintProductsUsingAllWorkstationTypes() {
        String query = "{call PrintProductsUsingAllWorkstationTypes}";

        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("BEGIN DBMS_OUTPUT.ENABLE(); END;");
            }

            callableStatement.execute();

            try (CallableStatement outputStatement = connection.prepareCall("{call DBMS_OUTPUT.GET_LINES(?, ?)}")) {
                outputStatement.registerOutParameter(1, Types.ARRAY, "SYS.DBMSOUTPUT_LINESARRAY");
                outputStatement.registerOutParameter(2, Types.INTEGER);

                outputStatement.execute();

                Array dbmsOutput = outputStatement.getArray(1);
                if (dbmsOutput != null) {
                    String[] outputLines = (String[]) dbmsOutput.getArray();
                    if (outputLines.length > 0) {
                        for (String line : outputLines) {
                            System.out.println(line);
                        }
                    } else {
                        System.out.println("There are no products using all the workstation types in the factory.");
                    }
                } else {
                    System.out.println("There are no products using all the workstation types in the factory.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printTable(List<String[]> rows) {
        int[] columnWidths = getColumnWidths(rows);

        String separator = "+";
        for (int width : columnWidths) {
            separator += "-".repeat(width + 2) + "+";
        }

        System.out.println(separator);
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            StringBuilder rowLine = new StringBuilder("|");
            for (int j = 0; j < row.length; j++) {
                rowLine.append(" ").append(String.format("%-" + columnWidths[j] + "s", row[j])).append(" |");
            }
            System.out.println(rowLine);

            if (i == 0) {
                System.out.println(separator);
            }
        }
        System.out.println(separator);
    }

    private int[] getColumnWidths(List<String[]> rows) {
        int columns = rows.get(0).length;
        int[] columnWidths = new int[columns];

        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }
        return columnWidths;
    }

    public void callDeactivateCustomer(String nif) {
        String query = "BEGIN DBMS_OUTPUT.PUT_LINE(DeactivateCustomer(?)); END;";

        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setString(1, nif);

            try (Statement statement = connection.createStatement()) {
                statement.execute("BEGIN DBMS_OUTPUT.ENABLE(); END;");
            }

            callableStatement.execute();

            try (CallableStatement outputStatement = connection.prepareCall("{call DBMS_OUTPUT.GET_LINES(?, ?)}")) {
                outputStatement.registerOutParameter(1, Types.ARRAY, "SYS.DBMSOUTPUT_LINESARRAY");
                outputStatement.registerOutParameter(2, Types.INTEGER);

                outputStatement.execute();

                Array dbmsOutput = outputStatement.getArray(1);
                if (dbmsOutput != null) {
                    String[] outputLines = (String[]) dbmsOutput.getArray();
                    if (outputLines.length > 0) {
                        System.out.println(outputLines[0]);
                    } else {
                        System.out.println("No output from DeactivateCustomer for NIF: " + nif);
                    }
                } else {
                    System.out.println("No output from DeactivateCustomer for NIF: " + nif);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void graphicVisualization(String pid) {
        while (true) {
            String productId = fetchProductId(pid);
            if (productId == null) {
                System.err.println("Produto não encontrado! Tente novamente.");
                continue;
            }

            System.out.println("Produto encontrado: " + productId);

            exportItems(connection, productId);
            exportBOO(connection, productId);
            exportOperations(connection, productId);
            return;
        }

    }

    private String fetchProductId(String userInput) {
        String sql = "SELECT Part_ID FROM Product WHERE Part_ID = ? OR Name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userInput);
            stmt.setString(2, userInput);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Part_ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void exportItems(Connection connection, String productId) {
        String sql = """
                SELECT DISTINCT Part_ID, Description 
                FROM Part 
                WHERE Part_ID = ? OR Part_ID IN (
                    SELECT Part_ID 
                    FROM Operation_Input 
                    WHERE Operation_ID IN (
                        SELECT Operation_ID FROM Operation WHERE Product_ID = ?
                    )
                )
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.setString(2, productId);

            try (ResultSet rs = stmt.executeQuery();
                 FileWriter writer = new FileWriter("textFiles/itemsLapr.csv")) {

                writer.write("id_item;item_name\n");
                while (rs.next()) {
                    String idItem = rs.getString("Part_ID");
                    String itemName = rs.getString("Description");
                    writer.write(idItem + ";" + itemName + "\n");
                }
                System.out.println("Arquivo items.csv gerado com sucesso.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void exportBOO(Connection connection, String productId) {
        String sql = """
                SELECT 
                    o.Operation_ID AS op_id,
                    o.Product_ID AS item_id,
                    oo.Quantity AS item_qtd,
                    o.NextOperation_ID AS predecessor_id,
                    oi.Part_ID AS input_item_id,
                    oi.Quantity AS input_quantity
                FROM 
                    Operation o
                LEFT JOIN 
                    Operation_Output oo ON o.Operation_ID = oo.Operation_ID
                LEFT JOIN 
                    Operation_Input oi ON o.Operation_ID = oi.Operation_ID
                WHERE 
                    o.Product_ID = ?
                ORDER BY 
                    o.Operation_ID, oi.Part_ID
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);

            try (ResultSet rs = stmt.executeQuery();
                 FileWriter writer = new FileWriter("textFiles/boo_v2Lapr.csv")) {

                writer.write("op_id;item_id;item_qtd;(;op1;op_qtd1;op2;op_qtd2;opN;op_qtdN;);(;item_id1;item_qtd1;item_id2;item_qtd2;item_idN;item_qtdN;)\n");

                String lastOpId = "";
                String currentItemId = "";
                double currentItemQtd = 0.0;
                StringBuilder predecessors = new StringBuilder();
                StringBuilder inputs = new StringBuilder();

                while (rs.next()) {
                    String opId = rs.getString("op_id");
                    String itemId = rs.getString("item_id");
                    double itemQtd = rs.getDouble("item_qtd");
                    String predecessorId = rs.getString("predecessor_id");
                    String inputItemId = rs.getString("input_item_id");
                    Double inputQuantity = rs.getDouble("input_quantity");

                    // Se mudarmos de operação, escreve a linha acumulada e reinicia os buffers
                    if (!opId.equals(lastOpId) && !lastOpId.isEmpty()) {
                        writer.write(String.format("%s;%s;%.3f;(;%s);(;%s)\n",
                                lastOpId,
                                currentItemId,
                                currentItemQtd,
                                padSection(predecessors.toString()),
                                padSection(inputs.toString())));

                        // Reinicia para a nova operação
                        predecessors = new StringBuilder();
                        inputs = new StringBuilder();
                    }

                    // Atualiza os dados da operação atual
                    lastOpId = opId;
                    currentItemId = itemId;
                    currentItemQtd = itemQtd;

                    // Adiciona predecessor se existir
                    if (predecessorId != null) {
                        predecessors.append(predecessorId).append(";1;");
                    }

                    // Adiciona entrada de material se existir
                    if (inputItemId != null) {
                        inputs.append(inputItemId).append(";").append(String.format("%.3f", inputQuantity)).append(";");
                    }
                }

                // Escreve a última operação, se necessário
                if (!lastOpId.isEmpty()) {
                    writer.write(String.format("%s;%s;%.3f;(;%s);(;%s)\n",
                            lastOpId,
                            currentItemId,
                            currentItemQtd,
                            padSection(predecessors.toString()),
                            padSection(inputs.toString())));
                }

                System.out.println("Arquivo boo.csv gerado com sucesso no formato corrigido.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Garante que uma seção contenha exatamente 7 delimitadores ";".
     * Se o número de entradas for menor, preenche com espaços extras.
     */
    private String padSection(String section) {
        int currentDelimiterCount = (int) section.chars().filter(ch -> ch == ';').count();
        int missingDelimiters = 7 - currentDelimiterCount;
        if (missingDelimiters > 0) {
            section += ";".repeat(missingDelimiters);
        }
        return section;
    }


    private void exportOperations(Connection connection, String productId) {
        String sql = """
                SELECT DISTINCT Operation_ID AS op_id, Designation 
                FROM Operation 
                WHERE Product_ID = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);

            try (ResultSet rs = stmt.executeQuery();
                 FileWriter writer = new FileWriter("textFiles/operationsLapr.csv")) {

                writer.write("op_id;op_name\n");
                while (rs.next()) {
                    String opId = rs.getString("op_id");
                    String opName = rs.getString("Designation");
                    writer.write(opId + ";" + opName + "\n");
                }
                System.out.println("Arquivo operations.csv gerado com sucesso.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public String registerOrder(String orderId, LocalDate orderDate, LocalDate deliveryDate, String vat, String productId) {
        String result = null;
        String query = "{? = call REGISTER_ORDER(?, ?, ?, ?, ?)}"; // PL/SQL Function Call

        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            // Bind parameters to the PL/SQL function
            callableStatement.registerOutParameter(1, Types.VARCHAR); // Result parameter
            callableStatement.setString(2, orderId);                 // p_order_id
            callableStatement.setDate(3, Date.valueOf(orderDate));    // p_order_date
            callableStatement.setDate(4, Date.valueOf(deliveryDate)); // p_delivery_date
            callableStatement.setString(5, vat);                     // p_customer_vat
            callableStatement.setString(6, productId);               // p_product_id

            // Execute the PL/SQL function
            callableStatement.execute();
            result = callableStatement.getString(1); // Retrieve the result
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


}
