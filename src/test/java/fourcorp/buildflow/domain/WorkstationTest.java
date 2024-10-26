package fourcorp.buildflow.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkstationTest {

    private Workstation workstation;
    private Product product;

    @BeforeEach
    void setUp() {
        workstation = new Workstation("WS123", 10);
        product = new Product("Product1", new LinkedList<>(List.of(new Operation("Cutting"), new Operation("Assembling"))));
    }

    @Test
    void testWorkstationInitialization() {
        assertEquals("WS123", workstation.getId(), "Workstation ID should be initialized correctly.");
        assertEquals(10, workstation.getTime(), "Workstation time should be initialized correctly.");
        assertTrue(workstation.isAvailable(), "Workstation should be available on initialization.");
    }

    @Test
    void testProcessProduct() {
        workstation.processProduct(product);
        assertEquals(1, workstation.getOprCounter(), "Operation counter should increase after processing a product.");
        assertEquals(10, workstation.getTotalOperationTime(), "Total operation time should equal workstation time after one operation.");
        assertFalse(workstation.isAvailable(), "Workstation should be busy after starting product processing.");
    }

    @Test
    void testStopClock() {
        workstation.startClock(true);
        int totalWait = workstation.stopClock();
        assertTrue(totalWait >= 0, "Total waiting time should be non-negative.");
        assertEquals(1, workstation.contWaiting, "Waiting counter should increment after stopping the clock.");
    }

    @Test
    void testAverageExecutionTimePerOperation() {
        workstation.processProduct(product);
        String avgExecTime = workstation.getAverageExecutionTimePerOperation();
        assertTrue(avgExecTime.contains("Average Execution Time = 10.0"), "Average execution time should be equal to workstation time for one operation.");
    }

    @Test
    void testAverageWaitingTime() {
        workstation.startClock(true);
        workstation.stopClock();
        String avgWaitTime = workstation.getAverageWaitingTime();
        assertTrue(avgWaitTime.contains("Average Waiting Time = "), "Average waiting time should be calculated.");
    }

    @Test
    void testIncrementOperationCounter() {
        workstation.increaseOpCounter();
        assertEquals(1, workstation.getOprCounter(), "Operation counter should increase when incremented.");
    }

    @Test
    void testSetAndGetTime() {
        workstation.setTime(15);
        assertEquals(15, workstation.getTime(), "Workstation time should update correctly.");
    }

    @Test
    void testAvailabilityToggle() {
        workstation.setAvailable(false);
        assertFalse(workstation.isAvailable(), "Workstation should be unavailable after setting available to false.");
        workstation.setAvailable(true);
        assertTrue(workstation.isAvailable(), "Workstation should be available after setting available to true.");
    }

    @Test
    void testGetTotalExecutionTime() {
        workstation.processProduct(product);
        double totalExecutionTime = workstation.getTotalExecutionTime();
        assertTrue(totalExecutionTime >= 10, "Total execution time should account for operation time and waiting time.");
    }

    @Test
    void testEquality() {
        Workstation sameWs = new Workstation("WS123", 15);
        Workstation differentWs = new Workstation("WS456", 15);
        assertEquals(workstation, sameWs, "Workstation with the same ID should be equal.");
        assertNotEquals(workstation, differentWs, "Workstation with a different ID should not be equal.");
    }
}
