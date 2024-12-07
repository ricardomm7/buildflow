package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class PERT_CPM {
    private final ActivitiesGraph graph;

    public PERT_CPM(ActivitiesGraph graph) {
        this.graph = graph;
    }


    /**
     * Prints the PERT/CPM graph to the console in a readable format.
     */
    public void printGraph() {
        System.out.println("\n╔══════════════════════════════════════════════════════");
        System.out.println("║ PERT/CPM GRAPH STRUCTURE");
        System.out.println("╠══════════════════════════════════════════════════════");

        for (LinkedList<Activity> list : graph.getGraph().getAdjacencyList()) {
            Activity activity = list.getFirst();
            System.out.printf("║ Activity %s: %s (Duration: %d)%n",
                    activity.getId(), activity.getName(), activity.getDuration());

            if (list.size() > 1) {
                System.out.println("║   Dependencies:");
                for (int i = 1; i < list.size(); i++) {
                    Activity dependency = list.get(i);
                    System.out.printf("║     • Activity %s: %s%n",
                            dependency.getId(), dependency.getName());
                }
            } else {
                System.out.println("║   No dependencies.");
            }
            System.out.println("║");
        }

        System.out.println("╚══════════════════════════════════════════════════════\n");
    }

    public static void main(String[] args) {
        ActivitiesGraph activitiesGraph = Repositories.getInstance().getActivitiesGraph();
        try {
            Reader.loadActivities("textFiles/activities.csv");
            PERT_CPM pertCpm = new PERT_CPM(activitiesGraph);

            // Print the graph
            pertCpm.printGraph();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}