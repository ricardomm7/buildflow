package fourcorp.buildflow.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Machine implements Identifiable<String> {
    Map<String, List<Machine>> machinesPerOperation = new HashMap<>();
    private String idMachine;
    private String operation;
    private int time;

    public Machine(String idMachine, String operation, int time) {
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

    public int getTime() {
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
