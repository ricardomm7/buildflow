package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.domain.Workstation;
import fourcorp.buildflow.repository.WorkstationsPerOperation;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
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

        List<Product> a = new ArrayList<>();
        a.add(product1);
        a.add(product2);
        a.add(product3);
        a.add(product4);
        a.add(product5);
        a.add(product6);
        a.add(product7);
        a.add(product8);
        a.add(product9);
        a.add(product10);

        Simulator s = new Simulator();
        s.run(a);

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
    void processItems() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Product product1 = new Product("P001", new LinkedList<>(Arrays.asList(new Operation("Cutting"), new Operation("Welding"))));
        Product product2 = new Product("P002", new LinkedList<>(Arrays.asList(new Operation("Assembling"), new Operation("Painting"))));

        List<Product> products = Arrays.asList(product1, product2);

        WorkstationsPerOperation w = new WorkstationsPerOperation();

        Simulator simulator = new Simulator(w);

        Workstation ws1 = new Workstation("WS1", 10);
        Workstation ws2 = new Workstation("WS2", 8);
        Workstation ws3 = new Workstation("WS1", 10);
        Workstation ws4 = new Workstation("WS2", 8);

        w.create(ws1, new Operation("Cutting"));
        w.create(ws2, new Operation("Assembling"));
        w.create(ws3, new Operation("Welding"));
        w.create(ws4, new Operation("Painting"));

        simulator.run(products);

        String output = outContent.toString();

        assertTrue(output.contains("Current operation: Cutting"), "The cutting operation should have been processed.");
        assertTrue(output.contains("The best machine: WS1"), "The WS1 machine should have been used for the cutting operation.");

        System.setOut(System.out);
    }
}