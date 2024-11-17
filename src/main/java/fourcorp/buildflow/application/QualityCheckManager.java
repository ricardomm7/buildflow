package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.util.List;
import java.util.stream.Collectors;

public class QualityCheckManager {
    private final ProductionTree productionTree;

    public QualityCheckManager() {
        // Obter o grafo de produção do repositório
        this.productionTree = Repositories.getInstance().getProductionTree();
    }

    public QualityCheckManager(ProductionTree productionTree) {
        this.productionTree = productionTree;
    }

    /**
     * Displays all operations from the repository in an organized format, in English.
     */
    public void displayAllOperations() {
        System.out.println("\n--- Operations List ---");

        // Get all nodes from the production tree
        List<ProductionNode> allNodes = productionTree.getAllNodes();

        for (ProductionNode node : allNodes) {
            // Display details for each operation/node
            System.out.println("Node Name: " + node.getName());
            System.out.println("    ID: " + node.getId());
            System.out.println("    Type: " + (node.isOperation() ? "Operation" : "Product"));
            System.out.println("    Dependencies: " + formatDependencies(node));
            if (!node.isOperation()) {
                System.out.println("    Produced Quantity: " + node.getProducedQuantity());

            }
            System.out.println("---------------------------");
        }
    }

    /**
     * Formats the dependencies of a node, obtained from the production tree, in English.
     */
    private String formatDependencies(ProductionNode node) {
        // Get dependent nodes using the repository method
        List<ProductionNode> dependencies = productionTree.getParentNodes(node);

        if (dependencies.isEmpty()) {
            return "None";
        }

        return dependencies.stream()
                .map(ProductionNode::getName)
                .collect(Collectors.joining(", "));
    }

}
