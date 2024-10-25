package fourcorp.buildflow.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class MachineFlowAnalyzer {
    private static Map<String, Map<String, Integer>> machineDependencies;

    public MachineFlowAnalyzer() {
        machineDependencies = new HashMap<>();
    }

    // Método para calcular as dependências entre máquinas
    public void calculateMachineDependencies(Map<String, List<String>> productMachineFlows) {
        for (List<String> machineFlow : productMachineFlows.values()) {
            for (int i = 0; i < machineFlow.size() - 1; i++) {
                String currentMachine = machineFlow.get(i);
                String nextMachine = machineFlow.get(i + 1);

                // Atualiza a contagem da dependência entre as duas máquinas
                machineDependencies.computeIfAbsent(currentMachine, k -> new HashMap<>())
                        .merge(nextMachine, 1, Integer::sum);
            }
        }
    }

    // Método para exibir as dependências entre máquinas
    public static void printMachineFlowDependencies() {
        System.out.println("\n=== Machine Flow Dependencies ===");

        // Ordena as máquinas por ID e depois suas dependências
        machineDependencies.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                String machine = entry.getKey();
                Map<String, Integer> dependencies = entry.getValue();

                System.out.print(machine + " : ");
                List<Map.Entry<String, Integer>> sortedDependencies = dependencies.entrySet().stream()
                        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Ordena em ordem decrescente de dependências
                        .toList();

                System.out.print("[");
                StringJoiner joiner = new StringJoiner(",");
                for (Map.Entry<String, Integer> dep : sortedDependencies) {
                    joiner.add("(" + dep.getKey() + "," + dep.getValue() + ")");
                }
                System.out.print(joiner.toString());
                System.out.println("]");
            });
    }

    public static void clearDependencies() {
        machineDependencies.clear();
    }

    public static Map<String, Map<String, Integer>> getMachineDependencies() {
        return new HashMap<>(machineDependencies);
    }
}
