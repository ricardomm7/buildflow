package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simulates project delays, calculates the impact on project metrics,
 * and identifies critical paths before and after delays.
 */
public class ProjectDelaySimulator {
    private ActivitiesGraph graph;
    private ActivityTimeCalculator timeCalculator;

    // Project metrics
    private int originalProjectDuration;
    private int newProjectDuration;
    private List<Activity> originalCriticalPath;
    private List<Activity> newCriticalPath;

    // Map to store original durations
    private final Map<String, Integer> originalDurations = new HashMap<>();

    /**
     * Constructor: Initializes the simulator with a default activities graph and calculator.
     */
    public ProjectDelaySimulator() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
        this.timeCalculator = new ActivityTimeCalculator();
        timeCalculator.setGraph(graph);
    }

    /**
     * Simulates delays for specified activities and analyzes project impact.
     * Complexity: O(n^2), where n is the number of activities and e is the number of dependencies.
     *
     * @param delayMap Map of activity IDs to their delay durations.
     */
    public void simulateProjectDelays(Map<String, Integer> delayMap) {
        saveOriginalDurations(); // O(n)
        calculateOriginalMetrics(); // O(n^2)
        applyDelays(delayMap); // O(n)
        calculateNewMetrics(); // O(n^2)
        displayImpactAnalysis(delayMap); // O(n)
    }

    /**
     * Saves the original durations of all activities.
     * Complexity: O(n), where n is the number of activities.
     */
    private void saveOriginalDurations() {
        for (Activity activity : graph.getGraph().vertices()) { // O(n)
            originalDurations.put(activity.getId(), activity.getDuration()); // O(1) * O(n) = O(n)
        }
    }

    /**
     * Restores the original durations of all activities.
     * Complexity: O(n), where n is the number of activities.
     */
    private void restoreOriginalDurations() {
        for (Activity activity : graph.getGraph().vertices()) { // O(n)
            activity.setDuration(originalDurations.get(activity.getId())); // O(1) * O(n) = O(n)
        }
    }

    /**
     * Calculates the original project metrics before applying delays.
     * Complexity: O(n^2), where n is the number of activities.
     */
    private void calculateOriginalMetrics() {
        restoreOriginalDurations(); // O(n)
        timeCalculator.calculateTimes(); // O(n^2)
        originalProjectDuration = timeCalculator.getProjectDuration(); // O(1)
        originalCriticalPath = findCriticalPath(); // O(n^2)
    }

    /**
     * Applies delays to specified activities in the graph.
     * Complexity: O(n), where n is the number of activities in the delay map.
     *
     * @param delayMap Map of activity IDs to their delay durations.
     */
    private void applyDelays(Map<String, Integer> delayMap) {
        for (Map.Entry<String, Integer> entry : delayMap.entrySet()) { // O(n)
            String activityId = entry.getKey();
            int delayAmount = entry.getValue();

            if (delayAmount < 0) { // O(1) * O(n) = O(n)
                System.err.printf("Erro: A atividade %s possui um delay negativo (%d).%n", activityId, delayAmount); // O(1) * O(n) = O(n)
                continue;
            }

            Activity activity = graph.getGraph().vertex(a -> a.getId().equals(activityId)); // O(1) * O(n) = O(n)
            if (activity != null) {
                activity.setDuration(originalDurations.get(activityId) + delayAmount); // O(1) * O(n) = O(n)
            } else {
                System.err.printf("Erro: A atividade %s não foi encontrada no grafo.%n", activityId); // O(1) * O(n) = O(n)
            }
        }
    }

    /**
     * Calculates the project metrics after applying delays.
     * Complexity: O(n^2), where n is the number of activities and e is the number of dependencies.
     */
    private void calculateNewMetrics() {
        timeCalculator.calculateTimes(); // O(n^2)
        newProjectDuration = timeCalculator.getProjectDuration(); // O(1)
        newCriticalPath = findCriticalPath(); // O(n^2)
    }

    /**
     * Finds the critical path based on activities with zero slack.
     * Complexity: O(n^2), where n is the number of activities.
     *
     * @return List of activities in the critical path.
     */
    List<Activity> findCriticalPath() {
        CriticalPathIdentifierGraph calculator = new CriticalPathIdentifierGraph();
        calculator.setGraph(graph);
        calculator.identifyCriticalPath(); // O(n^2)

        return calculator.getCriticalPath();
    }

    /**
     * Displays the delay impact analysis, including durations and critical paths.
     * Complexity: O(n), where n is the number of activities.
     *
     * @param delayMap Map of delayed activities.
     */
    private void displayImpactAnalysis(Map<String, Integer> delayMap) {
        System.out.println();
        System.out.println("PROJECT DELAY IMPACT ANALYSIS");
        String delayFormat = "| %-12s | %-17s |%n";
        String separator = "+--------------+-------------------+";

        System.out.println(separator);
        System.out.printf(delayFormat, "Activity ID", "Delay (units)");
        System.out.println(separator);

        delayMap.forEach((id, delay) -> // O(n)
                System.out.printf(delayFormat, id, "+" + delay)
        );
        System.out.println(separator);

        System.out.println();
        System.out.printf("| %-25s | %-5d |%n", "Original Project Duration", originalProjectDuration); // O(1)
        System.out.printf("| %-25s | %-5d |%n", "New Project Duration", newProjectDuration); // O(1)
        System.out.printf("| %-25s | %-5d |%n", "Total Delay", newProjectDuration - originalProjectDuration); // O(1)
        System.out.println();

        System.out.println("ORIGINAL CRITICAL PATH:");
        printCriticalPath(originalCriticalPath); // O(n)

        System.out.println("NEW CRITICAL PATH:");
        printCriticalPath(newCriticalPath); // O(n)

        System.out.println();
    }

    /**
     * Prints the critical path in a structured format.
     * Complexity: O(n), where n is the number of activities in the critical path.
     *
     * @param criticalPath List of activities in the critical path.
     */
    private void printCriticalPath(List<Activity> criticalPath) {
        String format = "| %-6s | %-30s | %-8s | %-3s | %-3s | %-3s | %-3s | %-5s |%n";
        String separator = "+--------+--------------------------------+----------+-----+-----+-----+-----+-------+";

        System.out.println(separator);
        System.out.printf(format, "ID", "Name", "Duration", "ES", "EF", "LS", "LF", "Slack");
        System.out.println(separator);

        for (Activity activity : criticalPath) { // O(n)
            System.out.printf(format,
                    activity.getId(), // O(1) * O(n) = O(n)
                    truncate(activity.getName(), 30), // O(1) * O(n) = O(n)
                    activity.getDuration(), // O(1) * O(n) = O(n)
                    activity.getEarlyStart(), // O(1) * O(n) = O(n)
                    activity.getEarlyFinish(), // O(1) * O(n) = O(n)
                    activity.getLateStart(), // O(1) * O(n) = O(n)
                    activity.getLateFinish(), // O(1) * O(n) = O(n)
                    activity.getLateStart() - activity.getEarlyStart()); // O(1) * O(n) = O(n)
        }
        System.out.println(separator);
    }

    /**
     * Truncates text for better formatting in outputs.
     * Complexity: O(1).
     *
     * @param text      The text to truncate.
     * @param maxLength The maximum length allowed.
     * @return The truncated text.
     */
    private String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text; // O(1)
    }

    /**
     * Gets new project duration.
     *
     * @return the new project duration
     */
    public int getNewProjectDuration() {
        return newProjectDuration;
    }

    /**
     * Gets original critical path.
     *
     * @return the original critical path
     */
    public List<Activity> getOriginalCriticalPath() {
        return originalCriticalPath;
    }

    /**
     * Gets new critical path.
     *
     * @return the new critical path
     */
    public List<Activity> getNewCriticalPath() {
        return newCriticalPath;
    }

    /**
     * Gets original project duration.
     *
     * @return the original project duration
     */
    public int getOriginalProjectDuration() {
        return originalProjectDuration;
    }

    /**
     * Sets graph.
     *
     * @param graph the graph
     */
    public void setGraph(ActivitiesGraph graph) {
        this.graph = graph;
        this.timeCalculator.setGraph(graph);
    }

    /**
     * Find activity by id activity.
     *
     * @param a1 the ID of the activity to find
     * @return the activity
     */
    public Activity findActivityById(String a1) {
        for (Activity activity : graph.getGraph().vertices()) {
            if (activity.getId().equals(a1)) {
                return activity;
            }
        }
        return null;
    }
}

