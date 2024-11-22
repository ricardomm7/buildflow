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

/**
 * The DisplayProductionTree class is responsible for displaying the production tree in different formats.
 * It can display the tree structure in the console, generate a DOT representation of the tree,
 * and create a visual graph using Graphviz. Additionally, it can display the material quantities
 * in the production tree and handle cycles in the tree structure.
 */
public class DisplayProductionTree {
    private ProductionTree productionTree = Repositories.getInstance().getProductionTree();

    /**
     * Displays the production tree structure in the console.
     * It prints the nodes and their respective sub-nodes recursively.
     */
    public void displayTree() {
        System.out.println();
        for (ProductionNode node : productionTree.getAllNodes()) {
            if (node.isProduct()) {
                printNode(node, 0, new HashSet<>());
                System.out.println();
            }
        }
    }

    /**
     * Recursively prints a production node and its sub-nodes in the tree structure.
     * This method also detects cycles to prevent infinite loops.
     *
     * @param node         The current production node.
     * @param level        The level of the current node in the tree (used for indentation).
     * @param visitedNodes A set of visited nodes to detect cycles in the tree.
     */
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

    /**
     * Generate graph from node.
     *
     * @param rootId the root id
     */
    public void generateGraphFromNode(String rootId) {
        ProductionNode rootNode = productionTree.getNodeById(rootId);
        if (rootNode == null) {
            System.out.println("Node with ID " + rootId + " not found.");
            return;
        }

        StringBuilder dotContent = new StringBuilder();
        dotContent.append("graph G {\n");
        dotContent.append("  splines=false;\n");
        dotContent.append("  nodesep=0.5;\n");
        dotContent.append("  ranksep=0.5;\n");

        Set<String> processedEdges = new HashSet<>();

        generateNodeDotRepresentation(rootNode, dotContent, new HashSet<>(), processedEdges);
        dotContent.append("}\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("outFiles/production_tree.dot"))) {
            writer.write(dotContent.toString());
            System.out.println("File .dot generated for node " + rootId + "!");
        } catch (IOException e) {
            System.err.println("Error writing to .dot file: " + e.getMessage());
        }

        generateGraphVizSVG();
    }

    /**
     * Generates a Graphviz DOT representation of the production tree and writes it to a file.
     * It also calls Graphviz to generate an SVG image of the production tree.
     */
    public void generateGraph() {
        StringBuilder dotContent = new StringBuilder();
        dotContent.append("graph G {\n");
        dotContent.append("  splines=false;\n");
        dotContent.append("  nodesep=0.5;\n");
        dotContent.append("  ranksep=0.5;\n");

        Set<String> processedEdges = new HashSet<>();

        // Generate DOT representation for each product node
        for (ProductionNode node : productionTree.getAllNodes()) {
            if (node.isProduct()) {
                generateNodeDotRepresentation(node, dotContent, new HashSet<>(), processedEdges);
            }
        }
        dotContent.append("}\n");

        // Write the DOT content to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("outFiles/production_tree.dot"))) {
            writer.write(dotContent.toString());
            System.out.println("File .dot generated!");
        } catch (IOException e) {
            System.err.println("Error writing to .dot file: " + e.getMessage());
        }

        // Generate SVG image using Graphviz
        generateGraphVizSVG();
    }

    /**
     * Recursively generates the DOT representation for a given node and its sub-nodes.
     *
     * @param node           The current node to represent.
     * @param dotContent     The StringBuilder containing the DOT content.
     * @param visitedNodes   A set of visited nodes to prevent cycles in the DOT graph.
     * @param processedEdges A set of processed edges to prevent duplicate edges.
     */
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

    /**
     * Escapes the input string for proper usage in DOT notation.
     *
     * @param input The input string to escape.
     * @return The escaped string.
     */
    private String escapeForDot(String input) {
        return input.replace("\"", "\\\"");
    }

    /**
     * Calls Graphviz to generate an SVG image of the production tree from the DOT file.
     */
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

    /**
     * Displays the material quantities in the production tree structure.
     * It prints the quantities of materials used in the production process.
     */
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
                Double connectionQuantity = childEntry.getValue();

                System.out.println("Father Node: " + parentNode.getName() + " (ID: " + parentNode.getId() + ")" +
                        " | Quantity: " + parentNode.getQuantity() +
                        " -> Son Node: " + childNode.getName() + " (ID: " + childNode.getId() + ")" +
                        " | Connection Quantity: " + connectionQuantity +
                        " | Child Node Quantity: " + childNode.getQuantity());
            }
        }
    }

    /**
     * Sets a new production tree for this DisplayProductionTree instance.
     * This is useful for changing the tree dynamically.
     *
     * @param productionTree The new ProductionTree to set.
     */
    public void setProductionTree(ProductionTree productionTree) {
        this.productionTree = productionTree;
    }
}
