package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.domain.PertCpmGraph;
import fourcorp.buildflow.external.Edge;

import java.util.*;

public class ActivitiesGraph {
    private PertCpmGraph graph;

    public ActivitiesGraph() {
        this.graph = new PertCpmGraph();
    }

    public PertCpmGraph getGraph() {
        return graph;
    }

    public void addActivity(Activity activity) {
        graph.addVertex(activity);
    }

    public void addDependency(Activity src, Activity dst) {
        graph.addEdge(src, dst);
    }

    public Activity[] getNeighbors(Activity activity) {
        Collection<Edge<Activity>> edges = graph.outgoingEdges(activity);
        List<Activity> neighbors = new ArrayList<>();
        for (Edge<Activity> edge : edges) {
            neighbors.add(edge.getVDest());
        }
        return neighbors.toArray(new Activity[0]);
    }

    public int numVertices() {
        return graph.numVertices();
    }

    public Map<Activity, Integer> getInDegrees() {
        Map<Activity, Integer> inDegrees = new HashMap<>();

        for (Activity vertex : graph.vertices()) {
            inDegrees.put(vertex, 0);
        }

        for (Edge<Activity> edge : graph.edges()) {
            Activity destination = edge.getVDest();
            inDegrees.put(destination, inDegrees.get(destination) + 1);
        }

        return inDegrees;
    }

    public String detectCircularDependencies() {
        Set<Activity> visited = new HashSet<>();
        Set<Activity> recursionStack = new HashSet<>();
        Set<String> uniqueCycles = new HashSet<>();
        List<Activity> currentPath = new ArrayList<>();

        for (Activity activity : graph.vertices()) {
            if (!visited.contains(activity)) {
                dfsCycleCheck(activity, visited, recursionStack, currentPath, uniqueCycles);
            }
        }

        return String.join(" - ", uniqueCycles);
    }

    private void dfsCycleCheck(Activity activity, Set<Activity> visited, Set<Activity> recursionStack, List<Activity> currentPath, Set<String> uniqueCycles) {
        if (recursionStack.contains(activity)) {
            int startIndex = -1;
            for (int i = 0; i < currentPath.size(); i++) {
                if (currentPath.get(i).equals(activity)) {
                    startIndex = i;
                    break;
                }
            }

            if (startIndex != -1) {
                StringBuilder cycleStr = new StringBuilder();
                for (int i = startIndex; i < currentPath.size(); i++) {
                    if (i > startIndex) {
                        cycleStr.append("->");
                    }
                    cycleStr.append(currentPath.get(i).getId());
                }
                cycleStr.append("->").append(activity.getId());
                uniqueCycles.add(cycleStr.toString());
            }
            return;
        }

        if (visited.contains(activity)) {
            return;
        }

        visited.add(activity);
        recursionStack.add(activity);
        currentPath.add(activity);

        for (Activity neighbor : getNeighbors(activity)) {
            dfsCycleCheck(neighbor, visited, recursionStack, currentPath, uniqueCycles);
        }

        recursionStack.remove(activity);
        currentPath.remove(currentPath.size() - 1);
    }

    public void setGraph(PertCpmGraph g) {
        this.graph = g;
    }
}
