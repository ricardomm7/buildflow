package fourcorp.buildflow.domain;

import java.util.ArrayList;
import java.util.Date;

public class Operation implements Identifiable<String> {
    private String name;
    private String type;
    private String status;
    private double executionTime;
    private Date startingDate;
    private Date endingDate;
    private ArrayList<Machine> machines;


    public Operation(String name, String type, String status, double executionTime, Date startingDate, Date endingDate, ArrayList<Machine> machines) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.executionTime = executionTime;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.machines = machines;
    }

    public Operation(String name, String type, String status, double executionTime, Date startingDate, Date endingDate) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.executionTime = executionTime;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
    }

    public Operation(String name, String type, String status, double executionTime, Date startingDate) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.executionTime = executionTime;
        this.startingDate = startingDate;
    }

    public Operation(String name, String type, String status, double executionTime) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.executionTime = executionTime;
    }

    public Operation(String operation, String inExecution, int time) {
        this.name = operation;
        this.type = inExecution;
        this.executionTime = time;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public ArrayList<Machine> getMachines() {
        return machines;
    }

    public void setMachines(ArrayList<Machine> machines) {
        this.machines = machines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getId() {
        return name;
    }
}
