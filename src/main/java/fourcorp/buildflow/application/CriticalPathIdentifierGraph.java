package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for identifying and displaying all critical paths in a project graph.
 */
public class CriticalPathIdentifierGraph {
    private ActivitiesGraph graph;
    private int totalProjectDuration;
    private List<List<Activity>> criticalPaths;

    public CriticalPathIdentifierGraph() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
        this.totalProjectDuration = 0;
        this.criticalPaths = new ArrayList<>();
    }

    /**
     * Identifies and prints all critical paths in the graph.
     */
    public void identifyAndPrintCriticalPaths() {
        calculateCriticalPaths();
        displayCriticalPaths();
    }

    /**
     * Calculates all critical paths in the graph.
     */
    public void calculateCriticalPaths() {
        // Clear previous results
        criticalPaths.clear();

        // Get the unique start and end vertices
        Activity start = findStartVertex();
        Activity end = findEndVertex();

        if (start == null || end == null) {
            System.err.println("Error: Graph does not have a valid start or end vertex.");
            return;
        }

        // Calculate early and late times for all activities
        calculateActivityTimes();

        // Find critical paths
        List<Activity> currentPath = new ArrayList<>();
        findCriticalPaths(start, end, currentPath);

        // Determine the total project duration
        totalProjectDuration = end.getEarlyFinish();
    }

    /**
     * Finds all critical paths from a start vertex to an end vertex.
     */
    /**
     * Recursively finds all critical paths from a start vertex to an end vertex.
     *
     * @param current     The current activity in the traversal.
     * @param end         The end activity of the graph.
     * @param currentPath The current path being traversed.
     */
    private void findCriticalPaths(Activity current, Activity end, List<Activity> currentPath) {
        // Adiciona a atividade atual ao caminho
        currentPath.add(current);

        // Se chegarmos ao vértice final, verificamos se o caminho é crítico
        if (current.equals(end)) {
            if (isCriticalPath(currentPath)) {
                criticalPaths.add(new ArrayList<>(currentPath)); // Adiciona o caminho crítico
            }
        } else {
            // Continua a explorar os sucessores
            for (Activity successor : graph.getSuccessors(current)) {
                // Apenas segue para sucessores válidos e não visitados no caminho atual
                if (!currentPath.contains(successor) && isCriticalActivity(successor)) {
                    findCriticalPaths(successor, end, currentPath);
                }
            }
        }

        // Remove a atividade atual ao retroceder (backtracking)
        currentPath.remove(currentPath.size() - 1);
    }

    /**
     * Determines if a given path is critical (all activities have slack = 0).
     *
     * @param path The path to be checked.
     * @return true if the path is critical, false otherwise.
     */
    private boolean isCriticalPath(List<Activity> path) {
        return path.stream().allMatch(this::isCriticalActivity);
    }

    /**
     * Determines if an activity is critical (slack = 0).
     *
     * @param activity The activity to be checked.
     * @return true if the activity is critical, false otherwise.
     */
    private boolean isCriticalActivity(Activity activity) {
        return activity.getLateStart() - activity.getEarlyStart() == 0;
    }


    /**
     * Calculates early and late times for all activities in the graph.
     */
    private void calculateActivityTimes() {
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();
        calculator.setGraph(graph);
        calculator.calculateTimes();
    }

    /**
     * Displays the critical paths and total project duration.
     */
    private void displayCriticalPaths() {
        System.out.println("\nCRITICAL PATHS ANALYSIS");
        if (criticalPaths.isEmpty()) {
            System.out.println("No critical paths were found.");
            return;
        }

        String headerFormat = "| %-5s | %-30s | %-8s | %-27s |%n";
        String separator = "+-------+--------------------------------+----------+-----------------------------+";

        for (int i = 0; i < criticalPaths.size(); i++) {
            System.out.println("Critical Path #" + (i + 1));
            System.out.println(separator);
            System.out.printf(headerFormat, "ID", "Activity", "Duration", "Timing (ES, EF, LS, LF)");
            System.out.println(separator);

            for (Activity activity : criticalPaths.get(i)) {
                System.out.printf("| %-5s | %-30s | %-8d | ES:%-3d EF:%-3d LS:%-3d LF:%-3d |%n",
                        activity.getId(),
                        truncate(activity.getName(), 30),
                        activity.getDuration(),
                        activity.getEarlyStart(),
                        activity.getEarlyFinish(),
                        activity.getLateStart(),
                        activity.getLateFinish()
                );
            }
            System.out.println(separator);
            System.out.println();
        }

        System.out.printf("Total Project Duration: %d time units%n", totalProjectDuration);
        System.out.println();
    }

    /**
     * Finds the unique start vertex (no incoming edges).
     */
    private Activity findStartVertex() {
        List<Activity> startVertices = graph.getStartActivities();
        return startVertices.isEmpty() ? null : startVertices.get(0); // Guaranteed single start vertex
    }

    /**
     * Finds the unique end vertex (no outgoing edges).
     */
    private Activity findEndVertex() {
        List<Activity> endVertices = graph.getEndActivities();
        return endVertices.isEmpty() ? null : endVertices.get(0); // Guaranteed single end vertex
    }

    /**
     * Utility method to truncate text for better formatting.
     */
    private String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

    public List<List<Activity>> getCriticalPaths() {
        return criticalPaths;
    }

    public int getTotalProjectDuration() {
        return totalProjectDuration;
    }

    public void setGraph(ActivitiesGraph graph) {
        this.graph = graph;
        this.criticalPaths.clear();
        this.totalProjectDuration = 0;
    }
}
