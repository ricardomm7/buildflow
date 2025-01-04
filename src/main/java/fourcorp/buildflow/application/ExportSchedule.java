package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * The {@code ExportSchedule} class is responsible for exporting the project schedule to a CSV file.
 * It calculates the early and late start/finish times for each activity and formats this information
 * into a CSV structure, which can be easily used for further analysis or reporting.
 * The CSV export includes details such as activity ID, cost, duration, early and late timings, and dependencies.
 */
public class ExportSchedule {
    private ActivityTimeCalculator timeCalculator;
    private ActivitiesGraph graph;

    /**
     * Constructs a new {@code ExportSchedule} instance. Initializes the activity graph and the
     * {@code ActivityTimeCalculator} used to calculate the activity times (early start, late start, etc.).
     */
    public ExportSchedule() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
        this.timeCalculator = new ActivityTimeCalculator();
    }

    /**
     * Exports the project schedule to a CSV file.
     * <p>
     * This method calculates the necessary activity times and writes the project details, including
     * activity ID, cost, duration, early and late start/finish times, and dependencies to the specified
     * CSV file at the given {@code outputPath}.
     *
     * <p>The method uses a topological sort to ensure the activities are written in a valid order.</p>
     *
     * @param outputPath the path where the CSV file should be saved.
     * @throws IOException if an error occurs while writing to the file.
     */
    public void exportToCsv(String outputPath) throws IOException {
        // First, calculate times
        timeCalculator.calculateTimes();

        try (FileWriter writer = new FileWriter(outputPath)) {
            // Write header to CSV file
            writer.write("act_id,cost,duration,es,ls,ef,lf,prev_act_id1,...,prev_act_idN\n");

            // Write each activity in topological order
            ActivityTopologicalSort topologicalSort = new ActivityTopologicalSort();
            // Iterate over all vertices (activities) in the graph
            for (Activity activity : topologicalSort.performTopologicalSort()) {
                // Prepare dependencies as a comma-separated string
                String dependencies = activity.getDependencies().isEmpty()
                        ? ""
                        : activity.getDependencies().stream()
                        .collect(Collectors.joining(","));

                // Calculate slack
                int slack = activity.getLateStart() - activity.getEarlyStart();

                // Construct CSV line with activity details
                String line = String.format("%s,%.2f,%d,%d,%d,%d,%d%s%n",
                        activity.getId(),
                        activity.getCost(),
                        activity.getDuration(),
                        activity.getEarlyStart(),
                        activity.getLateStart(),
                        activity.getEarlyFinish(),
                        activity.getLateFinish(),
                        dependencies.isEmpty() ? "" : "," + dependencies
                );

                // Write the line to the CSV file
                writer.write(line);
            }

            System.out.println("Schedule successfully exported to: " + outputPath);
        }
    }

    /**
     * Sets a new activity graph to be used by this {@code ExportSchedule} instance.
     * This allows the user to export the schedule for a different project graph.
     *
     * @param graph the new {@code ActivitiesGraph} instance to be used.
     */
    public void setGraph(ActivitiesGraph graph) {
        this.graph = graph;
    }
}
