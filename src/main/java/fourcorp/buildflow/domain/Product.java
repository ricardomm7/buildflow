package fourcorp.buildflow.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    Map<String, Product> articles = new HashMap<>();
    private String idItem;
    private int priority;
    private List<Operation> operation;

    public Product(String idItem, int priority, List<String> operations) {
        this.idItem = idItem;
        this.priority = priority;
        this.operation = new ArrayList<>(operations);
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public List<String> getOperations() {
        return operation;
    }

    public void setOperations(List<String> operations) {
        this.operation = operations;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}