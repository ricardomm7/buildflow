package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Workstation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The WorkstationsPerOperation class manages the relationship between workstations and operations.
 * It provides functionality to create associations, retrieve available workstations for specific operations,
 * and manipulate the workstation data as needed.
 */
public class WorkstationsPerOperation {
    private static MapLinked<Workstation, Operation, String> workstationsPerOperation;

    /**
     * Constructs a WorkstationsPerOperation instance.
     * Initializes the internal data structure to hold the associations between workstations and operations.
     */
    public WorkstationsPerOperation() {
        workstationsPerOperation = new MapLinked<>();
    }

    /**
     * Creates a new association between a workstation and an operation.
     *
     * @param workstation the workstation to be added
     * @param operation   the operation associated with the workstation
     */
    public void create(Workstation workstation, Operation operation) {
        workstationsPerOperation.newItem(workstation, operation);
    }


    /**
     * Retrieves a list of available workstations for a specified operation.
     * Optionally sorts the list based on the specified flag.
     *
     * @param operation the operation for which to retrieve available workstations
     * @param b         if true, the returned list is sorted by processing time in ascending order
     * @return a list of available workstations associated with the specified operation
     */
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
        if (b) {
            availableWorkstations.sort(Comparator.comparingDouble(Workstation::getTime));
        }
        return availableWorkstations;
    }

    /**
     * Retrieves a list of workstations sorted in ascending order based on their total operation time
     * expressed as a percentage of the given threshold time.
     *
     * @param t the reference time used to calculate the percentage
     * @return a sorted list of workstations based on their total operation time percentage
     */
    public List<Workstation> getWorkstationsAscendingByPercentage(double t) {
        List<Workstation> workstations = new ArrayList<>(workstationsPerOperation.getAllValues());
        workstations.sort((Workstation w1, Workstation w2) -> {
            double percentage1 = (w1.getTotalOperationTime() / t) * 100;
            double percentage2 = (w2.getTotalOperationTime() / t) * 100;
            return Double.compare(percentage1, percentage2);
        });
        return workstations;
    }

    /**
     * Removes the association between a specific workstation and an operation.
     *
     * @param b the workstation to remove
     * @param o the operation associated with the workstation
     */
    public void removeWorkstation(Workstation b, Operation o) {
        workstationsPerOperation.remove(b, o);
    }

    /**
     * Removes all workstation-operation associations from the repository.
     */
    public void removeAll() {
        workstationsPerOperation.removeAll();
    }

    /**
     * Retrieves a list of all workstations in the repository.
     *
     * @return a list of all workstations
     */
    public List<Workstation> getAllWorkstations() {
        return new ArrayList<>(workstationsPerOperation.getAllValues());
    }
}
