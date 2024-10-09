package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Machine;
import fourcorp.buildflow.domain.Product;

import java.util.LinkedList;
import java.util.Map;

public class CalculateProductionTime {


    public static void calculateTotalProductionTime() {
        for (Map.Entry<String, Product> entry : Reader.products.entrySet()) {
            String productId = entry.getKey();
            Product product = entry.getValue();

            double totalTime = 0;
            boolean skipProduct = false;

            for (String operation : product.getOperations()) {
                LinkedList<Machine> machines = Reader.machinesPerOperation.get(operation);

                if (machines != null && !machines.isEmpty()) {
                    Machine fastestMachine = findFastestMachine(machines);
                    totalTime += fastestMachine.getTime();
                    machines.remove(fastestMachine);
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
    }

    static Machine findFastestMachine(LinkedList<Machine> machines) {
        Machine fastestMachine = null;
        int minTime = Integer.MAX_VALUE;
        for (Machine machine : machines) {
            if (machine.getTime() < minTime) {
                minTime = machine.getTime();
                fastestMachine = machine;
            }
        }
        return fastestMachine;
    }

}


