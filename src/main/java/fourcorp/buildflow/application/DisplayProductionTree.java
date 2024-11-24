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
     * The complexity of this method is: O(n^3).
     */
    public void displayTree() {
        System.out.println();
        for (ProductionNode node : productionTree.getAllNodes()) { // O(n)
            if (node.isProduct()) {
                printNode(node, 0, new HashSet<>()); // O (n^3)
                System.out.println();
            }
        }
    }

    /**
     * Recursively prints a production node and its sub-nodes in the tree structure.
     * This method also detects cycles to prevent infinite loops.
     * The complexity of this method is: O(n^2).
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
        for (Map.Entry<ProductionNode, Double> entry : subNodes.entrySet()) { // O(n)
            ProductionNode subNode = entry.getKey();
            double quantity = entry.getValue();

            System.out.println("  ".repeat(level + 1) + "|- " +
                    (subNode.isOperation() ? "OPERATION" : "NEEDED MATERIAL") + ": " +
                    subNode.getName() + (subNode.isOperation() ? "" : " (Q: " + quantity + ")"));

            printNode(subNode, level + 1, visitedNodes); // O(n^2)
        }

        visitedNodes.remove(node);
    }

    /**
     * Generates a sub-tree from the specified node identifier, updates the current production tree with the sub-tree,
     * generates its graph, and then restores the original production tree.
     * <p>
     * The complexity of this method is: **O(n)**
     *
     * @param nodeIdentifier the identifier (name or ID) of the root node to generate the sub-tree from.
     * @throws IllegalArgumentException if the node with the specified identifier is not found in the production tree.
     */
    public void loadSubTreeFromNode(String nodeIdentifier) {
        ProductionTree productionTree = Repositories.getInstance().getProductionTree();
        ProductionNode startNode = productionTree.getNodeByNameOrId(nodeIdentifier); // O(n)
        if (startNode == null) {
            throw new IllegalArgumentException("Node with identifier '" + nodeIdentifier + "' not found in the production tree.");
        }

        ProductionTree newPt = createSubTree(productionTree, startNode); // O(n)
        setProductionTree(newPt);
        generateGraph();
        setProductionTree(Repositories.getInstance().getProductionTree());
    }

    /**
     * Creates a sub-tree starting from a specified node by copying the node and its dependencies.
     * <p>
     * The complexity of this method is: **O(n)**.
     *
     * @param originalTree the original production tree.
     * @param startNode    the starting node to create the sub-tree from.
     * @return a new production tree containing the sub-tree rooted at the specified node.
     */
    private ProductionTree createSubTree(ProductionTree originalTree, ProductionNode startNode) {
        ProductionTree subTree = new ProductionTree();

        ProductionNode newStartNode = copyNode(startNode);
        subTree.addNode(newStartNode); // O(n)

        addNodeDependenciesIncludingResults(originalTree, subTree, startNode, newStartNode); // O(n)

        return subTree;
    }

    /**
     * Recursively adds dependencies and results of a node to the sub-tree.
     * <p>
     * The complexity of this method is: **O(n^2)**.
     *
     * @param originalTree the original production tree containing all nodes and dependencies.
     * @param subTree      the sub-tree being built.
     * @param originalNode the current node in the original tree.
     * @param newNode      the corresponding node in the sub-tree.
     */
    private void addNodeDependenciesIncludingResults(ProductionTree originalTree, ProductionTree subTree, ProductionNode originalNode, ProductionNode newNode) {
        for (var entry : originalTree.getSubNodes(originalNode).entrySet()) { // O(n)
            ProductionNode originalChild = entry.getKey();
            Double quantity = entry.getValue();

            ProductionNode newChild = subTree.getNodeById(originalChild.getId()); // O(1)
            if (newChild == null) {
                newChild = copyNode(originalChild);
                subTree.addNode(newChild);
            }

            subTree.addDependencyBom(newNode, newChild, quantity);

            if (!originalChild.isProduct()) {
                processOperationResults(originalTree, subTree, originalChild, newChild); // O(n^2)
            }

            addNodeDependenciesIncludingResults(originalTree, subTree, originalChild, newChild);  // O(1)
        }
    }

    /**
     * Processes operation results by identifying and copying nodes that depend on a specified operation.
     * <p>
     * The complexity of this method is: **O(n)**.
     *
     * @param originalTree the original production tree.
     * @param subTree      the sub-tree being built.
     * @param operation    the operation node in the original tree.
     * @param newOperation the corresponding operation node in the sub-tree.
     */
    private void processOperationResults(ProductionTree originalTree, ProductionTree subTree, ProductionNode operation, ProductionNode newOperation) {
        for (ProductionNode node : originalTree.getAllNodes()) { // O(n)
            Map<ProductionNode, Double> dependencies = originalTree.getSubNodes(node);

            if (dependencies.containsKey(operation)) {
                ProductionNode resultNode = subTree.getNodeById(node.getId()); // O(1)
                if (resultNode == null) {
                    resultNode = copyNode(node);
                    subTree.addNode(resultNode);
                }

                Double resultQuantity = dependencies.get(operation);
                subTree.addDependencyBom(resultNode, newOperation, resultQuantity);
            }
        }
    }

    /**
     * Creates a copy of a production node with the same properties.
     * <p>
     * The complexity of this method is: **O(1)**, as it involves creating and initializing a single object.
     *
     * @param original the original production node to copy.
     * @return a new production node with the same properties as the original.
     */
    private ProductionNode copyNode(ProductionNode original) {
        ProductionNode copy = new ProductionNode(original.getId(), original.getName(), original.isProduct());
        copy.setQuantity(original.getQuantity());  // O(1)
        return copy;
    }

    /**
     * Recursively generates the DOT representation for a given node and its sub-nodes.
     * The complexity of this method is: O(n^2).
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

        String shape, color;
        if (node.isOperation()) {
            shape = "rect"; // Retângulo para operações
            color = "lightblue";
        } else if (isResultOfOperation(node)) {
            shape = "hexagon"; // Oval para itens resultantes de operações
            color = "orange";
        } else {
            shape = "ellipse"; // Hexágono para itens standalone
            color = "lightgreen";
        }

        dotContent.append("  \"" + node.getId() + "\" [shape=" + shape + " style=filled fillcolor=" + color +
                " label=\"" + escapeForDot(node.getName()) + "\"];\n");

        for (Map.Entry<ProductionNode, Double> entry : productionTree.getSubNodes(node).entrySet()) { // O(n)
            ProductionNode subNode = entry.getKey();
            double quantity = entry.getValue();

            String edgeKey = node.getId() + "--" + subNode.getId();
            if (!processedEdges.contains(edgeKey)) {
                processedEdges.add(edgeKey); // O(1)
                dotContent.append("  \"" + node.getId() + "\" -- \"" + subNode.getId() + "\" [label=\"" + quantity + "\"];\n");
            }

            generateNodeDotRepresentation(subNode, dotContent, visitedNodes, processedEdges); // O(n^2)
        }
        visitedNodes.remove(node);
    }


    /**
     * Checks if a node is the result of an operation.
     *
     * @param node The node to check.
     * @return True if the node results from an operation, false otherwise.
     */
    private boolean isResultOfOperation(ProductionNode node) {
        return productionTree.getAllNodes().stream()
                .anyMatch(parent -> productionTree.getSubNodes(parent).containsKey(node) && parent.isOperation());
    }

    /**
     * Generates a Graphviz DOT representation of the production tree and writes it to a file.
     * It also calls Graphviz to generate an SVG image of the production tree.
     * The complexity of this method is: O(n^3).
     */
    public void generateGraph() {
        StringBuilder dotContent = new StringBuilder();
        dotContent.append("graph G {\n");
        dotContent.append("  splines=false;\n");
        dotContent.append("  nodesep=0.5;\n");
        dotContent.append("  ranksep=0.5;\n");

        Set<String> processedEdges = new HashSet<>();

        // Generate DOT representation for each product node
        for (ProductionNode node : productionTree.getAllNodes()) { // O(n)
            if (node.isProduct()) {
                generateNodeDotRepresentation(node, dotContent, new HashSet<>(), processedEdges); // O(n^3)
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
     * The complexity of this method is: O(n^2).
     */
    public void displayMaterialQuantitiesInProductionTree() {
        System.out.println();
        Map<ProductionNode, Map<ProductionNode, Double>> connections = productionTree.getConnections();
        if (connections.isEmpty()) {
            System.out.println("There are no connections to display.");
            return;
        }

        for (Map.Entry<ProductionNode, Map<ProductionNode, Double>> entry : connections.entrySet()) { // O(n)
            ProductionNode parentNode = entry.getKey();
            Map<ProductionNode, Double> childNodes = entry.getValue();

            for (Map.Entry<ProductionNode, Double> childEntry : childNodes.entrySet()) {  // O(n^2)
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
