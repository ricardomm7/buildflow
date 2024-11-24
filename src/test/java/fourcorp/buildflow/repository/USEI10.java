package fourcorp.buildflow.repository;

import fourcorp.buildflow.application.Reader;
import fourcorp.buildflow.domain.ProductionNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class USEI10 {

    private MaterialQuantityBST materialQuantityBST;
    private ProductionTree productionTree;


    @BeforeEach
    void setUp() {
        materialQuantityBST = new MaterialQuantityBST();
        productionTree = new ProductionTree();
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

    @Test
    void testLoadItemsFromFile() throws IOException {
        Reader.setBST(materialQuantityBST);

        File tempFile = File.createTempFile("items", ".csv");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("ID;Name\n"); // Cabeçalho
            writer.write("1;Material1\n");
            writer.write("2;Material2\n");
            writer.write("3;Material3\n");
            writer.write("4;Material4\n");
            writer.write("5;Material5\n");
            writer.write("6;Material6\n");
        }

        Reader.loadItems(tempFile.getPath());

        List<ProductionNode> ascendingList = materialQuantityBST.getListInAscending();

        assertEquals(6, ascendingList.size());
        assertEquals("Material1", ascendingList.get(0).getName());
        assertEquals("Material2", ascendingList.get(1).getName());
        assertEquals("Material3", ascendingList.get(2).getName());
        assertEquals("Material4", ascendingList.get(3).getName());
        assertEquals("Material5", ascendingList.get(4).getName());
        assertEquals("Material6", ascendingList.get(5).getName());
    }

    @Test
    void testLoadItemsAndBOO_AscendingOrder() throws IOException {
        Reader.setBST(materialQuantityBST);
        Reader.setProdTree(productionTree);

        // Criar ficheiro temporário para itens
        File itemsFile = File.createTempFile("items", ".csv");
        itemsFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(itemsFile)) {
            writer.write("ID;Name\n"); // Cabeçalho
            writer.write("1001;Material1\n");
            writer.write("1002;Material2\n");
            writer.write("1003;Material3\n");
            writer.write("1004;Material4\n");
            writer.write("1005;Material5\n");
            writer.write("1006;Material6\n");
            writer.write("1007;Material7\n");
            writer.write("1014;Material8\n");
            writer.write("1013;Material9\n");
        }

        // Carregar os itens
        Reader.loadItems(itemsFile.getPath());

        // Criar ficheiro temporário para BOO
        File booFile = File.createTempFile("boo", ".csv");
        booFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(booFile)) {
            writer.write("op_id;item_id;item_qtd;(;op1;op_qtd1;op2;op_qtd2;opN;op_qtdN;);(;item_id1;item_qtd1;item_id2;item_qtd2;item_id3;item_qtd3;)\n");
            writer.write("20;1006;1;(;11;1;;;;;);(;1014;0.125;;;;;)\n");
            writer.write("11;1010;1;(;17;1;16;4;;;);(;;;;;;;)\n");
            writer.write("17;1004;1;(;15;1;;;;;);(;1007;4;1005;12;;;)\n");
            writer.write("16;1002;1;(;14;1;;;;;);(;1013;1;;;;;)\n");
        }

        // Criar ficheiro temporário para operações (operações do BOO)
        File operationsFile = File.createTempFile("operations", ".csv");
        operationsFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(operationsFile)) {
            writer.write("ID;Name\n"); // Cabeçalho
            writer.write("11;Operation11\n");
            writer.write("14;Operation14\n");
            writer.write("15;Operation15\n");
            writer.write("16;Operation16\n");
            writer.write("17;Operation17\n");
            writer.write("20;Operation20\n");
        }

        // Carregar as operações
        Reader.loadSimpleOperations(operationsFile.getPath());

        // Carregar o BOO
        Reader.loadBOO(booFile.getPath());

        // Obter lista ordenada da BST
        List<ProductionNode> ascendingList = materialQuantityBST.getListInAscending();

        // Verificar a quantidade e ordem dos materiais
        assertEquals(10, ascendingList.size()); // Todos os materiais

        // Verificar cada material em ordem crescente
        assertEquals("1003", ascendingList.get(0).getId());
        assertEquals(0.0, ascendingList.get(0).getQuantity(), 0.001);

        assertEquals("1001", ascendingList.get(1).getId());
        assertEquals(0, ascendingList.get(1).getQuantity(), 0.001);

        assertEquals("1014", ascendingList.get(2).getId());
        assertEquals(0.125, ascendingList.get(2).getQuantity(), 0.001);

        assertEquals("1006", ascendingList.get(3).getId());
        assertEquals(1.0, ascendingList.get(3).getQuantity(), 0.001);

        assertEquals("1004", ascendingList.get(4).getId());
        assertEquals(1.0, ascendingList.get(4).getQuantity(), 0.001);

        assertEquals("1013", ascendingList.get(5).getId());
        assertEquals(1.0, ascendingList.get(5).getQuantity(), 0.001);

        assertEquals("1002", ascendingList.get(6).getId());
        assertEquals(1.0, ascendingList.get(6).getQuantity(), 0.001);

        assertEquals("1010", ascendingList.get(7).getId());
        assertEquals(1.0, ascendingList.get(7).getQuantity(), 0.001);

        assertEquals("1007", ascendingList.get(8).getId());
        assertEquals(4.0, ascendingList.get(8).getQuantity(), 0.001);
    }

}
