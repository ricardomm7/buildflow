package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Identifiable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapLinked<T extends Identifiable<ID>, Q, ID> {
    private final Map<Q, LinkedList<T>> line = new HashMap<>();

    public void newItem(T value, Q key) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        List<T> valueList = line.computeIfAbsent(key, k -> new LinkedList<>());
        valueList.add(value);
    }

    public List<T> getByKey(Q key) {
        return new LinkedList<>(line.getOrDefault(key, new LinkedList<>()));
    }

    public void remove(T value, Q key) {
        LinkedList<T> orderList = line.get(key);
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

    public void removeAll() {
        line.clear();
    }
}

