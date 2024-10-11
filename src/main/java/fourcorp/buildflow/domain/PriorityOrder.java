package fourcorp.buildflow.domain;

public enum PriorityOrder {
    HIGH(1, "High"),
    MEDIUM(2, "Medium"),
    LOW(3, "Low");

    private final int priorityValue;
    private final String priorityString;

    PriorityOrder(int priorityValue, String priorityString) {
        this.priorityValue = priorityValue;
        this.priorityString = priorityString;
    }

    public int getPriorityValue() {
        return priorityValue;
    }

    @Override
    public String toString() {
        return priorityString;
    }
}
