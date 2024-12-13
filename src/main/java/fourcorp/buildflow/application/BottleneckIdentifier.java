package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.*;
import java.util.stream.Collectors;

public class BottleneckIdentifier {
    private final ActivitiesGraph graph;

    public BottleneckIdentifier() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
    }

    /**
     * Identifica atividades gargalo no grafo do projeto.
     */
    public void identifyBottleneckActivities() {
        // Analisa dependências diretas
        Map<Activity, Integer> dependencyCounts = countDirectDependencies();

        // Analisa a complexidade dos caminhos
        Map<Activity, Integer> pathComplexity = analyzePathComplexity();

        // Combina as métricas
        List<Map<String, Object>> bottlenecks = combineMetrics(dependencyCounts, pathComplexity);

        // Ordena pela ordem topológica
        bottlenecks = sortBottlenecksTopologically(bottlenecks);

        // Exibe os resultados
        displayBottleneckAnalysis(bottlenecks);
    }

    /**
     * Conta as dependências diretas de cada atividade.
     */
    private Map<Activity, Integer> countDirectDependencies() {
        Map<Activity, Integer> dependencyCounts = new HashMap<>();

        for (Activity activity : graph.getGraph().vertices()) {
            Collection<Activity> adjacencies = graph.getGraph().adjVertices(activity);

            for (Activity dependent : adjacencies) {
                dependencyCounts.put(dependent, dependencyCounts.getOrDefault(dependent, 0) + 1);
            }
        }

        return dependencyCounts;
    }

    /**
     * Calcula a complexidade dos caminhos para cada atividade.
     */
    private Map<Activity, Integer> analyzePathComplexity() {
        Map<Activity, Integer> pathCounts = new HashMap<>();

        for (Activity activity : graph.getGraph().vertices()) {
            pathCounts.put(activity, calculatePathComplexity(activity));
        }

        return pathCounts;
    }

    /**
     * Calcula a complexidade dos caminhos partindo de uma atividade inicial.
     */
    private int calculatePathComplexity(Activity startActivity) {
        Set<List<Activity>> uniquePaths = new HashSet<>();
        findAllPaths(startActivity, new ArrayList<>(), uniquePaths);
        return uniquePaths.size();
    }

    private void findAllPaths(Activity current, List<Activity> currentPath, Set<List<Activity>> uniquePaths) {
        List<Activity> newPath = new ArrayList<>(currentPath);
        newPath.add(current);

        if (!uniquePaths.contains(newPath)) {
            uniquePaths.add(new ArrayList<>(newPath));
        }

        for (Activity neighbor : graph.getNeighbors(current)) {
            if (!currentPath.contains(neighbor) && newPath.size() < 10) {
                findAllPaths(neighbor, newPath, uniquePaths);
            }
        }
    }

    /**
     * Combina as métricas de dependência e complexidade de caminho numa lista.
     */
    private List<Map<String, Object>> combineMetrics(Map<Activity, Integer> dependencyCounts, Map<Activity, Integer> pathComplexity) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Activity activity : graph.getGraph().vertices()) {
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("activity", activity);
            metrics.put("dependencyCount", dependencyCounts.getOrDefault(activity, 0));
            metrics.put("pathComplexity", pathComplexity.getOrDefault(activity, 0));
            result.add(metrics);
        }

        return result;
    }

    /**
     * Ordena os gargalos pela ordem topológica.
     */
    private List<Map<String, Object>> sortBottlenecksTopologically(List<Map<String, Object>> bottlenecks) {
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        sorter.setGraph(graph);

        List<Activity> topologicalOrder = sorter.performTopologicalSort();

        return bottlenecks.stream()
                .sorted(Comparator.comparingInt(
                        metrics -> topologicalOrder.indexOf(metrics.get("activity"))
                ))
                .collect(Collectors.toList());
    }

    /**
     * Exibe a análise dos gargalos de forma tabular.
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

    private String evaluateBottleneckImpact(int dependencyCount, int pathComplexity) {
        int totalScore = dependencyCount + pathComplexity;
        if (totalScore > 10) return "High Risk";
        if (totalScore > 5) return "Medium Risk";
        return "Low Risk";
    }

    private String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}

/*

Path Complexity Explanation:
Path complexity is a metric that attempts to measure how central or influential an activity is within the project
network by counting the number of unique paths that pass through it. Here's a more detailed breakdown:

What is Path Complexity?

It represents how many different routes exist through an activity in the project graph
Activities that appear on many different paths are potentially more critical to the overall project flow
It helps identify activities that, if delayed, could impact multiple other activities


How Path Complexity is Calculated:

The method uses a depth-first search (DFS) algorithm to count unique paths
It traverses the graph from the given activity, exploring all possible routes
Each unique path through the activity increases its complexity score
More paths suggest the activity is more interconnected in the project network
*/