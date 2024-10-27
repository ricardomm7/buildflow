# USEI06 - Class Simulator

## Present average execution times per operation and the corresponding waiting times

```java
public void printAverageTimesReport() {
    System.out.println();
    System.out.println("Table showing average operating time, waiting time and total waiting time:");
    String lineFormat = "| %-15s | %-30s | %-30s | %-25s | %-6s |%n";
    String separator = "+-----------------+--------------------------------+--------------------------------+---------------------------+--------+";
    if (operationWaitingTimes.isEmpty()) {
        System.out.println("--- Error --- No Waiting Times recorded.");
    } else if (countWaiting.isEmpty()) {
        System.out.println("--- Error --- Waiting Operation counts missing.");
    } else if (operationTimes.isEmpty()) {
        System.out.println("--- Error --- No Operation Times recorded.");
    } else if (operationCounts.isEmpty()) {
        System.out.println("--- Error --- Operation counts missing.");
    } else {
        System.out.println(separator);
        System.out.printf(lineFormat, "Operation", "Average Operation Time (sec)", "Average Waiting Time (sec)", "Total Waiting Time (sec)", "No. Op");
        System.out.println(separator);
        for (String name : operationWaitingTimes.keySet()) { // O(n)
            double avgWaitingTime = operationWaitingTimes.getOrDefault(name, 0.0) / countWaiting.getOrDefault(name, 1); // O(n) * O(1)
            avgWaitingTime = avgWaitingTime * 0.001; // O(n) * O(1)
            BigDecimal roundedWaitingTime = new BigDecimal(avgWaitingTime).setScale(2, RoundingMode.HALF_UP); // O(n) * O(1)

            double totalWaitingTime = operationWaitingTimes.getOrDefault(name, 0.0); // O(n) * O(1)
            totalWaitingTime = totalWaitingTime * 0.001; // O(n) * O(1)
            BigDecimal totalWaiting = new BigDecimal(totalWaitingTime).setScale(2, RoundingMode.HALF_UP); // O(n) * O(1)

            double avgOperationTime = operationTimes.getOrDefault(name, 0.0) / operationCounts.getOrDefault(name, 1); // O(n) * O(1)
            BigDecimal roundedOperationTime = new BigDecimal(avgOperationTime).setScale(2, RoundingMode.UP); // O(n) * O(1)

            System.out.printf(lineFormat, name, roundedOperationTime, roundedWaitingTime, totalWaiting, operationCounts.getOrDefault(name, 1)); // O(n) * O(1)
        }
        System.out.println(separator);
    }
}
```

> What this algorithm do: This code calculates and prints waiting and operation time statistics for different operations, doing the following for each name (operation name) present in operationWaitingTimes.

> Result of the complexity analysis: **O(n)** (where n is the total number of operations)