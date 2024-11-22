package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.util.ArrayList;
import java.util.List;

public class CriticalPathCalculator {
    private ProductionTree productionTree;

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
     * The complexity of this method is O(n^2), where n is the number of nodes in the production tree.
     */
    List<ProductionNode> calculateAllDependencies(ProductionNode node) {
        List<ProductionNode> allDependencies = new ArrayList<>();
        List<ProductionNode> visited = new ArrayList<>();
        collectAllDependenciesFromBottom(node, allDependencies, visited); // O(n^2)
        return allDependencies;
    }

    /**
     * Recursive helper to collect all dependencies for a node, starting from the bottom of the tree.
     *
     * @param node      the current node
     * @param collected the list of dependencies collected so far
     * @param visited   nodes already visited to prevent cycles
     *                  The complexity of this method is O(n^2), where n is the number of nodes in the production tree.
     */
    private void collectAllDependenciesFromBottom(ProductionNode node, List<ProductionNode> collected, List<ProductionNode> visited) {
        if (visited.contains(node)) { // O(n)
            return;
        }
        visited.add(node);

        for (ProductionNode child : productionTree.getSubNodes(node).keySet()) { // O(n)
            if (child.isOperation() && !collected.contains(child)) {             // O(n) * O(n) = O(n^2)
                collectAllDependenciesFromBottom(child, collected, visited);     // O(n) * O(n) = O(n^2)
                collected.add(child);
            }
        }
    }

    /**
     * Filters a list of nodes to include only operations.
     *
     * @param nodes the list of nodes to filter
     * @return a list containing only operations
     * The complexity of this method is O(n), where n is the number of nodes in the list.
     */
    private List<ProductionNode> filterOperations(List<ProductionNode> nodes) {
        List<ProductionNode> operations = new ArrayList<>();
        for (ProductionNode node : nodes) { // O(n)
            if (node.isOperation()) { // O (n) * O(1) = O(n)
                operations.add(node); // O(n) * O(1) = O(n)
            }
        }
        return operations;
    }

    /**
     * Displays operations with their dependencies (direct and total) in a user-friendly format.
     * The complexity of this method is O(n^3), where n is the number of operations.
     */
    public void displayOperationsWithDependencies() {
        List<ProductionNode> operations = productionTree.getAllNodes().stream()
                .filter(ProductionNode::isOperation)
                .sorted((nodeA, nodeB) -> {                                   // O(n log n)

                    int dependenciesForNodeA = calculateAllDependencies(nodeA).size(); // O(n)
                    int dependenciesForNodeB = calculateAllDependencies(nodeB).size(); // O(n)

                    return Integer.compare(dependenciesForNodeB, dependenciesForNodeA); // O(1)
                })
                .toList(); // O(n)


        System.out.println("\n--- Critical Path by Number of Dependencies ---");

        for (ProductionNode node : operations) { // O(n)
            List<ProductionNode> directDependencies = filterOperations(productionTree.getParentNodes(node)); // O(n) * O(n) = O(n^2)
            List<ProductionNode> allDependencies = calculateAllDependencies(node); // O(n) * O(n^2) = O(n^3)

            System.out.println("\n" + formatOperationDetails(node, directDependencies, allDependencies)); // O(n) * O(1) = O(n)
        }
    }

    /**
     * Formats the details of an operation, including its name, ID, direct dependencies, total dependencies, and the total number of dependencies.
     *
     * @param node               the operation node
     * @param directDependencies the direct dependencies of the operation
     * @param allDependencies    the total dependencies of the operation
     * @return a formatted string containing the operation details
     * The complexity of this method is O(n), where n is the number of dependencies.
     */
    private String formatOperationDetails(ProductionNode node, List<ProductionNode> directDependencies, List<ProductionNode> allDependencies) {
        StringBuilder sb = new StringBuilder();

        sb.append("Operation: ").append(node.getName()).append("\n"); // O(1)
        sb.append("   ID: ").append(node.getId()).append("\n"); // O(1)

        if (directDependencies.isEmpty()) { // O(1)
            sb.append("   Direct Dependencies: None\n"); // O(1)
        } else {
            sb.append("   Direct Dependencies: ").append(formatDependencies(directDependencies)).append("\n"); // O(n)
        }

        if (allDependencies.isEmpty()) { // O(1)
            sb.append("   Total Dependencies: None\n"); // O(1)
        } else {
            sb.append("   Total Dependencies: \n"); // O(1)
            for (ProductionNode dependency : allDependencies) { // O(n)
                sb.append("       - ").append(dependency.getName()).append("\n"); // O(1)
            }
        }

        sb.append("   Total Number of Dependencies: ").append(allDependencies.size()).append("\n"); // O(1)
        sb.append("----------------------------------------------"); // O(1)
        return sb.toString();
    }

    /**
     * Formats a list of dependencies as a comma-separated string.
     *
     * @param dependencies the list of dependencies to format
     * @return a comma-separated string of dependency names, or "None" if the list is empty
     * The complexity of this method is O(n), where n is the number of dependencies.
     */
    private String formatDependencies(List<ProductionNode> dependencies) {
        if (dependencies.isEmpty()) {
            return "None";
        }
        return dependencies.stream()
                .map(ProductionNode::getName) // O(n)
                .reduce((a, b) -> a + ", " + b)
                .orElse("None");
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