# USBD08 - As a Plant Floor Manager, I want to know the different operations the factory supports.

## Requirements Engineering

### A) User Story Description

As a Plant Floor Manager, I want to have a comprehensive list of all the operations that the factory supports so that I can better manage and allocate resources efficiently.
### B) Customer Specifications and Clarifications

**From the specifications document:**

> The Plant Floor Manager needs access to the list of all possible operations the factory can perform, as this is crucial for resource allocation and production planning.

**From the client clarifications:**

> **Question:** Considere o seguinte excerto do ficheiro excel:
9847	A4588	Press 03	220-630t precision cold forging press	 
9855	A4588	Press 04	160-1000t precison cold forging press
sendo A4588 o id do workstation type e 9847 e 9855 duas workstations diferentes. Claramente podemos concluir que duas workstations diferentes podem conter/ser o mesmo tipo de workstation. Não está clara a relação entre workstation type e workstation uma vez que o enunciado também não clarifica esta relação. Já li as outras respostas no fórum e também não encontrei nada que clarifique a 100%. É uma workstation que contém vários tipos de workstations ou cada workstation type contém diferentes workstations?
Em qualquer um dos casos a relação com a operação terá que ser com a workstation type uma vez que no ficheiro cada operação está relacionada a uma ou mais workstationType, o que implica que uma operação é feita numa workstationType. Porém, voltando ao excerto de excel, é uma workstationType que contém diversas workstations, dando entender que as operações são feitas pelas workstations.
Peço que me clarifique esta dúdivda pertinente.
>
> **Answer:** Há uma relação 1:N entre WorkstationType e Workstation. É o normal quando nos referimos a um "tipo" de qualquer coisa. Pode haver várias workstations do mesmo tipo.
Também é óbvio que as operações são realizadas por workstations, não WorkstationType. O contrário não fazia qualquer sentido, não é verdade?
Mas não fazia qualquer sentido alguém dizer que uma operação da BOO seria realizada numa Workstation em particular, porque estaria a fazer-se escalonamento na fase de projeto do produto. Uma operação está relacionada com uma ou mais entidades do tipo WorkstationType e, aquando do escalonamento da produção, faz-se a atribuição a uma workstation em concreto. Mas o escalonamento da produção nada tem a ver com o trabalho de BDDAD do sprint 1.



### C) Acceptance Criteria

* **AC1:** The system should display a list of all production-related operations supported by the factory.
* **AC2:** Each operation should have a description and a reference to the associated workstations or machinery.
* **AC3:** The list should be easily accessible and filterable based on operation type or workstation.
* **AC4:** Data must be imported from the legacy system to ensure completeness.

### D) Found out Dependencies

* None.

### E) Input and Output Data

**Input Data:**

* Typed data:
    * smthng

* Selected data:
    * smthng

**Output Data:**

* (In)Success of the operation

### F) Other Relevant Remarks

* Implement appropriate permissions and access controls to restrict the ability to register a skill to authorized HRM users only.