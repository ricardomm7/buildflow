package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Material;
import fourcorp.buildflow.domain.ProductionNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductionTree {
    private Map<String, ProductionNode> nodesMap;
    public List<ProductionNode> rootNodes;
    private final MaterialQuantityBST materialBST;

    public ProductionTree() {
        nodesMap = new HashMap<>();
        rootNodes = new ArrayList<>();
        materialBST = new MaterialQuantityBST();
    }

    public void insertNewOperationNode(String id, String name, double price, String parentId) {
        ProductionNode node = new ProductionNode(id, name, price);
        nodesMap.put(id, node);

        if (parentId.equalsIgnoreCase("root")) {
            rootNodes.add(node);
        } else {
            ProductionNode parentNode = nodesMap.get(parentId);
            if (parentNode != null) {
                parentNode.addChild(node);
            }
        }
    }

    public void insertNewMaterialNode(String id, String name, int quantity, double price, String operationId) {
        Material material = new Material(id, name, quantity, price);

        materialBST.addMaterial(material); // Adiciona o material na BST

        ProductionNode operationNode = nodesMap.get(operationId);
        if (operationNode != null) {
            ProductionNode materialNode = new ProductionNode(id, name, quantity, price);
            materialNode.setMaterial(true); // Marcar como material
            operationNode.addChild(materialNode);
            nodesMap.put(id, materialNode); // Adicionar ao mapa
        }
    }


    public void updateMaterialQuantity(String materialId, int newQuantity) {
        // Find the material node by ID
        ProductionNode materialNode = nodesMap.get(materialId);
        if (materialNode != null && materialNode.isMaterial()) {
            // Update the material's quantity
            materialNode.setQuantity(newQuantity);

            // Update the material in the material BST
            materialBST.updateMaterialQuantity(materialNode);

            // Cascade the quantity change to parent nodes if necessary
            cascadeQuantityUpdateToParents(materialNode);
        } else {
            System.out.println("Material not found or invalid material ID.");
        }
    }

    private void cascadeQuantityUpdateToParents(ProductionNode materialNode) {
        ProductionNode parentNode = materialNode.getParent();
        while (parentNode != null) {
            // Logic to update parent node based on the material change (e.g., cost recalculations)
            // Example: update operation's cost or total required material quantity

            // If parent node is an operation, you could implement logic to recalculate its cost or material needs
            if (!parentNode.isMaterial()) {
                // Example: Recalculate operation cost based on material updates
                parentNode.setCost(recalculateOperationCost(parentNode));
            }

            parentNode = parentNode.getParent();  // Move up to the next parent node
        }
    }

    private double recalculateOperationCost(ProductionNode operationNode) {
        double totalCost = 0;
        for (ProductionNode child : operationNode.getChildren()) {
            if (child.isMaterial()) {
                totalCost += child.getCost() * child.getQuantity();
            }
        }
        return totalCost;
    }


    public List<ProductionNode> getRootNodes() {
        return rootNodes;
    }

    public MaterialQuantityBST getMaterialBST() {
        return materialBST;
    }

    public String searchNodeByNameOrId(String identifier) {
        ProductionNode node = nodesMap.get(identifier);  // Primeiro tenta buscar pelo ID

        if (node == null) {
            // Se não encontrar pelo ID, tenta buscar pelo nome
            node = findNodeByName(identifier);
        }

        if (node == null) {
            return "Node not found.";
        }

        // Se encontrou o nó, monta a resposta
        StringBuilder result = new StringBuilder("Node Details:\n");
        result.append("ID: ").append(node.getId()).append("\n");
        result.append("Name: ").append(node.getName()).append("\n");
        result.append("Type: ").append(node.isMaterial() ? "Material" : "Operation").append("\n");

        if (node.isMaterial()) {
            result.append("Quantity: ").append(node.getQuantity()).append("\n");
            result.append("Parent Operation: ").append(node.getParent() != null ? node.getParent().getName() : "None").append("\n");
        }
        return result.toString();
    }

    private ProductionNode findNodeByName(String name) {
        for (ProductionNode node : nodesMap.values()) {
            if (node.getName().equalsIgnoreCase(name)) {
                return node;
            }
        }
        return null;  // Retorna null se não encontrar o nó pelo nome
    }

    public Map<String, ProductionNode> getNodesMap() {
        return new HashMap<>(nodesMap);
    }
}
