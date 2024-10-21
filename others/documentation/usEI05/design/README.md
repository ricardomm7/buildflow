# USEI05 - Present a list of machines with total time of operation, and percentages relative to the operation time and total execution time

## Design - User Story Realization 

### A) Rationale

| Question: Which class is responsible for...            | Answer                   | Justification                                                                                                                |
|:-------------------------------------------------------|:-------------------------|:-----------------------------------------------------------------------------------------------------------------------------|
| 	showing the statistical dada?                         | Simulator                | This class is internally related to the functionality. So it doesn't make sense to assign this functionality to another one. |
| get the Workstations in ascending order by percentage? | WorkstationsPerOperation | This class stores all the workstations, so it wouldn't make sense to assign this role to another class.                      |

### Systematization ##

According to the taken rationale, the conceptual classes promoted to software classes are: 

* **Simulator**: The Simulator class manages the entire workflow of simulating operations and product processing.
* **WorkstationsPerOperation**: The WorkstationsPerOperation class is responsible for finding the best machine for a given operation.

Other software classes (i.e. Pure Fabrication) identified: 

* **MapLinked**: The MapLinked class is responsible for maintaining and organizing the operation queues.

## B) Sequence Diagram (SD)

This diagram shows the full sequence of interactions between the classes involved in the realization of this user story.

![Sequence Diagram](svg/sequence-diagram.svg)

## C) Class Diagram (CD)

![Class Diagram](svg/class-diagram.svg)