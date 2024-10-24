package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
    private ProductPriorityLine productLine;
    private WorkstationsPerOperation workstationsPerOperation;
    private List<Product> processedProducts; // Lista para armazenar produtos processados

    public Simulator() {
        this.productLine = Repositories.getInstance().getProductPriorityRepository();
        this.workstationsPerOperation = Repositories.getInstance().getWorkstationsPerOperation();
        this.processedProducts = new ArrayList<>(); // Inicializa a lista de produtos processados
        runSimulation();
    }

    public boolean areAllQueuesEmpty() {
        return productLine.getAllProducts().isEmpty();
    }

    public void runSimulation() {
        boolean itemsProcessed;
        try {
            do {
                itemsProcessed = false;
                List<Product> articlesToMove = new ArrayList<>();

                // Itera sobre as prioridades, começando com HIGH, depois MEDIUM, e LOW
                for (PriorityOrder priority : PriorityOrder.values()) {
                    List<Product> productsInPriority = productLine.getProductsByPriority(priority);

                    for (Product product : new ArrayList<>(productsInPriority)) {
                        // Verifica se o produto já foi processado
                        if (processedProducts.contains(product)) {
                            continue; // Pula se já foi processado
                        }

                        Operation currentOperation = product.getCurrentOperation(); // Pega a operação atual

                        if (currentOperation != null) {
                            List<Workstation> availableWorkstations = workstationsPerOperation.getWorkstationsByOperation(currentOperation);

                            // Tenta processar o produto nas estações de trabalho disponíveis
                            for (Workstation workstation : availableWorkstations) {
                                if (workstation.isAvailable()) {
                                    workstation.processProduct(product);
                                    itemsProcessed = true;

                                    // Move para a próxima operação
                                    if (product.moveToNextOperation()) {
                                        articlesToMove.add(product);
                                        System.out.println("Moving product " + product.getIdItem() + " to the next operation: " + product.getCurrentOperation().getId());
                                    } else {
                                        // O produto completou todas as operações, mas não será removido
                                        processedProducts.add(product); // Adiciona o produto à lista de processados
                                        System.out.println("Product " + product.getIdItem() + " has completed all operations.");
                                    }

                                    break; // Sai do loop de estações assim que o produto é processado
                                }
                            }
                        }
                    }
                }

                // Adiciona os produtos à próxima fila de operações
                for (Product product : articlesToMove) {
                    Operation nextOperation = product.getCurrentOperation();
                    if (nextOperation != null) {
                        System.out.println("Adding product " + product.getIdItem() + " to queue for operation: " + nextOperation.getId());
                    }
                }

                // Atualiza a condição do loop
                // Agora precisamos garantir que não estamos processando produtos incompletos
                // e que a simulação termina quando todos os produtos estiverem processados e as filas estiverem vazias

            } while (itemsProcessed || !areAllQueuesEmpty() && processedProducts.isEmpty());  // Corrigida a condição de parada

        } catch (Exception e) {
            System.out.println("Error during simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
