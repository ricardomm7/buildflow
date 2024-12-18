package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CriticalPathIdentifierGraph {
    private ActivitiesGraph graph;
    private ActivityTimeCalculator timeCalculator;
    private ActivityTopologicalSort sort;

    private List<Activity> criticalPath;
    private int totalProjectDuration;

    public CriticalPathIdentifierGraph() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
        timeCalculator = new ActivityTimeCalculator();
        sort = new ActivityTopologicalSort();
        criticalPath = new ArrayList<>();
        totalProjectDuration = 0;
    }

    /**
     * Calculate the critical path(s) and total project duration.
     */
    public void identifyCriticalPath() {
        calculateCriticalPath(); // O(n^2)

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

    private void calculateCriticalPath() {
        // Calculate earliest and latest start/finish times
        timeCalculator.calculateTimes(); // O(n^2)

        for (Activity activity : sort.performTopologicalSort()) { // O(n) * O(n)
            int slack = activity.getLateStart() - activity.getEarlyStart();

            if (slack == 0) {
                criticalPath.add(activity);
            }
            totalProjectDuration = Math.max(totalProjectDuration, activity.getEarlyFinish());
        }

        // Sort critical path activities by early start time
        criticalPath.sort(Comparator.comparingInt(Activity::getEarlyStart)); // O(n log n)
    }

    // Utility method to truncate long names
    private String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

    public void setGraph(ActivitiesGraph graph) {
        this.graph = graph;
        this.timeCalculator.setGraph(graph);
        this.sort.setGraph(graph);
        this.criticalPath.clear();
        this.totalProjectDuration = 0;
    }

    public List<Activity> getCriticalPath() {
        return criticalPath;
    }

    public int getTotalProjectDuration() {
        return totalProjectDuration;
    }
}
