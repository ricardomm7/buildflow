package fourcorp.buildflow.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Order implements Identifiable<String> {
    private String id;
    private List<Product> products;
    private int quantity;
    private Client client;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private List<Workstation> preProcessedWorkstations;
    private boolean ready;

    public Order(List<Product> products, int quantity, Client client, LocalDate orderDate, LocalDate deliveryDate) {
        this.id = UUID.randomUUID().toString();
        this.products = products;
        this.quantity = quantity;
        this.client = client;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<Workstation> getPreProcessedMachines() {
        return preProcessedWorkstations;
    }

    public void setPreProcessedMachines(List<Workstation> preProcessedWorkstations) {
        this.preProcessedWorkstations = preProcessedWorkstations;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
