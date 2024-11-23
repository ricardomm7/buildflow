package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.MaterialQuantityBST;
import fourcorp.buildflow.repository.Repositories;

import java.util.List;

/**
 * The DisplayBST class is responsible for displaying a list of materials, ordered by their quantity,
 * either in ascending or descending order. The materials are fetched from a Binary Search Tree (BST).
 */
public class DisplayBST {
    private MaterialQuantityBST bst = Repositories.getInstance().getMaterialBST();

    /**
     * Displays the materials sorted by their quantity in either ascending or descending order.
     * The complexity of this method is: O(n^2).
     *
     * @param ascending A boolean indicating the sorting order. If true, materials are displayed in ascending order;
     *                  if false, they are displayed in descending order.
     */
    public void displayMaterialsByQuantity(boolean ascending) {
        List<ProductionNode> materials = ascending ? bst.getListInAscending() : bst.getListInDescending(); // O(n^2)

        System.out.println();
        for (ProductionNode material : materials) { // O(n)
            System.out.println("- " + material.getId() + " " + material.getName() + " (QUANTITY: " + material.getQuantity() + ")");
        }
    }
}
