package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.util.*;

/**
 * The {@code ProductionOrchestrator} class manages the orchestration of the production process
 * based on a given production tree. It calculates dependencies, determines the execution order
 * of operations, and simulates the production workflow.
 */
public class ProductionOrchestrator {
    private final ProductionTree productionTree;
    private final Simulator simulator;
    private final Map<ProductionNode, Integer> nodeDependencyLevels;
    private final WorkstationsPerOperation workstationsPerOperation;

    /**
     * Constructs a new {@code ProductionOrchestrator} with the specified production tree and simulator.
     *
     * @param productionTree the production tree containing the operations and dependencies
     * @param simulator      the simulator to execute the production simulation
     */
    public ProductionOrchestrator(ProductionTree productionTree, Simulator simulator) {
        this.productionTree = productionTree;
        this.simulator = simulator;
        this.nodeDependencyLevels = new HashMap<>();
        this.workstationsPerOperation = Repositories.getInstance().getWorkstationsPerOperation();
    }

    /**
     * Processes the production tree by analyzing dependencies, organizing the workflow,
     * and executing the production simulation.
     */
    public void processProductionTree() {
        try {
            System.out.println("\n========== Production Flow Analysis ==========");
            System.out.println("Initializing production system and analyzing workflow...\n");

            calculateDependencyLevels();

            List<ProductionNode> orderedNodes = getSequentialNodes();

            if (orderedNodes.isEmpty()) {
                System.out.println("Error: No operations available for production.");
                return;
            }

            List<Product> productsForSimulation = convertToProducts(orderedNodes);

            printProductionFlow(productsForSimulation);

            System.out.println("\n========== Starting Sequential Production Simulation ==========");
            simulator.runSimplesimulation(productsForSimulation, true);

        } catch (Exception e) {
            System.err.println("Error in production process: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Calculates the dependency levels of all nodes in the production tree.
     * Higher dependency levels indicate that a node depends on more operations being completed beforehand.
     */
    private void calculateDependencyLevels() {
        ProductionTreeSearcher searcher = new ProductionTreeSearcher();
        for (ProductionNode node : productionTree.getAllNodes()) {
            searcher.calculateDependencyLevel(node, nodeDependencyLevels);
        }
    }

    /**
     * Retrieves all nodes in the production tree, ordered by their dependency levels.
     *
     * @return a list of production nodes ordered by dependency level
     */
    private List<ProductionNode> getSequentialNodes() {
        return productionTree.getAllNodes().stream()
                .sorted(Comparator.comparingInt(nodeDependencyLevels::get))
                .toList();
    }

    /**
     * Converts the ordered nodes from the production tree into a list of {@code Product} objects
     * for simulation.
     *
     * @param orderedNodes the ordered list of production nodes
     * @return a list of {@code Product} objects representing the production workflow
     */
    private List<Product> convertToProducts(List<ProductionNode> orderedNodes) {
        List<Product> products = new ArrayList<>();

        for (ProductionNode node : orderedNodes) {
            if (node.isProduct()) {
                Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(node);

                // Para cada subnó, cria um produto com uma operação associada
                for (Map.Entry<ProductionNode, Double> entry : subNodes.entrySet()) {
                    ProductionNode subNode = entry.getKey();
                    if (!subNode.isProduct()) {
                        String operationName = subNode.getName();
                        Operation operation = new Operation(operationName);

                        // Cria um produto apenas para esta operação
                        Product product = new Product(node.getId(), List.of(operation));
                        products.add(product);
                    }
                }
            }
        }
        return products;
    }

    /**
     * Prints the detailed production flow analysis, including required operations and
     * available workstations for each operation.
     *
     * @param products the list of products to analyze and display
     */
    private void printProductionFlow(List<Product> products) {
        System.out.println("Detailed Production Flow Analysis:");
        System.out.println("----------------------------------------");

        for (Product product : products) {
            System.out.println("\nProduct: " + product.getIdItem());

            // Cada produto agora possui apenas uma operação
            Operation operation = product.getCurrentOperation();
            if (operation != null) {
                System.out.println("→ Operation Required: " + operation.getId());

                List<Workstation> availableWorkstations = workstationsPerOperation.getWorkstationsByOperation(operation, true);

                System.out.println("→ Available Workstations:");
                if (availableWorkstations.isEmpty()) {
                    System.out.println("  • No workstations available for this operation!");
                } else {
                    for (Workstation ws : availableWorkstations) {
                        System.out.printf("  • Workstation %s (Processing time: %.2f seconds)%n", ws.getId(), ws.getTime());
                    }
                }
            } else {
                System.out.println("→ No operation associated with this product.");
            }
            System.out.println("\n----------------------------------------");
        }
    }

/*
    public static void main(String[] args) throws IOException {
        System.out.println("Loading production system data...");
        Reader.loadOperations("textFiles/articles.csv");
        Reader.loadMachines("textFiles/workstations.csv");
        Reader.loadItems("textFiles/items.csv");
        Reader.loadSimpleOperations("textFiles/operations.csv");
        Reader.loadBOO("textFiles/boo_v2.csv");
        Reader.loadActivities("textFiles/small_project.csv");

        ProductionTree productionTree = Repositories.getInstance().getProductionTree();
        Simulator simulator = new Simulator();
        ProductionOrchestrator orchestrator = new ProductionOrchestrator(productionTree, simulator);
        orchestrator.processProductionTree();
    }
 */
}
