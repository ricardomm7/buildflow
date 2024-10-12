package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Workstation;

import java.util.List;

public class WorkstationsPerOperation {
    private static MapLinked<Workstation, Operation, String> workstationsPerOperation;

    public void create(Workstation workstation, Operation operation) {
        workstationsPerOperation.newItem(workstation, operation);
    }

    public MapLinked<Workstation, Operation, String> getWorkstationsPerOperation() {
        return workstationsPerOperation;
    }

    public List<Workstation> getProductsByPriority(Operation a) {
        return workstationsPerOperation.getByKey(a);
    }

    public void removeWorkstation(Workstation b, Operation o) {
        workstationsPerOperation.remove(b, o);
    }
}
