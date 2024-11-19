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

    /*public void updateMaterialQuantity(String materialId, double newQuantity) {
        ProductionNode materialNode = getNodeById(materialId);

        if (materialNode != null) {
            materialNode.setQuantity(newQuantity);
            System.out.println("Material quantity updated for " + materialNode.getName() + " (ID: " + materialNode.getId() + ")");
        } else {
            System.out.println("Material with ID " + materialId + " not found.");
        }
    }*/

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

    /**
     * Retrieves the parent nodes (predecessors) of a given node.
     *
     * @param node the node whose parents are to be retrieved
     * @return a list of parent nodes
     */
    public List<ProductionNode> getParentNodes(ProductionNode node) {
        List<ProductionNode> parents = new ArrayList<>();
        for (Map.Entry<ProductionNode, Map<ProductionNode, Double>> entry : connections.entrySet()) {
            if (entry.getValue().containsKey(node)) {
                parents.add(entry.getKey());
            }
        }
        return parents;
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

    public void addNode(ProductionNode operationA) {
        nodes.add(operationA);
    }

    public void addDependency(ProductionNode operationB, ProductionNode operationA) {
        if (operationB.equals(operationA)) {
            throw new IllegalArgumentException("Cannot add direct circular dependency from a node to itself");
        }

        if (!connections.containsKey(operationA)) {
            connections.put(operationA, new HashMap<>());
        }
        connections.get(operationA).put(operationB, 1.0);
    }

    public List<ProductionNode> getCriticalPath() {
        List<ProductionNode> criticalPath = new ArrayList<>();
        if (nodes.isEmpty()) {
            return criticalPath;
        }

        Map<ProductionNode, Double> longestPaths = new HashMap<>();
        Map<ProductionNode, ProductionNode> predecessors = new HashMap<>();

        // Initialize distances
        for (ProductionNode node : nodes) {
            longestPaths.put(node, 0.0);
        }

        // Calculate longest paths
        for (ProductionNode node : nodes) {
            Map<ProductionNode, Double> subNodes = getSubNodes(node);
            double currentPathLength = longestPaths.get(node);

            for (Map.Entry<ProductionNode, Double> subNode : subNodes.entrySet()) {
                ProductionNode target = subNode.getKey();
                double newPathLength = currentPathLength + subNode.getValue();

                if (newPathLength > longestPaths.get(target)) {
                    longestPaths.put(target, newPathLength);
                    predecessors.put(target, node);
                }
            }
        }

        // Find the node with maximum path length
        ProductionNode maxNode = null;
        double maxLength = Double.NEGATIVE_INFINITY;

        for (Map.Entry<ProductionNode, Double> entry : longestPaths.entrySet()) {
            if (entry.getValue() > maxLength) {
                maxLength = entry.getValue();
                maxNode = entry.getKey();
            }
        }

        // Reconstruct the critical path
        while (maxNode != null) {
            criticalPath.add(0, maxNode);
            maxNode = predecessors.get(maxNode);
        }

        return criticalPath;
    }
}

