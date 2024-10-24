package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.util.*;
import java.util.stream.Collectors;

public class Simulator {
    private ProductPriorityLine productLine;
    private WorkstationsPerOperation workstationsPerOperation;
    private List<Product> processedProducts;
    private Map<Product, Double> productTimes; // USEI003
    private double totalProductionTime; // USEI003
    private Map<String, Double> operationTimes; // USEI004
    private Map<String, Double> workstationTimes; // USEI005


    public Simulator() {
        this.productLine = Repositories.getInstance().getProductPriorityRepository();
        this.workstationsPerOperation = Repositories.getInstance().getWorkstationsPerOperation();
        this.processedProducts = new ArrayList<>();
        this.productTimes = new HashMap<>(); // USEI003
        this.totalProductionTime = 0.0; // USEI003
        this.operationTimes = new HashMap<>(); // USEI004
        this.workstationTimes = new HashMap<>();  // USEI005

    }

    public boolean areAllQueuesEmpty() {
        return productLine.getAllProducts().isEmpty();
    }

    public void runWithPriority(boolean b) {
        System.out.println("\n\n>>> NOW IT'S PROCESSING THE HIGH PRIORITY PRODUCTS\n\n");
        runSimulation(productLine.getProductsByPriority(PriorityOrder.HIGH), b);
        System.out.println("\n\n>>> NOW IT'S PROCESSING THE NORMAL PRIORITY PRODUCTS\n\n");
        runSimulation(productLine.getProductsByPriority(PriorityOrder.NORMAL), b);
        System.out.println("\n\n>>> NOW IT'S PROCESSING THE LOW PRIORITY PRODUCTS\n\n");
        runSimulation(productLine.getProductsByPriority(PriorityOrder.LOW), b);
    }

    public void runWithoutPriority(boolean b) {
        processedProducts.clear();
        for (Product a : productLine.getAllProducts()) {
            a.setCurrentOperationIndex(0);
        }
        runSimulation(productLine.getAllProducts(), b);
    }

    private void runSimulation(List<Product> products, boolean boo) {
        boolean itemsProcessed;
        try {
            do {
                itemsProcessed = false;
                List<Product> articlesToMove = new ArrayList<>();

                for (Product product : products) {
                    if (processedProducts.contains(product)) {
                        continue;
                    }

                    Operation currentOperation = product.getCurrentOperation();

                    if (currentOperation != null) {
                        List<Workstation> availableWorkstations = workstationsPerOperation.getWorkstationsByOperation(currentOperation, boo);

                        for (Workstation workstation : availableWorkstations) {
                            if (workstation.isAvailable()) {
                                workstation.processProduct(product);
                                double operationTime = workstation.getTime();

                                productTimes.merge(product, operationTime, Double::sum); //USEI03
                                totalProductionTime += operationTime; //USEI03

                                String operationName = currentOperation.getId(); // USEI04
                                operationTimes.merge(operationName, operationTime, Double::sum); //USEI04

                                String workstationId = workstation.getId(); // USEI05
                                workstationTimes.merge(workstationId, operationTime, Double::sum); // USEI05

                                itemsProcessed = true;

                                if (product.moveToNextOperation()) {
                                    articlesToMove.add(product);
                                    System.out.println("Moving product " + product.getIdItem() + " to the next operation: " + product.getCurrentOperation().getId());
                                } else {
                                    processedProducts.add(product);
                                    System.out.println("Product " + product.getIdItem() + " has completed all operations.");
                                }

                                break; // Sai do loop de estações assim que o produto é processado
                            }
                        }
                    }
                }

                for (Product product : articlesToMove) {
                    Operation nextOperation = product.getCurrentOperation();
                    if (nextOperation != null) {
                        System.out.println("Adding product " + product.getIdItem() + " to queue for operation: " + nextOperation.getId());
                    }
                }

            } while (itemsProcessed || !areAllQueuesEmpty() && processedProducts.isEmpty());  // Corrigida a condição de parada

        } catch (Exception e) {
            System.out.println("Error during simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // USEI003 e USEI004
    public void printProductionStatistics() {
        System.out.println("\n=== Production Time Statistics ===");

        System.out.println("Production Time per Product:");
        for (Map.Entry<Product, Double> entry : productTimes.entrySet()) {
            Product product = entry.getKey();
            Double time = entry.getValue();
            System.out.printf("Product %s: %.2f minutes\n", product.getIdItem(), time);
        }

        System.out.printf("\nTotal Production Time for all products: %.2f minutes\n", totalProductionTime);

        System.out.println("\nExecution Time by Operation:");
        for (Map.Entry<String, Double> entry : operationTimes.entrySet()) {
            String operation = entry.getKey();
            Double time = entry.getValue();
            System.out.printf("Operation %s: %.2f minutes\n", operation, time);
        }
    }

    // USEI005
    public void printWorkstationStatistics() {
        System.out.println("\n=== Workstation Time Statistics ===");

        double totalWorkstationTime = workstationTimes.values().stream().mapToDouble(Double::doubleValue).sum();

        List<AbstractMap.SimpleEntry<String, double[]>> sortedWorkstations = workstationTimes.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(
                        entry.getKey(),
                        new double[]{entry.getValue(), (entry.getValue() / totalWorkstationTime) * 100, (entry.getValue() / totalProductionTime) * 100}
                ))
                .sorted(Comparator.comparingDouble(entry -> entry.getValue()[2]))
                .collect(Collectors.toList()).reversed();

        System.out.println("Workstation ID | Total Time (minutes) | % of Workstation Time | % of Total Production Time");
        for (Map.Entry<String, double[]> entry : sortedWorkstations) {
            String workstationId = entry.getKey();
            double[] stats = entry.getValue();
            System.out.printf("%s          | %.2f               | %.2f%%                  | %.2f%%\n",
                    workstationId, stats[0], stats[1], stats[2]);
        }
    }
}

