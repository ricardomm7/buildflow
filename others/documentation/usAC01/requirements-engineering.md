# USAC01 - Implement the `int extract_data` function in Assembly.

## Requirements Engineering

### A) User Story Description

Implement the `int extract_data(char* str, char* token, char*
unit, int* value)` function.
It receives as input two strings, str and token, and for output, it receives two
pointers, one for a string, unit and another for an integer, value. The content
of the str is formatted as follows:
`TOKEN &unit:xxxxxxx &value:xx #TOKEN &unit:xxxxxxxx&value:xx`, where
TOKEN could be TEMP or HUM for temperature or humidity, respectively.
This function extracts value and unit data from str according to the token. It
should return 1 if it succeeds, 0 otherwise (in this case, it should set value to
zero and unit to empty string, "" ).
### B) Customer Specifications and Clarifications

**From the specifications document:**

> We need to create an Assembly function to format a String in a defined style.

**From the client clarifications:**

> **Question:** Ah?
>
> **Answer:** No.


### C) Acceptance Criteria

* **AC1:** The content of the str is formatted as follows:
  ```TOKEN &unit:xxxxxxx &value:xx #TOKEN &unit:xxxxxxxx&value:xx```, where TOKEN could be TEMP or HUM for temperature or humidity, respectively.

### D) Found out Dependencies

* None.

### E) Input and Output Data

**Input Data:**

* Typed data:
    * None

* Selected data:
    * None

**Output Data:**

* (In)Success of the operation

### F) Other Relevant Remarks

* None