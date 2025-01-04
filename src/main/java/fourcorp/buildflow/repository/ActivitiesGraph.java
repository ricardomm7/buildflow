package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.domain.PertCpmGraph;
import fourcorp.buildflow.external.Edge;

import java.util.*;

public class ActivitiesGraph {
    private PertCpmGraph graph;

    /**
     * Initializes an empty ActivitiesGraph.
     */
    public ActivitiesGraph() {
        this.graph = new PertCpmGraph();
    }

    /**
     * Retrieves the underlying graph representation.
     *
     * @return the {@code PertCpmGraph} instance representing this graph.
     */
    public PertCpmGraph getGraph() {
        return graph;
    }

    /**
     * Adds a new activity to the graph.
     *
     * @param activity the activity to be added.
     * @throws IllegalArgumentException if the activity is null.
     */
    public void addActivity(Activity activity) {
        graph.addVertex(activity);
    }

    /**
     * Adds a directed dependency (edge) between two activities.
     *
     * @param src the source activity.
     * @param dst the destination activity.
     * @throws IllegalArgumentException if either activity is null.
     */
    public void addDependency(Activity src, Activity dst) {
        graph.addEdge(src, dst);
    }

    /**
     * Retrieves the neighboring activities (outgoing edges) of a given activity.
     *
     * @param activity the activity whose neighbors are to be retrieved.
     * @return an array of neighboring activities.
     * Complexity: O(n).
     */
    public Activity[] getNeighbors(Activity activity) {
        Collection<Edge<Activity>> edges = graph.outgoingEdges(activity);
        List<Activity> neighbors = new ArrayList<>();
        for (Edge<Activity> edge : edges) {
            neighbors.add(edge.getVDest());
        }
        return neighbors.toArray(new Activity[0]);
    }

    /**
     * Returns the number of vertices in the graph.
     *
     * @return the number of vertices.
     */
    public int numVertices() {
        return graph.numVertices();
    }

    /**
     * Computes the in-degree (number of incoming edges) for each activity in the graph.
     *
     * @return a map of activities to their in-degree counts.
     * Complexity: O(n).
     */
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
        for (Activity activity : graph.vertices()) {
            if (activity.getId().equals(a)) {
                return activity;
            }
        }
        return null;
    }

    public void clearGraph() {
        graph.clear();
    }

    /**
     * Gets the incoming edges of a given activity.
     * <p>
     * This method collects all activities that have edges pointing to the given activity.
     * Complexity: O(n), where n is the number of edges in the graph.
     * </p>
     *
     * @param activity the activity whose incoming edges are to be found.
     * @return a list of activities that have outgoing edges to the given activity.
     */
    public List<Activity> getIncomingEdges(Activity activity) {
        List<Activity> incomingActivities = new ArrayList<>();

        for (Edge<Activity> edge : graph.edges()) { // O(n)
            if (edge.getVDest().equals(activity)) {
                incomingActivities.add(edge.getVOrig()); // O(1)
            }
        }

        return incomingActivities;
    }

    /**
     * Gets the outgoing edges of a given activity.
     * <p>
     * This method retrieves all edges originating from the given activity.
     * Complexity: O(n), where n is the number of edges connected to the activity.
     * </p>
     *
     * @param activity the activity whose outgoing edges are to be found.
     * @return a list of edges originating from the given activity.
     */
    public List<Edge<Activity>> getOutgoingEdges(Activity activity) {
        List<Edge<Activity>> outgoingEdges = new ArrayList<>();

        for (Edge<Activity> edge : graph.outgoingEdges(activity)) { // O(n), where n is the number of outgoing edges
            outgoingEdges.add(edge); // O(1)
        }

        return outgoingEdges;
    }


    /**
     * Gets the start vertices of the graph.
     * <p>
     * Start vertices are those with no incoming edges.
     * Complexity: O(n), where v is the number of vertices or the number of edges.
     * </p>
     *
     * @return a list of activities that are start vertices.
     */
    public List<Activity> getStartVertices() {
        List<Activity> startVertices = new ArrayList<>();
        Map<Activity, Integer> inDegrees = getInDegrees(); // O(n)

        for (Map.Entry<Activity, Integer> entry : inDegrees.entrySet()) { // O(n)
            if (entry.getValue() == 0) { // O(1)
                startVertices.add(entry.getKey());
            }
        }

        return startVertices;
    }

    /**
     * Gets the end vertices of the graph.
     * <p>
     * End vertices are those with no outgoing edges.
     * Complexity: O(n), where n is the number of vertices or Edges in the graph.
     * </p>
     *
     * @return a list of activities that are end vertices.
     */
    public List<Activity> getEndVertices() {
        List<Activity> endVertices = new ArrayList<>();

        for (Activity activity : graph.vertices()) { // O(n)
            if (graph.outgoingEdges(activity).isEmpty()) { // O(n)
                endVertices.add(activity); // O(1)
            }
        }

        return endVertices;
    }


public List<Activity> getStartActivities() {
    List<Activity> startVertices = new ArrayList<>();
    for (Activity activity : graph.vertices()) {
        if (graph.incomingEdges(activity).isEmpty()) {
            startVertices.add(activity);
        }
    }
    return startVertices;
}

public List<Activity> getEndActivities() {
    List<Activity> endVertices = new ArrayList<>();
    for (Activity activity : graph.vertices()) {
        if (graph.outgoingEdges(activity).isEmpty()) {
            endVertices.add(activity);
        }
    }
    return endVertices;
}

public Activity[] getSuccessors(Activity activity) {
    List<Activity> successors = new ArrayList<>();
    for (Edge<Activity> edge : graph.outgoingEdges(activity)) {
        successors.add(edge.getVDest());
    }
    return successors.toArray(new Activity[0]);
}


}
