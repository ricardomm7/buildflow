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

public class Simulator {
    private final ProductPriorityLine productLine;
    private final WorkstationsPerOperation workstationsPerOperation;
    private final List<Product> processedProducts;
    private final Map<Product, Double> productTimes; // USEI003
    private double totalProductionTime; // USEI003
    private final Map<String, Double> operationTimes; // USEI004
    private final Map<String, Double> workstationTimes; // USEI005
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
        this.workstationTimes = new HashMap<>();  // USEI005
        this.operationCounts = new HashMap<>(); // USEI006
        this.machineFlowAnalyzer = new MachineFlowAnalyzer(); // USEI007

        this.countWaiting = new HashMap<>();
        this.operationWaitingTimes = new HashMap<>();

        this.waitingQueue = new HashMap<>(); // Para organizar produtos por operação na espera
        this.waitingTimes = new HashMap<>(); // Tempo total de espera de cada produto
    }

    public Simulator(WorkstationsPerOperation a, ProductPriorityLine b) {
        this.productLine = b;
        this.workstationsPerOperation = a;
        this.processedProducts = new ArrayList<>();
        this.productTimes = new HashMap<>(); // USEI003
        this.totalProductionTime = 0.0; // USEI003
        this.operationCounts = new HashMap<>(); // USEI006
        this.operationTimes = new HashMap<>(); // USEI004
        this.workstationTimes = new HashMap<>();  // USEI005
        this.machineFlowAnalyzer = new MachineFlowAnalyzer(); // USEI007

        this.waitingQueue = new HashMap<>(); // Para organizar produtos por operação na espera
        this.waitingTimes = new HashMap<>(); // Tempo total de espera de cada produto
    }

    public void runWithPriority(boolean b) {
        resetSimulation();
        if (!productLine.getProductsByPriority(PriorityOrder.HIGH).isEmpty()) {
            processedProducts.clear();
            returnToFirstOp(productLine.getAllProducts());
            System.out.println("\n\n>>> NOW IT'S PROCESSING THE HIGH PRIORITY PRODUCTS\n\n");
            runSimulation(productLine.getProductsByPriority(PriorityOrder.HIGH), b);
        }

        if (!productLine.getProductsByPriority(PriorityOrder.NORMAL).isEmpty()) {
            processedProducts.clear();
            returnToFirstOp(productLine.getAllProducts());
            System.out.println("\n\n>>> NOW IT'S PROCESSING THE NORMAL PRIORITY PRODUCTS\n\n");
            runSimulation(productLine.getProductsByPriority(PriorityOrder.NORMAL), b);
        }

        if (!productLine.getProductsByPriority(PriorityOrder.LOW).isEmpty()) {
            processedProducts.clear();
            returnToFirstOp(productLine.getAllProducts());
            System.out.println("\n\n>>> NOW IT'S PROCESSING THE LOW PRIORITY PRODUCTS\n\n");
            runSimulation(productLine.getProductsByPriority(PriorityOrder.LOW), b);
        }
    }

    public void runWithoutPriority(boolean b) {
        resetSimulation();
        returnToFirstOp(productLine.getAllProducts());
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

                        boolean operationStarted = false;
                        for (Workstation workstation : availableWorkstations) {
                            if (workstation.isAvailable()) {
                                operationStarted = true;
                                workstation.processProduct(product);
                                double operationTime = workstation.getTime();


                                productTimes.merge(product, operationTime, Double::sum); //USEI03
                                totalProductionTime += operationTime; //USEI03

                                String operationName = currentOperation.getId(); // USEI04
                                operationTimes.merge(operationName, operationTime, Double::sum); //USEI04

                                String workstationId = workstation.getId(); // USEI05
                                workstationTimes.merge(workstationId, operationTime, Double::sum); // USEI05

                                operationCounts.merge(operationName, 1, Integer::sum); // USEI06


                                machineFlowAnalyzer.addFlow(workstation, product); // USEI007

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
                // Processa a fila de espera, se houver máquinas disponíveis para as operações pendentes
                processWaitingQueue();

                for (Product product : articlesToMove) {
                    Operation nextOperation = product.getCurrentOperation();
                    if (nextOperation != null) {
                        System.out.println("Adding product " + product.getIdItem() + " to queue for operation: " + nextOperation.getId());
                    }
                }

            } while (itemsProcessed || !areProductsQueueEmpty() && processedProducts.isEmpty());

        } catch (Exception e) {
            System.out.println("Error during simulation: " + e.getMessage());
        }
    }

    // Adiciona produto à fila de espera para uma operação
    private void addToWaitingQueue(Product product, Operation operation) {
        operation = calculateBeginingWaiting(operation);
        waitingQueue.computeIfAbsent(operation.getId(), k -> new LinkedList<>()).add(product);
        waitingTimes.merge(product, 0.0, Double::sum); // Inicializa o tempo de espera, se necessário
    }

    public boolean areProductsQueueEmpty() {
        return productLine.getAllProducts().isEmpty();
    }

    // Processa a fila de espera quando workstations ficam disponíveis
    private void processWaitingQueue() {
        for (Map.Entry<String, Queue<Product>> entry : waitingQueue.entrySet()) {
            String operationId = entry.getKey();
            Queue<Product> queue = entry.getValue();

            while (!queue.isEmpty()) {
                Product product = queue.peek();
                Operation currentOperation = product.getCurrentOperation();

                if (currentOperation != null && currentOperation.getId().equals(operationId)) {
                    List<Workstation> availableWorkstations = workstationsPerOperation.getWorkstationsByOperation(currentOperation, false);

                    for (Workstation workstation : availableWorkstations) {
                        if (workstation.isAvailable()) {
                            calculateFinishWaiting(currentOperation);
                            queue.poll(); // Remove o produto da fila
                            workstation.processProduct(product);
                            double operationTime = workstation.getTime();

                            productTimes.merge(product, operationTime, Double::sum);
                            totalProductionTime += operationTime;
                            operationTimes.merge(operationId, operationTime, Double::sum);

                            operationCounts.merge(currentOperation.getId(), 1, Integer::sum); // USEI06

                            machineFlowAnalyzer.addFlow(workstation, product); // USEI007


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


    private void returnToFirstOp(List<Product> f) {
        for (Product a : f) {
            a.setCurrentOperationIndex(0);
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
        System.out.format(lineFormat, "Workstation ID", "Total Operation", "Operation/Execution Percentage");
        System.out.println(separator);
        for (Workstation e : workstationsPerOperation.getWorkstationsAscendingByPercentage(totalProductionTime)) {
            if (e.getTotalOperationTime() == 0) {
                System.out.format(lineFormat, e.getId(), "N/A", "It didn't operate");
            } else {
                double operationExecutionPercentage = (e.getTotalOperationTime() / totalProductionTime) * 100;
                System.out.format(lineFormat, e.getId(), e.getTotalOperationTime() + " sec", String.format("%.4f%%", operationExecutionPercentage));
            }
        }
        System.out.println(separator);
    }

    public void resetSimulation() {
        for (Workstation a : workstationsPerOperation.getAllWorkstations()) {
            a.setTotalOperationTime(0);
        }
        productTimes.clear();
        totalProductionTime = 0.0;
        operationTimes.clear();
        workstationTimes.clear();
        waitingQueue.clear();
        waitingTimes.clear();
        processedProducts.clear();
        machineFlowAnalyzer.reset();

    }

    public Operation calculateBeginingWaiting(Operation opr) {
        opr.setTime();
        return opr;
    }

    public void calculateFinishWaiting(Operation opr) {
        String name = opr.getId();
        double time = (double) System.currentTimeMillis();
        double finalTime = (time - opr.getTime()) + operationWaitingTimes.getOrDefault(name, 0.0);
        operationWaitingTimes.put(name, finalTime);
        int counter = countWaiting.getOrDefault(name, 0) + 1;
        countWaiting.put(name, counter);
    }



    public void printAverageWaitingTimes (){
        System.out.println("______________Average_Waiting_Times_Report______________");

        if(operationWaitingTimes.isEmpty())
            System.out.println("---Error---You dont have any Waiting Time saved");
        else if (countWaiting.isEmpty())
            System.out.println("---Error---You didn't save how many times the Operation in Waiting was operated");
        else{
            for(String name : operationWaitingTimes.keySet()){
                double time = (double) operationWaitingTimes.getOrDefault(name, 0.0) / countWaiting.getOrDefault(name,1);
                time = time * 0.001;
                BigDecimal roundedValue = new BigDecimal(time).setScale(2,RoundingMode.HALF_UP);
                System.out.println("Operation "+name+" = "+roundedValue);

            }
        }

    }

    public void printAverageTimePerOperation(){
        System.out.println("______________Average_Time_Per_Operation_Report______________");

        if(operationTimes.isEmpty())
            System.out.println("---Error---You dont have any Operation Time saved");
        else if (operationCounts.isEmpty())
            System.out.println("---Error---You didn't save how many times the Operation was operated");
        else{
            for(String name : operationTimes.keySet()) {
                double time = operationTimes.getOrDefault(name, 0.0) / operationCounts.getOrDefault(name,  1);
                time = time * 0.001;
                BigDecimal roundedValue = new BigDecimal(time).setScale(2,RoundingMode.UP);
                System.out.println("Operation " + name + " = " + roundedValue);
            }

        }

    }


    public double getTotalProductionTime() {
        return totalProductionTime;
    }
}