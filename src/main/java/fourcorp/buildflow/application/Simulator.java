package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.MapLinked;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Simulator {
    private MapLinked<Operation, Product, String> operationQueues;
    private List<Product> products;
    private final WorkstationsPerOperation w;
    private final ProductPriorityLine p;

    public Simulator() {
        this.operationQueues = new MapLinked<>();
        products = new ArrayList<>();
        w = Repositories.getInstance().getWorkstationsPerOperation();
        p = Repositories.getInstance().getProductPriorityRepository();
    }

    public Simulator(WorkstationsPerOperation e, ProductPriorityLine a) {
        this.operationQueues = new MapLinked<>();
        products = new ArrayList<>();
        w = e;
        p = a;
    }

    public void runWithoutPriority() {
        createOperationQueues(p.getAllProducts());
        processItems();
    }

    public void runWithPriority() {
        System.out.println("\n\nNow it's processing the high priority products.");
        createOperationQueues(p.getProductsByPriority(PriorityOrder.HIGH));
        processItems();
        System.out.println("\n\nNow it's processing the normal priority products.");
        createOperationQueues(p.getProductsByPriority(PriorityOrder.NORMAL));
        processItems();
        System.out.println("\n\nNow it's processing the low priority products.");
        createOperationQueues(p.getProductsByPriority(PriorityOrder.LOW));
        processItems();
    }

    private void createOperationQueues(List<Product> products) {
        operationQueues.removeAll();
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
