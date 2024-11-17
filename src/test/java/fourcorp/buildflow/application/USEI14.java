package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class USEI14 {

    @Nested
    class CriticalPathManagerTest {
        private ProductionTree productionTree;

        @BeforeEach
        void setUp() {
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
        }


        @Test
        void testCriticalPathPriority() {
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
            assert first != null;
            assertEquals("Produto D", first.getName(), "Primeiro no caminho crítico deve ser o mais profundo");

            ProductionNode second = queue.poll();
            assert second != null;
            assertEquals("Operação C", second.getName(), "Segundo deve ser o próximo mais profundo");

            // AC3: Caminho crítico como sequência
            List<ProductionNode> criticalPath = productionTree.getCriticalPath();
            assertEquals(4, criticalPath.size(), "Caminho crítico deve ter 4 operações");
            assertEquals("Operação A", criticalPath.get(0).getName());
            assertEquals("Produto D", criticalPath.get(3).getName());
        }

    }
}
