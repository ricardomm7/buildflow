package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.MapLinked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por analisar e exibir o fluxo de dependência entre máquinas.
 */
public class MachineFlowAnalyzer {

    static MapLinked<Workstation, Product, String> flowDependency = new MapLinked<>(); // Armazena o fluxo de produtos entre estações
    static Map<String, Map<String, Integer>> workstationDependencies = new HashMap<>(); // Dependências entre workstations

    public MachineFlowAnalyzer() {
        flowDependency = new MapLinked<>(); // Inicializa o mapa de fluxo
        workstationDependencies = new HashMap<>(); // Inicializa o mapa de dependências
    }


    /**
     * Adiciona uma nova entrada ao fluxo de dependência para o produto e a workstation.
     */
    public void addFlow(Workstation workstation, Product product) {
        if (workstation == null || product == null) {
            throw new IllegalArgumentException("Workstation and Product cannot be null.");
        }
        flowDependency.newItem(workstation, product);
    }

    /**
     * Constrói as dependências entre as workstations com base no fluxo dos produtos.
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
     * Atualiza as dependências entre as workstations com uma nova transição.
     */
    private static void updateWorkstationDependencies(String fromWorkstationId, String toWorkstationId) {
        workstationDependencies
                .computeIfAbsent(fromWorkstationId, _ -> new HashMap<>())
                .merge(toWorkstationId, 1, Integer::sum);
    }

    /**
     * Imprime as dependências entre as workstations, ordenadas em ordem decrescente de frequência de transições.
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
     * Gets workstation dependencies.
     *
     * @return the workstation dependencies
     */
    public static Map<String, Map<String, Integer>> getWorkstationDependencies() {
        return workstationDependencies;
    }


    /**
     * Limpa os dados armazenados para uma nova simulação.
     */
    public static void reset() {
        flowDependency.removeAll();
        workstationDependencies.clear();
    }
}
