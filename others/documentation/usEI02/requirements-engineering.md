# USEI02 -  Implement a simulator that processes all the items

## Requirements Engineering

### A) User Story Description

As a Product Owner, I want to implement a simulator that processes all items according to specified criteria so that we can efficiently manage the production process and optimize resource allocation.
### B) Customer Specifications and Clarifications

**From the specifications document:**

> The simulator should process items based on a preliminary queue for each operation and assign items to machines based on processing availability and speed.

**From the client clarifications:**

> **Question:** Can an item have the same operation applied to it more than once? Or can we assume that any operation only shows once in the list?
>
> **Answer:** You can asume, at least for now, that an operation applies to an item just once.

> **Question:** Is it possible for a product to have multiple Bill Of Materials? For example, a product that has a variant with the size small and another with the size big will require different quantities of materials, thus having more than one Bill Of Materials for a single product.
> 
> **Answer:** no; different BOMs implies different articles/products.
Although products from same family or variants may share almost similar BOMs or BOOs.


### C) Acceptance Criteria

* **AC1:** The simulator should create a preliminary queue for each operation,
  containing all the items, whose next operation (according to the sequential
  production process) is that of the specified queue.
* **AC2:**  The items in the queue should be assigned based on the processing
  availability to the available machine capable of performing the required operation faster, in the order of their entry into the queue.
* **AC03:** The simulator must handle edge cases, such as no available machines or multiple items arriving simultaneously.
* **AC04:** The simulation process must log detailed output, including which items are assigned to which machines and their processing times.

### D) Found out Dependencies

* Requires proper integration with the data structures defined in USEI01 for storing items and machines.


### E) Input and Output Data

**Input Data:**

* Data imported from:
    * artigos.csv containing item details (e.g., id_item, priority, operations). 
    * maquinas.csv containing machine details (e.g., id_machine, operation name, processing time).

* Selected data:
    * Parameters for the simulation, such as the number of items to process and specific operational constraints.

**Output Data:**
    * A detailed log of the simulation results, including:
    * The assignment of items to machines
    * Processing times for each item and operation
    * Queue statuses before and after processing


* (In)Success of the operation

### F) Other Relevant Remarks

* Implement appropriate permissions and access controls to restrict the ability to register a skill to authorized HRM users only.