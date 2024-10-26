package fourcorp.buildflow.domain;

import java.util.Objects;

public class Operation implements Identifiable<String> {
    private String name;
    private boolean execute;
    private Double time;
    private int countWaiting;

    public Operation(String operation) {
        this.execute = false;
        this.name = operation;
        this.countWaiting = 0;
        this.time = 0.0;

    }

    public double getTime() {
        return time;
    }

    public void setTime() { this.time = (double) System.currentTimeMillis();
    }


    public boolean getExecute() {
        return execute;
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return name.equalsIgnoreCase(operation.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
