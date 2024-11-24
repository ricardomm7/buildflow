package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
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
     */
    public void handleNodeSearch() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID or name of the node to search: "); // O(1)
        String identifier = scanner.nextLine(); // O(1)

        String result = searchNodeByNameOrId(identifier); // O(n)

        System.out.println(result); // O(1)
    }

    /**
     * Searches for nodes by their name or ID. If multiple nodes match, the user is prompted to select one.
     *
     * @param identifier the name or ID of the node to search.
     * @return a string containing details of the matching node or an error message if no match is found.
     */
    public String searchNodeByNameOrId(String identifier) {
        List<ProductionNode> matchingNodes = productionTree.searchNodes(identifier);  // O(n)

        if (matchingNodes.isEmpty()) { // O(1)
            return "No matching nodes found."; // O(1)
        }

        if (matchingNodes.size() == 1) { // O(1)
            return getNodeDetails(matchingNodes.get(0)); // O(1)
        }

        System.out.println("Multiple matches found:"); // O(1)
        for (int i = 0; i < matchingNodes.size(); i++) { // O(n)
            System.out.println((i + 1) + ". " + matchingNodes.get(i).getName() + " (ID: " + matchingNodes.get(i).getId() + ")"); // O(1)
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please select a node by entering the number (1-" + matchingNodes.size() + "): "); // O(1)
        int choice = scanner.nextInt(); // O(1)

        if (choice < 1 || choice > matchingNodes.size()) { // O(1)
            return "Invalid choice."; // O(1)
        }

        return getNodeDetails(matchingNodes.get(choice - 1)); // O(1)
    }

    /**
     * Retrieves the details of a given production node, including its ID, name, type, and related materials or operations.
     *
     * @param node the production node whose details are to be retrieved.
     * @return a string containing the details of the node.
     */
    public String getNodeDetails(ProductionNode node) {
        StringBuilder result = new StringBuilder("Node Details:\n"); // O(1)
        result.append("ID: ").append(node.getId()).append("\n"); // O(1)
        result.append("Name: ").append(node.getName()).append("\n"); // O(1)

        if (node.isProduct()) { // O(1)
            result.append("Type: Material\n"); // O(1)

            Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(node); // O(n)
            if (!subNodes.isEmpty()) { // O(1)
                result.append("Material Quantities:\n"); // O(1)
                subNodes.forEach((materialNode, quantity) -> // O(n)
                        result.append("  ").append(materialNode.getName())
                                .append(" - Quantity: ").append(quantity).append("\n")); // O(1)
            } else { // O(1)
                result.append("No material details available.\n"); // O(1)
            }
        } else if (node.isOperation()) { // O(1)
            result.append("Type: Operation\n"); // O(1)

            Object parentOperation = node.getParent(); // O(1)
            if (parentOperation instanceof Operation) { // O(1)
                result.append("Parent Operation:\n"); // O(1)
                Operation parentOp = (Operation) parentOperation; // O(1)
                result.append("  ID: ").append(parentOp.getId()).append("\n"); // O(1)
                result.append("  Name: ").append(parentOp.getName()).append("\n"); // O(1)
            } else { // O(1)
                result.append("No parent operation available.\n"); // O(1)
            }

            Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(node); // O(n)
            if (!subNodes.isEmpty()) { // O(1)
                result.append("Sub-nodes (Materials):\n"); // O(1)
                subNodes.forEach((materialNode, quantity) -> // O(n)
                        result.append("  Material: ").append(materialNode.getName())
                                .append(" - Quantity: ").append(quantity).append("\n")); // O(1)
            } else { // O(1)
                result.append("No sub-nodes available.\n"); // O(1)
            }
        } else { // O(1)
            result.append("Type: Unknown\n"); // O(1)
        }

        return result.toString(); // O(1)
    }

    /**
     * Calculates the total quantities of materials based on the connections in the production tree.
     * Displays the material and its total quantity.
     */
    public void calculateQuantityOfMaterials() {
        if (connections == null || connections.isEmpty()) { // O(1)
            System.out.println("No connections available."); // O(1)
            return; // O(1)
        }

        System.out.println("Material --> Quantity"); // O(1)

        Map<String, Double> totalQuantities = new HashMap<>(); // O(1)

        for (Map.Entry<ProductionNode, Map<ProductionNode, Double>> entry : connections.entrySet()) { // O(n)
            ProductionNode node = entry.getKey(); // O(1)
            Map<ProductionNode, Double> connectedNodes = entry.getValue(); // O(1)

            if (node.isOperation()) { // O(1)
                if (connectedNodes != null) { // O(1)
                    for (Map.Entry<ProductionNode, Double> connectedEntry : connectedNodes.entrySet()) { // O(n)
                        ProductionNode connectedNode = connectedEntry.getKey(); // O(1)
                        Double quantity = connectedEntry.getValue(); // O(1)

                        if (connectedNode.isProduct()) { // O(1)
                            totalQuantities.merge(connectedNode.getName(), quantity, Double::sum); // O(1)
                        }
                    }
                }
            } else if (node.isProduct()) { // O(1)
                totalQuantities.merge(node.getName(), node.getQuantity(), Double::sum); // O(1)
            }
        }

        for (Map.Entry<String, Double> entry : totalQuantities.entrySet()) { // O(n)
            System.out.println(entry.getKey() + " --> " + entry.getValue()); // O(1)
        }
    }

    /**
     * Recursively calculates the dependency level of a given production node based on its parent nodes.
     *
     * @param node                 the production node whose dependency level is to be calculated.
     * @param nodeDependencyLevels a map storing the dependency levels of nodes.
     */
    public void calculateDependencyLevel(ProductionNode node, Map<ProductionNode, Integer> nodeDependencyLevels) {
        if (nodeDependencyLevels.containsKey(node)) { // O(1)
            return; // O(1)
        }

        int maxDependencyLevel = 0; // O(1)
        List<ProductionNode> parentNodes = productionTree.getParentNodes(node); // O(n)

        if (parentNodes != null) { // O(1)
            for (ProductionNode parent : parentNodes) { // O(n)
                calculateDependencyLevel(parent, nodeDependencyLevels); // O(n)
                maxDependencyLevel = Math.max(maxDependencyLevel, nodeDependencyLevels.get(parent) + 1); // O(1)
            }
        }

        nodeDependencyLevels.put(node, maxDependencyLevel); // O(1)
    }

    /**
     * Extracts the Bill of Operations (BOO) order by calculating the dependency levels of operations and simulating the production execution.
     *//*
    public void extractBOOAndSimulate() {
        Map<ProductionNode, Integer> nodeDependencyLevels = new HashMap<>(); // O(1)

        Comparator<ProductionNode> comparator = (node1, node2) -> { // O(1)
            Integer level1 = nodeDependencyLevels.getOrDefault(node1, 0); // O(1)
            Integer level2 = nodeDependencyLevels.getOrDefault(node2, 0); // O(1)
            return level1.compareTo(level2); // O(1)
        };

        AVLTree<ProductionNode> operationAVL = new AVLTree<>(comparator); // O(1)

        for (ProductionNode node : productionTree.getAllNodes()) { // O(n)
            if (node.isOperation()) { // O(1)
                calculateDependencyLevel(node, nodeDependencyLevels); // O(n)
                operationAVL.insert(node); // O(log n)
                System.out.println("Inserted " + node.getName() + " into AVL tree with dependency level " +
                        nodeDependencyLevels.get(node)); // O(1)
            }
        }

        System.out.println("Processing operations in BOO order:"); // O(1)
        operationAVL.inOrderTraversal(nodeDependencyLevels); // O(n)
    }*/

    /**
     * Simulates the execution of production operations based on the dependency levels calculated earlier.
     */
    public void simulateProductionExecution() {
        Map<ProductionNode, Integer> nodeDependencyLevels = new HashMap<>(); // O(1)

        // Comparador para ordenar os nós com base no nível de dependência
        Comparator<ProductionNode> comparator = (node1, node2) ->
                Integer.compare(nodeDependencyLevels.get(node2), nodeDependencyLevels.get(node1)); // O(1)

        // Árvore AVL com o comparador
        operationAVL = new AVLTree<>(comparator); // O(1)

        // Preenche a árvore AVL com os nós ordenados por dependência
        for (ProductionNode node : productionTree.getAllNodes()) { // O(n)
            if (node.isOperation()) { // O(1)
                calculateDependencyLevel(node, nodeDependencyLevels); // O(n)
                int dependencyLevel = nodeDependencyLevels.get(node); // O(1)
                operationAVL.insert(node); // O(log n)
                System.out.println("Inserted " + node.getName() + " into AVL tree with dependency level " + dependencyLevel); // O(1)
            }
        }

        // Lista para armazenar as operações ordenadas por dependência
        List<ProductionNode> orderedOperations = new ArrayList<>();
        System.out.println("\nProcessing operations in BOO order:"); // O(1)

        // Adiciona as operações na lista com base na travessia em ordem da árvore AVL
        operationAVL.inOrderTraversal(nodeDependencyLevels, orderedOperations); // O(n)

        // Exibe as operações na ordem desejada
        for (ProductionNode operation : orderedOperations) {
            int dependencyLevel = nodeDependencyLevels.get(operation);
            System.out.println("Processing node in BOO order: " + operation.getName() +
                    " with dependency level " + dependencyLevel); // O(1)
        }
    }
}