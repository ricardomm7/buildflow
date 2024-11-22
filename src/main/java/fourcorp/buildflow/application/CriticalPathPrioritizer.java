package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.util.PriorityQueue;

public class CriticalPathPrioritizer {
    private final CriticalPathCalculator calculator;
    private ProductionTree productionTree;
    private Repositories r = Repositories.getInstance();


    public CriticalPathPrioritizer() {
        this.productionTree = r.getProductionTree();
        this.calculator = new CriticalPathCalculator();

    }

    /**
     * Displays critical path operations prioritized by depth in the production tree.
     * Overall complexity: O(n log n) where n is the number of nodes in the production tree
     */
    public void displayCriticalPathByDepth() {
        PriorityQueue<ProductionNode> byDepth = new PriorityQueue<>(
                (node1, node2) -> {
                    int depth1 = node1.getDepth(productionTree);
                    int depth2 = node2.getDepth(productionTree);
                    return depth2 - depth1;
                }
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

    /**
     * Sets new production tree.
     *
     * @param productionTree the production tree
     */
    public void setProductionTree(ProductionTree productionTree) {
        this.productionTree = productionTree;
    }
}

