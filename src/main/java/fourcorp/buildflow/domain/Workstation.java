package fourcorp.buildflow.domain;

public class Workstation implements Identifiable<String> {
    private String idMachine;
    private String operation;
    private double time;

    public Workstation(String idMachine, String operation, double time) {
        this.idMachine = idMachine;
        this.operation = operation;
        this.time = time;
    }

    public String getIdMachine() {
        return idMachine;
    }

    public void setIdMachine(String idMachine) {
        this.idMachine = idMachine;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public double getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String getId() {
        return idMachine;
    }
}
