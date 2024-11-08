package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

public class DisplayProductionTree {
    private ProductionTree p;

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
}
