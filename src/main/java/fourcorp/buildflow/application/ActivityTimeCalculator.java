package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class for calculating the earliest and latest start/finish times for activities
 * in a PERT/CPM graph and identifying the critical path.
 */
public class ActivityTimeCalculator {
    private ActivitiesGraph graph;
    private int projectDuration;

    public ActivityTimeCalculator() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
        this.projectDuration = 0;
    }

    public void setGraph(ActivitiesGraph graph) {
        this.graph = graph;
        this.projectDuration = 0;
    }

    public int getProjectDuration() {
        return projectDuration;
    }

    /**
     * Calculates the earliest start/finish (ES/EF) and latest start/finish (LS/LF) times for all activities.
     * Also calculates the slack and determines the critical path.
     */
    public void calculateTimes() {
        if (graph == null) {
            throw new IllegalStateException("Graph not initialized.");
        }

        ActivityTopologicalSort topologicalSort = new ActivityTopologicalSort();
        List<Activity> topOrder = topologicalSort.performTopologicalSort();
        calculateEarliestTimes(topOrder);
        calculateLatestTimes(topOrder);
    }

    /**
     * Performs the forward pass to calculate ES and EF.
     */
    private void calculateEarliestTimes(List<Activity> topOrder) {
        for (Activity activity : topOrder) {
            // Calcular ES como o maior EF das dependências
            int earlyStart = activity.getDependencies().stream()
                    .mapToInt(depId -> findActivityById(depId).getEarlyFinish())
                    .max()
                    .orElse(0);

            activity.setEarlyStart(earlyStart);
            activity.setEarlyFinish(earlyStart + activity.getDuration());
        }

        // Atualizar a duração total do projeto
        projectDuration = topOrder.stream()
                .mapToInt(Activity::getEarlyFinish)
                .max()
                .orElse(0);
    }


    /**
     * Performs the backward pass to calculate LS and LF.
     */
    private void calculateLatestTimes(List<Activity> topOrder) {
        Collections.reverse(topOrder);

        for (Activity activity : topOrder) {
            int lateFinish;
            if (graph.getNeighbors(activity).length == 0) {
                lateFinish = projectDuration; // Last activities
            } else {
                lateFinish = Arrays.stream(graph.getNeighbors(activity))
                        .mapToInt(Activity::getLateStart)
                        .min()
                        .orElse(projectDuration);

            }

            activity.setLateFinish(lateFinish);
            activity.setLateStart(lateFinish - activity.getDuration());
        }
    }

    /**
     * Finds an activity by its ID.
     */
    private Activity findActivityById(String id) {
    Activity found = graph.getGraph().vertex(activity -> activity.getId().equals(id));
    if (found == null) {
        System.err.printf("Activity Id %s not found.%n", id);
    }
    return found;
}



    /**
     * Displays the project schedule analysis.
     */
    public void displayTimes() {
        System.out.println("\n╔══════════════════════════════════════════════════════");
        System.out.println("║ PROJECT SCHEDULE ANALYSIS");
        System.out.println("╠══════════════════════════════════════════════════════");
        System.out.printf("║ Total Project Duration: %d time units%n%n", projectDuration);

        System.out.println("║ ACTIVITY SCHEDULE:");
        System.out.println("╠══════════════════════════════════════════════════════");
        System.out.printf("║ %-6s %-25s %-4s %-4s %-4s %-4s %-6s %-10s%n",
                "ID", "Name", "ES", "EF", "LS", "LF", "Slack", "Critical?");
        System.out.println("╟──────────────────────────────────────────────────────");

        ActivityTopologicalSort topologicalSort = new ActivityTopologicalSort();
        for (Activity activity : topologicalSort.performTopologicalSort()) {
            int slack = activity.getLateStart() - activity.getEarlyStart();
            boolean isCritical = slack == 0;

            System.out.printf("║ %-6s %-25s %-4d %-4d %-4d %-4d %-6d %-10s%n",
                    activity.getId(),
                    activity.getName().length() > 25 ? activity.getName().substring(0, 22) + "..." : activity.getName(),
                    activity.getEarlyStart(),
                    activity.getEarlyFinish(),
                    activity.getLateStart(),
                    activity.getLateFinish(),
                    slack,
                    isCritical ? "Yes" : "No");
        }
    }
}
