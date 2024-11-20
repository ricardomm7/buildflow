package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class USEI11 {

    @Nested
    class QualityCheckManagerTest {
        private QualityCheckManager manager;
        private ProductionTree productionTree;

        @BeforeEach
        void setUp() {
            // Inicializar árvore de produção
            productionTree = new ProductionTree();

            // Configurar nós simulados
            ProductionNode operationA = new ProductionNode("A1", "Operação A", false);
            ProductionNode operationB = new ProductionNode("B1", "Operação B", false);
            ProductionNode productC = new ProductionNode("C1", "Produto C", true);

            // Adicionar nós à árvore
            productionTree.addNode(operationA);
            productionTree.addNode(operationB);
            productionTree.addNode(productC);

            // Definir dependências
            productionTree.addDependency(operationB, operationA); // B depende de A
            productionTree.addDependency(productC, operationB);  // C depende de B

            // Definir quantidades produzidas
            operationA.setQuantity(50);
            operationB.setQuantity(30);
            productC.setQuantity(20);

            // Inicializar o gerenciador de verificações
            manager = new QualityCheckManager(productionTree); // Passa a árvore diretamente
        }

        @Test
        void testDisplayAllOperations() {
            // AC: Listar todas as operações de forma organizada
            List<ProductionNode> allNodes = productionTree.getAllNodes();

            assertEquals(3, allNodes.size(), "Deve haver 3 nós no grafo");

            ProductionNode operationA = allNodes.get(0);
            assertEquals("Operação A", operationA.getName());
            assertEquals(50, operationA.getProducedQuantity());

            ProductionNode operationB = allNodes.get(1);
            assertEquals("Operação B", operationB.getName());
            assertEquals("Operação A", productionTree.getParentNodes(operationB).get(0).getName());
            assertEquals(30, operationB.getProducedQuantity());

            ProductionNode productC = allNodes.get(2);
            assertEquals("Produto C", productC.getName());
            assertEquals("Operação B", productionTree.getParentNodes(productC).get(0).getName());
            assertEquals(20, productC.getProducedQuantity());
        }

        @Test
        void testEmptyProductionTree() {
            // Gerenciador com uma árvore vazia
            ProductionTree emptyTree = new ProductionTree();
            QualityCheckManager emptyManager = new QualityCheckManager(emptyTree);

            List<ProductionNode> allNodes = emptyTree.getAllNodes();
            assertTrue(allNodes.isEmpty(), "A árvore de produção deve estar vazia");
        }

        @Test
        void testInvalidQuantityHandling() {
            // Configura uma operação com quantidade inválida
            ProductionNode invalidNode = new ProductionNode("E1", "Operação Inválida", false);
            productionTree.addNode(invalidNode);

            invalidNode.setQuantity(-10);
            assertEquals(-10, invalidNode.getProducedQuantity(), "Quantidade inválida deve ser detectada");
        }
    }
}
