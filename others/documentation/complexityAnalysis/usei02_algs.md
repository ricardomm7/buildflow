# USEI02


## Create Operation Queues
```java
    private void createOperationQueues(List<Product> products) {
    operationQueues.removeAll();
    this.products.clear();
    for (Product product : products) { //O(n)
        this.products.add(product);
        for (Operation o : product.getOperations()) { //O (n^2)
            if (o != null) {
                operationQueues.newItem(o, product);
            }
        }
    }
}
```

> What this algorithm do: Initializes the operation queues by clearing the existing queue and products list. It then adds each product to the list and populates the operation queue with the operations associated with each product.
> Result of the complexity analysis: **O(n^2)**


## Process Items
```java
  private void processItems() {
    for (Product product : products) { // O(n)
        LinkedList<Operation> operationQueue = operationQueues.getByKey(product);
        List<Operation> pendingOperations = new ArrayList<>();
        Workstation previousWorkstation = null;

        double productTotalTime = 0.0;

        while (!operationQueue.isEmpty()) { // O(2n log n)
            Operation currentOperation = operationQueue.poll();
            System.out.println("Current operation: " + currentOperation.getId());

            Workstation bestMachine = w.findBestMachineForOperation(currentOperation);
            if (bestMachine != null && bestMachine.isAvailable()) {
                System.out.println("The best machine: " + bestMachine.getId());

                double operationTime = bestMachine.getTime();
                productTotalTime += operationTime;
                totalProductionTime += operationTime;

                bestMachine.setAvailable(false);
                w.increaseWaitingTimes(bestMachine.getTime());
                bestMachine.processProduct(product);
                bestMachine.setAvailable(true);

                List<String> machineFlow = productMachineFlows.computeIfAbsent(product.getId(), _ -> new ArrayList<>());
                machineFlow.add(bestMachine.getId());

                if (previousWorkstation != null) {
                    addDependency(previousWorkstation.getId(), bestMachine.getId());
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

> What this algorithm do: Processes the operation queue for each product. It tries to find the best available machine for each operation, updates production times, adds machine dependencies, and handles operations that could not be processed.

> Result of the complexity analysis: 

