# USEI09 - Build the complete production tree of a product

## Requirements Engineering

### A) User Story Description

Import data from two CSV's and create a tree with the nodes corresponding to operations or materials. 

### B) Customer Specifications and Clarifications

**From the specifications document:**

> Build the complete production tree of a product As a user, I want to
import data from a CSV file containing the operations, components, materials
and quantities, for a product, to create a production tree. In this production
tree, each node represents either an operation or a material associated with a
specific stage in the production process. Notice that each subcomponent has its
own tree and a respective entry in the boo.csv file, instead of the materials that
are always leafs in the production tree. The figure 6 should be used as a reference.
For a more complex product like a bicycle, where each part (e.g., the frame,
wheels, brakes, chain, etc.) has its own assembly or manufacturing process, you
could have multiple sub-trees within the main production tree.

**From the client clarifications:**

> **Question:** Ah?
>
> **Answer:** No.


### C) Acceptance Criteria

* **AC1:** The materials should be inserted as materials in the tree.
* **AC2:** The operations should be inserted as operations in the tree.

### D) Found out Dependencies

* The CSV's format.

### E) Input and Output Data

**Input Data:**

* Typed data:
    * none

* Selected data:
    * none

**Output Data:**

* (In)Success of the operation

### F) Other Relevant Remarks

* None.