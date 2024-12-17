package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class USEI23 {
    private ActivitiesGraph graph;
    private BottleneckIdentifier bottleneckIdentifier;

    @BeforeEach
    void setUp() {
        graph = new ActivitiesGraph();

        Activity activityA = new Activity("1", "Activity A", 5, "days", 100.0, "USD", List.of());
        Activity activityB = new Activity("2", "Activity B", 3, "days", 50.0, "USD", List.of("1"));
        Activity activityC = new Activity("3", "Activity C", 4, "days", 75.0, "USD", List.of("1"));
        Activity activityD = new Activity("4", "Activity D", 2, "days", 30.0, "USD", List.of("2", "3"));
        Activity activityE = new Activity("5", "Activity E", 6, "days", 120.0, "USD", List.of("4"));

        graph.addActivity(activityA);
        graph.addActivity(activityB);
        graph.addActivity(activityC);
        graph.addActivity(activityD);
        graph.addActivity(activityE);

        graph.addDependency(activityA, activityB); // A -> B
        graph.addDependency(activityA, activityC); // A -> C
        graph.addDependency(activityB, activityD); // B -> D
        graph.addDependency(activityC, activityD); // C -> D
        graph.addDependency(activityD, activityE); // D -> E

        Repositories.getInstance().setActivitiesGraph(graph);

        bottleneckIdentifier = new BottleneckIdentifier();
    }

    @Test
    void testIdentifyBottleneckActivities() {
        bottleneckIdentifier.identifyBottleneckActivities();

        assertEquals(5, graph.getGraph().vertices().size(), "O grafo deve conter 5 vértices.");
    }

    @Test
    void testPathComplexityCalculation() {
        Activity startActivity = graph.getGraph().vertices().stream()
                .filter(a -> a.getId().equals("1"))
                .findFirst()
                .orElseThrow();

        int pathComplexity = calculatePathComplexity(startActivity);
        assertEquals(7, pathComplexity, "A complexidade dos caminhos a partir de A deve ser 5.");
    }

    @Test
    void testTopologicalSortOrder() {
        ActivityTopologicalSort a = new ActivityTopologicalSort();
        a.setGraph(graph);
        List<Activity> sortedOrder = a.performTopologicalSort();

        List<String> expectedOrder = Arrays.asList("1", "2", "3", "4", "5");
        List<String> actualOrder = new ArrayList<>();
        for (Activity activity : sortedOrder) {
            actualOrder.add(activity.getId());
        }

        assertEquals(expectedOrder, actualOrder, "A ordem topológica deve seguir a sequência correta.");
    }

    @Test
    void testEvaluateBottleneckImpact() {
        String lowRisk = invokeEvaluateBottleneckImpact(2, 2);  // total = 4
        String mediumRisk = invokeEvaluateBottleneckImpact(3, 3); // total = 6
        String highRisk = invokeEvaluateBottleneckImpact(6, 6);  // total = 12

        assertEquals("Low Risk", lowRisk, "Impacto deve ser 'Low Risk' para valores baixos.");
        assertEquals("Medium Risk", mediumRisk, "Impacto deve ser 'Medium Risk' para valores médios.");
        assertEquals("High Risk", highRisk, "Impacto deve ser 'High Risk' para valores altos.");
    }

    private int calculatePathComplexity(Activity activity) {
        try {
            var method = BottleneckIdentifier.class.getDeclaredMethod("calculatePathComplexity", Activity.class);
            method.setAccessible(true);
            return (int) method.invoke(bottleneckIdentifier, activity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String invokeEvaluateBottleneckImpact(int dependencyCount, int pathComplexity) {
        try {
            var method = BottleneckIdentifier.class.getDeclaredMethod("evaluateBottleneckImpact", int.class, int.class);
            method.setAccessible(true);
            return (String) method.invoke(bottleneckIdentifier, dependencyCount, pathComplexity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
