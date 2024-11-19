package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.util.PriorityQueue;

public class CriticalPathPrioritizer {
    private final CriticalPathCalculator calculator;
    private final ProductionTree productionTree;
    private Repositories r = Repositories.getInstance();


    public CriticalPathPrioritizer() {
        this.productionTree = r.getProductionTree();
        this.calculator = new CriticalPathCalculator();

    }

    /**
     * Displays critical path operations prioritized by depth in the production tree.
     */
    public void displayCriticalPathByDepth() {
        PriorityQueue<ProductionNode> byDepth = new PriorityQueue<>(
                (node1, node2) -> Integer.compare(
                        node2.getDepth(productionTree),
                        node1.getDepth(productionTree)
                )
        ); // O(n)

        for (ProductionNode node : productionTree.getAllNodes()) { // O(n)
            if (node.isOperation()) {
                byDepth.add(node); // O(n log n)
            }
        }

        System.out.println("\n--- Critical Path by Depth ---");

        while (!byDepth.isEmpty()) {
            ProductionNode operation = byDepth.poll(); // O(n log n)
            System.out.println("Operation: " + operation.getName());
            System.out.println("    ID: " + operation.getId());
            System.out.println("    Depth: " + operation.getDepth(productionTree));
            System.out.println("--------------------------------");
        }
    }

}

