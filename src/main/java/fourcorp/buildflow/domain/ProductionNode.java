package fourcorp.buildflow.domain;

import fourcorp.buildflow.repository.ProductionTree;

import java.util.List;

public class ProductionNode {
    private String id;
    private String name;
    private boolean isProduct;
    private ProductionNode parent;  // Reference to the parent operation (if any)
    private double quantity;  // Quantity for materials

    public ProductionNode(String id, String name, boolean isProduct) {
        this.id = id;
        this.name = name;
        this.isProduct = isProduct;
        this.parent = null;
        this.quantity = 0;  // Default quantity (for operations, set to 0)
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isProduct() {
        return isProduct;
    }

    public boolean isOperation() {
        return !isProduct;
    }

    public ProductionNode getParent() {
        return parent;
    }

    public void setParent(ProductionNode parent) {
        this.parent = parent;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getProducedQuantity() {
        return quantity;
    }

    public int getDepth(ProductionTree productionTree) {
        return calculateDepth(this, productionTree);
    }

    private int calculateDepth(ProductionNode node, ProductionTree tree) {
        List<ProductionNode> parents = tree.getParentNodes(node);
        if (parents.isEmpty()) {
            return 0; // Nó raiz
        }
        // Calcula a profundidade máxima entre os pais
        return 1 + parents.stream()
                .mapToInt(parent -> calculateDepth(parent, tree))
                .max()
                .orElse(0);
    }

    @Override
    public String toString() {
        return (isProduct ? "Product: " : "Operation: ") + name + " (ID: " + id + ")";
    }
}

