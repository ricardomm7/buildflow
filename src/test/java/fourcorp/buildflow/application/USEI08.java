package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class USEI08 {

    @Test
    void testLoadItems() throws IOException {
        ProductionTree pt = new ProductionTree();
        Reader.setAtt(pt);

        String filePath = "src/test/java/fourcorp/buildflow/items_test.csv"; // Substituir com o caminho correto do arquivo
        Reader.loadItems(filePath);

        // Validar se os itens foram carregados corretamente
        ProductionNode node = pt.getNodeById("2001");
        assertNotNull(node, "Node 2001 should exist");
        assertEquals("tábua de madeira", node.getName(), "Node 2001 name should match");
        assertTrue(node.isProduct(), "Node 2001 should be a product");

        ProductionNode node2008 = pt.getNodeById("2008");
        assertNotNull(node2008, "Node 2008 should exist");
        assertEquals("tinta", node2008.getName(), "Node 2008 name should match");
        assertTrue(node2008.isProduct(), "Node 2008 should be a product");
    }

    @Test
    void testLoadSimpleOperations() throws IOException {
        ProductionTree pt = new ProductionTree();
        Reader.setAtt(pt);

        String filePath = "src/test/java/fourcorp/buildflow/operations_test.csv"; // Substituir com o caminho correto do arquivo
        Reader.loadSimpleOperations(filePath);

        // Validar se as operações foram carregadas corretamente
        ProductionNode operation1 = pt.getNodeById("1");
        assertNotNull(operation1, "Operation 1 should exist");
        assertEquals("cortar tábua de madeira", operation1.getName(), "Operation 1 name should match");
        assertFalse(operation1.isProduct(), "Operation 1 should not be a product");

        ProductionNode operation5 = pt.getNodeById("5");
        assertNotNull(operation5, "Operation 5 should exist");
        assertEquals("pintar caixa", operation5.getName(), "Operation 5 name should match");
        assertFalse(operation5.isProduct(), "Operation 5 should not be a product");
    }

    @Test
    void testLoadBOO() throws IOException {
        ProductionTree pt = new ProductionTree();
        Reader.setAtt(pt);

        String filePath = "src/test/java/fourcorp/buildflow/boo_test.csv"; // Substituir com o caminho correto do arquivo
        Reader.loadBOO(filePath);

        // Validar conexões para a operação 1 (cortar tábua de madeira)
        ProductionNode operation1 = pt.getNodeById("1");
        assertNotNull(operation1, "Operation 1 should exist");

        Map<ProductionNode, Map<ProductionNode, Double>> connections = pt.getConnections();
        assertNotNull(connections, "Connections map should not be null");
        assertTrue(connections.containsKey(operation1), "Connections should contain Operation 1 as a key");

        Map<ProductionNode, Double> operation1Connections = connections.get(operation1);
        assertNotNull(operation1Connections, "Connections for Operation 1 should not be null");

        ProductionNode item2001 = pt.getNodeById("2001");
        assertNotNull(item2001, "Item 2001 should exist");
        assertTrue(operation1Connections.containsKey(item2001), "Operation 1 should have a connection to Item 2001");

        Double quantityFor2001 = operation1Connections.get(item2001);
        assertNotNull(quantityFor2001, "Quantity for Item 2001 should not be null");
        assertEquals(2.0, quantityFor2001, "The quantity for Item 2001 should be 2.0");

        // Validar conexões para a operação 5 (pintar caixa)
        ProductionNode operation5 = pt.getNodeById("5");
        assertNotNull(operation5, "Operation 5 should exist");

        assertTrue(connections.containsKey(operation5), "Connections should contain Operation 5 as a key");

        Map<ProductionNode, Double> operation5Connections = connections.get(operation5);
        assertNotNull(operation5Connections, "Connections for Operation 5 should not be null");

        ProductionNode item2008 = pt.getNodeById("2008");
        assertNotNull(item2008, "Item 2008 should exist");
        assertTrue(operation5Connections.containsKey(item2008), "Operation 5 should have a connection to Item 2008");

        Double quantityFor2008 = operation5Connections.get(item2008);
        assertNotNull(quantityFor2008, "Quantity for Item 2008 should not be null");
        assertEquals(0.5, quantityFor2008, "The quantity for Item 2008 should be 0.5");
    }
}
