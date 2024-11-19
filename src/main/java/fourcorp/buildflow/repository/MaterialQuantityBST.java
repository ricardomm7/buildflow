package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.ProductionNode;

import java.util.*;

public class MaterialQuantityBST {
    private class Node {
        double quantity;
        List<ProductionNode> materials;
        Node left, right;

        Node(double quantity, ProductionNode material) {
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

    public void insert(ProductionNode material, double quantity) {
        root = insert(root, material, quantity);
    }

    private Node insert(Node node, ProductionNode material, double quantity) {
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
        consolidatedList.sort(Comparator.comparingDouble(ProductionNode::getQuantity));
        return consolidatedList;
    }

    private List<ProductionNode> consolidateMaterials() {
        Map<String, Map<String, Double>> materialMap = new HashMap<>();
        consolidateNodeMaterials(root, materialMap);

        List<ProductionNode> consolidatedList = new ArrayList<>();
        for (Map.Entry<String, Map<String, Double>> idEntry : materialMap.entrySet()) {
            String id = idEntry.getKey();
            Map<String, Double> nameMap = idEntry.getValue();

            for (Map.Entry<String, Double> nameEntry : nameMap.entrySet()) {
                String name = nameEntry.getKey();
                double totalQuantity = nameEntry.getValue();

                ProductionNode node = new ProductionNode(id, name, true);
                node.setQuantity(totalQuantity);
                consolidatedList.add(node);
            }
        }
        return consolidatedList;
    }

    private void consolidateNodeMaterials(Node node, Map<String, Map<String, Double>> materialMap) {
        if (node == null) {
            return;
        }

        for (ProductionNode material : node.materials) {
            String id = material.getId();
            String name = material.getName();
            double quantity = material.getQuantity();

            materialMap
                    .computeIfAbsent(id, k -> new HashMap<>())
                    .merge(name, quantity, Double::sum);
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
