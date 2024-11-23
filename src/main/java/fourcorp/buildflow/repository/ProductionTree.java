package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.ProductionNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ProductionTree class represents a tree-like structure to manage production nodes and their dependencies.
 * It allows inserting nodes, adding dependencies between them, and managing their quantities. The tree also supports
 * searching nodes, retrieving the critical path, and propagating updates to dependent nodes.
 */
public class ProductionTree {
    private List<ProductionNode> nodes; // List of all nodes
    private Map<ProductionNode, Map<ProductionNode, Double>> connections; // Relations between nodes
    private Map<String, ProductionNode> nodesMap; // Nodes mapped by Name for quick lookup

    /**
     * Constructs an empty ProductionTree.
     */
    public ProductionTree() {
        nodes = new ArrayList<>();
        connections = new HashMap<>();
        nodesMap = new HashMap<>();
    }

    /**
     * Searches for nodes based on the provided query. The search is case-insensitive and looks for matches
     * in both the ID and the name of the nodes.
     *
     * @param query The query string to search for.
     * @return A list of nodes whose ID or name contains the query.
     */
    public List<ProductionNode> searchNodes(String query) {
        List<ProductionNode> results = new ArrayList<>();

        for (ProductionNode node : nodes) { // O(n)
            if (node.getId().toLowerCase().contains(query.toLowerCase())) {
                results.add(node);
            }
        }

        for (ProductionNode node : nodes) { // O(n)
            if (node.getName().toLowerCase().contains(query.toLowerCase()) && !results.contains(node)) {
                results.add(node);
            }
        }

        return results;
    }

    /**
     * Adds a new node to the tree.
     *
     * @param node The node to be added.
     * @throws IllegalArgumentException If the node is null.
     */
    public void addNode(ProductionNode node) {
        if (node == null) {
            throw new IllegalArgumentException("O nó não pode ser nulo.");
        }
        nodes.add(node);
        nodesMap.put(node.getId().toLowerCase(), node); // Adds the node to the map for efficient lookup
    }

    /**
     * Adds a dependency from one node (operationA) to another (operationB).
     *
     * @param operationB The dependent node (child).
     * @param operationA The parent node.
     * @throws IllegalArgumentException If the dependency would create a circular reference.
     */
    public void addDependency(ProductionNode operationB, ProductionNode operationA) {
        if (operationB.equals(operationA)) {
            throw new IllegalArgumentException("Cannot add direct circular dependency from a node to itself");
        }

        if (!connections.containsKey(operationA)) {
            connections.put(operationA, new HashMap<>());
        }
        connections.get(operationA).put(operationB, 1.0);
    }

    /**
     * Calculates and returns the critical path in the production process.
     * The critical path is the longest path from any node to a leaf node, where each node represents a task
     * and the edges represent dependencies.
     *
     * @return A list of nodes representing the critical path in the production process.
     */
    public List<ProductionNode> getCriticalPath() {
        List<ProductionNode> criticalPath = new ArrayList<>();
        if (nodes.isEmpty()) {
            return criticalPath;
        }

        Map<ProductionNode, Double> longestPaths = new HashMap<>();
        Map<ProductionNode, ProductionNode> predecessors = new HashMap<>();

        for (ProductionNode node : nodes) { // O(n)
            longestPaths.put(node, 0.0);
        }

        for (ProductionNode node : nodes) { // O(n)
            Map<ProductionNode, Double> subNodes = getSubNodes(node);
            double currentPathLength = longestPaths.get(node);

            for (Map.Entry<ProductionNode, Double> subNode : subNodes.entrySet()) { // O(n^2)
                ProductionNode target = subNode.getKey();
                double newPathLength = currentPathLength + subNode.getValue();

                if (newPathLength > longestPaths.get(target)) {
                    longestPaths.put(target, newPathLength);
                    predecessors.put(target, node);
                }
            }
        }

        ProductionNode maxNode = null;
        double maxLength = Double.NEGATIVE_INFINITY;

        for (Map.Entry<ProductionNode, Double> entry : longestPaths.entrySet()) { // O(n)
            if (entry.getValue() > maxLength) {
                maxLength = entry.getValue();
                maxNode = entry.getKey();
            }
        }

        while (maxNode != null) { // O(n)
            criticalPath.add(0, maxNode);
            maxNode = predecessors.get(maxNode);
        }

        return criticalPath;
    }

    /**
     * Inserts a new production node if it does not already exist in the tree.
     *
     * @param id        The ID of the new node.
     * @param name      The name of the new node.
     * @param isProduct A flag indicating whether the node is a product.
     */
    public void insertProductionNode(String id, String name, boolean isProduct) {
        if (getNodeById(id) == null) {
            ProductionNode node = new ProductionNode(id, name, isProduct);
            nodes.add(node);
            connections.put(node, new HashMap<>());
            nodesMap.put(id.toLowerCase(), node);
        }
    }

    /**
     * Adds a new connection between two nodes, indicating a dependency from parent to child.
     *
     * @param parentId The ID of the parent node.
     * @param childId  The ID of the child node.
     * @param quantity The quantity associated with this connection.
     */
    public void insertNewConnection(String parentId, String childId, double quantity) {
        ProductionNode parent = getNodeById(parentId);
        ProductionNode child = getNodeById(childId);

        if (parent != null && child != null) {
            connections.get(parent).put(child, quantity);
        } else {
            System.err.println("Parent or Child not found: " + parentId + " or " + childId);
        }
    }

    /**
     * Retrieves a node by its ID.
     *
     * @param id The ID of the node to retrieve.
     * @return The node with the given ID, or null if not found.
     */
    public ProductionNode getNodeById(String id) {
        return nodesMap.get(id.toLowerCase());
    }

    /**
     * Gets node by name or id.
     *
     * @param nameOrId the name or id
     * @return the node by name or id
     */
    public ProductionNode getNodeByNameOrId(String nameOrId) {
        ProductionNode node = nodesMap.get(nameOrId.toLowerCase());
        if (node != null) {
            return node;
        }
        for (ProductionNode n : nodes) {
            if (n.getName().equalsIgnoreCase(nameOrId) || n.getId().equalsIgnoreCase(nameOrId)) {
                return n;
            }
        }
        return null;

    }

    /**
     * Retrieves all parent nodes of a given node.
     *
     * @param node The node for which to find parent nodes.
     * @return A list of parent nodes.
     */
    public List<ProductionNode> getParentNodes(ProductionNode node) {
        List<ProductionNode> parents = new ArrayList<>();
        for (Map.Entry<ProductionNode, Map<ProductionNode, Double>> entry : connections.entrySet()) { // O(n)
            if (entry.getValue().containsKey(node)) {
                parents.add(entry.getKey());
            }
        }
        return parents;
    }

    /**
     * Retrieves all nodes in the tree.
     *
     * @return A list of all production nodes.
     */
    public List<ProductionNode> getAllNodes() {
        return nodes;
    }

    /**
     * Retrieves all the sub-nodes (dependencies) of a given node.
     *
     * @param node The node whose sub-nodes to retrieve.
     * @return A map of sub-nodes and their corresponding quantities.
     */
    public Map<ProductionNode, Double> getSubNodes(ProductionNode node) {
        return connections.getOrDefault(node, new HashMap<>());
    }

    /**
     * Retrieves all connections between nodes.
     *
     * @return A map of connections where the key is a node and the value is a map of child nodes with their quantities.
     */
    public Map<ProductionNode, Map<ProductionNode, Double>> getConnections() {
        return connections;
    }

    /**
     * Updates the quantity of a given node and propagates the update to all dependent nodes.
     *
     * @param nodeToUpdate The node whose quantity needs to be updated.
     * @param newQuantity  The new quantity to set for the node.
     */
    public void updateConnectionsQuantity(ProductionNode nodeToUpdate, double newQuantity) {
        double oldQuantity = nodeToUpdate.getQuantity();
        nodeToUpdate.setQuantity(newQuantity);

        // Propagate the updated quantity to all descendant nodes
        propagateQuantityUpdate(nodeToUpdate, newQuantity, oldQuantity);

        // Update all connections' quantities based on the new quantity
        updateConnectionQuantities(nodeToUpdate, newQuantity);
        deleteAndCreateNewConnections(nodeToUpdate, newQuantity);

    }

    /**
     * Deletes and recreates connections with updated quantities after a node's quantity is updated.
     *
     * @param nodeToUpdate The node whose connections need to be updated.
     * @param newQuantity  The updated quantity.
     */
    private void deleteAndCreateNewConnections(ProductionNode nodeToUpdate, double newQuantity) {
        for (Map.Entry<ProductionNode, Map<ProductionNode, Double>> entry : connections.entrySet()) {
            Map<ProductionNode, Double> subNodes = entry.getValue();

            if (subNodes.containsKey(nodeToUpdate)) {
                subNodes.remove(nodeToUpdate);
                subNodes.put(nodeToUpdate, newQuantity); // Update the connection with the new quantity
            }
        }
    }


    /**
     * Propagates the quantity update from a parent node to its child nodes.
     *
     * @param node        The node whose quantity update needs to be propagated.
     * @param newQuantity The new quantity of the parent node.
     */
    private void propagateQuantityUpdate(ProductionNode node, double newQuantity, double oldQuantity) {
        if (!connections.containsKey(node)) {
            return; // No sub-nodes, nothing to propagate
        }

        // Calculate the ratio for propagating the quantity change
        double ratio = newQuantity / oldQuantity;

        // Iterate through all sub-nodes (descendants)
        Map<ProductionNode, Double> subNodes = connections.get(node);
        for (Map.Entry<ProductionNode, Double> entry : subNodes.entrySet()) {
            ProductionNode childNode = entry.getKey();
            double connectionQuantity = entry.getValue();

            // Recalculate the child node's new quantity
            double newChildQuantity = newQuantity * connectionQuantity;

            // Update the child node's quantity
            childNode.setQuantity(newChildQuantity);

            // Recursively propagate the update to all descendants of the child node
            propagateQuantityUpdate(childNode, newChildQuantity, childNode.getQuantity());
        }
    }

    /**
     * Updates the quantities of all connections for a given node.
     *
     * @param nodeToUpdate The node whose connections need to be updated.
     */
    private void updateConnectionQuantities(ProductionNode nodeToUpdate, double newQuantity) {
        if (!connections.containsKey(nodeToUpdate)) {
            return;
        }

        Map<ProductionNode, Double> subNodes = connections.get(nodeToUpdate);
        for (Map.Entry<ProductionNode, Double> entry : subNodes.entrySet()) {
            ProductionNode childNode = entry.getKey();

            // Recalculate the connection quantity
            double connectionQuantity = entry.getValue();
            double newConnectionQuantity = newQuantity * connectionQuantity;

            // Update the connection quantity between the parent node and the child node
            entry.setValue(newConnectionQuantity);

            // Recurse into the child node to update all connections
            updateConnectionQuantities(childNode, newConnectionQuantity);
        }
    }


    /**
     * Clears all nodes and connections from the tree.
     */
    public void clear() {
        nodes.clear();
        connections.clear();
        nodesMap.clear();
    }
}