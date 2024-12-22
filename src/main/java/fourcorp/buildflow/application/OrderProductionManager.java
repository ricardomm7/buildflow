package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages the production of orders by interacting with a production tree,
 * sequence exporter, and a simulation system. The class provides functionalities to:
 * <ul>
 *     <li>Read and process orders from a CSV file.</li>
 *     <li>Build a production tree for each order, including necessary operations and components.</li>
 *     <li>Simulate production processes using a {@link Simulator}.</li>
 *     <li>Export operation sequences for further analysis or processing.</li>
 * </ul>
 */
public class OrderProductionManager {

    /**
     * Represents the production tree structure for the current order.
     */
    private final ProductionTree productionTree;

    /**
     * Handles the export of operation sequences extracted from production trees.
     */
    private final OperationSequenceExporter sequenceExporter;

    /**
     * Simulates the production process for a given production tree.
     */
    private final Simulator simulator;

    /**
     * Database connection for retrieving product and operation data.
     */
    private Connection connection;

    /**
     * Maps order IDs to their assigned priorities.
     */
    private final Map<String, Integer> orderPriorities;

    /**
     * Constructs a new {@code OrderProductionManager} with a new production tree, simulator,
     * and operation sequence exporter. The database connection is initialized via a repository.
     */
    public OrderProductionManager() {
        connection = Repositories.getInstance().getDatabase().getConnection();
        this.productionTree = new ProductionTree(); // Create new tree for each instance
        this.simulator = new Simulator();
        this.orderPriorities = new HashMap<>();
        this.sequenceExporter = new OperationSequenceExporter();
        //initializeProductionSystem();
    }

    /*
    private void initializeProductionSystem() {
        try {
            Reader.loadOperations("textFiles/articles.csv");
            Reader.loadMachines("textFiles/workstations.csv");
            Reader.loadItems("textFiles/items.csv");
            Reader.loadSimpleOperations("textFiles/operations.csv");
            Reader.loadBOO("textFiles/boo_v2.csv");
            Reader.loadActivities("textFiles/small_project.csv");
        } catch (Exception e) {
            System.err.println("Error initializing production system: " + e.getMessage());
        }
    }
     */

    /**
     * Processes orders specified in a CSV file. For each order:
     * <ul>
     *     <li>Reads the order details (ID, product, priority, and quantity).</li>
     *     <li>Builds a production tree for the product.</li>
     *     <li>Simulates the production process.</li>
     *     <li>Extracts the operation sequence and adds it to the exporter.</li>
     *     <li>Clears the production tree for the next order.</li>
     * </ul>
     * Finally, exports all operation sequences to a file.
     *
     * @param filePath the path to the CSV file containing order data.
     */
    public void processOrdersFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            System.out.println("\n=== Processing Orders from CSV ===");

            // Skip header
            if (firstLine) {
                br.readLine();
                firstLine = false;
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String orderId = parts[0];
                    String productId = parts[1];
                    int priority = Integer.parseInt(parts[2]);
                    int quantity = Integer.parseInt(parts[3]);

                    System.out.println("\n=== Processing Order: " + orderId + " ===");
                    processOrder(orderId, productId, priority, quantity);

                    // Adiciona a sequência desta order ao exportador
                    sequenceExporter.addOrderSequence(productionTree);

                    // Limpa a árvore para a próxima order
                    productionTree.clear();
                }

                // Exporta todas as sequências no final
                sequenceExporter.exportAllSequences("textFiles/instructions.txt");
            }
        } catch (Exception e) {
            System.err.println("Error processing orders file: " + e.getMessage());
        }
    }

    /**
     * Processes a single order by building its production tree, simulating production,
     * and logging order details.
     *
     * @param orderId   the unique identifier for the order.
     * @param productId the product to be produced for this order.
     * @param priority  the priority of the order.
     * @param quantity  the quantity of the product required.
     */
    private void processOrder(String orderId, String productId, int priority, int quantity) {
        try {
            System.out.println("\nOrder Details:");
            System.out.println("+-----------------+----------+-----------+");
            System.out.printf("| %-15s | %-8s | %-9s |%n", "Product ID", "Quantity", "Priority");
            System.out.println("+-----------------+----------+-----------+");
            System.out.printf("| %-15s | %-8d | %-9d |%n", productId, quantity, priority);
            System.out.println("+-----------------+----------+-----------+");

            // Clear previous production tree for new order
            productionTree.clear();

            ProductionNode rootNode = buildProductionTree(productId, quantity);
            if (rootNode != null) {
                // Create orchestrator and process the tree
                ProductionOrchestrator orchestrator = new ProductionOrchestrator(productionTree, simulator);
                System.out.println("\nStarting production simulation for order " + orderId);
                orchestrator.processProductionTree();
            } else {
                System.out.println("Could not build production tree for product: " + productId);
            }

        } catch (SQLException e) {
            System.err.println("Error processing order " + orderId + ": " + e.getMessage());
        }
    }

    /**
     * Builds the production tree for a specified product and quantity.
     * The tree includes all necessary operations and components required for production.
     *
     * @param productId the ID of the product.
     * @param quantity  the required quantity of the product.
     * @return the root node of the production tree, or {@code null} if the product does not exist.
     * @throws SQLException if an error occurs while querying the database.
     */
    private ProductionNode buildProductionTree(String productId, int quantity) throws SQLException {
        // Primeiro verifica se o produto existe
        if (!checkProductExists(productId)) {
            System.out.println("Product not found: " + productId);
            return null;
        }

        ProductionNode rootNode = new ProductionNode(productId, "Product " + productId, true);
        rootNode.setQuantity(quantity);
        productionTree.addNode(rootNode);

        System.out.println("\nBuilding production tree for: " + productId);
        buildProductionSubtree(rootNode, new HashSet<>());

        return rootNode;
    }

    /**
     * Checks if a product exists in the database.
     *
     * @param productId the ID of the product to check.
     * @return {@code true} if the product exists, {@code false} otherwise.
     * @throws SQLException if an error occurs while querying the database.
     */
    private boolean checkProductExists(String productId) throws SQLException {
        String checkQuery = "SELECT Part_ID FROM Product WHERE Part_ID = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, productId);
            ResultSet checkRs = checkStmt.executeQuery();
            return checkRs.next();
        }
    }

    /**
     * Builds the production subtree for a specified node by recursively adding all required operations
     * and components.
     *
     * @param node              the node for which the subtree is being built.
     * @param processedProducts a set of already processed products to avoid cycles.
     * @throws SQLException if an error occurs while querying the database.
     */
    private void buildProductionSubtree(ProductionNode node, Set<String> processedProducts) throws SQLException {
        String productId = node.getId();

        // Evita ciclos infinitos
        if (processedProducts.contains(productId)) {
            System.out.println("Circular dependency detected for product: " + productId);
            return;
        }
        processedProducts.add(productId);

        // Busca todas as operações necessárias para produzir este produto
        String operationsQuery = """
                SELECT DISTINCT 
                    o.Operation_ID,
                    o.Operation_TypeID,
                    o.NextOperation_ID,
                    ot.Description,
                    ot.Expec_Time as execution_time
                FROM Operation o
                JOIN Operation_Type ot ON o.Operation_TypeID = ot.ID
                WHERE o.Product_ID = ?
                ORDER BY o.Operation_ID
                """;

        Map<String, ProductionNode> operationNodes = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(operationsQuery)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String operationId = rs.getString("Operation_ID");
                String description = rs.getString("Description");
                double executionTime = rs.getDouble("execution_time");

                System.out.println("\nOperation ID: " + operationId);
                System.out.println("  • Description: " + description);
                System.out.printf("  • Execution Time: %.2f seconds%n", executionTime);

                ProductionNode operationNode = new ProductionNode(operationId, description, false);
                productionTree.addNode(operationNode);
                productionTree.addEdge(node, operationNode, executionTime);
                operationNodes.put(operationId, operationNode);

                // Busca os componentes necessários para esta operação
                String inputsQuery = """
                        SELECT 
                            oi.Part_ID,
                            oi.Quantity,
                            pt.Description as part_description
                        FROM Operation_Input oi
                        JOIN Product_Type pt ON oi.Part_ID = pt.Part_ID
                        WHERE oi.Operation_ID = ?
                        """;

                try (PreparedStatement inputStmt = connection.prepareStatement(inputsQuery)) {
                    inputStmt.setString(1, operationId);
                    ResultSet inputRs = inputStmt.executeQuery();

                    System.out.println("  • Required Parts:");
                    while (inputRs.next()) {
                        String inputPartId = inputRs.getString("Part_ID");
                        double inputQuantity = inputRs.getDouble("Quantity");
                        String partDescription = inputRs.getString("part_description");

                        System.out.printf("      - Part ID: %s, Name: %s, Quantity: %.2f%n",
                                inputPartId, partDescription, inputQuantity);

                        // Verifica se este componente é um produto intermediário
                        String intermediateCheck = """
                                SELECT p.Part_ID 
                                FROM Product p 
                                JOIN Intermediate_Product ip ON p.Part_ID = ip.Part_ID 
                                WHERE p.Part_ID = ?
                                """;

                        try (PreparedStatement checkStmt = connection.prepareStatement(intermediateCheck)) {
                            checkStmt.setString(1, inputPartId);
                            ResultSet checkRs = checkStmt.executeQuery();

                            if (checkRs.next()) {
                                // É um produto intermediário, precisa ser produzido
                                ProductionNode inputNode = new ProductionNode(
                                        inputPartId,
                                        partDescription,
                                        true
                                );
                                inputNode.setQuantity((int) (inputQuantity * node.getQuantity()));
                                productionTree.addNode(inputNode);
                                productionTree.addEdge(operationNode, inputNode, 0.0);

                                // Recursivamente constrói a árvore para este componente
                                buildProductionSubtree(inputNode, processedProducts);
                            } else {
                                // É um componente externo ou matéria-prima
                                ProductionNode inputNode = new ProductionNode(
                                        inputPartId,
                                        partDescription,
                                        false
                                );
                                productionTree.addNode(inputNode);
                                productionTree.addEdge(operationNode, inputNode, 0.0);
                            }
                        }
                    }
                }
            }
        }
    }
}
