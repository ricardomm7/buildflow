package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.ProductionNode;

import java.util.*;

public class ProductionTree {
    private List<ProductionNode> nodes; // List of all nodes
    private Map<ProductionNode, Map<ProductionNode, Double>> connections; // Relations between nodes
    private Map<String, ProductionNode> nodesMap; // Nodes mapped by Name for quick lookup

    public ProductionTree() {
        nodes = new ArrayList<>();
        connections = new HashMap<>();
        nodesMap = new HashMap<>();
    }

    public void insertProductionNode(String id, String name, boolean isProduct) {
        if (getNodeById(id) == null) {
            ProductionNode node = new ProductionNode(id, name, isProduct);
            nodes.add(node);
            connections.put(node, new HashMap<>());
            nodesMap.put(id.toLowerCase(), node);
        }
    }

    public void insertNewConnection(String parentId, String childId, double quantity) {
        ProductionNode parent = getNodeById(parentId);
        ProductionNode child = getNodeById(childId);

        if (parent != null && child != null) {
            connections.get(parent).put(child, quantity);
        } else {
            System.err.println("Parent or Child not found: " + parentId + " or " + childId);
        }
    }

    public ProductionNode getNodeById(String id) {
        return nodesMap.get(id.toLowerCase());
    }

    public List<ProductionNode> searchNodes(String query) {
        List<ProductionNode> results = new ArrayList<>();

        // Search by ID (case-insensitive)
        for (ProductionNode node : nodes) {
            if (node.getId().toLowerCase().contains(query.toLowerCase())) {
                results.add(node);
            }
        }

        // Search by name (case-insensitive)
        for (ProductionNode node : nodes) {
            if (node.getName().toLowerCase().contains(query.toLowerCase()) && !results.contains(node)) {
                results.add(node);
            }
        }

        return results;
    }

    public ProductionNode promptUserToSelectNode(List<ProductionNode> nodes) {
        if (nodes.isEmpty()) {
            System.out.println("No matching nodes found.");
            return null;
        }

        // Display all the matching nodes
        System.out.println("Search results:");
        for (int i = 0; i < nodes.size(); i++) {
            System.out.println(i + 1 + ". " + nodes.get(i));
        }

        // Ask the user to select one
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please select a node by entering the number (1-" + nodes.size() + "): ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > nodes.size()) {
            System.out.println("Invalid choice.");
            return null;
        }

        return nodes.get(choice - 1);
    }

    public List<ProductionNode> getAllNodes() {
        return nodes;
    }

    public Map<ProductionNode, Double> getSubNodes(ProductionNode node) {
        return connections.getOrDefault(node, new HashMap<>());
    }

    public Map<String, ProductionNode> getNodesMap() {
        return nodesMap;
    }
}

