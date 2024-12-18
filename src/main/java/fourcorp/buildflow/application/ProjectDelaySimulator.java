package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.*;

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

    public ProjectDelaySimulator() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
        this.timeCalculator = new ActivityTimeCalculator();
        timeCalculator.setGraph(graph);
    }

    /**
     * Simulates delays for specified activities and analyzes project impact.
     *
     * @param delayMap Map of activity IDs to their delay durations.
     */
    public void simulateProjectDelays(Map<String, Integer> delayMap) {
        // Save the original durations
        saveOriginalDurations();

        // Calculate original project metrics
        calculateOriginalMetrics();

        // Apply delays
        applyDelays(delayMap);

        // Recalculate project metrics after delays
        calculateNewMetrics();

        // Display analysis
        displayImpactAnalysis(delayMap);
    }

    /**
     * Saves the original durations of activities.
     */
    private void saveOriginalDurations() {
        for (Activity activity : graph.getGraph().vertices()) {
            originalDurations.put(activity.getId(), activity.getDuration());
        }
    }

    /**
     * Restores the original durations of activities.
     */
    private void restoreOriginalDurations() {
        for (Activity activity : graph.getGraph().vertices()) {
            activity.setDuration(originalDurations.get(activity.getId()));
        }
    }

    /**
     * Calculates the project metrics before applying delays.
     */
    private void calculateOriginalMetrics() {
        restoreOriginalDurations(); // Ensure calculations are based on original durations
        timeCalculator.calculateTimes();
        originalProjectDuration = timeCalculator.getProjectDuration();
        originalCriticalPath = findCriticalPath();
    }

    /**
     * Applies delays to the graph.
     *
     * @param delayMap Map of activity IDs to their delay durations.
     */
    private void applyDelays(Map<String, Integer> delayMap) {
        for (Map.Entry<String, Integer> entry : delayMap.entrySet()) {
            String activityId = entry.getKey();
            int delayAmount = entry.getValue();

            // Verifica se o delay é negativo
            if (delayAmount < 0) {
                System.err.printf("Erro: A atividade %s possui um delay negativo (%d).%n", activityId, delayAmount);
                continue; // Ignora a aplicação desse delay
            }

            // Procura a atividade no grafo e aplica o delay
            Activity activity = graph.getGraph().vertex(a -> a.getId().equals(activityId));
            if (activity != null) {
                activity.setDuration(originalDurations.get(activityId) + delayAmount);
            } else {
                System.err.printf("Erro: A atividade %s não foi encontrada no grafo.%n", activityId);
            }
        }
    }


    /**
     * Calculates the project metrics after applying delays.
     */
    private void calculateNewMetrics() {
        timeCalculator.calculateTimes(); // Recalculate times after delays
        newProjectDuration = timeCalculator.getProjectDuration();
        newCriticalPath = findCriticalPath();
    }

    /**
     * Finds the critical path in the current graph.
     *
     * @return List of activities in the critical path.
     */
    List<Activity> findCriticalPath() {
        List<Activity> criticalPath = new ArrayList<>();

        for (Activity activity : graph.getGraph().vertices()) {
            int slack = activity.getLateStart() - activity.getEarlyStart();
            if (slack == 0) { // Critical activities have zero slack
                criticalPath.add(activity);
            }
        }

        criticalPath.sort(Comparator.comparingInt(Activity::getEarlyStart));
        return criticalPath;
    }

    /**
     * Displays the delay impact analysis.
     *
     * @param delayMap Map of delayed activities.
     */
    private void displayImpactAnalysis(Map<String, Integer> delayMap) {
        System.out.println("\n╔══════════════════════════════════════════════════════");
        System.out.println("║ PROJECT DELAY IMPACT ANALYSIS");
        System.out.println("╠══════════════════════════════════════════════════════");

        // Delayed Activities
        System.out.println("║ DELAYED ACTIVITIES:");
        delayMap.forEach((id, delay) ->
                System.out.printf("║   • Activity %s: +%d time units%n", id, delay)
        );

        System.out.println("╠══════════════════════════════════════════════════════");

        // Project Duration Impact
        System.out.printf("║ Original Project Duration: %d time units%n", originalProjectDuration);
        System.out.printf("║ New Project Duration:      %d time units%n", newProjectDuration);
        System.out.printf("║ Total Delay:               %d time units%n", newProjectDuration - originalProjectDuration);

        System.out.println("╠══════════════════════════════════════════════════════");

        // Critical Path Analysis
        System.out.println("║ ORIGINAL CRITICAL PATH:");
        printCriticalPath(originalCriticalPath);

        System.out.println("║"); // Spacer
        System.out.println("║ NEW CRITICAL PATH:");
        printCriticalPath(newCriticalPath);

        System.out.println("╚══════════════════════════════════════════════════════\n");
    }

    /**
     * Prints the critical path in a structured format.
     *
     * @param criticalPath List of activities in the critical path.
     */
    private void printCriticalPath(List<Activity> criticalPath) {
        String format = "║   • %-6s %-30s | Duration: %-4d | ES: %-3d | EF: %-3d | LS: %-3d | LF: %-3d | Slack: %-3d%n";
        for (Activity activity : criticalPath) {
            System.out.printf(format,
                    activity.getId(),
                    truncate(activity.getName(), 30),
                    activity.getDuration(),
                    activity.getEarlyStart(),
                    activity.getEarlyFinish(),
                    activity.getLateStart(),
                    activity.getLateFinish(),
                    activity.getLateStart() - activity.getEarlyStart());
        }
    }

    /**
     * Truncates text for better formatting in outputs.
     */
    private String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

    public Activity findActivityById(String input) {
        for (Activity activity : Repositories.getInstance().getActivitiesGraph().getGraph().vertices()) {
            if (activity.getId().equals(input)) {
                return activity;
            }
        }
        return null;
    }

    public void setGraph(ActivitiesGraph customGraph) {
        this.graph.setGraph(customGraph.getGraph());
        this.timeCalculator.setGraph(this.graph);
    }

    public List<Activity> getNewCriticalPath() {
        return newCriticalPath;
    }

    public int getNewProjectDuration() {
        return newProjectDuration;
    }

    public int getOriginalProjectDuration() {
        return originalProjectDuration;
    }

    public List<Activity> getOriginalCriticalPath() {
        return originalCriticalPath;
    }
}
