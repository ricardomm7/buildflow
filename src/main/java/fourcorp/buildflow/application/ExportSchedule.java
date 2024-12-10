package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class ExportSchedule {
    private final ActivityTimeCalculator timeCalculator;
    private final ActivitiesGraph graph;

    public ExportSchedule(ActivitiesGraph graph) {
        this.graph = graph;
        this.timeCalculator = new ActivityTimeCalculator(graph);
    }

    public void exportToCsv(String outputPath) throws IOException {
        // First, calculate times
        timeCalculator.calculateTimes();

        try (FileWriter writer = new FileWriter(outputPath)) {
            // Write header
            writer.write("act_id,cost,duration,es,ls,ef,lf,prev_act_id1,...,prev_act_idN\n");

            // Write each activity
            for (var linkedList : graph.getGraph().getAdjacencyList()) {
                Activity activity = linkedList.getFirst();

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

    public static void main(String[] args) {
        try {
            // Load activities from CSV
            ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
            Reader.loadActivities("textFiles/activities.csv");

            // Create and export schedule
            ExportSchedule exporter = new ExportSchedule(graph);
            exporter.exportToCsv("outFiles/schedule.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}