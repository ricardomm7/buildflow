package fourcorp.buildflow.domain;

public class ProductionNode {
    private String id;
    private String name;
    private boolean isProduct;

    public ProductionNode(String id, String name, boolean isProduct) {
        this.id = id;
        this.name = name;
        this.isProduct = isProduct;
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

    @Override
    public String toString() {
        return (isProduct ? "Product: " : "Operation: ") + name + " (ID: " + id + ")";
    }
}
