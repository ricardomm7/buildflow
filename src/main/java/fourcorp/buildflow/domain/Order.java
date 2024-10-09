package fourcorp.buildflow.domain;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private String id;
    private List<Product> products;
    private int quantity;
    private Client client;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private List<Machine> preProcessedMachines;

}
