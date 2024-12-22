package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class responsible for calculating the earliest start (ES), earliest finish (EF),
 * latest start (LS), and latest finish (LF) times for activities in a PERT/CPM graph.
 * Also identifies the critical path and computes slack values for activities.
 */
public class ActivityTimeCalculator {
    private ActivitiesGraph graph;
    private final ActivityTopologicalSort topologicalSort;
    private int projectDuration;

    /**
     * Constructor: Initializes the time calculator with the default graph.
     */
    public ActivityTimeCalculator() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
        topologicalSort = new ActivityTopologicalSort();
        this.projectDuration = 0;
    }

    /**
     * Sets the graph to be used for calculations and resets project duration.
     *
     * @param graph The project graph.
     */
    public void setGraph(ActivitiesGraph graph) {
        this.graph = graph;
        this.topologicalSort.setGraph(graph);
        this.projectDuration = 0;
    }

    public int getProjectDuration() {
        return projectDuration;
    }

    /**
     * Calculates ES, EF, LS, and LF for all activities in the graph.
     * Also determines the project duration and computes slack values.
     * Complexity: O(n^2), where n is the number of activities.
     */
    public void calculateTimes() {
        if (graph == null) {
            throw new IllegalStateException("Graph not initialized."); // O(1)
        }
        List<Activity> topOrder = topologicalSort.performTopologicalSort(); // O(n^2)
        calculateEarliestTimes(topOrder); // O(n^2)
        calculateLatestTimes(topOrder); // O(n^2)
    }

    /**
     * Performs the forward pass to calculate ES and EF.
     * Formulae:
     * - ES(activity) = max(EF(dependency))
     * - EF(activity) = ES(activity) + duration(activity)
     * Complexity: O(n^2), where n is the number of activities.
     */
    private void calculateEarliestTimes(List<Activity> topOrder) {
        for (Activity activity : topOrder) { // O(n)
            int earlyStart = activity.getDependencies().stream() // O(n) * O(n) = O(n^2)
                    .mapToInt(depId -> findActivityById(depId).getEarlyFinish())
                    .max()
                    .orElse(0);

            activity.setEarlyStart(earlyStart); // O(1) * O(n) = O(n)
            activity.setEarlyFinish(earlyStart + activity.getDuration()); // O(1) * O(n) = O(n)
        }

        projectDuration = topOrder.stream() // O(n)
                .mapToInt(Activity::getEarlyFinish)
                .max()
                .orElse(0);
    }

    /**
     * Performs the backward pass to calculate LS and LF.
     * Formulae:
     * - LF(activity) = min(LS(successor))
     * - LS(activity) = LF(activity) - duration(activity)
     * Complexity: O(n^2), where n is the number of activities.
     */
    private void calculateLatestTimes(List<Activity> topOrder) {
        Collections.reverse(topOrder); // O(n)

        for (Activity activity : topOrder) { // O(n)
            int lateFinish;
            if (graph.getNeighbors(activity).length == 0) { // O(1) * O(n) = O(n)
                lateFinish = projectDuration; // O(1) * O(n) = O(n)
            } else {
                lateFinish = Arrays.stream(graph.getNeighbors(activity)) // O(n) * O(n) = O(n^2)
                        .mapToInt(Activity::getLateStart)
                        .min()
                        .orElse(projectDuration);
            }

            activity.setLateFinish(lateFinish); // O(1) * O(n) = O(n)
            activity.setLateStart(lateFinish - activity.getDuration()); // O(1) * O(n) = O(n)
        }
    }

    /**
     * Finds an activity by its ID.
     * Complexity: O(n) per activity (using graph's vertex lookup).
     *
     * @param id The ID of the activity.
     * @return The activity object, or null if not found.
     */
    private Activity findActivityById(String id) {
        Activity found = graph.getGraph().vertex(activity -> activity.getId().equals(id)); // O(n)
        if (found == null) {
            System.err.printf("Activity Id %s not found.%n", id); // O(1)
        }
        return found;
    }

    /**
     * Displays the project schedule, including ES, EF, LS, LF, slack, and critical path.
     * Complexity: O(n).
     */
    public void displayTimes() {
        System.out.println();
        System.out.println("PROJECT SCHEDULE ANALYSIS");
        String headerFormat = "| %-6s | %-25s | %-4s | %-4s | %-4s | %-4s | %-6s | %-10s |%n";
        String separator = "+--------+---------------------------+------+------+------+------+--------+------------+";

        System.out.println(separator);
        System.out.printf(headerFormat, "ID", "Name", "ES", "EF", "LS", "LF", "Slack", "Critical?");
        System.out.println(separator);

        for (Activity activity : topologicalSort.performTopologicalSort()) { // O(n)
            int slack = activity.getLateStart() - activity.getEarlyStart(); // O(1) * O(n) = O(n)
            boolean isCritical = slack == 0;

            System.out.printf(headerFormat,
                    activity.getId(),
                    truncate(activity.getName(), 25),
                    activity.getEarlyStart(),
                    activity.getEarlyFinish(),
                    activity.getLateStart(),
                    activity.getLateFinish(),
                    slack,
                    isCritical ? "Yes" : "No"); // O(1) * O(n) = O(n)
        }

        System.out.println(separator);
        System.out.printf("Total Project Duration: %d time units%n", projectDuration); // O(1)
        System.out.println();
    }

    /**
     * Truncates a given string to the specified maximum length and appends "..." if truncation occurs.
     *
     * @param name      The input string to truncate.
     * @param maxLength The maximum allowed length of the string.
     * @return The truncated string, if necessary; otherwise, the original string.
     */
    private String truncate(String name, int maxLength) {
        return name.length() > maxLength ? name.substring(0, maxLength - 3) + "..." : name;
    }

}
