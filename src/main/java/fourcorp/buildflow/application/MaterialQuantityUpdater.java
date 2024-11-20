package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.MaterialQuantityBST;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MaterialQuantityUpdater {
    private final ProductionTree productionTree;
    private final MaterialQuantityBST materialQuantityBST;
    private final Scanner scanner;

    public MaterialQuantityUpdater(ProductionTree productionTree, MaterialQuantityBST materialQuantityBST) {
        this.productionTree = productionTree;
        this.materialQuantityBST = materialQuantityBST;
        this.scanner = new Scanner(System.in);
    }

    public void updateMaterialQuantity() {
        System.out.print("Enter the name or ID of the material to update: ");
        String searchQuery = scanner.nextLine();
        List<ProductionNode> searchResults = productionTree.searchNodes(searchQuery);

        if (searchResults.isEmpty()) {
            System.out.println("No materials found matching the query.");
            return;
        }

        if (searchResults.size() > 1) {
            System.out.println("Multiple materials found. Please select the material you want to update:");
            for (int i = 0; i < searchResults.size(); i++) {
                ProductionNode node = searchResults.get(i);
                System.out.printf("[%d] %s (ID: %s) | Current Quantity: %.2f%n", i + 1, node.getName(), node.getId(), node.getQuantity());
            }
            System.out.print("Enter the number corresponding to the material: ");
        } else {
            System.out.println("Material found:");
            ProductionNode node = searchResults.get(0);
            System.out.printf("%s (ID: %s) | Current Quantity: %.2f%n", node.getName(), node.getId(), node.getQuantity());
            System.out.print("Would you like to update this material's quantity? (y/n): ");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("y")) {
                updateQuantityForNode(searchResults.get(0));
            }
            return;
        }

        int choice = getUserChoice(searchResults.size());
        if (choice == -1) {
            System.out.println("Invalid selection. Returning to main menu.");
            return;
        }

        ProductionNode selectedNode = searchResults.get(choice - 1);

        System.out.printf("Current quantity of '%s': %.2f%n", selectedNode.getName(), selectedNode.getQuantity());
        updateQuantityForNode(selectedNode);
    }

    private int getUserChoice(int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= max) {
                    return choice;
                }
                System.out.print("Invalid selection. Try again: ");
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    private void updateQuantityForNode(ProductionNode selectedNode) {
        System.out.print("Enter the new quantity: ");
        double newQuantity = getNewQuantity();
        double previousQuantity = selectedNode.getQuantity();

        // Ensure the correct node is retrieved from ProductionTree
        ProductionNode nodeToUpdate = productionTree.getNodeById(selectedNode.getId());
        if (nodeToUpdate == null) {
            System.err.println("Error: Node not found in the ProductionTree.");
            return;
        }

        // Update the quantity in MaterialQuantityBST
        materialQuantityBST.updateQuantity(nodeToUpdate, newQuantity);

        // Update the quantity in the ProductionTree and the connections
        updateConnectionsQuantity(nodeToUpdate, newQuantity);

        productionTree.updateNodeQuantity(nodeToUpdate, newQuantity);

        System.out.println("Quantity updated successfully!");
        System.out.printf("Updated quantity for '%s': %.2f (Previous: %.2f)%n", nodeToUpdate.getName(), nodeToUpdate.getQuantity(), previousQuantity);
    }

    private double getNewQuantity() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a numeric value: ");
            }
        }
    }

    private void updateConnectionsQuantity(ProductionNode nodeToUpdate, double newQuantity) {
        // Update the material quantity in the production tree connections
        for (Map.Entry<ProductionNode, Map<ProductionNode, Double>> entry : productionTree.getConnections().entrySet()) {
            Map<ProductionNode, Double> subNodes = entry.getValue();
            for (Map.Entry<ProductionNode, Double> subEntry : subNodes.entrySet()) {
                ProductionNode dependentNode = subEntry.getKey();
                if (dependentNode.getId().equals(nodeToUpdate.getId())) {
                    // Here you can update the quantities of the dependent nodes accordingly if needed
                    // You may want to scale or adjust the quantities based on the new value
                    subEntry.setValue(newQuantity); // This is a simple example to show updating the value
                }
            }
        }
    }
}
