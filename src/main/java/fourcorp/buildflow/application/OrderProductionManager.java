package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OrderProductionManager {
    private final ProductionTree productionTree;
    private final OperationSequenceExporter sequenceExporter;
    private final Simulator simulator;
    private final String URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private final String USERNAME = "fourcorp";
    private final String PASSWORD = "1234";
    private Connection connection;
    private final Map<String, Integer> orderPriorities;

    public OrderProductionManager() {
        connect();
        this.productionTree = new ProductionTree(); // Create new tree for each instance
        this.simulator = new Simulator();
        this.orderPriorities = new HashMap<>();
        this.sequenceExporter = new OperationSequenceExporter();
        initializeProductionSystem();
    }

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

    private void connect() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to database successfully.");
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

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

    private boolean checkProductExists(String productId) throws SQLException {
        String checkQuery = "SELECT Part_ID FROM Product WHERE Part_ID = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, productId);
            ResultSet checkRs = checkStmt.executeQuery();
            return checkRs.next();
        }
    }

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



    public static void main(String[] args) {
        WorkstationCompleter workstationCompleter = new WorkstationCompleter();
        workstationCompleter.ensureCompleteWorkstationsFile();
        OrderProductionManager manager = new OrderProductionManager();
        manager.processOrdersFromFile("textFiles/orders.csv");
    }
}
