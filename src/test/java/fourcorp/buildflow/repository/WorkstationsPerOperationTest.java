package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Workstation;
import org.junit.jupiter.api.Test;

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
}