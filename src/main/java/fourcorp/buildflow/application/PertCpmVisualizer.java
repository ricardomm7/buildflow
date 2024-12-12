package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.Collection;

public class PertCpmVisualizer {
    private final ActivitiesGraph graph;

    public PertCpmVisualizer() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
    }

    public void printGraph() {
        System.out.println();
        System.out.println("PERT/CPM GRAPH STRUCTURE");
        String activityFormat = "| Activity %-4s | %-30s | Duration: %-5d %n";
        String dependencyFormat = "|     • Activity %-4s | %-30s %n";
        String separator = "+------------------+--------------------------------+-------------------+";

        ActivityTopologicalSort topologicalSort = new ActivityTopologicalSort();
        // Iterate over all vertices (activities)
        for (Activity activity : topologicalSort.performTopologicalSort()) {
            // Print the activity details
            System.out.println(separator);
            System.out.format(activityFormat, activity.getId(), truncate(activity.getName(), 40), activity.getDuration());

            // Get the dependencies (adjacent activities)
            Collection<Activity> adjacencies = graph.getGraph().adjVertices(activity);

            if (!adjacencies.isEmpty()) {
                System.out.println("|   Dependencies:");
                // Print each dependency
                for (Activity dependency : adjacencies) {
                    System.out.format(dependencyFormat, dependency.getId(), truncate(dependency.getName(), 40));
                }
            } else {
                System.out.println("|   No dependencies.");
            }

            System.out.println(separator);
        }
        System.out.println();
    }

    private String truncate(String text, int maxLength) {
        if (text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + "...";
        }
        return text;
    }

}