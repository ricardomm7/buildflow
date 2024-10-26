package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.WorkstationsPerOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static fourcorp.buildflow.application.MachineFlowAnalyzer.buildDependencies;
import static fourcorp.buildflow.application.MachineFlowAnalyzer.flowDependency;
import static org.junit.jupiter.api.Assertions.*;


class MachineFlowAnalyzerTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private Simulator simulator;
    private MachineFlowAnalyzer machineFlowAnalyzer;

    @BeforeEach
    void setUp() {
        machineFlowAnalyzer = new MachineFlowAnalyzer();
        System.setOut(new PrintStream(outputStreamCaptor)); // Redireciona System.out para capturar a saída
        ProductPriorityLine productLine = new ProductPriorityLine();
        WorkstationsPerOperation workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator(workstationsPerOperation, productLine);
    }

    @AfterEach
    void tearDown() {
        MachineFlowAnalyzer.reset();  // Limpa depois de cada teste
        outputStreamCaptor.reset();   // Limpa a saída capturada para o próximo teste
    }


    @Test
    void testAddFlow() {
        MachineFlowAnalyzer analyzer = new MachineFlowAnalyzer();
        Workstation workstation = new Workstation("WS1", 1);
        Operation operation = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Product product = new Product("P1", List.of(operation, operation2));
        analyzer.addFlow(workstation, product);
        assertNotNull(flowDependency);
    }

    @Test
    void testBuildDependenciesWithSingleProduct() {
        MachineFlowAnalyzer analyzer = new MachineFlowAnalyzer();
        Workstation ws1 = new Workstation("WS1", 1);
        Workstation ws2 = new Workstation("WS2", 1);

        Operation operation = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Product product = new Product("P1", List.of(operation, operation2));

        analyzer.addFlow(ws1, product);
        analyzer.addFlow(ws2, product);

        buildDependencies();
        assertNotNull(MachineFlowAnalyzer.workstationDependencies);
    }


    @Test
    void testResetAnalyzer() {
        MachineFlowAnalyzer analyzer = new MachineFlowAnalyzer();
        Workstation ws1 = new Workstation("WS1", 1);
        Workstation ws2 = new Workstation("WS2", 1);

        Operation operation = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Product product = new Product("P1", List.of(operation, operation2));

        analyzer.addFlow(ws1, product);
        analyzer.addFlow(ws2, product);
        MachineFlowAnalyzer.reset();

        assertTrue(flowDependency.isEmpty());
    }

    @Test
    void testBuildDependenciesWithMultipleProducts() {
        MachineFlowAnalyzer analyzer = new MachineFlowAnalyzer();
        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 11);
        Workstation ws3 = new Workstation("WS3", 12);
        Operation operation = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Operation operation3 = new Operation("Op3");
        Operation operation4 = new Operation("Op4");
        Product product1 = new Product("P1", List.of(operation, operation2));
        Product product2 = new Product("P2", List.of(operation3, operation4));

        analyzer.addFlow(ws1, product1);
        analyzer.addFlow(ws2, product1);
        analyzer.addFlow(ws2, product2);
        analyzer.addFlow(ws3, product2);

        buildDependencies();
        assertFalse(MachineFlowAnalyzer.workstationDependencies.isEmpty());
    }


    @Test
    void testAddFlowWithNullWorkstation() {
        MachineFlowAnalyzer analyzer = new MachineFlowAnalyzer();
        Product product = new Product("P1", List.of(new Operation("Op1")));

        assertThrows(IllegalArgumentException.class, () -> {
            analyzer.addFlow(null, product);
        });
    }

    @Test
    void testFlowDependencyListing() {
        // Configurando Workstations
        Workstation m1 = new Workstation("m1", 5);
        Workstation m2 = new Workstation("m2", 5);
        Workstation m3 = new Workstation("m3", 5);
        Workstation m4 = new Workstation("m4", 5);
        Workstation m5 = new Workstation("m5", 5);

        // Configurando Produtos com o fluxo conforme o exemplo da USEI07
        Product a = new Product("a", List.of(new Operation("CUT"), new Operation("Assemble"), new Operation("Finish")));
        Product b = new Product("b", List.of(new Operation("CUT"), new Operation("Assemble"), new Operation("Finish")));
        Product c = new Product("c", List.of(new Operation("Assemble"), new Operation("Finish")));
        Product d = new Product("d", List.of(new Operation("Assemble"), new Operation("Finish")));
        Product e = new Product("e", List.of(new Operation("Assemble"), new Operation("Finish")));

        // Adicionando fluxo dos produtos para as estações
        machineFlowAnalyzer.addFlow(m1, a);
        machineFlowAnalyzer.addFlow(m1, b);
        machineFlowAnalyzer.addFlow(m2, b);
        machineFlowAnalyzer.addFlow(m4, b);
        machineFlowAnalyzer.addFlow(m5, b);
        machineFlowAnalyzer.addFlow(m5, a);

        machineFlowAnalyzer.addFlow(m1, c);
        machineFlowAnalyzer.addFlow(m2, c);
        machineFlowAnalyzer.addFlow(m3, c);
        machineFlowAnalyzer.addFlow(m5, c);

        machineFlowAnalyzer.addFlow(m1, d);
        machineFlowAnalyzer.addFlow(m4, d);
        machineFlowAnalyzer.addFlow(m3, d);

        machineFlowAnalyzer.addFlow(m1, e);
        machineFlowAnalyzer.addFlow(m3, e);
        machineFlowAnalyzer.addFlow(m5, e);

        // Construindo dependências após o processamento
        MachineFlowAnalyzer.buildDependencies();

        // Verificando as dependências conforme o esperado
        Map<String, Map<String, Integer>> dependencies = MachineFlowAnalyzer.getWorkstationDependencies();

        assertEquals(4, dependencies.size(), "Deve conter 4 estações de trabalho com dependências.");

        assertEquals(Map.of("m2", 2, "m5", 1, "m3", 1, "m4", 1), dependencies.get("m1"),
                "As dependências de m1 estão incorretas.");

        assertEquals(Map.of("m4", 1, "m3", 1), dependencies.get("m2"),
                "As dependências de m2 estão incorretas.");

        assertEquals(Map.of("m5", 2), dependencies.get("m3"),
                "As dependências de m3 estão incorretas.");

        assertEquals(Map.of("m5", 1, "m3", 1), dependencies.get("m4"),
                "As dependências de m4 estão incorretas.");
    }

    @Test
    void testPrintDependencies() {
        // Configuração das estações de trabalho
        Workstation m1 = new Workstation("m1", 5);
        Workstation m2 = new Workstation("m2", 5);
        Workstation m3 = new Workstation("m3", 5);
        Workstation m4 = new Workstation("m4", 5);
        Workstation m5 = new Workstation("m5", 5);

        // Configuração dos produtos e operações conforme o exemplo fornecido
        Product a = new Product("a", List.of(new Operation("Op1"), new Operation("Op5")));
        Product b = new Product("b", List.of(new Operation("Op1"), new Operation("Op2"), new Operation("Op4"), new Operation("Op5")));
        Product c = new Product("c", List.of(new Operation("Op1"), new Operation("Op2"), new Operation("Op3"), new Operation("Op5")));
        Product d = new Product("d", List.of(new Operation("Op1"), new Operation("Op4"), new Operation("Op3")));
        Product e = new Product("e", List.of(new Operation("Op1"), new Operation("Op3"), new Operation("Op5")));

        // Adicionando fluxo dos produtos nas estações de trabalho
        machineFlowAnalyzer.addFlow(m1, a);
        machineFlowAnalyzer.addFlow(m1, b);
        machineFlowAnalyzer.addFlow(m2, b);
        machineFlowAnalyzer.addFlow(m4, b);
        machineFlowAnalyzer.addFlow(m5, b);
        machineFlowAnalyzer.addFlow(m5, a);

        machineFlowAnalyzer.addFlow(m1, c);
        machineFlowAnalyzer.addFlow(m2, c);
        machineFlowAnalyzer.addFlow(m3, c);
        machineFlowAnalyzer.addFlow(m5, c);

        machineFlowAnalyzer.addFlow(m1, d);
        machineFlowAnalyzer.addFlow(m4, d);
        machineFlowAnalyzer.addFlow(m3, d);

        machineFlowAnalyzer.addFlow(m1, e);
        machineFlowAnalyzer.addFlow(m3, e);
        machineFlowAnalyzer.addFlow(m5, e);

        // Executando printDependencies e capturando a saída
        MachineFlowAnalyzer.printDependencies();


        // Saída esperada conforme a descrição da USEI07
        String expectedOutput = String.join(System.lineSeparator(),
                "",
                "Workstation Flow Dependency:",
                "m1 : [(m2,2), (m3,1), (m4,1), (m5,1)]",
                "m2 : [(m3,1), (m4,1)]",
                "m3 : [(m5,2)]",
                "m4 : [(m3,1), (m5,1)]"
        );
        // Comparação da saída gerada com a saída esperada
        assertEquals(expectedOutput.trim(), outputStreamCaptor.toString().trim(), "A saída gerada pelo printDependencies não corresponde ao esperado.");
    }

    @Test
    void testSmallCsvFiles() throws IOException {
        MachineFlowAnalyzer.reset();  // Limpa todas as dependências antes de executar este teste

        // Carregar dados dos arquivos CSV pequenos
        Reader.loadOperations("textFiles/small_articles.csv");
        Reader.loadMachines("textFiles/small_workstations.csv");

        // Executar a simulação para calcular dependências
        simulator.runWithoutPriority(true);
        MachineFlowAnalyzer.buildDependencies();

        // Dependências esperadas para os arquivos pequenos
        Map<String, Map<String, Integer>> expectedDependencies = Map.of(
                "WS1", Map.of("WS2", 1, "WS3", 1),
                "WS2", Map.of("WS4", 1),
                "WS3", Map.of("WS4", 1)
        );

        // Comparar as dependências geradas com as esperadas
        Map<String, Map<String, Integer>> actualDependencies = MachineFlowAnalyzer.getWorkstationDependencies();
        assertEquals(expectedDependencies, actualDependencies, "Dependências para small_workstations.csv não correspondem ao esperado.");
    }

     @Test
    void testMediumCsvFiles() throws IOException, IOException {
        // Carregar dados dos arquivos CSV médios
        Reader.loadOperations("textFiles/medium_articles.csv");
        Reader.loadMachines("textFiles/medium_workstations.csv");

        // Executar a simulação para calcular dependências
        simulator.runWithoutPriority(true);
        MachineFlowAnalyzer.buildDependencies();

        // Dependências esperadas para os arquivos médios
        Map<String, Map<String, Integer>> expectedDependencies = Map.of(
                "WS1", Map.of("WS3", 2, "WS2", 2, "WS6", 2),
                "WS3", Map.of("WS5", 3, "WS4", 1),
                "WS2", Map.of("WS3", 1, "WS5", 1, "WS4", 3, "WS7", 1),
                "WS4", Map.of("WS3", 1, "WS5", 2, "WS7", 2),
                "WS7", Map.of("WS5", 3),
                "WS6", Map.of("WS2", 2, "WS4", 1, "WS7", 1)
        );


        // Comparar as dependências geradas com as esperadas
        Map<String, Map<String, Integer>> actualDependencies = MachineFlowAnalyzer.getWorkstationDependencies();
        assertEquals(expectedDependencies, actualDependencies, "Dependências para medium_workstations.csv não correspondem ao esperado.");
    }

    @Test
    void testLargeCsvFiles() throws IOException, IOException {
        // Carregar dados dos arquivos CSV médios
        Reader.loadOperations("textFiles/articles.csv");
        Reader.loadMachines("textFiles/workstations.csv");

        // Executar a simulação para calcular dependências
        simulator.runWithPriority(true);
        MachineFlowAnalyzer.buildDependencies();

        // Verificar se as dependências foram geradas
        Map<String, Map<String, Integer>> actualDependencies = MachineFlowAnalyzer.getWorkstationDependencies();
        assertNotNull(actualDependencies, "As dependências não devem ser nulas para arquivos grandes");
        assertFalse(actualDependencies.isEmpty(), "As dependências não devem estar vazias para arquivos grandes");
    }

}