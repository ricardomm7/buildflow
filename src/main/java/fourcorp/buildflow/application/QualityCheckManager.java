package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.util.List;
import java.util.stream.Collectors;

public class QualityCheckManager {
    private final ProductionTree productionTree;

    public QualityCheckManager() {
        // Obter o grafo de produção do repositório
        this.productionTree = Repositories.getInstance().getProductionTree();
    }
    public QualityCheckManager(ProductionTree productionTree) {
        this.productionTree = productionTree;
    }

    /**
     * Exibe todas as operações lidas do repositório de maneira organizada.
     */
    public void displayAllOperations() {
        System.out.println("\n--- Lista de Operações ---");

        // Obter todos os nós do grafo de produção
        List<ProductionNode> allNodes = productionTree.getAllNodes();

        for (ProductionNode node : allNodes) {
            // Exibir detalhes para cada operação/nó
            System.out.println("Operação/Produto: " + node.getName());
            System.out.println("    ID: " + node.getId());
            System.out.println("    Tipo: " + (node.isOperation() ? "Operação" : "Produto"));
            System.out.println("    Dependências: " + formatDependencies(node));
            System.out.println("    Quantidade Produzida: " + node.getProducedQuantity());
            System.out.println("---------------------------");
        }
    }

    /**
     * Formata as dependências de um nó, obtidas do grafo de produção.
     */
    private String formatDependencies(ProductionNode node) {
        // Obter nós dependentes usando o método do repositório
        List<ProductionNode> dependencies = productionTree.getParentNodes(node);

        if (dependencies.isEmpty()) {
            return "Nenhuma";
        }

        return dependencies.stream()
                .map(ProductionNode::getName)
                .collect(Collectors.joining(", "));
    }
}
