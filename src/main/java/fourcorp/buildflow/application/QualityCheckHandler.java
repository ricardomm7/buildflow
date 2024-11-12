package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;

import java.util.PriorityQueue;

public class QualityCheckHandler {
/*
    // Fila de prioridade para armazenar verificações de qualidade
    private PriorityQueue<ProductionNode> qualityCheckQueue;

    public QualityCheckHandler() {
        // Ordena os nós com base na profundidade em ordem decrescente (maior prioridade primeiro)
        qualityCheckQueue = new PriorityQueue<>((node1, node2) -> Integer.compare(node2.getDepth(), node1.getDepth()));
    }

    // Adiciona uma verificação de qualidade com base num nó de operação
    public void addQualityCheck(ProductionNode node) {
        qualityCheckQueue.add(node);
    }

    // Exibe as verificações de qualidade em ordem de prioridade (sem removê-las)
    public void displayQualityChecks() {
        // Converter para uma lista ordenada para exibição (opcional)
        PriorityQueue<ProductionNode> tempQueue = new PriorityQueue<>(qualityCheckQueue);
        while (!tempQueue.isEmpty()) {
            ProductionNode check = tempQueue.poll();
            //System.out.println("Operação: " + check.getOperation() + " | Prioridade (Profundidade): " + check.getDepth());
        }
    }

    // Realiza a próxima verificação de qualidade
    public void performQualityCheck() {
        while (!isQueueEmpty()) {

            if (!qualityCheckQueue.isEmpty()) {
                ProductionNode nextCheck = qualityCheckQueue.poll(); // Remove e retorna o elemento de maior prioridade
               // System.out.println("Executando verificação de qualidade: " + nextCheck.getOperation() + " (Profundidade: " + nextCheck.getDepth() + ")");
            } else {
                System.out.println("Nenhuma verificação de qualidade pendente.");
            }
        }
    }

    // Verifica se a fila está vazia
    public boolean isQueueEmpty() {
        return qualityCheckQueue.isEmpty();
    }

 */
}
