package fourcorp.buildflow.domain;

public class Workstation implements Identifiable<String> {
    private final String idMachine;
    private double time;
    private boolean isAvailable;
    private int oprCounter;
    private long totalWaiting;
    private long totalOper;

    public Workstation(String idMachine, double time) {
        this.idMachine = idMachine;
        this.time = time;
        this.isAvailable = true;
        this.oprCounter = 0;
        this.totalWaiting = 0;
        this.totalOper = 0;
    }

    public long getTotalExecutionTime() {
        return (totalWaiting + totalOper);
    }

    public void setOprounter() {
        this.oprCounter = oprCounter + 1;
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
        setOprounter();
        System.out.println("Processing product " + product.getId() + " in machine " + idMachine + " - Estimated time: " + time + " min");
        new Thread(() -> {
            simulateExecutionTime();
            totalOper = totalOper + (long) time;
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

    public void increaseWaiting(double time) {
        totalWaiting += (long) time;
    }

    public double getTotalOperationTime() {
        return totalOper;
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
