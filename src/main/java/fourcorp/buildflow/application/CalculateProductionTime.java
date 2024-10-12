package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.Repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static fourcorp.buildflow.application.MachineFlowAnalyzer.addDependency;
import static fourcorp.buildflow.application.MachineFlowAnalyzer.printMachineDependencies;
import static fourcorp.buildflow.application.Reader.machines;

public class CalculateProductionTime {


    public static void calculateTotalProductionTime() {
        System.out.println("\nTotal production time for each product:");

        for (Product entry : Reader.products) {
            String productId = entry.getId();

            Workstation previousWorkstation = null; // US007

            double totalTime = 0;
            boolean skipProduct = false;

            for (Operation operation : entry.getOperations()) {
                List<Workstation> workstations = machines;//Repositories.getInstance().getMachinesPerOperation().getMachinesPerOperation().getByKey(operation);

                if (workstations != null && !workstations.isEmpty()) {
                    Workstation fastestWorkstation = findFastestMachine(new LinkedList<>(workstations));
                    totalTime += fastestWorkstation.getTime();

                    if (previousWorkstation != null) { // US007
                        addDependency(previousWorkstation.getIdMachine(), fastestWorkstation.getIdMachine());
                    }

                    previousWorkstation = fastestWorkstation; // US007
                    machines.remove(fastestWorkstation);
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


