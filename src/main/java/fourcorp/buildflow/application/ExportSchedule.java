package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class ExportSchedule {
    ActivityTimeCalculator timeCalculator;
    private ActivitiesGraph graph;

    public ExportSchedule() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
        this.timeCalculator = new ActivityTimeCalculator();
    }

    /**
     * Export to csv.
     *
     * @param outputPath the output path
     * @throws IOException the io exception
     */
    public void exportToCsv(String outputPath) throws IOException {
        // First, calculate times
        timeCalculator.calculateTimes();

        try (FileWriter writer = new FileWriter(outputPath)) {
            // Write header
            writer.write("act_id,cost,duration,es,ls,ef,lf,prev_act_id1,...,prev_act_idN\n");

            // Write each activity
            ActivityTopologicalSort topologicalSort = new ActivityTopologicalSort();
            // Iterate over all vertices (activities)
            for (Activity activity : topologicalSort.performTopologicalSort()) {
                // Prepare dependencies
                String dependencies = activity.getDependencies().isEmpty()
                        ? ""
                        : activity.getDependencies().stream()
                        .collect(Collectors.joining(","));

                // Calculate slack
                int slack = activity.getLateStart() - activity.getEarlyStart();

                // Construct CSV line
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

                writer.write(line);
            }

            System.out.println("Schedule successfully exported to: " + outputPath);
        }
    }

    /**
     * Sets graph.
     *
     * @param graph the graph
     */
    public void setGraph(ActivitiesGraph graph) {
        this.graph = graph;
    }
}