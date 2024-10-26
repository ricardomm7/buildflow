package fourcorp.buildflow.domain;

import fourcorp.buildflow.application.Clock;

public class Workstation implements Identifiable<String> {
    private final String idMachine;
    private int time;
    private boolean isAvailable;
    private int oprCounter;
    private double totalOper;
    private Clock clock = new Clock();

    public Workstation(String idMachine, int time) {
        this.idMachine = idMachine;
        this.time = time;
        this.isAvailable = true;
        this.oprCounter = 0;
        this.totalOper = 0;
    }

    public void startClock(boolean hasMoreOperation) {
        this.isAvailable = false;
        clock.countDownClock(this.time, () -> {
            this.isAvailable = true;
            increaseOpCounter();
            if (hasMoreOperation) {
                clock.countUpClock(true); // Começa a contagem ascendente se ainda houver operações
            }
        });
    }

    public String getAverageExecutionTimePerOperation() {
        double tempo = totalOper / oprCounter;
        return " ---Workstation " + idMachine + "---Average Execution Time = " + tempo + "\n";
    }

    public void processProduct(Product product) {
        increaseOpCounter();
        System.out.println("Processing product " + product.getId() + " in machine " + idMachine + " - Estimated time: " + time + " sec");
        increaseOperationTime();
        startClock(product.hasMoreOperations());
    }

    public void increaseOperationTime() {
        totalOper = totalOper + time;
    }

    public void increaseOpCounter() {
        this.oprCounter = oprCounter + 1;
    }

    public int getOprCounter() {
        return oprCounter;
    }

    public double getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public synchronized void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getTotalOperationTime() {
        return totalOper;
    }

    public void setTotalOperationTime(double a) {
        this.totalOper = a;
    }

    @Override
    public String getId() {
        return idMachine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workstation that = (Workstation) o;
        return idMachine.equalsIgnoreCase(that.idMachine);
    }

    @Override
    public int hashCode() {
        return idMachine.hashCode();
    }
}
