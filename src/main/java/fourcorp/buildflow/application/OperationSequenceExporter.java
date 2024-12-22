package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class responsible for exporting operation sequences derived from production trees.
 * This class processes production trees to extract operation sequences, maps these operations
 * to a limited integer range, and exports the results to a file.
 *
 * <p>Features include:
 * <ul>
 *   <li>Extracting operation sequences from a given {@code ProductionTree}.</li>
 *   <li>Mapping operation IDs to integers in the range 0-31 using {@link OperationIDMapper}.</li>
 *   <li>Exporting sequences to an external file in a format suitable for further processing.</li>
 * </ul>
 */
public class OperationSequenceExporter {

    /**
     * Mapper to translate operation IDs into unique integers within a limited range.
     */
    private final OperationIDMapper idMapper;

    /**
     * List of all extracted order sequences from production trees.
     */
    private final List<List<String>> allOrderSequences;

    /**
     * Constructs a new {@code OperationSequenceExporter} with an empty sequence list and an ID mapper.
     */
    public OperationSequenceExporter() {
        this.idMapper = new OperationIDMapper();
        this.allOrderSequences = new ArrayList<>();
    }

    /**
     * Adds an operation sequence extracted from the provided {@code ProductionTree}.
     * If the extracted sequence is empty, it is not added.
     *
     * @param productionTree the production tree from which to extract the sequence.
     */
    public void addOrderSequence(ProductionTree productionTree) {
        List<String> sequence = generateOperationSequence(productionTree);
        if (!sequence.isEmpty()) {
            allOrderSequences.add(sequence);
        }
    }

    /**
     * Exports all collected operation sequences to the specified file.
     * Each sequence is written as a series of integers, one per line.
     *
     * <p>Additionally, prints the mapping between original operation IDs and their mapped integers.
     *
     * @param outputFile the path to the output file.
     */
    public void exportAllSequences(String outputFile) {
        try (FileWriter writer = new FileWriter(outputFile)) {
            System.out.println("\nExporting sequences to: " + outputFile);

            for (List<String> sequence : allOrderSequences) {
                List<Integer> mappedSequence = mapOperationsToRange(sequence);

                // Write each mapped number to the file, one per line
                for (Integer number : mappedSequence) {
                    writer.write(number.toString() + "\n");
                }
            }

            // Print the mapping table to the console
            idMapper.printMapping();

        } catch (IOException e) {
            System.err.println("Error writing operation sequences: " + e.getMessage());
        }
    }

    /**
     * Generates a sequence of operation IDs from the given {@code ProductionTree}.
     * The sequence is determined by traversing the tree and extracting valid operation nodes.
     *
     * @param tree the production tree to process.
     * @return a list of operation IDs extracted from the tree.
     */
    private List<String> generateOperationSequence(ProductionTree tree) {
        List<String> sequence = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        // Traverse each root node in the tree
        List<ProductionNode> roots = tree.getRootNodes();
        for (ProductionNode root : roots) {
            traverseOperations(root, visited, sequence, tree);
        }

        return sequence;
    }

    /**
     * Traverses the production tree recursively, processing operation nodes before product nodes.
     * Ensures that nodes are visited only once to avoid cycles or redundant processing.
     *
     * @param node     the current node being traversed.
     * @param visited  a set of already visited node IDs.
     * @param sequence the sequence being built from operation nodes.
     * @param tree     the production tree being traversed.
     */
    private void traverseOperations(ProductionNode node, Set<String> visited,
                                    List<String> sequence, ProductionTree tree) {
        if (visited.contains(node.getId())) {
            return;
        }

        visited.add(node.getId());

        List<ProductionNode> children = tree.getChildren(node);

        // Process operation nodes first
        for (ProductionNode child : children) {
            if (!child.isProduct() && isOperationId(child.getId())) {
                sequence.add(child.getId());
                traverseOperations(child, visited, sequence, tree);
            }
        }

        // Process product nodes afterwards
        for (ProductionNode child : children) {
            if (child.isProduct()) {
                traverseOperations(child, visited, sequence, tree);
            }
        }
    }

    /**
     * Checks if the given ID represents a valid operation ID.
     * A valid operation ID consists entirely of numeric characters.
     *
     * @param id the ID to check.
     * @return {@code true} if the ID is numeric, {@code false} otherwise.
     */
    private boolean isOperationId(String id) {
        return id.matches("\\d+");
    }

    /**
     * Maps a list of operation IDs to their corresponding integers using the {@code OperationIDMapper}.
     * Filters out any IDs that do not represent valid operations.
     *
     * @param operationIds the list of operation IDs to map.
     * @return a list of mapped integers corresponding to the operation IDs.
     */
    private List<Integer> mapOperationsToRange(List<String> operationIds) {
        return operationIds.stream()
                .filter(this::isOperationId)
                .map(idMapper::mapOperationId)
                .toList();
    }
}