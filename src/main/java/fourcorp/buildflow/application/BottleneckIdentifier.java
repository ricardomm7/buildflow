package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.*;

public class BottleneckIdentifier {
    private final ActivitiesGraph graph;

    public BottleneckIdentifier() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
    }

    /**
     * Identify bottleneck activities in the project graph.
     */
    public void identifyBottleneckActivities() {
        // Analyze direct dependencies
        Map<Activity, Integer> dependencyCounts = countDirectDependencies();

        // Analyze path complexity
        Map<Activity, Integer> pathComplexity = analyzePathComplexity();

        // Combine and sort metrics
        List<Map<String, Object>> bottlenecks = combineMetrics(dependencyCounts, pathComplexity);

        // Display results
        displayBottleneckAnalysis(bottlenecks);
    }

    private Map<Activity, Integer> countDirectDependencies() {
        Map<Activity, Integer> dependencyCounts = new HashMap<>();

        for (var linkedList : graph.getGraph().getAdjacencyList()) {
            for (int i = 1; i < linkedList.size(); i++) {
                Activity dependent = linkedList.get(i);
                dependencyCounts.put(dependent, dependencyCounts.getOrDefault(dependent, 0) + 1);
            }
        }

        return dependencyCounts;
    }

    private Map<Activity, Integer> analyzePathComplexity() {
        Map<Activity, Integer> pathCounts = new HashMap<>();

        for (var linkedList : graph.getGraph().getAdjacencyList()) {
            Activity activity = linkedList.getFirst();
            pathCounts.put(activity, calculatePathComplexity(activity));
        }

        return pathCounts;
    }

    private int calculatePathComplexity(Activity startActivity) {
        Set<List<Activity>> uniquePaths = new HashSet<>();
        findAllPaths(startActivity, new ArrayList<>(), uniquePaths);
        return uniquePaths.size();
    }

    private void findAllPaths(Activity current, List<Activity> currentPath, Set<List<Activity>> uniquePaths) {
        // Create a copy of the current path and add the current activity
        List<Activity> newPath = new ArrayList<>(currentPath);
        newPath.add(current);

        // If the path is unique, add it to the set
        if (!uniquePaths.contains(newPath)) {
            uniquePaths.add(new ArrayList<>(newPath));
        }

        // Get all neighboring activities
        Activity[] neighbors = graph.getNeighbors(current);

        // Recursively explore paths
        for (Activity neighbor : neighbors) {
            // Prevent infinite loops by checking path length and avoiding revisits
            if (!currentPath.contains(neighbor) && newPath.size() < 10) {
                findAllPaths(neighbor, newPath, uniquePaths);
            }
        }
    }

    private List<Map<String, Object>> combineMetrics(Map<Activity, Integer> dependencyCounts, Map<Activity, Integer> pathComplexity) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Activity, Integer> entry : dependencyCounts.entrySet()) {
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("activity", entry.getKey());
            metrics.put("dependencyCount", entry.getValue());
            metrics.put("pathComplexity", pathComplexity.getOrDefault(entry.getKey(), 0));
            result.add(metrics);
        }

        result.sort((m1, m2) -> {
            int dependencyCountComparison = Integer.compare((int) m2.get("dependencyCount"), (int) m1.get("dependencyCount"));
            if (dependencyCountComparison != 0) {
                return dependencyCountComparison;
            }
            return Integer.compare((int) m2.get("pathComplexity"), (int) m1.get("pathComplexity"));
        });

        return result;
    }


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

    private String evaluateBottleneckImpact(int dependencyCount, int pathComplexity) {
        int totalScore = dependencyCount + pathComplexity;
        if (totalScore > 10) return "High Risk";
        if (totalScore > 5) return "Medium Risk";
        return "Low Risk";
    }

    // Utility method to truncate long names
    private String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}