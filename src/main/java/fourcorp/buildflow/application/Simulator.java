package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;

import java.util.*;

public class Simulator {
    private Map<String, LinkedList<Product>> operationQueues;  // Map operation name to queue
    //private PriorityLine<Product, String> operationQueues; // 1º elemento é o que vai estar na linked list, segundo elemento é o tipo de id do elemento (int, string etc...)
    private Map<String, List<Workstation>> availableMachines;      // Map operation name to machines
    private List<Product> allProducts;  // List of all products to simulate

    public Simulator(List<Product> products, List<Workstation> machines) {
        this.allProducts = products;
        this.operationQueues = new HashMap<>();
        this.availableMachines = new HashMap<>();

        for (Workstation machine : machines) {
            availableMachines.computeIfAbsent(machine.getOperation(), k -> new ArrayList<>()).add(machine);
        }

        for (Product product : products) {
            String firstOperation = product.getOperations().get(0);
            operationQueues.computeIfAbsent(firstOperation, k -> new LinkedList<>()).add(product);
        }
    }

    // AC1
    public void initializeOperationQueues() {
        for (Product product : allProducts) {
            String firstOperation = product.getOperations().get(0);
            operationQueues.computeIfAbsent(firstOperation, k -> new LinkedList<>()).add(product);
        }
    }

    // AC2
    public void processOperations() {
        for (Map.Entry<String, LinkedList<Product>> entry : operationQueues.entrySet()) {
            String operation = entry.getKey();
            LinkedList<Product> queue = entry.getValue();

            List<Workstation> machines = availableMachines.get(operation);
            if (machines == null || machines.isEmpty()) {
                System.out.println("No available machines for operation: " + operation);
                continue;
            }

            machines.sort(Comparator.comparingInt(Workstation::getTime));

            while (!queue.isEmpty()) {
                Product product = queue.poll();
                Workstation machine = machines.get(0);  // Choose the fastest machine available

                System.out.println("Assigning product " + product.getIdItem() + " to machine " + machine.getIdMachine() + " for operation " + operation);
            }
        }
    }
}

