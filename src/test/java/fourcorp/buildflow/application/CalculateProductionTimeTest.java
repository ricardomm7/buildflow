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

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculateProductionTimeTest {

    private static ProductPriorityLine p;
    private static WorkstationsPerOperation w;

    @BeforeEach
    void setUp() {
        p = Repositories.getInstance().getProductPriorityRepository();
        w = Repositories.getInstance().getWorkstationsPerOperation();
        p.removeAll();
        w.removeAll();
    }

    @Test
    void testCalculateTotalProductionTime_UnavailableMachine() {
        Product product = new Product("P001", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding")
        )));
        Workstation cuttingMachine = new Workstation("M001", 100000000);
        Workstation weldingMachine = new Workstation("M002", 15000000);
        weldingMachine.setAvailable(false);
        p.create(product, PriorityOrder.NORMAL);
        w.create(cuttingMachine, new Operation("Cutting"));
        w.create(weldingMachine, new Operation("Welding"));

        CalculateProductionTime.calculateTotalProductionTime();
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).get(0).isAvailable(), "Cutting machine should be unavailable after use.");
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Welding")).get(0).isAvailable(), "Welding machine should still be unavailable.");
    }


    @Test
    void testCalculateTotalProductionTime_DifferentPriorities() {
        Product highPriorityProduct = new Product("P001", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding")
        )));
        Product lowPriorityProduct = new Product("P002", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding")
        )));
        Workstation cuttingMachine = new Workstation("M001", 1000000);
        Workstation weldingMachine = new Workstation("M002", 1500000);
        p.create(highPriorityProduct, PriorityOrder.HIGH);
        p.create(lowPriorityProduct, PriorityOrder.LOW);
        w.create(cuttingMachine, new Operation("Cutting"));
        w.create(weldingMachine, new Operation("Welding"));

        CalculateProductionTime.calculateTotalProductionTime();
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).get(0).isAvailable(), "Cutting machine should be unavailable after use.");
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Welding")).get(0).isAvailable(), "Welding machine should be unavailable after use.");
    }

    @Test
    void testCalculateTotalProductionTime_MachineDependencies() {
        Product product = new Product("P001", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding"), new Operation("Polishing")
        )));
        Workstation cuttingMachine = new Workstation("M001", 100000);
        Workstation weldingMachine = new Workstation("M002", 1000005);
        Workstation polishingMachine = new Workstation("M003", 500000);
        p.create(product, PriorityOrder.NORMAL);
        w.create(cuttingMachine, new Operation("Cutting"));
        w.create(weldingMachine, new Operation("Welding"));
        w.create(polishingMachine, new Operation("Polishing"));

        CalculateProductionTime.calculateTotalProductionTime();
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).get(0).isAvailable(), "Cutting machine should be unavailable after use.");
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Welding")).get(0).isAvailable(), "Welding machine should be unavailable after use.");
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Polishing")).get(0).isAvailable(), "Polishing machine should be unavailable after use.");
    }


    @Test
    void testCalculateTotalProductionTime_FastestMachineSelection() {
        Product product = new Product("P001", new LinkedList<>(Arrays.asList(
                new Operation("Cutting")
        )));
        Workstation fastCuttingMachine = new Workstation("M001", 100000);
        Workstation slowCuttingMachine = new Workstation("M002", 200000);
        p.create(product, PriorityOrder.NORMAL);
        w.create(fastCuttingMachine, new Operation("Cutting"));
        w.create(slowCuttingMachine, new Operation("Cutting"));

        CalculateProductionTime.calculateTotalProductionTime();
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).get(0).isAvailable(), "Fast cutting machine should be unavailable after use.");
        assertTrue(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).get(1).isAvailable(), "Slow cutting machine should still be available.");
    }

    @Test
    void testCalculateTotalProductionTime_AvailableMachineSelection() {
        Product product = new Product("P001", new LinkedList<>(Arrays.asList(
                new Operation("Cutting")
        )));
        Workstation unavailableCuttingMachine = new Workstation("M001", 100000);
        Workstation availableCuttingMachine = new Workstation("M002", 150000);
        unavailableCuttingMachine.setAvailable(false);
        p.create(product, PriorityOrder.NORMAL);
        w.create(unavailableCuttingMachine, new Operation("Cutting"));
        w.create(availableCuttingMachine, new Operation("Cutting"));

        CalculateProductionTime.calculateTotalProductionTime();
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).get(0).isAvailable(), "Unavailable cutting machine should remain unavailable.");
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).get(1).isAvailable(), "Available cutting machine should be unavailable after use.");
    }


    @Test
    void testCalculateTotalProductionTime_MultipleProducts() {
        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding")
        )));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Polishing")
        )));
        Workstation cuttingMachine = new Workstation("M001", 10000);
        Workstation weldingMachine = new Workstation("M002", 150000);
        Workstation polishingMachine = new Workstation("M003", 200000);
        p.create(product1, PriorityOrder.NORMAL);
        p.create(product2, PriorityOrder.NORMAL);
        w.create(cuttingMachine, new Operation("Cutting"));
        w.create(weldingMachine, new Operation("Welding"));
        w.create(polishingMachine, new Operation("Polishing"));

        CalculateProductionTime.calculateTotalProductionTime();
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).get(0).isAvailable(), "Cutting machine should be unavailable after use.");
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Welding")).get(0).isAvailable(), "Welding machine should be unavailable after use.");
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Polishing")).get(0).isAvailable(), "Polishing machine should be unavailable after use.");
    }

    @Test
    void testCalculateTotalProductionTime_NoAvailableMachines() {
        Product product = new Product("P001", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding")
        )));
        Workstation cuttingMachine = new Workstation("M001", 100000);
        Workstation weldingMachine = new Workstation("M002", 150000);
        cuttingMachine.setAvailable(false);
        weldingMachine.setAvailable(false);
        p.create(product, PriorityOrder.NORMAL);
        w.create(cuttingMachine, new Operation("Cutting"));
        w.create(weldingMachine, new Operation("Welding"));

        CalculateProductionTime.calculateTotalProductionTime();
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).get(0).isAvailable(), "Cutting machine should remain unavailable.");
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Welding")).get(0).isAvailable(), "Welding machine should remain unavailable.");
    }

    @Test
    void testCalculateTotalProductionTime_MissingOperation() {
        Product product = new Product("P001", new LinkedList<>(Arrays.asList(
                new Operation("Cutting"), new Operation("Welding"), new Operation("Painting")
        )));
        Workstation cuttingMachine = new Workstation("M001", 100000);
        Workstation weldingMachine = new Workstation("M002", 150000);
        p.create(product, PriorityOrder.NORMAL);
        w.create(cuttingMachine, new Operation("Cutting"));
        w.create(weldingMachine, new Operation("Welding"));

        CalculateProductionTime.calculateTotalProductionTime();
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Cutting")).get(0).isAvailable(), "Cutting machine should be unavailable after use.");
        assertFalse(w.getWorkstationsPerOperation().getByKey(new Operation("Welding")).get(0).isAvailable(), "Welding machine should be unavailable after use.");
    }

    @Test
    void testCalculateTotalProductionTime_EmptyProductList() {
        CalculateProductionTime.calculateTotalProductionTime();
        assertTrue(w.getWorkstationsPerOperation().isEmpty(), "Workstations should remain empty.");
    }

    @Test
    void testCalculateTotalProductionTime_ProductWithNoOperations() {
        Product product = new Product("P001", new LinkedList<>());
        p.create(product, PriorityOrder.NORMAL);

        CalculateProductionTime.calculateTotalProductionTime();
        assertTrue(w.getWorkstationsPerOperation().isEmpty(), "Workstations should remain empty.");
    }
}
