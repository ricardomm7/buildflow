package fourcorp.buildflow.repository;

import fourcorp.buildflow.application.Reader;
import fourcorp.buildflow.domain.Machine;
import fourcorp.buildflow.domain.Order;
import fourcorp.buildflow.domain.Product;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProductionLine {
    private final Map<Integer, LinkedList<Order>> productionLine = new HashMap<>(); // Correct type

    public void newOrder(Order order, Integer priority) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        List<Order> orderList = productionLine.computeIfAbsent(priority, k -> new LinkedList<>());
        orderList.add(order);
    }

    public void removeOrderFromPL(String orderId) {
        Order orderToCancel = searchOrder(orderId);
        if (orderToCancel == null) {
            throw new IllegalArgumentException("Order with ID " + orderId + " does not exist.");
        }
        for (Map.Entry<Integer, LinkedList<Order>> entry : productionLine.entrySet()) {
            List<Order> orders = entry.getValue();
            if (orders.remove(orderToCancel)) {
                break;
            }
        }
    }

    public Order searchOrder(String id) {
        for (List<Order> orders : productionLine.values()) {
            for (Order order : orders) {
                if (order.getId().equals(id)) {
                    return order;
                }
            }
        }
        return null;
    }

    public LinkedList<Order> getOrdersByPriority(Integer priority) {
        return new LinkedList<>(productionLine.get(priority));
    }

    public void markOrderAsCompleted(String orderID) {
        Order o1 = searchOrder(orderID);
        o1.setReady(true);
        o1.setDeliveryDate(LocalDate.now());
        removeOrderFromPL(orderID);
    }
}
