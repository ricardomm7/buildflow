package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class USEI18 {

    @Test
    void verifyReading() throws IOException {
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities_test.csv");
        ActivitiesGraph a = Repositories.getInstance().getActivitiesGraph();

        List<Activity> activities = a.getGraph().vertices();

        List<Activity> expectedActivities = List.of(
                new Activity("B1", "Inicio do Projeto", 3, "dias", 1500, "USD", List.of()),
                new Activity("B2", "Planejamento", 4, "dias", 2000, "USD", List.of("B1")),
                new Activity("B3", "Análise de Riscos", 5, "dias", 1800, "USD", List.of("B1"))
        );

        for (Activity expected : expectedActivities) {
            boolean found = activities.stream()
                    .anyMatch(activity ->
                            activity.getId().equals(expected.getId()) &&
                                    activity.getName().equals(expected.getName()) &&
                                    activity.getDuration() == expected.getDuration() &&
                                    activity.getDurationUnit().equals(expected.getDurationUnit()) &&
                                    activity.getCost() == expected.getCost() &&
                                    activity.getCostUnit().equals(expected.getCostUnit()) &&
                                    activity.getDependencies().equals(expected.getDependencies())
                    );

            if (!found) {
                throw new AssertionError("Error: Activity " + expected.getId() + " not found or incorrect description.");
            }
        }
        System.out.println("All the activities were uploaded correctly.");
    }

    @Test
    void verifyReadingWithLoop() {
        String filePath = "src/test/java/fourcorp/buildflow/activities_test_with_loop.csv";

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            Reader.loadActivities(filePath);
        });

        assertEquals("Program aborted due to the circular dependency(ies) found.", thrownException.getMessage());
    }
}