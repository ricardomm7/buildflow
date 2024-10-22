package fourcorp.buildflow.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The MachineFlowAnalyzer class is responsible for managing the dependencies between machines in a build flow.
 * It provides methods to add dependencies between machines, print the current dependencies, and clear the dependencies.
 */
public class MachineFlowAnalyzer {

    /**
     * A map that stores the dependencies between machines.
     * It maps each machine to its dependencies, where each dependency is a machine and the value is the count of dependencies.
     */
    public static Map<String, Map<String, Integer>> machineDependencies = new HashMap<>();

    /**
     * Adds a dependency between two machines.
     * <p>
     * If machine1 depends on machine2, this dependency is recorded. If machine1 is the same as machine2,
     * the method returns immediately to avoid circular dependencies.
     *
     * @param machine1 the machine that has the dependency
     * @param machine2 the machine that machine1 depends on
     *
     */
    public static void addDependency(String machine1, String machine2) {
        if (machine1.equals(machine2)) {
            return;  // Evita dependências circulares (máquina dependendo de si mesma)
        }
        machineDependencies.computeIfAbsent(machine1, k -> new HashMap<>());
        Map<String, Integer> dependencies = machineDependencies.get(machine1);
        int count = dependencies.getOrDefault(machine2, 0);
        dependencies.put(machine2, count + 1);  // Incrementa a contagem de dependências
    }

    /**
     * For each machine, it prints the list of machines it depends on along with the count of those dependencies.
     * The dependencies for each machine are sorted in descending order of the count.
     *
     */
    public static void printMachineDependencies() {
        for (Map.Entry<String, Map<String, Integer>> entry : machineDependencies.entrySet()) {
            String machine = entry.getKey();
            Map<String, Integer> dependencies = entry.getValue();
            List<Map.Entry<String, Integer>> sortedDependencies = new ArrayList<>(dependencies.entrySet());

            sortedDependencies.sort((e1, e2) -> e2.getValue() - e1.getValue());
            System.out.print(machine + " : [");

            for (int i = 0; i < sortedDependencies.size(); i++) {
                Map.Entry<String, Integer> dep = sortedDependencies.get(i);
                System.out.print("(" + dep.getKey() + "," + dep.getValue() + ")");
                if (i < sortedDependencies.size() - 1) {
                    System.out.print(",");
                }
            }
            System.out.println("]");
        }
    }

    /**
     * This method removes all entries from the machineDependencies map.
     */
    public static void clearDependencies() {
        machineDependencies.clear();
    }
}
