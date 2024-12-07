package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.domain.Graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

public class ActivitiesGraph {
    private final Graph graph;

    public ActivitiesGraph() {
        this.graph = new Graph();
    }

    public Graph getGraph() {
        return graph;
    }

    public void addActivity(Activity activity) {
        graph.addNode(activity);
    }

    public void addDependency(int src, int dst) {
        graph.addEdge(src, dst);
    }

    public void printGraph() {
    }

    /**
     * Retrieves the neighbors (outgoing connections) of a given activity.
     *
     * @param activity The activity whose neighbors are to be retrieved.
     * @return An array of neighboring activities.
     */
    public Activity[] getNeighbors(Activity activity) {
        LinkedList<Activity> adjacencyList = graph.getAdjacencyList()
                .stream()
                .filter(list -> list.getFirst().equals(activity))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Activity not found in the graph"));

        return adjacencyList.subList(1, adjacencyList.size()).toArray(new Activity[0]);
    }


    /**
     * Returns the total number of vertices (activities) in the graph.
     *
     * @return The number of vertices in the graph.
     */
    public int numVertices() {
        return graph.getAdjacencyList().size();
    }

    /**
     * Calculates the in-degrees for all activities in the graph.
     *
     * @return A map where keys are activities and values are the in-degree counts.
     */
    public Map<Activity, Integer> getInDegrees() {
        Map<Activity, Integer> inDegrees = new HashMap<>();
        for (LinkedList<Activity> linkedList : graph.getAdjacencyList()) {
            Activity current = linkedList.getFirst();
            inDegrees.putIfAbsent(current, 0);
            for (int i = 1; i < linkedList.size(); i++) {
                Activity neighbor = linkedList.get(i);
                inDegrees.put(neighbor, inDegrees.getOrDefault(neighbor, 0) + 1);
            }
        }
        return inDegrees;
    }

    public ActivitiesGraph clone() {
        ActivitiesGraph clonedGraph = new ActivitiesGraph();

        // Clone each activity in the graph
        Map<Activity, Activity> activityMapping = new HashMap<>();

        // First pass: create cloned activities
        for (LinkedList<Activity> linkedList : graph.getAdjacencyList()) {
            Activity originalActivity = linkedList.getFirst();
            Activity clonedActivity = getActivity(originalActivity);

            clonedGraph.addActivity(clonedActivity);
            activityMapping.put(originalActivity, clonedActivity);
        }

        // Second pass: recreate dependencies
        for (LinkedList<Activity> linkedList : graph.getAdjacencyList()) {
            Activity originalSourceActivity = linkedList.getFirst();
            Activity clonedSourceActivity = activityMapping.get(originalSourceActivity);

            // Skip first element (source activity itself) and add dependencies
            for (int i = 1; i < linkedList.size(); i++) {
                Activity originalDependentActivity = linkedList.get(i);
                Activity clonedDependentActivity = activityMapping.get(originalDependentActivity);

                // Find the index of source and dependent activities in the graph
                int sourceIndex = graph.getAdjacencyList().stream()
                        .map(LinkedList::getFirst)
                        .toList()
                        .indexOf(originalSourceActivity);

                int dependentIndex = graph.getAdjacencyList().stream()
                        .map(LinkedList::getFirst)
                        .toList()
                        .indexOf(originalDependentActivity);

                // Add the edge in the cloned graph
                clonedGraph.addDependency(sourceIndex, dependentIndex);
            }
        }

        return clonedGraph;
    }

    private static Activity getActivity(Activity originalActivity) {
        Activity clonedActivity = new Activity(
                originalActivity.getId(),
                originalActivity.getName(),
                originalActivity.getDuration(),
                originalActivity.getDurationUnit(),
                originalActivity.getCost(),
                originalActivity.getCostUnit(),
                originalActivity.getDependencies()
        );

        // Copy additional properties if needed
        clonedActivity.setEarlyStart(originalActivity.getEarlyStart());
        clonedActivity.setEarlyFinish(originalActivity.getEarlyFinish());
        clonedActivity.setLateStart(originalActivity.getLateStart());
        clonedActivity.setLateFinish(originalActivity.getLateFinish());
        return clonedActivity;
    }
}