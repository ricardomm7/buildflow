package fourcorp.buildflow.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Product implements Identifiable<String> {
    private String idItem;
    private LinkedList<Operation> operation;

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

    @Override
    public String getId() {
        return idItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return idItem.equalsIgnoreCase(product.idItem);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idItem);
    }
}