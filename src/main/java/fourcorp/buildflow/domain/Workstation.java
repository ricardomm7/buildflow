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

    public Workstation(String idMachine, double time) {
        this.idMachine = idMachine;
        this.time = time;
        this.isAvailable = true;
        this.startWaiting = startWaiting;
        this.stopWaiting = stopWaiting;
        this.waitingCounter = waitingCounter;
        this.oprCounter = oprCounter;
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

    public long getStartWaiting(){
        return startWaiting;
    }

    public void setStartWaiting() {
        this.startWaiting = System.currentTimeMillis();
    }
    public long getStopWaiting(){
        return stopWaiting;
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
        System.out.println("Processing product " + product.getId() + " in machine " + idMachine + " - Estimated time: " + time + " min");

        // Simular o processamento em uma nova thread
        new Thread(() -> {
            simulateExecutionTime();  // Simular o tempo de execução
            this.setAvailable(true);  // Máquina fica disponível novamente após o tempo
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
    HashMap<String, Long> timeMap = new HashMap<>();
    HashMap<String, Long> waitingMap = new HashMap<>();

    // conta e guarda o temp de uma operação
    public void setTimePerOperation (String idWorkstation, long time){
        long tmp = timeMap.get(idWorkstation);
        tmp = time + tmp;
        timeMap.put(idWorkstation,tmp);
    }
    //inica o tempo de começo de espera
    public void startWaiting(String idWorkstation, Workstation workstation) {
        workstation.setStartWaiting();
    }
    //da stop a espera e guarda o tempo de espera
    public void stopWaiting (String idWorkstation, Workstation workstation){
        workstation.setStopWaiting();
        workstation.setWaitingCounter();
        long waiting = workstation.getStartWaiting() - workstation.getStopWaiting();
        waitingMap.put(idWorkstation,waiting);

    }
    public long getTotalTimePerOperation(String idWorkstation){
        long time =timeMap.get(idWorkstation);
        return time;
    }
    public long getTotalWaitingTimePerOperation(String idWorkstation){
        long time =waitingMap.get(idWorkstation);
        return time;
    }
    public long getAverageTotalTimePerOperation(String idWorkstation,Workstation workstation){
        long time =timeMap.get(idWorkstation);
        time = time/workstation.getOprCounter();
        return time;
    }
    public long getAverageTotalWaitingTimePerOperation(String idWorkstation,Workstation workstation){
        long time =timeMap.get(idWorkstation);
        time = time/workstation.getWaitingCounter();
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
