package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.MapLinked;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.util.*;

public class Simulator {
    private MapLinked<Operation, Product, String> operationQueues;
    private List<Product> products;
    private WorkstationsPerOperation w = Repositories.getInstance().getWorkstationsPerOperation();

    public Simulator() {
        this.operationQueues = new MapLinked<>();
        products = new ArrayList<>();
    }

    // AC1: Create preliminary queues for each operation
    public void createOperationQueues(List<Product> products) {
        for (Product product : products) {
            this.products.add(product);
            for (Operation o : product.getOperations()) {
                if (o != null) {
                    operationQueues.newItem(o, product);
                }
            }
        }
    }

    public MapLinked<Operation, Product, String> getOperationQueues() {
        return operationQueues;
    }

    public List<Product> getProducts() {
        return products;
    }

    // AC2: Assign items to machines based on availability and processing time
    public void processItems() {
        // Itera sobre cada produto
        for (Product product : products) {
            // Obtém a lista de operações associada ao produto
            LinkedList<Operation> operationQueue = (LinkedList<Operation>) operationQueues.getByKey(product);

            // Lista para manter o controle das operações que não puderam ser processadas neste ciclo
            List<Operation> pendingOperations = new ArrayList<>();

            // Enquanto houver operações na fila
            while (!operationQueue.isEmpty()) {
                Operation currentOperation = operationQueue.poll(); // Remove a primeira operação da fila

                // Encontra a melhor máquina disponível para a operação atual
                Workstation bestMachine = findBestMachineForOperation(currentOperation.getId());

                if (bestMachine != null && bestMachine.isAvailable()) {
                    // Processa a operação com a máquina
                    bestMachine.processProduct(product);
                } else {
                    // Se não houver máquina disponível, a operação é pendente
                    pendingOperations.add(currentOperation);
                }
            }

            // Reinsere as operações pendentes no final da fila para o mesmo produto
            operationQueue.addAll(pendingOperations);
        }
    }

    private Workstation findBestMachineForOperation(String operationName) {
        List<Workstation> workstations = w.getProductsByPriority(new Operation(operationName));

        // Usar uma priority queue para encontrar a máquina mais rápida disponível
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
}
