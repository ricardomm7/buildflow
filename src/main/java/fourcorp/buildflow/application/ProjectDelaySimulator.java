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
    private List<List<Activity>> originalCriticalPath;
    private List<List<Activity>> newCriticalPath;

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
     * Removed redundant critical path prints.
     * Complexity: O(n^2), where n is the number of activities.
     *
     * @param delayMap Map of activity IDs to their delay durations.
     */
    public void simulateProjectDelays(Map<String, Integer> delayMap) {
        saveOriginalDurations();
        calculateOriginalMetrics();
        applyDelays(delayMap);
        calculateNewMetrics();
        displayImpactAnalysis(delayMap);  // Only this method will print paths
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
     * Applies delays (positive or negative) to specified activities in the graph.
     * Complexity: O(n), where n is the number of activities in the delay map.
     * Now supports negative delays (advancements) while ensuring activity durations stay positive.
     *
     * @param delayMap Map of activity IDs to their delay durations (positive or negative)
     */
    private void applyDelays(Map<String, Integer> delayMap) {
    for (Map.Entry<String, Integer> entry : delayMap.entrySet()) {
        String activityId = entry.getKey();
        int delayAmount = entry.getValue();

        Activity activity = graph.getGraph().vertex(a -> a.getId().equals(activityId));
        if (activity != null) {
            int originalDuration = originalDurations.getOrDefault(activityId, activity.getDuration());
            int newDuration = originalDuration + delayAmount;

            // Garantir que a duração não fique negativa
            if (newDuration < 1) {
                System.err.printf(
                        "Warning: Activity %s duration cannot be less than 0 unit. Setting to 0.%n", activityId);
                newDuration = 0;
            }

            activity.setDuration(newDuration);
            System.out.printf("Updated Activity %s: Original Duration=%d, New Duration=%d%n",
                    activityId, originalDuration, newDuration);
        } else {
            System.err.printf("Error: Activity %s not found in the graph.%n", activityId);
        }
    }
}


    /**
     * Displays the delay impact analysis, including durations and critical paths.
     * This is now the only method that prints critical path information.
     * Complexity: O(n), where n is the number of activities.
     *
     * @param delayMap Map of delayed/advanced activities
     */
    private void displayImpactAnalysis(Map<String, Integer> delayMap) {
        System.out.println("\nPROJECT SCHEDULE IMPACT ANALYSIS");
        String changeFormat = "| %-12s | %-17s |%n";
        String separator = "+--------------+-------------------+";

        System.out.println(separator);
        System.out.printf(changeFormat, "Activity ID", "Change (units)");
        System.out.println(separator);

        delayMap.forEach((id, change) -> {
            String changeStr = (change >= 0) ? "+" + change : String.valueOf(change);
            System.out.printf(changeFormat, id, changeStr);
        });
        System.out.println(separator);

        System.out.println();
        int totalChange = newProjectDuration - originalProjectDuration;
        String changeType = totalChange >= 0 ? "Delay" : "Advancement";

        System.out.printf("| %-25s | %-5d |%n", "Original Project Duration", originalProjectDuration);
        System.out.printf("| %-25s | %-5d |%n", "New Project Duration", newProjectDuration);
        System.out.printf("| %-25s | %-5d |%n", "Total " + changeType, Math.abs(totalChange));
        System.out.println();

        System.out.println("ORIGINAL CRITICAL PATH:");
        printCriticalPath(originalCriticalPath);

        System.out.println("NEW CRITICAL PATH:");
        printCriticalPath(newCriticalPath);

        System.out.println();
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
    List<List<Activity>> findCriticalPath() {
        CriticalPathIdentifierGraph calculator = new CriticalPathIdentifierGraph();
        calculator.setGraph(graph);

        calculator.calculateCriticalPaths();

        List<List<Activity>> criticalPaths = calculator.getCriticalPaths();
        if (criticalPaths.isEmpty()) {
            System.err.println("No critical paths found. Please verify the graph structure.");
        } else {
            System.out.printf("Found %d critical path(s).%n", criticalPaths.size());
        }

        return criticalPaths;
    }


    /**
     * Prints the critical path in a structured format.
     * Complexity: O(n), where n is the number of activities in the critical path.
     *
     * @param criticalPath List of activities in the critical path.
     */
    private void printCriticalPath(List<List<Activity>> criticalPath) {
        int num = 1;
        for (List<Activity> path : criticalPath) { // O(n)
            System.out.println("Critical Path: " + num);
            String format = "| %-6s | %-30s | %-8s | %-3s | %-3s | %-3s | %-3s | %-5s |%n";
            String separator = "+--------+--------------------------------+----------+-----+-----+-----+-----+-------+";

            System.out.println(separator);
            System.out.printf(format, "ID", "Name", "Duration", "ES", "EF", "LS", "LF", "Slack");
            System.out.println(separator);

            for (Activity activity : path) { // O(n)
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
            num++;
        }
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
    public List<List<Activity>> getOriginalCriticalPath() {
        return originalCriticalPath;
    }

    /**
     * Gets new critical path.
     *
     * @return the new critical path
     */
    public List<List<Activity>> getNewCriticalPath() {
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

