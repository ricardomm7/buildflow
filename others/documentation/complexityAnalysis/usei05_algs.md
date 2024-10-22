# USEI05 - Class WorkstationsPerOperation

## Get Workstations Ascending By Percentage

```java
public List<Workstation> getWorkstationsAscendingByPercentage() {
    List<Workstation> workstations = new ArrayList<>(workstationsPerOperation.getAllValues());
    workstations.sort((Workstation w1, Workstation w2) -> { //O(log(n))
        double percentage1 = (w1.getTotalOperationTime() / w1.getTotalExecutionTime()) * 100;
        double percentage2 = (w2.getTotalOperationTime() / w2.getTotalExecutionTime()) * 100;
        return Double.compare(percentage1, percentage2); //O(n)
    });
    return workstations;
}
```

> What this algorithm do: This algorithm returns an ordinated list (by the ratio between operation and execution) containing all the workstations.

> Result of the complexity analysis: **O(nlog(n))** (where n is the total number of workstations)