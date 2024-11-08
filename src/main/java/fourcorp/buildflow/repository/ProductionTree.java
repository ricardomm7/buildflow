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

        if (parentId.equals("root")) {
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
}
