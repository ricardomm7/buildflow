package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.util.*;

public class ProductionTreeSearcher {

    private final ProductionTree productionTree;
    private Map<ProductionNode, Map<ProductionNode, Double>> connections;
    private AVLTree<ProductionNode> operationAVL;

    public ProductionTreeSearcher() {
        // Initialize with the repository's production tree
        this.productionTree = Repositories.getInstance().getProductionTree();
        this.connections = productionTree.getConnections();
    }

    public void handleNodeSearch() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID or name of the node to search: ");
        String identifier = scanner.nextLine();

        String result = searchNodeByNameOrId(identifier);

        System.out.println(result);
    }

    // Method to search for a node by its name or ID
    public String searchNodeByNameOrId(String identifier) {
        List<ProductionNode> matchingNodes = productionTree.searchNodes(identifier);  // Get all matching nodes

        // If no nodes match, return an appropriate message
        if (matchingNodes.isEmpty()) {
            return "No matching nodes found.";
        }

        // If there's only one result, return its details directly
        if (matchingNodes.size() == 1) {
            return getNodeDetails(matchingNodes.get(0));
        }

        // If there are multiple matches, prompt the user to select one
        System.out.println("Multiple matches found:");
        for (int i = 0; i < matchingNodes.size(); i++) {
            System.out.println((i + 1) + ". " + matchingNodes.get(i).getName() + " (ID: " + matchingNodes.get(i).getId() + ")");
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please select a node by entering the number (1-" + matchingNodes.size() + "): ");
        int choice = scanner.nextInt();

        // Validate user choice
        if (choice < 1 || choice > matchingNodes.size()) {
            return "Invalid choice.";
        }

        // Return the details of the selected node
        return getNodeDetails(matchingNodes.get(choice - 1));
    }

    // Helper method to get the details of a node
    private String getNodeDetails(ProductionNode node) {
        StringBuilder result = new StringBuilder("Node Details:\n");
        result.append("ID: ").append(node.getId()).append("\n");
        result.append("Name: ").append(node.getName()).append("\n");

        // Identify the type based on the ID format
        if (isMaterialId(node.getId())) {
            result.append("Type: Material\n");

            // Handle material details
            Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(node);
            if (!subNodes.isEmpty()) {
                result.append("Material Quantities:\n");
                subNodes.forEach((materialNode, quantity) ->
                        result.append("  ").append(materialNode.getName())
                                .append(" - Quantity: ").append(quantity).append("\n"));
            } else {
                result.append("No material details available.\n");
            }
        } else if (isOperationId(node.getId())) {
            result.append("Type: Operation\n");

            // Check if parent is an Operation (not just a ProductionNode)
            Object parentOperation = node.getParent();
            if (parentOperation instanceof Operation) {
                result.append("Parent Operation:\n");
                Operation parentOp = (Operation) parentOperation;
                result.append("  ID: ").append(parentOp.getId()).append("\n");
                result.append("  Name: ").append(parentOp.getName()).append("\n");
            } else {
                result.append("No parent operation available.\n");
            }

            // Handle sub-nodes or material information
            Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(node);
            if (!subNodes.isEmpty()) {
                result.append("Sub-nodes (Materials):\n");
                subNodes.forEach((materialNode, quantity) ->
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


    private boolean isOperationId(String id) {
        try {
            int numericId = Integer.parseInt(id);
            return numericId < 100; // Assuming IDs less than 100 are for materials
        } catch (NumberFormatException e) {
            return false; // Invalid format, can't be a material ID
        }
    }

    // Helper method to check if ID corresponds to an Operation
    private boolean isMaterialId(String id) {
        try {
            int numericId = Integer.parseInt(id);
            return numericId >= 100; // Assuming IDs 100 and above are for materials
        } catch (NumberFormatException e) {
            return false; // Invalid format, can't be an operation ID
        }
    }

    public void calculateQuantityOfMaterials() {
        if (connections == null || connections.isEmpty()) {
            System.out.println("No connections available.");
            return;
        }

        System.out.println("Material --> Quantity");

        // Mapa para armazenar as quantidades somadas por nome
        Map<String, Double> totalQuantities = new HashMap<>();

        // Iterar sobre as conexões
        for (Map.Entry<ProductionNode, Map<ProductionNode, Double>> entry : connections.entrySet()) {
            ProductionNode node = entry.getKey(); // Nó de origem
            Map<ProductionNode, Double> connectedNodes = entry.getValue(); // Nós conectados e suas quantidades

            if (node.isOperation()) {
                // Se o nó é uma operação, iterar nos materiais conectados a ela
                if (connectedNodes != null) {
                    for (Map.Entry<ProductionNode, Double> connectedEntry : connectedNodes.entrySet()) {
                        ProductionNode connectedNode = connectedEntry.getKey();
                        Double quantity = connectedEntry.getValue();

                        // Acumular apenas se o nó conectado é um material
                        if (connectedNode.isProduct()) {
                            totalQuantities.merge(connectedNode.getName(), quantity, Double::sum);
                        }
                    }
                }
            } else if (node.isProduct()) {
                // Se o nó é um material, considerar diretamente
                totalQuantities.merge(node.getName(), node.getQuantity(), Double::sum);
            }
        }

        // Imprimir os resultados acumulados
        for (Map.Entry<String, Double> entry : totalQuantities.entrySet()) {
            System.out.println(entry.getKey() + " --> " + entry.getValue());
        }
    }

    public void calculateDependencyLevel(ProductionNode node, Map<ProductionNode, Integer> nodeDependencyLevels) {
        if (nodeDependencyLevels.containsKey(node)) {
            return;
        }

        int maxDependencyLevel = 0;
        List<ProductionNode> parentNodes = productionTree.getParentNodes(node);

        if (parentNodes != null) { // Verifica se há pais
            for (ProductionNode parent : parentNodes) {
                calculateDependencyLevel(parent, nodeDependencyLevels);
                maxDependencyLevel = Math.max(maxDependencyLevel, nodeDependencyLevels.get(parent) + 1);
            }
        }

        nodeDependencyLevels.put(node, maxDependencyLevel); // Define o nível, mesmo que seja 0
    }

    public void extractBOOAndSimulate() {
        Map<ProductionNode, Integer> nodeDependencyLevels = new HashMap<>();

        // Comparador baseado no nível de dependência
        Comparator<ProductionNode> comparator = (node1, node2) -> {
            Integer level1 = nodeDependencyLevels.getOrDefault(node1, 0);
            Integer level2 = nodeDependencyLevels.getOrDefault(node2, 0);
            return level1.compareTo(level2); // Comparar níveis de dependência
        };

        AVLTree<ProductionNode> operationAVL = new AVLTree<>(comparator);

        // Calcular níveis de dependência e inserir na AVL tree
        for (ProductionNode node : productionTree.getAllNodes()) {
            if (node.isOperation()) {
                calculateDependencyLevel(node, nodeDependencyLevels); // Calcula o nível
                operationAVL.insert(node); // Insere na AVL tree
                System.out.println("Inserted " + node.getName() + " into AVL tree with dependency level " +
                        nodeDependencyLevels.get(node));
            }
        }

        // Processar as operações na ordem BOO
        System.out.println("Processing operations in BOO order:");
        operationAVL.inOrderTraversal(nodeDependencyLevels); // Processa os nós
    }

    public void simulateProductionExecution() {
        // Mapa para armazenar os níveis de dependência
        Map<ProductionNode, Integer> nodeDependencyLevels = new HashMap<>();

        // Cria o comparador baseado nos níveis de dependência (ordem decrescente)
        Comparator<ProductionNode> comparator = (node1, node2) ->
                Integer.compare(nodeDependencyLevels.get(node2), nodeDependencyLevels.get(node1));

        // Inicializa a AVLTree com o comparador
        operationAVL = new AVLTree<>(comparator);

        // Calcular os níveis de dependência e inserir na árvore AVL
        for (ProductionNode node : productionTree.getAllNodes()) {
            if (node.isOperation()) {
                calculateDependencyLevel(node, nodeDependencyLevels);  // Calcula o nível de dependência
                int dependencyLevel = nodeDependencyLevels.get(node);  // Acessa o nível de dependência
                operationAVL.insert(node);  // Insere o nó na árvore AVL
                System.out.println("Inserted " + node.getName() + " into AVL tree with dependency level " + dependencyLevel);
            }
        }

        // Simula a execução processando as operações na ordem BOO
        System.out.println("Processing operations in BOO order:");
        operationAVL.inOrderTraversal(nodeDependencyLevels);  // Passa o mapa de dependências para a simulação
    }
}
