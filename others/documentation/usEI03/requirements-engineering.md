# USEI03 - Calculate the total production time for the items.

## Requirements Engineering

### A) User Story Description

> Calculate the total production time for the items.

### B) Customer Specifications and Clarifications

**From the specifications document:**

> The system should be able to calculate the total production time for each item, taking into account the sequence of 
> operations that must be performed. Each operation is associated with a specific machine or station, and the time to 
> execute the operation can vary depending on the machine used.

**From the client clarifications:**

> **Question:** Should the output be the time that passes since the beggining of production until the end of the last operation of the last item, or the time that each item takes since its first operation to it's last to be processed?
>
> **Answer:** The time diference between the start instant and the end instant of the simulation.

> **Question:** Regarding USEI03 - Calculate the total production time for the items, the objetive is to calculate individually the production time for each item in the csv file, or the total operation time of all items, or the time between the start of the first machine and the end of the last one?
> 
> **Answer:** the total operation time of all items but the time between the start of the first machine and the end of the last one would be nice to have.

> **Question:** Calculate the total production time for the items, the objetive is to calculate individually the production time for each item in the csv file, or the total operation time of all items, or the time between the start of the first machine and the end of the last one?
> 
> **Answer:** the total operation time of all items but the time between the start of the first machine and the end of the last one would be nice to have.


### C) Acceptance Criteria

* **AC1:** The system must calculate the total production time for an item based on the sum of all required operations’ durations.
* **AC2:** The calculation must consider the sequential order of operations specified for each item.
* **AC3:** The result should be displayed clearly, showing the total time for each item.


### D) Found out Dependencies

* This functionality depends on having accurate data about the operations required for each item and the time taken for each operation.

* It relies on data from the Bill of Operations (BOO) for the correct sequence and duration of operations.

### E) Input and Output Data

**Input Data:**

* Typed data:
    * csv file names

* Selected data:
    * none

**Output Data:**

* The total production time for the item (in minutes or hours, as applicable).

### F) Other Relevant Remarks

* Implement appropriate permissions and access controls to restrict the ability to register a skill to authorized HRM users only.