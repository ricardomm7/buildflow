package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.MapLinked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code MachineFlowAnalyzer} class is responsible for analyzing and displaying
 * the flow of dependencies between workstations in a production process.
 * It maintains the flow of products through various workstations and builds
 * dependencies based on product transitions between the workstations.
 */
public class MachineFlowAnalyzer {

    static MapLinked<Workstation, Product, String> flowDependency = new MapLinked<>();
    static Map<String, Map<String, Integer>> workstationDependencies = new HashMap<>();

    /**
     * Constructs a new {@code MachineFlowAnalyzer}, initializing the flowDependency
     * and workstationDependencies maps.
     */
    public MachineFlowAnalyzer() {
        flowDependency = new MapLinked<>(); // Inicializa o mapa de fluxo
        workstationDependencies = new HashMap<>(); // Inicializa o mapa de dependências
    }

    /**
     * Adds a new entry to the flow dependency for the specified workstation and product.
     *
     * @param workstation the workstation involved in the product flow
     * @param product     the product being processed at the workstation
     * @throws IllegalArgumentException if the {@code workstation} or {@code product} is {@code null}
     */
    public void addFlow(Workstation workstation, Product product) {
        if (workstation == null || product == null) {
            throw new IllegalArgumentException("Workstation and Product cannot be null.");
        }
        flowDependency.newItem(workstation, product);
    }

    /**
     * Builds the dependencies between workstations based on the flow of products.
     * It analyzes the sequence of workstations for each product and updates the
     * transition counts between consecutive workstations.
     */
    public static void buildDependencies() {
        List<Product> products = flowDependency.getKeys(); // Obtém todos os produtos registrados no fluxo

        for (Product product : products) {
            List<Workstation> path = flowDependency.getByKey(product);
            for (int i = 0; i < path.size() - 1; i++) {
                String fromId = path.get(i).getId();
                String toId = path.get(i + 1).getId();
                updateWorkstationDependencies(fromId, toId);
            }
        }
    }

    /**
     * Updates the dependencies between workstations by incrementing the count
     * of transitions from one workstation to another.
     *
     * @param fromWorkstationId the ID of the originating workstation
     * @param toWorkstationId   the ID of the destination workstation
     */
    private static void updateWorkstationDependencies(String fromWorkstationId, String toWorkstationId) {
        workstationDependencies
                .computeIfAbsent(fromWorkstationId, _ -> new HashMap<>())
                .merge(toWorkstationId, 1, Integer::sum);
    }

    /**
     * Prints the dependencies between workstations in descending order of the
     * frequency of transitions. The output shows each workstation and its
     * dependencies with other workstations.
     */
    public static void printDependencies() {
        buildDependencies(); // Constrói as dependências antes de imprimir

        String lineFormat = "%s : %s%n";
        System.out.println("\nWorkstation Flow Dependency:");

        // Mapa auxiliar para armazenar a contagem total de transições para cada workstation
        Map<String, Integer> totalTransitions = new HashMap<>();

        // Calcula o número total de transições de cada workstation
        for (Map.Entry<String, Map<String, Integer>> entry : workstationDependencies.entrySet()) {
            int sum = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
            totalTransitions.put(entry.getKey(), sum);
        }

        // Ordena as workstations em ordem decrescente de total de transições
        totalTransitions.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> {
                    String workstation = entry.getKey();
                    List<String> dependencies = new ArrayList<>();

                    // Ordena as dependências em ordem decrescente de frequência
                    workstationDependencies.get(workstation).entrySet().stream()
                            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                            .forEach(e -> dependencies.add(String.format("(%s,%d)", e.getKey(), e.getValue())));

                    System.out.printf(lineFormat, workstation, dependencies);
                });
    }

    /**
     * Gets the map representing the workstation dependencies, where each key is a workstation ID
     * and the corresponding value is another map of target workstation IDs and transition counts.
     *
     * @return the map of workstation dependencies
     */
    public static Map<String, Map<String, Integer>> getWorkstationDependencies() {
        return workstationDependencies;
    }

    /**
     * Clears all stored data, including flow dependencies and workstation dependencies,
     * to reset the analyzer for a new analysis.
     */
    public static void reset() {
        flowDependency.removeAll();
        workstationDependencies.clear();
    }
}
