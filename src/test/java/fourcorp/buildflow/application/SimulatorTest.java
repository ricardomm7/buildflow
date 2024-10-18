package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SimulatorTest {

    @BeforeEach
    void resetData() {
        // Limpar os repositórios antes de cada teste
        Repositories.getInstance().getProductPriorityRepository().removeAll();
        Repositories.getInstance().getWorkstationsPerOperation().removeAll();
        MachineFlowAnalyzer.clearDependencies();
    }

    @Test
    void createOperationQueues() {
        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Welding"), new Operation("Polishing"))));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Painting"), new Operation("Packaging"))));
        Product product3 = new Product("P003", new LinkedList<>(Arrays.asList(new Operation("Forging"), new Operation("Drilling"), new Operation("Inspecting"))));
        Product product4 = new Product("P004", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Sanding"), new Operation("Coating"))));
        Product product5 = new Product("P005", new LinkedList<>(Arrays.asList(new Operation("Molding"), new Operation("Trimming"), new Operation("Polishing"))));
        Product product6 = new Product("P006", new LinkedList<>(Arrays.asList(new Operation("Soldering"), new Operation("Welding"), new Operation("Testing"))));
        Product product7 = new Product("P007", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Drilling"), new Operation("Inspecting"), new Operation("Assembling"))));
        Product product8 = new Product("P008", new LinkedList<>(Arrays.asList(new Operation("Casting"), new Operation("Machining"), new Operation("Painting"))));
        Product product9 = new Product("P009", new LinkedList<>(Arrays.asList(new Operation("Stamping"), new Operation("Welding"), new Operation("Inspecting"))));
        Product product10 = new Product("P010", new LinkedList<>(Arrays.asList(new Operation("Molding"), new Operation("Drilling"), new Operation("Packaging"))));

        WorkstationsPerOperation w = new WorkstationsPerOperation();
        ProductPriorityLine o = new ProductPriorityLine();

        o.create(product1, PriorityOrder.LOW);
        o.create(product2, PriorityOrder.LOW);
        o.create(product3, PriorityOrder.LOW);
        o.create(product4, PriorityOrder.LOW);
        o.create(product5, PriorityOrder.LOW);
        o.create(product6, PriorityOrder.LOW);
        o.create(product7, PriorityOrder.LOW);
        o.create(product8, PriorityOrder.LOW);
        o.create(product9, PriorityOrder.LOW);
        o.create(product10, PriorityOrder.LOW);

        Simulator s = new Simulator(w, o);
        s.runWithoutPriority();

        List<Operation> operations1 = s.getOperationQueues().getByKey(product1);
        List<Operation> operations2 = s.getOperationQueues().getByKey(product2);
        List<Operation> operations3 = s.getOperationQueues().getByKey(product3);
        List<Operation> operations4 = s.getOperationQueues().getByKey(product4);
        List<Operation> operations5 = s.getOperationQueues().getByKey(product5);
        List<Operation> operations6 = s.getOperationQueues().getByKey(product6);
        List<Operation> operations7 = s.getOperationQueues().getByKey(product7);
        List<Operation> operations8 = s.getOperationQueues().getByKey(product8);
        List<Operation> operations9 = s.getOperationQueues().getByKey(product9);
        List<Operation> operations10 = s.getOperationQueues().getByKey(product10);

        assertEquals(10, s.getProducts().size(), "The total products size should be 9.");
        assertEquals(3, operations1.size(), "The total products operations should be 3.");
        assertEquals(3, operations2.size(), "The total products operations should be 3.");
        assertEquals(3, operations3.size(), "The total products operations should be 3.");
        assertEquals(3, operations4.size(), "The total products operations should be 3.");
        assertEquals(3, operations5.size(), "The total products operations should be 3.");
        assertEquals(3, operations6.size(), "The total products operations should be 3.");
        assertEquals(4, operations7.size(), "The total products operations should be 4.");
        assertEquals(3, operations8.size(), "The total products operations should be 3.");
        assertEquals(3, operations9.size(), "The total products operations should be 3.");
        assertEquals(3, operations10.size(), "The total products operations should be 3.");
    }

    @Test
    void processItemsWithoutPriority() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Welding"))));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Painting"))));

        WorkstationsPerOperation w = new WorkstationsPerOperation();
        ProductPriorityLine p = new ProductPriorityLine();

        p.create(product1, PriorityOrder.LOW);
        p.create(product2, PriorityOrder.HIGH);

        Simulator simulator = new Simulator(w, p);

        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 8);
        Workstation ws3 = new Workstation("WS1", 10);
        Workstation ws4 = new Workstation("WS2", 8);

        w.create(ws1, new Operation("Cutting"));
        w.create(ws2, new Operation("Assembling"));
        w.create(ws3, new Operation("Welding"));
        w.create(ws4, new Operation("Painting"));

        simulator.runWithoutPriority();

        String output = outContent.toString();

        assertTrue(output.contains("Current operation: Cutting"), "The cutting operation should have been processed.");
        assertTrue(output.contains("The best machine: WS1"), "The WS1 machine should have been used for the cutting operation.");

        System.setOut(System.out);
    }

    @Test
    void processItemsWithPriority() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        WorkstationsPerOperation w = new WorkstationsPerOperation();
        ProductPriorityLine a = new ProductPriorityLine();

        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Welding"))));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Painting"))));
        Product product3 = new Product("P003", new LinkedList<>(Arrays.asList(new Operation("Drilling"), new Operation("Polishing"))));
        Product product4 = new Product("P004", new LinkedList<>(Arrays.asList(new Operation("Grinding"), new Operation("Coating"))));
        Product product5 = new Product("P005", new LinkedList<>(Arrays.asList(new Operation("Forging"), new Operation("Inspection"))));
        Product product6 = new Product("P006", new LinkedList<>(Arrays.asList(new Operation("Casting"), new Operation("Packaging"))));
        Product product7 = new Product("P007", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Bending"), new Operation("Welding"))));
        Product product8 = new Product("P008", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Testing"), new Operation("Painting"))));

        a.create(product1, PriorityOrder.LOW);
        a.create(product2, PriorityOrder.NORMAL);
        a.create(product3, PriorityOrder.HIGH);
        a.create(product4, PriorityOrder.NORMAL);
        a.create(product5, PriorityOrder.LOW);
        a.create(product6, PriorityOrder.HIGH);
        a.create(product7, PriorityOrder.NORMAL);
        a.create(product8, PriorityOrder.LOW);

        Simulator simulator = new Simulator(w, a);

        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 8);
        Workstation ws3 = new Workstation("WS1", 10);
        Workstation ws4 = new Workstation("WS2", 8);
        Workstation ws5 = new Workstation("WS3", 12);
        Workstation ws6 = new Workstation("WS4", 15);
        Workstation ws7 = new Workstation("WS5", 9);
        Workstation ws8 = new Workstation("WS6", 11);
        Workstation ws9 = new Workstation("WS7", 13);
        Workstation ws10 = new Workstation("WS8", 14);
        Workstation ws11 = new Workstation("WS9", 16);
        Workstation ws12 = new Workstation("WS10", 10);
        Workstation ws13 = new Workstation("WS11", 8);
        Workstation ws14 = new Workstation("WS12", 14);
        Workstation ws15 = new Workstation("WS13", 10);


        w.create(ws1, new Operation("Cutting"));
        w.create(ws2, new Operation("Assembling"));
        w.create(ws3, new Operation("Welding"));
        w.create(ws4, new Operation("Painting"));
        w.create(ws5, new Operation("Drilling"));
        w.create(ws6, new Operation("Polishing"));
        w.create(ws7, new Operation("Grinding"));
        w.create(ws8, new Operation("Coating"));
        w.create(ws9, new Operation("Forging"));
        w.create(ws10, new Operation("Inspection"));
        w.create(ws11, new Operation("Casting"));
        w.create(ws12, new Operation("Packaging"));
        w.create(ws13, new Operation("Bending"));
        w.create(ws14, new Operation("Testing"));
        w.create(ws15, new Operation("Painting"));

        simulator.runWithPriority();

        String output = outContent.toString();

        assertTrue(output.contains("Processing product " + product3.getId() + " in machine " + ws5.getId() + " - Estimated time: " + ws5.getTime() + " min"), "The drilling operation should have been processed.");
        assertTrue(output.contains("Processing product " + product6.getId() + " in machine " + ws11.getId() + " - Estimated time: " + ws11.getTime() + " min"), "The casting operation should have been processed.");
        assertTrue(output.contains("Processing product " + product2.getId() + " in machine " + ws2.getId() + " - Estimated time: " + ws2.getTime() + " min"), "The assembling operation should have been processed.");
        assertTrue(output.contains("Processing product " + product2.getId() + " in machine " + ws4.getId() + " - Estimated time: " + ws4.getTime() + " min"), "The painting operation should have been processed.");
        assertTrue(output.contains("Processing product " + product8.getId() + " in machine " + ws2.getId() + " - Estimated time: " + ws2.getTime() + " min"), "The assembling operation should have been processed.");


        System.setOut(System.out);
    }

    @Test
    void calculateTotalProductionTimeTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Welding"))));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Painting"))));

        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 8);
        Workstation ws3 = new Workstation("WS3", 12);
        Workstation ws4 = new Workstation("WS4", 15);

        WorkstationsPerOperation workstations = new WorkstationsPerOperation();
        workstations.create(ws1, new Operation("Cutting"));
        workstations.create(ws2, new Operation("Welding"));
        workstations.create(ws3, new Operation("Assembling"));
        workstations.create(ws4, new Operation("Painting"));

        ProductPriorityLine productLine = new ProductPriorityLine();
        productLine.create(product1, PriorityOrder.LOW);
        productLine.create(product2, PriorityOrder.HIGH);

        Simulator simulator = new Simulator(workstations, productLine);
        simulator.runWithoutPriority();

        double totalProductionTime = simulator.getTotalProductionTime();
        List<Double> productionTimePerProduct = simulator.getProductionTimePerProduct();

        assertEquals(45.0, totalProductionTime, 0.01, "The total production time should be 45 minutes (18 + 27).");
        assertEquals(2, productionTimePerProduct.size(), "There should be production times for 2 products.");
        assertTrue(productionTimePerProduct.contains(18.0), "The production time for product P001 should be 18 minutes (10 + 8).");
        assertTrue(productionTimePerProduct.contains(27.0), "The production time for product P002 should be 27 minutes (12 + 15).");

    }

    @Test
    void CalculateProductionsTimeTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Welding"))));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Painting"))));

        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 8);
        Workstation ws3 = new Workstation("WS3", 12);
        Workstation ws4 = new Workstation("WS4", 15);

        WorkstationsPerOperation workstations = new WorkstationsPerOperation();
        workstations.create(ws1, new Operation("Cutting"));
        workstations.create(ws2, new Operation("Welding"));
        workstations.create(ws3, new Operation("Assembling"));
        workstations.create(ws4, new Operation("Painting"));

        ProductPriorityLine productLine = new ProductPriorityLine();
        productLine.create(product1, PriorityOrder.LOW);
        productLine.create(product2, PriorityOrder.HIGH);

        Simulator simulator = new Simulator(workstations, productLine);
        simulator.runWithoutPriority();


        double totalProductionTime = simulator.getTotalProductionTime();
        List<Double> productionTimePerProduct = simulator.getProductionTimePerProduct();

        assertEquals(45.0, totalProductionTime, 0.01, "The total production time should be 45 minutes (18 + 27).");
        assertEquals(2, productionTimePerProduct.size(), "There should be production times for 2 products.");
        assertTrue(productionTimePerProduct.contains(18.0), "The production time for product P001 should be 18 minutes (10 + 8).");
        assertTrue(productionTimePerProduct.contains(27.0), "The production time for product P002 should be 27 minutes (12 + 15).");
    }

    // Caminhos para os arquivos CSV
    private final String smallOperationsFile = Paths.get("textFiles/small_articles.csv").toString();
    private final String smallWorkstationsFile = Paths.get("textFiles/small_workstations.csv").toString();
    private final String mediumOperationsFile = Paths.get("textFiles/medium_articles.csv").toString();
    private final String mediumWorkstationsFile = Paths.get("textFiles/medium_workstations.csv").toString();
    private final String largeOperationsFile = Paths.get("textFiles/articles.csv").toString();
    private final String largeWorkstationsFile = Paths.get("textFiles/workstations.csv").toString();

    @Test
    void testSmallDataSetExecutionTimes() throws IOException {
        // Carregar e rodar a simulação para o pequeno conjunto de dados
        runSimulationAndValidateTimes(smallOperationsFile, smallWorkstationsFile, "small dataset",
                new double[]{22, 25},  // Tempos esperados para P001 e P002
                47                     // Tempo total esperado
        );
    }

    @Test
    void testMediumDataSetExecutionTimes() throws IOException {
        // Carregar e rodar a simulação para o conjunto médio de dados
        runSimulationAndValidateTimes(mediumOperationsFile, mediumWorkstationsFile, "medium dataset",
                new double[]{54, 49, 27, 36, 48, 42, 45, 43, 36, 39},  // Tempos esperados para os produtos
                419                     // Tempo total esperado
        );
    }


    @Test
    void testLargeDataSet() throws IOException {
        runSimulationWithCSV(largeOperationsFile, largeWorkstationsFile, "large dataset");
    }


    private void runSimulationWithCSV(String operationsFilePath, String workstationsFilePath, String dataSetLabel) throws IOException {
        // Captura a saída do System.out para verificar a saída do simulador
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Carregar os dados dos arquivos CSV
        Reader.loadOperations(operationsFilePath);
        Reader.loadMachines(workstationsFilePath);

        // Inicializa o simulador
        Simulator simulator = new Simulator();
        simulator.runWithPriority();

        // Recuperar a saída gerada
        String output = outContent.toString().trim();

        // Verificações simples para garantir que a simulação foi executada corretamente
        assertTrue(output.contains("Processing product"), "Deve haver uma saída de processamento de produtos para o " + dataSetLabel);
        assertTrue(output.contains("The best machine:"), "Deve haver uma máquina escolhida para o " + dataSetLabel);

        // Verificar o tempo total de produção e o número de produtos
        double totalProductionTime = simulator.getTotalProductionTime();
        assertTrue(totalProductionTime > 0, "O tempo total de produção deve ser maior que 0 para o " + dataSetLabel);

        // Verificar as dependências entre as máquinas (US007)
        assertFalse(MachineFlowAnalyzer.machineDependencies.isEmpty(), "As dependências entre as máquinas devem existir para o " + dataSetLabel);

        // Limpar o System.out após a execução do teste
        System.setOut(System.out);
    }


    private void runSimulationAndValidateTimes(String operationsFilePath, String workstationsFilePath, String dataSetLabel, double[] expectedProductTimes, double expectedTotalTime) throws IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Carregar os arquivos CSV
        Reader.loadOperations(operationsFilePath);
        Reader.loadMachines(workstationsFilePath);

        // Inicializa o simulador e roda com prioridade
        Simulator simulator = new Simulator();
        simulator.runWithPriority();

        // Recuperar a saída gerada (para fins de verificação opcional)
        String output = outContent.toString().trim();

        // Verificar se os produtos foram processados
        assertTrue(output.contains("Processing product"), "Deve haver uma saída de processamento de produtos para o " + dataSetLabel);

        // Verificar tempos de produção por produto (US003)
        List<Double> actualProductTimes = simulator.getProductionTimePerProduct();

        // Ordenar os tempos reais e os esperados para garantir que estamos comparando na ordem correta
        actualProductTimes.sort(Double::compareTo);
        Arrays.sort(expectedProductTimes);

        // Comparar os tempos de produção esperados com os reais
        assertEquals(expectedProductTimes.length, actualProductTimes.size(), "O número de produtos deve corresponder para o " + dataSetLabel);

        for (int i = 0; i < expectedProductTimes.length; i++) {
            assertEquals(expectedProductTimes[i], actualProductTimes.get(i), 0.01, "O tempo de produção do produto " + (i + 1) + " deve ser correto no " + dataSetLabel);
        }

        // Verificar tempo total de produção (US003)
        double actualTotalTime = simulator.getTotalProductionTime();
        assertEquals(expectedTotalTime, actualTotalTime, 0.01, "O tempo total de produção deve ser correto para o " + dataSetLabel);

        System.setOut(System.out);
    }

    @Test
    void testSmallDataSetDependencies() throws IOException {
        // Dependências esperadas para o conjunto de dados pequeno (small dataset)
        Map<String, Map<String, Integer>> expectedDependencies = new HashMap<>();

        expectedDependencies.put("WS1", Map.of("WS2", 1, "WS3", 1));
        expectedDependencies.put("WS2", Map.of("WS4", 1));
        expectedDependencies.put("WS3", Map.of("WS4", 1));

        // Rodar a simulação e validar as dependências
        runSimulationAndValidateDependencies(smallOperationsFile, smallWorkstationsFile, "small dataset", expectedDependencies);
    }

    @Test
    void testMediumDataSetDependencies() throws IOException {
        // Dependências esperadas para o conjunto de dados médio (medium dataset)
        Map<String, Map<String, Integer>> expectedDependencies = new HashMap<>();
        expectedDependencies.put("WS1", Map.of("WS2", 3, "WS3", 2, "WS6", 2, "WS5", 1));
        expectedDependencies.put("WS2", Map.of("WS4", 2, "WS3", 1, "WS7", 1, "WS5", 1));
        expectedDependencies.put("WS3", Map.of("WS4", 1, "WS5", 2));
        expectedDependencies.put("WS4", Map.of("WS5", 3, "WS7", 1));
        expectedDependencies.put("WS6", Map.of("WS4", 1, "WS7", 2));
        expectedDependencies.put("WS7", Map.of("WS5", 3));
        // Rodar a simulação e validar as dependências
        runSimulationAndValidateDependencies(mediumOperationsFile, mediumWorkstationsFile, "medium dataset", expectedDependencies);
    }

    private void runSimulationAndValidateDependencies(String operationsFilePath, String workstationsFilePath, String dataSetLabel, Map<String, Map<String, Integer>> expectedDependencies) throws IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // Carregar os arquivos CSV
        Reader.loadOperations(operationsFilePath);
        Reader.loadMachines(workstationsFilePath);
        // Inicializa o simulador e roda com prioridade
        Simulator simulator = new Simulator();
        simulator.runWithPriority();
        // Verificar dependências geradas (US007)
        Map<String, Map<String, Integer>> actualDependencies = MachineFlowAnalyzer.machineDependencies;
        // Comparar as dependências geradas com as dependências esperadas
        assertEquals(expectedDependencies.size(), actualDependencies.size(), "O número de máquinas com dependências deve corresponder no " + dataSetLabel);
        for (String machine : expectedDependencies.keySet()) {
            assertTrue(actualDependencies.containsKey(machine), "A máquina " + machine + " deve estar presente nas dependências do " + dataSetLabel);
            Map<String, Integer> expectedFlows = expectedDependencies.get(machine);
            Map<String, Integer> actualFlows = actualDependencies.get(machine);
            assertEquals(expectedFlows.size(), actualFlows.size(), "O número de dependências para a máquina " + machine + " deve corresponder no " + dataSetLabel);
            for (String dependentMachine : expectedFlows.keySet()) {
                assertTrue(actualFlows.containsKey(dependentMachine), "A máquina " + machine + " deve ter uma dependência com " + dependentMachine);
                assertEquals(expectedFlows.get(dependentMachine), actualFlows.get(dependentMachine), "O número de vezes que " + machine + " usou " + dependentMachine + " deve ser correto.");
            }
        }
        System.setOut(System.out);
    }


}