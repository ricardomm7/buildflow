package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Identifiable;
import fourcorp.buildflow.domain.PriorityOrder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PriorityLine<T extends Identifiable<ID>, ID> {
    private final Map<PriorityOrder, LinkedList<T>> line = new HashMap<>();

    public void newItem(T value, PriorityOrder priority) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        List<T> valueList = line.computeIfAbsent(priority, k -> new LinkedList<>());
        valueList.add(value);
    }

    public List<T> getByPriority(PriorityOrder priority) {
        return new LinkedList<>(line.getOrDefault(priority, new LinkedList<>()));
    }

    public void remove(T value, PriorityOrder priority) {
        LinkedList<T> orderList = line.get(priority);
        if (orderList != null) {
            orderList.remove(value);
        }
    }

    public T searchById(ID id) {
        for (List<T> items : line.values()) {
            for (T item : items) {
                if (item.getId().equals(id)) {
                    return item;
                }
            }
        }
        return null;
    }
}

