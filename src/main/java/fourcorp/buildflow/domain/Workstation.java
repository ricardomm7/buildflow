package fourcorp.buildflow.domain;

import java.util.HashMap;

public class Workstation implements Identifiable<String> {
    private String idMachine;
    private double time;  // Tempo de execução da operação
    private boolean isAvailable;
    private long startWaiting;
    private long stopWaiting;
    private int waitingCounter;
    private int oprCounter;

    private double operationTimeTotal;
    private double executiontimeTotal; // Este tem de ser igual ao operationTimeTotal + waitingTimeTotal : FALTA IMPLEMENTAR POIS ESTA CLASSE TÁ COM REGRAS FALHADAS

    HashMap<String, Long> timeMap = new HashMap<>();
    HashMap<String, Long> waitingMap = new HashMap<>();

    public Workstation(String idMachine, double time) {
        this.idMachine = idMachine;
        this.time = time;
        this.isAvailable = true;
    }

    public void processProduct(Product product) {
        this.setAvailable(false);
        System.out.println("Processing product " + product.getId() + " in machine " + idMachine + " - Estimated time: " + time + " min");
        operationTimeTotal += time;
        new Thread(() -> {
            simulateExecutionTime();
            this.setAvailable(true);
        }).start();
    }

    private void simulateExecutionTime() {
        try {
            long sleepTime = (long) (time * 0.00015);
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void setTimePerOperation(String idWorkstation, long time) {
        long tmp = timeMap.get(idWorkstation);
        tmp = time + tmp;
        timeMap.put(idWorkstation, tmp);
    }

    public void startWaiting() {
        setStartWaiting();
    }

    public void stopWaiting(String idWorkstation, Workstation workstation) {
        workstation.setStopWaiting();
        workstation.setWaitingCounter();
        long waiting = workstation.getStartWaiting() - workstation.getStopWaiting();
        waitingMap.put(idWorkstation, waiting);
    }

    public long getTotalTimePerOperation(String idWorkstation) {
        long time = timeMap.get(idWorkstation);
        return time;
    }

    public long getTotalWaitingTimePerOperation(String idWorkstation) {
        long time = waitingMap.get(idWorkstation);
        return time;
    }

    public long getAverageTotalTimePerOperation(String idWorkstation) {
        long time = timeMap.get(idWorkstation);
        time = time / getOprCounter();
        return time;
    }

    public long getAverageTotalWaitingTimePerOperation(String idWorkstation) {
        long time = timeMap.get(idWorkstation);
        time = time / getWaitingCounter();
        return time;
    }


    public double getExecutiontimeTotal() {
        return executiontimeTotal;
    }

    public double getOperationTimeTotal() {
        return operationTimeTotal;
    }

    public void setOprounter() {
        this.oprCounter = oprCounter + 1;
    }

    public int getOprCounter() {
        return oprCounter;
    }

    public void setWaitingCounter() {
        this.waitingCounter = waitingCounter + 1;
    }

    public int getWaitingCounter() {
        return waitingCounter;
    }

    public long getStartWaiting() {
        return startWaiting;
    }

    public void setStartWaiting() {
        this.startWaiting = System.currentTimeMillis();
    }

    public long getStopWaiting() {
        return stopWaiting;
    }

    public void setStopWaiting() {
        this.stopWaiting = System.currentTimeMillis();
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
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
