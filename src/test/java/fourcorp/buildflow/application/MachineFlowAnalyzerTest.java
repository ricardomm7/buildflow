package fourcorp.buildflow.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MachineFlowAnalyzerTest {
    private final String operationsFile1 = "textFiles/small_articles.csv";   // Primeiro conjunto de dados de operações
    private final String workstationsFile1 = "textFiles/small_workstations.csv"; // Primeiro conjunto de dados de estações de trabalho
    private final String operationsFile2 = "textFiles/medium_articles.csv";  // Segundo conjunto de dados de operações
    private final String workstationsFile2 = "textFiles/medium_workstations.csv"; // Segundo conjunto de dados de estações de trabalho

    private MachineFlowAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new MachineFlowAnalyzer();
    }

    @Test
    @DisplayName("Test dependencies for first dataset")
    void testDependenciesForFirstDataset() throws IOException {
        // Executar a simulação e imprimir as dependências
        runSimulationAndPrintDependencies(operationsFile1, workstationsFile1);
    }

    @Test
    @DisplayName("Test dependencies for second dataset")
    void testDependenciesForSecondDataset() throws IOException {
        // Executar a simulação e imprimir as dependências
        runSimulationAndPrintDependencies(operationsFile2, workstationsFile2);
    }

    private void runSimulationAndPrintDependencies(String operationsFilePath, String workstationsFilePath) throws IOException {
        // Carregar os arquivos CSV
        Reader.loadOperations(operationsFilePath);
        Reader.loadMachines(workstationsFilePath);

        // Inicializa o simulador e executa a simulação
        Simulator simulator = new Simulator();
        simulator.runWithoutPriority(true);

        // Obter dependências das máquinas após a simulação
        Map<String, Map<String, Integer>> dependencies = MachineFlowAnalyzer.getMachineDependencies();

        // Imprimir as dependências no console
        System.out.println("Dependências entre máquinas:");
        dependencies.forEach((machine, flows) -> {
            System.out.println("Máquina " + machine + " depende de:");
            flows.forEach((dependentMachine, timesUsed) ->
                    System.out.println(" - Máquina " + dependentMachine + ": usada " + timesUsed + " vezes.")
            );
        });

        // Verificar que existem dependências
        assertFalse(dependencies.isEmpty(), "As dependências entre as máquinas devem existir.");
    }

    @Test
    @DisplayName("Test empty product machine flows")
    void testEmptyProductMachineFlows() {
        MachineFlowAnalyzer analyzer = new MachineFlowAnalyzer();
        Map<String, List<String>> emptyFlows = new HashMap<>();
        analyzer.calculateMachineDependencies(emptyFlows);

        Map<String, Map<String, Integer>> dependencies = MachineFlowAnalyzer.getMachineDependencies();
        assertTrue(dependencies.isEmpty());
    }

    @Test
    @DisplayName("Test single machine flow")
    void testSingleMachineFlow() {
        MachineFlowAnalyzer analyzer = new MachineFlowAnalyzer();
        Map<String, List<String>> singleFlow = new HashMap<>();
        singleFlow.put("P1", List.of("WS1"));

        analyzer.calculateMachineDependencies(singleFlow);
        Map<String, Map<String, Integer>> dependencies = MachineFlowAnalyzer.getMachineDependencies();
        assertTrue(dependencies.isEmpty());
    }

    @Test
    @DisplayName("Test multiple identical flows")
    void testMultipleIdenticalFlows() {
        MachineFlowAnalyzer analyzer = new MachineFlowAnalyzer();
        Map<String, List<String>> flows = new HashMap<>();
        flows.put("P1", Arrays.asList("WS1", "WS2"));
        flows.put("P2", Arrays.asList("WS1", "WS2"));
        flows.put("P3", Arrays.asList("WS1", "WS2"));

        analyzer.calculateMachineDependencies(flows);
        Map<String, Map<String, Integer>> dependencies = MachineFlowAnalyzer.getMachineDependencies();

        assertEquals(1, dependencies.size());
        assertEquals(3, dependencies.get("WS1").get("WS2").intValue());
    }

    @Test
    @DisplayName("Test clear dependencies")
    void testClearDependencies() {
        MachineFlowAnalyzer analyzer = new MachineFlowAnalyzer();
        Map<String, List<String>> flows = new HashMap<>();
        flows.put("P1", Arrays.asList("WS1", "WS2", "WS3"));

        analyzer.calculateMachineDependencies(flows);
        assertFalse(MachineFlowAnalyzer.getMachineDependencies().isEmpty());

        MachineFlowAnalyzer.clearDependencies();
        assertTrue(MachineFlowAnalyzer.getMachineDependencies().isEmpty());
    }

    @Test
    @DisplayName("Test circular machine flow")
    void testCircularMachineFlow() {
        MachineFlowAnalyzer analyzer = new MachineFlowAnalyzer();
        Map<String, List<String>> flows = new HashMap<>();
        flows.put("P1", Arrays.asList("WS1", "WS2", "WS3", "WS1"));

        analyzer.calculateMachineDependencies(flows);
        Map<String, Map<String, Integer>> dependencies = MachineFlowAnalyzer.getMachineDependencies();

        assertEquals(1, dependencies.get("WS1").get("WS2").intValue());
        assertEquals(1, dependencies.get("WS2").get("WS3").intValue());
        assertEquals(1, dependencies.get("WS3").get("WS1").intValue());
    }

    @Test
    @DisplayName("Test overlapping machine flows")
    void testOverlappingMachineFlows() {
        MachineFlowAnalyzer analyzer = new MachineFlowAnalyzer();
        Map<String, List<String>> flows = new HashMap<>();
        flows.put("P1", Arrays.asList("WS1", "WS2", "WS3"));
        flows.put("P2", Arrays.asList("WS2", "WS3", "WS4"));

        analyzer.calculateMachineDependencies(flows);
        Map<String, Map<String, Integer>> dependencies = MachineFlowAnalyzer.getMachineDependencies();

        assertEquals(1, dependencies.get("WS1").get("WS2").intValue());
        assertEquals(2, dependencies.get("WS2").get("WS3").intValue());
        assertEquals(1, dependencies.get("WS3").get("WS4").intValue());
    }

    @Test
    public void testPrintMachineFlowDependencies() {
        // Simula um fluxo e calcula dependências
        Map<String, List<String>> productMachineFlows = new HashMap<>();

        List<String> flow1 = new ArrayList<>();
        flow1.add("WS1");
        flow1.add("WS2");
        productMachineFlows.put("P1", flow1);

        analyzer.calculateMachineDependencies(productMachineFlows);

        // Garante que o método de impressão não jogue exceção
        assertDoesNotThrow(MachineFlowAnalyzer::printMachineFlowDependencies);
    }

    @Test
    public void testClearDependencies2() {
        Map<String, List<String>> productMachineFlows = new HashMap<>();

        List<String> flow1 = new ArrayList<>();
        flow1.add("WS1");
        flow1.add("WS2");
        productMachineFlows.put("P1", flow1);

        analyzer.calculateMachineDependencies(productMachineFlows);

        MachineFlowAnalyzer.clearDependencies();
        Map<String, Map<String, Integer>> dependencies = MachineFlowAnalyzer.getMachineDependencies();

        assertTrue(dependencies.isEmpty(), "As dependências devem estar vazias após chamar clearDependencies().");
    }
}

