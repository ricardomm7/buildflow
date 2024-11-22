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
        ProductionNode node = pt.getNodeById("1001");
        assertNotNull(node);
        assertEquals("bench leg w/hole", node.getName());
        assertTrue(node.isProduct());
    }

    @Test
    void testLoadSimpleOperations() throws IOException {
        ProductionTree pt = new ProductionTree();
        Reader.setAtt(pt);

        String filePath = "src/test/java/fourcorp/buildflow/operations_test.csv";
        Reader.loadSimpleOperations(filePath);

        ProductionNode operation = pt.getNodeById("11");
        assertNotNull(operation);
        assertEquals("assemble bench", operation.getName());
        assertFalse(operation.isProduct());
    }

    @Test
    void testLoadBOO() throws IOException {
        ProductionTree pt = new ProductionTree();
        Reader.setAtt(pt);

        String filePath = "src/test/java/fourcorp/buildflow/boo_test.csv"; // Substituir com o caminho correto do arquivo
        Reader.loadBOO(filePath);

        ProductionNode node20 = pt.getNodeById("20");
        assertNotNull(node20, "Node 20 should exist");

        Map<ProductionNode, Map<ProductionNode, Double>> connections = pt.getConnections();
        assertNotNull(connections, "Connections map should not be null");
        assertTrue(connections.containsKey(node20), "Connections should contain Node 20 as a key");

        Map<ProductionNode, Double> node20Connections = connections.get(node20);
        assertNotNull(node20Connections, "Connections for Node 20 should not be null");

        ProductionNode node1014 = pt.getNodeById("1014");
        assertNotNull(node1014, "Node 1014 should exist");

        assertTrue(node20Connections.containsKey(node1014), "Node 20 should have a connection to Node 1014");

        Double quantityFor1014 = node20Connections.get(node1014);
        assertNotNull(quantityFor1014, "Quantity for Node 1014 should not be null");
        assertEquals(0.125, quantityFor1014, "The quantity for Node 1014 should be 0.125");

        ProductionNode node1006 = pt.getNodeById("1006");
        assertNotNull(node1006, "Node 1006 should exist");

        assertTrue(connections.containsKey(node1006), "Connections should contain Node 1006 as a key");

        Map<ProductionNode, Double> node1006Connections = connections.get(node1006);
        assertNotNull(node1006Connections, "Connections for Node 1006 should not be null");
    }
}
