# USEI03 - Simulator


## Print Total Production Time
```java
 public void printProductionStatistics() {
    System.out.println();
    String lineFormat = "| %-15s | %-10s |%n";
    String separator = "+-----------------+------------+";

    System.out.println("Production Time per Product:");
    System.out.println(separator);
    System.out.printf(lineFormat, "Product ID", "Time (sec)");
    System.out.println(separator);

    Map<String, Double> accumulatedProductTimes = new HashMap<>();
    for (Map.Entry<Product, Double> entry : productTimes.entrySet()) {
        String productId = entry.getKey().getIdItem();
        double time = entry.getValue();
        accumulatedProductTimes.put(productId, accumulatedProductTimes.getOrDefault(productId, 0.0) + time);
    }

    for (Map.Entry<String, Double> entry : accumulatedProductTimes.entrySet()) {
        System.out.printf(lineFormat, entry.getKey(), String.format("%.2f", entry.getValue()));
    }
    System.out.println(separator);
    System.out.printf("%nTotal Production Time for all products: %.2f seconds%n", totalProductionTime); // O(1)
    System.out.println("\nExecution Time by Operation:");
    System.out.println(separator);
    System.out.printf(lineFormat, "Operation", "Time (sec)");
    System.out.println(separator);
    for (Map.Entry<String, Double> entry : operationTimes.entrySet()) {
        String operation = entry.getKey();
        Double time = entry.getValue();
        System.out.printf(lineFormat, operation, String.format("%.2f", time));
    }
    System.out.println(separator);
}
```
> What this algorithm do: This algorithm prints the total production time.

> Result of the complexity analysis: **O(1)**