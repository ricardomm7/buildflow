package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CriticalPathCalculator {
    private final ProductionTree productionTree;

    public CriticalPathCalculator() {
        Repositories repositories = Repositories.getInstance();
        this.productionTree = repositories.getProductionTree();
    }

    /**
     * Calculates the depth of all nodes in the production tree.
     * Depth is determined as the longest distance from any root node.
     *
     * @return a map containing each node and its depth
     */
    public Map<ProductionNode, Integer> calculateDepths() {
        Map<ProductionNode, Integer> depths = new HashMap<>();
        Set<ProductionNode> visited = new HashSet<>();

        // Traverse all nodes in the production tree
        for (ProductionNode node : productionTree.getAllNodes()) {
            calculateNodeDepth(node, depths, visited);
        }
        return depths;
    }

    /**
     * Recursively calculates the depth of a single node using DFS.
     *
     * @param node    the node to calculate depth for
     * @param depths  the map storing depths of each node
     * @param visited a set of already visited nodes
     * @return the depth of the given node
     */
    private int calculateNodeDepth(ProductionNode node, Map<ProductionNode, Integer> depths, Set<ProductionNode> visited) {
        // If depth already calculated, return it
        if (depths.containsKey(node)) {
            return depths.get(node);
        }

        // If already visited during this DFS path, avoid cycles
        if (visited.contains(node)) {
            return 0; // Cycle detected; treat depth as 0 (or handle as needed)
        }
        visited.add(node);

        // Calculate depth as max depth of parent nodes + 1
        int maxDepth = 0;
        for (ProductionNode parent : productionTree.getParentNodes(node)) {
            maxDepth = Math.max(maxDepth, calculateNodeDepth(parent, depths, visited));
        }
        visited.remove(node);

        int depth = maxDepth + 1;
        depths.put(node, depth);
        return depth;
    }

}
