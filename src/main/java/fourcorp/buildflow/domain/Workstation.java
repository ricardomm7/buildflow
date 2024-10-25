package fourcorp.buildflow.domain;

import fourcorp.buildflow.repository.Clock;

public class Workstation implements Identifiable<String> {
    private final String idMachine;
    private int time;
    private boolean isAvailable;
    private int oprCounter;
    private double totalWaiting;
    private double totalOper;
    int contWaiting;
    private Clock clock = new Clock();

    public Workstation(String idMachine, int time) {
        this.idMachine = idMachine;
        this.time = time;
        this.isAvailable = true;
        this.oprCounter = 0;
        this.totalWaiting = 0;
        this.totalOper = 0;
        this.contWaiting = 0;
    }

    public int stopClock() {
        // Para o relógio e retorna o tempo total de espera
        if (clock != null) {
            double elapsedTime = clock.countUpClock(false); // Para a contagem ascendente
            totalWaiting += elapsedTime;
            incrementContWaiting();
            return (int) totalWaiting; // Retorna o tempo total de espera
        }
        return 0;
    }

    public void startClock(boolean hasMoreOperation) {
        if (oprCounter > 0) {
            double temp = clock.countUpClock(false);  // Para a contagem anterior e obtém o tempo decorrido
            totalWaiting = totalWaiting + temp;
            incrementContWaiting();
            this.isAvailable = false;
        }
        clock.countDownClock(this.time, () -> {
            this.isAvailable = true;
            setOprounter();
            if (hasMoreOperation) {
                clock.countUpClock(true); // Começa a contagem ascendente se ainda houver operações
            }
        });
    }

    public String getAverageExecutionTimePerOperation() {
        double tempo = totalOper / oprCounter;
        return " ---Workstation " + idMachine + "---Average Execution Time = " + tempo + "\n";
    }

    public String getAverageWaitingTime() {
        double tempo = totalWaiting / contWaiting;
        return " ---Workstation " + idMachine + "---Average Waiting Time = " + tempo + "\n";
    }

    public void increaseOperationTime() {
        totalOper = totalOper + time;
    }

    public double getTotalExecutionTime() {
        return ((totalWaiting * 100) + totalOper);
    }

    public void incrementContWaiting() {
        this.contWaiting = contWaiting + 1;
    }

    public void setOprounter() {
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

    public void processProduct(Product product) {
        setOprounter();
        System.out.println("Processing product " + product.getId() + " in machine " + idMachine + " - Estimated time: " + time + " sec");
        increaseOperationTime();
        startClock(product.hasMoreOperations());
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
