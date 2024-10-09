package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Order;

import java.util.HashMap;
import java.util.Map;

public class ProductionLine {

    private Map<Integer, Order> productionLine = new HashMap<>();

    public void newOrder(Order order, int priority) {
        productionLine.put(priority, order);
    }

    public void cancelOrder(String orderId) {
        // To be completed
    }

    public Order searchOrder(String id) {
        for (Order order : productionLine.values()) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }
}
