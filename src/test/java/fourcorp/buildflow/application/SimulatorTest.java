package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.WorkstationsPerOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulatorTest {

    private Simulator simulator;
    private ProductPriorityLine productLine;
    private WorkstationsPerOperation workstationsPerOperation;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void testRunWithPriority() {
        simulator.runWithPriority(true);
        assertTrue(simulator.getTotalProductionTime() > 0, "Total production time should be positive after running simulation.");
    }

    @Test
    void testRunWithoutPriority() {
        simulator.runWithoutPriority(false);
        assertTrue(simulator.getTotalProductionTime() > 0, "Total production time should be positive after running simulation.");
    }

    @Test
    void testProcessingHighPriorityProducts() {
        simulator.runWithPriority(true);
        List<Product> highPriorityProducts = productLine.getProductsByPriority(PriorityOrder.HIGH);
        for (Product product : highPriorityProducts) {
            assertTrue(product.hasMoreOperations(), "High-priority products should be processed first.");
        }
    }

    @Test
    void testProcessingNormalPriorityProductsAfterHighPriority() {
        simulator.runWithPriority(true);
        List<Product> normalPriorityProducts = productLine.getProductsByPriority(PriorityOrder.NORMAL);
        for (Product product : normalPriorityProducts) {
            assertTrue(product.hasMoreOperations(), "Normal-priority products should be processed after high-priority products.");
        }
    }

    @Test
    void testWorkstationOperationTimes() {
        simulator.runWithoutPriority(false);
        for (Workstation workstation : workstationsPerOperation.getAllWorkstations()) {
            assertTrue(workstation.getTotalOperationTime() > 0, "Workstations should accumulate operation time during simulation.");
        }
    }

    @Test
    void testProductTimesCalculation() {
        simulator.runWithoutPriority(false);
        double totalProductionTime = simulator.getTotalProductionTime();
        assertTrue(totalProductionTime > 0, "Total production time should reflect cumulative product operation times.");
    }

    @Test
    void testOperationTimesCalculation() {
        simulator.runWithoutPriority(false);
        double sumOfOperationTimes = simulator.getTotalProductionTime();
        assertEquals(17, sumOfOperationTimes, "Sum of operation times should equal total production time.");
    }

    @Test
    void testProcessWaitingQueue() {
        simulator.runWithPriority(true);
        assertFalse(simulator.areProductsQueueEmpty(), "The products queue should not be empty. EVER!");
    }

    @Test
    void testPrintProductionStatistics() {
        simulator.runWithoutPriority(false);
        simulator.printProductionStatistics();
        assertTrue(simulator.getTotalProductionTime() > 0, "Production statistics should reflect the production time.");
    }

    @Test
    void testPrintAnalysis() {
        simulator.runWithoutPriority(false);
        simulator.printAnalysis();
        assertTrue(simulator.getTotalProductionTime() > 0, "Analysis should display workstation operation times and percentages.");
    }

    @Test
    void testResetSimulation() {
        simulator.runWithoutPriority(false);
        simulator.resetSimulation();
        assertEquals(0, simulator.getTotalProductionTime(), "Total production time should be reset to zero.");
        assertFalse(simulator.areProductsQueueEmpty(), "The products queue should not be empty. EVER!");
    }
}
