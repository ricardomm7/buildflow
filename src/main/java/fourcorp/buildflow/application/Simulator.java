package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
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
        this.processedProducts = new ArrayList<>();
    }

    public boolean areAllQueuesEmpty() {
        return productLine.getAllProducts().isEmpty();
    }

    public void runWithPriority() {
        System.out.println("\n\n>>> NOW IT'S PROCESSING THE HIGH PRIORITY PRODUCTS\n\n");
        runSimulation(productLine.getProductsByPriority(PriorityOrder.HIGH));
        System.out.println("\n\n>>> NOW IT'S PROCESSING THE NORMAL PRIORITY PRODUCTS\n\n");
        runSimulation(productLine.getProductsByPriority(PriorityOrder.NORMAL));
        System.out.println("\n\n>>> NOW IT'S PROCESSING THE LOW PRIORITY PRODUCTS\n\n");
        runSimulation(productLine.getProductsByPriority(PriorityOrder.LOW));
    }

    public void runWithoutPriority() {
        processedProducts.clear();
        for (Product a : productLine.getAllProducts()) {
            a.setCurrentOperationIndex(0);
        }
        runSimulation(productLine.getAllProducts());
    }

    private void runSimulation(List<Product> products) {
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
                        List<Workstation> availableWorkstations = workstationsPerOperation.getWorkstationsByOperation(currentOperation);

                        for (Workstation workstation : availableWorkstations) {
                            if (workstation.isAvailable()) {
                                workstation.processProduct(product);
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
}
