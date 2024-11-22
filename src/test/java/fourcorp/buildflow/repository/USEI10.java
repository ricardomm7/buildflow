package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.ProductionNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class USEI10 {

    private MaterialQuantityBST materialQuantityBST;

    @BeforeEach
    void setUp() {
        materialQuantityBST = new MaterialQuantityBST();
    }

    @Test
    void testInsertSingleNode() {
        ProductionNode node = new ProductionNode("1", "Material1", true);
        node.setQuantity(10);

        materialQuantityBST.insert(node, 10);

        List<ProductionNode> ascendingList = materialQuantityBST.getListInAscending();
        assertEquals(1, ascendingList.size());
        assertEquals("Material1", ascendingList.get(0).getName());
        assertEquals(10, ascendingList.get(0).getQuantity(), 0.001);
    }

    @Test
    void testInsertMultipleNodesWithDifferentQuantities() {
        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        node1.setQuantity(10);

        ProductionNode node2 = new ProductionNode("2", "Material2", true);
        node2.setQuantity(5);

        ProductionNode node3 = new ProductionNode("3", "Material3", true);
        node3.setQuantity(20);

        materialQuantityBST.insert(node1, 10);
        materialQuantityBST.insert(node2, 5);
        materialQuantityBST.insert(node3, 20);

        List<ProductionNode> ascendingList = materialQuantityBST.getListInAscending();
        assertEquals(3, ascendingList.size());
        assertEquals("Material2", ascendingList.get(0).getName());
        assertEquals("Material1", ascendingList.get(1).getName());
        assertEquals("Material3", ascendingList.get(2).getName());
    }

    @Test
    void testInsertNodesWithSameQuantity() {
        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        node1.setQuantity(10);

        ProductionNode node2 = new ProductionNode("2", "Material2", true);
        node2.setQuantity(10);

        materialQuantityBST.insert(node1, 10);
        materialQuantityBST.insert(node2, 10);

        List<ProductionNode> ascendingList = materialQuantityBST.getListInAscending();
        assertEquals(2, ascendingList.size());
        assertTrue(ascendingList.stream().anyMatch(node -> node.getName().equals("Material1")));
        assertTrue(ascendingList.stream().anyMatch(node -> node.getName().equals("Material2")));
    }

    @Test
    void testConsolidateMaterials() {
        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        node1.setQuantity(10);

        ProductionNode node2 = new ProductionNode("1", "Material1", true); // Mesmo ID que node1
        node2.setQuantity(15);

        ProductionNode node3 = new ProductionNode("2", "Material2", true);
        node3.setQuantity(20);

        materialQuantityBST.insert(node1, 10);
        materialQuantityBST.insert(node2, 15);
        materialQuantityBST.insert(node3, 20);

        List<ProductionNode> consolidatedList = materialQuantityBST.getListInAscending();
        assertEquals(2, consolidatedList.size());

        ProductionNode material1 = consolidatedList.stream().filter(n -> n.getId().equals("1")).findFirst().get();
        assertEquals(25, material1.getQuantity(), 0.001); // Quantidade consolidada

        ProductionNode material2 = consolidatedList.stream().filter(n -> n.getId().equals("2")).findFirst().get();
        assertEquals(20, material2.getQuantity(), 0.001);
    }

    @Test
    void testUpdateQuantity() {
        ProductionNode node = new ProductionNode("1", "Material1", true);
        node.setQuantity(10.0);

        materialQuantityBST.insert(node, 10.0);

        materialQuantityBST.updateQuantity(node, 20.0);

        List<ProductionNode> ascendingList = materialQuantityBST.getListInAscending();
        assertEquals(1, ascendingList.size());
        assertEquals(20, ascendingList.get(0).getQuantity(), 0.001);
    }

    @Test
    void testGetListInDescending() {
        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        node1.setQuantity(30);

        ProductionNode node2 = new ProductionNode("2", "Material2", true);
        node2.setQuantity(15);

        materialQuantityBST.insert(node1, 30);
        materialQuantityBST.insert(node2, 15);

        List<ProductionNode> descendingList = materialQuantityBST.getListInDescending();
        assertEquals(2, descendingList.size());
        assertEquals("Material1", descendingList.get(0).getName());
        assertEquals("Material2", descendingList.get(1).getName());
    }

    @Test
    void testGetListWithNoMaterials() {
        List<ProductionNode> ascendingList = materialQuantityBST.getListInAscending();
        List<ProductionNode> descendingList = materialQuantityBST.getListInDescending();

        assertTrue(ascendingList.isEmpty());
        assertTrue(descendingList.isEmpty());
    }

    @Test
    void testDuplicateMaterialsDifferentNames() {
        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        node1.setQuantity(10);

        ProductionNode node2 = new ProductionNode("1", "Material1_Alt", true); // Mesmo ID, nome diferente
        node2.setQuantity(15);

        materialQuantityBST.insert(node1, 10);
        materialQuantityBST.insert(node2, 15);

        List<ProductionNode> ascendingList = materialQuantityBST.getListInAscending();
        assertEquals(2, ascendingList.size());
    }

    @Test
    void testInsertOperation() {
        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        node1.setQuantity(10);

        ProductionNode node2 = new ProductionNode("1", "Operation", false);
        node2.setQuantity(15);

        materialQuantityBST.insert(node1, 10);
        materialQuantityBST.insert(node2, 15);

        List<ProductionNode> ascendingList = materialQuantityBST.getListInAscending();
        assertEquals(1, ascendingList.size());
    }
}
