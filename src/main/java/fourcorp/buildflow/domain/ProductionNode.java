package fourcorp.buildflow.domain;

import java.util.HashMap;
import java.util.Map;

public class ProductionNode {
    private String id;
    private String name;
    private boolean isProduct;  // true se for produto, false se for operação
    private Map<ProductionNode, Integer> subNodes = new HashMap<>();  // Subitens ou suboperações

    public ProductionNode(String id, String name, boolean isProduct) {
        this.id = id;
        this.name = name;
        this.isProduct = isProduct;
    }

    public String getId() {
        return id;
    }

    public boolean isOperation(){
        return !isProduct;
    }

    public String getName() {
        return name;
    }

    public boolean isProduct() {
        return isProduct;
    }

    public Map<ProductionNode, Integer> getSubNodes() {
        return subNodes;
    }

    public void addSubNode(ProductionNode subNode, int quantity) {
        subNodes.put(subNode, quantity);
    }

    public String toString() {
        return (isProduct ? "Produto: " : "Operação: ") + name + " (ID: " + id + ")";
    }
}
