package fourcorp.buildflow.application;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineFlowAnalyzer {

    static Map<String, Map<String, Integer>> machineDependencies = new HashMap<>();

    public static void addDependency(String machine1, String machine2) {
        if (!machineDependencies.containsKey(machine1)) {
            machineDependencies.put(machine1, new HashMap<>());
        }

        Map<String, Integer> dependencies = machineDependencies.get(machine1);
        int count = dependencies.getOrDefault(machine2, 0);
        dependencies.put(machine2, count + 1);

    }


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
}
