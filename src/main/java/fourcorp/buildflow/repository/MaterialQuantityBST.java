package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Material;
import fourcorp.buildflow.domain.ProductionNode;

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

    public void updateMaterialQuantity(ProductionNode materialNode) {
        // First, remove the old material from the BST
        root = removeMaterialByQuantity(root, materialNode.getQuantity(), materialNode);

        // Then, insert the updated material with the new quantity
        addMaterial(new Material(materialNode.getId(), materialNode.getName(), materialNode.getQuantity(), materialNode.getCost()));
    }

    private Node removeMaterialByQuantity(Node node, int oldQuantity, ProductionNode materialNode) {
        if (node == null) {
            return null;
        }

        if (oldQuantity < node.quantity) {
            node.left = removeMaterialByQuantity(node.left, oldQuantity, materialNode);
        } else if (oldQuantity > node.quantity) {
            node.right = removeMaterialByQuantity(node.right, oldQuantity, materialNode);
        } else {
            // Found the node, now remove it
            node.materials.removeIf(m -> m.getId().equals(materialNode.getId()));

            // If the node has no more materials, remove it from the BST
            if (node.materials.isEmpty()) {
                return removeNode(node);
            }
        }
        return node;
    }

    private Node removeNode(Node node) {
        if (node.left == null) return node.right;
        if (node.right == null) return node.left;

        Node minNode = findMin(node.right);
        node.quantity = minNode.quantity;
        node.materials = minNode.materials;
        node.right = removeMaterialByQuantity(node.right, minNode.quantity, null);
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
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