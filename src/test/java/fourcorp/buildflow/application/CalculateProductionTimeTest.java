package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class CalculateProductionTimeTest {

    @BeforeEach
    void setup() {
        Reader.products.clear();
        Reader.machines.clear();
    }

    @Test
    void testCalculateTotalProductionTime_AllOperationsProcessed() {
        Product product1 = new Product("P001", null, new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding"), new Operation("Polishing")
        )));
        Workstation cuttingMachine = new Workstation("M001", "Cutting", 10);
        Workstation weldingMachine = new Workstation("M002", "Welding", 15);
        Workstation polishingMachine = new Workstation("M003", "Polishing", 5);
        Reader.products.add(product1);
        Reader.machines.addAll(Arrays.asList(cuttingMachine, weldingMachine, polishingMachine));

        CalculateProductionTime.calculateTotalProductionTime();
        assertTrue(Reader.machines.isEmpty(), "All machines should have been used and removed.");
    }

    @Test
    void testCalculateTotalProductionTime_NoMachineForOperation() {
        Product product1 = new Product("P001", PriorityOrder.MEDIUM, new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding"), new Operation("Polishing")
        )));
        Workstation cuttingMachine = new Workstation("M001", "Cutting", 10);
        Reader.products.add(product1);
        Reader.machines.add(cuttingMachine); // No machines for Welding and Polishing

        CalculateProductionTime.calculateTotalProductionTime();
        assertEquals(0, Reader.machines.size(), "One machine should remain since no other machines were found.");
    }

    @Test
    void testFindFastestMachine() {
        Workstation machine1 = new Workstation("M001", "Cutting", 15);
        Workstation machine2 = new Workstation("M002", "Cutting", 10);
        Workstation machine3 = new Workstation("M003", "Cutting", 20);
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
        Product product1 = new Product("P001", PriorityOrder.HIGH, new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding")
        )));
        Product product2 = new Product("P002", PriorityOrder.LOW, new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Polishing")
        )));
        Workstation cuttingMachine = new Workstation("M001", "Cutting", 10);
        Workstation weldingMachine = new Workstation("M002", "Welding", 15);
        Workstation polishingMachine = new Workstation("M003", "Polishing", 5);
        Reader.products.addAll(Arrays.asList(product1, product2));
        Reader.machines.addAll(Arrays.asList(cuttingMachine, weldingMachine, polishingMachine));

        CalculateProductionTime.calculateTotalProductionTime();
        assertTrue(Reader.machines.isEmpty(), "All machines should have been used and removed.");
    }

    @Test
    void testCalculateTotalProductionTime_ProductWithNoPriority() {
        Product product = new Product("P001", null, new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding")
        )));
        Workstation cuttingMachine = new Workstation("M001", "Cutting", 10);
        Workstation weldingMachine = new Workstation("M002", "Welding", 15);
        Reader.products.add(product);
        Reader.machines.addAll(Arrays.asList(cuttingMachine, weldingMachine));

        CalculateProductionTime.calculateTotalProductionTime();
        assertTrue(Reader.machines.isEmpty(), "All machines should have been used and removed.");
    }

    @Test
    void testCalculateTotalProductionTime_ProductWithNoOperations() {
        Product product = new Product("P001", PriorityOrder.MEDIUM, new LinkedList<>());
        Workstation cuttingMachine = new Workstation("M001", "Cutting", 10);
        Reader.products.add(product);
        Reader.machines.add(cuttingMachine);

        CalculateProductionTime.calculateTotalProductionTime();
        assertEquals(1, Reader.machines.size(), "No machines should have been used.");
    }

    @Test
    void testFindFastestMachine_MultipleMachinesWithSameTime() {
        Workstation machine1 = new Workstation("M001", "Cutting", 10);
        Workstation machine2 = new Workstation("M002", "Cutting", 10);
        Workstation machine3 = new Workstation("M003", "Cutting", 10);
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
        Workstation machine = new Workstation("M001", "Cutting", 15);
        LinkedList<Workstation> workstations = new LinkedList<>(Arrays.asList(machine));
        Workstation fastestMachine = CalculateProductionTime.findFastestMachine(workstations);

        assertNotNull(fastestMachine);
        assertEquals("M001", fastestMachine.getIdMachine(), "The only machine should be returned.");
    }
}
