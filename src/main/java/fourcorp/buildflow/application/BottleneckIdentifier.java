package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The {@code BottleneckIdentifier} class identifies potential bottleneck activities in a graph-based PERT/CPM system.
 * It analyzes dependencies and path complexities to rank activities based on their criticality.
 */
public class BottleneckIdentifier {
    private final ActivitiesGraph graph;

    /**
     * Initializes the BottleneckIdentifier with the current activities graph.
     */
    public BottleneckIdentifier() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
    }

    /**
     * Identifies and displays bottleneck activities in the graph.
     * The method calculates dependency counts, path complexities, combines the results,
     * sorts activities topologically, and outputs the analysis in a formatted table.
     * <p>
     * The complexity of this method is: O(n^2).
     */
    public void identifyBottleneckActivities() {
        // Analisa dependências diretas
        Map<Activity, Integer> dependencyCounts = countDirectDependencies(); // O(n^2)

        // Analisa a complexidade dos caminhos
        Map<Activity, Integer> pathComplexity = analyzePathComplexity(); // O(n)

        List<Map<String, Object>> bottlenecks = combineMetrics(dependencyCounts, pathComplexity); // O(n)

        bottlenecks = sortBottlenecksTopologically(bottlenecks); // O(nlog(n))

        displayBottleneckAnalysis(bottlenecks);
    }

    /**
     * Counts the number of direct dependencies (incoming edges) for each activity in the graph.
     * <p>
     * The complexity of this method is: O(n^2).
     *
     * @return A map where keys are activities, and values are the number of direct dependencies.
     */
    private Map<Activity, Integer> countDirectDependencies() {
        Map<Activity, Integer> dependencyCounts = new HashMap<>();

        for (Activity activity : graph.getGraph().vertices()) { // O(n)
            dependencyCounts.put(activity, 0);
        }

        for (Activity activity : graph.getGraph().vertices()) { // O(n)
            for (Activity dependent : graph.getGraph().adjVertices(activity)) { // O(n^2)
                dependencyCounts.put(dependent, dependencyCounts.getOrDefault(dependent, 0) + 1);
            }
        }

        return dependencyCounts;
    }

    /**
     * Analyzes the path complexity for each activity, measuring the number of paths originating from it.
     * The complexity of this method is: O(n).
     *
     * @return A map where keys are activities, and values are the number of paths originating from the activity.
     */
    private Map<Activity, Integer> analyzePathComplexity() {
        Map<Activity, Integer> pathCounts = new HashMap<>();

        for (Activity activity : graph.getGraph().vertices()) { // O(n)
            pathCounts.put(activity, calculatePathComplexity(activity));
        }

        return pathCounts;
    }

    /**
     * Calculates the total number of unique paths originating from the given activity using memoization.
     * The complexity of this method is: O(n).
     *
     * @param startActivity The activity from which to calculate path complexity.
     * @return The number of paths originating from the given activity.
     */
    private int calculatePathComplexity(Activity startActivity) {
        Map<Activity, Integer> memo = new HashMap<>();
        return countPaths(startActivity, memo); // O(n)
    }

    /**
     * Recursively counts all paths from the current activity using memoization to avoid redundant computations.
     * The complexity of this method is: O(n).
     *
     * @param current The current activity being analyzed.
     * @param memo    A memoization map to store previously calculated path counts.
     * @return The number of paths from the current activity.
     */
    private int countPaths(Activity current, Map<Activity, Integer> memo) {
        if (memo.containsKey(current)) {
            return memo.get(current);
        }

        int count = 1; // Contar o caminho atual
        for (Activity neighbor : graph.getNeighbors(current)) { // O(n)
            count += countPaths(neighbor, memo);
        }

        memo.put(current, count);
        return count;
    }

    /**
     * Combines dependency counts and path complexity metrics for all activities into a list of maps.
     * The complexity of this method is: O(n).
     *
     * @param dependencyCounts A map of activities and their dependency counts.
     * @param pathComplexity   A map of activities and their path complexities.
     * @return A list of maps containing combined metrics for each activity.
     */
    private List<Map<String, Object>> combineMetrics(Map<Activity, Integer> dependencyCounts, Map<Activity, Integer> pathComplexity) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Activity activity : graph.getGraph().vertices()) { // O(n)
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("activity", activity);
            metrics.put("dependencyCount", dependencyCounts.getOrDefault(activity, 0));
            metrics.put("pathComplexity", pathComplexity.getOrDefault(activity, 0));
            result.add(metrics);
        }

        return result;
    }

    /**
     * Sorts bottleneck activities based on a topological order of the graph.
     * The complexity of this method is: O(nlog(n)).
     *
     * @param bottlenecks A list of maps containing metrics for each activity.
     * @return A list of sorted bottlenecks, where activities appear in topological order.
     */
    private List<Map<String, Object>> sortBottlenecksTopologically(List<Map<String, Object>> bottlenecks) {
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        sorter.setGraph(graph);

        List<Activity> topologicalOrder = sorter.performTopologicalSort(); // O(n)

        return bottlenecks.stream()
                .sorted(Comparator.comparingInt(
                        metrics -> topologicalOrder.indexOf(metrics.get("activity"))
                ))
                .collect(Collectors.toList()); // O(nlog(n))
    }

    /**
     * Displays the bottleneck analysis results in a formatted table.
     *
     * @param bottlenecks A list of maps containing combined metrics for each activity.
     */
    private void displayBottleneckAnalysis(List<Map<String, Object>> bottlenecks) {
        System.out.println();
        System.out.println("COMPREHENSIVE BOTTLENECK ACTIVITIES ANALYSIS");
        String headerFormat = "| %-4s | %-30s | %-12s | %-15s | %-18s |%n";
        String separator = "+------+--------------------------------+--------------+-----------------+--------------------+";

        System.out.println(separator);
        System.out.format(headerFormat, "ID", "Activity", "Dependencies", "Path Complexity", "Potential Impact");
        System.out.println(separator);

        for (Map<String, Object> metrics : bottlenecks) {
            Activity activity = (Activity) metrics.get("activity");
            int dependencyCount = (int) metrics.get("dependencyCount");
            int pathComplexity = (int) metrics.get("pathComplexity");

            System.out.format(headerFormat,
                    activity.getId(),
                    truncate(activity.getName(), 30),
                    dependencyCount,
                    pathComplexity,
                    evaluateBottleneckImpact(dependencyCount, pathComplexity)
            );
        }

        System.out.println(separator);
        System.out.printf("Total Activities Analyzed: %d%n", bottlenecks.size());
        System.out.println();
    }

    /**
     * Evaluates the potential impact of an activity based on dependency count and path complexity.
     *
     * @param dependencyCount The number of direct dependencies of the activity.
     * @param pathComplexity  The complexity of paths originating from the activity.
     * @return A string indicating the impact level: "High Risk", "Medium Risk", or "Low Risk".
     */
    private String evaluateBottleneckImpact(int dependencyCount, int pathComplexity) {
        int totalScore = dependencyCount + pathComplexity;
        if (totalScore > 10) return "High Risk";
        if (totalScore > 5) return "Medium Risk";
        return "Low Risk";
    }

    /**
     * Truncates a given string to the specified maximum length and appends "..." if truncation occurs.
     *
     * @param text      The input string to truncate.
     * @param maxLength The maximum allowed length of the string.
     * @return The truncated string, if necessary; otherwise, the original string.
     */
    private String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}