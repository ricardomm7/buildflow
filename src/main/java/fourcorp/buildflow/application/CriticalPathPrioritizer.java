package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.util.Comparator;
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
        PriorityQueue<ProductionNode> byDepth = getProductionNodes(); // O(n)

        for (ProductionNode node : productionTree.getAllNodes()) { // O(n)
            if (node.isOperation()) {
                byDepth.add(node); // O(1) * O(n) = O(n)
            }
        }

        System.out.println("\n--- Critical Path by Depth ---");

        while (!byDepth.isEmpty()) { // O(n)
            ProductionNode operation = byDepth.poll(); // O(n) * O(log n) = O(n log n)
            System.out.println("Operation: " + operation.getName());
            System.out.println("    ID: " + operation.getId());
            System.out.println("    Depth: " + operation.getDepth(productionTree));
            System.out.println("--------------------------------");
        }
    }

    /**
     * Creates a new priority queue of ProductionNode objects sorted by their depth in the production tree in descending order.
     * The priority queue is used to display critical path operations prioritized by depth.
     *
     * @return a new priority queue of ProductionNode objects sorted by depth
     * The complexity of this method is O(n), where n is the number of nodes in the production tree.
     */
    private PriorityQueue<ProductionNode> getProductionNodes() {
        PriorityQueue<ProductionNode> byDepth; // O(1)

        Comparator<ProductionNode> depthComparator = new Comparator<ProductionNode>() {
            @Override
            public int compare(ProductionNode node1, ProductionNode node2) {
                int depth1 = node1.getDepth(productionTree); // O(n)
                int depth2 = node2.getDepth(productionTree); // O(n)
                return depth2 - depth1;
            }
        };
        byDepth = new PriorityQueue<>(depthComparator); // O(1)
        return byDepth;
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

