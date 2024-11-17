package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.ProductionTree;

import java.util.Map;

public class ProductionTreeSearcher {

    private final ProductionTree productionTree;

    public ProductionTreeSearcher() {
        // Initialize with the repository's production tree
        this.productionTree = Repositories.getInstance().getProductionTree();
    }

    // Method to search for a node by its name or ID
    public String searchNodeByNameOrId(String identifier) {
        ProductionNode node = productionTree.getNodesMap().get(identifier);  // Try searching by ID

        if (node == null) {
            // If not found by ID, search by name
            node = findNodeByName(identifier);
        }

        if (node == null) {
            return "Node not found";
        }

        StringBuilder result = new StringBuilder("Node Details:\n");
        result.append("ID: ").append(node.getId()).append("\n");
        result.append("Name: ").append(node.getName()).append("\n");

        if (node.getId().startsWith("M")) {
            result.append("Type: Material\n");

            // Handle material details
            Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(node);
            if (!subNodes.isEmpty()) {
                subNodes.forEach((materialNode, quantity) ->
                        result.append("Quantity: ").append(quantity).append("\n"));
            } else {
                result.append("No material details available.\n");
            }
        } else if (node.getId().startsWith("O")) {
            result.append("Type: Operation\n");

            // Check if parent is an Operation (not just a ProductionNode)
            Object parentOperation = node.getParent();
            if (parentOperation instanceof Operation) {
                result.append("Parent Operation:\n");
                Operation parentOp = (Operation) parentOperation;
                result.append("  ID: ").append(parentOp.getId()).append("\n");
                result.append("  Name: ").append(parentOp.getName()).append("\n");
            } else {
                result.append("No parent operation available.\n");
            }

            // Handle sub-nodes or material information
            Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(node);
            if (!subNodes.isEmpty()) {
                result.append("Sub-nodes (Materials):\n");
                subNodes.forEach((materialNode, quantity) ->
                        result.append("  Material: ").append(materialNode.getName())
                                .append(" - Quantity: ").append(quantity).append("\n"));
            } else {
                result.append("No sub-nodes available.\n");
            }
        } else {
            result.append("Unknown Type\n");
        }

        return result.toString();
    }

    // Helper method to search a node by name if it isn't found by ID
    private ProductionNode findNodeByName(String name) {
        for (ProductionNode node : productionTree.getAllNodes()) {
            if (node.getName().equalsIgnoreCase(name)) {
                return node;
            }
        }
        return null;  // Return null if no matching node is found by name
    }
}

