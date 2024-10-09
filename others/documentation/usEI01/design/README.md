# USEI01 - Define the adequate data structures to store the information imported from the files

## Design - User Story Realization 

### A) Rationale

| Question: Which class is responsible for... | Answer                | Justification                                                                                                                                  |
|:--------------------------------------------|:----------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------|
| 	... interacting with the actor?            | **ProductionLine**    | This class represents the service responsible for handling orders and their priorities, which includes adding, searching, and removing orders. |
| ... creating the order data structure?      | **Order**             | This class encapsulates the necessary information about each order, such as the ID, readiness, and delivery date.                              |
| ... managing the priority structure?        | **Map**               | The Map data structure is used to store and group orders by their priority. Each priority is a key with a list of orders as its value.         |
| ... storing orders by priority?             | **LinkedList<Order>** | A linked list of orders is used for efficient insertion and removal of orders based on their priority.                                         |

### Systematization

According to the rationale taken, the conceptual classes promoted to software classes are:

* **Order**: Represents an order in the system. Contains attributes like `id`, `deliveryDate`, and `ready`.
* **ProductionLine**: Manages a collection of orders grouped by priority, using a `Map<Integer, LinkedList<Order>>` to store orders by their priority level.

Other software classes (i.e. Pure Fabrication) identified:

* **Map**: A pure fabrication to store orders based on their priority.
* **LinkedList<Order>**: A pure fabrication used to store and manage multiple orders efficiently by allowing operations like adding and removing orders.

## B) Sequence Diagram (SD)

This diagram shows the full sequence of interactions between the classes involved in the realization of this user story.

![Sequence Diagram](svg/sequence-diagram.svg)

## C) Class Diagram (CD)

![Class Diagram](svg/class-diagram.svg)