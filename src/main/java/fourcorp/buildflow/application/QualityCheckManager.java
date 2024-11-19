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
                        o1.getDepth(productionTree)
                )
        );

        for (ProductionNode node : productionTree.getAllNodes()) {
            if (node.isOperation()) {
                qualityChecks.add(node);
            }
        }

        System.out.println("\n--- Quality Checks in Priority Order ---");

        while (!qualityChecks.isEmpty()) {
            ProductionNode operation = qualityChecks.poll();
            System.out.println("Executing Operation: " + operation.getName());
            System.out.println("    ID: " + operation.getId());
            System.out.println("    Depth: " + operation.getDepth(productionTree));
            System.out.println("-----------------------------------------");

        }
    }
}
