package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Material;

import java.util.ArrayList;
import java.util.List;

public class MaterialQuantityBST {
    private static Node root;

    public void addMaterial(Material material) {
        root = addMaterialRecursive(root, material);
    }

    private Node addMaterialRecursive(Node node, Material material) {
        if (node == null) {
            Node newNode = new Node(material.getQuantity());
            newNode.materials.add(material);
            return newNode;
        }
        if (material.getQuantity() < node.quantity) {
            node.left = addMaterialRecursive(node.left, material);
        } else if (material.getQuantity() > node.quantity) {
            node.right = addMaterialRecursive(node.right, material);
        } else {
            node.materials.add(material);
        }
        return node;
    }

    private class Node {
        int quantity;
        List<Material> materials;
        Node left, right;

        Node(int quantity) {
            this.quantity = quantity;
            this.materials = new ArrayList<>();
        }
    }
}