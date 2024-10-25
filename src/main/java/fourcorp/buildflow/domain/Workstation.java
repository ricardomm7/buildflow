package fourcorp.buildflow.domain;

//import fourcorp.buildflow.application.Experiencia;
import fourcorp.buildflow.repository.Clock;
import java.util.List;

public class Workstation implements Identifiable<String> {
    private final String idMachine;
    private int time;
    private boolean isAvailable;
    private int oprCounter;
    private long totalWaiting;
    private long totalOper;
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
            int elapsedTime = clock.countUpClock(false); // Para a contagem ascendente
            totalWaiting += elapsedTime;
            setContWaiting();
            System.out.println("Cout Waiting=" + contWaiting);
            System.out.println("TotalWaiting ="+ totalWaiting);// Acumula o tempo de espera
            return (int) totalWaiting; // Retorna o tempo total de espera
        }
        return 0;
    }

    public void startClock(int i, boolean hasMoreOperation) {
        if (oprCounter == 0) {
            this.isAvailable = false;
            System.out.println("this.isAvailable = ;" + isAvailable);

            clock.countDownClock(this.time, () -> {
                this.isAvailable = true;
                System.out.println("this.isAvailable = ;" + isAvailable);

                if (isAvailable) {
                    setOprounter();
                    System.out.println("oprCounter"+oprCounter);
                    setTotalOper();
                    System.out.println("totalOper"+ totalOper);

                    if (hasMoreOperation) {
                        clock.countUpClock(true); // Começa a contagem ascendente se ainda houver operações
                    } else {
                        clock.countUpClock(false); // Para a contagem se não houver mais operações
                    }
                }
            });

        } else {
            int temp = clock.countUpClock(false);  // Para a contagem anterior e obtém o tempo decorrido
            totalWaiting = totalWaiting + temp;
            System.out.println("totalWaiting = "+totalWaiting);
            setContWaiting();
            System.out.println("countWaiting ="+contWaiting);
            this.isAvailable = false;

            clock.countDownClock(this.time, () -> {
                this.isAvailable = true;

                if (isAvailable) {
                    setOprounter();
                    System.out.println("oprCounter"+oprCounter);
                    setTotalOper();
                    System.out.println("totalOper"+ totalOper);

                    if (hasMoreOperation) {
                        clock.countUpClock(true);  // Inicia a contagem se houver mais operações
                    } else {
                        clock.countUpClock(false);  // Para a contagem se não houver mais operações
                    }
                }
            });
        }
    }


    public void setTotalOper(){ totalOper = totalOper + time; }
    public long getTotalExecutionTime() {
        return (totalWaiting + totalOper);
    }

    public void setContWaiting() {
        this.contWaiting = contWaiting + 1;
    }

    public void setOprounter() {
        this.oprCounter = oprCounter + 1;
    }

    public int  getOprCounter(){return oprCounter;}

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
    } //entendi nada

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
