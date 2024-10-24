package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Workstation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WorkstationsPerOperation {
    private static MapLinked<Workstation, Operation, String> workstationsPerOperation;

    public WorkstationsPerOperation() {
        workstationsPerOperation = new MapLinked<>();
    }

    public void create(Workstation workstation, Operation operation) {
        workstationsPerOperation.newItem(workstation, operation);
    }

    public List<Workstation> getWorkstationsByOperation(Operation operation, boolean b) {
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
        // Se 'b' for true, ordena as máquinas por tempo de operação
        if (b) {
            availableWorkstations.sort(Comparator.comparingDouble(Workstation::getTime));
        }
        return availableWorkstations;
    }

    public void increaseWaitingTimes(double time) {
        for (Workstation machine : workstationsPerOperation.getAllValues()) {
            if (machine.isAvailable()) {
                machine.increaseWaiting(time);
            }
        }
    }

    public List<Workstation> getWorkstationsAscendingByPercentage() {
        List<Workstation> workstations = new ArrayList<>(workstationsPerOperation.getAllValues());
        workstations.sort((Workstation w1, Workstation w2) -> {
            double percentage1 = (w1.getTotalOperationTime() / w1.getTotalExecutionTime()) * 100;
            double percentage2 = (w2.getTotalOperationTime() / w2.getTotalExecutionTime()) * 100;
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
