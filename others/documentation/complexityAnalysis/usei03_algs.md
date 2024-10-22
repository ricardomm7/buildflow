# USEI03 - Simulator

## Process Items (totalProductionTime)
```java
private void processItems() {
    for (Product product : products) {  // O(p)
        LinkedList<Operation> operationQueue = operationQueues.getByKey(product);
        List<Operation> pendingOperations = new ArrayList<>();
        Workstation previousWorkstation = null;

        double productTotalTime = 0.0;

        while (!operationQueue.isEmpty()) { // O(m) * O(n)) = O(n * m)
            Operation currentOperation = operationQueue.poll();
            System.out.println("Current operation: " + currentOperation.getId());

            Workstation bestMachine = w.findBestMachineForOperation(currentOperation); // O(w) * O(n * m) = O(n * m * w)
            if (bestMachine != null && bestMachine.isAvailable()) {
                System.out.println("The best machine: " + bestMachine.getId());

                double operationTime = bestMachine.getTime();
                productTotalTime += operationTime; // O(1) * O(n * m * w) = O(n * m * w)
                totalProductionTime += operationTime;

                bestMachine.setAvailable(false);
                w.increaseWaitingTimes(bestMachine.getTime());
                bestMachine.processProduct(product);
                bestMachine.setAvailable(true);

                List<String> machineFlow = productMachineFlows.computeIfAbsent(product.getId(), _ -> new ArrayList<>());
                machineFlow.add(bestMachine.getId());

                if (previousWorkstation != null) {
                    addDependency(previousWorkstation.getId(), bestMachine.getId()); // O(1) * O(n * m * w) = O(n * m * w)
                }

                previousWorkstation = bestMachine;
            } else {
                pendingOperations.add(currentOperation);
                System.out.println("No best machine found.");
            }
        }
        operationQueue.addAll(pendingOperations);

        productTimes.put(product.getId(), productTotalTime);
    }
}

```

> What this algorithm do: This algorithm processes each product through a series of operations, finding the best machine for each operation, calculating the production time, and managing the flow of products through workstations.

> Result of the complexity analysis:  **O(n * m * w)** (where n is the number of products, m is the average number of operations per product, and w is the number of workstations) 

## Get Total Production Time
```java
private void processItems() {
    for (Product product : products) {  // O(n)
        LinkedList<Operation> operationQueue = operationQueues.getByKey(product);
        List<Operation> pendingOperations = new ArrayList<>();
        Workstation previousWorkstation = null;

        double productTotalTime = 0.0;

        while (!operationQueue.isEmpty()) { // O(m) * O(n)) = O(n * m)
            Operation currentOperation = operationQueue.poll();
            System.out.println("Current operation: " + currentOperation.getId());

            Workstation bestMachine = w.findBestMachineForOperation(currentOperation); // O(w) * O( n * p) = O (n * m * w) 
            if (bestMachine != null && bestMachine.isAvailable()) {
                System.out.println("The best machine: " + bestMachine.getId());

                double operationTime = bestMachine.getTime(); 
                productTotalTime += operationTime;
                totalProductionTime += operationTime; // O(1) * O(n * m * w) = O(n * m * w)

                bestMachine.setAvailable(false);
                w.increaseWaitingTimes(bestMachine.getTime());
                bestMachine.processProduct(product);
                bestMachine.setAvailable(true);

                List<String> machineFlow = productMachineFlows.computeIfAbsent(product.getId(), _ -> new ArrayList<>());
                machineFlow.add(bestMachine.getId());

                if (previousWorkstation != null) {
                    addDependency(previousWorkstation.getId(), bestMachine.getId()); // O(1) * O(n * m * w) = O(n * m * w)
                }

                previousWorkstation = bestMachine;
            } else {
                pendingOperations.add(currentOperation);
                System.out.println("No best machine found.");
            }
        }
        operationQueue.addAll(pendingOperations);

        productTimes.put(product.getId(), productTotalTime);
    }
}

public double getTotalProductionTime() {
    return totalProductionTime; // O(1)
}
```

> What this algorithm do: This algorithm calculates and returns the total production time for all products by processing each product through its operations and summing up the individual operation times.

> Result of the complexity analysis: **O(n * m * w)** (where n is the number of products, m is the average number of operations per product, and w is the number of workstations)


## Print Production Time Per Product
```java
 public void printProductionTimePerProduct() {
        System.out.println("\nProduction Time per Product:");
        for (Map.Entry<String, Double> entry : productTimes.entrySet()) { // O(n)
            System.out.println("Product ID: " + entry.getKey() + ", Production Time: " + entry.getValue() + " minutes");
        }
    }
```

> What this algorithm do: This algorithm prints the production time for each product by iterating through the productTimes map and displaying the product ID and its corresponding production time.

> Result of the complexity analysis: **O(n)** (where n is the number of products)



## Print Total Production Time
```java
 public void printTotalProductionTime() {
        System.out.println("\nTotal Production Time: " + getTotalProductionTime() + " minutes"); // O(1)
    }

```
> What this algorithm do: This algorithm prints the total production time for all products by calling the getTotalProductionTime() method and displaying the result.

> Result of the complexity analysis: **O(1)**