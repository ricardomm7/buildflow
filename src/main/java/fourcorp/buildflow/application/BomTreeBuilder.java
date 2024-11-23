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

public class BomTreeBuilder {

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
                operationResults, operationDependencies);

        return tree;
    }

    private static void buildTreeRecursively(ProductionNode currentNode,
                                             ProductionTree tree,
                                             Map<String, String> itemNames,
                                             Map<String, List<Pair<String, Double>>> directConnections,
                                             Map<String, String> operationResults,
                                             Map<String, List<Pair<String, Double>>> operationDependencies) {

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
                        operationResults, operationDependencies);
            }
        }

        // Process operation dependencies
        for (Map.Entry<String, String> entry : operationResults.entrySet()) {
            String opId = entry.getKey();
            String resultItemId = entry.getValue();

            if (resultItemId.equals(currentItemId)) {
                List<Pair<String, Double>> depOps = operationDependencies.get(opId);
                if (depOps != null) {
                    for (Pair<String, Double> depOp : depOps) {
                        String depOpId = depOp.getFirst();
                        double opQuantity = depOp.getSecond();
                        String depItemId = operationResults.get(depOpId);

                        if (depItemId != null) {
                            double totalQuantity = opQuantity * currentQuantity;

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
                                    operationResults, operationDependencies);
                        }
                    }
                }
            }
        }
    }

    private static class Pair<T, U> {
        private final T first;
        private final U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }
    }
}