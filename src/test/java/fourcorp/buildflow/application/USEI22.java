package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class USEI22 {

    private CriticalPathIdentifierGraph pathIdentifier;
    private ActivitiesGraph graph;
    private ActivityTimeCalculator calculator = new ActivityTimeCalculator();
    private ArrayList<String> lista = new ArrayList<>();

    @BeforeEach
    void setUp() {
        graph = new ActivitiesGraph();
        pathIdentifier = new CriticalPathIdentifierGraph();
    }


    @Test
    void testEmptyGraph() {
        pathIdentifier.setGraph(graph);
        pathIdentifier.calculateCriticalPaths();

        List<List<Activity>> criticalPaths = pathIdentifier.getCriticalPaths();

        assertTrue(criticalPaths.isEmpty(), "Os caminhos críticos devem estar vazios para um grafo vazio");
        assertEquals(0, pathIdentifier.getTotalProjectDuration(),
                "A duração total do projeto deve ser 0 para um grafo vazio");
    }

    @Test
    void testSingleActivity() {
        Activity start = new Activity("START", "Virtual Start", 0, "0", 0, "0", lista);

        graph.addActivity(start);

        pathIdentifier.setGraph(graph);
        calculator.calculateTimes();
        pathIdentifier.calculateCriticalPaths();

        List<List<Activity>> criticalPaths = pathIdentifier.getCriticalPaths();

        assertEquals(1, criticalPaths.size(), "Deve haver um caminho crítico");
        assertEquals(1, criticalPaths.get(0).size(), "O caminho crítico deve ter uma atividade");
        assertEquals(start, criticalPaths.get(0).get(0), "O caminho crítico deve conter a atividade de início");
        assertEquals(0, pathIdentifier.getTotalProjectDuration(),
                "A duração total deve ser 0 para apenas a atividade inicial");
    }


    @Test
    void testDisconnectedGraph() {
        // Create two separate sets of activities
        Activity start1 = new Activity("START1", "Virtual Start 1", 0, "0", 0, "0", lista);
        Activity activityA = new Activity("A", "Task A", 2, "0", 2, "0", lista);
        Activity end1 = new Activity("END1", "Virtual End 1", 0, "2", 2, "2", lista);

        Activity start2 = new Activity("START2", "Virtual Start 2", 0, "0", 0, "0", lista);
        Activity activityB = new Activity("B", "Task B", 3, "0", 3, "0", lista);
        Activity end2 = new Activity("END2", "Virtual End 2", 0, "3", 3, "3", lista);

        // Add all activities
        graph.addActivity(start1);
        graph.addActivity(activityA);
        graph.addActivity(end1);
        graph.addActivity(start2);
        graph.addActivity(activityB);
        graph.addActivity(end2);

        // Add dependencies for separate paths
        graph.addDependency(start1, activityA);
        graph.addDependency(activityA, end1);
        graph.addDependency(start2, activityB);
        graph.addDependency(activityB, end2);

        pathIdentifier.setGraph(graph);
        calculator.calculateTimes();
        pathIdentifier.calculateCriticalPaths();

        List<List<Activity>> criticalPaths = pathIdentifier.getCriticalPaths();

        assertTrue(criticalPaths.isEmpty() || criticalPaths.size() == 2,
                "Disconnected graph should either have no valid paths or two separate critical paths");
    }

}
