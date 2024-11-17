package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.ProductionTree;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ProductionTreeSearcher {

    private final ProductionTree productionTree;

    public ProductionTreeSearcher() {
        // Initialize with the repository's production tree
        this.productionTree = Repositories.getInstance().getProductionTree();
    }

    // Method to search for a node by its name or ID
    public String searchNodeByNameOrId(String identifier) {
        List<ProductionNode> matchingNodes = productionTree.searchNodes(identifier);  // Get all matching nodes

        // If no nodes match, return an appropriate message
        if (matchingNodes.isEmpty()) {
            return "No matching nodes found.";
        }

        // If there's only one result, return its details directly
        if (matchingNodes.size() == 1) {
            return getNodeDetails(matchingNodes.get(0));
        }

        // If there are multiple matches, prompt the user to select one
        System.out.println("Multiple matches found:");
        for (int i = 0; i < matchingNodes.size(); i++) {
            System.out.println((i + 1) + ". " + matchingNodes.get(i).getName() + " (ID: " + matchingNodes.get(i).getId() + ")");
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please select a node by entering the number (1-" + matchingNodes.size() + "): ");
        int choice = scanner.nextInt();

        // Validate user choice
        if (choice < 1 || choice > matchingNodes.size()) {
            return "Invalid choice.";
        }

        // Return the details of the selected node
        return getNodeDetails(matchingNodes.get(choice - 1));
    }

    // Helper method to get the details of a node
    private String getNodeDetails(ProductionNode node) {
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
}
