package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.MaterialQuantityBST;
import fourcorp.buildflow.repository.ProductionTree;

import java.util.List;
import java.util.Scanner;

/**
 * The MaterialQuantityUpdater class provides functionality to update the quantity of materials
 * in the production system. It allows the user to search for materials by name or ID, select the material,
 * and update its quantity. The class interacts with both the ProductionTree and MaterialQuantityBST
 * repositories to ensure the changes are propagated correctly.
 */
public class MaterialQuantityUpdater {

    private final ProductionTree productionTree;
    private final MaterialQuantityBST materialQuantityBST;
    private final Scanner scanner;

    /**
     * Constructs a MaterialQuantityUpdater instance.
     *
     * @param productionTree      The ProductionTree containing the nodes (materials) to be updated.
     * @param materialQuantityBST The MaterialQuantityBST repository to manage the material quantities.
     */
    public MaterialQuantityUpdater(ProductionTree productionTree, MaterialQuantityBST materialQuantityBST) {
        this.productionTree = productionTree;
        this.materialQuantityBST = materialQuantityBST;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prompts the user to enter a material's name or ID, searches for matching materials, and allows
     * the user to update the quantity of the selected material.
     * <p>
     * Complexity: O(n²)
     */
    public void updateMaterialQuantity() {
        System.out.print("Enter the name or ID of the material to update: ");
        String searchQuery = scanner.nextLine();
        List<ProductionNode> searchResults = productionTree.searchNodes(searchQuery); // O(n)

        if (searchResults.isEmpty()) {
            System.out.println("No materials found matching the query.");
            return;
        }

        if (searchResults.size() > 1) {
            System.out.println("Multiple materials found. Please select the material you want to update:");
            for (int i = 0; i < searchResults.size(); i++) { // O(n)
                ProductionNode node = searchResults.get(i);
                System.out.printf("[%d] %s (ID: %s) | Current Quantity: %.2f%n", i + 1, node.getName(), node.getId(), node.getQuantity());
            }
            System.out.print("Enter the number corresponding to the material: ");
        } else {
            System.out.println("Material found:");
            ProductionNode node = searchResults.get(0);
            System.out.printf("%s (ID: %s) | Current Quantity: %.2f%n", node.getName(), node.getId(), node.getQuantity());

            if (confirmUpdate()) {
                updateQuantityForNode(searchResults.get(0)); // O(n²)
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
        updateQuantityForNode(selectedNode); // O(n²)
    }

    /**
     * Prompts the user to confirm if they want to update the material's quantity.
     * Ensures valid input ("y" or "n") is provided, and asks the user to try again for invalid inputs.
     * <p>
     * Complexity: O(n)
     *
     * @return true if the user confirms ("y"), false otherwise.
     */
    public boolean confirmUpdate() {
        while (true) { // O(n)
            System.out.print("Would you like to update this material's quantity? (y/n): ");
            String confirmation = scanner.nextLine().trim();
            if (confirmation.equalsIgnoreCase("y")) {
                return true;
            } else if (confirmation.equalsIgnoreCase("n")) {
                System.out.println("Update canceled.");
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
            }
        }
    }


    /**
     * Validates and retrieves the user's choice when selecting a material from a list.
     * <p>
     * Complexity: O(n)
     *
     * @param max The maximum number of available options.
     * @return The user's choice (1-based index), or -1 for invalid selection.
     */
    private int getUserChoice(int max) {
        while (true) { // O(n)
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

    /**
     * Updates the quantity of the selected material node and propagates the change
     * to the relevant data structures.
     * <p>
     * Complexity: O(n²)
     *
     * @param selectedNode The production node whose quantity is being updated.
     */
    void updateQuantityForNode(ProductionNode selectedNode) {
        double previousQuantity = selectedNode.getQuantity();

        double newQuantity = getNewQuantity();
        if (newQuantity == -1) {
            System.out.println("No changes were made to the material quantity.");
            return;
        }

        ProductionNode nodeToUpdate = productionTree.getNodeById(selectedNode.getId()); // O(n)
        if (nodeToUpdate == null) {
            System.err.println("Error: Node not found in the ProductionTree.");
            return;
        }

        productionTree.updateConnectionsQuantity(nodeToUpdate, newQuantity, materialQuantityBST); // O(n²)

        System.out.println("Quantity updated successfully!");
        System.out.printf("Updated quantity for '%s': %.2f (Previous: %.2f)%n", nodeToUpdate.getName(), nodeToUpdate.getQuantity(), previousQuantity);
    }

    /**
     * Continuously prompts the user to input a valid non-negative numeric quantity.
     * <p>
     * Complexity: O(n)
     *
     * @return The validated quantity entered by the user.
     */
    public double getNewQuantity() {
        while (true) { // O(n)
            System.out.print("Enter a new quantity (or type 'cancel' to exit): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("cancel")) {
                System.out.println("Operation canceled by user.");
                return -1;
            }
            try {
                double newQuantity = Double.parseDouble(input);
                if (newQuantity <= 0) {
                    System.out.println("Invalid input. Quantity must be greater than zero.");
                } else {
                    return newQuantity;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value greater than zero.");
            }
        }
    }
}
