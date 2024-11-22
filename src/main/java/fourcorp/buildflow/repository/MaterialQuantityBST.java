package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.ProductionNode;

import java.util.*;

/**
 * The MaterialQuantityBST class implements a Binary Search Tree (BST) for managing production materials and their quantities.
 * Each node in the tree represents a unique material, with the possibility to store multiple `ProductionNode` objects
 * for materials with the same ID but different names.
 * This structure allows efficient insertion, quantity updates, and querying of materials sorted by quantity.
 */
public class MaterialQuantityBST {
    private class Node {
        double quantity;
        List<ProductionNode> materials;
        Node left, right;

        /**
         * Creates a new node with the given quantity and material.
         *
         * @param quantity The quantity of the material.
         * @param material The material represented by this node.
         */
        Node(double quantity, ProductionNode material) {
            this.quantity = quantity;
            this.materials = new ArrayList<>();
            this.materials.add(material);
            this.left = null;
            this.right = null;
        }
    }

    private Node root;

    /**
     * Constructs an empty MaterialQuantityBST.
     */
    public MaterialQuantityBST() {
        root = null;
    }

    /**
     * Inserts a new material into the tree or updates the quantity of an existing material.
     * If the material already exists, its quantity will be updated; otherwise, the material will be added as a new node.
     *
     * @param material The material to insert.
     * @param quantity The quantity of the material.
     */
    public void insert(ProductionNode material, double quantity) {
        if (material.isOperation()) return; // Skip operations, only materials are inserted
        root = insert(root, material, quantity);
    }

    /**
     * Helper method to recursively insert a material into the tree.
     *
     * @param node     The current node being examined.
     * @param material The material to insert.
     * @param quantity The quantity of the material.
     * @return The updated node.
     */
    private Node insert(Node node, ProductionNode material, double quantity) {
        if (node == null) {
            return new Node(quantity, material);  // Create new node if current is null
        }

        int compare = material.getId().compareTo(node.materials.get(0).getId());
        if (compare < 0) {
            node.left = insert(node.left, material, quantity);  // Insert in left subtree
        } else if (compare > 0) {
            node.right = insert(node.right, material, quantity);  // Insert in right subtree
        } else {
            // If material with the same ID exists, check by name and update quantity if necessary
            boolean found = false;
            for (ProductionNode existingMaterial : node.materials) {
                if (existingMaterial.getName().equals(material.getName())) {
                    existingMaterial.setQuantity(existingMaterial.getQuantity() + quantity);
                    found = true;
                    break;
                }
            }
            if (!found) {
                node.materials.add(material);  // Add new material if not found
                node.quantity += quantity;  // Update the total quantity in the node
            }
        }

        return node;
    }

    /**
     * Updates the quantity of an existing material in the tree.
     * If the material is not found, no changes will be made.
     *
     * @param material    The material whose quantity needs to be updated.
     * @param newQuantity The new quantity for the material.
     */
    public void updateQuantity(ProductionNode material, double newQuantity) {
        root = updateQuantity(root, material, newQuantity);
    }

    /**
     * Helper method to recursively update the quantity of a material in the tree.
     *
     * @param node        The current node being examined.
     * @param material    The material whose quantity needs to be updated.
     * @param newQuantity The new quantity for the material.
     * @return The updated node.
     */
    private Node updateQuantity(Node node, ProductionNode material, double newQuantity) {
        if (node == null) {
            return null;
        }

        int compare = material.getId().compareTo(node.materials.get(0).getId());
        if (compare < 0) {
            node.left = updateQuantity(node.left, material, newQuantity);  // Update in left subtree
        } else if (compare > 0) {
            node.right = updateQuantity(node.right, material, newQuantity);  // Update in right subtree
        } else {
            // Update the quantity of the material in the current node
            for (ProductionNode existingMaterial : node.materials) {
                if (existingMaterial.getId().equals(material.getId()) && existingMaterial.getName().equals(material.getName())) {
                    existingMaterial.setQuantity(newQuantity);
                    node.quantity = node.materials.stream().mapToDouble(ProductionNode::getQuantity).sum(); // Update node quantity
                    break;
                }
            }
        }
        return node;
    }

    /**
     * Returns a list of materials sorted in ascending order by their total quantity.
     * The list is consolidated to sum the quantities of materials with the same ID and name.
     *
     * @return A list of materials sorted by quantity in ascending order.
     */
    public List<ProductionNode> getListInAscending() {
        List<ProductionNode> consolidatedList = consolidateMaterials();
        consolidatedList.sort(Comparator.comparingDouble(ProductionNode::getQuantity));
        return consolidatedList;
    }

    /**
     * Consolidates all materials from the tree into a list, summing quantities for materials with the same ID and name.
     *
     * @return A list of consolidated materials.
     */
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

    /**
     * Helper method to recursively consolidate materials from the tree.
     * It accumulates the quantities of materials with the same ID and name.
     *
     * @param node        The current node in the tree.
     * @param materialMap The map that accumulates quantities by material ID and name.
     */
    private void consolidateNodeMaterials(Node node, Map<String, Map<String, Double>> materialMap) {
        if (node == null) {
            return;
        }

        // Add the materials in the current node to the map
        for (ProductionNode material : node.materials) {
            String id = material.getId();
            String name = material.getName();
            double quantity = material.getQuantity();

            materialMap
                    .computeIfAbsent(id, k -> new HashMap<>())
                    .merge(name, quantity, Double::sum);
        }

        // Recursively consolidate materials in left and right subtrees
        consolidateNodeMaterials(node.left, materialMap);
        consolidateNodeMaterials(node.right, materialMap);
    }

    /**
     * Returns a list of materials sorted in descending order by their total quantity.
     * The list is consolidated to sum the quantities of materials with the same ID and name.
     *
     * @return A list of materials sorted by quantity in descending order.
     */
    public List<ProductionNode> getListInDescending() {
        List<ProductionNode> consolidatedList = consolidateMaterials();
        consolidatedList.sort((m1, m2) -> Double.compare(m2.getQuantity(), m1.getQuantity())); // Sort in descending order
        return consolidatedList;
    }
}
