package fourcorp.buildflow.repository;

import fourcorp.buildflow.application.Reader;
import fourcorp.buildflow.domain.ProductionNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class USEI08 {
    private ProductionTree productionTree;

    @BeforeEach
    void setUp() {
        productionTree = new ProductionTree();
    }

    @Test
    void testInsertProductionNode() {
        productionTree.insertProductionNode("1", "Node1", true);
        ProductionNode node = productionTree.getNodeById("1");

        assertNotNull(node);
        assertEquals("Node1", node.getName());
        assertTrue(node.isProduct());
    }

    @Test
    void testInsertDuplicateNode() {
        productionTree.insertProductionNode("1", "Node1", true);
        productionTree.insertProductionNode("1", "DuplicateNode", false);

        ProductionNode node = productionTree.getNodeById("1");
        assertNotNull(node);
        assertEquals("Node1", node.getName());
    }

    @Test
    void testInsertNewConnection() {
        productionTree.insertProductionNode("1", "Parent", true);
        productionTree.insertProductionNode("2", "Child", false);

        productionTree.insertNewConnection("1", "2", 5.0);

        Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(productionTree.getNodeById("1"));
        assertEquals(1, subNodes.size());
        assertEquals(5.0, subNodes.get(productionTree.getNodeById("2")));
    }

    @Test
    void testInsertConnectionWithInvalidNodes() {
        productionTree.insertNewConnection("1", "2", 5.0);

        Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(productionTree.getNodeById("1"));
        assertTrue(subNodes.isEmpty());
    }

    @Test
    void testGetParentNodes() {
        productionTree.insertProductionNode("1", "Parent", true);
        productionTree.insertProductionNode("2", "Child", false);

        productionTree.insertNewConnection("1", "2", 5.0);

        List<ProductionNode> parents = productionTree.getParentNodes(productionTree.getNodeById("2"));
        assertEquals(1, parents.size());
        assertEquals("Parent", parents.get(0).getName());
    }

    @Test
    void testSearchNodesById() {
        productionTree.insertProductionNode("1", "Node1", true);
        productionTree.insertProductionNode("2", "Node2", false);

        List<ProductionNode> results = productionTree.searchNodes("1");
        assertEquals(1, results.size());
        assertEquals("Node1", results.get(0).getName());
    }

    @Test
    void testSearchNodesByName() {
        productionTree.insertProductionNode("1", "AlphaNode", true);
        productionTree.insertProductionNode("2", "BetaNode", false);

        List<ProductionNode> results = productionTree.searchNodes("beta");
        assertEquals(1, results.size());
        assertEquals("BetaNode", results.get(0).getName());
    }

    @Test
    void testSearchNodesWithNoMatch() {
        productionTree.insertProductionNode("1", "AlphaNode", true);

        List<ProductionNode> results = productionTree.searchNodes("Gamma");
        assertTrue(results.isEmpty());
    }

    @Test
    void testGetSubNodes() {
        productionTree.insertProductionNode("1", "Parent", true);
        productionTree.insertProductionNode("2", "Child1", false);
        productionTree.insertProductionNode("3", "Child2", false);

        productionTree.insertNewConnection("1", "2", 3.0);
        productionTree.insertNewConnection("1", "3", 2.0);

        Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(productionTree.getNodeById("1"));
        assertEquals(2, subNodes.size());
        assertEquals(3.0, subNodes.get(productionTree.getNodeById("2")));
        assertEquals(2.0, subNodes.get(productionTree.getNodeById("3")));
    }

    @Test
    void testGetAllNodes() {
        productionTree.insertProductionNode("1", "Node1", true);
        productionTree.insertProductionNode("2", "Node2", false);

        List<ProductionNode> allNodes = productionTree.getAllNodes();
        assertEquals(2, allNodes.size());
    }

    @Test
    void testLoadItemsEmptyFile() throws IOException {
        String filePath = "empty.csv";
        Files.writeString(Path.of(filePath), "id;name\n"); // Apenas cabeçalho

        Reader.loadItems(filePath);

        List<ProductionNode> allNodes = productionTree.getAllNodes();
        assertTrue(allNodes.isEmpty());

        Files.delete(Path.of(filePath));
    }
}