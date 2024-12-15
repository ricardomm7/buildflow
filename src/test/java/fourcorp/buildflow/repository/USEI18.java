package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Activity;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class USEI18 {

    @Test
    void testDetectCircularDependencies_NoCycle() {
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity a1 = new Activity("A1", "Start", 5, "days", 1000, "USD", List.of());
        Activity a2 = new Activity("A2", "Middle", 3, "days", 2000, "USD", List.of("A1"));
        Activity a3 = new Activity("A3", "End", 4, "days", 3000, "USD", List.of("A2"));

        graph.addActivity(a1);
        graph.addActivity(a2);
        graph.addActivity(a3);

        graph.addDependency(a1, a2);
        graph.addDependency(a2, a3);

        Repositories.getInstance().setActivitiesGraph(graph);

        // No cycle expected
        boolean hasCycle = graph.detectCircularDependencies();
        assertFalse(hasCycle, "Graph should not have a cycle.");
    }

    @Test
    void testDetectCircularDependencies_WithCycle() {
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity a1 = new Activity("A1", "Start", 5, "days", 1000, "USD", List.of());
        Activity a2 = new Activity("A2", "Middle", 3, "days", 2000, "USD", List.of("A1"));
        Activity a3 = new Activity("A3", "End", 4, "days", 3000, "USD", List.of("A2"));

        graph.addActivity(a1);
        graph.addActivity(a2);
        graph.addActivity(a3);

        graph.addDependency(a1, a2);
        graph.addDependency(a2, a3);
        graph.addDependency(a3, a1); // Creates a cycle

        Repositories.getInstance().setActivitiesGraph(graph);

        // Cycle expected
        boolean hasCycle = graph.detectCircularDependencies();
        assertTrue(hasCycle, "Graph should have a cycle.");
    }

    @Test
    void testColorDFS_NoCycle() {
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity a1 = new Activity("A1", "Start", 5, "days", 1000, "USD", List.of());
        Activity a2 = new Activity("A2", "Middle", 3, "days", 2000, "USD", List.of("A1"));
        Activity a3 = new Activity("A3", "End", 4, "days", 3000, "USD", List.of("A2"));

        graph.addActivity(a1);
        graph.addActivity(a2);
        graph.addActivity(a3);

        graph.addDependency(a1, a2);
        graph.addDependency(a2, a3);

        Repositories.getInstance().setActivitiesGraph(graph);

        Map<Activity, Integer> color = new HashMap<>();
        for (Activity activity : graph.getGraph().vertices()) {
            color.put(activity, 0); // Initialize as white
        }

        // Start DFS from A1
        boolean cycleDetected = graph.colorDFS(a1, color);
        assertFalse(cycleDetected, "DFS should not detect a cycle.");
    }

    @Test
    void testColorDFS_WithCycle() {
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity a1 = new Activity("A1", "Start", 5, "days", 1000, "USD", List.of());
        Activity a2 = new Activity("A2", "Middle", 3, "days", 2000, "USD", List.of("A1"));
        Activity a3 = new Activity("A3", "End", 4, "days", 3000, "USD", List.of("A2"));

        graph.addActivity(a1);
        graph.addActivity(a2);
        graph.addActivity(a3);

        graph.addDependency(a1, a2);
        graph.addDependency(a2, a3);
        graph.addDependency(a3, a1); // Creates a cycle

        Repositories.getInstance().setActivitiesGraph(graph);

        Map<Activity, Integer> color = new HashMap<>();
        for (Activity activity : graph.getGraph().vertices()) {
            color.put(activity, 0); // Initialize as white
        }

        // Start DFS from A1
        boolean cycleDetected = graph.colorDFS(a1, color);
        assertTrue(cycleDetected, "DFS should detect a cycle.");
    }
}
