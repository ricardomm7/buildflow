# USEI07 - Class MachineFlowAnalyser

## Flow dependency

```java
public static void buildDependencies() {
    List<Product> products = flowDependency.getKeys(); // Obtém todos os produtos registrados no fluxo

    for (Product product : products) { // O(n)
        List<Workstation> path = flowDependency.getByKey(product);
        for (int i = 0; i < path.size() - 1; i++) { // O(n) * O(1) 
            String fromId = path.get(i).getId();
            String toId = path.get(i + 1).getId();
            updateWorkstationDependencies(fromId, toId);
        }
    }
}
```

> What this algorithm do: This code builds dependencies between workstations based on the flow path

> Result of the complexity analysis: **O(n)** (where n is the total number of products)

```java
    public static void printDependencies() {
        buildDependencies(); // Constrói as dependências antes de imprimir

        String lineFormat = "%s : %s%n";
        System.out.println("\nWorkstation Flow Dependency:");

        // Mapa auxiliar para armazenar a contagem total de transições para cada workstation
        Map<String, Integer> totalTransitions = new HashMap<>();

        // Calcula o número total de transições de cada workstation
        for (Map.Entry<String, Map<String, Integer>> entry : workstationDependencies.entrySet()) { // O(n)
            int sum = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();  // O(n) * O(1)
            totalTransitions.put(entry.getKey(), sum); // O(n) * O(1)
        }

        // Ordena as workstations em ordem decrescente de total de transições
        totalTransitions.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // O(nlog(n))
                .forEach(entry -> {
                    String workstation = entry.getKey();
                    List<String> dependencies = new ArrayList<>();

                    // Ordena as dependências em ordem decrescente de frequência
                    workstationDependencies.get(workstation).entrySet().stream()
                            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // O(nlog(n))
                            .forEach(e -> dependencies.add(String.format("(%s,%d)", e.getKey(), e.getValue())));

                    System.out.printf(lineFormat, workstation, dependencies);
                });
    }
```

> What this algorithm do: This printDependencies method displays the flow dependencies of a list of workstations, where each workstation has transitions to other workstations

> Result of the complexity analysis: **O(nlog(n))** (where n is the total number of workstations)
