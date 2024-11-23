package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class USEI13 {

    private ProductionTree productionTree;

    @BeforeEach
    void setUp() {
        // Initialize a ProductionTree instance
        productionTree = new ProductionTree();
    }

    @Test
    void testAddNode() {
        // Create a new node
        ProductionNode node = new ProductionNode("1", "Material1", true);
        productionTree.addNode(node);

        // Verify that the node was added
        assertNotNull(productionTree.getNodeById("1"));
    }

    @Test
    void testSearchNodesById() {
        // Add nodes to the tree
        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        ProductionNode node2 = new ProductionNode("2", "Material2", true);
        productionTree.addNode(node1);
        productionTree.addNode(node2);

        // Test searching by ID
        List<ProductionNode> results = productionTree.searchNodes("1");
        assertEquals(1, results.size());
        assertEquals("Material1", results.get(0).getName());
    }

    @Test
    void testAddDependency() {
        // Add some nodes
        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        ProductionNode node2 = new ProductionNode("2", "Material2", true);
        productionTree.addNode(node1);
        productionTree.addNode(node2);

        // Add a dependency from node1 to node2
        productionTree.addDependency(node2, node1);

        // Verify that the dependency exists
        List<ProductionNode> parents = productionTree.getParentNodes(node2);
        assertTrue(parents.contains(node1));
    }

    @Test
    void testCriticalPath() {
        // Add nodes and dependencies
        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        ProductionNode node2 = new ProductionNode("2", "Material2", true);
        ProductionNode node3 = new ProductionNode("3", "Material3", true);

        productionTree.addNode(node1);
        productionTree.addNode(node2);
        productionTree.addNode(node3);

        productionTree.addDependency(node2, node1);
        productionTree.addDependency(node3, node2);

        // Get the critical path
        List<ProductionNode> criticalPath = productionTree.getCriticalPath();

        // Validate the critical path
        assertEquals(3, criticalPath.size());
        assertEquals("Material1", criticalPath.get(0).getName());
        assertEquals("Material2", criticalPath.get(1).getName());
        assertEquals("Material3", criticalPath.get(2).getName());
    }

    @Test
    void testUpdateConnectionsQuantity() {
        // Create nodes and add them
        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        ProductionNode node2 = new ProductionNode("2", "Material2", true);
        productionTree.addNode(node1);
        productionTree.addNode(node2);

        // Add a connection between nodes
        productionTree.addDependency(node2, node1);

        // Verify the connection before updating the quantity
        assertTrue(productionTree.getSubNodes(node1).containsKey(node2));

        // Update the connection quantity
        productionTree.updateConnectionsQuantity(node1, 200.0);

        // Verify that the updated quantity reflects correctly
        assertEquals(200.0, productionTree.getSubNodes(node1).get(node2), 0.01);
    }
}