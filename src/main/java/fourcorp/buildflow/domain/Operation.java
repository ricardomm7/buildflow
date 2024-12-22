package fourcorp.buildflow.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an operation in the system with a name, execution status, execution time, and a count of waiting tasks.
 * This class implements the Identifiable interface, using the operation name as its identifier.
 */
public class Operation implements Identifiable<String> {
    private String id;
    private String name;
    private boolean execute;
    private Double time;
    private List<Operation> children; // Lista de operações filhas

    public Operation(String id, String name) {
        this.id = id;
        this.name = name;
        this.execute = false;
        this.time = 0.0;
        this.children = new ArrayList<>(); // Inicializa a lista de filhos
    }


    /**
     * Constructs an Operation with the specified name.
     * The operation is initialized as not executed, with zero waiting tasks,
     * and an initial execution time of zero.
     *
     * @param operation the name of the operation
     */
    public Operation(String operation) {
        this.execute = false;
        this.name = operation;
        this.time = 0.0;
    }

    public Operation(String operationId, String description, double executionTime) {
        this.id = operationId;
        this.name = description;
        this.time = executionTime;
    }

    /**
     * Gets the execution time of the operation.
     *
     * @return the execution time
     */
    public double getTime() {
        return time;
    }

    /**
     * Sets the execution time to the current system time in milliseconds.
     */
    public void setTime() {
        this.time = (double) System.currentTimeMillis();
    }

    /**
     * Gets the execution status of the operation.
     *
     * @return true if the operation is marked as executed, false otherwise
     */
    public boolean getExecute() {
        return execute;
    }

    /**
     * Sets the execution status of the operation.
     *
     * @param execute true to mark the operation as executed, false otherwise
     */
    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    /**
     * Gets the unique identifier of the operation, which is its name.
     *
     * @return the name of the operation
     */
    @Override
    public String getId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Método para adicionar filhos
    public void addChild(Operation child) {
        this.children.add(child);
    }

    // Método para obter filhos
    public List<Operation> getChildren() {
        return children;
    }

    // Método para verificar se a operação tem filhos
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * Compares this operation to the specified object. The result is true if and only if
     * the argument is not null, is an Operation object, and the names are equal
     * (case-insensitive).
     *
     * @param o the object to compare this operation against
     * @return true if the given object represents an Operation equivalent to this operation, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return name.equalsIgnoreCase(operation.name);
    }

    /**
     * Returns a hash code value for the operation, based on its name.
     *
     * @return a hash code value for this operation
     */
    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
}
