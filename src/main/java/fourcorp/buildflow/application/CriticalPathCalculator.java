package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.util.ArrayList;
import java.util.List;

public class CriticalPathCalculator {
    private final ProductionTree productionTree;

    public CriticalPathCalculator() {
        Repositories repositories = Repositories.getInstance();
        this.productionTree = repositories.getProductionTree();
    }


    /**
     * Calculates all dependencies (direct and indirect) for a given node.
     * Includes all operations required before the given node.
     *
     * @param node the node whose dependencies are to be calculated
     * @return a list of all dependent nodes (only operations)
     */
    private List<ProductionNode> calculateAllDependencies(ProductionNode node) {
        List<ProductionNode> allDependencies = new ArrayList<>();
        List<ProductionNode> visited = new ArrayList<>();
        collectAllDependenciesFromBottom(node, allDependencies, visited); // O(n)
        return allDependencies;
    }

    /**
     * Recursive helper to collect all dependencies for a node, starting from the bottom of the tree.
     *
     * @param node      the current node
     * @param collected the list of dependencies collected so far
     * @param visited   nodes already visited to prevent cycles
     */
    private void collectAllDependenciesFromBottom(ProductionNode node, List<ProductionNode> collected, List<ProductionNode> visited) {
        if (visited.contains(node)) {
            return; // Avoid cycles
        }
        visited.add(node);

        for (ProductionNode child : productionTree.getSubNodes(node).keySet()) { // O(n)
            if (child.isOperation() && !collected.contains(child)) {
                collectAllDependenciesFromBottom(child, collected, visited);
                collected.add(child);
            }
        }
    }

    /**
     * Filters a list of nodes to include only operations.
     *
     * @param nodes the list of nodes to filter
     * @return a list containing only operations
     */
    private List<ProductionNode> filterOperations(List<ProductionNode> nodes) {
        List<ProductionNode> operations = new ArrayList<>();
        for (ProductionNode node : nodes) { // O(n)
            if (node.isOperation()) {
                operations.add(node);
            }
        }
        return operations;
    }

    /**
     * Displays operations with their dependencies (direct and total) in a user-friendly format.
     */
    public void displayOperationsWithDependencies() {
        List<ProductionNode> operations = productionTree.getAllNodes().stream() // O(n)
                .filter(ProductionNode::isOperation)
                .sorted((a, b) -> {
                    int totalDependenciesA = calculateAllDependencies(a).size();
                    int totalDependenciesB = calculateAllDependencies(b).size();
                    return Integer.compare(totalDependenciesB, totalDependenciesA);
                })
                .toList(); // O(n)


        System.out.println("\n--- Critical Path by Number of Dependencies ---");

        for (ProductionNode node : operations) { // O(n)
            List<ProductionNode> directDependencies = filterOperations(productionTree.getParentNodes(node));
            List<ProductionNode> allDependencies = calculateAllDependencies(node);

            System.out.println("\n" + formatOperationDetails(node, directDependencies, allDependencies)); // O(n)
        }
    }

    // Helper method to format operation details
    private String formatOperationDetails(ProductionNode node, List<ProductionNode> directDependencies, List<ProductionNode> allDependencies) {
        StringBuilder sb = new StringBuilder();

        sb.append("Operation: ").append(node.getName()).append("\n");
        sb.append("   ID: ").append(node.getId()).append("\n");

        if (directDependencies.isEmpty()) {
            sb.append("   Direct Dependencies: None\n");
        } else {
            sb.append("   Direct Dependencies: ").append(formatDependencies(directDependencies)).append("\n");
        }

        if (allDependencies.isEmpty()) {
            sb.append("   Total Dependencies: None\n");
        } else {
            sb.append("   Total Dependencies: \n");
            for (ProductionNode dependency : allDependencies) {
                sb.append("       - ").append(dependency.getName()).append("\n");
            }
        }

        sb.append("   Total Number of Dependencies: ").append(allDependencies.size()).append("\n");
        sb.append("----------------------------------------------");
        return sb.toString();
    }

    // Helper method to format a list of dependencies
    private String formatDependencies(List<ProductionNode> dependencies) {
        if (dependencies.isEmpty()) {
            return "None";
        }
        return dependencies.stream()
                .map(ProductionNode::getName) // Extract names of the nodes
                .reduce((a, b) -> a + ", " + b) // Concatenate with commas
                .orElse("None");
    }
}
