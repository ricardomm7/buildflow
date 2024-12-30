package fourcorp.buildflow.application;

import fourcorp.buildflow.repository.Repositories;
import oracle.jdbc.internal.OracleTypes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * Controller for managing database-related tasks and procedures.
 * This class handles the initialization of the database, execution of SQL scripts,
 * and calling stored procedures to fetch or process data.
 */
public class DatabaseFunctionsController {
    private Connection connection;
    private final String DATA_INSERTS_AND_CREATION = "textFiles/databaseSQL/tables-insert-and-drop.sql";
    private final String FUNCTIONS_AND_PROCEDURES = "textFiles/databaseSQL/functions-and-procedures.sql";

    /**
     * Constructs a new {@code DatabaseFunctionsController}, connects to the database,
     * and executes all tasks to set up the database.
     */
    public DatabaseFunctionsController() {
        System.out.println();
        connection = Repositories.getInstance().getDatabase().getConnection();
        executeAllTasks();
    }

    /**
     * Executes all database setup tasks, including running SQL scripts for creating tables,
     * inserting data, and defining stored procedures.
     */
    private void executeAllTasks() {
        int totalSteps = calculateTotalSteps();
        int currentStep = 0;

        System.out.print("Initializing database setup:\n[-----------------------------------] 0%");
        currentStep = executeTask(DATA_INSERTS_AND_CREATION, ";", currentStep, totalSteps);
        currentStep = executeTask(FUNCTIONS_AND_PROCEDURES, "/", currentStep, totalSteps);

        displayProgressBar(totalSteps, totalSteps);
        System.out.println("\nDatabase setup completed successfully.");
    }

    /**
     * Calculates the total number of SQL commands in all scripts to estimate progress.
     *
     * @return the total number of SQL commands.
     */
    private int calculateTotalSteps() {
        return countSteps(DATA_INSERTS_AND_CREATION, ";") + countSteps(FUNCTIONS_AND_PROCEDURES, "/");
    }

    /**
     * Counts the number of SQL commands in a given file based on a delimiter.
     *
     * @param filePath  the path to the SQL file.
     * @param delimiter the delimiter used to separate SQL commands.
     * @return the number of SQL commands in the file.
     */
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

    /**
     * Executes all SQL commands in a given file.
     *
     * @param filePath    the path to the SQL file.
     * @param delimiter   the delimiter used to separate SQL commands.
     * @param currentStep the current progress step.
     * @param totalSteps  the total number of steps.
     * @return the updated progress step.
     */
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

    /**
     * Displays a progress bar in the console.
     *
     * @param current the current progress step.
     * @param total   the total number of steps.
     */
    private void displayProgressBar(int current, int total) {
        int width = 35;
        int completed = (current * width) / total;
        int remaining = width - completed;

        String bar = "[" + "#".repeat(completed) + "-".repeat(remaining) + "] " +
                String.format("%d%%", (current * 100) / total);

        System.out.print("\r" + bar);
    }

    /**
     * Calls the stored function {@code GetProductOperationParts} to fetch parts and their quantities for a given product.
     *
     * @param productId the ID of the product.
     */
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

    /**
     * Calls the stored function {@code GetProductOperationsAndWorkstations} to fetch operations
     * and workstation details for a given product.
     *
     * @param productId the ID of the product.
     */
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

    /**
     * Calls the stored procedure {@code PrintProductsUsingAllWorkstationTypes} to print details
     * of products using all workstation types.
     */
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

    /**
     * Prints a formatted table to the console.
     *
     * @param rows the data to be printed, where each sub-array represents a row.
     */
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

    /**
     * Calculates the maximum width for each column in the table.
     *
     * @param rows the data to be printed, where each sub-array represents a row.
     * @return an array of integers representing the width of each column.
     */
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

    /**
     * Deactivates a customer by calling a PL/SQL procedure that deactivates the customer based on their NIF (Tax Identification Number).
     *
     * @param nif the NIF of the customer to be deactivated.
     */
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

    /**
     * Initiates a process of graphical visualization by fetching product details
     * and exporting related data to CSV files based on the given product ID.
     *
     * @param pid the product ID or name to fetch the product details.
     */
    public void graphicVisualization(String pid) {
        while (true) {
            String productId = fetchProductId(pid);
            if (productId == null) {
                System.err.println("Produto não encontrado! Tente novamente.");
                return;
            }

            System.out.println("Produto encontrado: " + productId);

            exportItems(connection, productId);
            exportBOO(connection, productId);
            exportOperations(connection, productId);
            return;
        }

    }

    /**
     * Fetches the product ID from the database based on the user input.
     *
     * @param userInput the user input, which could be either the product ID or name.
     * @return the product ID if found, otherwise null.
     */
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

    /**
     * Exports the items related to a specific product ID to a CSV file.
     *
     * @param connection the database connection.
     * @param productId  the ID of the product whose items will be exported.
     */
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

    /**
     * Exports the Bill of Operations (BOO) for a given product ID to a CSV file.
     * The exported file contains detailed information about operations and materials used.
     *
     * @param connection the database connection.
     * @param productId  the ID of the product for which the BOO will be exported.
     */
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
     * Pads a section of data to ensure it contains exactly 7 semicolons as delimiters.
     * If the section has fewer entries, it is padded with extra semicolons.
     *
     * @param section the section of data to be padded.
     * @return the padded section with exactly 7 delimiters.
     */
    private String padSection(String section) {
        int currentDelimiterCount = (int) section.chars().filter(ch -> ch == ';').count();
        int missingDelimiters = 7 - currentDelimiterCount;
        if (missingDelimiters > 0) {
            section += ";".repeat(missingDelimiters);
        }
        return section;
    }

    /**
     * Exports the operations associated with a given product to a CSV file.
     *
     * @param connection The database connection to use for querying.
     * @param productId  The ID of the product whose operations are to be exported.
     */
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

    /**
     * Registers a new product in the system by calling a stored procedure in the database.
     *
     * @param partId   The part ID of the product.
     * @param name     The name of the product.
     * @param familyId The family ID of the product.
     * @return A string indicating the result of the operation, either success or error message.
     */
    public String registerNewProduct(String partId, String name, String familyId) {
        String result = null;
        String query = "{? = call RegisterProduct(?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            // Definir o tipo de retorno
            callableStatement.registerOutParameter(1, Types.VARCHAR);

            // Definir os parâmetros de entrada
            callableStatement.setString(2, partId);
            callableStatement.setString(3, name);
            callableStatement.setString(4, familyId);

            // Executar a função
            callableStatement.execute();

            // Obter o resultado
            result = callableStatement.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Error: An unexpected database error occurred.";
        }

        return result;
    }

    /**
     * Registers a new order by calling a PL/SQL stored procedure.
     *
     * @param orderDate    The order date.
     * @param deliveryDate The expected delivery date.
     * @param vat          The VAT number of the customer.
     * @param productId    The ID of the product being ordered.
     * @return A string indicating the result of the order registration, either success or error message.
     */
    public String registerOrder(java.time.LocalDate orderDate, java.time.LocalDate deliveryDate, String vat, String productId) {
        String plsqlBlock = """
                DECLARE
                    v_result VARCHAR2(255);
                BEGIN
                    v_result := REGISTER_ORDER(
                        p_order_date    => TO_DATE(?, 'YYYY-MM-DD'),
                        p_delivery_date => TO_DATE(?, 'YYYY-MM-DD'),
                        p_customer_vat  => ?,
                        p_product_id    => ?
                    );
                    ? := v_result;
                END;
                """;

        try (CallableStatement callableStatement = connection.prepareCall(plsqlBlock)) {
            // Set input parameters
            callableStatement.setString(1, orderDate.toString()); // Order date
            callableStatement.setString(2, deliveryDate.toString()); // Delivery date
            callableStatement.setString(3, vat); // VAT
            callableStatement.setString(4, productId); // Product ID

            // Register output parameter
            callableStatement.registerOutParameter(5, Types.VARCHAR);

            // Execute the PL/SQL block
            callableStatement.execute();

            // Get the result
            return callableStatement.getString(5);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Calls the stored procedure to find the product with the most operations associated with it.
     */
    public void callProductWithMostOperations() {
        String query = "{? = call ProductWithMostOperations()}";

        try (CallableStatement callableStatement = connection.prepareCall(query)) {

            callableStatement.registerOutParameter(1, Types.VARCHAR);


            callableStatement.execute();


            String result = callableStatement.getString(1);


            if (result != null) {
                System.out.println("Product with the longest sequence: " + result);
            } else {
                System.out.println("No product found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a new workstation in the system by calling a stored procedure in the database.
     *
     * @param workstationId   The ID of the workstation.
     * @param name            The name of the workstation.
     * @param description     A description of the workstation.
     * @param workstationType The type of the workstation.
     * @return A string indicating the result of the operation, either success or error message.
     */
    public String registerWorkstation(String workstationId, String name, String description, String workstationType) {
        String result = null;
        String query = "{? = call RegisterWorkstation(?, ?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.registerOutParameter(1, Types.VARCHAR);

            callableStatement.setString(2, workstationId);
            callableStatement.setString(3, name);
            callableStatement.setString(4, description);
            callableStatement.setString(5, workstationType);

            callableStatement.execute();

            result = callableStatement.getString(1);
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Error: An unexpected database error occurred.";
        }

        return result;
    }

    /**
     * Displays the available workstation types by querying the database and printing the results.
     */
    public void showWorkstationTypes() {
        String query = "SELECT WorkstationType_ID, Designation FROM Type_Workstation";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            List<String[]> rows = new ArrayList<>();
            rows.add(new String[]{"Workstation Type ID", "Designation"}); // Cabeçalhos

            while (rs.next()) {
                String id = rs.getString("WorkstationType_ID");
                String description = rs.getString("Designation");
                rows.add(new String[]{id, description});
            }

            printTable(rows);
        } catch (SQLException e) {
            System.out.println("Error fetching workstation types: " + e.getMessage());
        }
    }

    /**
     * Consume material.
     *
     * @param partID   the part id
     * @param quantity the quantity
     */
    public void consumeMaterial(String partID, double quantity) {
        String callStatement = "{call consume_material(?, ?, ?, ?)}";

        try (CallableStatement stmt = connection.prepareCall(callStatement)) {
            stmt.setString(1, partID);
            stmt.setDouble(2, quantity);

            stmt.registerOutParameter(3, OracleTypes.BOOLEAN);
            stmt.registerOutParameter(4, OracleTypes.VARCHAR); // For message

            stmt.execute();

            int successFlag = stmt.getInt(3); // Assumes BOOLEAN is represented as INTEGER
            String message = stmt.getString(4);

            if (successFlag == 1) {
                System.out.println("Success: " + message);
            } else {
                System.out.println("Failed: " + message);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public String displayOrders() {
        try {
            String query = "SELECT Order_ID, OrderDate, DeliveryDate, CostumerVAT FROM \"Order\"";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            HashSet<String> orderIds = new HashSet<>();

            String lineFormat = "| %-15s | %-12s | %-13s | %-15s |%n";
            String separator = "+-----------------+--------------+---------------+-----------------+";

            System.out.println("Available Orders:");
            System.out.println(separator);
            System.out.printf(lineFormat, "Order ID", "Order Date", "Delivery Date", "Customer VAT");
            System.out.println(separator);

            while (rs.next()) {
                String orderId = rs.getString("Order_ID");
                String orderDate = rs.getDate("OrderDate").toString();
                String deliveryDate = rs.getDate("DeliveryDate").toString();
                String customerVat = rs.getString("CostumerVAT");

                orderIds.add(orderId);
                System.out.printf(lineFormat, orderId, orderDate, deliveryDate, customerVat);
            }
            System.out.println(separator);

            if (orderIds.isEmpty()) {
                System.out.println("No orders available.");
                return null;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("\nEnter the Order ID: ");
            String inputOrderId = scanner.nextLine().trim();

            if (orderIds.contains(inputOrderId)) {
                return inputOrderId;
            } else {
                System.out.println("Invalid Order ID.");
                return null;
            }

        } catch (SQLException e) {
            System.err.println("Error querying orders: " + e.getMessage());
            return null;
        }
    }

    public void reserveOrderComponents(String orderId) {
        String callStatement = "{call Reserve_Order_Components(?)}";

        try (CallableStatement stmt = connection.prepareCall(callStatement)) {
            // Configura o parâmetro de entrada
            stmt.setString(1, orderId);

            // Executa o procedimento
            stmt.execute();

            System.out.println("Reservation successfully processed for order: " + orderId);
        } catch (SQLException e) {
            System.err.println("Error during reservation: " + e.getMessage());
        }
    }

    public void callGetProductOperations(String productId) {
        try {
            // Prepare the call to the stored function
            CallableStatement stmt = connection.prepareCall("{ ? = call GetProductOperations(?) }");
            stmt.registerOutParameter(1, OracleTypes.CURSOR); // Register output parameter (SYS_REFCURSOR)
            stmt.setString(2, productId); // Set the product ID parameter

            // Execute the function
            stmt.execute();

            // Retrieve the result
            ResultSet resultSet = (ResultSet) stmt.getObject(1);

            // Process the result set and print the operations
            while (resultSet.next()) {
                String operationId = resultSet.getString("operation_id");
                String productIdDb = resultSet.getString("product_id");
                String operationType = resultSet.getString("operation_type");
                String expectedTime = resultSet.getString("expected_time");
                String nextOperationId = resultSet.getString("next_operation_id");
                String outputPartId = resultSet.getString("output_part_id");

                // Print the operation details
                System.out.println("---------------------------------------------------------------------------------");
                System.out.println("Operation ID: " + operationId);
                System.out.println("Product ID: " + productIdDb);
                System.out.println("Operation Type: " + operationType);
                System.out.println("Expected Time: " + expectedTime);
                System.out.println("Next Operation ID: " + nextOperationId);
                System.out.println("Output Part ID: " + outputPartId);
                System.out.println("Inputs:");

                // Retrieve and print the input parts (nested cursor)
                ResultSet inputs = (ResultSet) resultSet.getObject("inputs");
                while (inputs.next()) {
                    String inputPartId = inputs.getString("Part_ID");
                    String inputDescription = inputs.getString("Description");
                    String inputQuantity = inputs.getString("Quantity");
                    System.out.println("  - " + inputPartId + " (" + inputDescription + "): " + inputQuantity);
                }
            }

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
