package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulator {
    private final ProductPriorityLine productLine;
    private final WorkstationsPerOperation workstationsPerOperation;
    private final List<Product> processedProducts;
    private final Map<Product, Double> productTimes; // USEI003
    private double totalProductionTime; // USEI003
    private final Map<String, Double> operationTimes; // USEI004
    private final Map<String, Double> workstationTimes; // USEI005
    private final Map<String, List<String>> productMachineFlows; // USEI007
    private final MachineFlowAnalyzer machineFlowAnalyzer; // Instância do MachineFlowAnalyzer

    public Simulator() {
        this.productLine = Repositories.getInstance().getProductPriorityRepository();
        this.workstationsPerOperation = Repositories.getInstance().getWorkstationsPerOperation();
        this.processedProducts = new ArrayList<>();
        this.productTimes = new HashMap<>(); // USEI003
        this.totalProductionTime = 0.0; // USEI003
        this.operationTimes = new HashMap<>(); // USEI004
        this.workstationTimes = new HashMap<>();  // USEI005
        this.productMachineFlows = new HashMap<>();  // USEI007
        this.machineFlowAnalyzer = new MachineFlowAnalyzer(); // USEI007
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

                                productMachineFlows.computeIfAbsent(product.getIdItem(), _ -> new ArrayList<>()).add(workstationId); // USEI007

                                itemsProcessed = true;

                                if (product.moveToNextOperation()) {
                                    articlesToMove.add(product);
                                    System.out.println("Moving product " + product.getIdItem() + " to the next operation: " + product.getCurrentOperation().getId());
                                } else {
                                    processedProducts.add(product);
                                    System.out.println("Product " + product.getIdItem() + " has completed all operations.");
                                }

                                break; // Sai do ‘loop’ de estações assim que o produto é processado
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

            } while (itemsProcessed || !areAllQueuesEmpty() && processedProducts.isEmpty());

            machineFlowAnalyzer.calculateMachineDependencies(productMachineFlows); // USEI007 - Calcula dependências

        } catch (Exception e) {
            System.out.println("Error during simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // USEI003 e USEI004
    public void printProductionStatistics() {
        String lineFormat = "| %-15s | %-10s |%n";
        String separator = "+-----------------+------------+";
        System.out.println("Production Time per Product:");
        System.out.println(separator);
        System.out.printf(lineFormat, "Product ID", "Time (sec)");
        System.out.println(separator);
        for (Map.Entry<Product, Double> entry : productTimes.entrySet()) {
            Product product = entry.getKey();
            Double time = entry.getValue();
            System.out.printf(lineFormat, product.getIdItem(), String.format("%.2f", time));
        }
        System.out.println(separator);
        System.out.printf("%nTotal Production Time for all products: %.2f seconds%n", totalProductionTime);
        System.out.println("\nExecution Time by Operation:");
        System.out.println(separator);
        System.out.printf(lineFormat, "Operation", "Time (sec)");
        System.out.println(separator);
        for (Map.Entry<String, Double> entry : operationTimes.entrySet()) {
            String operation = entry.getKey();
            Double time = entry.getValue();
            System.out.printf(lineFormat, operation, String.format("%.2f", time));
        }
        System.out.println(separator);
    }


    // USEI005
    public void printAnalysis() {
        String lineFormat = "| %-20s | %-17s | %-31s |%n";
        String separator = "+----------------------+-------------------+---------------------------------+";
        System.out.println(separator);
        System.out.format(lineFormat, "Workstation ID", "Total Execution", "Operation/Execution Percentage");
        System.out.println(separator);
        for (Workstation e : workstationsPerOperation.getWorkstationsAscendingByPercentage()) {
            if (e.getTotalExecutionTime() == 0) {
                System.out.format(lineFormat, e.getId(), "N/A", "It didn't operate");
            } else {
                double operationExecutionPercentage = (e.getTotalOperationTime() / e.getTotalExecutionTime()) * 100;
                System.out.format(lineFormat, e.getId(), e.getTotalExecutionTime() + " sec", String.format("%.4f%%", operationExecutionPercentage));
            }
        }
        System.out.println(separator);
    }

    public double getTotalProductionTime() {
        return totalProductionTime;
    }

    public List<Double> getProductionTimePerProduct() {
        List<Double> productionTimes = new ArrayList<>();
        for (Map.Entry<Product, Double> entry : productTimes.entrySet()) {
            productionTimes.add(entry.getValue());
        }
        return productionTimes;
    }
}
