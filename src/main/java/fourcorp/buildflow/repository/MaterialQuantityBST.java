package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.ProductionNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialQuantityBST {
    private class Node {
        int quantity;
        List<ProductionNode> materials;
        Node left, right;

        Node(int quantity, ProductionNode material) {
            this.quantity = quantity;
            this.materials = new ArrayList<>();
            this.materials.add(material);
            this.left = null;
            this.right = null;
        }
    }

    private Node root;

    public MaterialQuantityBST() {
        root = null;
    }

    public void insert(ProductionNode material, int quantity) {
        root = insert(root, material, quantity);
    }

    private Node insert(Node node, ProductionNode material, int quantity) {
        if (node == null) {
            return new Node(quantity, material);
        }

        if (quantity < node.quantity) {
            node.left = insert(node.left, material, quantity);
        } else if (quantity > node.quantity) {
            node.right = insert(node.right, material, quantity);
        } else {
            node.materials.add(material);
        }

        return node;
    }

    public List<ProductionNode> getListInAscending() {
        List<ProductionNode> consolidatedList = consolidateMaterials();
        consolidatedList.sort((m1, m2) -> Double.compare(m1.getQuantity(), m2.getQuantity()));
        return consolidatedList;
    }

    private List<ProductionNode> consolidateMaterials() {
        Map<String, Double> materialMap = new HashMap<>();
        consolidateNodeMaterials(root, materialMap);

        List<ProductionNode> consolidatedList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : materialMap.entrySet()) {
            String id = entry.getKey();
            double totalQuantity = entry.getValue();

            String materialName = getMaterialNameById(id);

            ProductionNode node = new ProductionNode(id, materialName, true);
            node.setQuantity(totalQuantity);
            consolidatedList.add(node);
        }
        return consolidatedList;
    }

    private String getMaterialNameById(String id) {
        Node node = searchByNode(root, id);
        if (node != null && !node.materials.isEmpty()) {
            return node.materials.get(0).getName();
        }
        return "Unknown Material";
    }

    private Node searchByNode(Node node, String id) {
        if (node == null) {
            return null;
        }

        for (ProductionNode material : node.materials) {
            if (material.getId().equals(id)) {
                return node;
            }
        }

        Node foundNode = searchByNode(node.left, id);
        if (foundNode != null) return foundNode;
        return searchByNode(node.right, id);
    }

    private void consolidateNodeMaterials(Node node, Map<String, Double> materialMap) {
        if (node == null) {
            return;
        }

        for (ProductionNode material : node.materials) {
            materialMap.put(material.getId(), materialMap.getOrDefault(material.getId(), 0.0) + material.getQuantity());
        }

        consolidateNodeMaterials(node.left, materialMap);
        consolidateNodeMaterials(node.right, materialMap);
    }

    public List<ProductionNode> getListInDescending() {
        List<ProductionNode> consolidatedList = consolidateMaterials();
        consolidatedList.sort((m1, m2) -> Double.compare(m2.getQuantity(), m1.getQuantity()));
        return consolidatedList;
    }
}
