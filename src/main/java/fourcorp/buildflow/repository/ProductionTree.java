package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.ProductionNode;

import java.util.HashMap;
import java.util.Map;

public class ProductionTree {
    private Map<String, ProductionNode> productionNodes;

    public ProductionTree() {
        productionNodes = new HashMap<>();
    }

    public void insertProductionNode(String id, String name, boolean isProduct) {
        productionNodes.putIfAbsent(id, new ProductionNode(id, name, isProduct));
    }

    public void insertNewNode(String itemId, String opId) {
        ProductionNode item = getNode(itemId);
        ProductionNode operation = getNode(opId);

        if (item != null && operation != null) {
            item.addSubNode(operation, 1);  // Associando o item com a operação
        } else {
            System.err.println("Item ou Operação não encontrada: " + itemId + " ou " + opId);
        }
    }

    public void insertSubitemToNode(String nodeId, String subitemId, int quantity) {
        ProductionNode node = getNode(nodeId);
        ProductionNode subitem = getNode(subitemId);

        if (node != null && subitem != null) {
            node.addSubNode(subitem, quantity);
        } else {
            System.err.println("Node ou Subnode não encontrado: " + nodeId + " ou " + subitemId);
        }
    }

    private ProductionNode getNode(String nodeId) {
        return productionNodes.get(nodeId);
    }

    public Map<String, ProductionNode> getAllProductionNodes() {
        return productionNodes;
    }
}
