package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
     * Retrieves the critical path by sorting all nodes in descending order of depth.
     *
     * @return a list of nodes representing the critical path
     */
    public List<ProductionNode> getCriticalPath() {
        // Calculate depths for all nodes
        Map<ProductionNode, Integer> depths = calculator.calculateDepths();

        // Use a priority queue to sort nodes by depth in descending order
        PriorityQueue<ProductionNode> queue = new PriorityQueue<>(
                (n1, n2) -> depths.get(n2) - depths.get(n1) // Sort by descending depth
        );
        queue.addAll(depths.keySet());

        // Retrieve nodes in sorted order
        List<ProductionNode> criticalPath = new ArrayList<>();
        while (!queue.isEmpty()) {
            criticalPath.add(queue.poll());
        }
        return criticalPath;
    }

    /**
     * Displays the critical path in a structured format, in English.
     */
    public void displayCriticalPath() {
        PriorityQueue<ProductionNode> tempQueue = new PriorityQueue<>(
                (a, b) -> Integer.compare(b.getDepth(productionTree), a.getDepth(productionTree))
        );

        // Add all nodes to the priority queue
        for (ProductionNode node : productionTree.getAllNodes()) {
            tempQueue.offer(node);
        }

        System.out.println("\n--- Critical Path ---");
        while (!tempQueue.isEmpty()) {
            ProductionNode node = tempQueue.poll();
            System.out.println("Node Type: " + (node.isOperation() ? "Operation" : "Material"));
            System.out.println("    Name: " + node.getName());
            System.out.println("    ID: " + node.getId());
            System.out.println("    Depth: " + node.getDepth(productionTree));
            System.out.println("---------------------------");
        }
    }

}

