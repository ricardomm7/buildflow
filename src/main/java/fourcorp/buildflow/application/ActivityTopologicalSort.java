package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.*;

/**
 * Class responsible for performing topological sorting of activities in a project graph.
 */
public class ActivityTopologicalSort {

    private ActivitiesGraph graph;

    /**
     * Constructor: Initializes the topological sort utility with the default graph.
     */
    public ActivityTopologicalSort() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
    }

    /**
     * Performs a topological sort on the project activities.
     * Complexity: O(n^2), where n is the number of activities.
     *
     * @return A list of activities sorted in a valid topological order.
     * @throws IllegalStateException If the graph contains a cycle.
     */
    public List<Activity> performTopologicalSort() {
        Map<Activity, Integer> inDegree = new HashMap<>(graph.getInDegrees());
        Queue<Activity> zeroInDegreeQueue = new LinkedList<>();

        for (Activity activity : graph.getGraph().vertices()) { // O(n)
            if (!inDegree.containsKey(activity)) {
                inDegree.put(activity, 0); // O(1) * O(n) = O(n)
            }
            if (inDegree.get(activity) == 0) {
                zeroInDegreeQueue.add(activity); // O(1) * O(n) = O(n)
            }
        }

        List<Activity> sortedOrder = new ArrayList<>();
        while (!zeroInDegreeQueue.isEmpty()) { // O(n)
            Activity current = zeroInDegreeQueue.poll(); // O(1)
            sortedOrder.add(current); // O(1)

            for (Activity neighbor : graph.getNeighbors(current)) { // O(n)* O(n) = O(n^2)
                int newInDegree = inDegree.get(neighbor) - 1; // O(1) * O(n^2) = O(n^2)
                inDegree.put(neighbor, newInDegree); // O(1) * O(n^2) = O(n^2)
                if (newInDegree == 0) {
                    zeroInDegreeQueue.add(neighbor); // O(1) * O(n^2) = O(n^2)
                }
            }
        }

        if (sortedOrder.size() != graph.numVertices()) { // O(1)
            boolean cycles = graph.detectCircularDependencies(); // O(n)
            throw new IllegalStateException(
                    "Graph contains a cycle. Topological sort is not possible. Cycles found: " + cycles
            );
        }
        return sortedOrder;
    }

    /**
     * Prints the topologically sorted activities in a readable format.
     * Complexity: O(n), where n is the number of activities.
     *
     * @param sortedActivities List of activities in topological order.
     */
    public void printSortedActivities(List<Activity> sortedActivities) {
        System.out.println("Topologically Sorted Activities:");
        for (int i = 0; i < sortedActivities.size(); i++) { // O(n)
            Activity activity = sortedActivities.get(i); // O(1)
            System.out.printf("%d. %s (ID: %s)\n", i + 1, activity.getName(), activity.getId()); // O(1)
        }
    }

    /**
     * Handles the user interaction for performing and displaying the topological sort.
     * Complexity: O(n^2), where n is the number of activities.
     */
    public void handleTopologicalSort() {
        try {
            List<Activity> sortedActivities = performTopologicalSort(); // O(n^2)
            printSortedActivities(sortedActivities); // O(n)
        } catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage()); // O(1)
        } catch (Exception e) {
            e.printStackTrace(); // O(1)
        }
    }

    /**
     * Sets the project graph for topological sorting.
     * Complexity: O(1).
     *
     * @param graph The project graph to be used.
     */
    public void setGraph(ActivitiesGraph graph) {
        this.graph = graph; // O(1)
    }
}

