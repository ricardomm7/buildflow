package fourcorp.buildflow.domain;

/**
 * Represents the priority levels for an order, categorized as HIGH, NORMAL, or LOW.
 * Each priority level has an associated integer value and a string representation.
 */
public enum PriorityOrder {
    HIGH(1, "High"),
    NORMAL(2, "Normal"),
    LOW(3, "Low");

    private final int priorityValue;
    private final String priorityString;

    /**
     * Constructs a PriorityOrder with the specified integer value and string representation.
     *
     * @param priorityValue the integer value associated with the priority
     * @param priorityString the string representation of the priority
     */
    PriorityOrder(int priorityValue, String priorityString) {
        this.priorityValue = priorityValue;
        this.priorityString = priorityString;
    }

    /**
     * Gets the integer value associated with the priority.
     *
     * @return the priority value
     */
    public int getPriorityValue() {
        return priorityValue;
    }

    /**
     * Returns the string representation of the priority.
     *
     * @return the priority as a string
     */
    @Override
    public String toString() {
        return priorityString;
    }
}
