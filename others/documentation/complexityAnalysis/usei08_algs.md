# USEI08 - Simulator

## Priority implementation
```java
public void runWithPriority(boolean b) {
        resetSimulation(); // O(n)
        if (!productLine.getProductsByPriority(PriorityOrder.HIGH).isEmpty()) {
            processedProducts.clear(); // O(1)
            returnToFirstOp(productLine.getAllProducts()); // O(n)
            System.out.println("\n\n>>> NOW IT'S PROCESSING THE HIGH PRIORITY PRODUCTS\n\n");
            runSimulation(productLine.getProductsByPriority(PriorityOrder.HIGH), b);
        }

        if (!productLine.getProductsByPriority(PriorityOrder.NORMAL).isEmpty()) {
            processedProducts.clear(); // O(1)
            returnToFirstOp(productLine.getAllProducts()); // O(n)
            System.out.println("\n\n>>> NOW IT'S PROCESSING THE NORMAL PRIORITY PRODUCTS\n\n");
            runSimulation(productLine.getProductsByPriority(PriorityOrder.NORMAL), b);
        }

        if (!productLine.getProductsByPriority(PriorityOrder.LOW).isEmpty()) {
            processedProducts.clear(); // O(1)
            returnToFirstOp(productLine.getAllProducts()); // O(n)
            System.out.println("\n\n>>> NOW IT'S PROCESSING THE LOW PRIORITY PRODUCTS\n\n");
            runSimulation(productLine.getProductsByPriority(PriorityOrder.LOW), b);
        }
    }
```


> What this algorithm does: This algorithm runs the simulation for priority given products.

> Result of the complexity analysis: **O(n)**, where "n" is the number of products.

## Reset simulation
```java
public void resetSimulation() {
        for (Workstation a : workstationsPerOperation.getAllWorkstations()) { // O(n)
            a.setTotalOperationTime(0);
        }
        productTimes.clear(); // O(1)
        totalProductionTime = 0.0; // O(1)
        operationTimes.clear(); // O(1)
        waitingQueue.clear(); // O(1)
        waitingTimes.clear(); // O(1)
        operationWaitingTimes.clear(); // O(1)
        countWaiting.clear(); // O(1)
        processedProducts.clear(); // O(1)
        machineFlowAnalyzer.reset(); // O(1)
        operationCounts.clear(); // O(1)
}
```

> What this algorithm does: This method resets all the data for a new simulation.

> Result of the complexity analysis: **O(n)**, where "n" is the number of workstations.

## Reset simulation
```java
private void returnToFirstOp(List<Product> f) {
        for (Product a : f) { // O(n)
            a.setCurrentOperationIndex(0); // O(n) * O(1)
        }
}
```

> What this algorithm does: This method resets all the operations from a product.

> Result of the complexity analysis: **O(n)**, where "n" is the number of products.
