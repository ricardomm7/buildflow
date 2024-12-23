package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class USEI17 {

    private static final String FILE_PATH = "src/test/java/fourcorp/buildflow/activities.csv";

    @BeforeEach
    void setup() {
        Repositories.getInstance().getActivitiesGraph().clearGraph();
    }

    /**
     * Verifica se as atividades foram carregadas corretamente do ficheiro.
     */
    @Test
    void testLoadActivitiesSuccess() throws IOException {
        Reader.loadActivities(FILE_PATH);
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Verifica o número total de atividades no grafo
        assertEquals(16, graph.numVertices(), "O grafo deve conter 16 atividades.");
    }

    /**
     * Verifica se as dependências foram adicionadas corretamente.
     */
    @Test
    void testDependenciesAddedCorrectly() throws IOException {
        Reader.loadActivities(FILE_PATH);
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        Activity a4 = graph.findActivityById("A4");
        assertNotNull(a4, "Atividade A4 deve estar no grafo.");

        // Verifica as dependências de A4
        List<Activity> incoming = graph.getIncomingEdges(a4);
        assertEquals(2, incoming.size(), "A4 deve ter 2 dependências.");
        assertTrue(incoming.stream().anyMatch(activity -> activity.getId().equals("A2")), "A4 deve depender de A2.");
        assertTrue(incoming.stream().anyMatch(activity -> activity.getId().equals("A3")), "A4 deve depender de A3.");
    }

    /**
     * Verifica que vértices virtuais não são criados quando há apenas um vértice inicial e final.
     */
    @Test
    void testVirtualVerticesNotAdded() throws IOException {
        // Carrega atividades do ficheiro
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activities.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Verifica que não há vértices virtuais criados
        Activity virtualStart = graph.findActivityById("START");
        Activity virtualEnd = graph.findActivityById("END");

        assertNull(virtualStart, "Nenhum vértice virtual de início deve ser criado.");
        assertNull(virtualEnd, "Nenhum vértice virtual de fim deve ser criado.");
    }


    /**
     * Verifica se o sistema deteta dependências circulares.
     */
    @Test
    void testDetectCircularDependencies() {
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Cria atividades com dependências circulares
        Activity a1 = new Activity("A1", "Task 1", 5, "days", 100.0, "USD", List.of());
        Activity a2 = new Activity("A2", "Task 2", 5, "days", 100.0, "USD", List.of("A1"));
        Activity a3 = new Activity("A3", "Task 3", 5, "days", 100.0, "USD", List.of("A2"));

        graph.addActivity(a1);
        graph.addActivity(a2);
        graph.addActivity(a3);

        graph.addDependency(a1, a2);
        graph.addDependency(a2, a3);
        graph.addDependency(a3, a1); // Cria ciclo

        boolean hasCycle = graph.detectCircularDependencies();
        assertTrue(hasCycle, "O sistema deve detectar dependências circulares.");
    }


    /**
     * Verifica se o sistema lida corretamente com dependências inexistentes.
     */
    @Test
    void testNonexistentDependencies() throws IOException {
        Reader.loadActivities(FILE_PATH);
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Tenta encontrar uma dependência inexistente
        Activity nonexistentDependency = graph.findActivityById("Nonexistent");
        assertNull(nonexistentDependency, "Dependências inexistentes devem retornar null.");
    }

    /**
     * Verifica se a validação de início e fim múltiplos funciona corretamente.
     */
    @Test
    void testValidateStartEndVertices() throws IOException {
        Reader.loadActivities(FILE_PATH);
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Verifica vértices de início e fim
        List<Activity> startVertices = graph.getStartVertices();
        List<Activity> endVertices = graph.getEndVertices();

        assertTrue(startVertices.size() <= 1, "Deve haver no máximo um vértice de início após a validação.");
        assertTrue(endVertices.size() <= 1, "Deve haver no máximo um vértice de fim após a validação.");
    }

    @Test
    public void testNoVirtualVerticesAddedWhenNotNeeded() throws IOException {
        // Carrega o ficheiro original sem múltiplos vértices iniciais ou finais
        Reader.loadActivities(FILE_PATH);
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Verifica que Virtual Start e Virtual End não existem
        assertNull(graph.findActivityById("START"),
                "O Virtual Start não deveria ser adicionado.");
        assertNull(graph.findActivityById("END"),
                "O Virtual End não deveria ser adicionado.");
    }

    @Test
    public void testVirtualStartAdded() throws IOException {
        // Carrega atividades do ficheiro
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activitiesW_Virtual.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Verifica se o Virtual Start foi adicionado
        Activity virtualStart = graph.findActivityById("START");
        assertNotNull(virtualStart, "O Virtual Start deveria ser adicionado ao grafo.");
        assertEquals(0, virtualStart.getDuration(), "A duração do Virtual Start deve ser 0.");

        // Verifica que o Virtual Start não tem dependências (usando getIncomingEdges)
        assertTrue(graph.getIncomingEdges(virtualStart).isEmpty(),
                "O Virtual Start não deve ter dependências.");

        // Verifica se o Virtual Start conecta a todos os nós iniciais
        List<Activity> startVertices = graph.getStartVertices();
        for (Activity activity : startVertices) {
            if (!activity.getId().equals("START")) {
                Activity[] neighbors = graph.getNeighbors(virtualStart);
                boolean isConnected = false;
                for (Activity neighbor : neighbors) {
                    if (neighbor.equals(activity)) {
                        isConnected = true;
                        break;
                    }
                }
                assertTrue(isConnected,
                        String.format("O Virtual Start deveria estar conectado a %s.", activity.getId()));
            }
        }
    }

    @Test
    public void testVirtualEndAdded() throws IOException {
        // Carrega atividades do ficheiro
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activitiesW_Virtual.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Verifica se o Virtual End foi adicionado
        Activity virtualEnd = graph.findActivityById("END");
        assertNotNull(virtualEnd, "O Virtual End deveria ser adicionado ao grafo.");
        assertEquals(0, virtualEnd.getDuration(), "A duração do Virtual End deve ser 0.");

        // Verifica que o Virtual End não tem arestas de saída
        Activity[] neighbors = graph.getNeighbors(virtualEnd);
        assertEquals(0, neighbors.length, "O Virtual End não deve ter arestas de saída.");

        // Verifica se o Virtual End está conectado a todos os nós finais
        List<Activity> endVertices = graph.getEndVertices();
        for (Activity activity : endVertices) {
            if (!activity.getId().equals("END")) {
                List<Activity> incomingToEnd = graph.getIncomingEdges(virtualEnd);
                boolean isConnected = false;
                for (Activity incoming : incomingToEnd) {
                    if (incoming.equals(activity)) {
                        isConnected = true;
                        break;
                    }
                }
                assertTrue(isConnected,
                        String.format("O Virtual End deveria estar conectado a %s.", activity.getId()));
            }
        }
    }


    @Test
    public void testVirtualVerticesIntegration() throws IOException {
        // Carrega atividades do ficheiro
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activitiesW_Virtual.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Verifica que os Virtual Start e Virtual End existem
        Activity virtualStart = graph.findActivityById("START");
        Activity virtualEnd = graph.findActivityById("END");
        assertNotNull(virtualStart, "O Virtual Start deveria existir.");
        assertNotNull(virtualEnd, "O Virtual End deveria existir.");

        // Verifica que o Virtual Start tem arestas de saída
        Activity[] startNeighbors = graph.getNeighbors(virtualStart);
        assertTrue(startNeighbors.length > 0,
                "O Virtual Start deveria ter arestas de saída.");

        // Verifica que o Virtual End tem arestas de entrada
        List<Activity> endIncoming = graph.getIncomingEdges(virtualEnd);
        assertFalse(endIncoming.isEmpty(),
                "O Virtual End deveria ter arestas de entrada.");
    }


    @Test
    void testMultipleInitialVertices() throws IOException {
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activitiesW_Virtual.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Verificar vértices iniciais (sem dependências)
        Activity virtualStart = graph.findActivityById("START");
        assertNotNull(virtualStart, "Virtual START deve ser criado com múltiplos vértices iniciais");

        // Verificar ligações aos vértices iniciais
        Activity[] startNeighbors = graph.getNeighbors(virtualStart);
        List<String> expectedInitials = Arrays.asList("A1", "A3", "A15", "A17", "A19");

        assertEquals(expectedInitials.size(), startNeighbors.length,
                "Virtual START deve estar ligado a todos os vértices iniciais");

        // Verificar que cada vértice inicial está conectado ao START
        for (Activity neighbor : startNeighbors) {
            assertTrue(expectedInitials.contains(neighbor.getId()),
                    "Vértice " + neighbor.getId() + " deve ser um dos vértices iniciais");
        }
    }

    @Test
    void testMultipleFinalVertices() throws IOException {
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activitiesW_Virtual.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        // Verificar vértices finais
        Activity virtualEnd = graph.findActivityById("END");
        assertNotNull(virtualEnd, "Virtual END deve ser criado com múltiplos vértices finais");

        // Verificar ligações dos vértices finais
        List<Activity> incomingToEnd = graph.getIncomingEdges(virtualEnd);
        List<String> expectedFinals = Arrays.asList("A16","A17", "A18", "A19");

        assertEquals(expectedFinals.size(), incomingToEnd.size(),
                "Virtual END deve ter ligações de todos os vértices finais");

        // Verificar que cada vértice final está conectado ao END
        for (Activity incoming : incomingToEnd) {
            assertTrue(expectedFinals.contains(incoming.getId()),
                    "Vértice " + incoming.getId() + " deve ser um dos vértices finais");
        }
    }

    @Test
    void testVirtualVerticesProperties() throws IOException {
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activitiesW_Virtual.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        Activity virtualStart = graph.findActivityById("START");
        Activity virtualEnd = graph.findActivityById("END");

        // Verificar propriedades do START
        assertNotNull(virtualStart);
        assertEquals(0, virtualStart.getDuration(), "Virtual START deve ter duração 0");
        assertTrue(graph.getIncomingEdges(virtualStart).isEmpty(),
                "Virtual START não deve ter arestas de entrada");

        // Verificar propriedades do END
        assertNotNull(virtualEnd);
        assertEquals(0, virtualEnd.getDuration(), "Virtual END deve ter duração 0");
        assertEquals(0, graph.getNeighbors(virtualEnd).length, "Virtual END não deve ter arestas de saída");
    }

    @Test
    void testGraphConnectivity() throws IOException {
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activitiesW_Virtual.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();

        Activity virtualStart = graph.findActivityById("START");
        Activity virtualEnd = graph.findActivityById("END");

        // Verificar que todos os vértices iniciais têm apenas o START como predecessor
        Activity[] startNeighbors = graph.getNeighbors(virtualStart);
        for (Activity activity : startNeighbors) {
            List<Activity> incoming = graph.getIncomingEdges(activity);
            assertEquals(1, incoming.size(),
                    "Vértice inicial " + activity.getId() + " deve ter apenas START como predecessor");
            assertEquals("START", incoming.getFirst().getId(),
                    "O único predecessor deve ser START");
        }

        // Verificar que todos os vértices finais têm apenas o END como sucessor
        List<Activity> incomingToEnd = graph.getIncomingEdges(virtualEnd);
        for (Activity activity : incomingToEnd) {
            Activity[] outgoing = graph.getNeighbors(activity);
            assertEquals(1, outgoing.length,
                    "Vértice final " + activity.getId() + " deve ter apenas END como sucessor");
            assertEquals("END", outgoing[0].getId(),
                    "O único sucessor deve ser END");
        }

    }

    @Test
    void createGraph() throws IOException {
        Reader.loadActivities("src/test/java/fourcorp/buildflow/activitiesW_Virtual.csv");
        ActivitiesGraph graph = Repositories.getInstance().getActivitiesGraph();
        DisplayGraph dp = new DisplayGraph();
        dp.setGraph(graph);
        dp.generateDotFile("outFiles/GraphTest.dot");
        dp.generateSVG("outFiles/GraphTest.dot","outFiles/GraphTest.svg");

    }
}

