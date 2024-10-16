package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.WorkstationsPerOperation;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulatorTest {

    @Test
    void createOperationQueues() {
        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Welding"), new Operation("Polishing"))));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Painting"), new Operation("Packaging"))));
        Product product3 = new Product("P003", new LinkedList<>(Arrays.asList(new Operation("Forging"), new Operation("Drilling"), new Operation("Inspecting"))));
        Product product4 = new Product("P004", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Sanding"), new Operation("Coating"))));
        Product product5 = new Product("P005", new LinkedList<>(Arrays.asList(new Operation("Molding"), new Operation("Trimming"), new Operation("Polishing"))));
        Product product6 = new Product("P006", new LinkedList<>(Arrays.asList(new Operation("Soldering"), new Operation("Welding"), new Operation("Testing"))));
        Product product7 = new Product("P007", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Drilling"), new Operation("Inspecting"), new Operation("Assembling"))));
        Product product8 = new Product("P008", new LinkedList<>(Arrays.asList(new Operation("Casting"), new Operation("Machining"), new Operation("Painting"))));
        Product product9 = new Product("P009", new LinkedList<>(Arrays.asList(new Operation("Stamping"), new Operation("Welding"), new Operation("Inspecting"))));
        Product product10 = new Product("P010", new LinkedList<>(Arrays.asList(new Operation("Molding"), new Operation("Drilling"), new Operation("Packaging"))));

        WorkstationsPerOperation w = new WorkstationsPerOperation();
        ProductPriorityLine o = new ProductPriorityLine();

        o.create(product1, PriorityOrder.LOW);
        o.create(product2, PriorityOrder.LOW);
        o.create(product3, PriorityOrder.LOW);
        o.create(product4, PriorityOrder.LOW);
        o.create(product5, PriorityOrder.LOW);
        o.create(product6, PriorityOrder.LOW);
        o.create(product7, PriorityOrder.LOW);
        o.create(product8, PriorityOrder.LOW);
        o.create(product9, PriorityOrder.LOW);
        o.create(product10, PriorityOrder.LOW);

        Simulator s = new Simulator(w, o);
        s.runWithoutPriority();

        List<Operation> operations1 = s.getOperationQueues().getByKey(product1);
        List<Operation> operations2 = s.getOperationQueues().getByKey(product2);
        List<Operation> operations3 = s.getOperationQueues().getByKey(product3);
        List<Operation> operations4 = s.getOperationQueues().getByKey(product4);
        List<Operation> operations5 = s.getOperationQueues().getByKey(product5);
        List<Operation> operations6 = s.getOperationQueues().getByKey(product6);
        List<Operation> operations7 = s.getOperationQueues().getByKey(product7);
        List<Operation> operations8 = s.getOperationQueues().getByKey(product8);
        List<Operation> operations9 = s.getOperationQueues().getByKey(product9);
        List<Operation> operations10 = s.getOperationQueues().getByKey(product10);

        assertEquals(10, s.getProducts().size(), "The total products size should be 9.");
        assertEquals(3, operations1.size(), "The total products operations should be 3.");
        assertEquals(3, operations2.size(), "The total products operations should be 3.");
        assertEquals(3, operations3.size(), "The total products operations should be 3.");
        assertEquals(3, operations4.size(), "The total products operations should be 3.");
        assertEquals(3, operations5.size(), "The total products operations should be 3.");
        assertEquals(3, operations6.size(), "The total products operations should be 3.");
        assertEquals(4, operations7.size(), "The total products operations should be 4.");
        assertEquals(3, operations8.size(), "The total products operations should be 3.");
        assertEquals(3, operations9.size(), "The total products operations should be 3.");
        assertEquals(3, operations10.size(), "The total products operations should be 3.");
    }

    @Test
    void processItemsWithoutPriority() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Welding"))));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Painting"))));

        WorkstationsPerOperation w = new WorkstationsPerOperation();
        ProductPriorityLine p = new ProductPriorityLine();

        p.create(product1, PriorityOrder.LOW);
        p.create(product2, PriorityOrder.HIGH);

        Simulator simulator = new Simulator(w, p);

        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 8);
        Workstation ws3 = new Workstation("WS1", 10);
        Workstation ws4 = new Workstation("WS2", 8);

        w.create(ws1, new Operation("Cutting"));
        w.create(ws2, new Operation("Assembling"));
        w.create(ws3, new Operation("Welding"));
        w.create(ws4, new Operation("Painting"));

        simulator.runWithoutPriority();

        String output = outContent.toString();

        assertTrue(output.contains("Current operation: Cutting"), "The cutting operation should have been processed.");
        assertTrue(output.contains("The best machine: WS1"), "The WS1 machine should have been used for the cutting operation.");

        System.setOut(System.out);
    }

    @Test
    void processItemsWithPriority() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        WorkstationsPerOperation w = new WorkstationsPerOperation();
        ProductPriorityLine a = new ProductPriorityLine();

        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Welding"))));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Painting"))));
        Product product3 = new Product("P003", new LinkedList<>(Arrays.asList(new Operation("Drilling"), new Operation("Polishing"))));
        Product product4 = new Product("P004", new LinkedList<>(Arrays.asList(new Operation("Grinding"), new Operation("Coating"))));
        Product product5 = new Product("P005", new LinkedList<>(Arrays.asList(new Operation("Forging"), new Operation("Inspection"))));
        Product product6 = new Product("P006", new LinkedList<>(Arrays.asList(new Operation("Casting"), new Operation("Packaging"))));
        Product product7 = new Product("P007", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Bending"), new Operation("Welding"))));
        Product product8 = new Product("P008", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Testing"), new Operation("Painting"))));

        a.create(product1, PriorityOrder.LOW);
        a.create(product2, PriorityOrder.NORMAL);
        a.create(product3, PriorityOrder.HIGH);
        a.create(product4, PriorityOrder.NORMAL);
        a.create(product5, PriorityOrder.LOW);
        a.create(product6, PriorityOrder.HIGH);
        a.create(product7, PriorityOrder.NORMAL);
        a.create(product8, PriorityOrder.LOW);

        Simulator simulator = new Simulator(w, a);

        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 8);
        Workstation ws3 = new Workstation("WS1", 10);
        Workstation ws4 = new Workstation("WS2", 8);
        Workstation ws5 = new Workstation("WS3", 12);
        Workstation ws6 = new Workstation("WS4", 15);
        Workstation ws7 = new Workstation("WS5", 9);
        Workstation ws8 = new Workstation("WS6", 11);
        Workstation ws9 = new Workstation("WS7", 13);
        Workstation ws10 = new Workstation("WS8", 14);
        Workstation ws11 = new Workstation("WS9", 16);
        Workstation ws12 = new Workstation("WS10", 10);
        Workstation ws13 = new Workstation("WS11", 8);
        Workstation ws14 = new Workstation("WS12", 14);
        Workstation ws15 = new Workstation("WS13", 10);


        w.create(ws1, new Operation("Cutting"));
        w.create(ws2, new Operation("Assembling"));
        w.create(ws3, new Operation("Welding"));
        w.create(ws4, new Operation("Painting"));
        w.create(ws5, new Operation("Drilling"));
        w.create(ws6, new Operation("Polishing"));
        w.create(ws7, new Operation("Grinding"));
        w.create(ws8, new Operation("Coating"));
        w.create(ws9, new Operation("Forging"));
        w.create(ws10, new Operation("Inspection"));
        w.create(ws11, new Operation("Casting"));
        w.create(ws12, new Operation("Packaging"));
        w.create(ws13, new Operation("Bending"));
        w.create(ws14, new Operation("Testing"));
        w.create(ws15, new Operation("Painting"));

        simulator.runWithPriority();

        String output = outContent.toString();

        assertTrue(output.contains("Processing product " + product3.getId() + " in machine " + ws5.getId() + " - Estimated time: " + ws5.getTime() + " min"), "The drilling operation should have been processed.");
        assertTrue(output.contains("Processing product " + product6.getId() + " in machine " + ws11.getId() + " - Estimated time: " + ws11.getTime() + " min"), "The casting operation should have been processed.");
        assertTrue(output.contains("Processing product " + product2.getId() + " in machine " + ws2.getId() + " - Estimated time: " + ws2.getTime() + " min"), "The assembling operation should have been processed.");
        assertTrue(output.contains("Processing product " + product2.getId() + " in machine " + ws4.getId() + " - Estimated time: " + ws4.getTime() + " min"), "The painting operation should have been processed.");
        assertTrue(output.contains("Processing product " + product8.getId() + " in machine " + ws2.getId() + " - Estimated time: " + ws2.getTime() + " min"), "The assembling operation should have been processed.");


        System.setOut(System.out);
    }

    @Test
    void calculateTotalProductionTimeTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Welding"))));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Painting"))));

        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 8);
        Workstation ws3 = new Workstation("WS3", 12);
        Workstation ws4 = new Workstation("WS4", 15);

        WorkstationsPerOperation workstations = new WorkstationsPerOperation();
        workstations.create(ws1, new Operation("Cutting"));
        workstations.create(ws2, new Operation("Welding"));
        workstations.create(ws3, new Operation("Assembling"));
        workstations.create(ws4, new Operation("Painting"));

        ProductPriorityLine productLine = new ProductPriorityLine();
        productLine.create(product1, PriorityOrder.LOW);
        productLine.create(product2, PriorityOrder.HIGH);

        Simulator simulator = new Simulator(workstations, productLine);
        simulator.runWithoutPriority();

        double totalProductionTime = simulator.getTotalProductionTime();
        List<Double> productionTimePerProduct = simulator.getProductionTimePerProduct();

        assertEquals(45.0, totalProductionTime, 0.01, "The total production time should be 45 minutes (18 + 27).");
        assertEquals(2, productionTimePerProduct.size(), "There should be production times for 2 products.");
        assertTrue(productionTimePerProduct.contains(18.0), "The production time for product P001 should be 18 minutes (10 + 8).");
        assertTrue(productionTimePerProduct.contains(27.0), "The production time for product P002 should be 27 minutes (12 + 15).");

    }

    @Test
    void CalculateProductionsTimeTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Welding"))));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Painting"))));

        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 8);
        Workstation ws3 = new Workstation("WS3", 12);
        Workstation ws4 = new Workstation("WS4", 15);

        WorkstationsPerOperation workstations = new WorkstationsPerOperation();
        workstations.create(ws1, new Operation("Cutting"));
        workstations.create(ws2, new Operation("Welding"));
        workstations.create(ws3, new Operation("Assembling"));
        workstations.create(ws4, new Operation("Painting"));

        ProductPriorityLine productLine = new ProductPriorityLine();
        productLine.create(product1, PriorityOrder.LOW);
        productLine.create(product2, PriorityOrder.HIGH);

        Simulator simulator = new Simulator(workstations, productLine);
        simulator.runWithoutPriority();


        double totalProductionTime = simulator.getTotalProductionTime();
        List<Double> productionTimePerProduct = simulator.getProductionTimePerProduct();

        assertEquals(45.0, totalProductionTime, 0.01, "The total production time should be 45 minutes (18 + 27).");
        assertEquals(2, productionTimePerProduct.size(), "There should be production times for 2 products.");
        assertTrue(productionTimePerProduct.contains(18.0), "The production time for product P001 should be 18 minutes (10 + 8).");
        assertTrue(productionTimePerProduct.contains(27.0), "The production time for product P002 should be 27 minutes (12 + 15).");
    }


}