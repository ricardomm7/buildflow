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
     */
    public void updateMaterialQuantity() {
        System.out.print("Enter the name or ID of the material to update: "); // O(1)
        String searchQuery = scanner.nextLine(); // O(1)
        List<ProductionNode> searchResults = productionTree.searchNodes(searchQuery); // O(n)

        if (searchResults.isEmpty()) { // O(1)
            System.out.println("No materials found matching the query."); // O(1)
            return; // O(1)
        }

        if (searchResults.size() > 1) { // O(1)
            System.out.println("Multiple materials found. Please select the material you want to update:"); // O(1)
            for (int i = 0; i < searchResults.size(); i++) { // O(n)
                ProductionNode node = searchResults.get(i); // O(1)
                System.out.printf("[%d] %s (ID: %s) | Current Quantity: %.2f%n", i + 1, node.getName(), node.getId(), node.getQuantity()); // O(1)
            }
            System.out.print("Enter the number corresponding to the material: "); // O(1)
        } else { // O(1)
            System.out.println("Material found:"); // O(1)
            ProductionNode node = searchResults.get(0); // O(1)
            System.out.printf("%s (ID: %s) | Current Quantity: %.2f%n", node.getName(), node.getId(), node.getQuantity()); // O(1)

            // Confirmation step with retry on invalid input
            if (confirmUpdate()) { // O(n)
                updateQuantityForNode(searchResults.get(0)); // O(n) for searching & updating node in ProductionTree, O(n) for updating MaterialQuantityBST, O(n) for updating connections in ProductionTree
            }
            return; // O(1)
        }

        int choice = getUserChoice(searchResults.size()); // O(n)
        if (choice == -1) { // O(1)
            System.out.println("Invalid selection. Returning to main menu."); // O(1)
            return; // O(1)
        }

        ProductionNode selectedNode = searchResults.get(choice - 1); // O(1)

        System.out.printf("Current quantity of '%s': %.2f%n", selectedNode.getName(), selectedNode.getQuantity()); // O(1)
        updateQuantityForNode(selectedNode); // O(n)
    }

    /**
     * Prompts the user to confirm if they want to update the material's quantity.
     * Ensures valid input ("y" or "n") is provided, and asks the user to try again for invalid inputs.
     *
     * @return true if the user confirms ("y"), false otherwise.
     */
    public boolean confirmUpdate() {
        while (true) { // O(n)
            System.out.print("Would you like to update this material's quantity? (y/n): "); // O(1)
            String confirmation = scanner.nextLine().trim(); // O(1)
            if (confirmation.equalsIgnoreCase("y")) { // O(1)
                return true; // User confirmed update
            } else if (confirmation.equalsIgnoreCase("n")) { // O(1)
                System.out.println("Update canceled."); // O(1)
                return false; // User declined update
            } else { // O(1)
                System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no."); // O(1)
            }
        }
    }



    /**
     * Validates and retrieves the user's choice when selecting a material from a list.
     *
     * @param max The maximum number of available options.
     * @return The user's choice (1-based index), or -1 for invalid selection.
     */
    private int getUserChoice(int max) {
        while (true) { // O(n)
            try {
                int choice = Integer.parseInt(scanner.nextLine()); // O(1)
                if (choice >= 1 && choice <= max) { // O(1)
                    return choice; // O(1)
                }
                System.out.print("Invalid selection. Try again: "); // O(1)
            } catch (NumberFormatException e) { // O(1)
                System.out.print("Please enter a valid number: "); // O(1)
            }
        }
    }

    /**
     * Updates the quantity of the selected material node and propagates the change
     * to the relevant data structures.
     *
     * @param selectedNode The production node whose quantity is being updated.
     */
    void updateQuantityForNode(ProductionNode selectedNode) {
        double previousQuantity = selectedNode.getQuantity(); // O(1)

        // Prompt user for a valid quantity
        double newQuantity = getNewQuantity(); // O(n)
        if (newQuantity == -1) { // Check for cancellation
            System.out.println("No changes were made to the material quantity."); // O(1)
            return; // O(1)
        }

        // Ensure the correct node is retrieved from ProductionTree
        ProductionNode nodeToUpdate = productionTree.getNodeById(selectedNode.getId()); // O(n)
        if (nodeToUpdate == null) { // O(1)
            System.err.println("Error: Node not found in the ProductionTree."); // O(1)
            return; // O(1)
        }

        productionTree.updateConnectionsQuantity(nodeToUpdate, newQuantity, materialQuantityBST); // O(n)

        System.out.println("Quantity updated successfully!"); // O(1)
        System.out.printf("Updated quantity for '%s': %.2f (Previous: %.2f)%n", nodeToUpdate.getName(), nodeToUpdate.getQuantity(), previousQuantity); // O(1)
    }

    /**
     * Continuously prompts the user to input a valid non-negative numeric quantity.
     *
     * @return The validated quantity entered by the user.
     */
    public double getNewQuantity() {
        while (true) { // O(n)
            System.out.print("Enter a new quantity (or type 'cancel' to exit): "); // O(1)
            String input = scanner.nextLine(); // O(1)
            if (input.equalsIgnoreCase("cancel")) { // O(1)
                System.out.println("Operation canceled by user."); // O(1)
                return -1; // Indicates cancellation
            }
            try {
                double newQuantity = Double.parseDouble(input); // O(1)
                if (newQuantity <= 0) { // O(1)
                    System.out.println("Invalid input. Quantity must be greater than zero."); // O(1)
                } else { // O(1)
                    return newQuantity; // O(1)
                }
            } catch (NumberFormatException e) { // O(1)
                System.out.println("Invalid input. Please enter a numeric value greater than zero."); // O(1)
            }
        }
    }
}
