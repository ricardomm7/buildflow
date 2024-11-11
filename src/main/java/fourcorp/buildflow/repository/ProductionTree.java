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
            return "Nó não encontrado";
        }

        // Se encontrou o nó, monta a resposta
        StringBuilder result = new StringBuilder("Detalhes do Nó:\n");
        result.append("ID: ").append(node.getId()).append("\n");
        result.append("Nome: ").append(node.getName()).append("\n");
        result.append("Tipo: ").append(node.isMaterial() ? "Material" : "Operação").append("\n");

        if (node.isMaterial()) {
            result.append("Quantidade: ").append(node.getQuantity()).append("\n");
            result.append("Operação Pai: ").append(node.getParent() != null ? node.getParent().getName() : "Nenhuma").append("\n");
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
}
