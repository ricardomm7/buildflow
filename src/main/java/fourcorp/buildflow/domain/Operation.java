package fourcorp.buildflow.domain;

import java.util.Objects;

public class Operation implements Identifiable<String> {
    private String name;
    private boolean execute;
    private int countExecution;
    private int countWaiting;
    private boolean isDoing;
    private int timeExecution;
    private int timeWaiting;

    public Operation(String operation) {
        this.execute = false;
        this.name = operation;
        this.isDoing = false;
        this.countExecution = 0;
        this.countWaiting = 0;
        this.timeExecution = 0;
        this.timeWaiting = 0;

    }

    public String getAverageExecutionTimePerOperation() {
        long tempo = timeExecution / countExecution;
        return " ---Process " + name + "---Average Execution Time = " + tempo + "\n";
    }

    public String getAverageWaitingTimePerOperation() {
        long tempo = timeWaiting / countWaiting;
        return " ---Process " + name + "---Average Waiting Time = " + tempo + "\n";
    }

    public void setTimeExecution(int time) {
        timeExecution = timeExecution + time;
    }

    public long getExecutionWaiting() {
        return (timeWaiting + timeExecution);
    }

    public void setcountWaiting() {
        this.countWaiting = countWaiting + 1;
    }

    public void setCountExecution() {
        this.countExecution = countExecution + 1;
    }

    public int getcountExecution() {
        return countExecution;
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
