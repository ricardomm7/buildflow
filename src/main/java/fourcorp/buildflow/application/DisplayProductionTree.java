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
        System.out.println();
        for (ProductionNode node : productionTree.getAllNodes()) {
            if (node.isProduct()) {
                printNode(node, 0, new HashSet<>());
                System.out.println();
            }
        }
    }

    private void printNode(ProductionNode node, int level, Set<ProductionNode> visitedNodes) {
        if (!visitedNodes.add(node)) {
            System.out.println("  ".repeat(level) + "[CYCLE DETECTED - ABORTING] " + node.getId());
            return;
        }

        System.out.println("  ".repeat(level) + node.getId() + " - " + node.getName());

        Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(node);
        for (Map.Entry<ProductionNode, Double> entry : subNodes.entrySet()) {
            ProductionNode subNode = entry.getKey();
            double quantity = entry.getValue();

            System.out.println("  ".repeat(level + 1) + "|- " +
                    (subNode.isOperation() ? "OPERATION" : "NEEDED MATERIAL") + ": " +
                    subNode.getName() + (subNode.isOperation() ? "" : " (Q: " + quantity + ")"));

            printNode(subNode, level + 1, visitedNodes);
        }

        visitedNodes.remove(node);
    }

    public void generateGraph() {
        StringBuilder dotContent = new StringBuilder();
        dotContent.append("graph G {\n");
        dotContent.append("  splines=false;\n");
        dotContent.append("  nodesep=0.5;\n");
        dotContent.append("  ranksep=0.5;\n");

        Set<String> processedEdges = new HashSet<>();

        for (ProductionNode node : productionTree.getAllNodes()) {
            if (node.isProduct()) {
                generateNodeDotRepresentation(node, dotContent, new HashSet<>(), processedEdges);
            }
        }
        dotContent.append("}\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("outFiles/production_tree.dot"))) {
            writer.write(dotContent.toString());
            System.out.println("File .dot generated!");
        } catch (IOException e) {
            System.err.println("Error writing to .dot file: " + e.getMessage());
        }

        generateGraphVizSVG();
    }

    private void generateNodeDotRepresentation(ProductionNode node, StringBuilder dotContent, Set<ProductionNode> visitedNodes, Set<String> processedEdges) {
        if (!visitedNodes.add(node)) {
            dotContent.append("  \"" + node.getId() + "\" [style=dashed color=red];\n");
            return;
        }

        String shape = node.isProduct() ? "box" : "hexagon";
        String label = escapeForDot(node.getName());
        dotContent.append("  \"" + node.getId() + "\" [shape=" + shape + " label=\"" + label + "\"];\n");

        Map<ProductionNode, Double> subNodes = productionTree.getSubNodes(node);
        for (Map.Entry<ProductionNode, Double> entry : subNodes.entrySet()) {
            ProductionNode subNode = entry.getKey();
            double quantity = entry.getValue();

            String edgeKey = node.getId() + "--" + subNode.getId();

            if (!processedEdges.contains(edgeKey)) {
                processedEdges.add(edgeKey);
                String edgeLabel = "" + quantity;
                dotContent.append("  \"" + node.getId() + "\" -- \"" + subNode.getId() + "\" [label=\"" + edgeLabel + "\"];\n");
            }

            generateNodeDotRepresentation(subNode, dotContent, visitedNodes, processedEdges);
        }

        visitedNodes.remove(node);
    }

    private String escapeForDot(String input) {
        return input.replace("\"", "\\\"");
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

    public void displayMaterialQuantitiesInProductionTree() {
        System.out.println();
        Map<ProductionNode, Map<ProductionNode, Double>> connections = productionTree.getConnections();
        if (connections.isEmpty()) {
            System.out.println("There are no connections to display.");
            return;
        }

        for (Map.Entry<ProductionNode, Map<ProductionNode, Double>> entry : connections.entrySet()) {
            ProductionNode parentNode = entry.getKey();
            Map<ProductionNode, Double> childNodes = entry.getValue();

            for (Map.Entry<ProductionNode, Double> childEntry : childNodes.entrySet()) {
                ProductionNode childNode = childEntry.getKey();
                Double quantity = childEntry.getValue();

                System.out.println("Father Node: " + parentNode.getName() + " (ID: " + parentNode.getId() + ") -> "
                        + "Son Node: " + childNode.getName() + " (ID: " + childNode.getId() + ")"
                        + " | Quantity: " + quantity);
            }
        }
    }
}
