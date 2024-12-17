package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.*;

public class ActivityTopologicalSort {

    private ActivitiesGraph graph;

    public ActivityTopologicalSort() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
    }

    /**
     * Perform a topological sort on the project activities.
     *
     * @return A list of activity IDs sorted in a valid order.
     * @throws IllegalStateException If the graph contains a cycle.
     */
    public List<Activity> performTopologicalSort() {
        Map<Activity, Integer> inDegree = new HashMap<>(graph.getInDegrees());
        Queue<Activity> zeroInDegreeQueue = new LinkedList<>();

        for (Activity activity : graph.getGraph().vertices()) { // O(n)
            if (!inDegree.containsKey(activity)) {
                inDegree.put(activity, 0);
            }
            if (inDegree.get(activity) == 0) {
                zeroInDegreeQueue.add(activity);
            }
        }

        List<Activity> sortedOrder = new ArrayList<>();
        while (!zeroInDegreeQueue.isEmpty()) { // O(n)
            Activity current = zeroInDegreeQueue.poll();
            sortedOrder.add(current);

            for (Activity neighbor : graph.getNeighbors(current)) { // O(n)
                int newInDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newInDegree);
                if (newInDegree == 0) {
                    zeroInDegreeQueue.add(neighbor);
                }
            }
        }


        //modificar este método pois não faz sentido, visto que as dependências circulares são verificadas no início do programa.
        if (sortedOrder.size() != graph.numVertices()) {
            boolean cycles = graph.detectCircularDependencies();
            throw new IllegalStateException(
                    "Graph contains a cycle. Topological sort is not possible. Cycles found: " + cycles
            );
        }
        return sortedOrder;
    }

    /**
     * Prints the topologically sorted activities.
     *
     * @param sortedActivities List of activities in topological order
     */
    public void printSortedActivities(List<Activity> sortedActivities) {
        System.out.println("Topologically Sorted Activities:");
        for (int i = 0; i < sortedActivities.size(); i++) {
            Activity activity = sortedActivities.get(i);
            System.out.printf("%d. %s (ID: %s)\n", i + 1, activity.getName(), activity.getId());
        }
    }

    /**
     * Handles the user interaction for performing and displaying the topological sort.
     */
    public void handleTopologicalSort() {
        try {
            List<Activity> sortedActivities = performTopologicalSort();
            printSortedActivities(sortedActivities);
        } catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGraph(ActivitiesGraph graph) {
        this.graph = graph;
    }
}
