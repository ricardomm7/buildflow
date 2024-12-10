package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.util.Arrays;
import java.util.List;

/**
 * Class for calculating the earliest and latest start/finish times for activities
 * in a PERT/CPM graph, using the correct dependency relationships and project duration logic.
 */
public class ActivityTimeCalculator {
    private ActivitiesGraph graph;
    private int projectDuration;

    public ActivityTimeCalculator() {
        graph = Repositories.getInstance().getActivitiesGraph();
        this.projectDuration = 0;
    }

    /**
     * Calcula os tempos de início e término mais cedo (ES/EF) e mais tarde (LS/LF) para todas as atividades.
     * Fórmulas aplicadas:
     * - **ES (Earliest Start)**: O maior EF (Earliest Finish) das atividades predecessoras.
     * - **EF (Earliest Finish)**: ES + Duração.
     * - **LF (Latest Finish)**: O menor LS (Latest Start) das atividades sucessoras.
     * - **LS (Latest Start)**: LF - Duração.
     * - **Slack (Folga)**: LS - ES ou LF - EF.
     */
    public void calculateTimes() {

        // Perform topological sort
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        sorter.setGraph(graph);
        List<Activity> topOrder = sorter.performTopologicalSort();

        // Forward Pass (Earliest Times)
        calculateEarliestTimes(topOrder);

        // Backward Pass (Latest Times)
        calculateLatestTimes(topOrder);
    }

    /**
     * Realiza a passagem para frente (‘Forward’ Pass) para calcular os tempos de início (ES) e término mais cedo (EF).
     * <p>
     * Fórmulas aplicadas:
     * - **ES (Earliest Start)** Se a atividade não tiver predecessores, ES é 0. Caso contrário, ES é o maior EF
     * entre todas as atividades predecessoras.
     * - **EF (Earliest Finish)**: ES + Duração.
     * <p>
     * Detalhes:
     * - A passagem para frente é feita em ordem topológica, garantindo que todos os predecessores sejam processados
     * antes de uma atividade.
     *
     * @param topOrder A lista de atividades na ordem topológica.
     */
    private void calculateEarliestTimes(List<Activity> topOrder) {
        for (Activity activity : topOrder) {
            // If no dependencies, set early start to 0
            if (activity.getDependencies().isEmpty()) {
                activity.setEarlyStart(0);
            } else {
                // Find the maximum EF among all dependencies
                int maxEF = activity.getDependencies().stream()
                        .mapToInt(depId -> findActivityById(depId).getEarlyFinish())
                        .max()
                        .orElse(0);
                activity.setEarlyStart(maxEF);
            }

            // Calculate early finish
            activity.setEarlyFinish(activity.getEarlyStart() + activity.getDuration());
        }

        // Determine project duration (max EF among all activities)
        this.projectDuration = topOrder.stream()
                .mapToInt(Activity::getEarlyFinish)
                .max()
                .orElse(0);
    }

    /**
     * Realiza a passagem para trás (Backward Pass) para calcular os tempos de início (LS) e término mais tarde (LF).
     * <p>
     * Fórmulas aplicadas:
     * - **LF (Latest Finish)** Para as últimas atividades do grafo, LF iguala a duração total do projeto.
     * Para atividades intermediárias, LF é o menor LS das atividades sucessoras.
     * - **LS (Latest Start)** LF - Duração.
     * <p>
     * Detalhes:
     * - A passagem para trás é feita em ordem inversa da topológica, garantindo que todos os sucessores sejam processados
     * antes de uma atividade.
     */

    private void calculateLatestTimes(List<Activity> topOrder) {
        // Initialize late finish to project duration for all activities without successors
        for (int i = topOrder.size() - 1; i >= 0; i--) {
            Activity activity = topOrder.get(i);

            // If no successors, set late finish to project duration
            Activity[] neighbors = graph.getNeighbors(activity);
            if (neighbors.length == 0) {
                activity.setLateFinish(projectDuration);
            } else {
                // Find the minimum LS among successors
                int minLS = Arrays.stream(neighbors)
                        .mapToInt(Activity::getLateStart)
                        .min()
                        .orElse(projectDuration);
                activity.setLateFinish(minLS);
            }

            // Calculate late start
            activity.setLateStart(activity.getLateFinish() - activity.getDuration());
        }
    }

    /**
     * Finds an activity by its ID in the graph.
     *
     * @param id The ID of the activity to find.
     * @return The activity with the given ID.
     * @throws IllegalArgumentException if the activity is not found.
     */
    private Activity findActivityById(String id) {
        for (var linkedList : graph.getGraph().getAdjacencyList()) {
            Activity activity = linkedList.getFirst();
            if (activity.getId().equals(id)) {
                return activity;
            }
        }
        throw new IllegalArgumentException("Activity not found with ID: " + id);
    }

    /**
     * Displays the calculated schedule for all activities, including slack and critical path details.
     */
    public void displayTimes() {
        System.out.println("\n╔══════════════════════════════════════════════════════");
        System.out.println("║ PROJECT SCHEDULE ANALYSIS");
        System.out.println("╠══════════════════════════════════════════════════════");
        System.out.printf("║ Total Project Duration: %d time units%n%n", projectDuration);

        System.out.println("║ ACTIVITY SCHEDULE DETAILS:");
        System.out.println("╠══════════════════════════════════════════════════════");

        // Table header
        System.out.printf("║ %-6s %-25s %-6s %-6s %-6s %-6s %-8s %-10s%n",
                "Act ID", "Name", "ES", "EF", "LS", "LF", "Slack", "Critical");
        System.out.println("╟──────────────────────────────────────────────────────");

        // Activity details
        for (var linkedList : graph.getGraph().getAdjacencyList()) {
            Activity activity = linkedList.getFirst();
            int slack = activity.getLateStart() - activity.getEarlyStart();
            boolean isCritical = slack == 0;

            System.out.printf("║ %-6s %-25s %-6d %-6d %-6d %-6d %-8d %-10s%n",
                    activity.getId(),
                    activity.getName().length() > 25 ? activity.getName().substring(0, 22) + "..." : activity.getName(),
                    activity.getEarlyStart(),
                    activity.getEarlyFinish(),
                    activity.getLateStart(),
                    activity.getLateFinish(),
                    slack,
                    isCritical ? "Yes" : "No");
        }

        System.out.println("╚══════════════════════════════════════════════════════\n");
    }

    /**
     * Returns the total project duration.
     *
     * @return The total duration of the project.
     */
    public int getProjectDuration() {
        return projectDuration;
    }
}

