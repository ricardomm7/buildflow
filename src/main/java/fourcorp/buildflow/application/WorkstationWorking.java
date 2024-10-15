package fourcorp.buildflow.application;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class WorkstationWorking {

    private int processCounter = 0;
    private Duration totalWaitingTime = Duration.ZERO;
    private Duration processingTime = Duration.ZERO;
    private Duration averageProcessingTime;
    private Duration averageWaitingTime;
    private HashMap<String, Runing> processinMap = new HashMap<>();

    public void startWorkstation(String idWorkstation) {
        Runing runing = processinMap.get(idWorkstation); // Procura a workstation no mapa
        if (runing != null && runing.getStop() != null) { // Se a worksation foi parada antes
            // Calcula o tempo de espera entre stop e o novo start
            Duration waitingTime = Duration.between(runing.getStop(), LocalDateTime.now());
            totalWaitingTime = totalWaitingTime.plus(waitingTime); // Acumula o tempo de espera
        }
        if (runing == null || !runing.isRuningState()) { // Verifica se a Workstation is runing
            if (runing == null) {
                runing = new Runing(); // Cria uma nova instância de Runing se for a primeira vez
            }
            runing.setStart(); // Define o tempo de início
            runing.setRuningState(true); // Define que está runing
            processinMap.put(idWorkstation, runing); // Atualiza o estado no mapa
            System.out.println("Workstation  " + idWorkstation + " started at " + runing.getStart());
        } else {
            System.out.println("Workstation  " + idWorkstation + " is already running.");
        }
    }

    public void stopWorkstation(String idWorkstation) {
        Runing runing = processinMap.get(idWorkstation); // Recupera o objeto Runing associado à workstation
        if (runing != null && runing.isRuningState()) {
            runing.setStop(); // Define o tempo de parada
            runing.setRuningState(false); // Define que parou
            processinMap.put(idWorkstation, runing); // Atualiza o estado no mapa
            System.out.println("Workstation  " + idWorkstation + " stopped at " + runing.getStop());

            // Atualiza o tempo de processamento
            processCounter++;
            processingTime = processingTime.plus(Duration.between(runing.getStart(), runing.getStop()));
        } else {
            System.out.println("Workstation  " + idWorkstation + " is already stopped or doesn't exist.");
        }
    }

    public Duration getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public Duration getTotalProcessingTime() {
        return processingTime;
    }

    public Duration getAverageWaitingTime() {
        if (processCounter > 0) {
            averageWaitingTime = totalWaitingTime.dividedBy(processCounter);
        }
        return averageWaitingTime;
    }

    public Duration getAverageProcessingTime() {
        if (processCounter > 0) {
            averageProcessingTime = processingTime.dividedBy(processCounter);
        }
        return averageProcessingTime;
    }
}
