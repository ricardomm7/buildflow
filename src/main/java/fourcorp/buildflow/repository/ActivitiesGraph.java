package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.domain.Graph;

import java.util.*;

public class ActivitiesGraph {
    private Graph graph;

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

    public Activity[] getNeighbors(Activity activity) {
        LinkedList<Activity> adjacencyList = graph.getAdjacencyList()
                .stream()
                .filter(list -> list.getFirst().equals(activity))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Activity not found in the graph"));

        return adjacencyList.subList(1, adjacencyList.size()).toArray(new Activity[0]);
    }

    public int numVertices() {
        return graph.getAdjacencyList().size();
    }

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

    public String detectCircularDependencies() {
        Set<Activity> visited = new HashSet<>();
        Set<Activity> recursionStack = new HashSet<>();

        StringBuilder cicleIds = new StringBuilder();
        for (LinkedList<Activity> list : graph.getAdjacencyList()) {
            Activity activity = list.getFirst();
            String cycleActivityId = dfsCycleCheck(activity, visited, recursionStack);
            if (cycleActivityId != null) {
                cicleIds.append(" - ").append(cycleActivityId);
            }
        }
        return cicleIds.toString();
    }

    private String dfsCycleCheck(Activity activity, Set<Activity> visited, Set<Activity> recursionStack) {
        // If the activity is already in the recursion stack, we've found a cycle
        if (recursionStack.contains(activity)) {
            return activity.getId();
        }

        // If already fully explored, no cycle through this node
        if (visited.contains(activity)) {
            return null;
        }

        // Mark as visited and add to recursion stack
        visited.add(activity);
        recursionStack.add(activity);

        // Check neighbors
        for (Activity neighbor : getNeighbors(activity)) {
            String cycleActivityId = dfsCycleCheck(neighbor, visited, recursionStack);
            if (cycleActivityId != null) {
                return cycleActivityId;
            }
        }

        // Remove from recursion stack after exploring all neighbors
        recursionStack.remove(activity);
        return null;
    }

    public void setGraph(Graph g) {
        this.graph = g;
    }
}