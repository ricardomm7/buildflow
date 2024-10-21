package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.MapLinked;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.util.*;

import static fourcorp.buildflow.application.MachineFlowAnalyzer.addDependency;

public class Simulator {
    private MapLinked<Operation, Product, String> operationQueues;
    private List<Product> products;
    private final WorkstationsPerOperation w;
    private final ProductPriorityLine p;
    private final Map<String, List<String>> productMachineFlows = new HashMap<>();
    private double totalProductionTime;
    private final Map<String, Double> productTimes = new HashMap<>();

    public Simulator() {
        this.operationQueues = new MapLinked<>();
        this.products = new ArrayList<>();
        this.w = Repositories.getInstance().getWorkstationsPerOperation();
        this.p = Repositories.getInstance().getProductPriorityRepository();
        this.totalProductionTime = 0.0;
    }

    public Simulator(WorkstationsPerOperation e, ProductPriorityLine a) {
        this.operationQueues = new MapLinked<>();
        this.products = new ArrayList<>();
        this.w = e;
        this.p = a;
        this.totalProductionTime = 0.0;
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
        this.products.clear();
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
            Workstation previousWorkstation = null;

            double productTotalTime = 0.0;

            while (!operationQueue.isEmpty()) {
                Operation currentOperation = operationQueue.poll();
                System.out.println("Current operation: " + currentOperation.getId());

                Workstation bestMachine = w.findBestMachineForOperation(currentOperation);
                if (bestMachine != null && bestMachine.isAvailable()) {
                    System.out.println("The best machine: " + bestMachine.getId());

                    double operationTime = bestMachine.getTime();
                    productTotalTime += operationTime;
                    totalProductionTime += operationTime;

                    bestMachine.setAvailable(false);
                    w.increaseWaitingTimes(bestMachine.getTime());
                    bestMachine.processProduct(product);
                    bestMachine.setAvailable(true);

                    List<String> machineFlow = productMachineFlows.computeIfAbsent(product.getId(), _ -> new ArrayList<>());
                    machineFlow.add(bestMachine.getId());

                    if (previousWorkstation != null) {
                        addDependency(previousWorkstation.getId(), bestMachine.getId());
                    }

                    previousWorkstation = bestMachine;
                } else {
                    pendingOperations.add(currentOperation);
                    System.out.println("No best machine found.");
                }
            }
            operationQueue.addAll(pendingOperations);

            productTimes.put(product.getId(), productTotalTime);
        }
    }

    public void printTotalProductionTime() {
        System.out.println("\nTotal Production Time: " + getTotalProductionTime() + " minutes");
    }

    public void printProductionTimePerProduct() {
        System.out.println("\nProduction Time per Product:");
        for (Map.Entry<String, Double> entry : productTimes.entrySet()) {
            System.out.println("Product ID: " + entry.getKey() + ", Production Time: " + entry.getValue() + " minutes");
        }
    }

    public void printMachineDependencies() {
        System.out.println("\nDependencies between machines:");
        MachineFlowAnalyzer.printMachineDependencies();
    }

    public MapLinked<Operation, Product, String> getOperationQueues() {
        return operationQueues;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Double> getProductionTimePerProduct() {
        List<Double> productionTimes = new ArrayList<>();
        for (Map.Entry<String, Double> entry : productTimes.entrySet()) {
            productionTimes.add(entry.getValue());
        }
        return productionTimes;
    }

    public double getTotalProductionTime() {
        return totalProductionTime;
    }

    public void printAnalysis() {
        for (Workstation e : w.getWorkstationsAscendingByPercentage()) {
            if (e.getTotalOperationTime() == 0) {
                System.out.println("Workstation ID: " + e.getId() + " | It didn't operate in the last simulation.");
            } else {
                System.out.println("Workstation ID: " + e.getId() + " | Total time in execution: " + e.getTotalExecutionTime() + "min" + " | Operation and execution relationship: " + String.format("%.4f", (e.getTotalOperationTime() / e.getTotalExecutionTime()) * 100) + "%");
            }
        }
    }
}
