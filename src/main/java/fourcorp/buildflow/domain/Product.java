package fourcorp.buildflow.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product implements Identifiable<String> {
    Map<String, Product> articles = new HashMap<>();
    private String idItem;
    private PriorityOrder priority;
    private List<String> operation;

    public Product(String idItem, PriorityOrder priority, List<String> operations) {
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

    public PriorityOrder getPriority() {
        return priority;
    }

    public void setPriority(PriorityOrder priority) {
        this.priority = priority;
    }

    @Override
    public String getId() {
        return idItem;
    }
}