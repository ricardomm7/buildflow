package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Workstation;

public class MachinesPerOperation {
    public static MapLinked<Workstation, Operation, String> machinesPerOperation = new MapLinked<>();

    public void create(Workstation workstation, Operation operation) {
        machinesPerOperation.newItem(workstation, operation);
    }

    public MapLinked<Workstation, Operation, String> getMachinesPerOperation() {
        return machinesPerOperation;
    }
}
