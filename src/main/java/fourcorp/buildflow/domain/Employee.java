package fourcorp.buildflow.domain;

import java.time.LocalDate;

/**
 * Represents an employee who is a type of person in the system.
 * In addition to the attributes inherited from the Person class, the Employee class includes a birth date and salary.
 */
public class Employee extends Person {
    private LocalDate birthDate;
    private double salary;

    /**
     * Constructs an Employee object with the specified details.
     *
     * @param id          the unique identifier for the employee
     * @param nif         the NIF (tax identification number) of the employee
     * @param name        the name of the employee
     * @param address     the street address of the employee
     * @param city        the city where the employee is located
     * @param zipCode     the zip code for the employee's address
     * @param phoneNumber the phone number of the employee
     * @param birthDate   the birth date of the employee
     * @param salary      the salary of the employee
     */
    public Employee(String id, int nif, String name, String address, String city, String zipCode, int phoneNumber, LocalDate birthDate, double salary) {
        super(id, nif, name, address, city, zipCode, phoneNumber);
        this.birthDate = birthDate;
        this.salary = salary;
    }

    /**
     * Gets the birth date of the employee.
     *
     * @return the birth date
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date of the employee.
     *
     * @param birthDate the birth date to set
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets the salary of the employee.
     *
     * @return the salary
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Sets the salary of the employee.
     *
     * @param salary the salary to set
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }
}
