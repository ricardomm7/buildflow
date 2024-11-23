package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.ProductionNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class USEI13 {


    private ProductionTree productionTree;
    private ProductionNode nodeA;
    private ProductionNode nodeB;

    @BeforeEach
    void setUp() {
        productionTree = new ProductionTree();
        nodeA = new ProductionNode("1", "Node A", true);
        nodeB = new ProductionNode("2", "Node B", true);
        productionTree.addNode(nodeA);
        productionTree.addNode(nodeB);
    }

    @Test
    void updateConnectionsQuantity() {
        productionTree.addDependency(nodeB, nodeA);
        nodeA.setQuantity(10);

        // Initially, nodeB should not have a quantity assigned
        assertEquals(0.0, nodeB.getQuantity());

        // Update nodeA quantity and propagate changes to nodeB
        productionTree.updateConnectionsQuantity(nodeA, 20);

        // After update, nodeB's quantity should reflect the change
        assertEquals(20.0, nodeB.getQuantity(), 0.01);
    }
}
