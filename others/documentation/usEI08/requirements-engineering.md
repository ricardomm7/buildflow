# USEI08 - Consider an improvement to the simulator developed in USEI02 that takes into account a processing order based on priority

## Requirements Engineering

### A) User Story Description

As a Product Owner, I want to enhance the existing simulator to process items based on their priority (high, normal, low), so that critical items are processed more quickly and efficiently.


### B) Customer Specifications and Clarifications

**From the specifications document:**

> Items should be processed in order of their priority, with high-priority items being assigned to machines first. The system should still assign items to the fastest available machine that can perform the required operation.

**From the client clarifications:**

> **Question:** Ah?
>
> **Answer:** No.


### C) Acceptance Criteria

* **AC1:** The simulator must sort items in the queue based on their priority, with high-priority items being processed first, followed by normal, and then low-priority items.
* **AC2:** Within each priority level, items should be assigned to machines based on their entry order (first-come, first-served).
* **AC3:**  The system should still prioritize assigning items to the fastest available machine that can perform the required operation.
* **AC4:**

### D) Found out Dependencies

* Depends on the base simulator developed in USEI02

### E) Input and Output Data

**Input Data:**

* Data from existing simulator:
    * Items from artigos.csv, which now include a priority field (high, normal, low). 
    * Machine details from maquinas.csv.

* Selected data:
    * Parameters for simulation, such as priority-based processing.

**Output Data:**
* A log of the simulation with priority-based item processing:
  * List of items processed in priority order 
  * Assignment of items to machines 
  * Processing times for each item and operation 
  * Queue statuses before and after processing, sorted by priority 
  * Machine utilization and total production time, reflecting priority-based scheduling. 

* Statistical measures, including:
  * Total production time for all items 
  * Machine utilization percentages 
  * Waiting times for each priority level (high, normal, low).

* (In)Success of the operation

### F) Other Relevant Remarks

* Implement appropriate permissions and access controls to restrict the ability to register a skill to authorized HRM users only.