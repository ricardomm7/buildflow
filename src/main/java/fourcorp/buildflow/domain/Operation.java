package fourcorp.buildflow.domain;

import java.util.Objects;

public class Operation implements Identifiable<String> {
    private String name;
    private boolean execute;
    private int countExecution;
    private int countWaiting;


    public Operation(String operation) {
        this.execute = false;
        this.name = operation;
    }
    public boolean getExecute (){
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
