package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import org.junit.jupiter.api.Test;

public class USEI15 {
    @Test
    public void testSimulateExecution() {
        // Criando nós de operação (não são produtos)
        ProductionNode op1 = new ProductionNode("1", "Operation1", false);
        ProductionNode op2 = new ProductionNode("2", "Operation2", false);
        ProductionNode op3 = new ProductionNode("3", "Operation3", false);

        // Adicionando dependências entre as operações
        op2.setParent(op1);  // op2 depende de op1
        op3.setParent(op1);  // op3 depende de op1
        op3.setParent(op2);  // op3 depende de op2 (op3 depende de op1 e op2)

        // Criando a árvore de produção e o simulador
        ProductionTree productionTree = new ProductionTree();
        productionTree.addNode(op1);
        productionTree.addNode(op2);
        productionTree.addNode(op3);

        ProductionTreeSearcher searcher = new ProductionTreeSearcher();

        // Simula a execução da produção
        searcher.simulateProductionExecution();

        // Agora precisamos verificar se as operações são processadas na ordem correta
        // Esperamos que as operações sejam processadas da seguinte maneira:
        // 1. op1 (sem dependências)
        // 2. op2 (depende de op1)
        // 3. op3 (depende de op2, e op2 depende de op1)

        // Abaixo está apenas um exemplo de como podemos verificar a ordem de execução
        // Se você modificar a implementação para registrar ou imprimir a ordem, você pode verificar a ordem aqui.

        // Para simplificação, você pode testar se a árvore AVL ou a lista de operações está ordenada corretamente
        // com base no nível de dependência.
    }
}
