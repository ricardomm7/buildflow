package fourcorp.buildflow.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product implements Identifiable<String> {
    Map<String, Product> articles = new HashMap<>();
    private String idItem;
    private PriorityOrder priority;
    private List<Operation> operation;

    public Product(String idItem, PriorityOrder priority, List<Operation> operations) {
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

    public List<Operation> getOperations() {
        return operation;
    }

    public void setOperations(List<Operation> operations) {
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