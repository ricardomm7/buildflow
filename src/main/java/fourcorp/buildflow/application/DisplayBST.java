package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.MaterialQuantityBST;
import fourcorp.buildflow.repository.Repositories;

import java.util.List;

public class DisplayBST {
    private MaterialQuantityBST bst = Repositories.getInstance().getMaterialBST();

    public void displayMaterialsByQuantity(boolean ascending) {
        List<ProductionNode> materials = ascending ? bst.getListInAscending() : bst.getListInDescending();

        System.out.println();
        for (ProductionNode material : materials) {
            System.out.println("- " + material.getId() + " " + material.getName() + " (QUANTITY: " + material.getQuantity() + ")");
        }
    }
}
