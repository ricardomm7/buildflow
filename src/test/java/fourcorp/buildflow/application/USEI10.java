package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class USEI10 {
    private ProductionTreeSearcher searcher;
    private ProductionTree productionTree;

    @BeforeEach
    void setUp() {
        productionTree = new ProductionTree();

        ProductionNode operation1 = new ProductionNode("op1", "Assembly Operation", false);
        ProductionNode operation2 = new ProductionNode("op2", "Welding Operation", false);
        ProductionNode material1 = new ProductionNode("mat1", "Steel", true);
        ProductionNode material2 = new ProductionNode("mat2", "Bolts", true);

        productionTree.addNode(operation1);
        productionTree.addNode(operation2);
        productionTree.addNode(material1);
        productionTree.addNode(material2);

        productionTree.addDependency(material1, operation1);
        productionTree.addDependency(material2, operation1);
        productionTree.insertNewConnection("op1", "mat1", 5.0);
        productionTree.insertNewConnection("op1", "mat2", 10.0);

        Repositories.getInstance().setProductionTree(productionTree);

        searcher = new ProductionTreeSearcher();
    }

    @Test
    void handleNodeSearch() {
        String input = "Assembly Operation\n1\n"; // Search term followed by selection
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        searcher.handleNodeSearch();

        String output = outputStream.toString();
        assertTrue(output.contains("Assembly Operation"));
        assertTrue(output.contains("op1"));

        System.setIn(System.in);
        System.setOut(System.out);
    }

    @Test
    void searchNodeByNameOrId_SingleMatch() {
        String result = searcher.searchNodeByNameOrId("op1");

        assertTrue(result.contains("Assembly Operation"));
        assertTrue(result.contains("op1"));
        assertTrue(result.contains("Type: Operation"));
    }

    @Test
    void searchNodeByNameOrId_NoMatch() {
        String result = searcher.searchNodeByNameOrId("nonexistent");

        assertEquals("No matching nodes found.", result);
    }

    @Test
    void searchNodeByNameOrId_MultipleMatches() {
        ProductionNode operation3 = new ProductionNode("op3", "Assembly Operation 2", false);
        productionTree.addNode(operation3);

        String input = "1\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        String result = searcher.searchNodeByNameOrId("Assembly");

        assertTrue(result.contains("Assembly Operation"));
        assertTrue(result.contains("op1"));

        System.setIn(System.in);
    }

    @Test
    void searchNodeByNameOrId_InvalidChoice() {
        ProductionNode operation3 = new ProductionNode("op3", "Assembly Operation 2", false);
        productionTree.addNode(operation3);

        String input = "99\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        String result = searcher.searchNodeByNameOrId("Assembly");

        assertEquals("Invalid choice.", result);

        System.setIn(System.in);
    }
}


