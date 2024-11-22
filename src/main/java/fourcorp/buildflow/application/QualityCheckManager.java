package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.util.PriorityQueue;

public class QualityCheckManager {
    private final ProductionTree productionTree;

    public QualityCheckManager() {
        this.productionTree = Repositories.getInstance().getProductionTree();
    }

    public QualityCheckManager(ProductionTree productionTree) {
        this.productionTree = productionTree;
    }

    /**
     * Displays and processes quality checks in order of priority.
     */
    public void prioritizeAndExecuteQualityChecks() {
        PriorityQueue<ProductionNode> qualityChecks = new PriorityQueue<>(
                (o1, o2) -> Integer.compare(
                        o2.getDepth(productionTree),
                        o1.getDepth(productionTree)                              // O(log n)
                )
        );

        for (ProductionNode node : productionTree.getAllNodes()) {               // O(n)
            if (node.isOperation()) {
                qualityChecks.add(node);                                         // O(log n)
            }
        }

        System.out.println("\n--- Quality Checks in Priority Order ---");        // O(1)

        while (!qualityChecks.isEmpty()) {                                       // O(n)
            ProductionNode operation = qualityChecks.poll();                     // O(log n)
            System.out.println("Executing Operation: " + operation.getName());   // O(1)
            System.out.println("    ID: " + operation.getId());                  // O(1)
            System.out.println("    Depth: " + operation.getDepth(productionTree)); // O(log n)
            System.out.println("-----------------------------------------");     // O(1)

        }
    }
}