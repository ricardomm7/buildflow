package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.util.LinkedList;
import java.util.List;

import static fourcorp.buildflow.application.MachineFlowAnalyzer.addDependency;
import static fourcorp.buildflow.application.MachineFlowAnalyzer.printMachineDependencies;

public class CalculateProductionTime {
    public static ProductPriorityLine p = Repositories.getInstance().getProductPriorityRepository();
    public static WorkstationsPerOperation w = Repositories.getInstance().getWorkstationsPerOperation();

    public static void calculateTotalProductionTime() {
        System.out.println("\nTotal production time for each product:");
        for (PriorityOrder x : PriorityOrder.values()) {
            for (Product entry : p.getProductsByPriority(x)) {
                String productId = entry.getId();

                Workstation previousWorkstation = null; // US007

                double totalTime = 0;
                boolean skipProduct = false;

                for (Operation operation : entry.getOperations()) {
                    //List<Workstation> workstations = machines;
                    List<Workstation> workstations = Repositories.getInstance().getWorkstationsPerOperation().getWorkstationsPerOperation().getByKey(operation);

                    if (workstations != null && !workstations.isEmpty()) {
                        Workstation fastestWorkstation = findFastestMachine(new LinkedList<>(workstations));
                        totalTime += fastestWorkstation.getTime();

                        if (previousWorkstation != null) { // US007
                            addDependency(previousWorkstation.getIdMachine(), fastestWorkstation.getIdMachine());
                        }

                        previousWorkstation = fastestWorkstation; // US007
                        w.removeWorkstation(fastestWorkstation, operation);
                    } else {
                        System.out.println("No machine found for the operation: " + operation.getId() + " of the article: " + productId);
                        skipProduct = true; //
                        break; // Caso seja para contar tempo de produção de produtos mesmo sem máquinas, remover o break e o boolean skipProduct
                    }
                }

                if (!skipProduct) {
                    System.out.println("Total production time for the article " + productId + ": " + totalTime + " minutes");
                }
            }
        }
        System.out.println("\nDependencies between machines:");
        printMachineDependencies(); // US007

    }

    static Workstation findFastestMachine(LinkedList<Workstation> workstations) {
        Workstation fastestWorkstation = null;
        double minTime = Integer.MAX_VALUE;
        for (Workstation workstation : workstations) {
            if (workstation.getTime() < minTime) {
                minTime = workstation.getTime();
                fastestWorkstation = workstation;

            }
        }
        return fastestWorkstation;
    }

}


