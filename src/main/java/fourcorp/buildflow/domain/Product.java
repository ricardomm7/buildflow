package fourcorp.buildflow.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Product implements Identifiable<String> {
    private String idItem;
    //private PriorityOrder priority;
    private LinkedList<Operation> operation;
    //private List<String> operationSequence;
    //private int currentOperationIndex;

    public Product(String idItem, List<Operation> operations) {
        this.idItem = idItem;
        //this.priority = priority;
        this.operation = new LinkedList<>(operations);
      //  this.operationSequence = new ArrayList<>(operationSequence);
      //  this.currentOperationIndex = 0; // Start at the first operation
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
   /* public List<String> getOperationSequence() {
        return operationSequence;
    }

    public void setOperationSequence(List<String> operationSequence) {
        this.operationSequence = operationSequence;
    }

    public String getNextOperation() {
        if (currentOperationIndex < operationSequence.size()) {
            return operationSequence.get(currentOperationIndex);
        }
        return null;
    }

    public void moveToNextOperation() {
        if (currentOperationIndex < operationSequence.size()) {
            currentOperationIndex++;
        }
    }*/

    @Override
    public String getId() {
        return idItem;
    }
}