package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Identifiable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PriorityLine<T extends Identifiable<ID>, ID, P> {
    private final Map<P, LinkedList<T>> line = new HashMap<>();

    public void newItem(T value, P priority) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        List<T> valueList = line.computeIfAbsent(priority, k -> new LinkedList<>());
        valueList.add(value);
    }

    public List<T> getByPriority(P priority) {
        return new LinkedList<>(line.getOrDefault(priority, new LinkedList<>()));
    }

    public void remove(T value, P priority) {
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

