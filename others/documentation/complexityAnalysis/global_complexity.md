# USEI - Complexity Analysis Documentation

## [USEI01](usei01_algs.md)

| Method           | Complexity |
|------------------|------------|
| `newItem`        | O(n)       |
| `getByKey`       | O(n)       |
| `searchById`     | O(n²)      |
| `getAllValues`   | O(n)       |

- **Overall Complexity**: **O(n²)**
  > **Reason**: The complexity of `searchById`, which is **O(n²)**, dominates the overall complexity. This is because `searchById` iterates through a nested data structure.

---

## [USEI02](usei02_algs.md)

| Method                       | Complexity |
|------------------------------|------------|
| `runSimulation`              | O(n³)      |
| `processWaitingQueue`        | O(n³)      |
| `getWorkstationsByOperation` | O(n²)      |
| `processProduct`             | O(1)       |

- **Overall Complexity**: **O(n⁶)**
  > **Reason**: The main complexity arises from `processWaitingQueue`, which has **O(n³)** complexity and is called within the `runSimulation` loop. Thus, **O(n³) * O(n³) = O(n⁶)**.

---

## [USEI03](usei03_algs.md)

| Method                     | Complexity |
|----------------------------|------------|
| `printProductionStatistics` | O(1)       |

- **Overall Complexity**: **O(1)**
  > **Reason**: `printProductionStatistics` executes only constant-time operations, making it **O(1)**.

---

## [USEI04](usei04_algs.md)

| Method                     | Complexity |
|----------------------------|------------|
| `printProductionStatistics` | O(n)       |

- **Overall Complexity**: **O(n³)**
  > **Reason**: This analysis includes the surrounding simulation process, where nested loops and data processing contribute to the **O(n³)** complexity.

---

## [USEI05](usei05_algs.md)

- **Overall Complexity**: **O(n log n)**
  > **Reason**: The main complexity factor comes from the sorting operation using the `Collections.sort` tool, which is **O(n log n)**.

---

## [USEI06](usei06_algs.md)

- **Overall Complexity**: **O(n)**
  > **Reason**: This analysis focuses on linear data processing without nested loops, resulting in **O(n)** complexity.

---

## [USEI07](usei07_algs.md)

- **Overall Complexity**: **O(n log n)**
  > **Reason**: Sorting operations dominate this part of the code, leading to **O(n log n)** complexity.

---

## [USEI08](usei08_algs.md)

- **Overall Complexity**: **O(n)**
  > **Reason**: The main operations involve linear processing without nested structures, giving it **O(n)** complexity.

---

This document presents a summary of complexity analyses for the various USEI modules, ensuring a quick and clear understanding of the performance impact of each module.
