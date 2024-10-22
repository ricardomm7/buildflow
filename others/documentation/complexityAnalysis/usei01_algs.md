# USEI01 - Class MapLinked (generic)

## New item

```java
public void newItem(T value, Q key) {
    if (value == null) {
        throw new IllegalArgumentException("Value cannot be null");
    }
    for (Q existingKey : line.keySet()) { //O(k)
        if (existingKey.equals(key)) {
            List<T> valueList = line.get(existingKey);
            valueList.add(value); //O(1)
            return;
        }
    }
    LinkedList<T> valueList = new LinkedList<>();
    valueList.add(value); //O(1)
    line.put(key, valueList); //O(1)
}
```

> What this algorithm do: This algorithm adds a new item to the MapLinked structure.

> Result of the complexity analysis: **O(k)** (where k is the number of keys)

## Get By Key

```java
public LinkedList<T> getByKey(Q key) {
    for (Q existingKey : line.keySet()) { //O(k)
        if (existingKey.equals(key)) {
            return new LinkedList<>(line.get(existingKey)); //O(1)
        }
    }
    return new LinkedList<>();
}
```

> What this algorithm do: This algorithm returns a List containing all the values of a key.

> Result of the complexity analysis: **O(k)** (where k is the number of keys)

## Search By ID

```java
public T searchById(ID id) {
    for (List<T> items : line.values()) { //O(k)
        for (T item : items) { //O(kn)
            if (item.getId().equals(id)) {
                return item; //O(1)
            }
        }
    }
    return null;
}
```

> What this algorithm do: This algorithm returns a T (value) findable by its ID field (implements Identifiable).

> Result of the complexity analysis: **O(kn)** (where k is the number of keys and n the number of values)

## Get All Values

```java
public LinkedList<T> getAllValues() {
    LinkedList<T> allValues = new LinkedList<>();
    for (LinkedList<T> items : line.values()) { //O(k)
        allValues.addAll(items); //O(k)
    }
    return allValues;
}
```

> What this algorithm do: This algorithm returns the T (values) of the all structure.

> Result of the complexity analysis: **O(k)** (where k is the number of keys)