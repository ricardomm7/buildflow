package fourcorp.buildflow.repository;

import fourcorp.buildflow.application.Simulator;
import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkstationsPerOperationTest {
    private WorkstationsPerOperation workstationsPerOperation;
    private Workstation ws1;
    private Workstation ws2;
    private Workstation ws3;
    private Operation op1;
    private Operation op2;

    @BeforeEach
    void setUp() {
        workstationsPerOperation = new WorkstationsPerOperation();
        ws1 = new Workstation("WS1", 8);
        ws2 = new Workstation("WS2", 5);
        ws3 = new Workstation("WS3", 3);
        op1 = new Operation("Cutting");
        op2 = new Operation("Assembling");

        ws1.setAvailable(true);
        ws3.setAvailable(false);

        workstationsPerOperation.create(ws1, op1);
        workstationsPerOperation.create(ws2, op1);
        workstationsPerOperation.create(ws3, op2);
    }

    @Test
    void testGetWorkstationsByOperationAvailableOnly() {
        List<Workstation> availableWorkstations = workstationsPerOperation.getWorkstationsByOperation(op1, false);
        assertEquals(2, availableWorkstations.size(), "Should return 2 available workstations for 'Cutting' operation.");
        assertTrue(availableWorkstations.contains(ws1));
        assertTrue(availableWorkstations.contains(ws2));
    }

    @Test
    void testGetWorkstationsByOperationSortedByTime() {
        List<Workstation> sortedWorkstations = workstationsPerOperation.getWorkstationsByOperation(op1, true);
        assertEquals(2, sortedWorkstations.size(), "Should return 2 available workstations sorted by time.");
        assertEquals(ws2, sortedWorkstations.get(0), "WS2 should be first since it has less operation time.");
        assertEquals(ws1, sortedWorkstations.get(1), "WS1 should be second with more operation time.");
    }

    @Test
    void testRemoveWorkstation() {
        workstationsPerOperation.removeWorkstation(ws1, op1);
        List<Workstation> workstations = workstationsPerOperation.getWorkstationsByOperation(op1, false);
        assertEquals(1, workstations.size(), "Should return 1 workstation after removal.");
        assertFalse(workstations.contains(ws1), "WS1 should be removed from 'Cutting' operation.");
    }

    @Test
    void testRemoveAllWorkstations() {
        workstationsPerOperation.removeAll();
        List<Workstation> allWorkstations = workstationsPerOperation.getAllWorkstations();
        assertEquals(0, allWorkstations.size(), "Should return an empty list after removing all workstations.");
    }

    @Test
    void testGetAllWorkstations() {
        List<Workstation> allWorkstations = workstationsPerOperation.getAllWorkstations();
        assertEquals(3, allWorkstations.size(), "Should return all 3 workstations.");
        assertTrue(allWorkstations.contains(ws1));
        assertTrue(allWorkstations.contains(ws2));
        assertTrue(allWorkstations.contains(ws3));
    }

    @Test
    void getWorkstationsAscendingByPercentage() {
        Workstation ws543 = new Workstation("WS65", 10);
        Workstation ws547 = new Workstation("WS66", 5);

        WorkstationsPerOperation u = new WorkstationsPerOperation();
        u.create(ws543, new Operation("Cutting"));
        u.create(ws547, new Operation("Assembling"));

        Product p4698 = new Product("P006", new LinkedList<>(List.of(new Operation("Cutting"), new Operation("Assembling"))));
        Product p4390 = new Product("P012", new LinkedList<>(List.of(new Operation("Cutting"))));

        ProductPriorityLine uisfd = new ProductPriorityLine();
        uisfd.create(p4698, PriorityOrder.LOW);
        uisfd.create(p4390, PriorityOrder.LOW);

        Simulator xo = new Simulator(u, uisfd);
        xo.runWithoutPriority(true);
        List<Workstation> urghoer = u.getWorkstationsAscendingByPercentage(25);

        assertEquals(2, urghoer.size(), "Should return 2 machines.");
        assertEquals(80.0, (ws543.getTotalOperationTime() / 25) * 100, "WS65 should have 80%.");
    }
}