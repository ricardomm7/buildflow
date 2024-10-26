package fourcorp.buildflow.domain;

import fourcorp.buildflow.repository.Clock;

import java.util.Objects;

public class Operation implements Identifiable<String> {
    private String name;
    private boolean execute;
    private int countExecution;
    private int countWaiting;
    private boolean isDoing;
    private double timeExecution;
    private double timeWaiting;
    private Clock clock = new Clock();
    private Workstation workstation;


    public Operation(String operation) {
        this.execute = false;
        this.name = operation;
        this.isDoing = false;
        this.countExecution = 0;
        this.countWaiting = 0;
        this.timeExecution = 0;
        this.timeWaiting = 0;
        this.workstation = null;


    }


    public int operationStopClock() {
        // Para o relógio e retorna o tempo total de espera
        if (clock != null) {
            double elapsedTime = clock.countUpClock(false); // Para a contagem ascendente
            timeWaiting += elapsedTime;
            setcountWaiting();
            System.out.println("Cout Waiting=" + countWaiting);
            System.out.println("timeWaiting =" + timeWaiting);// Acumula o tempo de espera
            return (int) timeWaiting; // Retorna o tempo total de espera
        }
        return 0;
    }

    public void operationStartClock(int time, boolean stopCountDown) {
        if (countExecution == 0) {
            this.isDoing = true;
            System.out.println("This process is doing = ;" + isDoing);

            clock.countDownClock(time, () -> {
                this.isDoing = false;
                System.out.println("This process is doing = ;" + isDoing);

                if (!isDoing) {
                    setCountExecution();
                    System.out.println("Execution Counter =" + countExecution);
                    setTimeExecution(time);
                    System.out.println("Execution Time=" + timeExecution);
                    //ativação da cronometragem do waiting

                    if (stopCountDown) {
                        clock.countUpClock(true); // Começa a contagem ascendente se ainda houver operações
                    } else {
                        clock.countUpClock(false); // Para a contagem se não houver mais operações
                    }
                }
            });

        } else {
            double temp = clock.countUpClock(false);  // Para a contagem anterior e obtém o tempo decorrido
            timeWaiting = timeWaiting + temp;
            System.out.println("totalWaiting = " + timeWaiting);
            setcountWaiting();
            System.out.println("countWaiting =" + countWaiting);
            this.isDoing = true;

            clock.countDownClock(time, () -> {

                this.isDoing = false;
                System.out.println("This process is doing = ;" + isDoing);

                if (!isDoing) {
                    setCountExecution();
                    System.out.println("Execution Counter =" + countExecution);
                    setTimeExecution(time);
                    System.out.println("Execution Time=" + timeExecution);
                    //ativação da cronometragem do waiting

                    if (stopCountDown) {
                        clock.countUpClock(true); // Começa a contagem ascendente se ainda houver operações
                    } else {
                        clock.countUpClock(false); // Para a contagem se não houver mais operações
                    }
                }

            });
        }
    }

    public String getAverageExecutionTimePerOperation() {
        double tempo = timeExecution / countExecution;
        return " ---Process " + name + "---Average Execution Time = " + tempo + "\n";
    }

    public String getAverageWaitingTimePerOperation() {
        double tempo = timeWaiting / countWaiting;
        return " ---Process " + name + "---Average Waiting Time = " + tempo + "\n";
    }

    public void setTimeExecution(int time) {
        timeExecution = timeExecution + time;
    }

    public double getExecutionWaiting() {
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

    public Workstation getWorkstation() {
        return workstation;
    }
    public  void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
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
