package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

public class USEI14 {

    private ProductionTree productionTree;

    @Nested
    class CriticalPathManagerTest {
        private ProductionTree productionTree;


        @Test
        void testCriticalPathPriority() {
            // Inicializar árvore de produção
            productionTree = new ProductionTree();

            // Configurar nós para teste
            ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
            ProductionNode operationB = new ProductionNode("B1", "Operação B", false);
            ProductionNode operationC = new ProductionNode("C1", "Operação C", false);
            ProductionNode productD = new ProductionNode("D1", "Produto D", true);

            // Adicionar nós à árvore
            productionTree.addNode(operationA);
            productionTree.addNode(operationB);
            productionTree.addNode(operationC);
            productionTree.addNode(productD);

            // Definir dependências
            productionTree.addDependency(operationB, operationA); // B depende de A
            productionTree.addDependency(operationC, operationB); // C depende de B
            productionTree.addDependency(productD, operationC);   // D depende de C


            // AC1: Ordenar operações pelo caminho crítico usando um heap
            PriorityQueue<ProductionNode> queue = new PriorityQueue<>(
                    (a, b) -> Integer.compare(
                            b.getDepth(productionTree), // Usando profundidade corrigida
                            a.getDepth(productionTree)
                    )
            );

            // Adicionar todos os nós ao heap
            for (ProductionNode node : productionTree.getAllNodes()) {
                queue.offer(node);
            }

            // AC2: Validar ordem por profundidade
            ProductionNode first = queue.poll();
            assertNotNull(first);
            assertEquals("Produto D", first.getName(), "Primeiro no caminho crítico deve ser o mais profundo");

            ProductionNode second = queue.poll();
            assertNotNull(second);
            assertEquals("Operação C", second.getName(), "Segundo deve ser o próximo mais profundo");

            // AC3: Caminho crítico como sequência
            List<ProductionNode> criticalPath = productionTree.getCriticalPath();
            assertEquals(4, criticalPath.size(), "Caminho crítico deve ter 4 operações");
            assertEquals("Operação A", criticalPath.get(0).getName());
            assertEquals("Produto D", criticalPath.get(3).getName());
        }

        @Test
        void testCriticalPathWithIndependentNodes() {

            // Inicializar árvore de produção
            productionTree = new ProductionTree();

            // Configurar nós para teste
            ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
            ProductionNode operationB = new ProductionNode("B1", "Operação B", false);
            ProductionNode operationC = new ProductionNode("C1", "Operação C", false);
            ProductionNode productD = new ProductionNode("D1", "Produto D", true);

            // Adicionar nós à árvore
            productionTree.addNode(operationA);
            productionTree.addNode(operationB);
            productionTree.addNode(operationC);
            productionTree.addNode(productD);

            // Definir dependências
            productionTree.addDependency(operationB, operationA); // B depende de A
            productionTree.addDependency(operationC, operationB); // C depende de B
            productionTree.addDependency(productD, operationC);   // D depende de C


            // Adiciona um nó sem dependências
            ProductionNode independentNode = new ProductionNode("E1", "Operação Independente", false);
            productionTree.addNode(independentNode);

            List<ProductionNode> criticalPath = productionTree.getCriticalPath();

            // Valida que o caminho crítico ignora o nó independente
            assertEquals(4, criticalPath.size(), "Caminho crítico deve ignorar nós independentes");
            assertEquals("Operação A", criticalPath.get(0).getName());
            assertEquals("Produto D", criticalPath.get(3).getName());
        }

        @Test
        void testInvalidDependencyHandling() {
            // Inicializar árvore de produção
            productionTree = new ProductionTree();

            // Configurar nós para teste
            ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
            ProductionNode operationB = new ProductionNode("B1", "Operação B", false);
            ProductionNode operationC = new ProductionNode("C1", "Operação C", false);
            ProductionNode productD = new ProductionNode("D1", "Produto D", true);

            // Adicionar nós à árvore
            productionTree.addNode(operationA);
            productionTree.addNode(operationB);
            productionTree.addNode(operationC);
            productionTree.addNode(productD);

            // Definir dependências
            productionTree.addDependency(operationB, operationA); // B depende de A
            productionTree.addDependency(operationC, operationB); // C depende de B
            productionTree.addDependency(productD, operationC);   // D depende de C


            // Cenário: Adicionar uma dependência circular direta
            ProductionNode nodeA = new ProductionNode("A1", "Operação A", false);
            productionTree.addNode(nodeA);

            // Testar dependência circular direta
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> productionTree.addDependency(nodeA, nodeA),
                    "Dependências circulares diretas devem lançar uma exceção"
            );

            assertEquals(
                    "Cannot add direct circular dependency from a node to itself",
                    exception.getMessage(),
                    "A mensagem da exceção deve ser clara e descrever o erro"
            );

            // Validar que nenhuma dependência foi registrada
            List<ProductionNode> parentsOfA = productionTree.getParentNodes(nodeA);
            assertEquals(0, parentsOfA.size(), "O nó A não deve ter dependências após tentativa de dependência circular");
        }
    }

    @Test
    void testCriticalPathWithMultipleBranches() {
        // Inicializar árvore de produção
        productionTree = new ProductionTree();

        // Configurar nós para teste
        ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
        ProductionNode operationB = new ProductionNode("B1", "Operação B", false);
        ProductionNode operationC = new ProductionNode("C1", "Operação C", false);
        ProductionNode operationD = new ProductionNode("D1", "Operação D", false);
        ProductionNode operationE = new ProductionNode("E1", "Operação E", false);
        ProductionNode productF = new ProductionNode("F1", "Produto Final", true);

        // Adicionar nós à árvore
        productionTree.addNode(operationA);
        productionTree.addNode(operationB);
        productionTree.addNode(operationC);
        productionTree.addNode(operationD);
        productionTree.addNode(operationE);
        productionTree.addNode(productF);

        // Configurar múltiplos ramos
        productionTree.addDependency(operationB, operationA); // B depende de A
        productionTree.addDependency(operationC, operationA); // C depende de A
        productionTree.addDependency(operationD, operationB); // D depende de B
        productionTree.addDependency(operationE, operationC); // E depende de C
        productionTree.addDependency(productF, operationD);   // Produto depende de D
        productionTree.addDependency(productF, operationE);   // Produto depende de E

        // Validar o caminho crítico
        List<ProductionNode> criticalPath = productionTree.getCriticalPath();
        assertEquals(4, criticalPath.size(), "Caminho crítico deve ter 4 operações.");
        assertEquals("Operação A", criticalPath.get(0).getName());
        assertEquals("Operação B", criticalPath.get(1).getName());
        assertEquals("Operação D", criticalPath.get(2).getName());
        assertEquals("Produto Final", criticalPath.get(3).getName());
    }

    @Test
    void testNodesWithEqualDepth() {
        // Inicializar árvore de produção
        productionTree = new ProductionTree();

        // Configurar nós para teste
        ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
        ProductionNode operationB = new ProductionNode("B1", "Operação B", false);
        ProductionNode operationC = new ProductionNode("C1", "Operação C", false);
        ProductionNode productD = new ProductionNode("D1", "Produto Final", true);

        // Adicionar nós à árvore
        productionTree.addNode(operationA);
        productionTree.addNode(operationB);
        productionTree.addNode(operationC);
        productionTree.addNode(productD);

        // Configurar dependências (profundidade igual)
        productionTree.addDependency(operationB, operationA); // B depende de A
        productionTree.addDependency(operationC, operationA); // C depende de A
        productionTree.addDependency(productD, operationB);   // Produto depende de B
        productionTree.addDependency(productD, operationC);   // Produto depende de C

        PriorityQueue<ProductionNode> byDepth = new PriorityQueue<>(
                (node1, node2) -> Integer.compare(
                        node2.getDepth(productionTree),
                        node1.getDepth(productionTree)
                )
        );

        for (ProductionNode node : productionTree.getAllNodes()) {
            if (node.isOperation()) {
                byDepth.add(node);
            }
        }

        // Verificar que as operações com profundidade igual são tratadas corretamente
        List<ProductionNode> result = new ArrayList<>();
        while (!byDepth.isEmpty()) {
            result.add(byDepth.poll());
        }

        assertEquals(3, result.size(), "Deve haver 3 operações na fila.");
        assertTrue(result.contains(operationA), "Operação A deve estar presente.");
        assertTrue(result.contains(operationB), "Operação B deve estar presente.");
        assertTrue(result.contains(operationC), "Operação C deve estar presente.");
    }

    @Test
    void testEmptyProductionTree() {
        // Inicializar uma árvore vazia
        productionTree = new ProductionTree();

        // Validar que o caminho crítico está vazio
        List<ProductionNode> criticalPath = productionTree.getCriticalPath();
        assertTrue(criticalPath.isEmpty(), "Caminho crítico deve estar vazio para uma árvore vazia.");
    }


    @Test
    void testDisplayCriticalPathInSequence() {
        // Inicializar árvore de produção
        productionTree = new ProductionTree();

        // Configurar nós para teste
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
        productionTree.addDependency(productD, operationC);   // Produto depende de C

        // Validar ordem do caminho crítico
        List<ProductionNode> criticalPath = productionTree.getCriticalPath();
        assertEquals(4, criticalPath.size(), "Caminho crítico deve ter 4 nós.");
        assertEquals("Operação A", criticalPath.get(0).getName());
        assertEquals("Operação B", criticalPath.get(1).getName());
        assertEquals("Operação C", criticalPath.get(2).getName());
        assertEquals("Produto Final", criticalPath.get(3).getName());
    }

    @Test
    void testOperationsPrioritizedByDepthUsingHeap_AC1() throws IOException {
        ProductionTree testTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");
        DisplayProductionTree display = new DisplayProductionTree();
        display.setProductionTree(testTree);
        display.generateGraph();


        PriorityQueue<ProductionNode> priorityQueue = new PriorityQueue<>(
                (node1, node2) -> Integer.compare(
                        node2.getDepth(testTree),
                        node1.getDepth(testTree)
                )
        );

        for (ProductionNode node : testTree.getAllNodes()) {
            if (node.isOperation()) {
                priorityQueue.add(node);
            }
        }

        assertEquals(5, priorityQueue.size(), "A fila de prioridade deve conter as 5 operações da árvore.");

        List<String> expectedOrder = List.of(
                "cortar tábua de madeira",
                "montar caixa",
                "pregar cantos da caixa",
                "lixar caixa de madeira",
                "pintar caixa"

        );

        List<String> actualOrder = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            actualOrder.add(priorityQueue.poll().getName());
        }

        assertEquals(expectedOrder, actualOrder, "As operações devem ser priorizadas pela profundidade na fila.");
    }

    @Test
    void testCriticalPathDisplayByDepth_AC2() throws IOException {
        ProductionTree testTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");

        CriticalPathPrioritizer prioritizer = new CriticalPathPrioritizer();
        prioritizer.setProductionTree(testTree);

        // Verifica se o método exibe corretamente o caminho crítico
        assertDoesNotThrow(prioritizer::displayCriticalPathByDepth,
                "A exibição do caminho crítico por profundidade não deve lançar exceções.");
    }

    @Test
    void testCriticalPathOperationDepths_AC2_AC3() throws IOException {
        ProductionTree testTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");

        CriticalPathCalculator calculator = new CriticalPathCalculator();
        calculator.setProductionTree(testTree);

        List<ProductionNode> criticalPath = testTree.getCriticalPath();

        for (ProductionNode node : criticalPath) {
            int depth = node.getDepth(testTree);
            assertTrue(depth >= 0, "A profundidade de cada operação deve ser válida (>= 0).");
        }

        // Verifica profundidades específicas de algumas operações
        ProductionNode paintOperation = testTree.getNodeById("5");
        assertNotNull(paintOperation, "A operação 'Pintar caixa' deve existir na árvore.");
        assertEquals(1, paintOperation.getDepth(testTree), "A profundidade da operação 'Pintar caixa' deve ser 1.");
    }

    @Test
    void testTotalDependenciesForCriticalPathOperations() throws IOException {
        ProductionTree testTree = Reader.loadProductionTree("src/test/java/fourcorp/buildflow/operations_test.csv", "src/test/java/fourcorp/buildflow/items_test.csv", "src/test/java/fourcorp/buildflow/boo_test.csv");

        CriticalPathCalculator calculator = new CriticalPathCalculator();
        calculator.setProductionTree(testTree);
        // Obter o caminho crítico da árvore de produção
        List<ProductionNode> criticalPath = testTree.getCriticalPath();

        // Mapeamento esperado: Nome da Operação -> Número Total de Dependências
        Map<String, Integer> expectedDependencies = Map.of(
                "pintar caixa", 4,        // Depende de "Lixar caixa", "Pregar cantos da caixa", "Montar caixa", "Cortar tábua de madeira"
                "lixar caixa de madeira", 3,         // Depende de "Pregar cantos da caixa", "Montar caixa", "Cortar tábua de madeira"
                "pregar cantos da caixa", 2, // Depende de "Montar caixa", "Cortar tábua de madeira"
                "montar caixa", 1,        // Depende de "Cortar tábua de madeira"
                "cortar tábua de madeira", 0 // Não tem dependências
        );

        for (ProductionNode node : criticalPath) {
            // Verificar se o nó é uma operação
            if (node.isOperation()) {
                // Calcular todas as dependências da operação
                List<ProductionNode> dependencies = calculator.calculateAllDependencies(node);

                // Verificar se o número de dependências é o esperado
                assertEquals(
                        expectedDependencies.get(node.getName()),
                        dependencies.size(),
                        "O número total de dependências para " + node.getName() + " está incorreto."
                );
            }
        }
    }

}
