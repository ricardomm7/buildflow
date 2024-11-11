package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Material;
import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.MaterialQuantityBST;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

public class DisplayProductionTree {
    private final ProductionTree p;

    public DisplayProductionTree() {
        p = Repositories.getInstance().getProductionTree();
    }

    public void displayProductionTrees() {
        System.out.println("\n");
        for (ProductionNode root : p.getRootNodes()) {
            displayProductionTree(root, "", root.getQuantity());
            System.out.println();
        }
    }

    private void displayProductionTree(ProductionNode node, String indent, int quant) {
        if (node.isMaterial()) {
            System.out.println(indent + "-> " + node.getName() + " (Qty: " + quant + ")");
        } else {
            System.out.println(indent + "-> " + node.getName());
        }

        for (ProductionNode child : node.getChildren()) {
            displayProductionTree(child, indent + "---", child.getQuantity());
        }
    }

    public void displayMaterialsByQuantity(boolean increasingOrder) {
        for (MaterialQuantityBST.Node node : p.getMaterialBST().getInOrder(increasingOrder)) {
            System.out.println("Quantity: " + node.getQuantity());
            for (Material material : node.getMaterials()) {
                System.out.println(" - Material: " + material.getName() + " (ID: " + material.getId() + ")");
            }
        }
    }

    public ProductionTree getProductionTree() {
        return p;
    }
}
