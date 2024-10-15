package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Identifiable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A generic class that manages a map where each key (of type {@code Q}) is associated with a
 * {@code LinkedList} of values (of type {@code T}). This class is designed to store and manipulate
 * elements in a multi-level data structure where the values are grouped by keys.
 *
 * <p>The class is generic and accepts three type parameters:</p>
 * <ul>
 *     <li>{@code T}: Represents the type of elements stored in the linked lists. This type
 *         must implement the {@link Identifiable} interface, which means that every instance
 *         of {@code T} must have an identifier of type {@code ID}.</li>
 *     <li>{@code Q}: Represents the type of the key used in the map. This could be any
 *         type, such as {@code String}, {@code Integer}, or any custom type that you need
 *         for the key like {@link fourcorp.buildflow.domain.PriorityOrder}.</li>
 *     <li>{@code ID}: Represents the type of the identifier used to uniquely identify
 *         elements of type {@code T}. This is the type of the identifier that each
 *         {@code T} implements via {@link Identifiable#getId()}. The identifier can be of
 *         any type, such as {@code String}, {@code Integer}, or any other comparable type.</li>
 * </ul>
 *
 * <p>The {@code MapLinked} class provides methods for adding, retrieving, and removing
 * elements, as well as for searching for an element by its identifier and removing
 * all elements from the map.</p>
 *
 * @param <T>  The type of the objects stored in the {@code LinkedList}, which must implement {@link Identifiable}.
 * @param <Q>  The type of the key used to associate a group of {@code T} objects.
 * @param <ID> The type of the unique identifier used by each {@code T} object.
 */
public class MapLinked<T extends Identifiable<ID>, Q, ID> {
    private final Map<Q, LinkedList<T>> line = new HashMap<>();

    /**
     * Adds a new item of type {@code T} to the map under the specified key {@code Q}.
     * If the key does not exist, a new entry is created.
     *
     * @param value The item to be added. Must not be {@code null}.
     * @param key   The key under which the item should be stored. If the key does not exist, a new entry is created.
     * @throws IllegalArgumentException if {@code value} is {@code null}.
     */
    public void newItem(T value, Q key) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        for (Q existingKey : line.keySet()) {
            if (existingKey.equals(key)) {
                List<T> valueList = line.get(existingKey);
                valueList.add(value);
                return;
            }
        }
        LinkedList<T> valueList = new LinkedList<>();
        valueList.add(value);
        line.put(key, valueList);
    }

    /**
     * Retrieves all items associated with the given key {@code Q}. If no items are associated with
     * the key, an empty list is returned.
     *
     * @param key The key for which to retrieve the associated items.
     * @return A {@code LinkedList} of items of type {@code T} associated with the specified key.
     */
    public LinkedList<T> getByKey(Q key) {
        for (Q existingKey : line.keySet()) {
            if (existingKey.equals(key)) {
                return new LinkedList<>(line.get(existingKey));
            }
        }
        return new LinkedList<>();
    }

    /**
     * Removes a specific item of type {@code T} from the list associated with the given key {@code Q}.
     * If the list does not contain the item or if the key is not present, no action is taken.
     *
     * @param value The item to be removed from the list.
     * @param key   The key for which to remove the item.
     */
    public void remove(T value, Q key) {
        LinkedList<T> orderList = line.get(key);
        if (orderList != null) {
            orderList.remove(value);
        }
    }

    /**
     * Searches for an item in the entire map by its unique identifier of type {@code ID}.
     * It iterates over all the lists in the map and returns the first item that matches the given identifier.
     *
     * @param id The unique identifier of the item to search for.
     * @return The item of type {@code T} if found, or {@code null} if no item with the given identifier exists.
     */
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

    /**
     * Removes all entries from the map, effectively clearing the entire structure.
     * After calling this method, the map will be empty.
     */
    public void removeAll() {
        line.clear();
    }

    /**
     * Returns a list of all the keys present in the map.
     *
     * @return A list of keys of type {@code Q} present in the map.
     */
    public List<Q> getKeys() {
        return new LinkedList<>(line.keySet());
    }

    public boolean isEmpty() {
        return line.isEmpty();
    }
}
