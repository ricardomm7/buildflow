package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Workstation;

import java.util.ArrayList;
import java.util.List;

public class WorkstationsPerOperation {
    private static MapLinked<Workstation, Operation, String> workstationsPerOperation;

    public WorkstationsPerOperation() {
        workstationsPerOperation = new MapLinked<>();
    }

    public void create(Workstation workstation, Operation operation) {
        workstationsPerOperation.newItem(workstation, operation);
    }

    public MapLinked<Workstation, Operation, String> getWorkstationsPerOperation() {
        return workstationsPerOperation;
    }

    private List<Workstation> getWorkstationsByOperation(Operation operation) {
        List<Workstation> availableWorkstations = new ArrayList<>();
        for (Operation keyOperation : workstationsPerOperation.getKeys()) {
            if (keyOperation.getId().equals(operation.getId())) {
                List<Workstation> workstations = workstationsPerOperation.getByKey(keyOperation);
                for (Workstation workstation : workstations) {
                    if (workstation.isAvailable()) {
                        availableWorkstations.add(workstation);
                    }
                }
                break;
            }
        }
        return availableWorkstations;
    }

    public Workstation findBestMachineForOperation(Operation operation) {
        List<Workstation> workstations = getWorkstationsByOperation(operation);
        Workstation bestMachine = null;
        for (Workstation machine : workstations) {
            if (machine.isAvailable()) {
                if (bestMachine == null || machine.getTime() < bestMachine.getTime()) {
                    bestMachine = machine;
                }
            }
        }
        return bestMachine;
    }

    public List<Workstation> getWorkstationsAscendingByPercentage() {
        List<Workstation> workstations = new ArrayList<>(workstationsPerOperation.getAllValues());
        workstations.sort((Workstation w1, Workstation w2) -> {
            double percentage1 = (w1.getTotalTimePerOperation() / w1.getTotalExecution()) * 100;
            double percentage2 = (w2.getTotalTimePerOperation() / w2.getTotalExecution()) * 100;
            return Double.compare(percentage1, percentage2);
        });
        return workstations;
    }


    public void removeWorkstation(Workstation b, Operation o) {
        workstationsPerOperation.remove(b, o);
    }

    public void removeAll() {
        workstationsPerOperation.removeAll();
    }
}
