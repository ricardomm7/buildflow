package fourcorp.buildflow.domain;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Graph representation using an adjacency list for activities.
 */
public class Graph {
    private final ArrayList<LinkedList<Activity>> adjacencyList;

    public Graph() {
        this.adjacencyList = new ArrayList<>();
    }

    /**
     * Adds a new activity as a node in the graph.
     *
     * @param activity the activity to add.
     */
    public void addNode(Activity activity) {
        LinkedList<Activity> currentList = new LinkedList<>();
        currentList.add(activity);
        adjacencyList.add(currentList);
    }

    /**
     * Adds a directed edge between two activities in the graph.
     *
     * @param src the index of the source activity .
     * @param dst the index of the destination activity .
     */

    public void addEdge(int src, int dst) {
        LinkedList<Activity> currentList = adjacencyList.get(src);
        Activity dstNode = adjacencyList.get(dst).get(0);
        currentList.add(dstNode);
    }

    /**
     * Checks if an edge exists between two activities.
     *
     * @param src the index of the source activity.
     * @param dst the index of the destination activity.
     * @return true if the edge exists,false otherwise.
     */

    public boolean checkEdge(int src, int dst) {
        LinkedList<Activity> currentList = adjacencyList.get(src);
        Activity dstNode = adjacencyList.get(dst).get(0);
        return currentList.contains(dstNode);
    }


    /**
     * Retrieves the adjacency list.
     *
     * @return the adjacency list.
     */
    public ArrayList<LinkedList<Activity>> getAdjacencyList() {
        return adjacencyList;
    }
}
