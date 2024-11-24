package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;

import java.io.IOException;
import java.util.Map;

public class BooTreeBuilder {

    public static ProductionTree loadSubTreeFromNode(String operationsFile, String itemsFile, String booFile, String nodeIdentifier) throws IOException {
        ProductionTree productionTree = Reader.loadProductionTree(operationsFile, itemsFile, booFile);
        ProductionNode startNode = productionTree.getNodeByNameOrId(nodeIdentifier);
        if (startNode == null) {
            throw new IllegalArgumentException("Node with identifier '" + nodeIdentifier + "' not found in the production tree.");
        }

        return createSubTree(productionTree, startNode);
    }

    private static ProductionTree createSubTree(ProductionTree originalTree, ProductionNode startNode) {
        ProductionTree subTree = new ProductionTree();

        // Adicionar o nó inicial na subárvore
        ProductionNode newStartNode = copyNode(startNode);
        subTree.addNode(newStartNode);

        // Adicionar dependências recursivamente
        addNodeDependenciesIncludingResults(originalTree, subTree, startNode, newStartNode);

        return subTree;
    }

    private static void addNodeDependenciesIncludingResults(ProductionTree originalTree, ProductionTree subTree,
                                                          ProductionNode originalNode, ProductionNode newNode) {
        // Processar dependências diretas do nó
        for (var entry : originalTree.getSubNodes(originalNode).entrySet()) {
            ProductionNode originalChild = entry.getKey();
            Double quantity = entry.getValue();

            // Criar ou obter o nó filho na subárvore
            ProductionNode newChild = subTree.getNodeById(originalChild.getId());
            if (newChild == null) {
                newChild = copyNode(originalChild);
                subTree.addNode(newChild);
            }

            // Adicionar a dependência com a quantidade correta
            subTree.addDependencyBom(newNode, newChild, quantity);

            // Se o nó filho é uma operação, procurar por seus resultados (Items que a têm como dependência)
            if (!originalChild.isProduct()) {
                processOperationResults(originalTree, subTree, originalChild, newChild);
            }

            // Continuar recursão para os nós filhos
            addNodeDependenciesIncludingResults(originalTree, subTree, originalChild, newChild);
        }
    }

    private static void processOperationResults(ProductionTree originalTree, ProductionTree subTree,
                                              ProductionNode operation, ProductionNode newOperation) {
        // Para cada nó na árvore original
        for (ProductionNode node : originalTree.getAllNodes()) {
            Map<ProductionNode, Double> dependencies = originalTree.getSubNodes(node);

            // Se este nó tem a operação como dependência, ele é um resultado da operação
            if (dependencies.containsKey(operation)) {
                // Criar ou obter o nó resultado na subárvore
                ProductionNode resultNode = subTree.getNodeById(node.getId());
                if (resultNode == null) {
                    resultNode = copyNode(node);
                    subTree.addNode(resultNode);
                }

                // Importante: Agora invertemos a relação - o Item resultante é pai da operação
                Double resultQuantity = dependencies.get(operation);
                // O Item resultante (resultNode) se torna pai da operação (newOperation)
                subTree.addDependencyBom(resultNode, newOperation, resultQuantity);
            }
        }
    }

    private static ProductionNode copyNode(ProductionNode original) {
        ProductionNode copy = new ProductionNode(
                original.getId(),
                original.getName(),
                original.isProduct()
        );
        copy.setQuantity(original.getQuantity());
        return copy;
    }

    public static void main(String[] args) {
        try {
            ProductionTree booTree = loadSubTreeFromNode("textFiles/operations.csv", "textFiles/items.csv", "textFiles/boo_v2.csv", "1006");
            DisplayProductionTree display = new DisplayProductionTree();
            display.setProductionTree(booTree);
            display.generateGraph();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}