package fourcorp.buildflow.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Represents an order in the system, containing information about the products,
 * quantity, client, order date, delivery date, pre-processed workstations, and whether the order is ready for delivery.
 * This class implements the Identifiable interface, using a unique identifier (UUID) for the order.
 */
public class Order implements Identifiable<String> {
    private String id;
    private List<Product> products;
    private int quantity;
    private Client client;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private List<Workstation> preProcessedWorkstations;
    private boolean ready;

    /**
     * Constructs an Order with the specified products, quantity, client, order date, and delivery date.
     * A unique identifier (UUID) is automatically generated for the order.
     *
     * @param products the list of products in the order
     * @param quantity the total quantity of products in the order
     * @param client the client placing the order
     * @param orderDate the date the order was placed
     * @param deliveryDate the expected delivery date for the order
     */
    public Order(List<Product> products, int quantity, Client client, LocalDate orderDate, LocalDate deliveryDate) {
        this.id = UUID.randomUUID().toString();
        this.products = products;
        this.quantity = quantity;
        this.client = client;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
    }

    /**
     * Gets the unique identifier for the order.
     *
     * @return the order ID
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Gets the list of products in the order.
     *
     * @return the list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Sets the list of products in the order.
     *
     * @param products the list of products to set
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Gets the quantity of products in the order.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of products in the order.
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the client who placed the order.
     *
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Sets the client for the order.
     *
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Gets the order date.
     *
     * @return the order date
     */
    public LocalDate getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the order date.
     *
     * @param orderDate the order date to set
     */
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Gets the expected delivery date for the order.
     *
     * @return the delivery date
     */
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Sets the expected delivery date for the order.
     *
     * @param deliveryDate the delivery date to set
     */
    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * Gets the list of workstations that pre-processed the order.
     *
     * @return the list of pre-processed workstations
     */
    public List<Workstation> getPreProcessedMachines() {
        return preProcessedWorkstations;
    }

    /**
     * Sets the list of workstations that pre-processed the order.
     *
     * @param preProcessedWorkstations the list of pre-processed workstations to set
     */
    public void setPreProcessedMachines(List<Workstation> preProcessedWorkstations) {
        this.preProcessedWorkstations = preProcessedWorkstations;
    }

    /**
     * Checks if the order is ready for delivery.
     *
     * @return true if the order is ready, false otherwise
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Sets the readiness status of the order.
     *
     * @param ready true if the order is ready, false otherwise
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
