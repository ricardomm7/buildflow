package fourcorp.buildflow.domain;

import java.util.LinkedList;
import java.util.List;

public class Product implements Identifiable<String> {
    private String idItem;
    //private PriorityOrder priority;
    private LinkedList<Operation> operation;

    public Product(String idItem, List<Operation> operations) {
        this.idItem = idItem;
        //this.priority = priority;
        this.operation = new LinkedList<>(operations);
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

    public void setOperations(LinkedList<Operation> operations) {
        this.operation = operations;
    }
/*
    public PriorityOrder getPriority() {
        return priority;
    }

    public void setPriority(PriorityOrder priority) {
        this.priority = priority;
    }

 */

    @Override
    public String getId() {
        return idItem;
    }
}