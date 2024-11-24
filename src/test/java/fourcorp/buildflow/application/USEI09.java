package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class USEI09 {
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
        String input = "Assembly Operation\n1\n";
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

    @Test
    void testMapAssociation_AC1() {
        productionTree = new ProductionTree();
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode op1 = new ProductionNode("op1", "Operation 1", false);
        ProductionNode mat1 = new ProductionNode("mat1", "Material 1", true);
        ProductionNode mat2 = new ProductionNode("mat2", "Material 2", true);

        productionTree.addNode(op1);
        productionTree.addNode(mat1);
        productionTree.addNode(mat2);

        productionTree.addDependencyBom(op1, mat1, 5.0);
        productionTree.addDependencyBom(op1, mat2, 10.0);

        Map<ProductionNode, Map<ProductionNode, Double>> connections = productionTree.getConnections();
        assertTrue(connections.containsKey(op1));
        assertEquals(2, connections.get(op1).size());

        Map<ProductionNode, Double> op1Connections = connections.get(op1);
        assertEquals(5.0, op1Connections.get(mat1));
        assertEquals(10.0, op1Connections.get(mat2));
    }

    @Test
    void testCustomTreeConstruction_AC1() {
        ProductionTree productionTree = new ProductionTree();
        ProductionNode op1 = new ProductionNode("1", "Cortar Tábua", false);
        ProductionNode op2 = new ProductionNode("2", "Montar Estrutura", false);
        ProductionNode mat1 = new ProductionNode("101", "Tábua de Madeira", true);
        ProductionNode mat2 = new ProductionNode("102", "Estrutura Montada", true);

        productionTree.addNode(op1);
        productionTree.addNode(op2);
        productionTree.addNode(mat1);
        productionTree.addNode(mat2);

        productionTree.addDependencyBom(op1, mat1, 2.0);
        productionTree.addDependencyBom(op2, mat2, 1.0);
        productionTree.addDependencyBom(op2, op1, 1.0);

        assertEquals(2, productionTree.getSubNodes(op2).size());
        assertTrue(productionTree.getSubNodes(op2).containsKey(op1));
        assertTrue(productionTree.getSubNodes(op2).containsKey(mat2));
    }

    @Test
    void testCustomSearchFunctionality_AC2() {
        ProductionTree productionTree = new ProductionTree();
        ProductionNode op1 = new ProductionNode("2", "Lavar", false);
        ProductionNode mat1 = new ProductionNode("101", "Prato", true);

        productionTree.addNode(op1);
        productionTree.addNode(mat1);
        productionTree.addDependencyBom(op1, mat1, 3.0);

        List<ProductionNode> searchById = productionTree.searchNodes("2");
        assertEquals(1, searchById.size());
        assertEquals(op1, searchById.get(0));
        assertTrue(searchById.contains(op1));


        List<ProductionNode> searchByName = productionTree.searchNodes("Prato");
        assertEquals(1, searchByName.size());
        assertTrue(searchByName.contains(mat1));
    }


    @Test
    void testSearchByNameOrId_AC2() {
        productionTree = new ProductionTree();
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode op1 = new ProductionNode("op1", "Operation 1", false);
        ProductionNode mat1 = new ProductionNode("mat1", "Material 1", true);
        ProductionNode mat2 = new ProductionNode("mat2", "Material 2", true);

        productionTree.addNode(op1);
        productionTree.addNode(mat1);
        productionTree.addNode(mat2);

        List<ProductionNode> resultById = productionTree.searchNodes("op1");
        assertEquals(1, resultById.size());
        assertEquals(op1, resultById.get(0));

        List<ProductionNode> resultByName = productionTree.searchNodes("Material 1");
        assertEquals(1, resultByName.size());
        assertEquals(mat1, resultByName.get(0));

        List<ProductionNode> resultNotFound = productionTree.searchNodes("nonexistent");
        assertTrue(resultNotFound.isEmpty());
    }


    @Test
    void testEmptyTreeSearch_AC2() {
        productionTree = new ProductionTree();
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        List<ProductionNode> results = productionTree.searchNodes("anything");
        assertTrue(results.isEmpty(), "A busca em uma árvore vazia deve retornar uma lista vazia.");
    }

    @Test
    void testNullSearchQuery_AC2() {
        productionTree = new ProductionTree();
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        List<ProductionNode> results = productionTree.searchNodes(null);
        assertTrue(results.isEmpty(), "A busca com consulta nula deve retornar uma lista vazia.");
    }


    @Test
    void testSearchByPartialName_AC2() {
        productionTree = new ProductionTree();
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode op1 = new ProductionNode("op1", "Operation ABC", false);
        ProductionNode op2 = new ProductionNode("op2", "Operation XYZ", false);
        productionTree.addNode(op1);
        productionTree.addNode(op2);

        List<ProductionNode> partialResults = productionTree.searchNodes("Operation");
        assertEquals(2, partialResults.size());
        assertTrue(partialResults.contains(op1));
        assertTrue(partialResults.contains(op2));

        partialResults = productionTree.searchNodes("XYZ");
        assertEquals(1, partialResults.size());
        assertEquals(op2, partialResults.get(0));
    }

    @Test
    void testCaseInsensitiveSearch_AC2() {
        productionTree = new ProductionTree();
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode mat1 = new ProductionNode("mat1", "Material A", true);
        productionTree.addNode(mat1);

        List<ProductionNode> resultsLowercase = productionTree.searchNodes("material a");
        List<ProductionNode> resultsUppercase = productionTree.searchNodes("MATERIAL A");

        assertEquals(1, resultsLowercase.size());
        assertEquals(1, resultsUppercase.size());
        assertEquals(mat1, resultsLowercase.get(0));
        assertEquals(mat1, resultsUppercase.get(0));
    }



    @Test
    void testSearchWithMultipleMatches_AC3() {
        productionTree = new ProductionTree();
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode op1 = new ProductionNode("op1", "Operation 1", false);
        ProductionNode mat1 = new ProductionNode("mat1", "Material 1", true);
        ProductionNode duplicateOp = new ProductionNode("op2", "Operation 1", false);

        productionTree.addNode(op1);
        productionTree.addNode(mat1);
        productionTree.addNode(duplicateOp);

        List<ProductionNode> matchingNodes = productionTree.searchNodes("Operation 1");
        assertEquals(2, matchingNodes.size());
        assertTrue(matchingNodes.contains(op1));
        assertTrue(matchingNodes.contains(duplicateOp));
    }



    @Test
    void testGetNodeDetailsForNonExistentNode_AC3() {
        productionTree = new ProductionTree();
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode nonExistentNode = new ProductionNode("fake", "NonExistent", false);

        String details = searcher.getNodeDetails(nonExistentNode);
        assertTrue(details.contains(""));
    }

    @Test
    void testNodeDependencies_AC1() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode op1 = productionTree.getNodeById("1");
        Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(op1);

        assertEquals(1, subNodes.size());
        ProductionNode material = subNodes.keySet().iterator().next();
        assertEquals("2001", material.getId());
        assertEquals(2.0, subNodes.get(material));
    }

    @Test
    void testOperationWithMultipleDependencies_AC1() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree(
                "src/test/java/fourcorp/buildflow/operations_test.csv",
                "src/test/java/fourcorp/buildflow/items_test.csv",
                "src/test/java/fourcorp/buildflow/boo_test.csv"
        );
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode op2 = productionTree.getNodeById("2");
        Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(op2);

        assertEquals(1, subNodes.size());
        ProductionNode subOp = subNodes.keySet().iterator().next();
        assertEquals("1", subOp.getId());
        assertEquals(4.0, subNodes.get(subOp));
    }



    @Test
    void testSearchMultipleMatches_AC2() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);


        ProductionNode duplicateOp = new ProductionNode("op_duplicate", "cortar tábua de madeira", false);
        productionTree.addNode(duplicateOp);

        List<ProductionNode> matches = productionTree.searchNodes("cortar tábua de madeira");
        assertEquals(2, matches.size());
        assertTrue(matches.stream().anyMatch(node -> node.getId().equals("1")));
        assertTrue(matches.stream().anyMatch(node -> node.getId().equals("op_duplicate")));
    }

    @Test
    void testSearchByOperationName_AC2() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree(
                "src/test/java/fourcorp/buildflow/operations_test.csv",
                "src/test/java/fourcorp/buildflow/items_test.csv",
                "src/test/java/fourcorp/buildflow/boo_test.csv"
        );
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        List<ProductionNode> matches = productionTree.searchNodes("montar caixa");
        assertEquals(1, matches.size());
        assertEquals("2", matches.get(0).getId());
        assertEquals("montar caixa", matches.get(0).getName());
    }

    @Test
    void testSearchByPartialMaterialName_AC2() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree(
                "src/test/java/fourcorp/buildflow/operations_test.csv",
                "src/test/java/fourcorp/buildflow/items_test.csv",
                "src/test/java/fourcorp/buildflow/boo_test.csv"
        );
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        List<ProductionNode> matches = productionTree.searchNodes("caixa");
        assertTrue(matches.size() >= 2);
        assertTrue(matches.stream().anyMatch(node -> node.getId().equals("2005")));
        assertTrue(matches.stream().anyMatch(node -> node.getId().equals("2006")));
    }


    @Test
    void testSearchNonExistentNode_AC2() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        List<ProductionNode> results = productionTree.searchNodes("nonexistent");
        assertTrue(results.isEmpty(), "A busca por um nó inexistente deve retornar uma lista vazia.");
    }

    @Test
    void testRetrieveMaterialWithNoParent_AC3() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode item2001 = productionTree.getNodeById("2001");
        String detailsItem2001 = searcher.getNodeDetails(item2001);

        assertTrue(detailsItem2001.contains("ID: 2001"));
        assertTrue(detailsItem2001.contains("Name: tábua de madeira"));
        assertTrue(detailsItem2001.contains("Type: Material"));
        assertTrue(detailsItem2001.contains(""));
    }

    @Test
    void testRetrieveAllSubNodes_AC3() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode op2 = productionTree.getNodeById("2");
        Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(op2);

        assertEquals(1, subNodes.size());
        ProductionNode subNode = subNodes.keySet().iterator().next();
        assertEquals("1", subNode.getId());
        assertEquals(4.0, subNodes.get(subNode));
    }

    @Test
    void testRetrieveAllNodesWithDependencies_AC3() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        List<ProductionNode> allNodes = productionTree.getAllNodes();
        assertEquals(13, allNodes.size());

        assertTrue(allNodes.stream().anyMatch(node -> node.getId().equals("1")));
        assertTrue(allNodes.stream().anyMatch(node -> node.getId().equals("2008")));
        assertTrue(allNodes.stream().anyMatch(node -> node.getId().equals("5")));
    }



    @Test
    void testFindLeafNodes_AC3() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode tabuaDeMadeira = productionTree.getNodeById("2001");
        Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(tabuaDeMadeira);

        assertTrue(subNodes.isEmpty());
    }

    @Test
    void testNodeParentDetails_AC3() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode item2002 = productionTree.getNodeById("2002");
        ProductionNode parentOp = (ProductionNode) item2002.getParent();

        assertNotNull(parentOp);
        assertEquals("1", parentOp.getId());
        assertEquals("cortar tábua de madeira", parentOp.getName());
    }

    @Test
    void testEmptyNodeDetails_AC3() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");
        searcher = new ProductionTreeSearcher();
        Repositories.getInstance().setProductionTree(productionTree);

        ProductionNode mat1 = new ProductionNode("mat1", "Material A", true);
        productionTree.addNode(mat1);

        String details = searcher.getNodeDetails(mat1);
        assertTrue(details.contains("Type: Material"));
        assertTrue(details.contains("No material details available."));
    }


    @Test
    void generateTree() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");
        DisplayProductionTree dp = new DisplayProductionTree();
        dp.setProductionTree(productionTree);
        dp.generateGraph();
    }
}


