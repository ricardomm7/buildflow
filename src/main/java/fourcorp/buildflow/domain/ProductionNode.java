package fourcorp.buildflow.domain;

import java.util.ArrayList;
import java.util.List;

public class ProductionNode {
    private String id;
    private String name;
    private boolean isMaterial;
    private int quantity; // Usado apenas para materiais
    private double cost;  // Custo associado ao nó de produção
    private ProductionNode parent; // Referência ao nó pai, se houver
    private List<ProductionNode> children;

    // Construtor para Operações
    public ProductionNode(String id, String name, double cost) {
        this.id = id;
        this.name = name;
        this.children = new ArrayList<>();
        this.isMaterial = false; // Padrão é operação
        this.cost = cost;
    }

    // Construtor para Materiais (com quantidade)
    public ProductionNode(String id, String name, int quantity, double cost) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.children = new ArrayList<>();
        this.isMaterial = true; // Marcar como material
        this.cost = cost;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isMaterial() {
        return isMaterial;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductionNode getParent() {
        return parent;
    }

    public List<ProductionNode> getChildren() {
        return children;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    public void setParent(ProductionNode parent) {
        this.parent = parent;
    }

    public void addChild(ProductionNode child) {
        child.setParent(this); // Define o pai do filho como este nó
        this.children.add(child);
    }

    public void setMaterial(boolean isMaterial) {
        this.isMaterial = isMaterial;
    }

    // Método para calcular a profundidade do nó na árvore de produção
    public int getDepth() {
        int depth = 0;
        ProductionNode currentNode = this;
        while (currentNode.parent != null) {
            depth++;
            currentNode = currentNode.parent;
        }
        return depth;
    }

    @Override
    public String toString() {
        return "ProductionNode{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isMaterial=" + isMaterial +
                ", quantity=" + quantity +
                ", parent=" + (parent != null ? parent.getName() : "None") +
                '}';
    }
}
