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

    public boolean detectCircularDependencies() {
        Map<Activity, Integer> color = new HashMap<>();

        // Inicializar todos os nós como "White" (0)
        for (Activity activity : graph.vertices()) {
            color.put(activity, 0);
        }

        // Executar o DFS
        for (Activity activity : graph.vertices()) {
            if (color.get(activity) == 0) { // White
                if (colorDFS(activity, color)) {
                    return true; // Ciclo detectado
                }
            }
        }

        return false; // Nenhum ciclo detectado
    }

    private boolean colorDFS(Activity activity, Map<Activity, Integer> color) {
        color.put(activity, 1); // Marcar como "Gray" (1)

        for (Activity neighbor : getNeighbors(activity)) {
            if (color.get(neighbor) == 1) {
                // Encontrou um nó cinza -> ciclo detectado
                return true;
            }
            if (color.get(neighbor) == 0) { // White
                if (colorDFS(neighbor, color)) {
                    return true;
                }
            }
        }

        color.put(activity, 2); // Marcar como "Black" (2)
        return false; // Nenhum ciclo encontrado a partir deste nó
    }

    public void setGraph(PertCpmGraph g) {
        this.graph = g;
    }
}
