# USEI07 - Produce a listing representing the flow dependency between machines

## Requirements Engineering

### A) User Story Description

> The system must produce a listing that represents the flow dependency between machines used during the production 
> process. The listing should show how often each machine transfers items to another machine, sorted in descending order 
> based on the number of processed items.

### B) Customer Specifications and Clarifications

**From the specifications document:**

> The system needs to track the flow of items between machines. For each machine, it should display the subsequent 
> machines that process the items and how many times that transition occurs. The listing must be sorted in descending 
> order by the number of processed items, highlighting the dependencies between machines.

**From the client clarifications:**

> **Question:** Ah?
>
> **Answer:** No.


### C) Acceptance Criteria

* **AC1:** The system must track the transitions between machines for all items processed, showing the flow from one
  machine to the next.
* **AC2:** The listing should display, for each machine, the machines it passed items to, along with the number of items
  processed in that transition.
* **AC3:** The listing must be sorted in descending order of processed items for each machine's transitions.


### D) Found out Dependencies

* This functionality depends on accurate data about the processing sequence of each item, including which machines handled 
  the item and in what order.

* Relies on machine data and item processing sequences being correctly tracked during the production process.

### E) Input and Output Data

**Input Data:**

* Typed data:
    * csv file's name

* Selected data:
    * none

**Output Data:**

* The machines that received items from it.

* The number of items transferred between each machine.

* Sorted by the number of processed items (in descending order).

### F) Other Relevant Remarks

* Implement appropriate permissions and access controls to restrict the ability to register a skill to authorized HRM users only.