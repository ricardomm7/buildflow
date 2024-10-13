/*package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.Repositories;

import java.util.*;


public class Simulator {
    private Map<String, Queue<Product>> operationQueues;
    private Repositories repositories;


    public Simulator() {
        this.operationQueues = new HashMap<>();
        this.repositories = Repositories.getInstance();
    }

    // AC1: Create preliminary queues for each operation

    public void createOperationQueues(List<Product> products) {
        for (Product product : products) {
            String nextOperation = product.getNextOperation();
            if (nextOperation != null) {
                operationQueues
                        .computeIfAbsent(nextOperation, k -> new LinkedList<>())
                        .add(product);
            }
        }
    }

    // AC2: Assign items to machines based on availability and processing time

    public void processItems() {
        for (Map.Entry<String, Queue<Product>> entry : operationQueues.entrySet()) {
            String operationName = entry.getKey();
            Queue<Product> productQueue = entry.getValue();

            // List to keep track of products that couldn't be processed in this cycle
            List<Product> pendingProducts = new ArrayList<>();

            while (!productQueue.isEmpty()) {
                Product product = productQueue.poll();

                // Find the fastest available machine for the current operation
                Workstation bestMachine = findBestMachineForOperation(operationName);

                if (bestMachine != null && bestMachine.isAvailable()) {
                    // Process the product with the machine
                    bestMachine.processProduct(product);
                    // Move to the next operation for the product
                    product.moveToNextOperation();
                    // Add the product to the queue for the next operation if there is one
                    String nextOperation = product.getNextOperation();
                    if (nextOperation != null) {
                        operationQueues
                                .computeIfAbsent(nextOperation, k -> new LinkedList<>())
                                .add(product);
                    }
                } else {
                    // No machine available, add the product to the pending list
                    pendingProducts.add(product);
                }
            }
            // Re-queue the pending products at the end
            productQueue.addAll(pendingProducts);
        }
    }



    private Workstation findBestMachineForOperation(String operationName) {
        List<Workstation> workstations = repositories.getWorkstationsPerOperation()
                .getProductsByPriority(new Operation(operationName));

        // Use a priority queue to find the fastest available machine
        PriorityQueue<Workstation> availableMachines = new PriorityQueue<>(
                Comparator.comparingDouble(Workstation::getTime)
        );

        for (Workstation machine : workstations) {
            if (machine.isAvailable()) {
                availableMachines.add(machine);
            }
        }
        return availableMachines.isEmpty() ? null : availableMachines.poll();
    }
} */