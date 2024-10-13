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

    public List<Workstation> getWorkstationsByOperation(Operation operation) {
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


    public void removeWorkstation(Workstation b, Operation o) {
        workstationsPerOperation.remove(b, o);
    }
}
