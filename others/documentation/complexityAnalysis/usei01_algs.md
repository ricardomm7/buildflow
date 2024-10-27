# USEI01 - Class MapLinked (generic)

## New item
```java
public void newItem(T value, Q key) {
    if (value == null) {
        throw new IllegalArgumentException("Value cannot be null");
    }
    for (Q existingKey : line.keySet()) { // O(n)
        if (existingKey.equals(key)) {
            List<T> valueList = line.get(existingKey);
            valueList.add(value); // O(n) * O(1) = O(n)
            return;
        }
    }
    LinkedList<T> valueList = new LinkedList<>();
    valueList.add(value); // O(1)
    line.put(key, valueList); // O(1)
}
```

> What this algorithm do: This algorithm adds a new item to the MapLinked structure.

> Result of the complexity analysis: **O(n)**

## Get By Key

```java
public LinkedList<T> getByKey(Q key) {
    for (Q existingKey : line.keySet()) { // O(n)
        if (existingKey.equals(key)) {
            return new LinkedList<>(line.get(existingKey)); // O(n) * O(1) = O(n)
        }
    }
    return new LinkedList<>();
}
```

> What this algorithm do: This algorithm returns a List containing all the values of a key.

> Result of the complexity analysis: **O(n)**

## Search By ID

```java
public T searchById(ID id) {
    for (List<T> items : line.values()) { // O(n)
        for (T item : items) { // O(n) * O(n) = O(n^2)
            if (item.getId().equals(id)) {
                return item; // O(n^2) * O(1) = O(n^2)
            }
        }
    }
    return null;
}
```

> What this algorithm do: This algorithm returns a T (value) findable by its ID field (implements Identifiable).

> Result of the complexity analysis: **O(n^2)**

## Get All Values

```java
public LinkedList<T> getAllValues() {
    LinkedList<T> allValues = new LinkedList<>();
    for (LinkedList<T> items : line.values()) { //  O(n)
        allValues.addAll(items); // O(n) * 0(1) = O(n)
    }
    return allValues;
}
```

> What this algorithm do: This algorithm returns the T (values) of the all structure.

> Result of the complexity analysis: **O(n)**