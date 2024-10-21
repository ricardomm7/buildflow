package fourcorp.buildflow.repository;

import fourcorp.buildflow.application.Simulator;
import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class WorkstationsPerOperationTest {

    @Test
    void findBestMachineForOperation() {
        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 5);
        Workstation ws3 = new Workstation("WS3", 15);
        ws3.setAvailable(false);

        Operation operation = new Operation("Welding");

        WorkstationsPerOperation w = new WorkstationsPerOperation();
        w.create(ws1, operation);
        w.create(ws2, operation);
        w.create(ws3, operation);

        Workstation bestMachine = w.findBestMachineForOperation(operation);

        assertEquals("WS2", bestMachine.getId(), "The fastest machine available should be WS2.");
    }

    @Test
    void findBestMachineForOperation2() {
        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 5);
        Workstation ws3 = new Workstation("WS3", 15);
        ws2.setAvailable(false);

        Operation operation = new Operation("Welding");

        WorkstationsPerOperation w = new WorkstationsPerOperation();
        w.create(ws1, operation);
        w.create(ws2, operation);
        w.create(ws3, operation);

        Workstation bestMachine = w.findBestMachineForOperation(operation);

        assertEquals("WS1", bestMachine.getId(), "The fastest machine available should be WS2.");
    }

    @Test
    void findBestMachineForOperation3() {
        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 5);
        Workstation ws3 = new Workstation("WS3", 15);
        ws1.setAvailable(false);
        ws2.setAvailable(false);
        ws3.setAvailable(false);

        Operation operation = new Operation("Welding");

        WorkstationsPerOperation w = new WorkstationsPerOperation();
        w.create(ws1, operation);
        w.create(ws2, operation);
        w.create(ws3, operation);

        assertNull(w.findBestMachineForOperation(operation), "Should be null!");
    }

    @Test
    void getWorkstationsAscendingByPercentage() {
        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 5);
        Workstation ws3 = new Workstation("WS3", 15);

        WorkstationsPerOperation w = new WorkstationsPerOperation();
        w.create(ws1, new Operation("Cutting"));
        w.create(ws2, new Operation("Painting"));
        w.create(ws3, new Operation("Assembling"));

        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Painting"), new Operation("Assembling"))));

        ProductPriorityLine o = new ProductPriorityLine();
        o.create(product1, PriorityOrder.LOW);

        Simulator s = new Simulator(w, o);
        s.runWithoutPriority();
        List<Workstation> a = w.getWorkstationsAscendingByPercentage();

        assertEquals(3, a.size(), "Should return 3 machines.");
        assertEquals(100, (ws1.getTotalOperationTime() / ws1.getTotalExecutionTime()) * 100, 0.0001, "WS1 should have 100%.");
        assertEquals(100, (ws2.getTotalOperationTime() / ws2.getTotalExecutionTime()) * 100, 0.0001, "WS2 should have.");
        assertEquals(100, (ws3.getTotalOperationTime() / ws3.getTotalExecutionTime()) * 100, 0.0001, "WS3 should have.");
    }

    @Test
    void getWorkstationsAscendingByPercentage2() {
        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 5);

        WorkstationsPerOperation w = new WorkstationsPerOperation();
        w.create(ws1, new Operation("Cutting"));
        w.create(ws2, new Operation("Assembling"));

        Product product1 = new Product("P001", new LinkedList<>(List.of(new Operation("Cutting"), new Operation("Assembling"))));
        Product product2 = new Product("P002", new LinkedList<>(List.of(new Operation("Cutting"))));

        ProductPriorityLine o = new ProductPriorityLine();
        o.create(product1, PriorityOrder.LOW);
        o.create(product2, PriorityOrder.LOW);

        Simulator s = new Simulator(w, o);
        s.runWithoutPriority();
        List<Workstation> a = w.getWorkstationsAscendingByPercentage();

        s.printAnalysis();

        assertEquals(2, a.size(), "Should return 1 machines.");
        assertEquals(80, (ws1.getTotalOperationTime() / ws1.getTotalExecutionTime()) * 100, 0.0001, "WS1 should have 100%.");
    }
}