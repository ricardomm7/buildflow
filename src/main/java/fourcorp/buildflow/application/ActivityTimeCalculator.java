package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.*;

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

        List<Activity> topOrder = performTopologicalSort();
        calculateEarliestTimes(topOrder);
        calculateLatestTimes(topOrder);
    }

    /**
     * Performs the forward pass to calculate ES and EF.
     */
    private void calculateEarliestTimes(List<Activity> topOrder) {
        for (Activity activity : topOrder) {
            int earlyStart = activity.getDependencies().stream()
                    .mapToInt(depId -> findActivityById(depId).getEarlyFinish())
                    .max()
                    .orElse(0);

            activity.setEarlyStart(earlyStart);
            activity.setEarlyFinish(earlyStart + activity.getDuration());
        }

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
     * Performs a topological sort on the graph.
     *
     * @return a list of activities in topological order
     */
    private List<Activity> performTopologicalSort() {
        List<Activity> sorted = new ArrayList<>();
        Map<Activity, Integer> inDegree = graph.getInDegrees();
        Queue<Activity> queue = new LinkedList<>();

        inDegree.forEach((activity, degree) -> {
            if (degree == 0) {
                queue.add(activity);
            }
        });

        while (!queue.isEmpty()) {
            Activity current = queue.poll();
            sorted.add(current);

            for (Activity neighbor : graph.getNeighbors(current)) {
                int updatedDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, updatedDegree);
                if (updatedDegree == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (sorted.size() != graph.getGraph().numVertices()) {
            throw new IllegalStateException("Graph contains a cycle, cannot perform topological sort.");
        }

        return sorted;
    }

    /**
     * Finds an activity by its ID.
     */
    private Activity findActivityById(String id) {
        return graph.getGraph().vertex(activity -> activity.getId().equals(id));
    }

    /**
     * Identifies the critical path by finding activities with zero slack.
     *
     * @return a list of activities on the critical path
     */
    public List<Activity> findCriticalPath() {
        List<Activity> criticalPath = new ArrayList<>();
        for (Activity activity : graph.getGraph().vertices()) {
            int slack = activity.getLateStart() - activity.getEarlyStart();
            if (slack == 0) {
                criticalPath.add(activity);
            }
        }
        return criticalPath;
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

        for (Activity activity : graph.getGraph().vertices()) {
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

        System.out.println("╚══════════════════════════════════════════════════════");
        System.out.println("\nCritical Path:");
        findCriticalPath().forEach(activity ->
                System.out.printf("• %s: %s (Duration: %d)%n",
                        activity.getId(),
                        activity.getName(),
                        activity.getDuration()));
    }
}
