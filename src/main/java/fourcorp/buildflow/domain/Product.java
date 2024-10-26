package fourcorp.buildflow.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Product implements Identifiable<String> {
    private String idItem;
    private LinkedList<Operation> operation;
    private int currentOperationIndex;

    public Product(String idItem, List<Operation> operations) {
        this.idItem = idItem;
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

    public Operation getCurrentOperation() {
        if (currentOperationIndex < operation.size()) {
            return operation.get(currentOperationIndex);
        }
        return null;
    }

    public boolean moveToNextOperation() {
        if (currentOperationIndex < operation.size() - 1) {
            currentOperationIndex++;
            return true;
        }
        return false;
    }

    public boolean hasMoreOperations() {
        return currentOperationIndex < operation.size();
    }

    public void setCurrentOperationIndex(int currentOperationIndex) {
        this.currentOperationIndex = currentOperationIndex;
    }

    @Override
    public String getId() {
        return idItem;
    }
}