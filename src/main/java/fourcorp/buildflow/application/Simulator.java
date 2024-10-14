package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.MapLinked;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Simulator {
    private MapLinked<Operation, Product, String> operationQueues;
    private List<Product> products;
    private final WorkstationsPerOperation w;

    public Simulator() {
        this.operationQueues = new MapLinked<>();
        products = new ArrayList<>();
        w = Repositories.getInstance().getWorkstationsPerOperation();
    }

    public Simulator(WorkstationsPerOperation e) {
        this.operationQueues = new MapLinked<>();
        products = new ArrayList<>();
        w = e;
    }

    public void run(List<Product> products) {
        createOperationQueues(products);
        processItems();
    }

    private void createOperationQueues(List<Product> products) {
        for (Product product : products) {
            this.products.add(product);
            for (Operation o : product.getOperations()) {
                if (o != null) {
                    operationQueues.newItem(o, product);
                }
            }
        }
    }

    private void processItems() {
        for (Product product : products) {
            LinkedList<Operation> operationQueue = operationQueues.getByKey(product);
            List<Operation> pendingOperations = new ArrayList<>();
            while (!operationQueue.isEmpty()) {
                Operation currentOperation = operationQueue.poll();
                System.out.println("Current operation: " + currentOperation.getId());
                Workstation bestMachine = w.findBestMachineForOperation(currentOperation);
                if (bestMachine != null && bestMachine.isAvailable()) {
                    System.out.println("The best machine: " + bestMachine.getId());
                    bestMachine.processProduct(product);
                } else {
                    pendingOperations.add(currentOperation);
                    System.out.println("No best machine found.");
                }
            }
            operationQueue.addAll(pendingOperations);
        }
    }

    public MapLinked<Operation, Product, String> getOperationQueues() {
        return operationQueues;
    }

    public List<Product> getProducts() {
        return products;
    }
}
