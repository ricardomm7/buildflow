package fourcorp.buildflow.domain;

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

    @Override
    public String toString() {
        return (isProduct ? "Product: " : "Operation: ") + name + " (ID: " + id + ")";
    }
}

