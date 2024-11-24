package fourcorp.buildflow.application;


import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.MaterialQuantityBST;
import fourcorp.buildflow.repository.ProductionTree;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class USEI12 {

    private ProductionTree productionTree;
    private ProductionNode nodeA;
    private ProductionNode nodeB;
    private MaterialQuantityBST materialQuantityBST;

    @Test
    void updateConnectionsQuantity() {
        productionTree = new ProductionTree();
        nodeA = new ProductionNode("1", "Node A", true);
        nodeB = new ProductionNode("2", "Node B", true);
        materialQuantityBST = new MaterialQuantityBST();
        productionTree.addNode(nodeA);
        productionTree.addNode(nodeB);

        productionTree.addDependencyBom(nodeA, nodeB, 2.0);

        nodeA.setQuantity(10);

        assertEquals(0.0, nodeB.getQuantity(), 0.01);

        productionTree.updateConnectionsQuantity(nodeA, nodeA.getQuantity(), materialQuantityBST);

        assertEquals(20.0, nodeB.getQuantity(), 0.01);
    }


    @Test
    void testPropagateQuantityUpdate() {
        productionTree = new ProductionTree();
        nodeA = new ProductionNode("1", "Node A", true);
        nodeB = new ProductionNode("2", "Node B", true);
        materialQuantityBST = new MaterialQuantityBST();
        productionTree.addNode(nodeA);
        productionTree.addNode(nodeB);

        ProductionNode nodeC = new ProductionNode("3", "Node C", true);
        productionTree.addNode(nodeC);

        productionTree.addDependencyBom(nodeA, nodeB, 2.0); // nodeA -> nodeB
        productionTree.addDependencyBom(nodeB, nodeC, 1.5); // nodeB -> nodeC

        nodeA.setQuantity(10);

        productionTree.updateConnectionsQuantity(nodeA, nodeA.getQuantity(), materialQuantityBST);

        assertEquals(20.0, nodeB.getQuantity(), 0.01);

        assertEquals(30.0, nodeC.getQuantity(), 0.01);
    }

    @Test
    void testUpdateConnectionsQuantity() {
        productionTree = new ProductionTree();
        nodeA = new ProductionNode("1", "Node A", true);
        nodeB = new ProductionNode("2", "Node B", true);
        materialQuantityBST = new MaterialQuantityBST();
        productionTree.addNode(nodeA);
        productionTree.addNode(nodeB);

        ProductionNode node1 = new ProductionNode("1", "Material1", true);
        ProductionNode node2 = new ProductionNode("2", "Material2", true);
        productionTree.addNode(node1);
        productionTree.addNode(node2);

        productionTree.addDependency(node2, node1);

        assertTrue(productionTree.getSubNodes(node1).containsKey(node2));

        double newQuantity = 200.0;
        MaterialQuantityBST materialQuantityBST = new MaterialQuantityBST();
        productionTree.updateConnectionsQuantity(node1, newQuantity, materialQuantityBST);

        assertEquals(newQuantity, productionTree.getSubNodes(node1).get(node2), 0.01);
    }

    @Test
    void testUpdatePropagationMultipleLevels() {
        ProductionTree productionTree = new ProductionTree();
        MaterialQuantityBST materialQuantityBST = new MaterialQuantityBST();

        // Criar os nós
        ProductionNode nodeA = new ProductionNode("A", "Material A", true);
        ProductionNode nodeB = new ProductionNode("B", "Material B", true);
        ProductionNode nodeC = new ProductionNode("C", "Material C", true);
        ProductionNode nodeD = new ProductionNode("D", "Material D", true);

        // Adicionar nós à árvore
        productionTree.addNode(nodeA);
        productionTree.addNode(nodeB);
        productionTree.addNode(nodeC);
        productionTree.addNode(nodeD);

        // Definir dependências
        productionTree.addDependencyBom(nodeA, nodeB, 2.0); // nodeA → nodeB (2.0)
        productionTree.addDependencyBom(nodeB, nodeC, 1.5); // nodeB → nodeC (1.5)
        productionTree.addDependencyBom(nodeC, nodeD, 3.0); // nodeC → nodeD (3.0)

        // Configurar quantidade inicial de nodeA
        nodeA.setQuantity(10);

        // Atualizar quantidades
        productionTree.updateConnectionsQuantity(nodeA, nodeA.getQuantity(), materialQuantityBST);

        // Verificar propagação
        assertEquals(20.0, nodeB.getQuantity(), 0.01); // 10 * 2.0
        assertEquals(30.0, nodeC.getQuantity(), 0.01); // 20 * 1.5
        assertEquals(90.0, nodeD.getQuantity(), 0.01); // 30 * 3.0
    }

    @Test
    void testPropagationToMultipleLevels() {
        // Criar uma árvore com múltiplos níveis de dependências
        ProductionTree productionTree = new ProductionTree();
        MaterialQuantityBST materialQuantityBST = new MaterialQuantityBST();

        ProductionNode nodeA = new ProductionNode("A", "Root Material", true);
        ProductionNode nodeB = new ProductionNode("B", "Intermediate Material", true);
        ProductionNode nodeC = new ProductionNode("C", "Final Material", true);

        productionTree.addNode(nodeA);
        productionTree.addNode(nodeB);
        productionTree.addNode(nodeC);

        productionTree.addDependencyBom(nodeA, nodeB, 2.0); // A → B (x2)
        productionTree.addDependencyBom(nodeB, nodeC, 3.0); // B → C (x3)

        nodeA.setQuantity(5.0); // Quantidade inicial de A

        // Atualizar A e propagar
        productionTree.updateConnectionsQuantity(nodeA, 5.0, materialQuantityBST);

        // Verificar propagação
        assertEquals(10.0, nodeB.getQuantity(), "Quantidade de B deve ser 10.0 (5.0 x 2)");
        assertEquals(30.0, nodeC.getQuantity(), "Quantidade de C deve ser 30.0 (10.0 x 3)");
    }

    @Test
    void testComplexPropagationWithDependencyChange() {
        ProductionTree productionTree = new ProductionTree();
        MaterialQuantityBST materialQuantityBST = new MaterialQuantityBST();

        ProductionNode nodeA = new ProductionNode("A", "Material A", true);
        ProductionNode nodeB = new ProductionNode("B", "Material B", true);
        ProductionNode nodeC = new ProductionNode("C", "Material C", true);

        productionTree.addNode(nodeA);
        productionTree.addNode(nodeB);
        productionTree.addNode(nodeC);

        productionTree.addDependencyBom(nodeA, nodeB, 2.0); // A → B (x2)
        productionTree.addDependencyBom(nodeB, nodeC, 3.0); // B → C (x3)

        nodeA.setQuantity(4.0);

        // Atualizar A e propagar
        productionTree.updateConnectionsQuantity(nodeA, 4.0, materialQuantityBST);

        // Verificar valores antes de alterar dependência
        assertEquals(8.0, nodeB.getQuantity(), "B deve ser 8.0 (4.0 x 2)");
        assertEquals(24.0, nodeC.getQuantity(), "C deve ser 24.0 (8.0 x 3)");

    }


    @Test
    void testUpdateQuantityInLoadedTree() throws IOException {
        ProductionTree testTree = Reader.loadProductionTree(
                "src/test/java/fourcorp/buildflow/operations_test.csv",
                "src/test/java/fourcorp/buildflow/items_test.csv",
                "src/test/java/fourcorp/buildflow/boo_test.csv"
        );
        MaterialQuantityBST materialQuantityBST = new MaterialQuantityBST();

        // Configurar "tábua de madeira" como nó base
        ProductionNode tabuaMadeira = testTree.getNodeById("2001");
        assertNotNull(tabuaMadeira);
        tabuaMadeira.setQuantity(10.0);

        // Atualizar quantidade
        testTree.updateConnectionsQuantity(tabuaMadeira, tabuaMadeira.getQuantity(), materialQuantityBST);

        // Verificar propagação
        ProductionNode tabua = testTree.getNodeById("2001");
        assertNotNull(tabua);
        assertEquals(10.0, tabua.getQuantity(), 0.01); // Quantidade deve refletir mudança
    }


    @Test
    void testFullPathPropagation() throws IOException {
        ProductionTree testTree = Reader.loadProductionTree(
                "src/test/java/fourcorp/buildflow/operations_test.csv",
                "src/test/java/fourcorp/buildflow/items_test.csv",
                "src/test/java/fourcorp/buildflow/boo_test.csv"
        );
        MaterialQuantityBST materialQuantityBST = new MaterialQuantityBST();

        // Atualizar "tábua cortada" e verificar propagação para "tábua de madeira"
        ProductionNode tabuaCortada = testTree.getNodeById("2002");
        assertNotNull(tabuaCortada);
        tabuaCortada.setQuantity(5.0);

        testTree.updateConnectionsQuantity(tabuaCortada, 5.0, materialQuantityBST);

        // Verificar atualização no nó pai
        ProductionNode tabuaMadeira = testTree.getNodeById("2001");
        assertNotNull(tabuaMadeira);
        assertEquals(10.0, tabuaMadeira.getQuantity(), "A quantidade de tábua de madeira deve ser 5.0 x 2.");

    }

    @Test
    void testLeafNodeUpdate() throws IOException {
        ProductionTree testTree = Reader.loadProductionTree(
                "src/test/java/fourcorp/buildflow/operations_test.csv",
                "src/test/java/fourcorp/buildflow/items_test.csv",
                "src/test/java/fourcorp/buildflow/boo_test.csv"
        );
        MaterialQuantityBST materialQuantityBST = new MaterialQuantityBST();

        // Atualizar "caixa lixada"
        ProductionNode caixaLixada = testTree.getNodeById("2007");
        assertNotNull(caixaLixada);
        caixaLixada.setQuantity(3.0);

        testTree.updateConnectionsQuantity(caixaLixada, 3.0, materialQuantityBST);

        // Verificar propagação para ancestrais
        ProductionNode caixaMontada = testTree.getNodeById("2004");
        assertNotNull(caixaMontada);
        assertEquals(30.0, caixaMontada.getQuantity(), "A quantidade de caixa montada deve ser 1.0.");

        // Verificar que nós não relacionados não são afetados
        ProductionNode tinta = testTree.getNodeById("2008");
        assertNotNull(tinta);
        assertEquals(0.5, tinta.getQuantity(), "A quantidade de tinta não deve ser afetada.");
    }


    @Test
    void testUpdateIndependentNodeInLoadedTree() throws IOException {
        ProductionTree testTree = Reader.loadProductionTree(
                "src/test/java/fourcorp/buildflow/operations_test.csv",
                "src/test/java/fourcorp/buildflow/items_test.csv",
                "src/test/java/fourcorp/buildflow/boo_test.csv"
        );
        MaterialQuantityBST materialQuantityBST = new MaterialQuantityBST();

        // Atualizar quantidade de "tinta" (ID: 2008)
        ProductionNode tinta = testTree.getNodeById("2008");
        assertNotNull(tinta);
        assertEquals(0.5, tinta.getQuantity());

        tinta.setQuantity(2.0);
        testTree.updateConnectionsQuantity(tinta, tinta.getQuantity(), materialQuantityBST);

        // Verificar que não há propagação para outros nós
        ProductionNode caixaPintada = testTree.getNodeById("2006");
        assertNotNull(caixaPintada);
        assertEquals(1.0, caixaPintada.getQuantity(), 0.01); // Quantidade inicial permanece
    }


    @Test
    void generateTree() throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");
        DisplayProductionTree dp = new DisplayProductionTree();
        dp.setProductionTree(productionTree);
        dp.generateGraph();
    }

}
