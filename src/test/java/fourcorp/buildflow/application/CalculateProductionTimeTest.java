package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class CalculateProductionTimeTest {

    public static ProductPriorityLine p = Repositories.getInstance().getProductPriorityRepository();
    public static WorkstationsPerOperation w = Repositories.getInstance().getWorkstationsPerOperation();

    @Test
    void testCalculateTotalProductionTime_AllOperationsProcessed() {
        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding"), new Operation("Polishing")
        )));
        Workstation cuttingMachine = new Workstation("M001", 10);
        Workstation weldingMachine = new Workstation("M002", 15);
        Workstation polishingMachine = new Workstation("M003", 5);
        p.create(product1, null);
        w.create(cuttingMachine, new Operation("Cutting"));
        w.create(weldingMachine, new Operation("Welding"));
        w.create(polishingMachine, new Operation("Polishing"));

        CalculateProductionTime.calculateTotalProductionTime();
        assertTrue(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).isEmpty(), "All machines should have been used and removed.");
        assertTrue(w.getWorkstationsPerOperation().getByKey(new Operation("Welding")).isEmpty(), "All machines should have been used and removed.");
        assertTrue(w.getWorkstationsPerOperation().getByKey(new Operation("Polishing")).isEmpty(), "All machines should have been used and removed.");
    }

    @Test
    void testCalculateTotalProductionTime_NoMachineForOperation() {
        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding"), new Operation("Polishing")
        )));
        Workstation cuttingMachine = new Workstation("M001", 10);
        p.create(product1, PriorityOrder.MEDIUM);
        w.create(cuttingMachine, new Operation("Cutting")); // No machines for Welding and Polishing

        CalculateProductionTime.calculateTotalProductionTime();
        assertEquals(0, w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).size(), "One machine should remain since no other machines were found.");
    }

    @Test
    void testFindFastestMachine() {
        Workstation machine1 = new Workstation("M001", 15);
        Workstation machine2 = new Workstation("M002", 10);
        Workstation machine3 = new Workstation("M003", 20);
        LinkedList<Workstation> workstations = new LinkedList<>(Arrays.asList(machine1, machine2, machine3));
        Workstation fastestMachine = CalculateProductionTime.findFastestMachine(workstations);

        assertNotNull(fastestMachine);
        assertEquals("M002", fastestMachine.getIdMachine(), "The fastest machine should be M002 with 10 minutes time.");
    }

    @Test
    void testFindFastestMachine_EmptyList() {
        LinkedList<Workstation> emptyWorkstations = new LinkedList<>();
        Workstation fastestMachine = CalculateProductionTime.findFastestMachine(emptyWorkstations);
        assertNull(fastestMachine, "No machines present, so the result should be null.");
    }


    @Test
    void testCalculateTotalProductionTime_MultipleProducts() {
        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding")
        )));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Polishing")
        )));
        Workstation cuttingMachine = new Workstation("M001", 10);
        Workstation weldingMachine = new Workstation("M002", 15);
        Workstation polishingMachine = new Workstation("M003", 5);
        p.create(product1, PriorityOrder.HIGH);
        p.create(product2, PriorityOrder.LOW);
        w.create(cuttingMachine, new Operation("Cutting"));
        w.create(weldingMachine, new Operation("Welding"));
        w.create(polishingMachine, new Operation("Polishing"));

        CalculateProductionTime.calculateTotalProductionTime();
        assertTrue(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).isEmpty(), "All machines should have been used and removed.");
        assertTrue(w.getWorkstationsPerOperation().getByKey(new Operation("Welding")).isEmpty(), "All machines should have been used and removed.");
        assertTrue(w.getWorkstationsPerOperation().getByKey(new Operation("Polishing")).isEmpty(), "All machines should have been used and removed.");
    }

    @Test
    void testCalculateTotalProductionTime_ProductWithNoPriority() {
        Product product = new Product("P001", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding")
        )));
        Workstation cuttingMachine = new Workstation("M001", 10);
        Workstation weldingMachine = new Workstation("M002", 15);
        p.create(product, null);
        w.create(cuttingMachine, new Operation("Cutting"));
        w.create(weldingMachine, new Operation("Welding"));

        CalculateProductionTime.calculateTotalProductionTime();
        assertTrue(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).isEmpty(), "All machines should have been used and removed.");
        assertTrue(w.getWorkstationsPerOperation().getByKey(new Operation("Welding")).isEmpty(), "All machines should have been used and removed.");
    }

    @Test
    void testCalculateTotalProductionTime_ProductWithNoOperations() {
        Product product = new Product("P001", new LinkedList<>());
        Workstation cuttingMachine = new Workstation("M001", 10);
        p.create(product, PriorityOrder.MEDIUM);
        w.create(cuttingMachine, new Operation("Cutting"));

        CalculateProductionTime.calculateTotalProductionTime();
        assertEquals(1, w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).size(), "No machines should have been used.");
    }

    @Test
    void testFindFastestMachine_MultipleMachinesWithSameTime() {
        Workstation machine1 = new Workstation("M001", 10);
        Workstation machine2 = new Workstation("M002", 10);
        Workstation machine3 = new Workstation("M003", 10);
        LinkedList<Workstation> workstations = new LinkedList<>(Arrays.asList(machine1, machine2, machine3));
        Workstation fastestMachine = CalculateProductionTime.findFastestMachine(workstations);

        assertNotNull(fastestMachine);
        assertTrue(fastestMachine.getIdMachine().equals("M001") ||
                        fastestMachine.getIdMachine().equals("M002") ||
                        fastestMachine.getIdMachine().equals("M003"),
                "One of the machines with 10 minutes time should be returned.");
    }

    @Test
    void testFindFastestMachine_SingleMachine() {
        Workstation machine = new Workstation("M001", 15);
        LinkedList<Workstation> workstations = new LinkedList<>(Arrays.asList(machine));
        Workstation fastestMachine = CalculateProductionTime.findFastestMachine(workstations);

        assertNotNull(fastestMachine);
        assertEquals("M001", fastestMachine.getIdMachine(), "The only machine should be returned.");
    }
}
