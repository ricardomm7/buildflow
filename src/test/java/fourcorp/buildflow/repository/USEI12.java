package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.ProductionNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class USEI12 {

    private ProductionTree productionTree;
    private ProductionNode nodeA;
    private ProductionNode nodeB;
    private MaterialQuantityBST materialQuantityBST;

    @BeforeEach
    void setUp() {
        productionTree = new ProductionTree();
        nodeA = new ProductionNode("1", "Node A", true);
        nodeB = new ProductionNode("2", "Node B", true);
        materialQuantityBST = new MaterialQuantityBST();
        productionTree.addNode(nodeA);
        productionTree.addNode(nodeB);
    }

    @Test
    void updateConnectionsQuantity() {
        productionTree.addDependencyBom(nodeA, nodeB, 2.0);

        nodeA.setQuantity(10);

        assertEquals(0.0, nodeB.getQuantity(), 0.01);

        productionTree.updateConnectionsQuantity(nodeA, nodeA.getQuantity(), materialQuantityBST);

        assertEquals(20.0, nodeB.getQuantity(), 0.01);
    }


    @Test
    void testPropagateQuantityUpdate() {
        ProductionNode nodeC = new ProductionNode("3", "Node C", true);
        productionTree.addNode(nodeC);

        productionTree.addDependencyBom(nodeA, nodeB, 2.0); // nodeA -> nodeB
        productionTree.addDependencyBom(nodeB, nodeC, 1.5); // nodeB -> nodeC

        nodeA.setQuantity(10);

        productionTree.updateConnectionsQuantity(nodeA, nodeA.getQuantity(), materialQuantityBST);

        assertEquals(20.0, nodeB.getQuantity(), 0.01);

        assertEquals(30.0, nodeC.getQuantity(), 0.01);
    }
}
