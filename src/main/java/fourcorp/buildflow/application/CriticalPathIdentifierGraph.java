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
     * The complexity is O(n^2), where n is the number of activities.
     */
    public void identifyAndPrintCriticalPaths() {
        calculateCriticalPaths(); // O(n^2)
        displayCriticalPaths(); // O(n^2)
    }

    /**
     * Calculates all critical paths in the graph.
     * The complexity is O(n^2), where n is the number of activities.
     */
    public void calculateCriticalPaths() {
        // Clear previous results
        criticalPaths.clear();

        // Get the unique start and end vertices
        Activity start = findStartVertex(); // O(n)
        Activity end = findEndVertex(); // O(n)

        if (start == null || end == null) {
            System.err.println("Error: Graph does not have a valid start or end vertex.");
            return;
        }

        // Calculate early and late times for all activities
        calculateActivityTimes(); // O(n^2)

        // Find critical paths
        List<Activity> currentPath = new ArrayList<>();
        findCriticalPaths(start, end, currentPath); // O(n^2)

        // Determine the total project duration
        totalProjectDuration = end.getEarlyFinish();
    }

    /**
     * Finds all critical paths from a start vertex to an end vertex.
     */
    /**
     * Recursively finds all critical paths from a start vertex to an end vertex.
     * <p>
     * The complexity of this method is O(n^2), where n is the number of activities in the graph.
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
            if (isCriticalPath(currentPath)) { // O(n)
                criticalPaths.add(new ArrayList<>(currentPath)); // Adiciona o caminho crítico
            }
        } else {
            // Continua a explorar os sucessores
            for (Activity successor : graph.getSuccessors(current)) { // O(n)
                // Apenas segue para sucessores válidos e não visitados no caminho atual
                if (!currentPath.contains(successor) && isCriticalActivity(successor)) { // O(n) * O(n) = O(n^2)
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
     * The complexity of this method is O(n), where n is the number of activities in the path.
     */
    private boolean isCriticalPath(List<Activity> path) {
        return path.stream().allMatch(this::isCriticalActivity); // O(n)
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
     * The complexity of this method is O(n^2), where n is the number of activities.
     */
    private void calculateActivityTimes() {
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();
        calculator.setGraph(graph);
        calculator.calculateTimes(); // O(n^2)
    }

    /**
     * Displays the critical paths and total project duration.
     * The complexity of this method is O(n^2), where n is the number of activities.
     */
    private void displayCriticalPaths() {
        System.out.println("\nCRITICAL PATHS ANALYSIS");
        if (criticalPaths.isEmpty()) {
            System.out.println("No critical paths were found.");
            return;
        }

        String headerFormat = "| %-5s | %-30s | %-8s | %-27s |%n";
        String separator = "+-------+--------------------------------+----------+-----------------------------+";

        for (int i = 0; i < criticalPaths.size(); i++) { // O(n)
            System.out.println("Critical Path #" + (i + 1));
            System.out.println(separator);
            System.out.printf(headerFormat, "ID", "Activity", "Duration", "Timing (ES, EF, LS, LF)");
            System.out.println(separator);

            for (Activity activity : criticalPaths.get(i)) { // O(n) * O(n) = O(n^2)
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
     * The complexity of this method is O(n), where n is the number of activities.
     */
    private Activity findStartVertex() {
        List<Activity> startVertices = graph.getStartActivities();
        return startVertices.isEmpty() ? null : startVertices.get(0); // O(n)
    }

    /**
     * Finds the unique end vertex (no outgoing edges).
     * The complexity of this method is O(n), where n is the number of activities.
     */
    private Activity findEndVertex() {
        List<Activity> endVertices = graph.getEndActivities();
        return endVertices.isEmpty() ? null : endVertices.get(0); // O(n)
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
