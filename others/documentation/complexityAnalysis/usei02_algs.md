# USEI02 - Simulator


## Run Simulation
```java
    private void runSimulation(List<Product> products, boolean boo) {
    boolean itemsProcessed;
    try {
        do {
            itemsProcessed = false;
            List<Product> articlesToMove = new ArrayList<>();

            for (Product product : products) { // O(n) * O(n) = O(n^2)
                if (processedProducts.contains(product)) {
                    continue;
                }

                Operation currentOperation = product.getCurrentOperation(); // O(n^2) * O(1) = O(n^2)

                if (currentOperation != null) {
                    List<Workstation> availableWorkstations = workstationsPerOperation.getWorkstationsByOperation(currentOperation, boo);

                    boolean operationStarted = false;
                    for (Workstation workstation : availableWorkstations) { // O(n^2) * O(n) = O(n^3)
                        if (workstation.isAvailable()) {
                            operationStarted = true;
                            workstation.processProduct(product); // O(n^3) * O(1) = O(n^3)
                            double operationTime = workstation.getTime();

                            productTimes.merge(product, operationTime, Double::sum);
                            totalProductionTime += operationTime;

                            String operationName = currentOperation.getId();
                            operationTimes.merge(operationName, operationTime, Double::sum);

                            operationCounts.merge(operationName, 1, Integer::sum);

                            machineFlowAnalyzer.addFlow(workstation, product);

                            itemsProcessed = true;

                            if (product.moveToNextOperation()) {
                                articlesToMove.add(product);
                                System.out.println("Moving product " + product.getIdItem() + " to the next operation: " + product.getCurrentOperation().getId());
                            } else {
                                processedProducts.add(product);
                                System.out.println("Product " + product.getIdItem() + " has completed all operations.");
                            }

                            break;
                        }
                    }

                    if (!operationStarted) {
                        addToWaitingQueue(product, currentOperation);
                        itemsProcessed = true;
                    }
                }
            }

            processWaitingQueue();

            for (Product product : articlesToMove) {
                Operation nextOperation = product.getCurrentOperation();
                if (nextOperation != null) {
                    System.out.println("Adding product " + product.getIdItem() + " to queue for operation: " + nextOperation.getId());
                }
            }

        } while (itemsProcessed || !areProductsQueueEmpty() && processedProducts.isEmpty()); // O(n)

    } catch (Exception e) {
        System.out.println("Error during simulation: " + e.getMessage());
    }
}
```

> What this algorithm do: This algorithm runs the simulation of the production process.
> Result of the complexity analysis: **O(n^3)**

## Process Waiting Queue

```java
  private void processWaitingQueue() {
    for (Map.Entry<String, Queue<Product>> entry : waitingQueue.entrySet()) { // O(n)
        String operationId = entry.getKey();
        Queue<Product> queue = entry.getValue();

        while (!queue.isEmpty()) { // O(n) * O(n) = O(n^2)
            Product product = queue.peek();
            Operation currentOperation = product.getCurrentOperation();

            if (currentOperation != null && currentOperation.getId().equals(operationId)) {
                List<Workstation> availableWorkstations = workstationsPerOperation.getWorkstationsByOperation(currentOperation, false);

                for (Workstation workstation : availableWorkstations) { // O(n^2) * O(n) = O(n^3)
                    if (workstation.isAvailable()) {
                        calculateFinishWaiting(currentOperation);
                        queue.poll();
                        workstation.processProduct(product); // O(n^3) * O(1) = O(n^3)
                        double operationTime = workstation.getTime();

                        productTimes.merge(product, operationTime, Double::sum);
                        totalProductionTime += operationTime;
                        operationTimes.merge(operationId, operationTime, Double::sum);

                        operationCounts.merge(currentOperation.getId(), 1, Integer::sum);

                        machineFlowAnalyzer.addFlow(workstation, product);


                        if (product.moveToNextOperation()) {
                            System.out.println("Moving product " + product.getIdItem() + " to the next operation: " + product.getCurrentOperation().getId());
                        } else {
                            processedProducts.add(product);
                            System.out.println("Product " + product.getIdItem() + " has completed all operations.");
                        }
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }
}
```

> What this algorithm do: This algorithm processes the waiting queue by iterating through each operation in the waiting
> queue.

> Result of the complexity analysis: **O(n^3)**

## Get Workstations by Operation

```java
public List<Workstation> getWorkstationsByOperation(Operation operation, boolean b) {
    List<Workstation> availableWorkstations = new ArrayList<>(); // O(1)
    for (Operation keyOperation : workstationsPerOperation.getKeys()) { // O(n)
        if (keyOperation.getId().equals(operation.getId())) {
            List<Workstation> workstations = peworkstationsPerOperation.getByKey(keyOration); // O(n) * O(1) = O(n)
            for (Workstation workstation : workstations) { // O(n) * O(n) = O(n^2)
                if (workstation.isAvailable()) {
                    availableWorkstations.add(workstation); // O(n^2) * O(1) = O(n^2)
                }
            }
            break;
        }
    }
    if (b) {
        availableWorkstations.sort(Comparator.comparingDouble(Workstation::getTime)); // O(n log n)
    }
}
```

> What this algorithm do: This algorithm retrieves a list of workstations that are available for a given operation, in
> order of the list (AC1) or in order of the time (AC2)

> Result of the complexity analysis: **O(n^2)**

## Process Product

```java
public void processProduct(Product product) {
    System.out.println("Processing product " + product.getId() + " in machine " + idMachine + " - Estimated time: " + time + " sec");
    startClock(); // O(1)
}

public void startClock() {
    this.isAvailable = false;
    clock.countDownClock(this.time, () -> {
        this.isAvailable = true;
        increaseOpCounter();
        increaseOperationTime();
    });
}
```

> What this algorithm do: This algorithm processes a product in a workstation, printing a message and starting a
> countdown timer.

> Result of the complexity analysis: **O(1)**
