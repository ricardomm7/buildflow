package fourcorp.buildflow.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a product in the system, with a unique identifier, a list of operations,
 * and tracking of the current operation's progress. Implements the Identifiable interface.
 */
public class Product implements Identifiable<String> {
    private String idItem;
    private LinkedList<Operation> operation;
    private int currentOperationIndex;

    /**
     * Constructs a Product with the specified identifier and list of operations.
     *
     * @param idItem     the unique identifier for the product
     * @param operations the list of operations associated with the product
     */
    public Product(String idItem, List<Operation> operations) {
        this.idItem = idItem;
        this.operation = new LinkedList<>(operations);
    }

    /**
     * Gets the unique identifier for the product.
     *
     * @return the product ID
     */
    public String getIdItem() {
        return idItem;
    }

    /**
     * Sets the unique identifier for the product.
     *
     * @param idItem the product ID to set
     */
    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    /**
     * Gets the list of operations associated with the product.
     *
     * @return the list of operations
     */
    public List<Operation> getOperations() {
        return operation;
    }

    /**
     * Sets the list of operations associated with the product.
     *
     * @param operations the LinkedList of operations to set
     */
    public void setOperations(LinkedList<Operation> operations) {
        this.operation = operations;
    }

    /**
     * Gets the current operation being performed on the product.
     *
     * @return the current Operation, or null if there are no more operations
     */
    public Operation getCurrentOperation() {
        if (currentOperationIndex < operation.size()) {
            return operation.get(currentOperationIndex);
        }
        return null;
    }

    /**
     * Advances to the next operation in the list.
     *
     * @return true if successfully moved to the next operation, false if at the end of the list
     */
    public boolean moveToNextOperation() {
        if (currentOperationIndex < operation.size() - 1) {
            currentOperationIndex++;
            return true;
        }
        return false;
    }

    /**
     * Checks if there are more operations remaining for the product.
     *
     * @return true if more operations are available, false otherwise
     */
    public boolean hasMoreOperations() {
        return currentOperationIndex < operation.size();
    }

    /**
     * Sets the index of the current operation.
     *
     * @param currentOperationIndex the index to set as the current operation
     */
    public void setCurrentOperationIndex(int currentOperationIndex) {
        this.currentOperationIndex = currentOperationIndex;
    }

    /**
     * Gets the unique identifier for the product, implementing the Identifiable interface.
     *
     * @return the product ID
     */
    @Override
    public String getId() {
        return idItem;
    }
}