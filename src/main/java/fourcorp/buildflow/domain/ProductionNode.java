package fourcorp.buildflow.domain;

import fourcorp.buildflow.repository.ProductionTree;

import java.util.List;

/**
 * The ProductionNode class represents a node in the production tree, which can either be an operation or a product/material.
 * It holds information about the node's ID, name, quantity (for materials), and parent (if it's part of an operation).
 * This class provides methods for managing and querying production nodes in a production tree structure.
 */
public class ProductionNode implements Comparable<ProductionNode> {
    private String id;
    private String name;
    private boolean isProduct;
    private ProductionNode parent;  // Reference to the parent operation (if any)
    private double quantity;  // Quantity for materials

    /**
     * Constructs a new ProductionNode with the specified ID, name, and type (product or operation).
     *
     * @param id        The unique identifier for the node.
     * @param name      The name of the node.
     * @param isProduct A flag indicating whether this node represents a product (true) or an operation (false).
     */
    public ProductionNode(String id, String name, boolean isProduct) {
        this.id = id;
        this.name = name;
        this.isProduct = isProduct;
        this.parent = null;
        this.quantity = 0;  // Default quantity (for operations, set to 0)
    }

    /**
     * Gets the unique ID of this production node.
     *
     * @return The ID of the node.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of this production node.
     *
     * @return The name of the node.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this node represents a product.
     *
     * @return true if this node is a product, false if it's an operation.
     */
    public boolean isProduct() {
        return isProduct;
    }

    /**
     * Checks if this node represents an operation.
     *
     * @return true if this node is an operation, false if it's a product.
     */
    public boolean isOperation() {
        return !isProduct;
    }

    /**
     * Gets the parent node of this production node, which represents the operation to which this product belongs.
     *
     * @return The parent operation node, or null if this node is a root product or operation.
     */
    public ProductionNode getParent() {
        return parent;
    }

    /**
     * Sets the parent node for this production node.
     *
     * @param parent The parent operation node to set.
     */
    public void setParent(ProductionNode parent) {
        this.parent = parent;
    }

    /**
     * Gets the quantity associated with this production node (only meaningful for product nodes).
     *
     * @return The quantity of the product node.
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity for this production node. This value is typically used for product nodes to define the amount produced.
     *
     * @param quantity The quantity to set.
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the produced quantity for this production node, which is the same as the quantity in this implementation.
     *
     * @return The produced quantity of the product node.
     */
    public double getProducedQuantity() {
        return quantity;
    }

    /**
     * Calculates the depth of this production node in the production tree, based on the number of parent operations.
     * The depth represents how many levels up the node is in the tree from the root node.
     *
     * @param productionTree The production tree object used to retrieve parent nodes.
     * @return The depth of the node in the production tree.
     */
    public int getDepth(ProductionTree productionTree) {
        return calculateDepth(this, productionTree);
    }

    /**
     * Helper method to calculate the depth of this production node by traversing up its parent nodes.
     *
     * @param node The current node for which to calculate the depth.
     * @param tree The production tree object used to retrieve parent nodes.
     * @return The depth of the node in the production tree.
     */
    private int calculateDepth(ProductionNode node, ProductionTree tree) {
        List<ProductionNode> parents = tree.getParentNodes(node);
        if (parents.isEmpty()) {
            return 0; // Root node, no parents
        }
        // Calculate the maximum depth among all parent nodes
        return 1 + parents.stream()
                .mapToInt(parent -> calculateDepth(parent, tree))
                .max()
                .orElse(0);
    }

    /**
     * Returns a string representation of this production node, including its type (Product or Operation),
     * name, and ID.
     *
     * @return A string representation of the production node.
     */
    @Override
    public String toString() {
        return (isProduct ? "Product: " : "Operation: ") + name + " (ID: " + id + ")";
    }

    /**
     * Compares this production node with another production node for sorting purposes.
     *
     * @param o The production node to compare with.
     * @return 0 for equality (default implementation); this method can be customized for specific sorting logic.
     */
    @Override
    public int compareTo(ProductionNode o) {
        return 0; // Default comparison, can be overridden for custom sorting
    }
}
