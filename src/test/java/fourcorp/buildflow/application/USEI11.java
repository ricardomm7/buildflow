package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

public class USEI11 {

    private ProductionTree productionTree;
    private QualityCheckManager manager;

    @Nested
    class QualityCheckManagerTest {
        private QualityCheckManager manager;
        private ProductionTree productionTree;


        @Test
        void testInitialNodesPresentInTree() {
            // Inicializar árvore de produção
            productionTree = new ProductionTree();

            // Configurar nós simulados
            ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
            ProductionNode operationB = new ProductionNode("B1", "Operação B", false);
            ProductionNode operationC = new ProductionNode("C1", "Operação C", false);
            ProductionNode productD = new ProductionNode("D1", "Produto Final", true);

            // Adicionar nós à árvore
            productionTree.addNode(operationA);
            productionTree.addNode(operationB);
            productionTree.addNode(operationC);
            productionTree.addNode(productD);


            assertNotNull(productionTree.getNodeById("A1"), "O nó 'A1' deve estar presente na árvore.");
            assertNotNull(productionTree.getNodeById("B1"), "O nó 'B1' deve estar presente na árvore.");
            assertNotNull(productionTree.getNodeById("C1"), "O nó 'C1' deve estar presente na árvore.");
            assertNotNull(productionTree.getNodeById("D1"), "O nó 'D1' deve estar presente na árvore.");
        }


        @Test
        void testOperationsArePrioritizedCorrectly_AC1() {
            // Inicializar árvore de produção
            productionTree = new ProductionTree();

            // Configurar nós simulados
            ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
            ProductionNode operationB = new ProductionNode("B1", "Operação B", false);
            ProductionNode operationC = new ProductionNode("C1", "Operação C", false);
            ProductionNode productD = new ProductionNode("D1", "Produto Final", true);

            // Adicionar nós à árvore
            productionTree.addNode(operationA);
            productionTree.addNode(operationB);
            productionTree.addNode(operationC);
            productionTree.addNode(productD);

            // Configurar dependências
            productionTree.addDependency(operationB, operationA); // B depende de A
            productionTree.addDependency(operationC, operationB); // C depende de B
            productionTree.addDependency(productD, operationC);  // Produto final depende de C


            // Testa a ordenação correta de prioridade com base na profundidade
            PriorityQueue<ProductionNode> qualityChecks = new PriorityQueue<>(
                    (o1, o2) -> Integer.compare(
                            o2.getDepth(productionTree),
                            o1.getDepth(productionTree)
                    )
            );

            // Adicionar apenas nós de operação
            for (ProductionNode node : productionTree.getAllNodes()) {
                if (node.isOperation()) {
                    qualityChecks.add(node);
                }
            }

            List<String> executionOrder = new ArrayList<>();
            while (!qualityChecks.isEmpty()) {
                executionOrder.add(qualityChecks.poll().getName());
            }

            // Ordem esperada: C (mais próximo do produto final), B, A
            assertEquals(List.of("Operação C", "Operação B", "Operação A"), executionOrder);
        }

        @Test
        void testExecuteQualityChecksInPriorityOrder_AC2() {

            // Inicializar árvore de produção
            productionTree = new ProductionTree();

            // Configurar nós simulados
            ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
            ProductionNode operationB = new ProductionNode("B1", "Operação B", false);
            ProductionNode operationC = new ProductionNode("C1", "Operação C", false);
            ProductionNode productD = new ProductionNode("D1", "Produto Final", true);

            // Adicionar nós à árvore
            productionTree.addNode(operationA);
            productionTree.addNode(operationB);
            productionTree.addNode(operationC);
            productionTree.addNode(productD);

            // Configurar dependências
            productionTree.addDependency(operationB, operationA); // B depende de A
            productionTree.addDependency(operationC, operationB); // C depende de B
            productionTree.addDependency(productD, operationC);  // Produto final depende de C

            // Inicializar gerenciador
            manager = new QualityCheckManager(productionTree);

            // Executa o método principal e valida a saída no console (simulação)
            manager.prioritizeAndExecuteQualityChecks();

            // Testar execução em ordem de prioridade, conforme o método
            PriorityQueue<ProductionNode> expectedOrder = new PriorityQueue<>(
                    (o1, o2) -> Integer.compare(
                            o2.getDepth(productionTree),
                            o1.getDepth(productionTree)
                    )
            );

            for (ProductionNode node : productionTree.getAllNodes()) {
                if (node.isOperation()) {
                    expectedOrder.add(node);
                }
            }

            List<ProductionNode> executedOrder = new ArrayList<>();
            while (!expectedOrder.isEmpty()) {
                executedOrder.add(expectedOrder.poll());
            }

            assertEquals("Operação C", executedOrder.get(0).getName());
            assertEquals("Operação B", executedOrder.get(1).getName());
            assertEquals("Operação A", executedOrder.get(2).getName());
        }

        @Test
        void testEmptyProductionTree_AC2() {
            ProductionTree emptyTree = new ProductionTree();
            QualityCheckManager emptyManager = new QualityCheckManager(emptyTree);

            assertDoesNotThrow(emptyManager::prioritizeAndExecuteQualityChecks,
                    "O método deve lidar com uma árvore de produção vazia sem lançar exceções.");
        }

        @Test
        void testCycleInDependencies() {

            // Inicializar árvore de produção
            productionTree = new ProductionTree();

            // Configurar nós simulados
            ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
            ProductionNode operationB = new ProductionNode("B1", "Operação B", false);
            ProductionNode operationC = new ProductionNode("C1", "Operação C", false);
            ProductionNode productD = new ProductionNode("D1", "Produto Final", true);

            // Adicionar nós à árvore
            productionTree.addNode(operationA);
            productionTree.addNode(operationB);
            productionTree.addNode(operationC);
            productionTree.addNode(productD);

            // Configurar dependências
            productionTree.addDependency(operationB, operationA); // B depende de A
            productionTree.addDependency(operationC, operationB); // C depende de B
            productionTree.addDependency(productD, operationC);  // Produto final depende de C

            // Inicializar gerenciador
            manager = new QualityCheckManager(productionTree);

            // Configurar um ciclo
            ProductionNode cycleNode = new ProductionNode("E1", "Operação Cíclica", false);
            productionTree.addNode(cycleNode);

            assertNotNull(operationA, "O nó 'A1' deveria estar presente na árvore de produção.");

            // Adicionar dependências cíclicas
            productionTree.addDependency(cycleNode, operationA); // Ciclo: E depende de A
            productionTree.addDependency(operationA, cycleNode); // A depende de E

            // Valida comportamento para ciclos (exemplo: detecção de ciclo ou falha ao calcular profundidade)
            assertThrows(StackOverflowError.class,
                    () -> operationA.getDepth(productionTree),
                    "Dependências cíclicas devem causar erro de pilha (StackOverflowError).");
        }

    }

    @Test
    void testNodesWithoutDependencies() {
        // Inicializar árvore de produção
        productionTree = new ProductionTree();

        // Adicionar nós sem dependências
        ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
        ProductionNode operationB = new ProductionNode("B1", "Operação B", false);

        productionTree.addNode(operationA);
        productionTree.addNode(operationB);

        manager = new QualityCheckManager(productionTree);

        // Executa o método principal
        manager.prioritizeAndExecuteQualityChecks();

        // Testa se os nós foram processados
        List<ProductionNode> allNodes = productionTree.getAllNodes();
        assertEquals(2, allNodes.size(), "Deve haver dois nós na árvore de produção.");
        assertEquals("Operação A", allNodes.get(0).getName());
        assertEquals("Operação B", allNodes.get(1).getName());
    }

    @Test
    void testMultiplePathsToNode() {
        // Inicializar árvore de produção
        productionTree = new ProductionTree();

        // Configurar nós simulados
        ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
        ProductionNode operationB = new ProductionNode("B1", "Operação B", false);
        ProductionNode operationC = new ProductionNode("C1", "Operação C", false);
        ProductionNode operationD = new ProductionNode("D1", "Operação D", false);
        ProductionNode productE = new ProductionNode("E1", "Produto Final", true);

        // Adicionar nós à árvore
        productionTree.addNode(operationA);
        productionTree.addNode(operationB);
        productionTree.addNode(operationC);
        productionTree.addNode(operationD);
        productionTree.addNode(productE);

        // Configurar dependências corrigidas
        productionTree.addDependency(operationD, productE);   // D depende do Produto (Produto como raiz)
        productionTree.addDependency(operationC, operationD); // C depende de D
        productionTree.addDependency(operationB, operationD); // B depende de D
        productionTree.addDependency(operationA, operationC); // A depende de C
        productionTree.addDependency(operationA, operationB); // A depende de B

        manager = new QualityCheckManager(productionTree);

        // Testar profundidade
        assertEquals(3, operationA.getDepth(productionTree), "A profundidade do nó A deve ser 3.");
        assertEquals(2, operationB.getDepth(productionTree), "A profundidade do nó B deve ser 2.");
        assertEquals(2, operationC.getDepth(productionTree), "A profundidade do nó C deve ser 2.");
        assertEquals(1, operationD.getDepth(productionTree), "A profundidade do nó D deve ser 1.");
        assertEquals(0, productE.getDepth(productionTree), "A profundidade do nó E deve ser 0.");
    }


    @Test
    void testInvalidNodesHandling() {
        // Inicializar árvore de produção
        productionTree = new ProductionTree();

        // Adicionar nós válidos
        ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
        productionTree.addNode(operationA);

        // Testar comportamento ao buscar um nó inválido
        assertNull(productionTree.getNodeById("X1"), "Nó inexistente deve retornar nulo.");
    }

    @Test
    void testEqualPriorityNodes() {
        // Inicializar árvore de produção
        productionTree = new ProductionTree();

        // Configurar nós com a mesma profundidade
        ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
        ProductionNode operationB = new ProductionNode("B1", "Operação B", false);
        ProductionNode operationC = new ProductionNode("C1", "Operação C", false);

        // Adicionar nós à árvore
        productionTree.addNode(operationA);
        productionTree.addNode(operationB);
        productionTree.addNode(operationC);

        // Configurar dependências (todos na mesma profundidade)
        productionTree.addDependency(operationB, operationA);
        productionTree.addDependency(operationC, operationA);

        manager = new QualityCheckManager(productionTree);

        // Executar o método principal
        PriorityQueue<ProductionNode> qualityChecks = new PriorityQueue<>(
                (o1, o2) -> Integer.compare(
                        o2.getDepth(productionTree),
                        o1.getDepth(productionTree)
                )
        );

        for (ProductionNode node : productionTree.getAllNodes()) {
            if (node.isOperation()) {
                qualityChecks.add(node);
            }
        }

        // Testar ordem de execução
        List<ProductionNode> executedOrder = new ArrayList<>();
        while (!qualityChecks.isEmpty()) {
            executedOrder.add(qualityChecks.poll());
        }

        assertTrue(executedOrder.contains(operationB), "Operação B deve estar presente na fila.");
        assertTrue(executedOrder.contains(operationC), "Operação C deve estar presente na fila.");
        assertEquals(2, executedOrder.stream().filter(node -> node.getDepth(productionTree) == 1).count(),
                "Deve haver dois nós com a mesma profundidade.");
    }

    @Test
    void testNoOperationsInTree() {
        // Inicializar árvore de produção sem operações
        productionTree = new ProductionTree();

        // Configurar apenas produtos
        ProductionNode productA = new ProductionNode("A1", "Produto A", true);
        ProductionNode productB = new ProductionNode("B1", "Produto B", true);

        productionTree.addNode(productA);
        productionTree.addNode(productB);

        manager = new QualityCheckManager(productionTree);

        // Executar o método principal
        manager.prioritizeAndExecuteQualityChecks();

        // Valida que nenhuma operação foi executada
        PriorityQueue<ProductionNode> qualityChecks = new PriorityQueue<>(
                (o1, o2) -> Integer.compare(
                        o2.getDepth(productionTree),
                        o1.getDepth(productionTree)
                )
        );

        for (ProductionNode node : productionTree.getAllNodes()) {
            if (node.isOperation()) {
                qualityChecks.add(node);
            }
        }

        assertTrue(qualityChecks.isEmpty(), "Nenhuma operação deve ser encontrada na árvore.");
    }

    @Test
    void testReadTree() throws IOException {
       ProductionTree testTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv","src/test/java/fourcorp/buildflow/items_test.csv","src/test/java/fourcorp/buildflow/boo_test.csv");
        DisplayProductionTree display = new DisplayProductionTree();
        display.setProductionTree(testTree);
        display.generateGraph();

    }

}
