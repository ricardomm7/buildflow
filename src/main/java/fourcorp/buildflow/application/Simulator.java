package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Simulator class manages the simulation of a production line, handling product flow through various operations
 * and workstations while tracking statistics such as production times, waiting times, and flow dependencies.
 */
public class Simulator {
    private final ProductPriorityLine productLine;
    private final WorkstationsPerOperation workstationsPerOperation;
    private final List<Product> processedProducts;
    private final Map<Product, Double> productTimes; // USEI003
    private double totalProductionTime; // USEI003
    private final Map<String, Double> operationTimes; // USEI004
    private final Map<String, Integer> operationCounts; // USEI006
    private final MachineFlowAnalyzer machineFlowAnalyzer; // USEI007

    private final Map<String, Queue<Product>> waitingQueue; // Fila de espera para operações
    private final Map<Product, Double> waitingTimes; // Tempo de espera para produtos
    private HashMap<String, Double> operationWaitingTimes;
    private HashMap<String, Integer> countWaiting;

    public Simulator() {
        this.productLine = Repositories.getInstance().getProductPriorityRepository();
        this.workstationsPerOperation = Repositories.getInstance().getWorkstationsPerOperation();
        this.processedProducts = new ArrayList<>();
        this.productTimes = new HashMap<>(); // USEI003
        this.totalProductionTime = 0.0; // USEI003
        this.operationTimes = new HashMap<>(); // USEI004
        this.operationCounts = new HashMap<>(); // USEI006
        this.machineFlowAnalyzer = new MachineFlowAnalyzer(); // USEI007
        this.waitingQueue = new HashMap<>(); // Para organizar produtos por operação na espera
        this.waitingTimes = new HashMap<>(); // Tempo total de espera de cada produto
        this.operationWaitingTimes = new HashMap<>();
        this.countWaiting = new HashMap<>();
    }

    /**
     * Constructor with specific repositories for workstations and product priorities.
     *
     * @param a the WorkstationsPerOperation repository
     * @param b the ProductPriorityLine repository
     */
    public Simulator(WorkstationsPerOperation a, ProductPriorityLine b) {
        this.productLine = b;
        this.workstationsPerOperation = a;
        this.processedProducts = new ArrayList<>();
        this.productTimes = new HashMap<>(); // USEI003
        this.totalProductionTime = 0.0; // USEI003
        this.operationCounts = new HashMap<>(); // USEI006
        this.operationTimes = new HashMap<>(); // USEI004
        this.machineFlowAnalyzer = new MachineFlowAnalyzer(); // USEI007
        this.waitingQueue = new HashMap<>(); // Para organizar produtos por operação na espera
        this.waitingTimes = new HashMap<>(); // Tempo total de espera de cada produto
        this.operationWaitingTimes = new HashMap<>();
        this.countWaiting = new HashMap<>();
    }

    /**
     * Runs the simulation considering product priorities, processing high, normal, and low priority products in order.
     * The complexity of USEI08 is O(n)
     *
     * @param b a boolean flag for workstation availability filtering
     */
    public void runWithPriority(boolean b) {
        resetSimulation(); // O(n)
        if (!productLine.getProductsByPriority(PriorityOrder.HIGH).isEmpty()) {
            processedProducts.clear(); // O(1)
            returnToFirstOp(productLine.getAllProducts()); // O(n)
            System.out.println("\n\n>>> NOW IT'S PROCESSING THE HIGH PRIORITY PRODUCTS\n\n");
            runSimulation(productLine.getProductsByPriority(PriorityOrder.HIGH), b);
        }

        if (!productLine.getProductsByPriority(PriorityOrder.NORMAL).isEmpty()) {
            processedProducts.clear(); // O(1)
            returnToFirstOp(productLine.getAllProducts()); // O(n)
            System.out.println("\n\n>>> NOW IT'S PROCESSING THE NORMAL PRIORITY PRODUCTS\n\n");
            runSimulation(productLine.getProductsByPriority(PriorityOrder.NORMAL), b);
        }

        if (!productLine.getProductsByPriority(PriorityOrder.LOW).isEmpty()) {
            processedProducts.clear(); // O(1)
            returnToFirstOp(productLine.getAllProducts()); // O(n)
            System.out.println("\n\n>>> NOW IT'S PROCESSING THE LOW PRIORITY PRODUCTS\n\n");
            runSimulation(productLine.getProductsByPriority(PriorityOrder.LOW), b);
        }
    }

    /**
     * Runs the simulation without considering product priorities.
     * The complexity of USEI08 is O(n).
     *
     * @param b a boolean flag for workstation availability filtering
     */
    public void runWithoutPriority(boolean b) {
        resetSimulation();
        returnToFirstOp(productLine.getAllProducts());
        runSimulation(productLine.getAllProducts(), b);
    }

    /**
     * Executes the main simulation loop for the given list of products.
     * <p>The complexity is O(n^6).</p>
     *
     * @param products the list of products to be processed
     * @param boo      a boolean flag for workstation availability filtering
     */
    public void runSimulation(List<Product> products, boolean boo) {
        boolean itemsProcessed;

        try {
            do { // O(n)
                itemsProcessed = false;
                List<Product> articlesToMove = new ArrayList<>();

                for (Product product : products) { // O(n) * O(n) = O(n^2)
                    if (processedProducts.contains(product)) {
                        continue;
                    }

                    Operation currentOperation = product.getCurrentOperation(); // O(n^2) * O(1) = O(n^2)

                    if (currentOperation != null) {
                        List<Workstation> availableWorkstations = workstationsPerOperation.getWorkstationsByOperation(currentOperation, boo); // O(n^2) * O(n^2) = O(n^4)
                        boolean operationStarted = false;

                        for (Workstation workstation : availableWorkstations) { // O(n^4) * O(n) = O(n^5)
                            if (workstation.isAvailable()) {
                                operationStarted = true;
                                workstation.processProduct(product); // O(n^5) * O(1) = O(n^5)
                                double operationTime = workstation.getTime();

                                String operationName = currentOperation.getId();
                                operationTimes.merge(operationName, operationTime, Double::sum);

                                operationCounts.merge(operationName, 1, Integer::sum);

                                machineFlowAnalyzer.addFlow(workstation, product);

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

                        // Caso nenhuma estação esteja disponível, adiciona à fila de espera
                        if (!operationStarted) {
                            addToWaitingQueue(product, currentOperation);
                            itemsProcessed = true;
                        }
                    }
                }

                if (waitingQueue.isEmpty()) {
                    System.out.println("The waiting queue is empty.");
                    return;
                }

                /*metodo para ver a waiting queue
                System.out.println("Waiting queue contents:");
                for (Map.Entry<String, Queue<Product>> entry : waitingQueue.entrySet()) {
                    String operation = entry.getKey();
                    Queue<Product> products2 = entry.getValue();

                    for (Product prd2 : products2) {
                        System.out.println("Product: " + prd2.getIdItem());
                        System.out.println("Operation ID: " + operation);
                    }
                }*/

                // Processa a fila de espera, se houver máquinas disponíveis para as operações pendentes
                processWaitingQueue(); // O(n) * O(n^5) = O(n^)

                for (Product product : articlesToMove) {
                    Operation nextOperation = product.getCurrentOperation();
                    if (nextOperation != null) {
                        System.out.println("Adding product " + product.getIdItem() + " to queue for operation: " + nextOperation.getId());
                    }
                }

                // erro ele não sai do ciclo while
            } while (itemsProcessed || !areProductsQueueEmpty() && processedProducts.isEmpty());
        } catch (Exception e) {
            System.out.println("Error during simulation: " + e.getMessage());
        }
    }


    /**
     * Adds a product to the waiting queue for a specified operation.
     *
     * @param product   the product to add to the waiting queue
     * @param operation the operation for which the product is waiting
     */
    private void addToWaitingQueue(Product product, Operation operation) {
        operation = calculateBeginingWaiting(operation);
        waitingQueue.computeIfAbsent(operation.getId(), k -> new LinkedList<>()).add(product);
        waitingTimes.merge(product, 0.0, Double::sum);
    }

    /**
     * Checks whether all products have been processed or are waiting.
     *
     * @return true if there are no products left in the queue, false otherwise
     */
    public boolean areProductsQueueEmpty() {
        return productLine.getAllProducts().isEmpty();
    }

    /**
     * Processes the waiting queue, attempting to move products to available workstations.
     * <p>The complexity is O(n^5).</p>
     */
    private void processWaitingQueue() {
        for (Map.Entry<String, Queue<Product>> entry : waitingQueue.entrySet()) { // O(n)
            String operationId = entry.getKey();
            Queue<Product> queue = entry.getValue();

            while (!queue.isEmpty()) {  // O(n) * O(n) = O(n^2)
                Product product = queue.peek();
                Operation currentOperation = product.getCurrentOperation();

                if (currentOperation != null && currentOperation.getId().equals(operationId)) {
                    List<Workstation> availableWorkstations = workstationsPerOperation.getWorkstationsByOperation(currentOperation, false); // O(n^2) * O(n^2) = O(n^4)

                    for (Workstation workstation : availableWorkstations) { // O(n^4) * O(n) = O(n^5)
                        if (workstation.isAvailable()) {
                            calculateFinishWaiting(currentOperation);
                            queue.poll();
                            workstation.processProduct(product);
                            double operationTime = workstation.getTime();

                            productTimes.merge(product, operationTime, Double::sum);
                            totalProductionTime += operationTime;
                            operationTimes.merge(operationId, operationTime, Double::sum);

                            operationCounts.merge(currentOperation.getId(), 1, Integer::sum);

                            machineFlowAnalyzer.addFlow(workstation, product);


                            if (product.moveToNextOperation()) {
                                System.out.println("Moving product " + product.getIdItem() + " to the next operation: " + product.getCurrentOperation().getId());
                            } else {
                                processedProducts.add(product);
                                System.out.println("Product " + product.getIdItem() + " has completed all operations.");
                            }
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Resets each product to its initial operation.
     *
     * @param f the list of products to reset
     */
    private void returnToFirstOp(List<Product> f) {
        for (Product a : f) { // O(n)
            a.setCurrentOperationIndex(0); // O(n) * O(1) = O(n)
        }
    }

    // USEI003 e USEI004

    /**
     * Prints statistics on production time for each product and total production time.
     * The complexity of USEI03 is O(1).
     * The complexity of USEI04 is O(n).
     */
    public void printProductionStatistics() {
        System.out.println();
        String lineFormat = "| %-15s | %-10s |%n";
        String separator = "+-----------------+------------+";

        System.out.println("Production Time per Product:");
        System.out.println(separator);
        System.out.printf(lineFormat, "Product ID", "Time (sec)");
        System.out.println(separator);

        Map<String, Double> accumulatedProductTimes = new HashMap<>();
        for (Map.Entry<Product, Double> entry : productTimes.entrySet()) {
            String productId = entry.getKey().getIdItem();
            double time = entry.getValue();
            accumulatedProductTimes.put(productId, accumulatedProductTimes.getOrDefault(productId, 0.0) + time);
        }

        for (Map.Entry<String, Double> entry : accumulatedProductTimes.entrySet()) {
            System.out.printf(lineFormat, entry.getKey(), String.format("%.2f", entry.getValue()));
        }
        System.out.println(separator);
        System.out.printf("%nTotal Production Time for all products: %.2f seconds%n", totalProductionTime); // O(1)
        System.out.println("\nExecution Time by Operation:");
        System.out.println(separator);
        System.out.printf(lineFormat, "Operation", "Time (sec)");
        System.out.println(separator);
        for (Map.Entry<String, Double> entry : operationTimes.entrySet()) { // O(n)
            String operation = entry.getKey(); // O(n) * O(1) = O(n)
            Double time = entry.getValue(); // O(n) * O(1) = O(n)
            System.out.printf(lineFormat, operation, String.format("%.2f", time)); // O(n) * O(1) = O(n)
        }
        System.out.println(separator);
    }

    // USEI005

    /**
     * Prints analysis on the total operating time and its importance in the production line.
     * The complexity of USEI05 is O(nlog(n)).
     */
    public void printAnalysis() {
        System.out.println();
        System.out.println("Total operating time and its respective importance in production:");
        String lineFormat = "| %-20s | %-17s | %-31s |%n";
        String separator = "+----------------------+-------------------+---------------------------------+";
        System.out.println(separator);
        System.out.format(lineFormat, "Workstation ID", "Total Operation", "Operation/Execution Percentage");
        System.out.println(separator);
        for (Workstation e : workstationsPerOperation.getWorkstationsAscendingByPercentage(totalProductionTime)) { // O(nlog(n))
            if (e.getTotalOperationTime() == 0) {
                System.out.format(lineFormat, e.getId(), "N/A", "It didn't operate");
            } else {
                double operationExecutionPercentage = (e.getTotalOperationTime() / totalProductionTime) * 100;
                System.out.format(lineFormat, e.getId(), e.getTotalOperationTime() + " sec", String.format("%.4f%%", operationExecutionPercentage));
            }
        }
        System.out.println(separator);
    }

    /**
     * Resets the simulation by clearing all data and resetting workstations.
     */
    public void resetSimulation() {
        for (Workstation a : workstationsPerOperation.getAllWorkstations()) {
            a.setTotalOperationTime(0);
        }
        productTimes.clear(); // O(1)
        totalProductionTime = 0.0; // O(1)
        operationTimes.clear(); // O(1)
        waitingQueue.clear(); // O(1)
        waitingTimes.clear(); // O(1)
        operationWaitingTimes.clear(); // O(1)
        countWaiting.clear(); // O(1)
        processedProducts.clear(); // O(1)
        MachineFlowAnalyzer.reset(); // O(1)
        operationCounts.clear(); // O(1)
    }

    /**
     * Calculates the beginning waiting time for an operation.
     *
     * @param opr the operation for which to calculate the waiting time
     * @return the modified operation with updated waiting time
     */
    public Operation calculateBeginingWaiting(Operation opr) {
        opr.setTime();

        return opr;
    }

    /**
     * Calculates the finishing waiting time for an operation.
     *
     * @param opr the operation for which to calculate the waiting time
     */
    public void calculateFinishWaiting(Operation opr) {
        String name = opr.getId();
        double time = (double) System.currentTimeMillis();
        double finalTime = (time - opr.getTime()) + operationWaitingTimes.getOrDefault(name, 0.0);
        operationWaitingTimes.put(name, finalTime);
        int counter = countWaiting.getOrDefault(name, 0) + 1;
        countWaiting.put(name, counter);
    }

    /**
     * Prints a report on average operating times, waiting times, and total waiting times for operations.
     * The complexity of the USEI06 is O(n).
     */
    public void printAverageTimesReport() {
        System.out.println();
        System.out.println("Table showing average operating time, waiting time and total waiting time:");
        String lineFormat = "| %-15s | %-30s | %-30s | %-25s | %-6s |%n";
        String separator = "+-----------------+--------------------------------+--------------------------------+---------------------------+--------+";
        if (operationWaitingTimes.isEmpty()) {
            System.out.println("--- Error --- No Waiting Times recorded.");
        } else if (countWaiting.isEmpty()) {
            System.out.println("--- Error --- Waiting Operation counts missing.");
        } else if (operationTimes.isEmpty()) {
            System.out.println("--- Error --- No Operation Times recorded.");
        } else if (operationCounts.isEmpty()) {
            System.out.println("--- Error --- Operation counts missing.");
        } else {
            System.out.println(separator);
            System.out.printf(lineFormat, "Operation", "Average Operation Time (sec)", "Average Waiting Time (sec)", "Total Waiting Time (sec)", "No. Op");
            System.out.println(separator);
            for (String name : operationWaitingTimes.keySet()) { // O(n)
                double avgWaitingTime = operationWaitingTimes.getOrDefault(name, 0.0) / countWaiting.getOrDefault(name, 1); // O(n) * O(1) = O(n)
                avgWaitingTime = avgWaitingTime * 0.001; // O(n) * O(1) = O(n)
                BigDecimal roundedWaitingTime = new BigDecimal(avgWaitingTime).setScale(2, RoundingMode.HALF_UP); // O(n) * O(1) = O(n)

                double totalWaitingTime = operationWaitingTimes.getOrDefault(name, 0.0); // O(n) * O(1) = O(n)
                totalWaitingTime = totalWaitingTime * 0.001; // O(n) * O(1) = O(n)
                BigDecimal totalWaiting = new BigDecimal(totalWaitingTime).setScale(2, RoundingMode.HALF_UP); // O(n) * O(1) = O(n)

                double avgOperationTime = operationTimes.getOrDefault(name, 0.0) / operationCounts.getOrDefault(name, 1); // O(n) * O(1) = O(n)
                BigDecimal roundedOperationTime = new BigDecimal(avgOperationTime).setScale(2, RoundingMode.UP); // O(n) * O(1) = O(n)
// adicionar aqui uslp7

                System.out.printf(lineFormat, name, roundedOperationTime, roundedWaitingTime, totalWaiting, operationCounts.getOrDefault(name, 1)); // O(n) * O(1) = O(n)
            }
            System.out.println(separator);
        }
    }

    /**
     * Retrieves the total production time for all products.
     *
     * @return the total production time
     */
    public double getTotalProductionTime() {
        return totalProductionTime;
    }

    /**
     * Gets the total execution time for a specific operation.
     * Implementation for USEI04.
     *
     * @param op the operation identifier
     * @return the total execution time for the operation
     */
    public double getOperationExecutionTime(String op) {
        return operationTimes.getOrDefault(op, 0.0);
    }

    /**
     * Gets a map of workstation IDs to their total operation times.
     * Implementation for USEI05.
     *
     * @return a map containing workstation IDs and their corresponding total operation times
     */
    public Map<String, Double> getWorkstationOperationTimes() {
        Map<String, Double> workstationTimes = new HashMap<>();
        for (Workstation workstation : workstationsPerOperation.getAllWorkstations()) {
            workstationTimes.put(workstation.getId(), workstation.getTotalOperationTime());
        }
        return workstationTimes;
    }

    /**
     * Gets the workstations per operation mapping.
     * Implementation for USEI05.
     *
     * @return the WorkstationsPerOperation object containing the mapping of operations to workstations
     */
    public WorkstationsPerOperation getWorkstationsPerOperation() {
        return workstationsPerOperation;
    }

    /**
     * Calculates the average waiting time for a specific operation.
     * Implementation for USEI06.
     *
     * @param op the operation identifier
     * @return the average waiting time for the operation
     */
    public double getAverageWaitingTime(String op) {
        return operationWaitingTimes.getOrDefault(op, 0.0) / countWaiting.getOrDefault(op, 1);
    }

    /**
     * Calculates the average execution time for a specific operation.
     * Implementation for USEI06.
     *
     * @param op the operation identifier
     * @return the average execution time for the operation
     */
    public double getAverageExecutionTime(String op) {
        return operationTimes.getOrDefault(op, 0.0) / operationCounts.getOrDefault(op, 1);
    }

    /**
     * Gets the map containing operation waiting times.
     *
     * @return a map containing operation identifiers and their corresponding waiting times
     */
    public Map<String, Double> getOperationWaitingTimes() {
        return operationWaitingTimes;
    }

    /**
     * Gets the map containing waiting counts per operation.
     *
     * @return a map containing operation identifiers and their corresponding waiting counts
     */
    public Map<String, Integer> getCountWaiting() {
        return countWaiting;
    }

    /**
     * Gets the map containing operation execution times.
     *
     * @return a map containing operation identifiers and their corresponding execution times
     */
    public Map<String, Double> getOperationTimes() {
        return operationTimes;
    }

    /**
     * Gets the map containing operation execution counts.
     *
     * @return a map containing operation identifiers and their corresponding execution counts
     */
    public Map<String, Integer> getOperationCounts() {
        return operationCounts;
    }
}