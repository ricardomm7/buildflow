package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;

import java.util.LinkedList;
import java.util.Map;

import static fourcorp.buildflow.application.MachineFlowAnalyzer.addDependency;
import static fourcorp.buildflow.application.MachineFlowAnalyzer.printMachineDependencies;

public class CalculateProductionTime {


    public static void calculateTotalProductionTime() {
        System.out.println("\nTotal production time for each product:");

        for (Map.Entry<String, Product> entry : Reader.products.entrySet()) {
            String productId = entry.getKey();
            Product product = entry.getValue();

            Workstation previousWorkstation = null; // US007

            double totalTime = 0;
            boolean skipProduct = false;

            for (String operation : product.getOperations()) {
                LinkedList<Workstation> workstations = Reader.machinesPerOperation.get(operation);

                if (workstations != null && !workstations.isEmpty()) {
                    Workstation fastestWorkstation = findFastestMachine(workstations);
                    totalTime += fastestWorkstation.getTime();

                    if (previousWorkstation != null) { // US007
                        addDependency(previousWorkstation.getIdMachine(), fastestWorkstation.getIdMachine());
                    }

                    previousWorkstation = fastestWorkstation; // US007
                    //machines.remove(fastestMachine);
                } else {
                    System.out.println("No machine found for the operation: " + operation + " of the article: " + productId);
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
        int minTime = Integer.MAX_VALUE;
        for (Workstation workstation : workstations) {
            if (workstation.getTime() < minTime) {
                minTime = workstation.getTime();
                fastestWorkstation = workstation;

            }
        }
        return fastestWorkstation;
    }

}


