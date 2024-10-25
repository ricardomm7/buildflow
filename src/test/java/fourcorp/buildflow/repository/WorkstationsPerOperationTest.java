package fourcorp.buildflow.repository;

class WorkstationsPerOperationTest {
/*
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

/*
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
        xo.runWithoutPriority();
        List<Workstation> urghoer = u.getWorkstationsAscendingByPercentage();

        double asd3 = (ws543.getTotalOperationTime() / ws543.getTotalExecutionTime()) * 100;
        System.out.println(asd3);
        assertEquals(2, urghoer.size(), "Should return 2 machines.");
        assertEquals(80.0, (ws543.getTotalOperationTime() / ws543.getTotalExecutionTime()) * 100, "WS65 should have 80%.");
    }
 */
}