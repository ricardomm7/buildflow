package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CalculateProductionTimeTest {


    @Test
    public void test_calculate_total_production_time_with_available_machines() {
        Reader.products.put("P1", new Product("P1", 1, Arrays.asList("Op1", "Op2")));
        Reader.machinesPerOperation.put("Op1", new LinkedList<>(Arrays.asList(new Workstation("M1", "Op1", 10))));
        Reader.machinesPerOperation.put("Op2", new LinkedList<>(Arrays.asList(new Workstation("M2", "Op2", 20))));

        CalculateProductionTime.calculateTotalProductionTime();

        // Expected output: Total production time for the article P1: 30 minutes
    }

    @Test
    public void test_find_fastest_machine_for_each_operation() {
        LinkedList<Workstation> workstations = new LinkedList<>();
        workstations.add(new Workstation("M1", "Op1", 15));
        workstations.add(new Workstation("M2", "Op1", 10));
        workstations.add(new Workstation("M3", "Op1", 20));

        Workstation fastestWorkstation = CalculateProductionTime.findFastestMachine(workstations);

        assertEquals("M2", fastestWorkstation.getIdMachine());
        assertEquals(10, fastestWorkstation.getTime());
    }

    @Test
    public void test_handle_products_with_no_available_machines() {
        Reader.products.put("P2", new Product("P2", 1, Arrays.asList("Op3")));

        CalculateProductionTime.calculateTotalProductionTime();

        // Expected output: No machine found for the operation: Op3 of the article: P2
    }

    @Test
    public void test_manage_empty_product_list_without_errors() {
        Reader.products.clear();

        CalculateProductionTime.calculateTotalProductionTime();

        // Expected output: No output, no errors should occur
    }

    @Test
    public void test_process_operations_with_multiple_machines_same_time() {
        LinkedList<Workstation> workstations = new LinkedList<>();
        workstations.add(new Workstation("M1", "Op4", 10));
        workstations.add(new Workstation("M2", "Op4", 10));

        Workstation fastestWorkstation = CalculateProductionTime.findFastestMachine(workstations);

        assertNotNull(fastestWorkstation);
        assertEquals(10, fastestWorkstation.getTime());
    }

    @org.junit.jupiter.api.Test
    void calculateTotalProductionTime() {
    }

    @org.junit.jupiter.api.Test
    void findFastestMachine() {
    }
}