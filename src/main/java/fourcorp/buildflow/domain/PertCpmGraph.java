package fourcorp.buildflow.domain;

import fourcorp.buildflow.external.Edge;
import fourcorp.buildflow.external.Graph;

import java.util.*;
import java.util.function.Predicate;

public class PertCpmGraph implements Graph<Activity, Edge<Activity>> {
    private final Map<Activity, LinkedList<Edge<Activity>>> adjacencyList;

    private int numEdges;

    /**
     * Constructs an empty directed graph.
     */
    public PertCpmGraph() {
        this.adjacencyList = new HashMap<>();
        this.numEdges = 0;
    }

    /**
     * Indicates that this is a directed graph.
     *
     * @return {@code true}, as the graph is directed.
     */
    @Override
    public boolean isDirected() {
        return true;
    }

    /**
     * Returns the number of vertices in the graph.
     *
     * @return the number of vertices.
     * Complexity: O(1).
     */
    @Override
    public int numVertices() {
        return adjacencyList.size();
    }

    /**
     * Returns a list of all vertices in the graph.
     *
     * @return an {@code ArrayList} of all vertices.
     * Complexity: O(n).
     */
    @Override
    public ArrayList<Activity> vertices() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    /**
     * Checks if a given vertex is valid (exists in the graph).
     *
     * @param vert the vertex to check.
     * @return {@code true} if the vertex exists, {@code false} otherwise.
     */
    @Override
    public boolean validVertex(Activity vert) {
        return adjacencyList.containsKey(vert);
    }

    /**
     * Returns the key (index) of a given vertex.
     *
     * @param vert the vertex to find.
     * @return the index of the vertex, or -1 if not found.
     * Complexity: O(n).
     */
    @Override
    public int key(Activity vert) {
        ArrayList<Activity> vertList = new ArrayList<>(adjacencyList.keySet());
        return vertList.indexOf(vert);
    }

    /**
     * Retrieves a vertex by its key (index).
     *
     * @param key the key of the vertex.
     * @return the vertex corresponding to the key, or {@code null} if invalid.
     * Complexity: O(n).
     */
    @Override
    public Activity vertex(int key) {
        ArrayList<Activity> vertList = new ArrayList<>(adjacencyList.keySet());
        if (key < 0 || key >= vertList.size()) return null;
        return vertList.get(key);
    }

    /**
     * Finds a vertex that matches a given predicate.
     *
     * @param p the predicate to test vertices.
     * @return the first matching vertex, or {@code null} if none found.
     * Complexity: O(n).
     */
    @Override
    public Activity vertex(Predicate<Activity> p) {
        return adjacencyList.keySet().stream()
                .filter(p)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the vertices adjacent to a given vertex.
     *
     * @param vert the vertex to query.
     * @return a collection of adjacent vertices.
     * Complexity: O(n).
     */
    @Override
    public Collection<Activity> adjVertices(Activity vert) {
        if (!adjacencyList.containsKey(vert)) {
            return new ArrayList<>();
        }
        return adjacencyList.get(vert).stream()
                .map(Edge::getVDest)
                .toList();
    }

    /**
     * Returns the number of edges in the graph.
     *
     * @return the number of edges.
     */
    @Override
    public int numEdges() {
        return numEdges;
    }

    /**
     * Returns all edges in the graph.
     *
     * @return a collection of all edges.
     * Complexity: O(n).
     */
    @Override
    public Collection<Edge<Activity>> edges() {
        List<Edge<Activity>> allEdges = new ArrayList<>();
        for (LinkedList<Edge<Activity>> vertexEdges : adjacencyList.values()) {
            allEdges.addAll(vertexEdges);
        }
        return allEdges;
    }

    /**
     * Finds an edge between two vertices.
     *
     * @param vOrig the source vertex.
     * @param vDest the destination vertex.
     * @return the edge if found, or {@code null} if not found.
     * Complexity: O(n).
     */
    @Override
    public Edge<Activity> edge(Activity vOrig, Activity vDest) {
        if (!adjacencyList.containsKey(vOrig)) {
            return null;
        }
        return adjacencyList.get(vOrig).stream()
                .filter(edge -> edge.getVDest().equals(vDest))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds an edge using the keys (indices) of the source and destination vertices.
     *
     * @param vOrigKey the key of the source vertex.
     * @param vDestKey the key of the destination vertex.
     * @return the edge if found, or {@code null} if not found.
     * Complexity: O(n).
     */
    @Override
    public Edge<Activity> edge(int vOrigKey, int vDestKey) {
        Activity vOrig = vertex(vOrigKey);
        Activity vDest = vertex(vDestKey);

        if (vOrig == null || vDest == null) {
            return null;
        }
        return edge(vOrig, vDest);
    }

    /**
     * Returns the out-degree (number of outgoing edges) of a vertex.
     *
     * @param vert the vertex to query.
     * @return the out-degree of the vertex.
     */
    @Override
    public int outDegree(Activity vert) {
        return adjacencyList.containsKey(vert) ? adjacencyList.get(vert).size() : 0;
    }

    /**
     * Returns the in-degree (number of incoming edges) of a vertex.
     *
     * @param vert the vertex to query.
     * @return the in-degree of the vertex.
     * Complexity: O(n).
     */
    @Override
    public int inDegree(Activity vert) {
        return (int) adjacencyList.values().stream()
                .flatMap(List::stream)
                .filter(edge -> edge.getVDest().equals(vert))
                .count();
    }

    /**
     * Returns the outgoing edges of a vertex.
     *
     * @param vert the vertex to query.
     * @return a collection of outgoing edges.
     */
    @Override
    public Collection<Edge<Activity>> outgoingEdges(Activity vert) {
        return adjacencyList.containsKey(vert) ?
                adjacencyList.get(vert) :
                new ArrayList<>();
    }

    /**
     * Returns the incoming edges of a vertex.
     *
     * @param vert the vertex to query.
     * @return a collection of incoming edges.
     * Complexity: O(n).
     */
    @Override
    public Collection<Edge<Activity>> incomingEdges(Activity vert) {
        return adjacencyList.values().stream()
                .flatMap(List::stream)
                .filter(edge -> edge.getVDest().equals(vert))
                .toList();
    }

    /**
     * Adds a vertex to the graph.
     *
     * @param vert the vertex to add.
     * @return {@code true} if the vertex was added, {@code false} if it already exists.
     */
    @Override
    public boolean addVertex(Activity vert) {
        if (adjacencyList.containsKey(vert)) {
            return false;
        }
        adjacencyList.put(vert, new LinkedList<>());
        return true;
    }

    /**
     * Adds a directed edge between two vertices. If the vertices do not exist, they are created.
     *
     * @param vOrig the source vertex.
     * @param vDest the destination vertex.
     * @return {@code true} if the edge was added, {@code false} if the edge already exists.
     * Complexity: O(n).
     */
    @Override
    public boolean addEdge(Activity vOrig, Activity vDest) {
        // Ensure both vertices exist
        if (!adjacencyList.containsKey(vOrig)) {
            addVertex(vOrig);
        }
        if (!adjacencyList.containsKey(vDest)) {
            addVertex(vDest);
        }

        // Check if edge already exists
        if (edge(vOrig, vDest) != null) {
            return false;
        }

        // Create and add the edge
        Edge<Activity> newEdge = new Edge<>(vOrig, vDest);
        adjacencyList.get(vOrig).add(newEdge);
        numEdges++;
        return true;
    }

    /**
     * Removes a vertex and all its associated edges from the graph.
     *
     * @param vert the vertex to remove.
     * @return {@code true} if the vertex was removed, {@code false} if it did not exist.
     * Complexity: O(n).
     */
    @Override
    public boolean removeVertex(Activity vert) {
        if (!adjacencyList.containsKey(vert)) {
            return false;
        }

        // Remove edges originating from this vertex
        numEdges -= adjacencyList.get(vert).size();

        // Remove the vertex
        adjacencyList.remove(vert);

        // Remove edges pointing to this vertex from other vertices
        adjacencyList.values().forEach(
                edges -> edges.removeIf(edge -> edge.getVDest().equals(vert))
        );

        return true;
    }

    /**
     * Removes a directed edge between two vertices.
     *
     * @param vOrig the source vertex.
     * @param vDest the destination vertex.
     * @return {@code true} if the edge was removed, {@code false} if it did not exist.
     * Complexity: O(n).
     */
    @Override
    public boolean removeEdge(Activity vOrig, Activity vDest) {
        if (!adjacencyList.containsKey(vOrig)) {
            return false;
        }

        LinkedList<Edge<Activity>> origEdges = adjacencyList.get(vOrig);
        boolean removed = origEdges.removeIf(edge -> edge.getVDest().equals(vDest));

        if (removed) {
            numEdges--;
        }

        return removed;
    }

    /**
     * Creates a deep copy of the graph.
     *
     * @return a cloned graph.
     * Complexity: O(n).
     */
    @Override
    public Graph<Activity, Edge<Activity>> clone() {
        PertCpmGraph clonedGraph = new PertCpmGraph();

        // Clone vertices
        for (Activity activity : adjacencyList.keySet()) {
            clonedGraph.addVertex(activity);
        }

        // Clone edges
        for (Map.Entry<Activity, LinkedList<Edge<Activity>>> entry : adjacencyList.entrySet()) {
            Activity vOrig = entry.getKey();
            for (Edge<Activity> edge : entry.getValue()) {
                clonedGraph.addEdge(vOrig, edge.getVDest());
            }
        }

        return clonedGraph;
    }

    /**
     * Retrieves all activities in the graph.
     *
     * @return an iterable of activities.
     */
    public Iterable<? extends Activity> getActivities() {
        return adjacencyList.keySet();
    }

    /**
     * Retrieves the incoming edges for a given activity.
     *
     * @param activity the activity to query.
     * @return a map of source activities and their corresponding edges.
     * Complexity: O(n).
     */
    public Map<Activity, Edge<Activity>> getIncomingEdges(Activity activity) {
        Map<Activity, Edge<Activity>> incomingEdges = new HashMap<>();

        for (Map.Entry<Activity, LinkedList<Edge<Activity>>> entry : adjacencyList.entrySet()) {
            Activity source = entry.getKey();
            for (Edge<Activity> edge : entry.getValue()) {
                if (edge.getVDest().equals(activity)) {
                    incomingEdges.put(source, edge);
                }
            }
        }

        return incomingEdges;
    }

    /**
     * Retrieves the outgoing edges for a given activity.
     *
     * @param activity the activity to query.
     * @return a map of destination activities and their corresponding edges.
     * Complexity: O(n).
     */
    public Map<Object, Object> getOutgoingEdges(Activity activity) {
        Map<Object, Object> outgoingEdges = new HashMap<>();

        for (Map.Entry<Activity, LinkedList<Edge<Activity>>> entry : adjacencyList.entrySet()) {
            Activity source = entry.getKey();
            for (Edge<Activity> edge : entry.getValue()) {
                if (edge.getVOrig().equals(activity)) {
                    outgoingEdges.put(source, edge);
                }
            }
        }

        return outgoingEdges;
    }

    /**
     * Adds a new activity to the graph.
     *
     * @param virtualStart the activity to add.
     */
    public void addActivity(Activity virtualStart) {
        addVertex(virtualStart);
    }

    /**
     * Adds a dependency between two activities.
     *
     * @param virtualStart the source activity.
     * @param start        the destination activity.
     *                     Complexity: O(n).
     */
    public void addDependency(Activity virtualStart, Activity start) {
        addEdge(virtualStart, start);
    }

    /**
     * Clears all vertices and edges from the graph.
     */
    public void clear() {
        adjacencyList.clear();
    }
}