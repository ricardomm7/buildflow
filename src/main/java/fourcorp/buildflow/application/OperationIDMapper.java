package fourcorp.buildflow.application;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for mapping operation IDs (originally strings) to unique integers within a specific range (0-31).
 * This class ensures that mapped IDs are as close as possible to the original numeric characteristics of the input,
 * while avoiding conflicts.
 *
 * <p>The mapping algorithm works as follows:
 * <ol>
 *   <li>Tries to preserve the last digit of the numeric portion of the original ID, if it is ≤ 31 and available.</li>
 *   <li>If the preferred number is unavailable, uses the remainder of the original number divided by 32.</li>
 *   <li>If a conflict persists, finds the next available number within the range 0-31.</li>
 * </ol>
 * <p>
 * For example:
 * <pre>
 * Operation_123 -> Extracts 123 -> Last digit is 3 -> Uses 3 if available.
 * Operation_164 -> Extracts 164 -> 164 % 32 = 4 -> Uses 4 if available.
 * If a conflict occurs, finds the next available number in the range.
 * </pre>
 */
public class OperationIDMapper {

    /**
     * Map to store the mapping between original IDs and their mapped integers.
     */
    private final Map<String, Integer> operationMap;

    /**
     * Map to store the reverse mapping between mapped integers and original IDs.
     */
    private final Map<Integer, String> reverseMap;

    /**
     * Constructs a new {@code OperationIDMapper} with empty mappings.
     */
    public OperationIDMapper() {
        this.operationMap = new HashMap<>();
        this.reverseMap = new HashMap<>();
    }

    /**
     * Maps an original operation ID to a unique integer.
     * If the ID has already been mapped, returns the existing mapping.
     *
     * @param originalId the original operation ID to map.
     * @return the mapped unique integer for the given ID.
     */
    public int mapOperationId(String originalId) {
        // Return existing mapping if available
        if (operationMap.containsKey(originalId)) {
            return operationMap.get(originalId);
        }

        // Extract numeric part of the original ID
        int originalNumber = extractNumber(originalId);

        // Determine preferred mapping
        int preferredMapping = getPreferredMapping(originalNumber);

        // Record the mapping
        operationMap.put(originalId, preferredMapping);
        reverseMap.put(preferredMapping, originalId);

        return preferredMapping;
    }

    /**
     * Finds the preferred mapping for a given numeric ID, attempting to preserve
     * certain numeric characteristics or find the next available number within a range.
     *
     * @param originalNumber the numeric portion of the original ID.
     * @return the preferred unique integer mapping.
     */
    private int getPreferredMapping(int originalNumber) {
        int preferredMapping = originalNumber % 10;

        // Check if the preferred mapping is available and within range
        if (reverseMap.containsKey(preferredMapping) || preferredMapping > 31) {
            preferredMapping = originalNumber % 32;

            // Resolve conflicts by finding the next available number in range
            while (reverseMap.containsKey(preferredMapping)) {
                preferredMapping = (preferredMapping + 1) % 32;
            }
        }
        return preferredMapping;
    }

    /**
     * Extracts the numeric part of the operation ID by removing non-numeric characters.
     *
     * @param operationId the operation ID as a string.
     * @return the extracted numeric value as an integer.
     * @throws NumberFormatException if no numeric characters are present in the input.
     */
    private int extractNumber(String operationId) {
        return Integer.parseInt(operationId.replaceAll("[^0-9]", ""));
    }

    /**
     * Prints the current mapping of original operation IDs to their mapped integers.
     * The output is sorted by the original ID for better readability.
     */
    public void printMapping() {
        System.out.println("\nOperation ID Mapping:");
        System.out.println("Original -> Mapped");
        System.out.println("-------------------");

        // Print mappings in alphabetical order of original IDs
        operationMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.printf("%s -> %d%n", entry.getKey(), entry.getValue());
                });
    }
}
