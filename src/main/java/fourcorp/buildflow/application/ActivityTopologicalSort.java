package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.*;

public class ActivityTopologicalSort {

    private final ActivitiesGraph graph;

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
        Map<Activity, Integer> inDegree = graph.getInDegrees();
        Queue<Activity> zeroInDegreeQueue = new LinkedList<>();
        for (var entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                zeroInDegreeQueue.add(entry.getKey());
            }
        }

        List<Activity> sortedOrder = new ArrayList<>();
        while (!zeroInDegreeQueue.isEmpty()) {
            Activity current = zeroInDegreeQueue.poll();
            sortedOrder.add(current);

            for (Activity neighbor : graph.getNeighbors(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    zeroInDegreeQueue.add(neighbor);
                }
            }
        }

        if (sortedOrder.size() != graph.numVertices()) {
            throw new IllegalStateException("Graph contains a cycle. Topological sort is not possible.");
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
}
