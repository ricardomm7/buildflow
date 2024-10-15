package fourcorp.buildflow.domain;

public class Workstation implements Identifiable<String> {
    private String idMachine;
    private double time;  // Tempo de execução da operação
    private boolean isAvailable;

    public Workstation(String idMachine, double time) {
        this.idMachine = idMachine;
        this.time = time;
        this.isAvailable = true;
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
            System.out.println("Machine " + idMachine + " is now available again.");
        }).start();
    }

    /**
     * Simula o tempo de execução da operação usando sleep.
     * Este método é usado para imitar o tempo de processamento real de um produto na estação de trabalho.
     * A simulação usa um tempo escalado onde 1 segundo de sleep representa 1 minuto de tempo de processamento real.
     */
    private void simulateExecutionTime() {
        try {
            // Calcula o tempo de sleep: 1 segundo = 1 minuto na simulação
            // O fator 0.00015 é usado para reduzir o tempo para fins de demonstração
            long sleepTime = (long) (time * 0.00015);

            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Execução interrompida para a máquina " + idMachine);
        }
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
