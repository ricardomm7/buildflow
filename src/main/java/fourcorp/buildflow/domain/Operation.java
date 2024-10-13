package fourcorp.buildflow.domain;

import java.util.Objects;

public class Operation implements Identifiable<String> {
    private String name;

    public Operation(String operation) {
        this.name = operation;
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
