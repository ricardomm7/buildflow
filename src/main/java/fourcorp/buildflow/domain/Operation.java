package fourcorp.buildflow.domain;

public class Operation implements Identifiable<String> {
    private String name;

    public Operation(String operation) {
        this.name = operation;
    }

    @Override
    public String getId() {
        return name;
    }
}
