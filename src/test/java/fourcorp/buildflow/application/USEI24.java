package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class USEI24 {

    private ProjectDelaySimulator projectDelaySimulator;

    // Simulate project delays with valid delay map and verify new project duration
    @Test
    public void test_simulate_delays_with_valid_map() {
        projectDelaySimulator = new ProjectDelaySimulator();

        Activity activity1 = new Activity("A1", "Task 1", 5, "days", 100.0, "USD", new ArrayList<>());
        Activity activity2 = new Activity("A2", "Task 2", 3, "days", 200.0, "USD", List.of("A1"));

        ActivitiesGraph graph = new ActivitiesGraph();
        graph.addActivity(activity1);
        graph.addActivity(activity2);

        ProjectDelaySimulator simulator = new ProjectDelaySimulator();
        simulator.setGraph(graph);

        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A1", 2);
        delayMap.put("A2", 1);

        simulator.simulateProjectDelays(delayMap);

        assertEquals(7, activity1.getDuration());
        assertEquals(4, activity2.getDuration());
    }


    @Test
    void test_simulate_delays_with_empty_map() {
        projectDelaySimulator = new ProjectDelaySimulator();

        Activity activity1 = new Activity("A1", "Task 1", 5, "days", 100.0, "USD", new ArrayList<>());
        Activity activity2 = new Activity("A2", "Task 2", 3, "days", 200.0, "USD", List.of("A1"));

        ActivitiesGraph graph = new ActivitiesGraph();
        graph.addActivity(activity1);
        graph.addActivity(activity2);

        ProjectDelaySimulator simulator = new ProjectDelaySimulator();
        simulator.setGraph(graph);

        Map<String, Integer> emptyDelayMap = new HashMap<>();
        simulator.simulateProjectDelays(emptyDelayMap);

        assertEquals(5, activity1.getDuration());
        assertEquals(3, activity2.getDuration());
    }

    @Test
    void test_simulate_delays_with_negative_delays() {
        projectDelaySimulator = new ProjectDelaySimulator();

        Activity activity1 = new Activity("A1", "Task 1", 5, "days", 100.0, "USD", new ArrayList<>());

        ActivitiesGraph graph = new ActivitiesGraph();
        graph.addActivity(activity1);

        ProjectDelaySimulator simulator = new ProjectDelaySimulator();
        simulator.setGraph(graph);

        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A1", -2);

        simulator.simulateProjectDelays(delayMap);
        assertEquals(5, activity1.getDuration());
    }

    @Test
    void test_find_activity_by_id_existing() {
        ActivitiesGraph graph = new ActivitiesGraph();

        projectDelaySimulator = new ProjectDelaySimulator();
        projectDelaySimulator.setGraph(graph);

        // Configuração do grafo
        Activity activity1 = new Activity("A1", "Task 1", 5, "days", 100.0, "USD", new ArrayList<>());
        graph.addActivity(activity1);

        // Atribuir o grafo ao simulador
        projectDelaySimulator = new ProjectDelaySimulator();
        projectDelaySimulator.setGraph(graph);

        // Testa a busca pela atividade
        Activity found = projectDelaySimulator.findActivityById("A1");
        assertNotNull(found, "Activity A1 should exist in the graph");
        assertEquals("A1", found.getId());
    }


    @Test
    void test_find_activity_by_id_non_existing() {
        Activity activity1 = new Activity("A1", "Task 1", 5, "days", 100.0, "USD", new ArrayList<>());

        ActivitiesGraph graph = new ActivitiesGraph();
        graph.addActivity(activity1);

        ProjectDelaySimulator simulator = new ProjectDelaySimulator();
        simulator.setGraph(graph);

        Activity notFound = simulator.findActivityById("NonExistent");
        assertNull(notFound);
    }

    @Test
    void test_simulate_delays_with_multiple_activities_chain() {
        Activity activity1 = new Activity("A1", "Task 1", 3, "days", 100.0, "USD", new ArrayList<>());
        Activity activity2 = new Activity("A2", "Task 2", 4, "days", 200.0, "USD", List.of("A1"));
        Activity activity3 = new Activity("A3", "Task 3", 2, "days", 300.0, "USD", List.of("A2"));

        ActivitiesGraph graph = new ActivitiesGraph();
        graph.addActivity(activity1);
        graph.addActivity(activity2);
        graph.addActivity(activity3);

        ProjectDelaySimulator simulator = new ProjectDelaySimulator();
        simulator.setGraph(graph);

        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A1", 2);
        delayMap.put("A2", 1);
        delayMap.put("A3", 3);

        simulator.simulateProjectDelays(delayMap);

        assertEquals(5, activity1.getDuration());
        assertEquals(5, activity2.getDuration());
        assertEquals(5, activity3.getDuration());
    }

    // Cenário: Simular delays em atividades sem predecessores
    @Test
    public void test_delay_on_activity_without_predecessors() {
        Activity activity1 = new Activity("A1", "Task 1", 5, "days", 100.0, "USD", new ArrayList<>());

        ActivitiesGraph graph = new ActivitiesGraph();
        graph.addActivity(activity1);

        ProjectDelaySimulator simulator = new ProjectDelaySimulator();
        simulator.setGraph(graph);

        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A1", 3); // Delay na atividade sem predecessores

        simulator.simulateProjectDelays(delayMap);

        assertEquals(8, activity1.getDuration(), "A1 duration should be updated to 8.");
        assertEquals(8, simulator.getNewProjectDuration(), "Project duration should match the delay.");
    }

    // Cenário: Simular delays em atividades sem sucessores
    @Test
    public void test_delay_on_activity_without_successors() {
        Activity activity1 = new Activity("A1", "Task 1", 5, "days", 100.0, "USD", new ArrayList<>());
        Activity activity2 = new Activity("A2", "Task 2", 4, "days", 200.0, "USD", List.of("A1"));

        ActivitiesGraph graph = new ActivitiesGraph();
        graph.addActivity(activity1);
        graph.addActivity(activity2);

        ProjectDelaySimulator simulator = new ProjectDelaySimulator();
        simulator.setGraph(graph);

        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A2", 3); // Delay em atividade sem sucessores

        simulator.simulateProjectDelays(delayMap);

        assertEquals(7, activity2.getDuration(), "A2 duration should be updated to 7.");
        assertEquals(12, simulator.getNewProjectDuration(), "Project duration should match the longest path.");
    }

    // Cenário: Validar Critical Path após múltiplos delays
    @Test
    public void test_critical_path_after_multiple_delays() {
        Activity activity1 = new Activity("A1", "Task 1", 5, "days", 100.0, "USD", new ArrayList<>());
        Activity activity2 = new Activity("A2", "Task 2", 6, "days", 200.0, "USD", List.of("A1"));
        Activity activity3 = new Activity("A3", "Task 3", 4, "days", 300.0, "USD", List.of("A2"));
        Activity activity4 = new Activity("A4", "Task 4", 3, "days", 300.0, "USD", List.of("A2"));

        ActivitiesGraph graph = new ActivitiesGraph();
        graph.addActivity(activity1);
        graph.addActivity(activity2);
        graph.addActivity(activity3);
        graph.addActivity(activity4);

        ProjectDelaySimulator simulator = new ProjectDelaySimulator();
        simulator.setGraph(graph);

        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A3", 2); // Atraso em A3
        delayMap.put("A1", 3); // Atraso em A1

        simulator.simulateProjectDelays(delayMap);

        // Validar novas durações
        assertEquals(8, activity1.getDuration(), "A1 duration should be updated to 8.");
        assertEquals(4 + 2, activity3.getDuration(), "A3 duration should be updated to 6.");

        // Validar duração do projeto e critical path
        assertEquals(17, simulator.getNewProjectDuration(), "Project duration should be updated to 17.");
    }

    // Cenário: Validar comportamento com delays inválidos
    @Test
    public void test_invalid_delays() {
        Activity activity1 = new Activity("A1", "Start", 5, "days", 100.0, "USD", new ArrayList<>());

        ActivitiesGraph graph = new ActivitiesGraph();
        graph.addActivity(activity1);

        ProjectDelaySimulator simulator = new ProjectDelaySimulator();
        simulator.setGraph(graph);

        // Delays inválidos
        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A1", -10); // Delay negativo excessivo

        simulator.simulateProjectDelays(delayMap);

        // Durations não devem ser negativas
        assertEquals(5, activity1.getDuration(), "Activity duration should not be negative.");
    }

    @Test
    public void test_small_delay_single_activity() throws IOException {
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ProjectDelaySimulator simulator = new ProjectDelaySimulator();

        // Delay de 2 unidades para uma única atividade
        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A1", 2);

        simulator.simulateProjectDelays(delayMap);

        // Validar nova duração do projeto
        assertEquals(70, simulator.getNewProjectDuration(), "New project duration should reflect a small delay.");

        // Verificar mudanças no caminho crítico
        List<Activity> newCriticalPath = simulator.getNewCriticalPath();
        assertEquals(10, newCriticalPath.size(), "Critical path should not change significantly for a small delay.");
    }

    @Test
    public void test_delay_in_intermediate_activity() throws IOException {
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ProjectDelaySimulator simulator = new ProjectDelaySimulator();

        // Delay numa atividade intermediária que afeta o caminho crítico
        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A3", 5); // Atraso de 5 unidades

        simulator.simulateProjectDelays(delayMap);

        // Validar nova duração do projeto
        assertEquals(71, simulator.getNewProjectDuration(), "Project duration should increase by 5 time units.");

        // Verificar nova duração e caminho crítico
        List<Activity> originalCriticalPath = simulator.getOriginalCriticalPath();
        List<Activity> newCriticalPath = simulator.getNewCriticalPath();

        assertNotEquals(originalCriticalPath, newCriticalPath, "Critical path should change due to the delay.");
    }


    @Test
    public void test_simulate_delays_txt() throws IOException {
        // Load activities from the CSV file
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");

        ProjectDelaySimulator simulator = new ProjectDelaySimulator();

        // Verificar duração do projeto antes do delay
        simulator.simulateProjectDelays(new HashMap<>()); // Sem delays
        int originalDuration = simulator.getOriginalProjectDuration();
        assertEquals(68, originalDuration, "Original project duration should be 68 time units.");

        // Aplicar atrasos e verificar mudanças no caminho crítico
        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A9", 21); // Adiciona 21 unidades de atraso na atividade A9

        simulator.simulateProjectDelays(delayMap);

        // Verificar nova duração do projeto
        int newDuration = simulator.getNewProjectDuration();
        assertEquals(69, newDuration, "New project duration should be 89 time units after delays.");

        // Verificar mudanças no caminho crítico
        List<Activity> originalCriticalPath = simulator.getOriginalCriticalPath();
        assertEquals(10, originalCriticalPath.size(), "Original critical path should have 10 activities.");

        List<Activity> newCriticalPath = simulator.getNewCriticalPath();
        assertEquals(8, newCriticalPath.size(), "New critical path should have 8 activities.");

        // Validar se a atividade atrasada entrou no caminho crítico
        boolean isA9Critical = newCriticalPath.stream().anyMatch(activity -> activity.getId().equals("A9"));
        assertTrue(isA9Critical, "Activity A9 should now be part of the critical path.");
    }

    @Test
    public void test_delay_in_multiple_activities() throws IOException {
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ProjectDelaySimulator simulator = new ProjectDelaySimulator();

        // Atrasos em várias atividades
        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A4", 3);
        delayMap.put("A9", 10);

        simulator.simulateProjectDelays(delayMap);

        // Validar nova duração do projeto
        assertEquals(71, simulator.getNewProjectDuration(), "Project duration should reflect cumulative delays.");

        // Validar se as atividades atrasadas estão no novo caminho crítico
        List<Activity> newCriticalPath = simulator.getNewCriticalPath();
        boolean isA4Critical = newCriticalPath.stream().anyMatch(activity -> activity.getId().equals("A4"));
        boolean isA9Critical = newCriticalPath.stream().anyMatch(activity -> activity.getId().equals("A9"));

        assertTrue(isA4Critical, "Activity A4 should be part of the critical path after delay.");
        assertFalse(isA9Critical, "Activity A9 should not be part of the critical path after delay.");
    }

    @Test
    public void test_large_delay_extreme_scenario() throws IOException {
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ProjectDelaySimulator simulator = new ProjectDelaySimulator();

        // Delay extremo em uma atividade
        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A9", 50);

        simulator.simulateProjectDelays(delayMap);

        // Validar nova duração do projeto
        assertEquals(98, simulator.getNewProjectDuration(), "Project duration should reflect the large delay.");

        // Validar o impacto no caminho crítico
        List<Activity> newCriticalPath = simulator.getNewCriticalPath();
        boolean isA9Critical = newCriticalPath.stream().anyMatch(activity -> activity.getId().equals("A9"));

        assertTrue(isA9Critical, "Activity A9 should dominate the critical path after large delay.");
    }

    @Test
    public void test_combined_delays_complex_scenario() throws IOException {
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ProjectDelaySimulator simulator = new ProjectDelaySimulator();

        // Atrasos em várias atividades
        Map<String, Integer> delayMap = new HashMap<>();
        delayMap.put("A4", 3);
        delayMap.put("A8", 7);
        delayMap.put("A9", 15);
        delayMap.put("A11", 10);

        simulator.simulateProjectDelays(delayMap);

        // Validar nova duração do projeto
        assertEquals(84, simulator.getNewProjectDuration(), "Project duration should reflect cumulative and cascading delays.");

        // Verificar mudanças no caminho crítico
        List<Activity> originalCriticalPath = simulator.getOriginalCriticalPath();
        List<Activity> newCriticalPath = simulator.getNewCriticalPath();

        assertNotEquals(originalCriticalPath, newCriticalPath, "Critical path should change due to multiple delays.");

        // Validar se as atividades atrasadas estão no novo caminho crítico
        boolean isA9Critical = newCriticalPath.stream().anyMatch(activity -> activity.getId().equals("A9"));
        boolean isA11Critical = newCriticalPath.stream().anyMatch(activity -> activity.getId().equals("A11"));

        assertFalse(isA9Critical, "Activity A9 should not be part of the critical path.");
        assertTrue(isA11Critical, "Activity A11 should be part of the critical path.");
    }


}
