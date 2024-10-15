package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.util.List;

import static fourcorp.buildflow.application.MachineFlowAnalyzer.addDependency;
import static fourcorp.buildflow.application.MachineFlowAnalyzer.printMachineDependencies;

public class CalculateProductionTime {
    public static ProductPriorityLine p = Repositories.getInstance().getProductPriorityRepository();
    public static WorkstationsPerOperation w = Repositories.getInstance().getWorkstationsPerOperation();

    public static void calculateTotalProductionTime() {
        for (PriorityOrder x : PriorityOrder.values()) {
            for (Product entry : p.getProductsByPriority(x)) {
                String productId = entry.getId();

                Workstation previousWorkstation = null; // US007
                double totalTime = 0;
                boolean skipProduct = false;

                for (Operation operation : entry.getOperations()) {
                    // Recupera as estações de trabalho para a operação
                    List<Workstation> workstations = Repositories.getInstance()
                            .getWorkstationsPerOperation()
                            .getWorkstationsPerOperation()
                            .getByKey(operation);

                    if (workstations != null && !workstations.isEmpty()) {
                        // Encontra a máquina mais rápida disponível
                        Workstation fastestWorkstation = w.findBestMachineForOperation(operation);

                        if (fastestWorkstation != null && fastestWorkstation.isAvailable()) {
                            // Processa o produto na máquina em uma thread separada
                            fastestWorkstation.processProduct(entry);

                            totalTime += fastestWorkstation.getTime();

                            if (previousWorkstation != null) { // US007 - Adicionar dependências entre máquinas
                                addDependency(previousWorkstation.getIdMachine(), fastestWorkstation.getIdMachine());
                            }

                            previousWorkstation = fastestWorkstation; // Atualiza a máquina anterior
                        } else {
                            System.out.println("No machine available for the operation: " + operation.getId() + " of the product: " + productId);
                            skipProduct = true; // Se não houver máquina disponível, pula o produto
                            break;
                        }
                    } else {
                        System.out.println("No machine found for the operation: " + operation.getId() + " of the product: " + productId);
                        skipProduct = true;
                        break;
                    }
                }

                if (!skipProduct) {
                    System.out.println("Total production time for the product " + productId + ": " + totalTime + " minutes\n");
                }
            }
        }
    }
}
