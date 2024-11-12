package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import fourcorp.buildflow.repository.Repositories;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DisplayProductionTree {

    private ProductionTree productionTree = Repositories.getInstance().getProductionTree();

    public void displayTree() {
        System.out.println("Árvore de Produção:");
        for (ProductionNode node : productionTree.getAllProductionNodes().values()) {
            if (node.isProduct()) {  // Filtra apenas os produtos
                printNode(node, 0, new HashSet<>());
            }
        }
    }

    private void printNode(ProductionNode node, int level, Set<ProductionNode> visitedNodes) {
        if (!visitedNodes.add(node)) {
            System.out.println("  ".repeat(level) + "[CICLO DETECTADO] " + node.getId() + " - " + node.getName());
            return;
        }

        System.out.println("  ".repeat(level) + node.getId() + " - " + node.getName());
        node.getSubNodes().forEach((subNode, quantity) -> {
            System.out.println("  ".repeat(level + 1) + "|- " +
                    (subNode.isOperation() ? "Operação" : "Material Necessário") + ": " +
                    subNode.getName() + " (Q: " + quantity + ")");
            printNode(subNode, level + 1, visitedNodes);
        });

        visitedNodes.remove(node);
    }

    public void generateGraph() {
        StringBuilder dotContent = new StringBuilder();
        dotContent.append("digraph G {\n");
        dotContent.append("  splines=false;\n");
        for (ProductionNode node : productionTree.getAllProductionNodes().values()) {
            if (node.isProduct()) {
                generateNodeDotRepresentation(node, dotContent, new HashSet<>());
            }
        }
        dotContent.append("}\n");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("outFiles/production_tree.dot"))) {
            writer.write(dotContent.toString());
            System.out.println("Arquivo .dot gerado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo .dot: " + e.getMessage());
        }
        generateGraphVizSVG();
    }

    private void generateNodeDotRepresentation(ProductionNode node, StringBuilder dotContent, Set<ProductionNode> visitedNodes) {
        if (!visitedNodes.add(node)) {
            dotContent.append("  \"" + node.getId() + "\" [style=dashed color=red];\n");
            return;
        }

        String shape = node.isProduct() ? "box" : "hexagon";
        dotContent.append("  \"" + node.getId() + "\" [shape=" + shape + "];\n");

        for (Map.Entry<ProductionNode, Integer> entry : node.getSubNodes().entrySet()) {
            ProductionNode subNode = entry.getKey();
            int quantity = entry.getValue();
            String edgeLabel = "  ";
            if (entry.getKey().isProduct()) {
                edgeLabel = "Q: " + quantity;
            }
            dotContent.append("  \"" + node.getId() + "\" -> \"" + subNode.getId() + "\" [label=\"" + edgeLabel + "\"];\n");
            generateNodeDotRepresentation(subNode, dotContent, visitedNodes);
        }

        visitedNodes.remove(node);
    }

    private void generateGraphVizSVG() {
        try {
            String command = "dot -Tsvg outFiles/production_tree.dot -o outFiles/production_tree.svg";
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Graph image generated successfully!");
            } else {
                System.out.println("Error: Failed to generate the graph image. Exit code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
