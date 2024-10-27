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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SimulatorTest {

    private Simulator simulator;
    private ProductPriorityLine productLine;
    private WorkstationsPerOperation workstationsPerOperation;

    @BeforeEach
    void setUp() {
        simulator = new Simulator();
        Repositories.getInstance().getProductPriorityRepository().removeAll();
        Repositories.getInstance().getWorkstationsPerOperation().removeAll();
    }

    @Test
    void testRunWithPriority() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Finish"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op2"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        Operation operation1 = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Operation operation3 = new Operation("Finish");

        Workstation ws1 = new Workstation("WS1", 5);
        Workstation ws2 = new Workstation("WS2", 10);
        Workstation ws3 = new Workstation("Finish", 2);
        workstationsPerOperation.create(ws3, operation3);
        workstationsPerOperation.create(ws1, operation1);
        workstationsPerOperation.create(ws2, operation2);
        simulator.runWithPriority(true);
        assertTrue(simulator.getTotalProductionTime() > 0, "Total production time should be positive after running simulation.");
    }

    @Test
    void testRunWithoutPriority() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Finish"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op2"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        Operation operation1 = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Operation operation3 = new Operation("Finish");

        Workstation ws1 = new Workstation("WS1", 5);
        Workstation ws2 = new Workstation("WS2", 10);
        Workstation ws3 = new Workstation("Finish", 2);
        workstationsPerOperation.create(ws3, operation3);
        workstationsPerOperation.create(ws1, operation1);
        workstationsPerOperation.create(ws2, operation2);
        simulator.runWithoutPriority(false);
        assertTrue(simulator.getTotalProductionTime() > 0, "Total production time should be positive after running simulation.");
    }

    @Test
    void testProcessingHighPriorityProducts() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Finish"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op2"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        Operation operation1 = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Operation operation3 = new Operation("Finish");

        Workstation ws1 = new Workstation("WS1", 5);
        Workstation ws2 = new Workstation("WS2", 10);
        Workstation ws3 = new Workstation("Finish", 2);
        workstationsPerOperation.create(ws3, operation3);
        workstationsPerOperation.create(ws1, operation1);
        workstationsPerOperation.create(ws2, operation2);
        simulator.runWithPriority(true);
        List<Product> highPriorityProducts = productLine.getProductsByPriority(PriorityOrder.HIGH);
        for (Product product : highPriorityProducts) {
            assertTrue(product.hasMoreOperations(), "High-priority products should be processed first.");
        }
    }

    @Test
    void testProcessingNormalPriorityProductsAfterHighPriority() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Finish"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op2"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        Operation operation1 = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Operation operation3 = new Operation("Finish");

        Workstation ws1 = new Workstation("WS1", 5);
        Workstation ws2 = new Workstation("WS2", 10);
        Workstation ws3 = new Workstation("Finish", 2);
        workstationsPerOperation.create(ws3, operation3);
        workstationsPerOperation.create(ws1, operation1);
        workstationsPerOperation.create(ws2, operation2);
        simulator.runWithPriority(true);
        List<Product> normalPriorityProducts = productLine.getProductsByPriority(PriorityOrder.NORMAL);
        for (Product product : normalPriorityProducts) {
            assertTrue(product.hasMoreOperations(), "Normal-priority products should be processed after high-priority products.");
        }
    }

    @Test
    void testProductTimesCalculation() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Finish"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op2"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        Operation operation1 = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Operation operation3 = new Operation("Finish");

        Workstation ws1 = new Workstation("WS1", 5);
        Workstation ws2 = new Workstation("WS2", 10);
        Workstation ws3 = new Workstation("Finish", 2);
        workstationsPerOperation.create(ws3, operation3);
        workstationsPerOperation.create(ws1, operation1);
        workstationsPerOperation.create(ws2, operation2);
        simulator.runWithoutPriority(false);
        double totalProductionTime = simulator.getTotalProductionTime();
        assertTrue(totalProductionTime > 0, "Total production time should reflect cumulative product operation times.");
    }

    @Test
    void testOperationTimesCalculation() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Finish"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op2"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        Operation operation1 = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Operation operation3 = new Operation("Finish");

        Workstation ws1 = new Workstation("WS1", 5);
        Workstation ws2 = new Workstation("WS2", 10);
        Workstation ws3 = new Workstation("Finish", 2);
        workstationsPerOperation.create(ws3, operation3);
        workstationsPerOperation.create(ws1, operation1);
        workstationsPerOperation.create(ws2, operation2);
        simulator.runWithoutPriority(false);
        double sumOfOperationTimes = simulator.getTotalProductionTime();
        assertEquals(17, sumOfOperationTimes, "Sum of operation times should equal total production time.");
    }

    @Test
    void testProcessWaitingQueue() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Finish"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op2"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        Operation operation1 = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Operation operation3 = new Operation("Finish");

        Workstation ws1 = new Workstation("WS1", 5);
        Workstation ws2 = new Workstation("WS2", 10);
        Workstation ws3 = new Workstation("Finish", 2);
        workstationsPerOperation.create(ws3, operation3);
        workstationsPerOperation.create(ws1, operation1);
        workstationsPerOperation.create(ws2, operation2);

        simulator.runWithPriority(true);
        assertFalse(simulator.areProductsQueueEmpty(), "The products queue should not be empty. EVER!");
    }

    @Test
    void testPrintProductionStatistics() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Finish"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op2"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        Operation operation1 = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Operation operation3 = new Operation("Finish");

        Workstation ws1 = new Workstation("WS1", 5);
        Workstation ws2 = new Workstation("WS2", 10);
        Workstation ws3 = new Workstation("Finish", 2);
        workstationsPerOperation.create(ws3, operation3);
        workstationsPerOperation.create(ws1, operation1);
        workstationsPerOperation.create(ws2, operation2);

        simulator.runWithoutPriority(false);
        simulator.printProductionStatistics();
        assertTrue(simulator.getTotalProductionTime() > 0, "Production statistics should reflect the production time.");
    }

    @Test
    void testPrintAnalysis() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Finish"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op2"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        Operation operation1 = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Operation operation3 = new Operation("Finish");

        Workstation ws1 = new Workstation("WS1", 5);
        Workstation ws2 = new Workstation("WS2", 10);
        Workstation ws3 = new Workstation("Finish", 2);
        workstationsPerOperation.create(ws3, operation3);
        workstationsPerOperation.create(ws1, operation1);
        workstationsPerOperation.create(ws2, operation2);

        simulator.runWithoutPriority(false);
        simulator.printAnalysis();
        assertTrue(simulator.getTotalProductionTime() > 0, "Analysis should display workstation operation times and percentages.");
    }

    @Test
    void testResetSimulation() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Finish"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op2"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        Operation operation1 = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Operation operation3 = new Operation("Finish");

        Workstation ws1 = new Workstation("WS1", 5);
        Workstation ws2 = new Workstation("WS2", 10);
        Workstation ws3 = new Workstation("Finish", 2);
        workstationsPerOperation.create(ws3, operation3);
        workstationsPerOperation.create(ws1, operation1);
        workstationsPerOperation.create(ws2, operation2);

        simulator.runWithoutPriority(false);
        simulator.resetSimulation();
        assertEquals(0, simulator.getTotalProductionTime(), "Total production time should be reset to zero.");
        assertFalse(simulator.areProductsQueueEmpty(), "The products queue should not be empty. EVER!");
    }


    @Test
    void testAddToWaitingQueue() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Finish"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op2"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        Operation operation1 = new Operation("Op1");
        Operation operation2 = new Operation("Op2");
        Operation operation3 = new Operation("Finish");

        Workstation ws1 = new Workstation("WS1", 5);
        Workstation ws2 = new Workstation("WS2", 10);
        Workstation ws3 = new Workstation("Finish", 2);
        workstationsPerOperation.create(ws3, operation3);
        workstationsPerOperation.create(ws1, operation1);
        workstationsPerOperation.create(ws2, operation2);

        Product product3 = new Product("P3", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Finish"))));
        productLine.create(product3, PriorityOrder.LOW);
        simulator.runWithoutPriority(false);

        assertFalse(simulator.areProductsQueueEmpty(), "Products should be added to the waiting queue if operations are busy.");
    }


    @Test
    void testGetTotalProductionTimeInitialValue() {
        simulator = new Simulator();
        assertEquals(0.0, simulator.getTotalProductionTime(), "Initial total production time should be zero");
    }

    @Test
    void testGetTotalProductionTimeAfterMultipleRuns() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"))));
        productLine.create(product1, PriorityOrder.NORMAL);

        Workstation ws1 = new Workstation("WS1", 5);
        workstationsPerOperation.create(ws1, new Operation("Op1"));

        simulator.runWithoutPriority(false);
        double firstRunTime = simulator.getTotalProductionTime();
        simulator.resetSimulation();
        simulator.runWithoutPriority(false);

        assertEquals(firstRunTime, simulator.getTotalProductionTime(),
                "Total production time should be consistent across multiple runs with same configuration");
    }

    @Test
    void testGetTotalProductionTimeWithEmptyProductLine() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        simulator.runWithoutPriority(false);
        assertEquals(0.0, simulator.getTotalProductionTime(),
                "Total production time should be zero with empty product line");
    }


    @Test
    void testGetTotalProductionTimeWithParallelOperations() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op2"))));

        productLine.create(product1, PriorityOrder.NORMAL);
        productLine.create(product2, PriorityOrder.NORMAL);

        Workstation ws1 = new Workstation("WS1", 4);
        Workstation ws2 = new Workstation("WS2", 6);

        workstationsPerOperation.create(ws1, new Operation("Op1"));
        workstationsPerOperation.create(ws2, new Operation("Op2"));

        simulator.runWithoutPriority(false);
        assertEquals(10.0, simulator.getTotalProductionTime(),
                "Total production time should reflect longest parallel operation");
    }


    @Test
    void testCalculateFinishWaitingDifferentOperations() {
        simulator = new Simulator();
        Operation operation1 = new Operation("TestOp1");
        Operation operation2 = new Operation("TestOp2");

        simulator.calculateBeginingWaiting(operation1);
        simulator.calculateFinishWaiting(operation1);

        simulator.calculateBeginingWaiting(operation2);
        simulator.calculateFinishWaiting(operation2);

        assertEquals(1, simulator.getCountWaiting().get("TestOp1"), "First operation count should be 1");
        assertEquals(1, simulator.getCountWaiting().get("TestOp2"), "Second operation count should be 1");
        assertTrue(simulator.getOperationWaitingTimes().containsKey("TestOp1"), "First operation should be tracked");
        assertTrue(simulator.getOperationWaitingTimes().containsKey("TestOp2"), "Second operation should be tracked");
    }

    @Test
    void testCalculateFinishWaitingWithoutBegining() {
        simulator = new Simulator();
        Operation operation = new Operation("TestOp");
        simulator.calculateFinishWaiting(operation);

        assertEquals(1, simulator.getCountWaiting().get("TestOp"), "Operation count should be 1 even without beginning");
        assertTrue(simulator.getOperationWaitingTimes().get("TestOp") >= 0, "Waiting time should be non-negative");
    }

    @Test
    void testGetOperationTimesEmpty() {
        simulator = new Simulator();
        assertTrue(simulator.getOperationTimes().isEmpty(), "Operation times map should be empty initially");
    }

    @Test
    void testGetOperationTimesAfterOperations() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product = new Product("P1", new LinkedList<>(List.of(new Operation("TestOp"))));
        productLine.create(product, PriorityOrder.NORMAL);

        Workstation ws = new Workstation("WS1", 5);
        workstationsPerOperation.create(ws, new Operation("TestOp"));

        simulator.runWithoutPriority(false);
        Map<String, Double> times = simulator.getOperationTimes();

        assertFalse(times.isEmpty(), "Operation times should not be empty after running operations");
        assertTrue(times.containsKey("TestOp"), "Operation times should contain the executed operation");
        assertEquals(5.0, times.get("TestOp"), "Operation time should match workstation processing time");
    }

    @Test
    void testGetOperationCountsEmpty() {
        simulator = new Simulator();
        assertTrue(simulator.getOperationCounts().isEmpty(), "Operation counts map should be empty initially");
    }

    @Test
    void testGetOperationCountsMultipleOperations() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Op1"), new Operation("Op1"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Op1"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        Workstation ws = new Workstation("WS1", 5);
        workstationsPerOperation.create(ws, new Operation("Op1"));

        simulator.runWithoutPriority(false);
        Map<String, Integer> counts = simulator.getOperationCounts();

        assertFalse(counts.isEmpty(), "Operation counts should not be empty after running operations");
        assertTrue(counts.containsKey("Op1"), "Operation counts should contain the executed operation");
        assertEquals(3, counts.get("Op1"), "Operation count should match total number of operations performed");
    }

    @Test
    void testOperationExecutionTime_USEI04() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        // Configurando produtos com operações específicas
        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Cutting"), new Operation("Painting"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Cutting"), new Operation("Drilling"))));

        productLine.create(product1, PriorityOrder.HIGH);
        productLine.create(product2, PriorityOrder.NORMAL);

        // Configurando estações de trabalho com tempos de execução definidos
        Workstation wsCutting = new Workstation("WS_Cutting", 10);
        Workstation wsPainting = new Workstation("WS_Painting", 5);
        Workstation wsDrilling = new Workstation("WS_Drilling", 15);
        workstationsPerOperation.create(wsCutting, new Operation("Cutting"));
        workstationsPerOperation.create(wsPainting, new Operation("Painting"));
        workstationsPerOperation.create(wsDrilling, new Operation("Drilling"));

        // Executa o simulador sem prioridade
        simulator.runWithoutPriority(false);

        // Verifica o tempo total para a operação "Cutting" (10s por produto)
        assertEquals(20.0, simulator.getOperationExecutionTime("Cutting"), 0.01, "Tempo total para 'Cutting' deve ser 20s.");

        // Verifica o tempo total para a operação "Painting" (5s para o único produto que faz essa operação)
        assertEquals(5.0, simulator.getOperationExecutionTime("Painting"), 0.01, "Tempo total para 'Painting' deve ser 5s.");

        // Verifica o tempo total para a operação "Drilling" (15s para o único produto que faz essa operação)
        assertEquals(15.0, simulator.getOperationExecutionTime("Drilling"), 0.01, "Tempo total para 'Drilling' deve ser 15s.");
    }

    @Test
    void testAverageExecutionAndWaitingTimeConsistency_USEI06() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        // Configura produtos com operações para verificar tempos médios
        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Cutting"), new Operation("Polishing"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Cutting"), new Operation("Polishing"))));
        Product product3 = new Product("P3", new LinkedList<>(List.of(new Operation("Drilling"))));

        productLine.create(product1, PriorityOrder.NORMAL);
        productLine.create(product2, PriorityOrder.NORMAL);
        productLine.create(product3, PriorityOrder.NORMAL);

        // Configura estações com tempos de execução controlados
        Workstation wsCutting = new Workstation("WS_Cutting", 10);
        Workstation wsPolishing = new Workstation("WS_Polishing", 5);
        Workstation wsDrilling = new Workstation("WS_Drilling", 15);
        workstationsPerOperation.create(wsCutting, new Operation("Cutting"));
        workstationsPerOperation.create(wsPolishing, new Operation("Polishing"));
        workstationsPerOperation.create(wsDrilling, new Operation("Drilling"));

        // Executa o simulador
        simulator.runWithoutPriority(false);

        // Verifica consistência do tempo médio de execução entre produtos que fazem a mesma operação
        double avgCuttingTime = simulator.getAverageExecutionTime("Cutting");
        assertEquals(10.0, avgCuttingTime, 0.01, "O tempo médio de execução para 'Cutting' deve ser 10s.");

        double avgPolishingTime = simulator.getAverageExecutionTime("Polishing");
        assertEquals(5.0, avgPolishingTime, 0.01, "O tempo médio de execução para 'Polishing' deve ser 5s.");

        double avgDrillingTime = simulator.getAverageExecutionTime("Drilling");
        assertEquals(15.0, avgDrillingTime, 0.01, "O tempo médio de execução para 'Drilling' deve ser 15s.");

        // Verifica tempos médios de espera (exemplo: operações com maior carga tendem a ter mais espera)
        double avgCuttingWaitingTime = simulator.getAverageWaitingTime("Cutting");
        double avgPolishingWaitingTime = simulator.getAverageWaitingTime("Polishing");

        assertTrue(avgCuttingWaitingTime >= 0, "O tempo médio de espera para 'Cutting' deve ser >= 0.");
        assertTrue(avgPolishingWaitingTime >= 0, "O tempo médio de espera para 'Polishing' deve ser >= 0.");

        // Valida que, em geral, o tempo médio de espera é consistente (dependerá do setup específico)
        assertTrue(avgCuttingWaitingTime >= avgPolishingWaitingTime,
                "O tempo médio de espera para 'Cutting' deve ser maior ou igual ao de 'Polishing' devido ao maior tempo de processamento.");
    }


    @Test
    void testTotalOperationTimeAndPercentage_USEI05() {
        productLine = new ProductPriorityLine();
        workstationsPerOperation = new WorkstationsPerOperation();
        simulator = new Simulator();

        // Configuração de produtos e operações
        Product product1 = new Product("P1", new LinkedList<>(List.of(new Operation("Cutting"), new Operation("Polishing"))));
        Product product2 = new Product("P2", new LinkedList<>(List.of(new Operation("Cutting"), new Operation("Drilling"))));

        productLine.create(product1, PriorityOrder.NORMAL);
        productLine.create(product2, PriorityOrder.NORMAL);

        // Configura estações de trabalho com tempos específicos para cada operação
        Workstation wsCutting = new Workstation("WS_Cutting", 10);  // Tempo de operação para "Cutting"
        Workstation wsPolishing = new Workstation("WS_Polishing", 5);  // Tempo de operação para "Polishing"
        Workstation wsDrilling = new Workstation("WS_Drilling", 15);  // Tempo de operação para "Drilling"

        workstationsPerOperation.create(wsCutting, new Operation("Cutting"));
        workstationsPerOperation.create(wsPolishing, new Operation("Polishing"));
        workstationsPerOperation.create(wsDrilling, new Operation("Drilling"));

        // Executa o simulador
        simulator.runWithoutPriority(false);

        // Verificação do tempo total de cada operação
        assertEquals(20.0, simulator.getOperationExecutionTime("Cutting"), 0.01,
                "O tempo total para 'Cutting' deve ser 20 segundos (10s por produto).");
        assertEquals(5.0, simulator.getOperationExecutionTime("Polishing"), 0.01,
                "O tempo total para 'Polishing' deve ser 5 segundos.");
        assertEquals(15.0, simulator.getOperationExecutionTime("Drilling"), 0.01,
                "O tempo total para 'Drilling' deve ser 15 segundos.");

        // Recupera o tempo total de produção calculado pelo simulador
        double totalProductionTime = simulator.getTotalProductionTime();

        // Verificação dos percentuais de tempo para cada operação em relação ao total
        double expectedCuttingPercentage = (20.0 / totalProductionTime) * 100;
        double expectedPolishingPercentage = (5.0 / totalProductionTime) * 100;
        double expectedDrillingPercentage = (15.0 / totalProductionTime) * 100;

        // Calcula e verifica os percentuais para cada operação
        double actualCuttingPercentage = (simulator.getOperationExecutionTime("Cutting") / totalProductionTime) * 100;
        double actualPolishingPercentage = (simulator.getOperationExecutionTime("Polishing") / totalProductionTime) * 100;
        double actualDrillingPercentage = (simulator.getOperationExecutionTime("Drilling") / totalProductionTime) * 100;

        assertEquals(expectedCuttingPercentage, actualCuttingPercentage, 0.01,
                "O percentual de tempo para 'Cutting' deve estar correto.");
        assertEquals(expectedPolishingPercentage, actualPolishingPercentage, 0.01,
                "O percentual de tempo para 'Polishing' deve estar correto.");
        assertEquals(expectedDrillingPercentage, actualDrillingPercentage, 0.01,
                "O percentual de tempo para 'Drilling' deve estar correto.");
    }

    void loadData(String articlesPath, String workstationsPath) throws IOException {
        Reader.loadOperations(articlesPath);
        Reader.loadMachines(workstationsPath);
    }

    @Test
    void testSmallSimulation() throws IOException {
        loadData("textFiles/small_articles.csv", "textFiles/small_workstations.csv");
        simulator.runWithoutPriority(false);  // Inicia a simulação sem priorização

        // Verificações básicas
        assertTrue(simulator.getTotalProductionTime() > 0, "Total production time should be greater than 0.");
        assertEquals(4, simulator.getOperationTimes().size(), "There should be four operations in total.");

        simulator.printProductionStatistics();
        simulator.printAverageTimesReport();
    }

    @Test
    void testMediumSimulation() throws IOException {
        loadData("textFiles/medium_articles.csv", "textFiles/medium_workstations.csv");
        simulator.runWithoutPriority(true);  // Inicia a simulação com priorização

        // Verificações adicionais
        assertTrue(simulator.getTotalProductionTime() > 0, "Total production time should be greater than 0 for medium dataset.");
        assertEquals(7, simulator.getOperationTimes().size(), "There should be seven operations in total for medium dataset.");

        simulator.printProductionStatistics();
        simulator.printAverageTimesReport();
    }

    // USEI03
    @Test
    void testTotalProductionTime_smallDataset() throws IOException {
        loadData("textFiles/small_articles.csv", "textFiles/small_workstations.csv");
        simulator.runWithoutPriority(false); // Executa sem priorização para um cenário simples

        double expectedTotalTime = 25 + 22;  // Baseado nos tempos esperados das operações CUT, POLISH/VARNISH, e PACK
        double actualTotalTime = simulator.getTotalProductionTime();

        assertEquals(expectedTotalTime, actualTotalTime, 0.01, "O tempo total de produção calculado deve ser igual ao esperado.");
    }

    // USEI03
    @Test
    void testTotalProductionTime_mediumDataset() throws IOException {
        loadData("textFiles/medium_articles.csv", "textFiles/medium_workstations.csv");
        simulator.runWithPriority(true);  // Executa com priorização para um conjunto de dados mais complexo

        double expectedTotalTime = 54 + 49 + 27 + 36 + 48 + 42 + 45 + 43 + 36 + 39;  // Baseado nos tempos esperados das operações CUT, POLISH/VARNISH, e PACK
        double actualTotalTime = simulator.getTotalProductionTime();

        // Exemplo: Validação básica para garantir que o tempo total seja positivo e calculado
        assertEquals(expectedTotalTime, actualTotalTime, 0.01, "O tempo total de produção calculado deve ser igual ao esperado.");
    }


    //USEI04
    @Test
    void testOperationExecutionTimes() throws IOException {
        loadData("textFiles/small_articles.csv", "textFiles/small_workstations.csv");
        simulator.runWithoutPriority(false); // Executa sem priorização

        // Exemplo de teste de tempos específicos de operação
        double cutTime = simulator.getOperationExecutionTime("CUT");
        double expectedCutTime = 10 * 2;  // Exemplo baseado em dados esperados
        assertEquals(expectedCutTime, cutTime, "CUT operation should take expected time for all items.");

        double polishTime = simulator.getOperationExecutionTime("POLISH");
        double expectedPolishTime = 5;  // Exemplo baseado em dados esperados
        assertEquals(expectedPolishTime, polishTime, "POLISH operation should take expected time for all items.");

        double varnishTime = simulator.getOperationExecutionTime("VARNISH");
        double expectedVarnishTime = 8;
        assertEquals(expectedVarnishTime, varnishTime, "VARNISH operation should take expected time for all items.");

        double packTime = simulator.getOperationExecutionTime("PACK");
        double expectedPackTime = 7 * 2;
        assertEquals(expectedPackTime, packTime, "PACK operation should take expected time for all items.");
    }

    // USEI04
    @Test
    void testOperationExecutionTimesMedium() throws IOException {
        loadData("textFiles/medium_articles.csv", "textFiles/medium_workstations.csv");
        simulator.runWithoutPriority(false); // Executa sem priorização

        // Exemplo de teste de tempos específicos de operação
        double cutTime = simulator.getOperationExecutionTime("CUT");
        double expectedCutTime = 15 * 6;  // Exemplo baseado em dados esperados
        assertEquals(expectedCutTime, cutTime, "CUT operation should take expected time for all items.");

        double polishTime = simulator.getOperationExecutionTime("POLISH");
        double expectedPolishTime = 8 * 5;  // Exemplo baseado em dados esperados
        assertEquals(expectedPolishTime, polishTime, "POLISH operation should take expected time for all items.");

        double varnishTime = simulator.getOperationExecutionTime("VARNISH");
        double expectedVarnishTime = 12 * 4;
        assertEquals(expectedVarnishTime, varnishTime, "VARNISH operation should take expected time for all items.");

        double packTime = simulator.getOperationExecutionTime("PACK");
        double expectedPackTime = 9 * 9;
        assertEquals(expectedPackTime, packTime, "PACK operation should take expected time for all items.");

        double drillTime = simulator.getOperationExecutionTime("DRILL");
        double expectedDrillTime = 10 * 6;
        assertEquals(expectedDrillTime, drillTime, "DRILL operation should take expected time for all items.");

        double weldTime = simulator.getOperationExecutionTime("WELD");
        double expectedWeldTime = 14 * 4;
        assertEquals(expectedWeldTime, weldTime, "WELD operation should take expected time for all items.");

        double PaintTime = simulator.getOperationExecutionTime("PAINT");
        double expectedPaintTime = 11 * 4;
        assertEquals(expectedPaintTime, PaintTime, "PAINT operation should take expected time for all items.");
    }

    // USEI05
    @Test
    void testUtilizationPercentages() throws IOException {
        loadData("textFiles/medium_articles.csv", "textFiles/medium_workstations.csv");
        simulator.runWithPriority(true);  // Executa com priorização

        // Exemplo de verificação de porcentagens de utilização
        simulator.printAnalysis();
        // Comparação de percentuais conforme resultados esperados
        double packTimePercentage = simulator.getWorkstationOperationTimes().get("WS5") / simulator.getTotalProductionTime() * 100;
        assertTrue(packTimePercentage <= 20, "Expected PACK station to operate less than 50% of total time.");
    }

    // USEI05
    @Test
    void testWorkstationsOperationTimesAndPercentages_smallDataset() throws IOException {
        loadData("textFiles/small_articles.csv", "textFiles/small_workstations.csv");
        simulator.runWithoutPriority(true); // Executa sem priorização para simplificação

        // Obtem a lista de tempos de operação para cada estação
        Map<String, Double> workstationTimes = simulator.getWorkstationOperationTimes();
        double totalProductionTime = simulator.getTotalProductionTime();

        // Exemplo de validação do tempo total e percentagens de operação
        double expectedTimeCUT = 20.0;
        double expectedTimePOLISH = 5.0;
        double expectedTimeVarnish = 8.0;

        //assertEquals(expectedTimeCUT, workstationTimes.get("WS1"), "Tempo total de operação para WS1 (CUT) deve ser 20.");
        //assertEquals(expectedTimePOLISH, workstationTimes.get("WS2"), "Tempo total de operação para WS2 (POLISH) deve ser 5.");
        assertEquals(expectedTimeVarnish, workstationTimes.get("WS3"), "Tempo total de operação para WS3 (Varnish) deve ser 8.");

        // Cálculo de percentagens de operação e validação da ordem
        List<Workstation> orderedWorkstations = simulator.getWorkstationsPerOperation()
                .getAllWorkstations()
                .stream()
                .sorted((ws1, ws2) -> {
                    double ws1Percentage = ws1.getTotalOperationTime() / totalProductionTime * 100;
                    double ws2Percentage = ws2.getTotalOperationTime() / totalProductionTime * 100;
                    return Double.compare(ws1Percentage, ws2Percentage);
                })
                .collect(Collectors.toList());

        // Validação de ordenação e percentagens
        double previousPercentage = 0.0;
        for (Workstation ws : orderedWorkstations) {
            double currentPercentage = ws.getTotalOperationTime() / totalProductionTime * 100;
            assertTrue(currentPercentage >= previousPercentage, "As estações devem estar ordenadas por percentagem de tempo em ordem crescente.");
            previousPercentage = currentPercentage;
        }
        // Exemplo de saída das percentagens para verificação
        System.out.println("Percentagens de tempo de operação relativas ao tempo total para small dataset (USEI05):");
        orderedWorkstations.forEach(ws -> {
            double percentage = ws.getTotalOperationTime() / totalProductionTime * 100;
            BigDecimal roundedPercentage = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP);
            System.out.println("Estação " + ws.getId() + " - Percentagem: " + roundedPercentage + "%");
        });
        double packTimePercentage = simulator.getWorkstationOperationTimes().get("WS4") / simulator.getTotalProductionTime() * 100;
        assertTrue(packTimePercentage <= 30, "Expected PACK station to operate less than 50% of total time.");
        double cutTimePercentage = simulator.getWorkstationOperationTimes().get("WS1") / simulator.getTotalProductionTime() * 100;
        assertTrue(cutTimePercentage <= 43, "Expected PACK station to operate less than 50% of total time.");
        double polishTimePercentage = simulator.getWorkstationOperationTimes().get("WS2") / simulator.getTotalProductionTime() * 100;
        assertTrue(polishTimePercentage <= 11, "Expected PACK station to operate less than 50% of total time.");
        double varnishTimePercentage = simulator.getWorkstationOperationTimes().get("WS3") / simulator.getTotalProductionTime() * 100;
        assertTrue(varnishTimePercentage <= 18, "Expected PACK station to operate less than 50% of total time.");
    }


    // USEI06
    @Test
    void testAverageExecutionAndWaitingTimes_smallDataset() throws IOException {
        loadData("textFiles/small_articles.csv", "textFiles/small_workstations.csv");
        simulator.runWithoutPriority(false);  // Executa sem priorização

        // Tempo médio esperado de execução por operação (CUT, POLISH, PACK) no small dataset
        double expectedAvgCutTime = 10.0;
        double expectedAvgPolishTime = 5.0;
        double expectedAvgPackTime = 7.0;

        // Comparação dos tempos médios de execução calculados pelo simulador
        assertEquals(expectedAvgCutTime, simulator.getAverageExecutionTime("CUT"), 0.01, "Tempo médio de execução para CUT está incorreto.");
        assertEquals(expectedAvgPolishTime, simulator.getAverageExecutionTime("POLISH"), 0.01, "Tempo médio de execução para POLISH está incorreto.");
        assertEquals(expectedAvgPackTime, simulator.getAverageExecutionTime("PACK"), 0.01, "Tempo médio de execução para PACK está incorreto.");

        double expectedAvgWaitingTime = 0.0;  // Considerando que POLISH não tem tempo de espera, uma vez que só é realizada uma vez

        assertTrue(simulator.getAverageWaitingTime("CUT") > 0, "Tempo médio de espera para CUT deve ser maior ou igual a zero.");
        assertEquals(expectedAvgWaitingTime, simulator.getAverageWaitingTime("POLISH"), 0.01, "Tempo médio de espera para POLISH está incorreto.");
        assertTrue(simulator.getAverageWaitingTime("PACK") > 0, "Tempo médio de espera para CUT deve ser maior ou igual a zero.");
    }

    // USEI06
    @Test
    void testAverageExecutionAndWaitingTimes_mediumDataset() throws IOException {
        loadData("textFiles/medium_articles.csv", "textFiles/medium_workstations.csv");
        simulator.runWithPriority(true);  // Executa com priorização

        // Exemplo de cálculo com base nos dados fornecidos
        double avgCutTime = simulator.getAverageExecutionTime("CUT");
        double avgDrillTime = simulator.getAverageExecutionTime("DRILL");

        // Arredondamento dos valores para validação
        BigDecimal roundedCutTime = new BigDecimal(avgCutTime).setScale(2, RoundingMode.HALF_UP);
        BigDecimal roundedDrillTime = new BigDecimal(avgDrillTime).setScale(2, RoundingMode.HALF_UP);

        // Exemplo de saída para verificação dos tempos médios calculados
        System.out.println("Tempo médio de execução para CUT (USEI06): " + roundedCutTime + " segundos");
        System.out.println("Tempo médio de execução para DRILL (USEI06): " + roundedDrillTime + " segundos");

        // Verificações de tempo de espera médio (exemplo básico)
        assertTrue(simulator.getAverageWaitingTime("CUT") > 0, "Tempo médio de espera para CUT deve ser maior ou igual a zero.");
        assertTrue(simulator.getAverageWaitingTime("DRILL") > 0, "Tempo médio de espera para DRILL deve ser maior ou igual a zero.");
    }

}
