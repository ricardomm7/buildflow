package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CriticalPathIdentifierGraph} class is responsible for identifying and displaying
 * all critical paths in a project graph. It performs calculations to determine the critical paths,
 * total project duration, and provides methods to print and retrieve the results.
 *
 * <p>The class utilizes an activity graph and evaluates each activity's early and late times to
 * determine if a path is critical, meaning that all activities in the path have zero slack.</p>
 */
public class CriticalPathIdentifierGraph {
    private ActivitiesGraph graph;
    private int totalProjectDuration;
    private List<List<Activity>> criticalPaths;

    /**
     * Constructs a new {@code CriticalPathIdentifierGraph} and initializes the activity graph,
     * total project duration, and the list of critical paths.
     */
    public CriticalPathIdentifierGraph() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
        this.totalProjectDuration = 0;
        this.criticalPaths = new ArrayList<>();
    }

    /**
     * Identifies and prints all critical paths in the graph. This method first calculates the
     * critical paths and then displays them in a formatted output.
     *
     * <p>The complexity of this method is O(n^2), where n is the number of activities.</p>
     */
    public void identifyAndPrintCriticalPaths() {
        calculateCriticalPaths(); // O(n^2)
        displayCriticalPaths(); // O(n^2)
    }

    /**
     * Calculates all critical paths in the graph. It computes the early and late times for each
     * activity and determines which paths are critical by checking if all activities in the path
     * have zero slack.
     *
     * <p>The complexity of this method is O(n^2), where n is the number of activities.</p>
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
     * Recursively finds all critical paths from a start vertex to an end vertex.
     * The method explores the graph from the start activity to the end activity, adding activities
     * to the path and checking if the path is critical (all activities with zero slack).
     *
     * <p>The complexity of this method is O(n^2), where n is the number of activities in the graph.</p>
     *
     * @param current     The current activity being processed.
     * @param end         The end activity of the project.
     * @param currentPath The current path being traversed.
     */
    private void findCriticalPaths(Activity current, Activity end, List<Activity> currentPath) {
        currentPath.add(current);

        // If we have reached the end vertex, check if the path is critical
        if (current.equals(end)) {
            if (isCriticalPath(currentPath)) { // O(n)
                criticalPaths.add(new ArrayList<>(currentPath)); // Add critical path
            }
        } else {
            // Continue exploring successors
            for (Activity successor : graph.getSuccessors(current)) { // O(n)
                if (!currentPath.contains(successor) && isCriticalActivity(successor)) { // O(n)
                    findCriticalPaths(successor, end, currentPath);
                }
            }
        }

        // Backtrack (remove current activity from the path)
        currentPath.remove(currentPath.size() - 1);
    }

    /**
     * Determines if a given path is critical (all activities have slack = 0).
     *
     * <p>The complexity of this method is O(n), where n is the number of activities in the path.</p>
     *
     * @param path The path to be checked.
     * @return true if the path is critical, false otherwise.
     */
    private boolean isCriticalPath(List<Activity> path) {
        return path.stream().allMatch(this::isCriticalActivity); // O(n)
    }

    /**
     * Determines if an activity is critical by checking if its slack is zero.
     * Slack is the difference between the early start and late start times.
     *
     * @param activity The activity to be checked.
     * @return true if the activity is critical (slack = 0), false otherwise.
     */
    private boolean isCriticalActivity(Activity activity) {
        return activity.getLateStart() - activity.getEarlyStart() == 0;
    }

    /**
     * Calculates early and late times for all activities in the graph.
     * These times are essential for determining which paths are critical.
     *
     * <p>The complexity of this method is O(n^2), where n is the number of activities.</p>
     */
    private void calculateActivityTimes() {
        ActivityTimeCalculator calculator = new ActivityTimeCalculator();
        calculator.setGraph(graph);
        calculator.calculateTimes(); // O(n^2)
    }

    /**
     * Displays the critical paths and the total project duration.
     * The critical paths are printed in a formatted table, showing the activities in each path
     * along with their durations and timings (early start, early finish, late start, late finish).
     *
     * <p>The complexity of this method is O(n^2), where n is the number of activities.</p>
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

            for (Activity activity : criticalPaths.get(i)) { // O(n^2)
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
     * Finds the unique start vertex in the graph, which has no incoming edges.
     *
     * <p>The complexity of this method is O(n), where n is the number of activities.</p>
     *
     * @return the start activity, or {@code null} if no valid start activity exists.
     */
    private Activity findStartVertex() {
        List<Activity> startVertices = graph.getStartActivities();
        return startVertices.isEmpty() ? null : startVertices.get(0); // O(n)
    }

    /**
     * Finds the unique end vertex in the graph, which has no outgoing edges.
     *
     * <p>The complexity of this method is O(n), where n is the number of activities.</p>
     *
     * @return the end activity, or {@code null} if no valid end activity exists.
     */
    private Activity findEndVertex() {
        List<Activity> endVertices = graph.getEndActivities();
        return endVertices.isEmpty() ? null : endVertices.get(0); // O(n)
    }

    /**
     * Utility method to truncate text for better formatting in the display output.
     *
     * @param text      The text to truncate.
     * @param maxLength The maximum length of the truncated text.
     * @return the truncated text.
     */
    private String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

    /**
     * Retrieves the list of all critical paths calculated by this class.
     *
     * @return a list of critical paths, where each path is represented as a list of activities.
     */
    public List<List<Activity>> getCriticalPaths() {
        return criticalPaths;
    }

    /**
     * Retrieves the total project duration, based on the critical path analysis.
     *
     * @return the total duration of the project in time units.
     */
    public int getTotalProjectDuration() {
        return totalProjectDuration;
    }

    /**
     * Sets a new activity graph for analysis and resets the existing results.
     *
     * @param graph the new {@code ActivitiesGraph} instance to analyze.
     */
    public void setGraph(ActivitiesGraph graph) {
        this.graph = graph;
        this.criticalPaths.clear();
        this.totalProjectDuration = 0;
    }
}
