package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.util.*;

/**
 * The {@code ProductionTreeSearcher} class is responsible for searching and managing operations and materials within a production tree.
 * It provides methods to search for nodes by their name or ID, calculate quantities of materials, determine node dependency levels,
 * and simulate production execution in a specific order based on node dependencies.
 */
public class ProductionTreeSearcher {

    private final ProductionTree productionTree;
    private Map<ProductionNode, Map<ProductionNode, Double>> connections;
    private AVLTree<ProductionNode> operationAVL;
    private Simulator simulator = new Simulator();


    /**
     * Constructs a {@code ProductionTreeSearcher} instance and initializes the production tree and connections map.
     */
    public ProductionTreeSearcher() {
        this.productionTree = Repositories.getInstance().getProductionTree(); // O(1)
        this.connections = productionTree.getConnections(); // O(1)
    }

    /**
     * Handles user input to search for a production node by its name or ID.
     * It prompts the user for input and displays the result of the search.
     * <p>
     * Complexity: O(n)
     */
    public void handleNodeSearch() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID or name of the node to search: ");
        String identifier = scanner.nextLine();

        String result = searchNodeByNameOrId(identifier); // O(n)

        System.out.println(result);
    }

    /**
     * Searches for nodes by their name or ID. If multiple nodes match, the user is prompted to select one.
     *
     * @param identifier the name or ID of the node to search.
     * @return a string containing details of the matching node or an error message if no match is found.
     */
    public String searchNodeByNameOrId(String identifier) {
        List<ProductionNode> matchingNodes = productionTree.searchNodes(identifier);  // O(n)

        if (matchingNodes.isEmpty()) {
            return "No matching nodes found.";
        }

        if (matchingNodes.size() == 1) {
            return getNodeDetails(matchingNodes.get(0));
        }

        System.out.println("Multiple matches found:");
        for (int i = 0; i < matchingNodes.size(); i++) { // O(n)
            System.out.println((i + 1) + ". " + matchingNodes.get(i).getName() + " (ID: " + matchingNodes.get(i).getId() + ")");
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please select a node by entering the number (1-" + matchingNodes.size() + "): ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > matchingNodes.size()) {
            return "Invalid choice.";
        }

        return getNodeDetails(matchingNodes.get(choice - 1)); // O(n)
    }

    /**
     * Retrieves the details of a given production node, including its ID, name, type, and related materials or operations.
     * <p>
     * Complexity: O(n)
     *
     * @param node the production node whose details are to be retrieved.
     * @return a string containing the details of the node.
     */
    public String getNodeDetails(ProductionNode node) {
        StringBuilder result = new StringBuilder("Node Details:\n");
        result.append("ID: ").append(node.getId()).append("\n");
        result.append("Name: ").append(node.getName()).append("\n");

        if (node.isProduct()) {
            result.append("Type: Material\n");

            Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(node); // O(n)
            if (!subNodes.isEmpty()) {
                result.append("Material Quantities:\n");
                subNodes.forEach((materialNode, quantity) -> // O(n)
                        result.append("  ").append(materialNode.getName())
                                .append(" - Quantity: ").append(quantity).append("\n"));
            } else {
                result.append("No material details available.\n");
            }
        } else if (node.isOperation()) {
            result.append("Type: Operation\n");

            Object parentOperation = node.getParent();
            if (parentOperation instanceof Operation) {
                result.append("Parent Operation:\n");
                Operation parentOp = (Operation) parentOperation;
                result.append("  ID: ").append(parentOp.getId()).append("\n");
                result.append("  Name: ").append(parentOp.getName()).append("\n");
            } else {
                result.append("No parent operation available.\n");
            }

            Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(node); // O(n)
            if (!subNodes.isEmpty()) {
                result.append("Sub-nodes (Materials):\n");
                subNodes.forEach((materialNode, quantity) -> // O(n)
                        result.append("  Material: ").append(materialNode.getName())
                                .append(" - Quantity: ").append(quantity).append("\n"));
            } else {
                result.append("No sub-nodes available.\n");
            }
        } else {
            result.append("Type: Unknown\n");
        }

        return result.toString();
    }

    /**
     * Calculates the total quantities of materials based on the connections in the production tree.
     * Displays the material and its total quantity.
     * <p>
     * Complexity: O(n²)
     */
    public void calculateQuantityOfMaterials() {
        if (connections == null || connections.isEmpty()) {
            System.out.println("No connections available.");
            return;
        }

        System.out.println("Material --> Quantity");

        Map<String, Double> totalQuantities = new HashMap<>();

        for (Map.Entry<ProductionNode, Map<ProductionNode, Double>> entry : connections.entrySet()) { // O(n)
            ProductionNode node = entry.getKey();
            Map<ProductionNode, Double> connectedNodes = entry.getValue();

            if (node.isOperation()) {
                if (connectedNodes != null) {
                    for (Map.Entry<ProductionNode, Double> connectedEntry : connectedNodes.entrySet()) { // O(n)
                        ProductionNode connectedNode = connectedEntry.getKey();
                        Double quantity = connectedEntry.getValue();

                        if (connectedNode.isProduct()) {
                            totalQuantities.merge(connectedNode.getName(), quantity, Double::sum);
                        }
                    }
                }
            } else if (node.isProduct()) {
                totalQuantities.merge(node.getName(), node.getQuantity(), Double::sum);
            }
        }

        for (Map.Entry<String, Double> entry : totalQuantities.entrySet()) { // O(n)
            System.out.println(entry.getKey() + " --> " + entry.getValue());
        }
    }

    /**
     * Recursively calculates the dependency level of a given production node based on its parent nodes.
     * <p>
     * Complexity: O(n²)
     *
     * @param node                 the production node whose dependency level is to be calculated.
     * @param nodeDependencyLevels a map storing the dependency levels of nodes.
     */
    public void calculateDependencyLevel(ProductionNode node, Map<ProductionNode, Integer> nodeDependencyLevels) {
        if (nodeDependencyLevels.containsKey(node)) {
            return;
        }

        int maxDependencyLevel = 0;
        List<ProductionNode> parentNodes = productionTree.getParentNodes(node); // O(n)

        if (parentNodes != null) {
            for (ProductionNode parent : parentNodes) { // O(n)
                calculateDependencyLevel(parent, nodeDependencyLevels); // O(n)
                maxDependencyLevel = Math.max(maxDependencyLevel, nodeDependencyLevels.get(parent) + 1);
            }
        }

        nodeDependencyLevels.put(node, maxDependencyLevel);
    }


    /**
     * Simulates the execution of production operations based on the dependency levels calculated earlier.
     */
    public void simulateProductionExecution() {
        List<ProductionTree> productionTreeList = new ArrayList<>();
        if (productionTree == null) {
            System.err.println("Production tree is not initialized.");
            return;
        }
        productionTreeList.add(productionTree);
        for (ProductionTree tree : productionTreeList) {
            if (tree.getAllNodes().isEmpty()) {
                System.err.println("No nodes found in the production tree.");
                continue;
            }
            List<Product> products = new ArrayList<>(); // Reinicia a lista para cada árvore
            for (ProductionNode node : tree.getAllNodes()) {
                Map<ProductionNode, Double> dependencies = tree.getSubNodes(node);
                if (dependencies.isEmpty()) {
                    System.out.println("No dependencies for node: " + node.getId());
                }
                List<Operation> operations = new ArrayList<>();
                for (Map.Entry<ProductionNode, Double> dependency : dependencies.entrySet()) {
                    ProductionNode depNode = dependency.getKey();
                    String name = depNode.getName();
                    name = name.toUpperCase();
                    int firstSpaceIndex = name.indexOf(" ");
                    if (firstSpaceIndex != -1) {
                        name = name.substring(0, firstSpaceIndex);
                    }
                    Operation operation = new Operation(name);
                    operations.add(operation);
                }
                Product product = new Product(node.getId(), operations);
                products.add(product);
            }
            System.out.println("Starting simulation...");
            simulator.runSimulation(products, false);
        }
    }
}