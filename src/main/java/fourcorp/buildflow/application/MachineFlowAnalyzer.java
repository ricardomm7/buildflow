package fourcorp.buildflow.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class MachineFlowAnalyzer {
    private static Map<String, Map<String, Integer>> machineDependencies;

    public MachineFlowAnalyzer() {
        this.machineDependencies = new HashMap<>();
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

        // Ordena as dependências com base no número de itens processados (em ordem decrescente)
        machineDependencies.forEach((machine, dependencies) -> {
            System.out.print(machine + " : ");
            List<Map.Entry<String, Integer>> sortedDependencies = dependencies.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Ordena em ordem decrescente de dependências
                    .toList();

            System.out.print("[");
            StringJoiner joiner = new StringJoiner(",");
            for (Map.Entry<String, Integer> entry : sortedDependencies) {
                joiner.add("(" + entry.getKey() + "," + entry.getValue() + ")");
            }
            System.out.print(joiner.toString());
            System.out.println("]");
        });
    }
}
