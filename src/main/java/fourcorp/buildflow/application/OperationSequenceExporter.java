package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class OperationSequenceExporter {
    private final OperationIDMapper idMapper;
    private final List<List<String>> allOrderSequences;

    public OperationSequenceExporter() {
        this.idMapper = new OperationIDMapper();
        this.allOrderSequences = new ArrayList<>();
    }

    public void addOrderSequence(ProductionTree productionTree) {
        List<String> sequence = generateOperationSequence(productionTree);
        if (!sequence.isEmpty()) {
            allOrderSequences.add(sequence);
        }
    }

    public void exportAllSequences(String outputFile) {
        try (FileWriter writer = new FileWriter(outputFile)) {
            System.out.println("\nExporting sequences to: " + outputFile);

            // Process each order's sequence
            for (List<String> sequence : allOrderSequences) {
                List<Integer> mappedSequence = mapOperationsToRange(sequence);

                // Write each number on its own line
                for (Integer number : mappedSequence) {
                    writer.write(number.toString() + "\n");
                }
            }

            // Print the mapping table
            idMapper.printMapping();

        } catch (IOException e) {
            System.err.println("Error writing operation sequences: " + e.getMessage());
        }
    }

    private List<String> generateOperationSequence(ProductionTree tree) {
        List<String> sequence = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        List<ProductionNode> roots = tree.getRootNodes();
        for (ProductionNode root : roots) {
            traverseOperations(root, visited, sequence, tree);
        }

        return sequence;
    }

    private void traverseOperations(ProductionNode node, Set<String> visited,
                                  List<String> sequence, ProductionTree tree) {
        if (visited.contains(node.getId())) {
            return;
        }

        visited.add(node.getId());

        List<ProductionNode> children = tree.getChildren(node);

        // Process operations first
        for (ProductionNode child : children) {
            if (!child.isProduct() && isOperationId(child.getId())) {
                sequence.add(child.getId());
                traverseOperations(child, visited, sequence, tree);
            }
        }

        // Then process product nodes
        for (ProductionNode child : children) {
            if (child.isProduct()) {
                traverseOperations(child, visited, sequence, tree);
            }
        }
    }

    private boolean isOperationId(String id) {
        return id.matches("\\d+");
    }

    private List<Integer> mapOperationsToRange(List<String> operationIds) {
        return operationIds.stream()
            .filter(this::isOperationId)
            .map(idMapper::mapOperationId)
            .toList();
    }
}