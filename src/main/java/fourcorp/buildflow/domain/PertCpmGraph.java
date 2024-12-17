package fourcorp.buildflow.domain;

import fourcorp.buildflow.external.Edge;
import fourcorp.buildflow.external.Graph;

import java.util.*;
import java.util.function.Predicate;

public class PertCpmGraph implements Graph<Activity, Edge<Activity>> {
    private final Map<Activity, LinkedList<Edge<Activity>>> adjacencyList;

    private int numEdges;

    public PertCpmGraph() {
        this.adjacencyList = new HashMap<>();
        this.numEdges = 0;
    }

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int numVertices() {
        return adjacencyList.size();
    }

    @Override
    public ArrayList<Activity> vertices() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    @Override
    public boolean validVertex(Activity vert) {
        return adjacencyList.containsKey(vert);
    }

    @Override
    public int key(Activity vert) {
        ArrayList<Activity> vertList = new ArrayList<>(adjacencyList.keySet());
        return vertList.indexOf(vert);
    }

    @Override
    public Activity vertex(int key) {
        ArrayList<Activity> vertList = new ArrayList<>(adjacencyList.keySet());
        if (key < 0 || key >= vertList.size()) return null;
        return vertList.get(key);
    }

    @Override
    public Activity vertex(Predicate<Activity> p) {
        return adjacencyList.keySet().stream()
                .filter(p)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<Activity> adjVertices(Activity vert) {
        if (!adjacencyList.containsKey(vert)) {
            return new ArrayList<>();
        }
        return adjacencyList.get(vert).stream()
                .map(Edge::getVDest)
                .toList();
    }

    @Override
    public int numEdges() {
        return numEdges;
    }

    @Override
    public Collection<Edge<Activity>> edges() {
        List<Edge<Activity>> allEdges = new ArrayList<>();
        for (LinkedList<Edge<Activity>> vertexEdges : adjacencyList.values()) {
            allEdges.addAll(vertexEdges);
        }
        return allEdges;
    }

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

    @Override
    public Edge<Activity> edge(int vOrigKey, int vDestKey) {
        Activity vOrig = vertex(vOrigKey);
        Activity vDest = vertex(vDestKey);

        if (vOrig == null || vDest == null) {
            return null;
        }
        return edge(vOrig, vDest);
    }

    @Override
    public int outDegree(Activity vert) {
        return adjacencyList.containsKey(vert) ? adjacencyList.get(vert).size() : 0;
    }

    @Override
    public int inDegree(Activity vert) {
        return (int) adjacencyList.values().stream()
                .flatMap(List::stream)
                .filter(edge -> edge.getVDest().equals(vert))
                .count();
    }

    @Override
    public Collection<Edge<Activity>> outgoingEdges(Activity vert) {
        return adjacencyList.containsKey(vert) ?
                adjacencyList.get(vert) :
                new ArrayList<>();
    }

    @Override
    public Collection<Edge<Activity>> incomingEdges(Activity vert) {
        return adjacencyList.values().stream()
                .flatMap(List::stream)
                .filter(edge -> edge.getVDest().equals(vert))
                .toList();
    }

    @Override
    public boolean addVertex(Activity vert) {
        if (adjacencyList.containsKey(vert)) {
            return false;
        }
        adjacencyList.put(vert, new LinkedList<>());
        return true;
    }

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


    public Iterable<? extends Activity> getActivities() {
        return adjacencyList.keySet();
    }

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

    public void addActivity(Activity virtualStart) {
        addVertex(virtualStart);
    }

    public void addDependency(Activity virtualStart, Activity start) {
        addEdge(virtualStart, start);
    }
}