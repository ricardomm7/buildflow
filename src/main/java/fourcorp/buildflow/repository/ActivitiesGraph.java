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

    /**
     * Detects if the directed graph contains any circular dependencies.
     * <p>
     * This method performs a Depth-First Search (DFS) across all vertices
     * in the graph using a color-based algorithm. Each vertex is marked
     * with one of three colors during the traversal:
     * <ul>
     *   <li><b>White (0):</b> The vertex has not been visited yet.</li>
     *   <li><b>Gray (1):</b> The vertex is currently being visited (in the recursion stack).</li>
     *   <li><b>Black (2):</b> The vertex has been fully processed.</li>
     * </ul>
     * If a gray vertex is encountered during the traversal, it indicates
     * the presence of a cycle in the graph.
     * </p>
     * The complexity of this method is: O(n).
     *
     * @return {@code true} if a circular dependency is detected, {@code false} otherwise.
     */
    public boolean detectCircularDependencies() {
        Map<Activity, Integer> color = new HashMap<>();

        // Inicializar todos os nós como "White" (0)
        for (Activity activity : graph.vertices()) { // O(n)
            color.put(activity, 0);
        }

        // Executar o DFS
        for (Activity activity : graph.vertices()) { // O(n)
            if (color.get(activity) == 0) { // White
                if (colorDFS(activity, color)) {
                    return true; // Ciclo detectado
                }
            }
        }

        return false; // Nenhum ciclo detectado
    }

    /**
     * Helper method to perform a recursive Depth-First Search (DFS) on the graph
     * to detect cycles.
     * <p>
     * This method is called by {@link #detectCircularDependencies()} to traverse
     * the graph. It uses a map to keep track of the state of each vertex during
     * the traversal:
     * <ul>
     *   <li><b>White (0):</b> Not visited yet.</li>
     *   <li><b>Gray (1):</b> In the current recursion stack (potential cycle).</li>
     *   <li><b>Black (2):</b> Fully processed, no cycle detected from this vertex.</li>
     * </ul>
     * The method returns {@code true} if a cycle is detected during the traversal,
     * and {@code false} otherwise.
     * </p>
     * The complexity of this method is: O(n).
     *
     * @param activity the current vertex being processed.
     * @param color    a map representing the state (color) of each vertex during traversal.
     * @return {@code true} if a cycle is detected starting from the given vertex,
     * {@code false} otherwise.
     */
    protected boolean colorDFS(Activity activity, Map<Activity, Integer> color) {
        color.put(activity, 1); // Marcar como "Gray" (1)

        for (Activity neighbor : getNeighbors(activity)) { // O(n)
            if (color.get(neighbor) == 1) {
                // Encontrou um nó cinza -> ciclo detectado
                return true;
            }
            if (color.get(neighbor) == 0) { // White
                if (colorDFS(neighbor, color)) { // O(n)
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

    public Activity findActivityById(String a) {
        for (Activity activity: graph.vertices()){
            if (activity.getId().equals(a)) {
                return activity;
            }
        }
        return null;
    }
}
