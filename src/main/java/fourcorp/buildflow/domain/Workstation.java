package fourcorp.buildflow.domain;

import java.util.HashMap;

public class Workstation implements Identifiable<String> {
    private String idMachine;
    private double time;
    private long waitingTime;
    private boolean isAvailable;
    private long startWaiting;
    private long stopWaiting;
    private int waitingCounter;
    private int oprCounter;
    private long totalWaiting;
    private long totalOper;


    public Workstation(String idMachine, double time) {
        this.idMachine = idMachine;
        this.time = time;
        this.isAvailable = true;
        this.startWaiting = 0;
        this.stopWaiting = 0;
        this.waitingCounter = 0;
        this.oprCounter = 0;
        this.waitingTime = 0;
        this.totalWaiting = 0;
        this.totalOper = 0;
    }
    public void setOprounter() {
        this.oprCounter = oprCounter + 1;
    }

    public int getOprCounter() {
        return oprCounter;
    }

    public void setWaitingCounter() {this.waitingCounter = waitingCounter + 1;}

    public int getWaitingCounter() {
        return waitingCounter;
    }

    public long getStartWaiting(){
        return startWaiting;
    }

    public void setStartWaiting() {
        this.startWaiting = System.currentTimeMillis();
    }

    public long getStopWaiting(){
        return stopWaiting ;
    }

    public void setStopWaiting() {
        this.stopWaiting = System.currentTimeMillis();
    }

    public String getIdMachine() {
        return idMachine;
    }

    public void setIdMachine(String idMachine) {
        this.idMachine = idMachine;
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

    public synchronized void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void processProduct(Product product) {

        this.setAvailable(false);
        setOprounter();
        if(startWaiting != 0 ){
            setStopWaiting();
            stopWaiting();
        }
        System.out.println("Processing product " + product.getId() + " in machine " + idMachine + " - Estimated time: " + time + " min");

        // Simular o processamento em uma nova thread
        new Thread(() -> {
            simulateExecutionTime();  // Simular o tempo de execução
            this.setAvailable(true);
            setStartWaiting();
            totalOper = totalOper + (long)time;
            // Máquina fica disponível novamente após o tempo
            //System.out.println("Machine " + idMachine + " is now available again.");
        }).start();
    }

    private void simulateExecutionTime() {
        try {
            // Calcula o tempo de sleep: 1 segundo = 1 minuto na simulação
            // O fator 0.00015 é usado para reduzir o tempo para fins de demonstração
            long sleepTime = (long) (time * 0.00015);

            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            //System.out.println("Execução interrompida para a máquina " + idMachine);
        }
    }

    //da stop a espera e guarda o tempo de espera
    public void stopWaiting (){
        setWaitingCounter();
        long waiting =   stopWaiting- startWaiting;
        waitingTime = waiting;
        totalWaiting = totalWaiting + waiting;

    }
    public double getTotalTimePerOperation(){
        return totalOper;
    }
    public long getTotalWaitingTimePerOperation(){
        return totalWaiting;
    }
    public double getAverageTotalTimePerOperation(){
        long time = totalOper / oprCounter;
        return time;
    }
    public long getAverageTotalWaitingTimePerOperation(){
        long time = totalWaiting / waitingCounter;
        return time;
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
