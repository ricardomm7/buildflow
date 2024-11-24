package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.MaterialQuantityBST;
import fourcorp.buildflow.repository.ProductionTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class USEI12 {

    private ProductionTree productionTree;

    @BeforeEach
    void setUp() {
        productionTree = new ProductionTree();
    }

    @Test
    void testAddNode() {
        ProductionNode node = new ProductionNode("1", "Material1", true);
        productionTree.addNode(node);

        ProductionNode foundNode = productionTree.getNodeById("1");
        assertNotNull(foundNode);
        assertEquals("Material1", foundNode.getName());
    }


    @Test
    void testSearchNodesById() {
        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        ProductionNode node2 = new ProductionNode("2", "Material2", true);
        productionTree.addNode(node1);
        productionTree.addNode(node2);

        List<ProductionNode> results = productionTree.searchNodes("1");
        assertEquals(1, results.size());
        assertEquals("Material1", results.get(0).getName());
    }

    @Test
    void testAddDependency() {
        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        ProductionNode node2 = new ProductionNode("2", "Material2", true);
        productionTree.addNode(node1);
        productionTree.addNode(node2);

        productionTree.addDependency(node2, node1);

        List<ProductionNode> parents = productionTree.getParentNodes(node2);
        assertTrue(parents.contains(node1));
    }


    @Test
    void testUpdateConnectionsQuantity() {

        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        ProductionNode node2 = new ProductionNode("2", "Material2", true);
        productionTree.addNode(node1);
        productionTree.addNode(node2);

        productionTree.addDependency(node2, node1);

        assertTrue(productionTree.getSubNodes(node1).containsKey(node2));

        double newQuantity = 200.0;
        MaterialQuantityBST materialQuantityBST = new MaterialQuantityBST();
        productionTree.updateConnectionsQuantity(node1, newQuantity, materialQuantityBST);

        assertEquals(newQuantity, productionTree.getSubNodes(node1).get(node2), 0.01);
    }
}