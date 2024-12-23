package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class USEI19 {

    @BeforeEach
    public void setUp() {
        Repositories.getInstance().getActivitiesGraph().clearGraph();
    }


    // Successfully sort activities in correct topological order for DAG
    @Test
    public void test_topological_sort_simple_dag() {
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity a1 = new Activity("1", "Task 1", 1, "days", 100.0, "USD", new ArrayList<>());
        Activity a2 = new Activity("2", "Task 2", 2, "days", 200.0, "USD", Arrays.asList("1"));
        Activity a3 = new Activity("3", "Task 3", 3, "days", 300.0, "USD", Arrays.asList("2"));

        graph.addActivity(a1);
        graph.addActivity(a2);
        graph.addActivity(a3);
        graph.addDependency(a1, a2);
        graph.addDependency(a2, a3);

        sorter.setGraph(graph);

        List<Activity> result = sorter.performTopologicalSort();

        assertEquals(3, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("2", result.get(1).getId());
        assertEquals("3", result.get(2).getId());
    }

    // Detect and throw IllegalStateException for graph with cycles
    @Test
    public void test_detect_circular_dependencies() {
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity a1 = new Activity("1", "Task 1", 1, "days", 100.0, "USD", new ArrayList<>());
        Activity a2 = new Activity("2", "Task 2", 2, "days", 200.0, "USD", Arrays.asList("1"));

        graph.addActivity(a1);
        graph.addActivity(a2);
        graph.addDependency(a1, a2);
        graph.addDependency(a2, a1);

        sorter.setGraph(graph);

        assertThrows(IllegalStateException.class, () -> sorter.performTopologicalSort());
    }

    // Handle graph with single vertex and no edges
    @Test
    public void test_single_vertex_graph() {
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity single = new Activity("1", "Single Task", 1, "days", 100.0, "USD", new ArrayList<>());
        graph.addActivity(single);

        sorter.setGraph(graph);
        List<Activity> result = sorter.performTopologicalSort();

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Single Task", result.get(0).getName());
    }

    // Handle graph with all vertices having dependencies
    @Test
    public void test_all_vertices_with_dependencies() {
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity root = new Activity("1", "Root", 1, "days", 100.0, "USD", new ArrayList<>());
        Activity dep1 = new Activity("2", "Dep1", 2, "days", 200.0, "USD", Arrays.asList("1"));
        Activity dep2 = new Activity("3", "Dep2", 3, "days", 300.0, "USD", Arrays.asList("1"));
        Activity finalTask = new Activity("4", "Final", 4, "days", 400.0, "USD", Arrays.asList("2", "3"));

        graph.addActivity(root);
        graph.addActivity(dep1);
        graph.addActivity(dep2);
        graph.addActivity(finalTask);

        graph.addDependency(root, dep1);
        graph.addDependency(root, dep2);
        graph.addDependency(dep1, finalTask);
        graph.addDependency(dep2, finalTask);

        sorter.setGraph(graph);
        List<Activity> result = sorter.performTopologicalSort();

        assertEquals(4, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("4", result.get(3).getId());
    }

    /**
     * Test with a simple Directed Acyclic Graph (DAG) where activities are added in random order.
     */
    @Test
    public void test_topological_sort_with_disordered_input() {
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        ActivitiesGraph graph = new ActivitiesGraph();

        Activity a3 = new Activity("3", "Task 3", 3, "days", 300.0, "USD", Arrays.asList("2"));
        Activity a1 = new Activity("1", "Task 1", 1, "days", 100.0, "USD", new ArrayList<>());
        Activity a2 = new Activity("2", "Task 2", 2, "days", 200.0, "USD", Arrays.asList("1"));

        // Adiciona as atividades em ordem desordenada
        graph.addActivity(a3);
        graph.addActivity(a1);
        graph.addActivity(a2);

        // Adiciona dependências
        graph.addDependency(a1, a2);
        graph.addDependency(a2, a3);

        sorter.setGraph(graph);
        List<Activity> result = sorter.performTopologicalSort();

        // Verifica a ordem correta após a ordenação topológica
        assertEquals(3, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("2", result.get(1).getId());
        assertEquals("3", result.get(2).getId());
    }

    /**
     * Test with a complex DAG with multiple levels of dependencies and random order of addition.
     */
    @Test
    public void test_topological_sort_complex_graph() {
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        ActivitiesGraph graph = new ActivitiesGraph();

        // Define atividades
        Activity a6 = new Activity("6", "Task 6", 1, "days", 600.0, "USD", Arrays.asList("4", "5"));
        Activity a5 = new Activity("5", "Task 5", 3, "days", 500.0, "USD", Arrays.asList("3"));
        Activity a4 = new Activity("4", "Task 4", 2, "days", 400.0, "USD", Arrays.asList("2"));
        Activity a3 = new Activity("3", "Task 3", 2, "days", 300.0, "USD", Arrays.asList("2"));
        Activity a1 = new Activity("1", "Task 1", 1, "days", 100.0, "USD", new ArrayList<>());
        Activity a2 = new Activity("2", "Task 2", 2, "days", 200.0, "USD", Arrays.asList("1"));

        // Adiciona atividades desordenadas
        graph.addActivity(a6);
        graph.addActivity(a5);
        graph.addActivity(a4);
        graph.addActivity(a3);
        graph.addActivity(a1);
        graph.addActivity(a2);

        // Adiciona dependências
        graph.addDependency(a1, a2);
        graph.addDependency(a2, a3);
        graph.addDependency(a2, a4);
        graph.addDependency(a3, a5);
        graph.addDependency(a4, a6);
        graph.addDependency(a5, a6);

        sorter.setGraph(graph);
        List<Activity> result = sorter.performTopologicalSort();

        // Verifica tamanho da lista ordenada
        assertEquals(6, result.size());

        // Verifica ordem relativa entre nós
        assertTrue(result.indexOf(a1) < result.indexOf(a2));
        assertTrue(result.indexOf(a2) < result.indexOf(a3));
        assertTrue(result.indexOf(a2) < result.indexOf(a4));
        assertTrue(result.indexOf(a3) < result.indexOf(a5));
        assertTrue(result.indexOf(a4) < result.indexOf(a6));
        assertTrue(result.indexOf(a5) < result.indexOf(a6));
    }

    /**
     * Test a graph with a large number of activities.
     */
    @Test
    public void test_large_graph() {
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        ActivitiesGraph graph = new ActivitiesGraph();

        // Adiciona 100 atividades conectadas sequencialmente
        Activity previous = null;
        for (int i = 1; i <= 100; i++) {
            Activity current = new Activity(
                    String.valueOf(i),
                    "Task " + i,
                    i,
                    "days",
                    100.0 * i,
                    "USD",
                    previous != null ? List.of(previous.getId()) : new ArrayList<>()
            );
            graph.addActivity(current);
            if (previous != null) {
                graph.addDependency(previous, current);
            }
            previous = current;
        }

        sorter.setGraph(graph);
        List<Activity> result = sorter.performTopologicalSort();

        // Verifica o número total de atividades ordenadas
        assertEquals(100, result.size());

        // Verifica ordem crescente
        for (int i = 0; i < 99; i++) {
            assertTrue(Integer.parseInt(result.get(i).getId()) < Integer.parseInt(result.get(i + 1).getId()));
        }
    }

    /**
     * Test with a graph that has multiple roots (activities with no dependencies).
     */
    @Test
    public void test_graph_with_multiple_roots() {
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        ActivitiesGraph graph = new ActivitiesGraph();

        // Adiciona atividades sem dependências
        Activity root1 = new Activity("1", "Root 1", 1, "days", 100.0, "USD", new ArrayList<>());
        Activity root2 = new Activity("2", "Root 2", 2, "days", 200.0, "USD", new ArrayList<>());

        // Adiciona atividades com dependências nos "roots"
        Activity child1 = new Activity("3", "Child 1", 3, "days", 300.0, "USD", Arrays.asList("1"));
        Activity child2 = new Activity("4", "Child 2", 4, "days", 400.0, "USD", Arrays.asList("2"));

        graph.addActivity(root1);
        graph.addActivity(root2);
        graph.addActivity(child1);
        graph.addActivity(child2);

        graph.addDependency(root1, child1);
        graph.addDependency(root2, child2);

        sorter.setGraph(graph);
        List<Activity> result = sorter.performTopologicalSort();

        // Verifica o tamanho da ordenação
        assertEquals(4, result.size());

        // Verifica ordem relativa
        assertTrue(result.indexOf(root1) < result.indexOf(child1));
        assertTrue(result.indexOf(root2) < result.indexOf(child2));
    }

    /**
     * Testa a ordenação topológica de um grafo criado a partir do ficheiro de atividades.
     */
    @Test
    public void test_topological_sort_with_file_input() throws IOException {
        // Carrega atividades do ficheiro
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        sorter.setGraph(graph);

        // Executa a ordenação topológica
        List<Activity> result = sorter.performTopologicalSort();

        // Verifica a ordem correta dos nós no grafo
        assertNotNull(result);
        assertEquals(16, result.size()); // Deve conter 16 atividades
        assertEquals("A1", result.get(0).getId()); // A1 é o root
        assertEquals("A16", result.get(15).getId()); // A16 é a última
    }

    /**
     * Testa o grafo para verificar que atividades intermediárias aparecem na ordem correta.
     */
    @Test
    public void test_intermediate_nodes_order() throws IOException {
        // Carrega atividades do ficheiro
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        sorter.setGraph(graph);

        // Ordenação topológica
        List<Activity> result = sorter.performTopologicalSort();

        // Verifica dependências intermediárias
        int indexA4 = result.indexOf(graph.findActivityById("A4"));
        int indexA5 = result.indexOf(graph.findActivityById("A5"));
        int indexA6 = result.indexOf(graph.findActivityById("A6"));
        int indexA13 = result.indexOf(graph.findActivityById("A13"));

        assertTrue(indexA4 < indexA5); // A4 deve vir antes de A5
        assertTrue(indexA4 < indexA6); // A4 deve vir antes de A6
        assertTrue(indexA5 < indexA13); // A5 deve vir antes de A13
        assertTrue(indexA6 < indexA13); // A6 deve vir antes de A13
    }

    /**
     * Testa o comportamento do grafo carregado ao verificar dependências de nós.
     */
    @Test
    public void test_node_dependencies() throws IOException {
        // Carrega atividades do ficheiro
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Verifica dependências de atividades específicas
        Activity a4 = graph.findActivityById("A4");
        Activity a5 = graph.findActivityById("A5");
        Activity a13 = graph.findActivityById("A13");

        assertNotNull(a4);
        assertEquals(List.of("A2", "A3"), a4.getDependencies()); // A4 depende de A2 e A3

        assertNotNull(a5);
        assertEquals(List.of("A4"), a5.getDependencies()); // A5 depende de A4

        assertNotNull(a13);
        assertEquals(List.of("A12", "A9"), a13.getDependencies()); // A13 depende de A12 e A9
    }

    /**
     * Testa se o grafo detecta corretamente a ausência de ciclos (grafo válido).
     */
    @Test
    public void test_no_cycles_in_graph() throws IOException {
        // Carrega atividades do ficheiro
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Verifica se o grafo não tem ciclos
        assertFalse(graph.detectCircularDependencies(), "O grafo não deve conter ciclos.");
    }

    /**
     * Testa a integridade do grafo após a criação a partir do ficheiro.
     */
    @Test
    public void test_graph_integrity() throws IOException, IOException {
        // Carrega atividades do ficheiro
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Verifica o número total de nós no grafo
        assertEquals(16, graph.numVertices(), "O grafo deve conter 16 atividades.");

        // Verifica se todas as atividades esperadas estão no grafo
        for (int i = 1; i <= 16; i++) {
            String activityId = "A" + i;
            assertNotNull(graph.findActivityById(activityId), "Atividade " + activityId + " deve existir no grafo.");
        }
    }

    /**
     * Testa a ordenação topológica completa garantindo que todas as dependências
     * aparecem antes dos seus vértices dependentes.
     */
    @Test
    public void test_full_topological_sort_order_all_vertices() throws IOException {
        // Carrega atividades do ficheiro
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
        ActivityTopologicalSort sorter = new ActivityTopologicalSort();
        sorter.setGraph(graph);

        // Executa a ordenação topológica
        List<Activity> result = sorter.performTopologicalSort();

        // Verifica que todas as atividades foram ordenadas
        assertEquals(16, result.size(), "O grafo deve conter 16 atividades ordenadas.");

        // Verifica que para cada atividade, todas as suas dependências aparecem antes dela
        for (Activity activity : result) {
            List<String> dependencies = activity.getDependencies();
            for (String depId : dependencies) {
                Activity dependency = graph.findActivityById(depId);
                assertNotNull(dependency, String.format("A dependência %s deve existir no grafo.", depId));
                assertTrue(result.indexOf(dependency) < result.indexOf(activity),
                        String.format("Atividade %s deve preceder %s na ordenação.", depId, activity.getId()));
            }
        }
    }

}
