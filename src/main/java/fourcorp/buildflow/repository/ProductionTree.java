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
            nodesMap.put(id, node);
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
        return nodesMap.get(id);
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

