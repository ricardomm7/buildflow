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
    private final Map<String, Double> operationTotalTimes = new HashMap<>();
    private final Map<String, Integer> operationCounts = new HashMap<>();
    private final Map<String, Double> workstationTotalTimes = new HashMap<>();
    private final Map<String, Double> operationWaitingTimes = new HashMap<>();
    private final Map<String, Integer> operationWaitingCounts = new HashMap<>();
    private boolean chooseFastestMachine;

    private final List<String> logMessages;
    private final List<String> pendingLogMessages;

    public Simulator() {
        this.operationQueues = new MapLinked<>();
        this.products = new ArrayList<>();
        this.w = Repositories.getInstance().getWorkstationsPerOperation();
        this.p = Repositories.getInstance().getProductPriorityRepository();
        this.totalProductionTime = 0.0;
        this.chooseFastestMachine = true;

        this.logMessages = new ArrayList<>();
        this.pendingLogMessages = new ArrayList<>();
    }

    // Executa a simulação sem prioridade
    public void runWithoutPriority(boolean boo) {
        setChooseFastestMachine(boo);
        createOperationQueues(p.getAllProducts());
        processItems();
        printLogMessages();
    }

    public void runWithPriority(boolean boo) {
        setChooseFastestMachine(boo);

        // Processar produtos de alta prioridade
        System.out.println("\nProcessing high priority products:");
        List<Product> highPriorityProducts = p.getProductsByPriority(PriorityOrder.HIGH);
        createOperationQueues(highPriorityProducts);
        processItems();

        // Processar produtos de prioridade normal
        System.out.println("\nProcessing normal priority products:");
        List<Product> normalPriorityProducts = p.getProductsByPriority(PriorityOrder.NORMAL);
        createOperationQueues(normalPriorityProducts);
        processItems();

        // Processar produtos de baixa prioridade
        System.out.println("\nProcessing low priority products:");
        List<Product> lowPriorityProducts = p.getProductsByPriority(PriorityOrder.LOW);
        createOperationQueues(lowPriorityProducts);
        processItems();

        // Exibe o log completo de processamento
        printLogMessages();
    }

    // Define a escolha para usar a máquina mais rápida disponível
    private void setChooseFastestMachine(boolean chooseFastestMachine) {
        this.chooseFastestMachine = chooseFastestMachine;
    }

    // Cria as filas de operações com base nos produtos lidos
    private void createOperationQueues(List<Product> products) {
        operationQueues.removeAll();  // Limpa todas as operações anteriores
        this.products.clear();  // Limpa a lista de produtos do simulador

        // Adiciona todos os produtos e suas operações na ordem correta
        for (Product product : products) {
            this.products.add(product);  // Adiciona o produto à lista

            // Adiciona as operações na fila respeitando a ordem original de leitura
            for (Operation o : product.getOperations()) {
                if (o != null) {
                    operationQueues.newItem(o, product);  // Enfileira cada operação com o respectivo produto
                }
            }
        }
    }

    // Processa os itens e as operações em fila
    private void processItems() {
        Map<Product, LinkedList<Operation>> productOperationMap = new HashMap<>();
        List<Operation> pendingOperations = new ArrayList<>();
        Map<Operation, Product> operationToProductMap = new HashMap<>();
        Workstation previousWorkstation = null;

        for (Product product : products) {
            LinkedList<Operation> operationQueue = new LinkedList<>(product.getOperations());
            productOperationMap.put(product, operationQueue);
        }

        boolean allOperationsComplete;

        do {
            allOperationsComplete = true;

            for (Map.Entry<Product, LinkedList<Operation>> entry : productOperationMap.entrySet()) {
                Product product = entry.getKey();
                LinkedList<Operation> operationQueue = entry.getValue();

                if (!operationQueue.isEmpty()) {
                    allOperationsComplete = false;
                    Operation currentOperation = operationQueue.peek();
                    Workstation bestMachine = findBestMachineForOperation(currentOperation);

                    if (bestMachine != null) {
                        addLogMessageStartOperation(product, currentOperation, bestMachine, false);
                        double operationTime = bestMachine.getTime();
                        totalProductionTime += operationTime;

                        recordOperationTime(currentOperation.getId(), operationTime);  // USEI04
                        recordWorkstationTime(bestMachine.getId(), operationTime);  // USEI05

                        bestMachine.setAvailable(false);
                        simulateProcessingTime(bestMachine, operationTime);

                        previousWorkstation = bestMachine;

                        operationQueue.poll();
                        if (operationQueue.isEmpty()) {
                            addLogMessageProductComplete(product);
                        }

                    } else {
                        pendingOperations.add(currentOperation);
                        operationToProductMap.put(currentOperation, product);
                    }
                }
            }

            processPendingOperations(pendingOperations, operationToProductMap, productOperationMap, previousWorkstation);
            allOperationsComplete = allOperationsComplete && pendingOperations.isEmpty();

        } while (!allOperationsComplete);
    }

    // Processa operações pendentes quando as máquinas se tornam disponíveis
    private void processPendingOperations(List<Operation> pendingOperations, Map<Operation, Product> operationToProductMap,
                                          Map<Product, LinkedList<Operation>> productOperationMap, Workstation previousWorkstation) {
        List<Operation> successfullyProcessed = new ArrayList<>();

        for (Operation pendingOperation : pendingOperations) {
            Workstation bestMachine = findBestMachineForOperation(pendingOperation);

            if (bestMachine != null) {
                Product product = operationToProductMap.get(pendingOperation);
                addLogMessageStartOperation(product, pendingOperation, bestMachine, true);

                double operationTime = bestMachine.getTime();
                totalProductionTime += operationTime;

                recordOperationTime(pendingOperation.getId(), operationTime);  // USEI04
                recordWorkstationTime(bestMachine.getId(), operationTime);  // USEI05

                bestMachine.setAvailable(false);
                simulateProcessingTime(bestMachine, operationTime);

                w.increaseWaitingTimes(operationTime);

                List<String> machineFlow = productMachineFlows.computeIfAbsent(product.getId(), _ -> new ArrayList<>());
                machineFlow.add(bestMachine.getId());

                if (previousWorkstation != null) {
                    addDependency(previousWorkstation.getId(), bestMachine.getId());
                }

                previousWorkstation = bestMachine;
                successfullyProcessed.add(pendingOperation);

                LinkedList<Operation> operationQueue = productOperationMap.get(product);
                operationQueue.poll();

                if (operationQueue.isEmpty()) {
                    addLogMessageProductComplete(product);
                }
            }
        }

        pendingOperations.removeAll(successfullyProcessed);
    }

    // Encontra a melhor máquina disponível para uma operação
    public Workstation findBestMachineForOperation(Operation operation) {
        List<Workstation> workstations = w.getWorkstationsByOperation(operation);

        if (chooseFastestMachine) {
            // Seleciona a máquina mais rápida disponível
            Workstation selectedMachine = null;
            double minTime = Double.MAX_VALUE;

            for (Workstation machine : workstations) {
                if (machine.isAvailable()) {
                    double operationTime = machine.getTime();
                    if (operationTime < minTime) {
                        minTime = operationTime;
                        selectedMachine = machine;
                    }
                }
            }
            return selectedMachine;  // Retorna a máquina com o menor tempo de operação
        } else {
            // Seleciona a primeira máquina disponível
            for (Workstation machine : workstations) {
                if (machine.isAvailable()) {
                    return machine;  // Retorna a primeira máquina disponível
                }
            }
            return null;  // Nenhuma máquina disponível
        }
    }

    // Exibe as estatísticas de produção
    public void printProductionStatistics() {
        System.out.println("\n=== Production Statistics ===");

        // Tempo total de produção
        System.out.printf("Total Production Time: %.2f minutes\n", totalProductionTime);

        // Tempo de produção por produto
        System.out.println("Production Time per Product:");
        productTimes.forEach((productId, time) -> {
            System.out.printf("Product %s: %.2f minutes\n", productId, time);
        });

        // Tempos de execução por operação
        System.out.println("\nExecution Times by Operation:");
        operationTotalTimes.forEach((operation, time) -> {
            System.out.printf("Operation %s: %.2f minutes\n", operation, time);
        });

        // Estatísticas por estação de trabalho
        System.out.println("\nWorkstation Statistics (sorted by execution time percentage):");
        workstationTotalTimes.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(
                        entry.getKey(),
                        new double[]{entry.getValue(), (entry.getValue() / totalProductionTime) * 100}
                ))
                .sorted(Comparator.comparingDouble(entry -> entry.getValue()[1]))
                .forEach(entry -> {
                    System.out.printf("Workstation %s: %.2f minutes (%.2f%% of total time)\n",
                            entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
                });

        // Tempos médios de execução e espera por operação
        System.out.println("\nAverage Times per Operation:");
        operationTotalTimes.forEach((operation, totalTime) -> {
            int count = operationCounts.get(operation);
            double avgExecution = totalTime / count;
            double avgWaiting = operationWaitingTimes.getOrDefault(operation, 0.0) /
                    operationWaitingCounts.getOrDefault(operation, 1);
            System.out.printf("Operation %s:\n  Avg Execution: %.2f minutes\n  Avg Waiting: %.2f minutes\n",
                    operation, avgExecution, avgWaiting);
        });
    }

    // Métodos auxiliares para estatísticas
    private void recordOperationTime(String operationId, double time) {
        operationTotalTimes.merge(operationId, time, Double::sum);
        operationCounts.merge(operationId, 1, Integer::sum);
    }

    private void recordWorkstationTime(String workstationId, double time) {
        workstationTotalTimes.merge(workstationId, time, Double::sum);
    }

    // Simula o tempo de processamento de uma operação
    private void simulateProcessingTime(Workstation machine, double operationTime) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                machine.setAvailable(true);
            }
        }, (long) (operationTime * 0.100));  // Converte para milissegundos
    }

    private void addLogMessageStartOperation(Product product, Operation operation, Workstation machine, boolean fromWaitingList) {
        String status = fromWaitingList ? " (from waiting list)" : "";
        pendingLogMessages.add(">>> Starting operation " + operation.getId() + status +
                " for Product " + product.getId() + " on Machine " + machine.getId() +
                ". Estimated time: " + machine.getTime() + " minutes.");
    }

    private void addLogMessageProductComplete(Product product) {
        pendingLogMessages.add("\n>>> Product " + product.getId() + " has completed all operations!");
    }

    private void printLogMessages() {
        logMessages.addAll(pendingLogMessages);
        pendingLogMessages.clear();
        for (String message : logMessages) {
            System.out.println(message);
        }
    }
}
