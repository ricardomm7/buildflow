# USDB07 - know the materials/components to be ordered to fulfill a given production order

## Requirements Engineering

### A) User Story Description

> As a Production Manager, I want to know the materials/components to be ordered to fulfill a given production order, 
> including the quantity of each material/component.

### B) Customer Specifications and Clarifications

**From the specifications document:**

> The system should allow the Production Manager to view the materials and components associated with a production order, 
  based on the product structure (BOM - Bill of Materials) defined during Production Planning. The displayed quantities 
  must reflect the specific needs of the production order.

**From the client clarifications:**

> **Question:** Ah?
>
> **Answer:** No.


### C) Acceptance Criteria

* **AC1:** The system must allow the Production Manager to view the list of materials/components required for a production order.
* **AC2:** The system must automatically calculate the quantity of each material/component based on the production
  order.
* **AC3:** The interface must be simple and display the information clearly and organized, showing material, component,
  and respective quantities.


### D) Found out Dependencies

* The functionality depends on having accurate data in the BOM (Bill of Materials) and the associated production order.

* It relies on integration with the Production Planning module to extract production orders and the corresponding quantity
  of the product to be produced.

### E) Input and Output Data

**Input Data:**

* Typed data:
    * none

* Selected data:
    * none

**Output Data:**

* Material/component name.

* Quantity to be ordered

### F) Other Relevant Remarks

* Implement appropriate permissions and access controls to restrict the ability to register a skill to authorized HRM users only.