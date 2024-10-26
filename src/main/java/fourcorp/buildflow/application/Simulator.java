package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.util.*;

import static java.lang.System.currentTimeMillis;

public class Simulator {
    private final ProductPriorityLine productLine;
    private final WorkstationsPerOperation workstationsPerOperation;
    private final List<Product> processedProducts;
    private final Map<Product, Double> productTimes; // USEI003
    private double totalProductionTime; // USEI003
    private final Map<String, Double> operationTimes; // USEI004
    private final Map<String, Double> workstationTimes; // USEI005
    private final Map<String, Map<String, Integer>> workstationDependencies = new HashMap<>(); // USEI07
    private final Map<String, Queue<Product>> waitingQueue; // Fila de espera para operações
    private final Map<Product, Double> waitingTimes; // Tempo de espera para produtos
    private HashMap<String, Double > operationWaitingTimes;
    private HashMap<String, Integer >  countWaiting;
    private HashMap<String,Integer> countOperation;


    public Simulator() {
        this.productLine = Repositories.getInstance().getProductPriorityRepository();
        this.workstationsPerOperation = Repositories.getInstance().getWorkstationsPerOperation();
        this.processedProducts = new ArrayList<>();
        this.productTimes = new HashMap<>(); // USEI003
        this.totalProductionTime = 0.0; // USEI003
        this.operationTimes = new HashMap<>(); // USEI004
        this.workstationTimes = new HashMap<>();  // USEI005
        this.waitingQueue = new HashMap<>(); // Para organizar produtos por operação na espera
        this.waitingTimes = new HashMap<>(); // Tempo total de espera de cada produto
        this.countOperation = new HashMap<>();
        this.countWaiting = new HashMap<>();
        this.operationWaitingTimes = new HashMap<>();

    }

    public Simulator(WorkstationsPerOperation a, ProductPriorityLine b) {
        this.productLine = b;
        this.workstationsPerOperation = a;
        this.processedProducts = new ArrayList<>();
        this.productTimes = new HashMap<>(); // USEI003
        this.totalProductionTime = 0.0; // USEI003
        this.operationTimes = new HashMap<>(); // USEI004
        this.workstationTimes = new HashMap<>();  // USEI005

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

                                currentOperation.setWorkstation(workstation); // USEI07

                                double operationTime = workstation.getTime();

                                // Marcar a workstation como não disponível e agendar para voltar a ficar disponível
                                //markWorkstationAsUnavailable(workstation, operationTime);

                                productTimes.merge(product, operationTime, Double::sum); //USEI03
                                totalProductionTime += operationTime; //USEI03

                                String operationName = currentOperation.getId(); // USEI04
                                operationTimes.merge(operationName, operationTime, Double::sum);
                                countOperation.put(operationName,countOperation.getOrDefault(operationName, 0) + 1);//USEI04

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

                            currentOperation.setWorkstation(workstation); // USEI07


                            //markWorkstationAsUnavailable(workstation, operationTime);
                            productTimes.merge(product, operationTime, Double::sum);
                            totalProductionTime += operationTime;
                            operationTimes.merge(operationId, operationTime, Double::sum);
                            countOperation.put(operationId,countOperation.getOrDefault(operationId,0)+1);

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

    private void doDependency() {
        for (Product product : processedProducts) {
            List<Operation> dependencies = product.getOperations();
            for (int i = 0; i < dependencies.size() - 1; i++) {
                Workstation first = dependencies.get(i).getWorkstation();
                Workstation second = dependencies.get(i + 1).getWorkstation();

                // Verifique se as workstations não são nulas antes de atualizar as dependências
                if (first != null && second != null) {
                    updateWorkstationDependencies(first, second);
                } else {
                    System.out.println("Workstation is null for operation " + dependencies.get(i).getId());
                }
            }
        }
    }

    // Novo método para atualizar dependências
    private void updateWorkstationDependencies(Workstation from, Workstation to) {
        workstationDependencies
                .computeIfAbsent(from.getId(), k -> new HashMap<>())
                .merge(to.getId(), 1, Integer::sum);
    }

    // Método para exibir o fluxo de dependência USEI07
    public void printWorkstationDependencies() {
        doDependency();
        String lineFormat = "%s : %s%n";

        for (Map.Entry<String, Map<String, Integer>> entry : workstationDependencies.entrySet()) {
            String workstation = entry.getKey();
            List<String> dependencies = new ArrayList<>();

            entry.getValue().entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Ordena em ordem decrescente
                    .forEach(e -> dependencies.add(String.format("(%s,%d)", e.getKey(), e.getValue())));

            System.out.printf(lineFormat, workstation, dependencies);
        }
    }
/*
    private void markWorkstationAsUnavailable(Workstation workstation, double operationTime) {
        workstation.setAvailable(false);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                workstation.setAvailable(true);
                //System.out.println("Workstation " + workstation.getId() + " is now available.");
            }
        }, (long) (operationTime * 0.1)); // Tempo em milissegundos
    }

 */


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
        workstationDependencies.clear();
        productTimes.clear();
        totalProductionTime = 0.0;
        operationTimes.clear();
        workstationTimes.clear();
        waitingQueue.clear();
        waitingTimes.clear();
        processedProducts.clear();
    }
    public Operation calculateBeginingWaiting(Operation opr){
        long time = currentTimeMillis();
        opr.setTime();
        return opr;
    }
    public void calculateFinishWaiting(Operation opr) {
        String name = opr.getId();
        double time =  System.currentTimeMillis();
        double finalTime = (time - opr.getTime()) + operationWaitingTimes.getOrDefault(name, (double) 0);
        operationWaitingTimes.put(name, finalTime);
        int count = countWaiting.getOrDefault(name, 0) + 1;
        countWaiting.put(name, count);
    }

    public void printAverageWaitingTimes (){
        System.out.println("__________________Average_Waiting_Times_Report__________________");

        if(operationWaitingTimes.isEmpty())
            System.out.println("---Error---You dont have any Waiting Time saved");
        else if (countWaiting.isEmpty())
            System.out.println("---Error---You didn't save how many times the Operation in Waiting was operated");
        else{
            for(int i = 0; i < operationWaitingTimes.size(); i++ ){
                String name =operationWaitingTimes.toString();
                for(int j = 0; j< countWaiting.size(); j++){
                    double time = (double) operationWaitingTimes.getOrDefault(name, (double) 0) / countWaiting.getOrDefault(name,(int)1);
                    System.out.println("Operation "+name+" = "+time);
                }
            }
        }

    }

    public void printAverageTimePerOperation(){
        System.out.println("__________________Average_Time_Per_Operation_Report__________________");

        if(operationTimes.isEmpty())
            System.out.println("---Error---You dont have any Operation Time saved");
        else if (countOperation.isEmpty())
            System.out.println("---Error---You didn't save how many times the Operation was operated");
        else{
            for(int i = 0; i < operationTimes.size(); i++ ){
                String name =operationTimes.toString();
                for(int j = 0; j< countOperation.size(); j++){
                    double time = (double) operationTimes.getOrDefault(name, (double) 0) / countOperation.getOrDefault(name,(int)1);
                    System.out.println("Operation "+name+" = "+time);
                }
            }
        }

    }


    public double getTotalProductionTime() {
        return totalProductionTime;
    }
}