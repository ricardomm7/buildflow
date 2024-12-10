package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CriticalPathIdentifierGraph {
    private final ActivitiesGraph graph;

    public CriticalPathIdentifierGraph(ActivitiesGraph graph) {
        this.graph = graph;
    }

    /**
     * Calculate the critical path(s) and total project duration.
     */
    public void identifyCriticalPath() {
        // Calculate earliest and latest start/finish times
        ActivityTimeCalculator timeCalculator = new ActivityTimeCalculator(graph);
        timeCalculator.calculateTimes();

        // Collect critical path activities
        List<Activity> criticalPath = new ArrayList<>();
        int totalProjectDuration = 0;

        for (var linkedList : graph.getGraph().getAdjacencyList()) {
            Activity activity = linkedList.getFirst();
            int slack = activity.getLateStart() - activity.getEarlyStart();

            if (slack == 0) {
                criticalPath.add(activity);
            }
            totalProjectDuration = Math.max(totalProjectDuration, activity.getEarlyFinish());
        }

        // Sort critical path activities by early start time
        criticalPath.sort(Comparator.comparingInt(Activity::getEarlyStart));

        // Professional and clear output
        System.out.println("\n╔══════════════════════════════════════════════════════");
        System.out.println("║ CRITICAL PATH ANALYSIS");
        System.out.println("╠══════════════════════════════════════════════════════");
        System.out.printf("║ Total Project Duration: %d time units%n", totalProjectDuration);
        System.out.println("╠══════════════════════════════════════════════════════");
        System.out.println("║ ID   | Activity                      | Duration | Timing");
        System.out.println("╟──────┼───────────────────────────────┼──────────┼────────────────────");

        for (Activity activity : criticalPath) {
            System.out.printf("║ %-4s | %-50s | %-8d | ES:%d, EF:%d, LS:%d, LF:%d%n",
                    activity.getId(),
                    truncate(activity.getName(), 50),
                    activity.getDuration(),
                    activity.getEarlyStart(),
                    activity.getEarlyFinish(),
                    activity.getLateStart(),
                    activity.getLateFinish()
            );
        }
        System.out.println("╚══════════════════════════════════════════════════════\n");
    }

    // Utility method to truncate long names
    private String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

    public static void main(String[] args) {
        try {
            ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
            Reader.loadActivities("textFiles/activities.csv");

            CriticalPathIdentifierGraph criticalPathIdentifier = new CriticalPathIdentifierGraph(graph);
            criticalPathIdentifier.identifyCriticalPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
