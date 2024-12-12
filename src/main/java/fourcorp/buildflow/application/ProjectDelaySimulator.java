package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectDelaySimulator {
    private final ActivitiesGraph originalGraph;
    private ActivitiesGraph workingGraph;
    private ActivityTimeCalculator timeCalculator;

    // Original project metrics
    private int originalProjectDuration;
    private List<Activity> originalCriticalPath;

    public ProjectDelaySimulator() {
        this.originalGraph = Repositories.getInstance().getActivitiesGraph();
        this.workingGraph = cloneGraph(Repositories.getInstance().getActivitiesGraph());
    }

    /**
     * Simulates delays for specified activities and analyzes project impact
     *
     * @param delayMap Map of activity IDs to their delay durations
     */
    public void simulateProjectDelays(Map<String, Integer> delayMap) {
        // Reset working graph to original state
        workingGraph = cloneGraph(originalGraph);

        // Calculate original project metrics
        calculateOriginalProjectMetrics();

        // Apply delays
        applyDelays(delayMap);

        // Recalculate project schedule
        timeCalculator = new ActivityTimeCalculator();
        timeCalculator.calculateTimes();

        // Display comprehensive delay analysis
        displayDelayImpactAnalysis(delayMap);
    }

    /**
     * Applies delays to specified activities
     *
     * @param delayMap Map of activity IDs to their delay durations
     */
    private void applyDelays(Map<String, Integer> delayMap) {
        for (Activity activity : workingGraph.getGraph().vertices()) {
            if (delayMap.containsKey(activity.getId())) {
                int delayAmount = delayMap.get(activity.getId());
                activity.setDuration(activity.getDuration() + delayAmount);
            }
        }
    }

    /**
     * Calculates original project metrics before delay simulation
     */
    private void calculateOriginalProjectMetrics() {
        ActivityTimeCalculator originalCalculator = new ActivityTimeCalculator();
        originalCalculator.calculateTimes();

        // Determine original project duration
        originalProjectDuration = originalCalculator.getProjectDuration();

        // Identify original critical path
        originalCriticalPath = findCriticalPath(originalGraph);
    }

    /**
     * Finds critical path activities
     *
     * @param graph ActivitiesGraph to analyze
     * @return List of critical path activities
     */
    private List<Activity> findCriticalPath(ActivitiesGraph graph) {
        List<Activity> criticalPath = new ArrayList<>();

        for (Activity activity : graph.getGraph().vertices()) {
            int slack = activity.getLateStart() - activity.getEarlyStart();
            if (slack == 0) {
                criticalPath.add(activity);
            }
        }
        return criticalPath.stream()
                .sorted(Comparator.comparingInt(Activity::getEarlyStart))
                .collect(Collectors.toList());
    }

    /**
     * Displays comprehensive delay impact analysis
     *
     * @param delayMap Map of delayed activities
     */
    private void displayDelayImpactAnalysis(Map<String, Integer> delayMap) {
        int newProjectDuration = timeCalculator.getProjectDuration();
        List<Activity> newCriticalPath = findCriticalPath(workingGraph);

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

        // Critical Path Changes
        System.out.println("║ CRITICAL PATH ANALYSIS:");
        System.out.println("║ Original Critical Path Activities:");
        originalCriticalPath.forEach(activity ->
                System.out.printf("║   • %s: %s (Duration: %d, Slack: %d)%n",
                        activity.getId(), activity.getName(), activity.getDuration(),
                        activity.getLateStart() - activity.getEarlyStart())
        );

        System.out.println("║");  // Space between original and new critical paths

        System.out.println("║ New Critical Path Activities:");
        newCriticalPath.forEach(activity ->
                System.out.printf("║   • %s: %s (Duration: %d, Slack: %d)%n",
                        activity.getId(), activity.getName(), activity.getDuration(),
                        activity.getLateStart() - activity.getEarlyStart())
        );

        System.out.println("╚══════════════════════════════════════════════════════\n");
    }

    /**
     * Creates a deep clone of the Activities Graph
     *
     * @param originalGraph Original graph to clone
     * @return Cloned ActivitiesGraph
     */
    private ActivitiesGraph cloneGraph(ActivitiesGraph originalGraph) {
        ActivitiesGraph clone = new ActivitiesGraph();
        clone.setGraph(originalGraph.getGraph());

        return clone;
    }

    /**
     * Finds an activity by ID in the original graph.
     *
     * @param id Activity ID to find.
     * @return The activity with the specified ID, or null if not found.
     */
    public Activity findActivityById(String id) {
        for (Activity activity : originalGraph.getGraph().vertices()) {
            if (activity.getId().equals(id)) {
                return activity;
            }
        }
        return null;
    }
}