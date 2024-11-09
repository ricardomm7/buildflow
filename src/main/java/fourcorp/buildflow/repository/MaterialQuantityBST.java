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

    public List<Node> getInOrder(boolean ascending) {
        List<Node> nodesList = new ArrayList<>();
        if (ascending) {
            getAscending(root, nodesList);
        } else {
            getDescending(root, nodesList);
        }
        return nodesList;
    }

    private void getAscending(Node node, List<Node> nodesList) {
        if (node != null) {
            getAscending(node.left, nodesList);
            nodesList.add(node);
            getAscending(node.right, nodesList);
        }
    }

    private void getDescending(Node node, List<Node> nodesList) {
        if (node != null) {
            getDescending(node.right, nodesList);
            nodesList.add(node);
            getDescending(node.left, nodesList);
        }
    }

    public class Node {
        private int quantity;
        private List<Material> materials;
        Node left, right;

        Node(int quantity) {
            this.quantity = quantity;
            this.materials = new ArrayList<>();
        }

        public int getQuantity() {
            return quantity;
        }

        public List<Material> getMaterials() {
            return new ArrayList<>(materials);
        }
    }
}