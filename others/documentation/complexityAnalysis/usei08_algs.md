# USEI08 - MachineFlowAnalyzer and Simulator

## Add Dependency
```java
private void processItems() {
    for (Product product : products) { // O(n)
        LinkedList<Operation> operationQueue = operationQueues.getByKey(product);
        List<Operation> pendingOperations = new ArrayList<>();
        Workstation previousWorkstation = null;

        double productTotalTime = 0.0;

        while (!operationQueue.isEmpty()) { // O(m) * O(n) = O(n * m)
            Operation currentOperation = operationQueue.poll();
            System.out.println("Current operation: " + currentOperation.getId());

            Workstation bestMachine = w.findBestMachineForOperation(currentOperation); // O(w) * O(n * m) = O(n * m * w)
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
    
public static void addDependency(String machine1, String machine2) {
    if (machine1.equals(machine2)) {
        return;  // Evita dependências circulares (máquina dependendo de si mesma)
    }
    machineDependencies.computeIfAbsent(machine1, k -> new HashMap<>());
    Map<String, Integer> dependencies = machineDependencies.get(machine1);
    int count = dependencies.getOrDefault(machine2, 0);
    dependencies.put(machine2, count + 1);  // Incrementa a contagem de dependências
}
```


> What this algorithm does: This algorithm processes a list of products, finding the best machine for each operation, updating machine dependencies, and calculating production times.

| Code                                               | Operations |
|:---------------------------------------------------|:-----------|
| (Product product : products)                       | n          |
| (!operationQueue.isEmpty())                        | m          |
| .findBestMachineForOperation(currentOperation)     | k          |
| (previousWorkstation.getId(), bestMachine.getId()) | 1          |
| .addAll(pendingOperations)                         | p          |

> Result of the complexity analysis: **O(n * m * k)**, where "n" is the number of products, "m" is the average number of operations per product, and "k" is the complexity of finding the best machine for an operation.


## Print Machine Dependencies
```java
public void printMachineDependencies() {
    System.out.println("\nDependencies between machines:");
    MachineFlowAnalyzer.printMachineDependencies(); // O(n^3)
}
    
public static void printMachineDependencies() {
    for (Map.Entry<String, Map<String, Integer>> entry : machineDependencies.entrySet()) { // O(n)
        String machine = entry.getKey();
        Map<String, Integer> dependencies = entry.getValue();
        List<Map.Entry<String, Integer>> sortedDependencies = new ArrayList<>(dependencies.entrySet());

        sortedDependencies.sort((e1, e2) -> e2.getValue() - e1.getValue()); // O(n) * O(log(m)) = O(n * log m)
        System.out.print(machine + " : [");

        for (int i = 0; i < sortedDependencies.size(); i++) { // O(m) * O(n * m log m) = O(n * m * log m)
            Map.Entry<String, Integer> dep = sortedDependencies.get(i);
            System.out.print("(" + dep.getKey() + "," + dep.getValue() + ")");
            if (i < sortedDependencies.size() - 1) {
                System.out.print(",");
            }
        }
        System.out.println("]");
    }
}
```

> What this algorithm does:
This algorithm prints the dependencies between machines, sorting them by the number of dependencies in descending order.

> Result of the complexity analysis: **O(n * m log m)**, where "n" is the number of machines and "m" is the average number of dependencies per machine.
