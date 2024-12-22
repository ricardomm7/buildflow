package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.io.IOException;
import java.util.*;

public class ProductionOrchestrator {
    private final ProductionTree productionTree;
    private final Simulator simulator;
    private final Map<ProductionNode, Integer> nodeDependencyLevels;
    private final WorkstationsPerOperation workstationsPerOperation;

    public ProductionOrchestrator(ProductionTree productionTree, Simulator simulator) {
        this.productionTree = productionTree;
        this.simulator = simulator;
        this.nodeDependencyLevels = new HashMap<>();
        this.workstationsPerOperation = Repositories.getInstance().getWorkstationsPerOperation();
    }

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
            simulator.runSimulation(productsForSimulation, true);

        } catch (Exception e) {
            System.err.println("Error in production process: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void calculateDependencyLevels() {
        ProductionTreeSearcher searcher = new ProductionTreeSearcher();
        for (ProductionNode node : productionTree.getAllNodes()) {
            searcher.calculateDependencyLevel(node, nodeDependencyLevels);
        }
    }

    private List<ProductionNode> getSequentialNodes() {
        return productionTree.getAllNodes().stream()
                .sorted(Comparator.comparingInt(nodeDependencyLevels::get))
                .toList();
    }

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
                        Product product = new Product(subNode.getId(), List.of(operation));
                        products.add(product);
                    }
                }
            }
        }
        return products;
    }


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
}
