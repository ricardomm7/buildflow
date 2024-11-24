package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for building a Bill of Materials (BOM) tree from input files containing production and item data.
 * The class processes a BOO (Bill of Operations) file and an items file to construct a {@link ProductionTree}.
 */
public class BomTreeBuilder {

    /**
     * Creates a Bill of Materials (BOM) tree from a BOO file and an items file, starting from the requested production node.
     * <p>
     * This method reads and parses data from the BOO and items files to create a hierarchical representation of
     * production nodes and their dependencies in a {@link ProductionTree}.
     * <p>
     * **Steps**:
     * 1. Parse the items file to map item IDs to their names.
     * 2. Parse the BOO file to extract direct connections and operation dependencies.
     * 3. Build the production tree recursively starting from the given requested node.
     *
     * @param booFilePath   the path to the BOO file (Bill of Operations).
     * @param itemsFilePath the path to the items file, containing item IDs and names.
     * @param requestedNode the starting node for the tree, representing the product to be built.
     * @return a {@link ProductionTree} representing the BOM rooted at the requested node.
     * @throws IOException if an error occurs while reading the files.
     */
    public static ProductionTree createBOMTree(String booFilePath, String itemsFilePath, ProductionNode requestedNode) throws IOException {
        ProductionTree tree = new ProductionTree();
        Map<String, String> itemNames = new HashMap<>();
        Map<String, List<Pair<String, Double>>> directConnections = new HashMap<>();
        Map<String, String> operationResults = new HashMap<>();
        Map<String, List<Pair<String, Double>>> operationDependencies = new HashMap<>();

        // First load all items
        try (BufferedReader br = new BufferedReader(new FileReader(itemsFilePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String itemId = parts[0];
                String itemName = parts[1];
                itemNames.put(itemId, itemName);
            }
        }

        // Process BOO file to get both direct connections and operation dependencies
        try (BufferedReader br = new BufferedReader(new FileReader(booFilePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] mainParts = line.split(";\\(;");
                if (mainParts.length != 3) continue;

                // Process header
                String[] headerParts = mainParts[0].split(";");
                String opId = headerParts[0];
                String resultItemId = headerParts[1];

                // Initialize lists if not exists
                operationDependencies.putIfAbsent(opId, new ArrayList<>());
                directConnections.putIfAbsent(resultItemId, new ArrayList<>());

                // Store operation result
                operationResults.put(opId, resultItemId);

                // Process operations dependencies
                String[] operationsPart = mainParts[1].replace(");", "").split(";");
                for (int i = 0; i < operationsPart.length - 1; i += 2) {
                    if (!operationsPart[i].isEmpty()) {
                        String depOpId = operationsPart[i];
                        String qtdStr = operationsPart[i + 1].replace(",", ".");
                        double opQtd = Double.parseDouble(qtdStr);
                        operationDependencies.get(opId).add(new Pair<>(depOpId, opQtd));
                    }
                }

                // Process direct item connections
                String[] itemsPart = mainParts[2].replace(");", "").split(";");
                for (int i = 0; i < itemsPart.length - 1; i += 2) {
                    if (!itemsPart[i].isEmpty()) {
                        String childItemId = itemsPart[i];
                        String qtdStr = itemsPart[i + 1].replace(",", ".");
                        double quantity = Double.parseDouble(qtdStr);
                        directConnections.get(resultItemId).add(new Pair<>(childItemId, quantity));
                    }
                }
            }
        }

        // Create initial node
        String itemName = itemNames.get(requestedNode.getId());
        ProductionNode rootNode = new ProductionNode(requestedNode.getId(), itemName, true);
        rootNode.setQuantity(requestedNode.getQuantity());
        tree.addNode(rootNode);

        // Create tree starting from requested node
        buildTreeRecursively(rootNode, tree, itemNames, directConnections,
                operationResults, operationDependencies, new HashMap<>());

        return tree;
    }

    /**
     * Recursively builds the BOM tree starting from a given node.
     * <p>
     * This method processes direct connections and operation dependencies to populate the {@link ProductionTree}
     * with nodes and their hierarchical relationships.
     * <p>
     * **Steps**:
     * 1. For direct connections, add child nodes to the tree and update their quantities.
     * 2. For operation dependencies, resolve dependencies and their quantities to build the tree structure.
     *
     * @param currentNode           the current node being processed in the tree.
     * @param tree                  the {@link ProductionTree} being built.
     * @param itemNames             a map of item IDs to their names.
     * @param directConnections     a map of item IDs to their direct child connections and quantities.
     * @param operationResults      a map of operation IDs to their result item IDs.
     * @param operationDependencies a map of operation IDs to their dependencies and quantities.
     * @param processedOperations   a map tracking already processed operations to avoid duplication.
     */
    private static void buildTreeRecursively(ProductionNode currentNode,
                                             ProductionTree tree,
                                             Map<String, String> itemNames,
                                             Map<String, List<Pair<String, Double>>> directConnections,
                                             Map<String, String> operationResults,
                                             Map<String, List<Pair<String, Double>>> operationDependencies,
                                             Map<String, Double> processedOperations) {

        String currentItemId = currentNode.getId();
        double currentQuantity = currentNode.getQuantity();

        // Process direct connections
        if (directConnections.containsKey(currentItemId)) {
            for (Pair<String, Double> connection : directConnections.get(currentItemId)) {
                String childId = connection.getFirst();
                double baseQuantity = connection.getSecond();
                double totalQuantity = baseQuantity * currentQuantity;

                // Create or get child node
                ProductionNode childNode = tree.getNodeById(childId);
                if (childNode == null) {
                    String childName = itemNames.get(childId);
                    childNode = new ProductionNode(childId, childName, true);
                    childNode.setQuantity(totalQuantity);
                    tree.addNode(childNode);
                } else {
                    double updatedQuantity = childNode.getQuantity() + totalQuantity;
                    childNode.setQuantity(updatedQuantity);
                }

                tree.addDependencyBom(currentNode, childNode, totalQuantity);

                buildTreeRecursively(childNode, tree, itemNames, directConnections,
                        operationResults, operationDependencies, processedOperations);
            }
        }

        // Process operation dependencies
        for (Map.Entry<String, String> entry : operationResults.entrySet()) {
            String opId = entry.getKey();
            String resultItemId = entry.getValue();

            if (resultItemId.equals(currentItemId) && !processedOperations.containsKey(opId)) {
                processedOperations.put(opId, currentQuantity);

                List<Pair<String, Double>> depOps = operationDependencies.get(opId);
                if (depOps != null) {
                    for (Pair<String, Double> depOp : depOps) {
                        String depOpId = depOp.getFirst();
                        double opQuantity = depOp.getSecond();
                        String depItemId = operationResults.get(depOpId);

                        if (depItemId != null) {
                            // Use the operation quantity directly without multiplying by current quantity
                            double totalQuantity = opQuantity;

                            ProductionNode depNode = tree.getNodeById(depItemId);
                            if (depNode == null) {
                                String depItemName = itemNames.get(depItemId);
                                depNode = new ProductionNode(depItemId, depItemName, true);
                                depNode.setQuantity(totalQuantity);
                                tree.addNode(depNode);
                            } else {
                                double updatedQuantity = depNode.getQuantity() + totalQuantity;
                                depNode.setQuantity(updatedQuantity);
                            }

                            tree.addDependencyBom(currentNode, depNode, totalQuantity);

                            buildTreeRecursively(depNode, tree, itemNames, directConnections,
                                    operationResults, operationDependencies, processedOperations);
                        }
                    }
                }
            }
        }
    }

    /**
     * A simple generic class representing a pair of values.
     *
     * @param <T> the type of the first value.
     * @param <U> the type of the second value.
     */
    private static class Pair<T, U> {
        private final T first;
        private final U second;

        /**
         * Constructs a new pair with the given values.
         *
         * @param first  the first value.
         * @param second the second value.
         */
        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        /**
         * Returns the first value of the pair.
         *
         * @return the first value.
         */
        public T getFirst() {
            return first;
        }

        /**
         * Returns the second value of the pair.
         *
         * @return the second value.
         */
        public U getSecond() {
            return second;
        }
    }
}