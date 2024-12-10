package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.LinkedList;

public class PERT_CPM {
    private final ActivitiesGraph graph;

    public PERT_CPM() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
    }

    public void printGraph() {
        System.out.println();
        System.out.println("PERT/CPM GRAPH STRUCTURE");
        String activityFormat = "| Activity %-4s | %-30s | Duration: %-5d %n";
        String dependencyFormat = "|     • Activity %-4s | %-30s %n";
        String separator = "+------------------+--------------------------------+-------------------+";

        for (LinkedList<Activity> list : graph.getGraph().getAdjacencyList()) {
            Activity activity = list.getFirst();
            System.out.println(separator);
            System.out.format(activityFormat, activity.getId(), truncate(activity.getName(), 40), activity.getDuration());

            if (list.size() > 1) {
                System.out.println("|   Dependencies:");
                for (int i = 1; i < list.size(); i++) {
                    Activity dependency = list.get(i);
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