package fourcorp.buildflow.domain;

public class Workstation implements Identifiable<String> {
    private String idMachine;
    //private Operation operation;
    private double time;
    private boolean isAvailable;

    public Workstation(String idMachine, double time) {
        this.idMachine = idMachine;
        //this.operation = new Operation(operation);
        this.time = time;
    }

    public String getIdMachine() {
        return idMachine;
    }

    public void setIdMachine(String idMachine) {
        this.idMachine = idMachine;
    }

    /*public Operation getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = new Operation(operation);
    }
     */

    public double getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public String getId() {
        return idMachine;
    }
}
